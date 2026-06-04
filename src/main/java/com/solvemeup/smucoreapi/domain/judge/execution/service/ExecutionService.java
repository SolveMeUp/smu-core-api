package com.solvemeup.smucoreapi.domain.judge.execution.service;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.SampleCaseResult;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionSampleCase;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ParameterSpec;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.producer.ExecutionRequestProducer;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionRepository;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionResultRepository;
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

    private final ExecutionRepository executionRepository;
    private final ExecutionResultRepository executionResultRepository;

    private final ExecutionRequestProducer producer;

    @Transactional
    public ExecutionResponse execute(Long userId, ExecutionRequest request) {
        User user = userReader.getUser(userId);
        Long problemId = request.problemId();
        Problem problem = problemReader.getPublishedProblem(problemId);

        List<SampleCase> sampleCases = problemReader.getSampleCases(problemId);

        Execution execution = Execution.create(
                user,
                problem,
                request.language(),
                request.sourceCode(),
                sampleCases.size()
        );
        executionRepository.save(execution);

        List<ParameterSpec> parameterSpecs = problem.getParameters().stream()
                .map(p -> new ParameterSpec(p.name(), p.type().name()))
                .toList();

        List<ExecutionSampleCase> testCases = sampleCases.stream()
                .map(tc -> new ExecutionSampleCase(
                        tc.getOrderIndex(),
                        tc.getArgumentsJson()
                ))
                .toList();

        ExecutionMessage message = new ExecutionMessage(
                execution.getId(),
                problem.getFunctionName(),
                parameterSpecs,
                problem.getReturnType().name(),
                testCases,
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                request.language().name(),
                request.sourceCode()
        );

        producer.send(message);

        return new ExecutionResponse(execution.getId());
    }

    public ExecutionStatusResponse getExecutionStatus(Long executionId) {

        Execution execution = executionRepository.findById(executionId)
                .orElseThrow();

        var results = executionResultRepository
                .findResults(executionId)
                .stream()
                .map(r -> new SampleCaseResult(
                        r.getCaseIndex(),
                        r.getStatus().name(),
                        r.getExpectedOutput(),
                        r.getActualOutput(),
                        r.getTimeUsedMillis(),
                        r.getMemoryUsedKilobytes()
                ))
                .toList();

        return new ExecutionStatusResponse(
                execution.getStatus().name(),
                results
        );
    }
}
