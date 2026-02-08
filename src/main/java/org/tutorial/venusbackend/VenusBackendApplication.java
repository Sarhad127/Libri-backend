package org.tutorial.venusbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tutorial.venusbackend.model.Author;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.repository.AuthorRepository;
import org.tutorial.venusbackend.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class VenusBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenusBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner run(BookRepository bookRepository, AuthorRepository authorRepository) {
        return args -> {

            Author author = authorRepository.findByName("J.K. Rowling")
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName("J.K. Rowling");
                        return authorRepository.save(newAuthor);
                    });

            boolean exists = bookRepository.findByIsbn("9781408855898").isPresent();
            if (!exists) {
                Book book = new Book();
                book.setTitle("Harry Potter and the Philosopher's Stone");
                book.setCategory("Fantasy");
                book.setFormat("Inbunden");
                book.setLanguage("English");
                book.setSeriesName("Harry Potter");
                book.setSeriesNumber(1);
                book.setPublisher("Bloomsbury");
                book.setDescription("Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive. Soon, he is whisked away to a school of witchcraft and wizardry, where he learns the truth about himself, his family, and the terrible evil that haunts the magical world.");
                book.setAuthor(author);
                book.setIsbn("9781408855898");
                book.setAmount(10);
                book.setCreatedAt(LocalDateTime.now());
                book.setUpdatedAt(LocalDateTime.now());
                book.setImageUrl("https://image.bokus.com/images/9781408855898_383x_harry-potter-and-the-philosophers-stone");

                book.setReviews(new ArrayList<>());
                book.setFavoritedBy(new ArrayList<>());
                book.setBorrowHistory(new ArrayList<>());

                bookRepository.save(book);
                System.out.println("Seeded book: " + book.getTitle());
            } else {
                System.out.println("Book already exists: Harry Potter and the Philosopher's Stone");
            }
        };
    }
}