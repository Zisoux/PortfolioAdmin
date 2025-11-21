package com.hongjisu.pf.contact.controller;

import com.hongjisu.pf.contact.dto.ContactMessageForm;
import com.hongjisu.pf.contact.entity.ContactMessage;
import com.hongjisu.pf.contact.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

    // 폼이 보내는 경로: /contact
    @PostMapping("/contact")
    @ResponseBody
    public ResponseEntity<?> submitContact(@Valid @ModelAttribute ContactMessageForm form) {

        ContactMessage message = new ContactMessage();
        message.setName(form.getName());
        message.setEmail(form.getEmail());
        message.setMessage(form.getMessage());
        contactMessageService.save(message);

        // 프론트에는 JSON으로 성공 응답
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "감사합니다!"
        ));
    }
}
