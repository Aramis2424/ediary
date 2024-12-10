package org.srd.ediary.infrastructure.exception;

public class OwnerDeletionRestrictException extends RuntimeException{
    public OwnerDeletionRestrictException() {
        super("Deletion owners is restricted");
    }
}
