package org.tutorial.venusbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String password;

    private String role; // ADMIN or USER

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phoneNumber;
    private String address;

    private boolean isActive = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
