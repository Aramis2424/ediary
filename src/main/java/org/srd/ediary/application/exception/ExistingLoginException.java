package org.srd.ediary.application.exception;

public class ExistingLoginException extends RuntimeException {
    public ExistingLoginException(String login) {
        super("User with login '" + login + "' has already created");
    }
}
