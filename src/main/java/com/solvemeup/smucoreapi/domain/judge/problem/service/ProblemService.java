package com.solvemeup.smucoreapi.domain.judge.problem.service;

import com.solvemeup.smucoreapi.domain.judge.problem.dto.response.ProblemResponse;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemReader problemReader;

    public ProblemResponse getProblem(Long problemId) {
        Problem problem = problemReader.getPublishedProblem(problemId);
        List<SampleCase> sampleCases = problemReader.getSampleCases(problemId);
        return ProblemResponse.from(problem, sampleCases);
    }
}
