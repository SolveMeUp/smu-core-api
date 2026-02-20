package com.solvemeup.smucoreapi.domain.judge.submission.repository;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionResultRepository extends JpaRepository<SubmissionResult, Long> {
}
