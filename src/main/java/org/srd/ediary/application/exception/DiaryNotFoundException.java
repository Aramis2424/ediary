package org.srd.ediary.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class DiaryNotFoundException extends EntityNotFoundException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
}
