package org.tutorial.venusbackend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tutorial.venusbackend.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
           SELECT oi.book, SUM(oi.quantity) AS totalSold
           FROM OrderItem oi
           GROUP BY oi.book
           ORDER BY totalSold DESC
           """)
    List<Object[]> findBookPopularity(org.springframework.data.domain.Pageable pageable);

    default List<Object[]> findBookPopularity(int limit) {
        return findBookPopularity(org.springframework.data.domain.PageRequest.of(0, limit));
    }

    @Query("""
       SELECT oi.book, SUM(oi.quantity) AS totalSold
       FROM OrderItem oi
       WHERE oi.order.createdAt >= :fromDate
       GROUP BY oi.book
       ORDER BY totalSold DESC
       """)
    List<Object[]> findBookPopularitySince(java.time.LocalDateTime fromDate, Pageable pageable);

    default List<Object[]> findBookPopularitySince(int days, int limit) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
        return findBookPopularitySince(fromDate, org.springframework.data.domain.PageRequest.of(0, limit));
    }
}