package org.tutorial.venusbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.repository.OrderItemRepository;
import org.tutorial.venusbackend.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final OrderItemRepository orderItemRepository;
    private final ReviewRepository reviewRepository;

    public List<Book> getMostPopularBooks(int limit) {
        List<Object[]> results = orderItemRepository.findBookPopularity(limit);

        return results.stream()
                .map(obj -> (Book) obj[0])
                .toList();
    }

    public List<Book> getMostPopularBooksRecent(int days, int limit) {
        List<Object[]> results = orderItemRepository.findBookPopularitySince(days, limit);
        return results.stream()
                .map(obj -> (Book) obj[0])
                .toList();
    }

    public List<Book> getTopRatedBooks(int limit) {
        List<Object[]> results = reviewRepository.findBooksByWeightedTopRated(limit);
        return results.stream()
                .map(obj -> (Book) obj[0])
                .toList();
    }
}