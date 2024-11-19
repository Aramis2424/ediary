package org.srd.ediary.application.exception;

public class OwnerNotFoundException extends AuthenticationException {
    public OwnerNotFoundException(String message) {
        super(message);
    }
}
