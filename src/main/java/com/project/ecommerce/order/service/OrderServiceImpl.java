package com.project.ecommerce.order.service;

import com.project.ecommerce.cart.repository.CartItemRepository;
import com.project.ecommerce.order.dto.response.OrderResponse;
import com.project.ecommerce.order.entity.OrderStatus;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderItem;
import com.project.ecommerce.order.mapper.OrderMapper;
import com.project.ecommerce.order.repository.OrderRepository;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.ProductRepository;
import com.project.ecommerce.cart.entity.Cart;
import com.project.ecommerce.cart.entity.CartItem;
import com.project.ecommerce.cart.repository.CartRepository;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CartItemRepository cartItemRepository;

    public OrderServiceImpl(CartRepository cartRepository, OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderMapper orderMapper, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    @Override
    public OrderResponse checkout(Long userId) {
        //1. Get user and cart
        Cart cart = getCart(userId);
        validateCart(cart);
        //2. Create order
        Order order = createOrder(cart.getUser());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        //3. Convert cart items to order items
        for(CartItem cartItem : cart.getCartItems()){
            Product product = cartItem.getProduct();

            // Stock validation
            validateStock(product, cartItem.getQuantity());
//
            // reduce stock
            reduceStock(product, cartItem.getQuantity());
            // create order item
            OrderItem orderItem =  createOrderItem( order, cartItem);
//
            orderItems.add(orderItem);

            // Total calculation
            total = calculateTotal(total, product.getPrice(), cartItem.getQuantity());
//
        }

        //4. finalize order
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        //5. save order
        Order savedOrder = orderRepository.save(order);

        //6. clear cart
        cartItemRepository.deleteAll(cart.getCartItems());
        cartRepository.save(cart);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Order not found"));
        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Order not found"));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    @Transactional
    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                        .orElseThrow(()->new EntityNotFoundException("Order not found"));
        if(order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED){
            throw new IllegalArgumentException("Cannot cancel shipped or delivered order");
        }
        if(order.getStatus() == OrderStatus.CANCELLED){
            return orderMapper.toResponse(order);
        }
        for(OrderItem item : order.getOrderItems()){
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponse(orderRepository.save(order));
    }


    //-------------Private methods

    private Cart getCart(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseThrow(()->new EntityNotFoundException("Cart not found"));
    }

    private Order createOrder(User user){
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    private void validateStock(Product product, int quantity){
        if(product.getStockQuantity() < quantity){
            throw new IllegalArgumentException("Not enough stock for product " + product.getName());
        }
    }

    private BigDecimal calculateTotal(BigDecimal total, BigDecimal price, int quantity){
        return total.add(price
                .multiply(BigDecimal.valueOf(quantity)));
    }

    private OrderItem createOrderItem(Order order, CartItem cartItem){
        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

        return orderItem;
    }

    private void reduceStock(Product product, int quantity){
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);
    }

    private void validateCart(Cart cart){
        if(cart.getCartItems().isEmpty()){
            throw new IllegalArgumentException("Cart is empty");
        }
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next){
        switch (current){
            case CREATED -> {
                if(!(next == OrderStatus.PAID || next == OrderStatus.CANCELLED)){
                    throw new IllegalArgumentException("Invalid transition from CREATED");
                }
            }
            case PAID -> {
                if(!(next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED)){
                    throw new IllegalArgumentException("Invalid transition from PAID");
                }
            }
            case SHIPPED -> {
                if(!(next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED)){
                    throw new IllegalArgumentException("Invalid transition from SHIPPED");
                }
            }
            case DELIVERED -> {
                throw new IllegalArgumentException("Order already completed");
            }
            case CANCELLED -> {
                throw new IllegalArgumentException("Order already cancelled");
            }
        }
    }
}
