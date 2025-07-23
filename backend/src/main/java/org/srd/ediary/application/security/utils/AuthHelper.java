package org.srd.ediary.application.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.srd.ediary.application.security.OwnerDetails;

@Component
public class AuthHelper {
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((OwnerDetails) auth.getPrincipal()).getId();
    }

    public OwnerDetails getCurrentUser() {
        return (OwnerDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
