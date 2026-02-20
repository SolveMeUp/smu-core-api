package com.solvemeup.smucoreapi.domain.judge.run.repository;

import com.solvemeup.smucoreapi.domain.judge.run.entity.RunResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RunResultRepository extends JpaRepository<RunResult, Long> {

    @Query("""
                select r
                from RunResult r
                where r.run.id = :runId
                order by r.caseIndex asc
            """)
    List<RunResult> findResults(Long runId);

    boolean existsByRunIdAndCaseIndex(Long runId, int caseIndex);

    long countByRunId(Long runId);
}
