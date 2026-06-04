package com.solvemeup.smucoreapi.domain.judge.execution.repository;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecutionResultRepository extends JpaRepository<ExecutionResult, Long> {

    List<ExecutionResult> findByExecutionIdOrderByCaseIndexAsc(Long executionId);

    boolean existsByExecutionIdAndCaseIndex(Long executionId, int caseIndex);

    long countByExecutionId(Long executionId);
}
