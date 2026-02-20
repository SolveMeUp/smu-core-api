package com.solvemeup.smucoreapi.domain.judge.run.repository;

import com.solvemeup.smucoreapi.domain.judge.run.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunRepository extends JpaRepository<Run, Long> {
}
