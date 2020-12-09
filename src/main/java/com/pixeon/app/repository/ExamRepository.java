package com.pixeon.app.repository;

import com.pixeon.app.model.Exam;
import com.pixeon.app.model.HealthCareInstitution;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value   ="10000")})
    Optional<Exam> findFirstByIdAndHealthCareInstitution(Long id, HealthCareInstitution hci);

    @Override
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value   ="10000")})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Exam save(Exam exam);
}
