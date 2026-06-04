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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            return;
        }

        if (submission.getStatus().isDone()) {
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
    }
}
