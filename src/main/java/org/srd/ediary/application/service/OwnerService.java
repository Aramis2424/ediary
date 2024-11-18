package org.srd.ediary.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.application.dto.OwnerCreateDTO;
import org.srd.ediary.application.dto.OwnerInfoDTO;
import org.srd.ediary.application.exception.ExistingLoginException;
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

    public OwnerInfoDTO loginOwner(String login, String password) {
        Optional<Owner> optionalOwner = ownerRepo.getByLogin(login);
        if (optionalOwner.isEmpty() ||
                !passwordEncoder.matches(password, optionalOwner.get().getPassword())) {
            throw new EntityNotFoundException("Incorrect login or password"); // TODO создать отдельное исключение
        }
        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(optionalOwner.get());
    }

    public OwnerInfoDTO registerOwner(OwnerCreateDTO dto) {
        Owner owner = new Owner(dto.name(), dto.birthDate(), dto.login(), passwordEncoder.encode(dto.password()));

        if (ownerRepo.getByLogin(owner.getLogin()).isPresent())
            throw new ExistingLoginException(owner.getLogin());
        owner = ownerRepo.save(owner);

        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(owner);
    }
}
