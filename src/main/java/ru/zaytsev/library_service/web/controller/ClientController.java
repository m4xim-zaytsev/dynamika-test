package ru.zaytsev.library_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zaytsev.library_service.entity.Client;
import ru.zaytsev.library_service.mapper.ClientMapper;
import ru.zaytsev.library_service.service.ClientService;
import ru.zaytsev.library_service.web.request.ClientRequest;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("client", new ClientRequest());
        return "clients";
    }

    @PostMapping
    public String addClient(@Valid @ModelAttribute("client") ClientRequest clientRequest, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            return "clients";
        }
        clientService.addClient(clientMapper.toEntity(clientRequest));
        return "redirect:/clients";
    }

    @GetMapping("/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        Client client = clientService.getClientById(id);
        ClientRequest clientRequest = new ClientRequest(client.getFullName(), client.getBirthDate());
        model.addAttribute("client", clientRequest);
        return "edit-client";
    }

    @PostMapping("/{id}/edit")
    public String updateClient(@PathVariable Long id, @Valid @ModelAttribute("client") ClientRequest clientRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-client";
        }
        clientService.updateClient(id, clientMapper.toEntity(clientRequest));
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully!");
        return "redirect:/clients";
    }
}

