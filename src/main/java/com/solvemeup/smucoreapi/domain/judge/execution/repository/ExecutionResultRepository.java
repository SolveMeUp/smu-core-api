package com.solvemeup.smucoreapi.domain.judge.execution.repository;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExecutionResultRepository extends JpaRepository<ExecutionResult, Long> {

    @Query("""
                select r
                from ExecutionResult r
                where r.execution.id = :executionId
                order by r.caseIndex asc
            """)
    List<ExecutionResult> findResults(Long executionId);

    boolean existsByExecutionIdAndCaseIndex(Long executionId, int caseIndex);

    long countByExecutionId(Long executionId);
}
