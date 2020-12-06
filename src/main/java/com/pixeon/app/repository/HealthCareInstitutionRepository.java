package com.pixeon.app.repository;

import com.pixeon.app.model.HealthCareInstitution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthCareInstitutionRepository extends CrudRepository<HealthCareInstitution, Long> {

    Optional<HealthCareInstitution> findFirstByCNPJ(String cnpj);
}
