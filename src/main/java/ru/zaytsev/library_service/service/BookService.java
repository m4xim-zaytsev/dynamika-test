package ru.zaytsev.library_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zaytsev.library_service.entity.Book;
import ru.zaytsev.library_service.exceptions.ActiveRentalException;
import ru.zaytsev.library_service.exceptions.DuplicateEntityException;
import ru.zaytsev.library_service.exceptions.EntityNotFoundException;
import ru.zaytsev.library_service.exceptions.ValidationException;
import ru.zaytsev.library_service.repository.BookRentalRepository;
import ru.zaytsev.library_service.repository.BookRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookRentalRepository bookRentalRepository;

    public List<Book> getAllBooks() {
        log.info("Fetching all books from the database");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        log.info("Fetching book with ID {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book with ID {} not found", id);
                    return new EntityNotFoundException("Book with ID " + id + " not found");
                });
    }

    public Book addBook(Book book) {
        log.info("Adding new book with title '{}'", book.getTitle());
        validateBook(book);

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            log.warn("Book with ISBN {} already exists", book.getIsbn());
            throw new DuplicateEntityException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        log.info("Updating book with ID {}", id);
        validateBook(bookDetails);

        Book book = getBookById(id);
        if (!book.getIsbn().equals(bookDetails.getIsbn()) && bookRepository.existsByIsbn(bookDetails.getIsbn())) {
            log.warn("Book with ISBN {} already exists", bookDetails.getIsbn());
            throw new DuplicateEntityException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
        }

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        log.info("Attempting to delete book with ID {}", id);

        if (bookRentalRepository.findByBookIdAndReturnDateIsNull(id).size() > 0) {
            log.warn("Cannot delete book with ID {}: active rentals exist", id);
            throw new ActiveRentalException("Cannot delete a book with active rentals");
        }

        if (!bookRepository.existsById(id)) {
            log.warn("Book with ID {} not found", id);
            throw new EntityNotFoundException("Book with ID " + id + " not found");
        }

        bookRepository.deleteById(id);
        log.info("Book with ID {} deleted successfully", id);
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            log.warn("Validation failed: Book title is empty");
            throw new ValidationException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            log.warn("Validation failed: Book author is empty");
            throw new ValidationException("Book author cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            log.warn("Validation failed: Book ISBN is empty");
            throw new ValidationException("Book ISBN cannot be empty");
        }
    }
}



