package com.solvemeup.smucoreapi.domain.submission.repository;

import com.solvemeup.smucoreapi.domain.submission.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeRepository extends JpaRepository<Judge, Long> {
}
