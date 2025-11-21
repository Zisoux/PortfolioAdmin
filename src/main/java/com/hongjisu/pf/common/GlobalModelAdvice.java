package com.hongjisu.pf.common;

import com.hongjisu.pf.contact.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final ContactMessageService contactService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        long unread = contactService.countUnreadContacts();
        model.addAttribute("unreadContactCount", unread);
    }
}
