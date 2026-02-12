package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.dto.CreateOrderRequest;
import org.tutorial.venusbackend.dto.ShippingMethodRequest;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.model.Order;
import org.tutorial.venusbackend.model.OrderItem;
import org.tutorial.venusbackend.repository.BookRepository;
import org.tutorial.venusbackend.repository.OrderRepository;
import org.tutorial.venusbackend.service.AuthHelperService;
import org.tutorial.venusbackend.dto.CartItemRequest;
import org.tutorial.venusbackend.dto.OrderResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final AuthHelperService authHelperService;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateOrderRequest request) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        List<CartItemRequest> cartItems = request.getCartItems();
        ShippingMethodRequest shippingMethod = request.getShippingMethod();

        if (cartItems.isEmpty() || shippingMethod == null) {
            return ResponseEntity.badRequest().build();
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PAID);
        order.setOrderNumber(generateOrderNumber());

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (CartItemRequest ci : cartItems) {
            Book book = bookRepository.findById(ci.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(ci.getQuantity());
            item.setPriceAtPurchase(book.getPrice());

            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            items.add(item);
        }

        order.setItems(items);
        order.setTotalAmount(total.add(shippingMethod.getPrice()));
        order.setShippingMethodLabel(shippingMethod.getLabel());
        order.setShippingCost(shippingMethod.getPrice());
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        List<Order> orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user);

        List<OrderResponse> response = orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private String generateOrderNumber() {
        return String.valueOf(
                100_000_000L + ThreadLocalRandom.current().nextLong(900_000_000L)
        );
    }
}