package org.srd.ediary.application.exception;

public class OwnerAlreadyExistException extends AuthenticationException {
    public OwnerAlreadyExistException(String login) {
        super("User with login '" + login + "' has already created");
    }
}
