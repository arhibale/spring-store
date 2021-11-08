package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends CrudRepository<PersonEntity, UUID> {
    @NonNull
    List<PersonEntity> findAll();
    Optional<PersonEntity> findByKeycloakId(UUID keycloakId);
}
