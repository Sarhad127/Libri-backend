package org.tutorial.venusbackend.service;

import org.springframework.stereotype.Service;
import org.tutorial.venusbackend.model.Author;
import org.tutorial.venusbackend.model.Book;
import org.tutorial.venusbackend.model.enums.Category;
import org.tutorial.venusbackend.model.enums.Format;
import org.tutorial.venusbackend.model.enums.Language;
import org.tutorial.venusbackend.repository.AuthorRepository;
import org.tutorial.venusbackend.repository.BookRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class BookImportService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ObjectMapper objectMapper;

    public BookImportService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Integer> importBooksFromJson(String jsonFilePath) {
        int added = 0;
        int updated = 0;
        int skipped = 0;

        try (InputStream is = getClass().getResourceAsStream(jsonFilePath)) {
            if (is == null) {
                throw new RuntimeException("File not found: " + jsonFilePath);
            }

            List<Map<String, Object>> books = objectMapper.readValue(is, new TypeReference<>() {});

            for (Map<String, Object> data : books) {
                try {
                    String title = (String) data.get("title");
                    String authorName = (String) data.get("author");
                    Category category = Category.valueOf(((String) data.get("category")).toUpperCase());
                    Format format = Format.valueOf(((String) data.get("format")).toUpperCase());
                    Language language = Language.valueOf(((String) data.get("language")).toUpperCase());
                    String series_name = (String) data.get("series_name");
                    Integer series_number = data.get("series_number") != null ? ((Number) data.get("series_number")).intValue() : null;
                    String publisher = (String) data.get("publisher");
                    String description = (String) data.get("description");
                    String isbn = (String) data.get("isbn");
                    int stock = data.get("stock") != null ? ((Number) data.get("stock")).intValue() : 0;
                    String imageUrl = (String) data.get("image_url");
                    BigDecimal price = new BigDecimal(data.get("price").toString());
                    Integer pages = data.get("pages") != null ? ((Number) data.get("pages")).intValue() : null;

                    Author author = authorRepository.findByName(authorName)
                            .stream()
                            .findFirst()
                            .orElseGet(() -> {
                                Author newAuthor = new Author();
                                newAuthor.setName(authorName);
                                return authorRepository.save(newAuthor);
                            });

                    Optional<Book> existingBookOpt = bookRepository.findByIsbn(isbn);

                    if (existingBookOpt.isEmpty()) {
                        Book book = new Book();
                        book.setTitle(title);
                        book.setAuthor(author);
                        book.setCategory(category);
                        book.setFormat(format);
                        book.setLanguage(language);
                        book.setSeriesName(series_name);
                        book.setSeriesNumber(series_number);
                        book.setPublisher(publisher);
                        book.setDescription(description);
                        book.setIsbn(isbn);
                        book.setStock(stock);
                        book.setImageUrl(imageUrl);
                        book.setPrice(price);
                        book.setPages(pages);

                        bookRepository.save(book);
                        added++;
                    } else {
                        Book existingBook = existingBookOpt.get();
                        boolean changed = false;

                        if (!existingBook.getTitle().equals(title)) { existingBook.setTitle(title); changed = true; }
                        if (!existingBook.getAuthor().equals(author)) { existingBook.setAuthor(author); changed = true; }
                        if (!existingBook.getCategory().equals(category)) { existingBook.setCategory(category); changed = true; }
                        if (!existingBook.getFormat().equals(format)) { existingBook.setFormat(format); changed = true; }
                        if (!existingBook.getLanguage().equals(language)) { existingBook.setLanguage(language); changed = true; }
                        if ((existingBook.getSeriesName() == null && series_name != null) ||
                                (existingBook.getSeriesName() != null && !existingBook.getSeriesName().equals(series_name))) {
                            existingBook.setSeriesName(series_name); changed = true;
                        }
                        if ((existingBook.getSeriesNumber() == null && series_number != null) ||
                                (existingBook.getSeriesNumber() != null && !existingBook.getSeriesNumber().equals(series_number))) {
                            existingBook.setSeriesNumber(series_number); changed = true;
                        }
                        if (!existingBook.getPublisher().equals(publisher)) { existingBook.setPublisher(publisher); changed = true; }
                        if (!existingBook.getDescription().equals(description)) { existingBook.setDescription(description); changed = true; }
                        if (existingBook.getStock() != stock) { existingBook.setStock(stock); changed = true; }
                        if (!existingBook.getImageUrl().equals(imageUrl)) { existingBook.setImageUrl(imageUrl); changed = true; }
                        if (existingBook.getPrice().compareTo(price) != 0) { existingBook.setPrice(price); changed = true; }
                        if ((existingBook.getPages() == null && pages != null) ||
                                (existingBook.getPages() != null && !existingBook.getPages().equals(pages))) { existingBook.setPages(pages); changed = true; }

                        if (changed) {
                            bookRepository.save(existingBook);
                            updated++;
                        } else {
                            skipped++;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Map.of(
                "added", added,
                "updated", updated,
                "skipped", skipped
        );
    }
}