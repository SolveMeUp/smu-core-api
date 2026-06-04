package com.solvemeup.smucoreapi.domain.judge.submission.service;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResultResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.exception.SubmissionResultNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionResultRepository;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request.SubmissionMessage;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.producer.SubmissionRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubmissionService {

    private final UserReader userReader;
    private final ProblemReader problemReader;

    private final SubmissionRepository submissionRepository;
    private final SubmissionResultRepository submissionResultRepository;

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

        SubmissionResult submissionResult = SubmissionResult.create(submission);
        submissionResultRepository.save(submissionResult);

        SubmissionMessage message = new SubmissionMessage(
                submissionResult.getId(),
                submission.getId(),
                problem.getId(),
                problem.getFunctionName(),
                problem.getParameters(),
                problem.getReturnType(),
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                request.language(),
                request.sourceCode()
        );

        submissionRequestProducer.send(message);

        return new SubmissionResponse(submission.getId());
    }

    public SubmissionResultResponse getResult(Long submissionId) {
        SubmissionResult submissionResult = submissionResultRepository.findBySubmissionId(submissionId)
                .orElseThrow(SubmissionResultNotFoundException::new);
        return SubmissionResultResponse.from(submissionResult);
    }
}
