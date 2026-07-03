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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
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
            if(success){
                payment.setStatus(PaymentStatus.SUCCESS);
                markOrderPaid(payment.getOrder());
            }else {
                payment.setStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);
        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentResponse getByOrderId(Long orderId) {
        return paymentMapper.toResponse(paymentRepository.findByOrderId(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Payment not found")));
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
        payment.setStatus(PaymentStatus.PENDING);
        return payment;
    }

    private Order getOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new EntityNotFoundException("Order not found"));
        return order;
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
        return Math.random() > 0.2; // 80% success
    }
}

