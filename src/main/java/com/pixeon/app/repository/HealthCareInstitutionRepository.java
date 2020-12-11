package com.pixeon.app.repository;

import com.pixeon.app.model.HealthCareInstitution;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface HealthCareInstitutionRepository extends CrudRepository<HealthCareInstitution, Long> {

    Optional<HealthCareInstitution> findFirstByCNPJAndName(String cnpj, String name);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value   ="10000")})
    HealthCareInstitution save(HealthCareInstitution healthCareInstitution);

}
