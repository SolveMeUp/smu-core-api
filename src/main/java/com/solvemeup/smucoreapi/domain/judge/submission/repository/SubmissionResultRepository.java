package com.solvemeup.smucoreapi.domain.judge.submission.repository;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionCaseResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionResultRepository extends JpaRepository<SubmissionCaseResult, Long> {
}
