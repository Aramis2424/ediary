package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.InvalidCredentialsException;
import org.srd.ediary.application.exception.OwnerAlreadyExistException;
import org.srd.ediary.application.mapper.OwnerMapper;
import org.srd.ediary.domain.model.Owner;
import org.srd.ediary.domain.repository.OwnerRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepo;

    @PreAuthorize("#ownerId.equals(authentication.principal.id)")
    public OwnerInfoDTO fetchOwner(Long ownerId) {
        Optional<Owner> optionalOwner = ownerRepo.getByID(ownerId);
        if (optionalOwner.isEmpty()) {
            throw new InvalidCredentialsException("Authorised error");
        }
        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(optionalOwner.get());
    }

    public OwnerInfoDTO loginOwner(String login, String password) {
        Optional<Owner> optionalOwner = ownerRepo.getByLogin(login);
        if (optionalOwner.isEmpty() ||
                !passwordEncoder.matches(password, optionalOwner.get().getPassword())) {
            throw new InvalidCredentialsException("Incorrect login or password");
        }
        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(optionalOwner.get());
    }

    public OwnerInfoDTO registerOwner(OwnerCreateDTO dto) {
        Owner owner = new Owner(dto.name(), dto.birthDate(), dto.login(), passwordEncoder.encode(dto.password()));

        if (ownerRepo.getByLogin(owner.getLogin()).isPresent())
            throw new OwnerAlreadyExistException(owner.getLogin());
        owner = ownerRepo.save(owner);

        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(owner);
    }
}
