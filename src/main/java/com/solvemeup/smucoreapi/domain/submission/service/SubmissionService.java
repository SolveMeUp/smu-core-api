package com.solvemeup.smucoreapi.domain.submission.service;

import com.solvemeup.smucoreapi.domain.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.problem.reader.ProblemReader;
import com.solvemeup.smucoreapi.domain.submission.dto.request.SubmitSolutionRequest;
import com.solvemeup.smucoreapi.domain.submission.dto.response.SubmitSolutionResponse;
import com.solvemeup.smucoreapi.domain.submission.entity.Judge;
import com.solvemeup.smucoreapi.domain.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.submission.repository.JudgeRepository;
import com.solvemeup.smucoreapi.domain.submission.repository.SubmissionRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.domain.submission.messaging.dto.request.JudgeRequestMessage;
import com.solvemeup.smucoreapi.domain.submission.messaging.producer.JudgeRequestProducer;
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
    private final JudgeRepository judgeRepository;

    private final JudgeRequestProducer judgeRequestProducer;

    @Transactional
    public SubmitSolutionResponse submit(Long userId, Long ProblemId, SubmitSolutionRequest request) {
        User user = userReader.getUser(userId);
        Problem problem = problemReader.getProblem(ProblemId);

        Submission submission = Submission.createSubmission(
                problem,
                user,
                request.language(),
                request.sourceCode()
        );
        submissionRepository.save(submission);

        Judge judge = Judge.createJudge(submission);
        judgeRepository.save(judge);

        JudgeRequestMessage message = new JudgeRequestMessage(
                judge.getId(),
                submission.getId(),
                problem.getId(),
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitMegabytes(),
                request.language(),
                request.sourceCode()
        );

        judgeRequestProducer.send(message);

        return new SubmitSolutionResponse(submission.getId());
    }
}
