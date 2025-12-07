package com.hongjisu.pf.contact.service;

import com.hongjisu.pf.contact.entity.ContactMessage;
import com.hongjisu.pf.contact.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ContactMessage> findAll() {
        return contactMessageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ContactMessage> findUnread() {
        return contactMessageRepository.findByReadFlagFalse();
    }

    @Transactional(readOnly = true)
    public long countUnread() {
        return contactMessageRepository.countByReadFlagFalse();
    }

    public void markAsRead(Long id) {
        ContactMessage msg = contactMessageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found: " + id));
        msg.setReadFlag(true);
    }

    public long countUnreadContacts() {
        return contactMessageRepository.countByReadFlagFalse();
    }

    @Transactional
    public void deleteAllByIds(List<Long> contactIds) {
        if (contactIds == null || contactIds.isEmpty()) return;

        List<Long> ids = contactIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (ids.isEmpty()) return;

        contactMessageRepository.deleteAllByIdInBatch(ids);
    }
}
