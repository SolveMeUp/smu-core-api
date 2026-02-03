package com.solvemeup.smucoreapi.domain.submission.reader;

import com.solvemeup.smucoreapi.domain.submission.entity.Judge;
import com.solvemeup.smucoreapi.domain.submission.exception.JudgeNotFoundException;
import com.solvemeup.smucoreapi.domain.submission.repository.JudgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JudgeReader {

    private final JudgeRepository judgeRepository;

    public Judge getJudge(Long judgeId) {
        return judgeRepository.findById(judgeId)
                .orElseThrow(JudgeNotFoundException::new);
    }
}
