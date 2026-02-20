package com.solvemeup.smucoreapi.domain.judge.problem.repository;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleCaseRepository extends JpaRepository<SampleCase, Long> {

    List<SampleCase> findByProblemIdOrderByOrderIndexAsc(Long problemId);
}
