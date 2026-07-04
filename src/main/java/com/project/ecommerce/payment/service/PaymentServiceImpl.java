package com.project.ecommerce.payment.service;

import com.project.ecommerce.global.exception.DuplicateResourceException;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderStatus;
import com.project.ecommerce.order.repository.OrderRepository;
import com.project.ecommerce.payment.dto.PaymentResponse;
import com.project.ecommerce.payment.entity.Payment;
import com.project.ecommerce.payment.entity.PaymentMethod;
import com.project.ecommerce.payment.entity.PaymentStatus;
import com.project.ecommerce.payment.mapper.PaymentMapper;
import com.project.ecommerce.payment.repository.PaymentRepository;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.user.repository.UserRepository;
import com.project.ecommerce.user.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentMapper paymentMapper, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentResponse pay(Long orderId, PaymentMethod method) {

        validateDuplicate(orderId);
        Order order = getOrder(orderId);
        validateOrderForPayment(order);

        // 1. create payment (PENDING only)
        Payment payment = createPayment(order, method);
        Payment savedPayment = paymentRepository.save(payment);

        // 2. process payment (simulation layer)
        processPaymentAsync(savedPayment.getId());
        return paymentMapper.toResponse(savedPayment);
    }
    @Async
    public void processPaymentAsync(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()->new EntityNotFoundException("Payment not found"));
        //-----MOCK payment gateway (simulate external gateway)
        try{
            Thread.sleep(2000); // simulate bank delay
            boolean success = processFakeGateway();
            processWebhook(paymentId, success);
        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public PaymentResponse getByOrderId(Long orderId) {
        return paymentMapper.toResponse(paymentRepository.findByOrderId(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Payment not found")));
    }

    @Override
    public void processWebhook(Long paymentId, boolean success) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new EntityNotFoundException("Payment not found"));
        if(payment.getStatus() != PaymentStatus.PENDING) {
            return;
        }
        payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        if(success){
            markOrderPaid(payment.getOrder());
        }
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponse> getMyPayments() {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        List<Payment> payments = paymentRepository.findByOrderUserId(user.getId());
        return payments.stream().map(paymentMapper::toResponse).toList();
    }

    //-------------Private methods

    private void validateDuplicate(Long orderId){
        if(paymentRepository.findByOrderId(orderId).isPresent()){
            throw new DuplicateResourceException("Payment already exists");
        }
    }

    private Payment createPayment(Order order, PaymentMethod method){
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod(method);
        //payment.setStatus(PaymentStatus.PENDING);
        return payment;
    }

    private Order getOrder(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Order not found"));
    }

    private void validateOrderForPayment(Order order){
        if(order.getStatus() != OrderStatus.CREATED){
            throw new IllegalArgumentException("Order is not waiting for payment");
        }
    }

    private void markOrderPaid(Order order){
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private boolean processFakeGateway(){
        return Math.random() > 0.1; // 90% success
    }
}

