package com.bookworms.service;

import com.bookworms.exception.ApiException;
import com.bookworms.model.Book;
import com.bookworms.model.Review;
import com.bookworms.repository.BookRepository;
import com.bookworms.repository.ListingRepository;
import com.bookworms.repository.LoanRepository;
import com.bookworms.repository.ReviewLikeRepository;
import com.bookworms.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ListingRepository listingRepository;
    private final LoanRepository loanRepository;

    public BookService(BookRepository bookRepository, ReviewRepository reviewRepository,
                        ReviewLikeRepository reviewLikeRepository, ListingRepository listingRepository,
                        LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.listingRepository = listingRepository;
        this.loanRepository = loanRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> ApiException.notFound("Book not found."));
    }

    public List<Book> search(String query) {
        if (query == null || query.isBlank()) return getAll();
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    /** Reuses an existing book with the same title + author (case-insensitive), else creates one. */
    public Book findOrCreate(String title, String author, String genre, String description, Long userId) {
        String t = title.trim();
        String a = author.trim();
        for (Book b : bookRepository.findAll()) {
            if (b.getTitle().equalsIgnoreCase(t) && b.getAuthor().equalsIgnoreCase(a)) {
                return b;
            }
        }
        int color = 0xFF000000 | ((t.hashCode() & 0x00FFFFFF) ^ 0x6B4A32);
        Book book = new Book(t, a, (genre == null || genre.isBlank()) ? "General" : genre.trim(),
                description == null ? "" : description.trim(), color, userId);
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteCascade(Long bookId) {
        findById(bookId); // 404 if missing
        loanRepository.deleteByBookId(bookId);
        listingRepository.deleteByBookId(bookId);
        for (Review r : reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId)) {
            reviewLikeRepository.deleteByReviewId(r.getId());
        }
        reviewRepository.deleteByBookId(bookId);
        bookRepository.deleteById(bookId);
    }

    public long countAll() {
        return bookRepository.count();
    }
}
