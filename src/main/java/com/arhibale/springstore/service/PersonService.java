package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.exception.PersonNotFoundException;
import com.arhibale.springstore.integration.KeycloakIntegration;
import com.arhibale.springstore.repository.PersonRepository;
import com.arhibale.springstore.util.DecodeJwtToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakIntegration keycloakIntegration;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, KeycloakIntegration keycloakIntegration) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloakIntegration = keycloakIntegration;
    }


    public PersonEntity save(PersonEntity person) {
        keycloakIntegration.createUser(person);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setKeycloakId(UUID.fromString((String) DecodeJwtToken.decode("sub")));
        return personRepository.save(person);
    }

    public PersonEntity findByKeycloakId(UUID keycloakId) {
        return personRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new PersonNotFoundException("Пользователь с таким id не найден: " + keycloakId));
    }
}
