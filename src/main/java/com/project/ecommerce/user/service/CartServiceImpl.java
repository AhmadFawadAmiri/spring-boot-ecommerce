package com.project.ecommerce.user.service;

import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.ProductRepository;
import com.project.ecommerce.user.entity.Cart;
import com.project.ecommerce.user.entity.CartItem;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.user.repository.CartItemRepository;
import com.project.ecommerce.user.repository.CartRepository;
import com.project.ecommerce.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId).
                orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                }
        );
        Optional<CartItem> cartItemOpt = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId);

        int existingQty = cartItemOpt.map(CartItem::getQuantity).orElse(0);

        if(product.getStockQuantity() < existingQty + quantity){
            throw new RuntimeException("Not enough stock");
        }

        if(cartItemOpt.isPresent()){
            CartItem existing = cartItemOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);

            cartItemRepository.save(existing);
        }else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);

            cartItemRepository.save(item);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(()-> new EntityNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {
        if(!cartItemRepository.existsById(cartItemId)){
            throw new EntityNotFoundException("Cart item not found");
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
