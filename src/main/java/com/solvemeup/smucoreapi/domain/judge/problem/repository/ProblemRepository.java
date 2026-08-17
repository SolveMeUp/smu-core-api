package com.solvemeup.smucoreapi.domain.judge.problem.repository;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.ProblemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByIdAndStatus(Long id, ProblemStatus status);

    Page<Problem> findByStatus(ProblemStatus status, Pageable pageable);
}
