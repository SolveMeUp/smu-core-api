package com.solvemeup.smucoreapi.domain.submission.repository;

import com.solvemeup.smucoreapi.domain.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
