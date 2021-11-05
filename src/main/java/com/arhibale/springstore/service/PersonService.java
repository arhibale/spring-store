package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.PersonEntity;
import com.arhibale.springstore.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonEntity save(PersonEntity person) {
        return personRepository.save(person);
    }
}
