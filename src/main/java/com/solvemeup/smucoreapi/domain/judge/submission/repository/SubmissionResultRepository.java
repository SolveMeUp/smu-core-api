package com.solvemeup.smucoreapi.domain.judge.submission.repository;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionResultRepository extends JpaRepository<SubmissionResult, Long> {

    Optional<SubmissionResult> findBySubmissionId(Long submissionId);
}
