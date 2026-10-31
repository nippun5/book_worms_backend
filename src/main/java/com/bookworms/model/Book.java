package com.bookworms.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String genre = "General";

    @Column(length = 2000)
    private String description = "";

    @Column(nullable = false)
    private Integer coverColorValue;

    @Column(nullable = false)
    private Long addedByUserId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Book() {}

    public Book(String title, String author, String genre, String description,
                Integer coverColorValue, Long addedByUserId) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.coverColorValue = coverColorValue;
        this.addedByUserId = addedByUserId;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCoverColorValue() { return coverColorValue; }
    public void setCoverColorValue(Integer coverColorValue) { this.coverColorValue = coverColorValue; }

    public Long getAddedByUserId() { return addedByUserId; }

    public Instant getCreatedAt() { return createdAt; }
}
