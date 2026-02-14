package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.MyUser;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);
}