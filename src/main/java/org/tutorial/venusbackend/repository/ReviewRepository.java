package org.tutorial.venusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookAndUser(Book book, MyUser user);
}