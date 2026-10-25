package com.crb.seaturtlewebservice.controller;

import com.crb.seaturtlewebservice.domain.dto.request.ContactRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateContactRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @GetMapping
    private ResponseEntity<ResponsePayload> getContacts(){
        ResponsePayload responsePayload = contactService.getContacts();
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
    @PostMapping
    private ResponseEntity<ResponsePayload> addContact(@RequestBody ContactRequestDto contactRequestDto) {
        ResponsePayload responsePayload = contactService.addContact(contactRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
    @PutMapping
    private ResponseEntity<ResponsePayload> updateContact(@RequestBody UpdateContactRequestDto updateContactRequestDto) {
        ResponsePayload responsePayload = contactService.updateContact(updateContactRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping
    private ResponseEntity<ResponsePayload> deleteContactList(@RequestBody DeleteListRequestDto deleteListRequestDto) {
        ResponsePayload responsePayload = contactService.deleteContactList(deleteListRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @DeleteMapping("{id}")
    private ResponseEntity<ResponsePayload> deleteContactById(@PathVariable Long id) {
        ResponsePayload responsePayload = contactService.deleteContactById(id);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}
