package com.project.ecommerce.cart.service;

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

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {

        User user = getUser(userId);
        Product product = getProduct(productId);
//        validateStock(product, quantity);
        Cart cart = getOrCreateCart(user);

        Optional<CartItem> existingItem = findCartItemByCartAndProduct(cart, productId);

        int totalQuantity = existingItem.map(item->item.getQuantity()+quantity)
                .orElse(quantity);
        if (product.getStockQuantity()<totalQuantity){
            throw new EntityNotFoundException("Not enough stock");
        }

        CartItem cartItem;

        if(existingItem.isPresent()){
            cartItem = existingItem.get();
            updateQuantity(cartItem, quantity);
        }else {
            cartItem = createCartItem(cart, product, quantity);
        }

        cartItemRepository.save(cartItem);
        return cart;


//        int existingQty = cartItemOpt.map(CartItem::getQuantity).orElse(0);
//
//        if(product.getStockQuantity() < existingQty + quantity){
//            throw new RuntimeException("Not enough stock");
//        }
//
//        if(cartItemOpt.isPresent()){
//            CartItem existing = cartItemOpt.get();
//            existing.setQuantity(existing.getQuantity() + quantity);
//
//            cartItemRepository.save(existing);
//        }else {
//            CartItem item = new CartItem();
//            item.setCart(cart);
//            item.setProduct(product);
//            item.setQuantity(quantity);
//
//            cartItemRepository.save(item);
//        }
//
//        return cartRepository.save(cart);
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

//    private void validateStock(Product product, int quantity){
//        if(product.getStockQuantity() < quantity){
//            throw new EntityNotFoundException("Not enough stock");
//        }
//    }

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
