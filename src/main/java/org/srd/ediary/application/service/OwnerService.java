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

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepo;

    public OwnerInfoDTO loginOwner(String login, String password) {
        Owner owner = ownerRepo
                .getByLoginAndPassword(login, passwordEncoder.encode(password)).orElseThrow(() ->
                        new EntityNotFoundException("User not found"));
        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(owner);
    }

    public OwnerInfoDTO registerOwner(OwnerCreateDTO dto) {
        Owner owner = new Owner(dto.name(), dto.birthDate(), dto.login(), passwordEncoder.encode(dto.password()));

        if (ownerRepo.getByLogin(owner.getLogin()).isPresent())
            throw new ExistingLoginException(owner.getLogin());
        ownerRepo.save(owner);

        return OwnerMapper.INSTANCE.OwnerToOwnerInfoDto(owner);
    }
}
