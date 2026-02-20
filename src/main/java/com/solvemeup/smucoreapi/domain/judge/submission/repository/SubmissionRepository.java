package com.solvemeup.smucoreapi.domain.judge.submission.repository;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
