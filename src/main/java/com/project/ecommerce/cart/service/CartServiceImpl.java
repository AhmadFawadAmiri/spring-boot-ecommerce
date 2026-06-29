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
import jakarta.persistence.EntityNotFoundException;
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
    public CartResponse addToCart(Long userId, Long productId, int quantity) {

        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        User user = getUser(userId);
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
    public CartResponse getCartByUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new EntityNotFoundException("Cart not found"));
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new EntityNotFoundException("Cart item not found"));
        if(!item.getCart().getUser().getId().equals(userId)) {
            throw new EntityNotFoundException("Not allowed");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    @Override
    public CartItemResponse updateItem(Long id, CartItemRequest item) {
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(()-> new EntityNotFoundException("Item not found"));
        if(product.getStockQuantity() < item.getQuantity()){
            throw new IllegalArgumentException("Not enough stock");
        }
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Item not found"));
        Cart cart = cartRepository.findById(cartItem.getCart().getId())
                .orElseThrow(()->new EntityNotFoundException("Cart not found"));
        cartMapper.updateCartItem(cartItem, item, cart);
        cartItem.setProduct(getProduct(item.getProductId()));
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
}
