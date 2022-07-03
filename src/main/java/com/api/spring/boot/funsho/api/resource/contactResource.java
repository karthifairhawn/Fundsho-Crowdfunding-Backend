package com.api.spring.boot.funsho.api.resource;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.spring.boot.funsho.api.entity.contact.Contact;

@RestController
@CrossOrigin
public class contactResource {
    
    @Autowired
    private com.api.spring.boot.funsho.api.repository.contactRepository contactRepository;

    @GetMapping("/contactinfo")
    public List<Contact> getContactInfo(@RequestParam("sessionKey") String sessionKey) {
        return contactRepository.findAll();
    }

    @PostMapping("/contactinfo")
    public Contact saveContactInfo(Contact contact) {
        contact.setTimestamp(new Date());
        return contactRepository.save(contact);
    }
    
}
