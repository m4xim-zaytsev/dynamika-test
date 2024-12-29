package ru.zaytsev.library_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zaytsev.library_service.entity.BookRental;

import java.util.List;

public interface BookRentalRepository extends JpaRepository<BookRental, Long> {
    List<BookRental> findByReturnDateIsNull();

    List<BookRental> findByClientIdAndReturnDateIsNull(Long clientId);

    List<BookRental> findByBookIdAndReturnDateIsNull(Long bookId);

}

