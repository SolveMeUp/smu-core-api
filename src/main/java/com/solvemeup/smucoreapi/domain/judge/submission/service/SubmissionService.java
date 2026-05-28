package com.solvemeup.smucoreapi.domain.judge.submission.service;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionCreateRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionCreateResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionDetailResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.exception.AccessDeniedSubmissionException;
import com.solvemeup.smucoreapi.domain.judge.submission.reader.SubmissionReader;
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
    private final SubmissionReader submissionReader;

    private final SubmissionRepository submissionRepository;

    private final SubmissionRequestProducer submissionRequestProducer;

    @Transactional
    public SubmissionCreateResponse createSubmission(Long userId, Long problemId, SubmissionCreateRequest request) {
        User user = userReader.getUser(userId);
        Problem problem = problemReader.getProblem(problemId);

        Submission submission = Submission.create(
                user,
                problem,
                request.language(),
                request.sourceCode()
        );
        submissionRepository.save(submission);

        SubmissionRequestMessage message =
                SubmissionRequestMessage.from(submission, problem);

        submissionRequestProducer.send(message);

        return new SubmissionCreateResponse(submission.getId());
    }

    public SubmissionDetailResponse getSubmissionDetail(Long userId, Long submissionId) {
        Submission submission = submissionReader.getSubmission(submissionId);

        if (!submission.getUser().getId().equals(userId)) {
            throw new AccessDeniedSubmissionException();
        }

        return SubmissionDetailResponse.from(submission);
    }
}
