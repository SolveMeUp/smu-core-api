package com.solvemeup.smucoreapi.domain.judge.execution.repository;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
}
