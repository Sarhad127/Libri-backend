package org.tutorial.venusbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.tutorial.venusbackend.model.enums.Category;
import org.tutorial.venusbackend.model.enums.Format;
import org.tutorial.venusbackend.model.enums.Language;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Format format;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String seriesName;
    private Integer seriesNumber;
    private String publisher;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private String isbn;

    @Column(nullable = false)
    private int stock;

    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Integer pages;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteBooks")
    private List<MyUser> favoritedBy = new ArrayList<>();
}