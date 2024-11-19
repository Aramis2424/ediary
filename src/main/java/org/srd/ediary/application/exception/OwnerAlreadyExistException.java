package org.srd.ediary.application.exception;

public class OwnerAlreadyExistException extends RuntimeException {
    public OwnerAlreadyExistException(String login) {
        super("User with login '" + login + "' has already created");
    }
}
