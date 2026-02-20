package com.solvemeup.smucoreapi.domain.judge.problem.repository;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
