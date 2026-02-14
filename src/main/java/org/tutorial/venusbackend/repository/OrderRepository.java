package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByCreatedAtDesc(MyUser user);
}
