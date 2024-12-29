package ru.zaytsev.library_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zaytsev.library_service.entity.Book;
import ru.zaytsev.library_service.mapper.BookMapper;
import ru.zaytsev.library_service.service.BookService;
import ru.zaytsev.library_service.web.request.BookRequest;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("book", new BookRequest());
        return "books";
    }

    @PostMapping
    public String addBook(@Valid @ModelAttribute("book") BookRequest bookRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            return "books";
        }
        bookService.addBook(bookMapper.toEntity(bookRequest));
        return "redirect:/books";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable Long id, @Valid @ModelAttribute("book") BookRequest bookRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-book";
        }
        bookService.updateBook(id, bookMapper.toEntity(bookRequest));
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        BookRequest bookRequest = new BookRequest(book.getTitle(), book.getAuthor(), book.getIsbn());
        model.addAttribute("book", bookRequest);
        return "edit-book";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        return "redirect:/books";
    }
}



