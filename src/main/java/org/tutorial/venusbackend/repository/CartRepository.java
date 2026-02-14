package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.Cart;
import org.tutorial.venusbackend.model.MyUser;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(MyUser user);
}