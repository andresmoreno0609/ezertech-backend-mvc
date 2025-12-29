CREATE TABLE book_status (
    id          SMALLSERIAL PRIMARY KEY,
    code        VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL
);
INSERT INTO book_status (code, description) VALUES
('AVAILABLE', 'Libro disponible'),
('BORROWED', 'Libro prestado'),
('RESERVED', 'Libro reservado');

CREATE TABLE books (
    id                BIGSERIAL PRIMARY KEY,
    title             VARCHAR(150) NOT NULL,
    author            VARCHAR(100) NOT NULL,
    isbn              CHAR(13) NOT NULL UNIQUE,
    publication_year  INTEGER NOT NULL,
    status_id         SMALLINT NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_books_status
        FOREIGN KEY (status_id)
        REFERENCES book_status (id),

    CONSTRAINT chk_publication_year
        CHECK (
            publication_year >= 1000
            AND publication_year <= EXTRACT(YEAR FROM CURRENT_DATE)
        )
);


CREATE INDEX idx_books_isbn ON books (isbn);
CREATE INDEX idx_books_status_id ON books (status_id);
CREATE INDEX idx_books_author ON books (author);



INSERT INTO books (title, author, isbn, publication_year, status_id)
VALUES (
    'Effective Java',
    'Joshua Bloch',
    '9780134685991',
    2018,
    (SELECT id FROM book_status WHERE code = 'AVAILABLE')
);


CREATE TABLE loans (
    id               BIGSERIAL PRIMARY KEY,
    book_id          BIGINT NOT NULL,
    borrower_name    VARCHAR(100) NOT NULL,
    borrower_email   VARCHAR(150) NOT NULL,
    loan_date        DATE NOT NULL,
    due_date         DATE NOT NULL,
    return_date      DATE,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_loans_book
        FOREIGN KEY (book_id)
        REFERENCES books (id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_due_date
        CHECK (due_date = loan_date + INTERVAL '14 days'),

    CONSTRAINT chk_borrower_email
        CHECK (borrower_email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$')
);


CREATE INDEX idx_loans_book_id ON loans (book_id);
CREATE INDEX idx_loans_loan_date ON loans (loan_date);
CREATE INDEX idx_loans_borrower_email ON loans (borrower_email);

INSERT INTO loans (
    book_id,
    borrower_name,
    borrower_email,
    loan_date,
    due_date
)
VALUES (
    1,
    'Andrés Moreno',
    'andres.moreno@email.com',
    CURRENT_DATE,
    CURRENT_DATE + INTERVAL '14 days'
);
