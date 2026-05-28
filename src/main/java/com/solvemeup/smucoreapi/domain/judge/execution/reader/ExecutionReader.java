package com.solvemeup.smucoreapi.domain.judge.execution.reader;

import com.solvemeup.smucoreapi.domain.judge.execution.entity.Execution;
import com.solvemeup.smucoreapi.domain.judge.execution.exception.ExecutionNotFoundException;
import com.solvemeup.smucoreapi.domain.judge.execution.repository.ExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExecutionReader {

    private final ExecutionRepository executionRepository;

    public Execution getExecution(Long executionId) {
        return executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException::new);
    }
}
