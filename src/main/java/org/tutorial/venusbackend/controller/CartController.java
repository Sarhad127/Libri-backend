package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.Cart;
import org.tutorial.venusbackend.model.CartItem;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.BookRepository;
import org.tutorial.venusbackend.repository.CartItemRepository;
import org.tutorial.venusbackend.repository.CartRepository;
import org.tutorial.venusbackend.dto.CartItemRequest;
import org.tutorial.venusbackend.dto.CartItemResponse;
import org.tutorial.venusbackend.repository.MyUserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final MyUserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        MyUser user = getAuthenticatedUser(authentication);
        Cart cart = getOrCreateCart(user);

        List<CartItemResponse> items = cartItemRepository.findByCart(cart)
                .stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(Authentication authentication,
                                                      @RequestBody CartItemRequest request) {
        MyUser user = getAuthenticatedUser(authentication);

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItem cartItem = cartItemRepository.findByCartAndBook(cart, book)
                .orElseGet(() -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setBook(book);
                    item.setQuantity(0);
                    return item;
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok(CartItemResponse.fromEntity(cartItem));
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(Authentication authentication,
                                                           @PathVariable Long cartItemId,
                                                           @RequestBody CartItemRequest request) {
        MyUser user = getAuthenticatedUser(authentication);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return ResponseEntity.ok(CartItemResponse.fromEntity(item));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, String>> removeCartItem(Authentication authentication,
                                                              @PathVariable Long cartItemId) {
        MyUser user = getAuthenticatedUser(authentication);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Cart cart = item.getCart();

        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartItemRepository.flush();

        if (cart.getItems().isEmpty()) {
            cartRepository.delete(cart);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Cart item removed"));
    }

    private MyUser getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Cart getOrCreateCart(MyUser user) {
        return cartRepository.findByUser(user)
                .orElse(null);
    }
}