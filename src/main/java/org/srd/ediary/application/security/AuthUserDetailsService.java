package org.srd.ediary.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {
    private final OwnerRepository ownerRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Owner> optionalOwner = ownerRepo.getByLogin(username);
        if (optionalOwner.isEmpty())
            throw new UsernameNotFoundException(String.format("User not found, or unauthorized %s", username));
        Owner owner = optionalOwner.get();
        return new OwnerDetails(owner.getLogin(), owner.getPassword(), Collections.emptyList(), owner.getId());
    }
}
