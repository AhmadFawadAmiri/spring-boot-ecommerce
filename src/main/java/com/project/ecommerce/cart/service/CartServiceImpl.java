package com.project.ecommerce.cart.service;

import com.project.ecommerce.cart.dto.request.CartItemRequest;
import com.project.ecommerce.cart.dto.response.CartItemResponse;
import com.project.ecommerce.cart.dto.response.CartResponse;
import com.project.ecommerce.cart.mapper.CartMapper;
import com.project.ecommerce.product.entity.Product;
import com.project.ecommerce.product.repository.ProductRepository;
import com.project.ecommerce.cart.entity.Cart;
import com.project.ecommerce.cart.entity.CartItem;
import com.project.ecommerce.user.entity.User;
import com.project.ecommerce.cart.repository.CartItemRepository;
import com.project.ecommerce.cart.repository.CartRepository;
import com.project.ecommerce.user.repository.UserRepository;
import com.project.ecommerce.user.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public CartResponse addToCart(Long productId, int quantity) {

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        User user = getCurrentUser();
//        User user = getUser(userId);
        Product product = getProduct(productId);
        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = findCartItemByCartAndProduct(cart, productId);

        int totalQuantity = existingItem.map(item->item.getQuantity()+quantity)
                .orElse(quantity);
        if (product.getStockQuantity()<totalQuantity){
            throw new IllegalArgumentException("Not enough stock");
        }

        CartItem cartItem;

        if(existingItem.isPresent()){
            cartItem = existingItem.get();
            updateQuantity(cartItem, quantity);
        }else {
            cartItem = createCartItem(cart, product, quantity);
        }

        cartItemRepository.save(cartItem);
        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        Cart cart = cartRepository.findByUserId(getCurrentUser().getId())
                .orElseThrow(()-> new EntityNotFoundException("Cart not found"));
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId){
        User user = getCurrentUser();
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new EntityNotFoundException("Cart item not found"));
        if(!item.getCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    @Override
    public CartItemResponse updateItem(Long cartItemId, CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new EntityNotFoundException("Item not found"));
        if(product.getStockQuantity() < request.getQuantity()){
            throw new IllegalArgumentException("Not enough stock");
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new EntityNotFoundException("Item not found"));
        if(!cartItem.getCart().getUser().getId().equals(getCurrentUser().getId())) {
            throw new AccessDeniedException("Not allowed");
        }
        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(()->new EntityNotFoundException("Cart not found"));
        cartMapper.updateCartItem(cartItem, request, cart);
        cartItem.setProduct(getProduct(request.getProductId()));
        cartItemRepository.save(cartItem);
        return cartMapper.toCartItemResponse(cartItem);
    }

    // -------------------------
    //Private domain methods
    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));
    }

    private Product getProduct(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));
    }

    private Cart getOrCreateCart(User user){
        return cartRepository.findByUserId(user.getId()).
                orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private Optional<CartItem> findCartItemByCartAndProduct(Cart cart, Long productId){
        return cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId);
    }

    private CartItem createCartItem(Cart cart, Product product, int quantity){
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }

    private void updateQuantity(CartItem item, int quantity){
        item.setQuantity(item.getQuantity() + quantity);
    }

    private User getCurrentUser(){
        return userRepository.findByEmail(SecurityUtils.getCurrentUserEmail())
                .orElseThrow(()-> new EntityNotFoundException("User not found") );
    }
}
