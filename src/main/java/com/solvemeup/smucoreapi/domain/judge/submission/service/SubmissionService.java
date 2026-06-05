package com.solvemeup.smucoreapi.domain.judge.submission.service;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.exception.SubmissionNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request.SubmissionParameter;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request.SubmissionRequestMessage;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.result.SubmissionResultMessage;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.producer.SubmissionRequestProducer;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionRepository;
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
public class SubmissionService {

    private final UserReader userReader;
    private final ProblemReader problemReader;

    private final SubmissionRepository submissionRepository;

    private final SubmissionRequestProducer submissionRequestProducer;

    @Transactional
    public SubmissionResponse submit(Long userId, SubmissionRequest request) {
        log.info("제출 채점 요청 수신 userId={} problemId={} language={}",
                userId, request.problemId(), request.language());

        User user = userReader.getUser(userId);
        Problem problem = problemReader.getPublishedProblem(request.problemId());

        Submission submission = Submission.create(
                user,
                problem,
                request.language(),
                request.sourceCode()
        );
        submissionRepository.save(submission);

        SubmissionRequestMessage message = buildRequestMessage(submission, problem, request);
        submissionRequestProducer.send(message);

        log.info("제출 채점 요청 발행 submissionId={} userId={} problemId={} language={}",
                submission.getId(), userId, problem.getId(), request.language());

        return new SubmissionResponse(submission.getId());
    }

    private SubmissionRequestMessage buildRequestMessage(Submission submission, Problem problem, SubmissionRequest request) {
        List<SubmissionParameter> parameters = problem.getParameters().stream()
                .map(p -> new SubmissionParameter(p.name(), p.type()))
                .toList();

        return new SubmissionRequestMessage(
                submission.getId(),
                problem.getId(),
                problem.getFunctionName(),
                parameters,
                problem.getReturnType(),
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                request.language(),
                request.sourceCode()
        );
    }

    public SubmissionStatusResponse getSubmissionStatus(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(SubmissionNotFoundException::new);

        return SubmissionStatusResponse.from(submission);
    }

    @Transactional
    public void applyResult(SubmissionResultMessage message) {
        Submission submission = submissionRepository.findById(message.submissionId())
                .orElse(null);
        if (submission == null) {
            log.warn("제출 결과 무시: 알 수 없는 submissionId={}", message.submissionId());
            return;
        }

        if (submission.getStatus().isDone()) {
            log.debug("제출 결과 중복 무시(이미 완료) submissionId={}", message.submissionId());
            return;
        }

        submission.markDone(
                message.verdict(),
                message.failedCaseIndex(),
                message.arguments(),
                message.expectedOutput(),
                message.actualOutput(),
                message.timeUsedMillis(),
                message.memoryUsedKilobytes()
        );

        long elapsedMs = Duration.between(submission.getCreatedAt(), submission.getFinishedAt()).toMillis();
        log.info("제출 채점 완료 submissionId={} verdict={} elapsedMs={}",
                submission.getId(), message.verdict(), elapsedMs);
    }
}
