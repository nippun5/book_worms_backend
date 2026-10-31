-- Book Worms backend schema (MySQL 8+)
-- Mirrors the JPA entities in com.bookworms.model exactly.
-- With spring.jpa.hibernate.ddl-auto=update, Hibernate will create this
-- automatically from the entities; this file is provided for reference,
-- for DBAs who prefer reviewing/running DDL by hand, and as the source of
-- truth if you set ddl-auto=validate in production.

CREATE TABLE IF NOT EXISTS users (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    bio                 VARCHAR(1000) DEFAULT '',
    avatar_color_value  INT NOT NULL,
    role_               VARCHAR(20) NOT NULL DEFAULT 'READER',
    banned              BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    author              VARCHAR(255) NOT NULL,
    genre               VARCHAR(100) NOT NULL DEFAULT 'General',
    description         VARCHAR(2000) DEFAULT '',
    cover_color_value   INT NOT NULL,
    added_by_user_id    BIGINT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_books_added_by FOREIGN KEY (added_by_user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id             BIGINT NOT NULL,
    user_id             BIGINT NOT NULL,
    rating              INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    text                VARCHAR(4000) NOT NULL,
    like_count          INT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS review_likes (
    review_id           BIGINT NOT NULL,
    user_id             BIGINT NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT fk_likes_review FOREIGN KEY (review_id) REFERENCES reviews (id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS listings (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id             BIGINT NOT NULL,
    owner_user_id       BIGINT NOT NULL,
    type                VARCHAR(10) NOT NULL,         -- SALE | LOAN
    price               DECIMAL(10,2),                -- NULL when type = LOAN
    condition_          VARCHAR(20) NOT NULL DEFAULT 'GOOD', -- LIKE_NEW | GOOD | FAIR
    status              VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE | PENDING | UNAVAILABLE
    note                VARCHAR(1000) DEFAULT '',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_listings_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_listings_owner FOREIGN KEY (owner_user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS loans (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    listing_id          BIGINT NOT NULL,
    book_id             BIGINT NOT NULL,
    owner_user_id       BIGINT NOT NULL,
    borrower_user_id    BIGINT NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'REQUESTED', -- REQUESTED | DECLINED | ACTIVE | RETURNED
    requested_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date            TIMESTAMP NULL,
    returned_at         TIMESTAMP NULL,
    CONSTRAINT fk_loans_listing FOREIGN KEY (listing_id) REFERENCES listings (id),
    CONSTRAINT fk_loans_book FOREIGN KEY (book_id) REFERENCES books (id),
    CONSTRAINT fk_loans_owner FOREIGN KEY (owner_user_id) REFERENCES users (id),
    CONSTRAINT fk_loans_borrower FOREIGN KEY (borrower_user_id) REFERENCES users (id)
);

CREATE INDEX idx_reviews_book ON reviews (book_id);
CREATE INDEX idx_reviews_user ON reviews (user_id);
CREATE INDEX idx_listings_book ON listings (book_id);
CREATE INDEX idx_listings_owner ON listings (owner_user_id);
CREATE INDEX idx_loans_borrower ON loans (borrower_user_id);
CREATE INDEX idx_loans_owner ON loans (owner_user_id);
