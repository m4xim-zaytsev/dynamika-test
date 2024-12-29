package ru.zaytsev.library_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zaytsev.library_service.service.BookRentalService;
import ru.zaytsev.library_service.service.BookService;
import ru.zaytsev.library_service.service.ClientService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class BookRentalController {

    private final BookRentalService bookRentalService;
    private final ClientService clientService;
    private final BookService bookService;

    @GetMapping
    public String listRentals(Model model) {
        model.addAttribute("activeRentals", bookRentalService.getAllActiveRentals());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("books", bookService.getAllBooks());
        return "rentals";
    }

    @PostMapping
    public String rentBook(@RequestParam Long clientId, @RequestParam Long bookId, RedirectAttributes redirectAttributes) {
        bookRentalService.rentBook(clientId, bookId);
        redirectAttributes.addFlashAttribute("successMessage", "Book rented successfully!");
        return "redirect:/rentals";
    }

    @PostMapping("/{rentalId}/return")
    public String returnBook(@PathVariable Long rentalId, RedirectAttributes redirectAttributes) {
        bookRentalService.returnBook(rentalId);
        redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully!");
        return "redirect:/rentals";
    }
}
