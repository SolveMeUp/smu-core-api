package com.solvemeup.smucoreapi.domain.judge.run.service;

import com.solvemeup.smucoreapi.domain.judge.run.dto.request.RunRequest;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.SampleCaseResult;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.RunResponse;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.RunStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.run.entity.Run;
import com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request.RunMessage;
import com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request.RunSampleCase;
import com.solvemeup.smucoreapi.domain.judge.run.messaging.dto.request.ParameterSpec;
import com.solvemeup.smucoreapi.domain.judge.run.messaging.producer.RunRequestProducer;
import com.solvemeup.smucoreapi.domain.judge.run.repository.RunRepository;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.problem.repository.SampleCaseRepository;
import com.solvemeup.smucoreapi.domain.judge.run.repository.RunResultRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RunService {

    private final UserReader userReader;
    private final ProblemReader problemReader;

    private final SampleCaseRepository sampleCaseRepository;

    private final RunRepository runRepository;
    private final RunResultRepository runResultRepository;

    private final RunRequestProducer producer;

    @Transactional
    public RunResponse execute(Long userId, Long problemId, RunRequest request) {
        User user = userReader.getUser(userId);
        Problem problem = problemReader.getProblem(problemId);

        List<SampleCase> sampleCases =
                sampleCaseRepository.findByProblemIdOrderByOrderIndexAsc(problemId);

        Run run = Run.create(
                user,
                problem,
                request.language(),
                request.sourceCode(),
                sampleCases.size()
        );
        runRepository.save(run);

        List<ParameterSpec> parameterSpecs = problem.getParameters().stream()
                .map(p -> new ParameterSpec(p.name(), p.type().name()))
                .toList();

        List<RunSampleCase> testCases = sampleCases.stream()
                .map(tc -> new RunSampleCase(
                        tc.getOrderIndex(),
                        tc.getArgumentsJson()
                ))
                .toList();

        RunMessage message = new RunMessage(
                run.getId(),
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

        return new RunResponse(run.getId());
    }

    public RunStatusResponse getRunStatus(Long executionId) {

        Run run = runRepository.findById(executionId)
                .orElseThrow();

        var results = runResultRepository
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

        return new RunStatusResponse(
                run.getStatus().name(),
                results
        );
    }
}
