package com.solvemeup.smucoreapi.domain.problem.repository;

import com.solvemeup.smucoreapi.domain.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
