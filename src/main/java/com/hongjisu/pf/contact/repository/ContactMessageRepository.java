package com.hongjisu.pf.contact.repository;

import com.hongjisu.pf.contact.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByReadFlagFalse();

    long countByReadFlagFalse();
}
