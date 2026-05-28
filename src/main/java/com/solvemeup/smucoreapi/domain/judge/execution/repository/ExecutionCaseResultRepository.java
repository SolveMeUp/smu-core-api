package com.solvemeup.smucoreapi.domain.judge.execution.repository;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionCaseResultRepository extends JpaRepository<ExecutionCaseResult, Long> {

    List<ExecutionCaseResult> findByExecutionIdOrderByCaseIndex(Long executionId);

    long countByExecutionId(Long executionId);

    boolean existsByExecutionIdAndCaseIndex(Long executionId, int caseIndex);
}
