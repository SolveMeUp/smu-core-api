package com.solvemeup.smucoreapi.domain.judge.execution.service;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionCreateRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionCreateResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionDetailResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionCaseResult;
import com.solvemeup.smucoreapi.domain.judge.execution.exception.AccessDeniedExecutionException;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionRequestMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.producer.ExecutionRequestProducer;
import com.solvemeup.smucoreapi.domain.judge.execution.reader.ExecutionReader;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionRepository;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleTestCase;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.problem.repository.SampleTestCaseRepository;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionCaseResultRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExecutionService {

    private final UserReader userReader;
    private final ProblemReader problemReader;
    private final ExecutionReader executionReader;

    private final ExecutionRepository executionRepository;
    private final ExecutionCaseResultRepository executionCaseResultRepository;
    private final SampleTestCaseRepository sampleTestCaseRepository;

    private final ExecutionRequestProducer producer;

    @Transactional
    public ExecutionCreateResponse createExecution(Long userId, Long problemId, ExecutionCreateRequest request) {
        User user = userReader.getUser(userId);
        Problem problem = problemReader.getProblem(problemId);

        List<SampleTestCase> sampleTestCases =
                sampleTestCaseRepository.findByProblemId(problemId);

        Execution execution = Execution.create(
                user,
                problem,
                request.language(),
                request.sourceCode(),
                sampleTestCases.size()
        );
        executionRepository.save(execution);

        ExecutionRequestMessage message =
                ExecutionRequestMessage.from(execution, problem, sampleTestCases);

        producer.send(message);

        return new ExecutionCreateResponse(execution.getId());
    }

    public ExecutionDetailResponse getExecutionDetail(Long userId, Long executionId) {
        Execution execution = executionReader.getExecution(executionId);

        if (!execution.getUser().getId().equals(userId)) {
            throw new AccessDeniedExecutionException();
        }

        List<ExecutionCaseResult> results = executionCaseResultRepository
                .findByExecutionIdOrderByCaseIndex(executionId);

        return ExecutionDetailResponse.from(execution, results);
    }
}
