package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.ContactRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.DeleteListRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.UpdateContactRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface ContactService {
    ResponsePayload getContacts();

    ResponsePayload addContact(ContactRequestDto contactRequestDto);

    ResponsePayload updateContact(UpdateContactRequestDto updateContactRequestDto);

    ResponsePayload deleteContactList(DeleteListRequestDto deleteListRequestDto);

    ResponsePayload deleteContactById(Long id);
}
