package com.solvemeup.smucoreapi.domain.judge.submission.reader;

import com.solvemeup.smucoreapi.domain.judge.submission.entity.SubmissionResult;
import com.solvemeup.smucoreapi.domain.judge.submission.exception.SubmissionResultNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.submission.repository.SubmissionResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionResultReader {

    private final SubmissionResultRepository submissionResultRepository;

    public SubmissionResult getSubmissionResult(Long submissionResultId) {
        return submissionResultRepository.findById(submissionResultId)
                .orElseThrow(SubmissionResultNotFoundException::new);
    }
}
