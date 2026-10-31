package com.bookworms.controller;

import com.bookworms.dto.CreateBookRequest;
import com.bookworms.model.Book;
import com.bookworms.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAll(@RequestParam(required = false) String q) {
        return (q == null || q.isBlank()) ? bookService.getAll() : bookService.search(q);
    }

    @GetMapping("/{id}")
    public Book getOne(@PathVariable Long id) {
        return bookService.findById(id);
    }

    /** Creates a book, or reuses an existing one with the same title + author. */
    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody CreateBookRequest req) {
        Book book = bookService.findOrCreate(req.title(), req.author(), req.genre(), req.description(), req.addedByUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    /** Admin: removes the book and every review/listing/loan tied to it. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.deleteCascade(id);
        return ResponseEntity.noContent().build();
    }
}
