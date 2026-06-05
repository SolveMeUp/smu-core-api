package com.solvemeup.smucoreapi.domain.judge.problem.reader;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.judge.problem.exception.ProblemNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.problem.repository.ProblemRepository;
import com.solvemeup.smucoreapi.domain.judge.problem.repository.SampleCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.solvemeup.smucoreapi.domain.judge.problem.entity.ProblemStatus.*;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemReader {

    private final ProblemRepository problemRepository;
    private final SampleCaseRepository sampleCaseRepository;

    public Problem getProblem(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(ProblemNotFoundException::new);
    }

    public Problem getPublishedProblem(Long problemId) {
        return problemRepository.findByIdAndStatus(problemId, PUBLISHED)
                .orElseThrow(ProblemNotFoundException::new);
    }

    public List<SampleCase> getSampleCases(Long problemId) {
        return sampleCaseRepository.findByProblemIdOrderByOrderIndexAsc(problemId);
    }
}
