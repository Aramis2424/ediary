package org.srd.ediary.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class OwnerNotFoundException extends EntityNotFoundException {
    public OwnerNotFoundException(String message) {
        super(message);
    }
}
