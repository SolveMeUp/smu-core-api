package com.solvemeup.smucoreapi.domain.judge.execution.service;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.entity.ExecutionResult;
import com.solvemeup.smucoreapi.domain.judge.execution.exception.ExecutionNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionParameter;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionRequestMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.request.ExecutionTestCase;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.dto.result.ExecutionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.execution.messaging.producer.ExecutionRequestProducer;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionRepository;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionResultRepository;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExecutionService {

    private final UserReader userReader;
    private final ProblemReader problemReader;

    private final ExecutionRepository executionRepository;
    private final ExecutionResultRepository executionResultRepository;

    private final ExecutionRequestProducer executionRequestProducer;

    @Transactional
    public ExecutionResponse execute(Long userId, ExecutionRequest request) {
        log.info("실행 채점 요청 수신 userId={} problemId={} language={}",
                userId, request.problemId(), request.language());

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

        ExecutionRequestMessage message = buildRequestMessage(execution, problem, sampleCases, request);
        executionRequestProducer.send(message);

        log.info("실행 채점 요청 발행 executionId={} userId={} problemId={} language={} caseCount={}",
                execution.getId(), userId, problemId, request.language(), sampleCases.size());

        return new ExecutionResponse(execution.getId());
    }

    private ExecutionRequestMessage buildRequestMessage(
            Execution execution,
            Problem problem,
            List<SampleCase> sampleCases,
            ExecutionRequest request
    ) {
        List<ExecutionParameter> parameters = problem.getParameters().stream()
                .map(p -> new ExecutionParameter(p.name(), p.type()))
                .toList();

        List<ExecutionTestCase> testCases = sampleCases.stream()
                .map(tc -> new ExecutionTestCase(
                        tc.getOrderIndex(),
                        tc.getArguments(),
                        tc.getExpectedOutput()
                ))
                .toList();

        return new ExecutionRequestMessage(
                execution.getId(),
                problem.getId(),
                problem.getFunctionName(),
                parameters,
                problem.getReturnType(),
                testCases,
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                request.language(),
                request.sourceCode()
        );
    }

    public ExecutionStatusResponse getExecutionStatus(Long executionId) {
        Execution execution = executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException::new);

        List<ExecutionResult> results = executionResultRepository
                .findByExecutionIdOrderByCaseIndexAsc(executionId);

        return ExecutionStatusResponse.from(execution, results);
    }

    @Transactional
    public void applyResult(ExecutionResultMessage message) {
        Execution execution = executionRepository.findById(message.executionId())
                .orElse(null);
        if (execution == null) {
            log.warn("실행 결과 무시: 알 수 없는 executionId={} caseIndex={}",
                    message.executionId(), message.caseIndex());
            return;
        }

        if (executionResultRepository.existsByExecutionIdAndCaseIndex(
                message.executionId(),
                message.caseIndex())) {
            log.debug("실행 결과 중복 무시 executionId={} caseIndex={}",
                    message.executionId(), message.caseIndex());
            return;
        }

        ExecutionResult result = ExecutionResult.create(
                execution,
                message.caseIndex(),
                message.verdict(),
                message.arguments(),
                message.expectedOutput(),
                message.actualOutput(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );
        executionResultRepository.save(result);

        long completedCount = executionResultRepository.countByExecutionId(message.executionId());
        if (completedCount >= execution.getTotalCaseCount()) {
            execution.markDone();
            long elapsedMs = Duration.between(execution.getCreatedAt(), execution.getFinishedAt()).toMillis();
            log.info("실행 채점 완료 executionId={} caseCount={} elapsedMs={}",
                    execution.getId(), execution.getTotalCaseCount(), elapsedMs);
        }
    }
}
