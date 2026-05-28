package com.solvemeup.smucoreapi.domain.judge.submission.reader;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.Submission;
import com.solvemeup.smucoreapi.domain.judge.submission.exception.SubmissionNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionReader {

    private final SubmissionRepository submissionRepository;

    public Submission getSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(SubmissionNotFoundException::new);
    }
}
