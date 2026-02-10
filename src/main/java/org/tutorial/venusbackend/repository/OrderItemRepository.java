package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}