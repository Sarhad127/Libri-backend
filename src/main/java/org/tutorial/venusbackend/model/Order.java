package org.tutorial.venusbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MyUser user;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private String status; // PLACED, PAID, CANCELLED
}