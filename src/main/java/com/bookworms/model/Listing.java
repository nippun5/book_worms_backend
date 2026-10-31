package com.bookworms.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long ownerUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingType type;

//    private Double price; // null when type == LOAN
    @Column(name = "price")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_", nullable = false)
    private ListingCondition condition = ListingCondition.GOOD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus status = ListingStatus.AVAILABLE;

    @Column(length = 1000)
    private String note = "";

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Listing() {}

    public Listing(Long bookId, Long ownerUserId, ListingType type, Double price,
                   ListingCondition condition, String note) {
        this.bookId = bookId;
        this.ownerUserId = ownerUserId;
        this.type = type;
        this.price = price;
        this.condition = condition;
        this.note = note;
    }

    public Long getId() { return id; }

    public Long getBookId() { return bookId; }

    public Long getOwnerUserId() { return ownerUserId; }

    public ListingType getType() { return type; }
    public void setType(ListingType type) { this.type = type; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public ListingCondition getCondition() { return condition; }
    public void setCondition(ListingCondition condition) { this.condition = condition; }

    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Instant getCreatedAt() { return createdAt; }
}
