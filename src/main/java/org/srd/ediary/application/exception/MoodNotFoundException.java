package org.srd.ediary.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class MoodNotFoundException extends EntityNotFoundException {
    public MoodNotFoundException(String message) {
        super(message);
    }
}
