package org.srd.ediary.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class EntryNotFoundException extends EntityNotFoundException {
    public EntryNotFoundException(String message) {
        super(message);
    }
}
