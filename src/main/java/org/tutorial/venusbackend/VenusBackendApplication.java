package org.tutorial.venusbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tutorial.venusbackend.model.Author;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.repository.AuthorRepository;
import org.tutorial.venusbackend.repository.BookRepository;

import java.math.BigDecimal;
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
                book.setStock(10);
                book.setCreatedAt(LocalDateTime.now());
                book.setUpdatedAt(LocalDateTime.now());
                book.setImageUrl("https://image.bokus.com/images/9781408855898_383x_harry-potter-and-the-philosophers-stone");
                book.setReviews(new ArrayList<>());
                book.setFavoritedBy(new ArrayList<>());
                book.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book);
                System.out.println("Seeded book: " + book.getTitle());
            } else {
                System.out.println("Book already exists: Harry Potter and the Philosopher's Stone");
            }
            Author rowling = authorRepository.findByName("J.K. Rowling")
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName("J.K. Rowling");
                        return authorRepository.save(newAuthor);
                    });

            boolean exists2 = bookRepository.findByIsbn("9780439064873").isPresent();
            if (!exists2) {
                Book book2 = new Book();
                book2.setTitle("Harry Potter and the Chamber of Secrets");
                book2.setCategory("Fantasy");
                book2.setFormat("Inbunden");
                book2.setLanguage("English");
                book2.setSeriesName("Harry Potter");
                book2.setSeriesNumber(2);
                book2.setPublisher("Bloomsbury");
                book2.setDescription("Harry Potter's second year at Hogwarts is full of mysterious messages and the legend of the Chamber of Secrets.");
                book2.setAuthor(rowling);
                book2.setIsbn("9780439064873");
                book2.setStock(12);
                book2.setCreatedAt(LocalDateTime.now());
                book2.setUpdatedAt(LocalDateTime.now());
                book2.setImageUrl("https://media.harrypotterfanzone.com/chamber-of-secrets-uk-childrens-edition.jpg");
                book2.setReviews(new ArrayList<>());
                book2.setFavoritedBy(new ArrayList<>());
                book2.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book2);
                System.out.println("Seeded book: " + book2.getTitle());
            }

            boolean exists3 = bookRepository.findByIsbn("9780439136365").isPresent();
            if (!exists3) {
                Book book3 = new Book();
                book3.setTitle("Harry Potter and the Prisoner of Azkaban");
                book3.setCategory("Fantasy");
                book3.setFormat("Inbunden");
                book3.setLanguage("English");
                book3.setSeriesName("Harry Potter");
                book3.setSeriesNumber(3);
                book3.setPublisher("Bloomsbury");
                book3.setDescription("Harry's third year at Hogwarts is threatened by the escaped prisoner Sirius Black, who is believed to be after him.");
                book3.setAuthor(rowling);
                book3.setIsbn("9780439136365");
                book3.setStock(15);
                book3.setCreatedAt(LocalDateTime.now());
                book3.setUpdatedAt(LocalDateTime.now());
                book3.setImageUrl("https://m.media-amazon.com/images/I/816KXCejhwL._AC_UF1000,1000_QL80_.jpg");
                book3.setReviews(new ArrayList<>());
                book3.setFavoritedBy(new ArrayList<>());
                book3.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book3);
                System.out.println("Seeded book: " + book3.getTitle());
            }

            boolean exists4 = bookRepository.findByIsbn("9780439139595").isPresent();
            if (!exists4) {
                Book book4 = new Book();
                book4.setTitle("Harry Potter and the Goblet of Fire");
                book4.setCategory("Fantasy");
                book4.setFormat("Inbunden");
                book4.setLanguage("English");
                book4.setSeriesName("Harry Potter");
                book4.setSeriesNumber(4);
                book4.setPublisher("Bloomsbury");
                book4.setDescription("Harry competes in the dangerous Triwizard Tournament and faces new dark forces in his fourth year.");
                book4.setAuthor(rowling);
                book4.setIsbn("9780439139595");
                book4.setStock(10);
                book4.setCreatedAt(LocalDateTime.now());
                book4.setUpdatedAt(LocalDateTime.now());
                book4.setImageUrl("https://m.media-amazon.com/images/I/91-LL7OnDCL._AC_UF1000,1000_QL80_.jpg");
                book4.setReviews(new ArrayList<>());
                book4.setFavoritedBy(new ArrayList<>());
                book4.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book4);
                System.out.println("Seeded book: " + book4.getTitle());
            }

            boolean exists5 = bookRepository.findByIsbn("9780439358071").isPresent();
            if (!exists5) {
                Book book5 = new Book();
                book5.setTitle("Harry Potter and the Order of the Phoenix");
                book5.setCategory("Fantasy");
                book5.setFormat("Inbunden");
                book5.setLanguage("English");
                book5.setSeriesName("Harry Potter");
                book5.setSeriesNumber(5);
                book5.setPublisher("Bloomsbury");
                book5.setDescription("In his fifth year, Harry battles the Ministry of Magic and the growing threat of Voldemort with his friends.");
                book5.setAuthor(rowling);
                book5.setIsbn("9780439358071");
                book5.setStock(12);
                book5.setCreatedAt(LocalDateTime.now());
                book5.setUpdatedAt(LocalDateTime.now());
                book5.setImageUrl("https://m.media-amazon.com/images/I/81CVqFSEO0L._AC_UF894,1000_QL80_.jpg");
                book5.setReviews(new ArrayList<>());
                book5.setFavoritedBy(new ArrayList<>());
                book5.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book5);
                System.out.println("Seeded book: " + book5.getTitle());
            }

            boolean exists6 = bookRepository.findByIsbn("9780545010221").isPresent();
            if (!exists6) {
                Book book6 = new Book();
                book6.setTitle("Harry Potter and the Half-Blood Prince");
                book6.setCategory("Fantasy");
                book6.setFormat("Inbunden");
                book6.setLanguage("English");
                book6.setSeriesName("Harry Potter");
                book6.setSeriesNumber(6);
                book6.setPublisher("Bloomsbury");
                book6.setDescription("Harry discovers a mysterious book that once belonged to the Half-Blood Prince while Voldemort's threat grows.");
                book6.setAuthor(rowling);
                book6.setIsbn("9780545010221");
                book6.setStock(8);
                book6.setCreatedAt(LocalDateTime.now());
                book6.setUpdatedAt(LocalDateTime.now());
                book6.setImageUrl("https://m.media-amazon.com/images/I/813zbNPhO5L._UF1000,1000_QL80_.jpg");
                book6.setReviews(new ArrayList<>());
                book6.setFavoritedBy(new ArrayList<>());
                book6.setPrice(BigDecimal.valueOf(249.00));

                bookRepository.save(book6);
                System.out.println("Seeded book: " + book6.getTitle());
            }
        };
    };
}