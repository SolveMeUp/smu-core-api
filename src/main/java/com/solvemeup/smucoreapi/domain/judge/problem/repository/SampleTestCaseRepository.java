package com.solvemeup.smucoreapi.domain.judge.problem.repository;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleTestCaseRepository extends JpaRepository<SampleTestCase, Long> {

    List<SampleTestCase> findByProblemId(Long problemId);
}
