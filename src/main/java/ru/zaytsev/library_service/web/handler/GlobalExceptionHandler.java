package ru.zaytsev.library_service.web.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zaytsev.library_service.exceptions.ActiveRentalException;
import ru.zaytsev.library_service.exceptions.DuplicateEntityException;
import ru.zaytsev.library_service.exceptions.EntityNotFoundException;
import ru.zaytsev.library_service.exceptions.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return determineRedirectPath(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public String handleDuplicateEntityException(DuplicateEntityException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return determineRedirectPath(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public String handleValidationException(ValidationException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return determineRedirectPath(ex.getMessage());
    }

    @ExceptionHandler(ActiveRentalException.class)
    public String handleActiveRentalException(ActiveRentalException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return determineRedirectPath(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpectedException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Page not found: " + ex.getRequestURL());
        return "error";
    }

    private String determineRedirectPath(String errorMessage) {
        if (errorMessage.contains("Book")) {
            return "redirect:/books";
        } else if (errorMessage.contains("Client")) {
            return "redirect:/clients";
        } else if (errorMessage.contains("Rental")) {
            return "redirect:/rentals";
        }
        return "redirect:/";
    }
}


