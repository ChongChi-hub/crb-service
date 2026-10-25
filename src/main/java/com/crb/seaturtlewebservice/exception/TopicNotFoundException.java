package com.crb.seaturtlewebservice.exception;

import jakarta.persistence.EntityNotFoundException;

public class TopicNotFoundException extends EntityNotFoundException {
    public TopicNotFoundException(String message) {
        super(message);
    }
}
