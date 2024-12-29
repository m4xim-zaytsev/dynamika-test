package ru.zaytsev.library_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zaytsev.library_service.entity.Client;
import ru.zaytsev.library_service.service.ClientService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("client", new Client());
        return "clients";
    }

    @PostMapping
    public String addClient(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        clientService.addClient(client);
        redirectAttributes.addFlashAttribute("successMessage", "Client added successfully!");
        return "redirect:/clients";
    }

    @GetMapping("/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "edit-client";
    }

    @PostMapping("/{id}/edit")
    public String updateClient(@PathVariable Long id, @ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        clientService.updateClient(id, client);
        redirectAttributes.addFlashAttribute("successMessage", "Client updated successfully!");
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully!");
        return "redirect:/clients";
    }
}

