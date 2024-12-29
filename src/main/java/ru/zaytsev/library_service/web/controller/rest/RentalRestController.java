package ru.zaytsev.library_service.web.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zaytsev.library_service.entity.BookRental;
import ru.zaytsev.library_service.repository.BookRentalRepository;
import ru.zaytsev.library_service.web.response.ClientRentalInfoResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rentals")
public class RentalRestController {

    private final BookRentalRepository bookRentalRepository;

    @GetMapping("/active")
    public ResponseEntity<List<ClientRentalInfoResponse>> getAllActiveRentals() {
        List<BookRental> activeRentals = bookRentalRepository.findByReturnDateIsNull();

        return ResponseEntity.ok().body(activeRentals.stream()
                .map(rental -> new ClientRentalInfoResponse(
                        rental.getClient().getFullName(),
                        rental.getClient().getBirthDate(),
                        rental.getBook().getTitle(),
                        rental.getBook().getAuthor(),
                        rental.getBook().getIsbn(),
                        rental.getRentalDate()
                ))
                .collect(Collectors.toList()));
    }
}
