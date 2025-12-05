package com.hongjisu.pf.contact.controller;

import com.hongjisu.pf.contact.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/contacts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminContactController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public String listContacts(@RequestParam(required = false) Boolean unreadOnly,
                               Model model) {

        if (Boolean.TRUE.equals(unreadOnly)) {
            model.addAttribute("contacts", contactMessageService.findUnread());
        } else {
            model.addAttribute("contacts", contactMessageService.findAll());
        }
        model.addAttribute("unreadCount", contactMessageService.countUnread());

        return "admin/contact/list";
    }

    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id,
                             @RequestParam(required = false) Boolean unreadOnly) {

        contactMessageService.markAsRead(id);
        return "redirect:/admin/contacts" + (Boolean.TRUE.equals(unreadOnly) ? "?unreadOnly=true" : "");
    }
}
