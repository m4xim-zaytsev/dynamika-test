package ru.zaytsev.library_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zaytsev.library_service.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
