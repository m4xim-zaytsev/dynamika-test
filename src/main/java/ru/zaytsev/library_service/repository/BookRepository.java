package ru.zaytsev.library_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zaytsev.library_service.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
