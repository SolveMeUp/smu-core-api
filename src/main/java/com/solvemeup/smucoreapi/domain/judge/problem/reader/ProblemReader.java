package com.solvemeup.smucoreapi.domain.judge.problem.reader;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.exception.ProblemNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemReader {

    private final ProblemRepository problemRepository;

    public Problem getProblem(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(ProblemNotFoundException::new);
    }
}
