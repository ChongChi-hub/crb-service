package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.request.ContactRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateContactRequestDto;
import com.crb.seaturtlewebservice.domain.entity.Contact;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.ContactRepository;
import com.crb.seaturtlewebservice.service.ContactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public ResponsePayload getContacts() {
        LOGGER.info("ContactServiceImpl -> get all contact invoked");
        try {
            Iterable<Contact> contacts = contactRepository.findAll();
            return ResponsePayload.builder()
                    .data(contacts)
                    .status(HttpStatus.OK)
                    .message("Find all data of categoy")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Not find data of category")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload addContact(ContactRequestDto contactRequestDto) {
        LOGGER.info("ContactServiceImpl -> Add new contact invoked");
        try {
            Contact contact = new Contact();
            contact.setPhoneNumber(contactRequestDto.getPhoneNumber());
            contact.setDescription(contactRequestDto.getDescription());
            Contact newContact = contactRepository.save(contact);
            return ResponsePayload.builder()
                    .data(newContact)
                    .message("Save contact success")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Save contact fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload updateContact(UpdateContactRequestDto updateContactRequestDto) {
        LOGGER.info("ContactServiceImpl -> Update contact invoked");
        try {
            Contact contact = contactRepository.findById(updateContactRequestDto.getId()).orElse(null);
            if (contact != null) {
                contact.setPhoneNumber(updateContactRequestDto.getPhoneNumber());
                contact.setDescription(updateContactRequestDto.getDescription());
                Contact updateContact = contactRepository.save(contact);
                return ResponsePayload.builder()
                        .data(updateContact)
                        .message("Update contact success")
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ResponsePayload.builder()
                        .message("Contact not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Update contact fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteContactList(DeleteListRequestDto deleteListRequestDto) {
        LOGGER.info("ContactServiceImpl -> Delete list contact invoked");
        try {
            List<Long> ids = deleteListRequestDto.getId();
            contactRepository.deleteAllByIdInBatch(ids);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete list contact successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete contact list fail")
                    .error(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @Override
    public ResponsePayload deleteContactById(Long id) {
        LOGGER.info("ContactServiceImpl -> Delete contact with id: " + id + " invoked");
        try {
            contactRepository.deleteById(id);
            return ResponsePayload.builder()
                    .status(HttpStatus.OK)
                    .message("Delete contact with id: " + id + "successful")
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Delete contact fail")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }
}
