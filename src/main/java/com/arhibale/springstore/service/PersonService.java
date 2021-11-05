package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.exception.PersonNotFoundException;
import com.arhibale.springstore.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PersonEntity save(PersonEntity person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    public PersonEntity findByLogin(String login) {
        return personRepository.findByLogin(login)
                .orElseThrow(() -> new PersonNotFoundException("Пользователь с таким логином не найден: " + login));
    }
}
