package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import org.tutorial.venusbackend.service.JwtService;

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
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("Authorization") String authHeader) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(401).build();

        Cart cart = getOrCreateCart(user);

        List<CartItemResponse> items = cartItemRepository.findByCart(cart)
                .stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestHeader("Authorization") String authHeader,
                                                      @RequestBody CartItemRequest request) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(401).build();

        Cart cart = getOrCreateCart(user);

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
    public ResponseEntity<CartItemResponse> updateCartItem(@RequestHeader("Authorization") String authHeader,
                                                           @PathVariable Long cartItemId,
                                                           @RequestBody CartItemRequest request) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(401).build();

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
    public ResponseEntity<Map<String, String>> removeCartItem(@RequestHeader("Authorization") String authHeader,
                                                              @PathVariable Long cartItemId) {
        MyUser user = getUserFromToken(authHeader);
        if (user == null) return ResponseEntity.status(401).build();

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

    private Cart getOrCreateCart(MyUser user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private MyUser getUserFromToken(String authHeader) {
        System.out.println("Auth header: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            System.out.println("JWT parse error: " + e.getMessage());
            return null;
        }

        return userRepository.findByEmail(email).orElse(null);
    }
}