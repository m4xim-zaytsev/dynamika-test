package ru.zaytsev.library_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.zaytsev.library_service.entity.Client;
import ru.zaytsev.library_service.exceptions.ActiveRentalException;
import ru.zaytsev.library_service.exceptions.DuplicateEntityException;
import ru.zaytsev.library_service.exceptions.EntityNotFoundException;
import ru.zaytsev.library_service.exceptions.ValidationException;
import ru.zaytsev.library_service.repository.BookRentalRepository;
import ru.zaytsev.library_service.repository.ClientRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final BookRentalRepository bookRentalRepository;

    public List<Client> getAllClients() {
        log.info("Fetching all clients from the database");
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        log.info("Fetching client with ID {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client with ID {} not found", id);
                    return new EntityNotFoundException("Client with ID " + id + " not found");
                });
    }

    public Client addClient(Client client) {
        log.info("Adding new client with full name '{}'", client.getFullName());
        validateClient(client);

        if (clientRepository.findAll().stream()
                .anyMatch(existingClient -> existingClient.getFullName().equalsIgnoreCase(client.getFullName())
                        && existingClient.getBirthDate().equals(client.getBirthDate()))) {
            log.warn("Client with the same name '{}' and birth date '{}' already exists",
                    client.getFullName(), client.getBirthDate());
            throw new DuplicateEntityException("Client with the same name and birth date already exists");
        }

        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        log.info("Updating client with ID {}", id);
        validateClient(clientDetails);

        Client client = getClientById(id);
        client.setFullName(clientDetails.getFullName());
        client.setBirthDate(clientDetails.getBirthDate());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        log.info("Attempting to delete client with ID {}", id);

        if (bookRentalRepository.findByClientIdAndReturnDateIsNull(id).size() > 0) {
            log.warn("Cannot delete client with ID {}: active rentals exist", id);
            throw new ActiveRentalException("Cannot delete a client with active rentals");
        }

        if (!clientRepository.existsById(id)) {
            log.warn("Client with ID {} not found", id);
            throw new EntityNotFoundException("Client with ID " + id + " not found");
        }

        clientRepository.deleteById(id);
        log.info("Client with ID {} deleted successfully", id);
    }

    private void validateClient(Client client) {
        if (client.getFullName() == null || client.getFullName().isEmpty()) {
            log.warn("Validation failed: Client full name is empty");
            throw new ValidationException("Client full name cannot be empty");
        }
        if (client.getBirthDate() == null) {
            log.warn("Validation failed: Client birth date is null");
            throw new ValidationException("Client birth date cannot be null");
        }
    }
}

