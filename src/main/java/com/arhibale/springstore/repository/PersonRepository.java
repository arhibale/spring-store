package com.arhibale.springstore.repository;

import com.arhibale.springstore.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PersonRepository extends CrudRepository<PersonEntity, UUID> {
}
