package com.solvemeup.smucoreapi.domain.judge.submission.service;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmitSolutionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmitSolutionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionResultRepository;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.domain.judge.submission.messaging.dto.request.SubmissionRequestMessage;
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
    public SubmitSolutionResponse submit(Long userId, Long ProblemId, SubmitSolutionRequest request) {
        User user = userReader.getUser(userId);
        Problem problem = problemReader.getProblem(ProblemId);

        Submission submission = Submission.create(
                user,
                problem,
                request.language(),
                request.sourceCode()
        );
        submissionRepository.save(submission);

        SubmissionResult submissionResult = SubmissionResult.create(submission);
        submissionResultRepository.save(submissionResult);

        SubmissionRequestMessage message = new SubmissionRequestMessage(
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

        return new SubmitSolutionResponse(submission.getId());
    }
}
