package ru.zaytsev.library_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zaytsev.library_service.entity.Book;
import ru.zaytsev.library_service.entity.BookRental;
import ru.zaytsev.library_service.entity.Client;
import ru.zaytsev.library_service.exceptions.EntityNotFoundException;
import ru.zaytsev.library_service.exceptions.ValidationException;
import ru.zaytsev.library_service.repository.BookRentalRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookRentalService {

    private final BookRentalRepository bookRentalRepository;
    private final ClientService clientService;
    private final BookService bookService;

    public List<BookRental> getAllActiveRentals() {
        log.info("Fetching all active rentals");
        return bookRentalRepository.findByReturnDateIsNull();
    }

    public BookRental rentBook(Long clientId, Long bookId) {
        log.info("Client with ID {} is renting book with ID {}", clientId, bookId);

        Client client = clientService.getClientById(clientId);
        Book book = bookService.getBookById(bookId);

        if (bookRentalRepository.findByClientIdAndReturnDateIsNull(clientId).stream()
                .anyMatch(rental -> rental.getBook().getId().equals(bookId))) {
            log.warn("Client with ID {} already rented book with ID {}", clientId, bookId);
            throw new ValidationException("Client already rented this book");
        }

        BookRental rental = new BookRental();
        rental.setClient(client);
        rental.setBook(book);
        rental.setRentalDate(LocalDate.now());
        log.info("Book with ID {} rented by client with ID {}", bookId, clientId);
        return bookRentalRepository.save(rental);
    }

    public BookRental returnBook(Long rentalId) {
        log.info("Processing return of rental with ID {}", rentalId);

        BookRental rental = bookRentalRepository.findById(rentalId)
                .orElseThrow(() -> {
                    log.warn("Rental with ID {} not found", rentalId);
                    return new EntityNotFoundException("Rental with ID " + rentalId + " not found");
                });

        if (rental.getReturnDate() != null) {
            log.warn("Rental with ID {} already returned", rentalId);
            throw new ValidationException("Book already returned");
        }

        rental.setReturnDate(LocalDate.now());
        log.info("Rental with ID {} marked as returned", rentalId);
        return bookRentalRepository.save(rental);
    }
}
