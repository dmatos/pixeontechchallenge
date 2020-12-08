package com.pixeon.app.repository;

import com.pixeon.app.model.Budget;
import com.pixeon.app.model.HealthCareInstitution;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface BudgetRepository extends CrudRepository<Budget, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value   ="10000")})
    Optional<Budget> findByHealthCareInstitution(HealthCareInstitution hci);

}
