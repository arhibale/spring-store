package com.arhibale.springstore.service;

import com.arhibale.springstore.entity.Person;
import com.arhibale.springstore.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonById(UUID id) {
        return personRepository.findById(id)
                .orElseThrow();
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }
}
