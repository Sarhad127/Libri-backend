package org.tutorial.venusbackend.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookAndUser(Book book, MyUser user);

    @Query("""
    SELECT b, 
           COALESCE(SUM(CASE WHEN r.rating = 5 THEN 5 ELSE 0 END), 0) +
           COALESCE(SUM(CASE WHEN r.rating = 4 THEN 4 ELSE 0 END), 0) +
           COALESCE(SUM(CASE WHEN r.rating = 3 THEN 3 ELSE 0 END), 0) +
           COALESCE(SUM(CASE WHEN r.rating = 2 THEN 2 ELSE 0 END), 0) +
           COALESCE(SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END), 0)
           AS weightedScore
    FROM Book b
    LEFT JOIN Review r ON r.book = b
    GROUP BY b
    ORDER BY weightedScore DESC
""")
    List<Object[]> findBooksByWeightedTopRated(Pageable pageable);

    default List<Object[]> findBooksByWeightedTopRated(int limit) {
        return findBooksByWeightedTopRated(PageRequest.of(0, limit));
    }
}