package com.solvemeup.smucoreapi.domain.judge.execution.controller;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionDetailResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.service.ExecutionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @GetMapping("/{executionId}")
    public ResponseEntity<ExecutionDetailResponse> getExecutionDetail(@AuthUserId Long userId,
                                                                      @PathVariable Long executionId) {
        return ResponseEntity.ok(executionService.getExecutionDetail(userId, executionId));
    }
}
