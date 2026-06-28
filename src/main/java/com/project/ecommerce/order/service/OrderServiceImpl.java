package com.project.ecommerce.order.orderService;

import com.project.ecommerce.order.OrderStatus;
import com.project.ecommerce.order.entity.Order;
import com.project.ecommerce.order.entity.OrderItem;
import com.project.ecommerce.order.repository.OrderRepository;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.ProductRepository;
import com.project.ecommerce.user.entity.Cart;
import com.project.ecommerce.user.entity.CartItem;
import com.project.ecommerce.user.repository.CartRepository;
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

    public OrderServiceImpl(CartRepository cartRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public Order createOrderFromCart(Long userId) {
        //1. Get cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new EntityNotFoundException("Cart not found"));
        //2. Create order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        //3. Convert cart items to order items
        for(CartItem cartItem : cart.getCartItems()){
            Product product = cartItem.getProduct();

            // Stock validation
            if(product.getStockQuantity() < cartItem.getQuantity()){
                throw new RuntimeException("Not enough stock for product"+product.getName());
            }
            // reduce stock
            product.setStockQuantity(product.getStockQuantity()-cartItem.getQuantity());
            productRepository.save(product);
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            // Total calculation
            total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        //4. finalize order
        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        //5. save order
        Order savedOrder = orderRepository.save(order);

        //6. clear cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }
}
