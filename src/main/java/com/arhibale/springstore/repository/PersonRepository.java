package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends CrudRepository<PersonEntity, UUID> {
    Optional<PersonEntity> findByLogin(String login);
}
