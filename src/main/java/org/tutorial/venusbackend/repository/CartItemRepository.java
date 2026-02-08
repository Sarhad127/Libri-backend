package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.Cart;
import org.tutorial.venusbackend.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);

}