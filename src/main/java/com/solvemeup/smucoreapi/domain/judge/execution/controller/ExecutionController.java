package com.solvemeup.smucoreapi.domain.judge.execution.controller;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.service.ExecutionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping
    public ResponseEntity<ExecutionResponse> execute(@AuthUserId Long userId,
                                                     @RequestBody ExecutionRequest request) {
        return ResponseEntity.accepted().body(executionService.execute(userId, request));
    }

    @GetMapping("/{executionId}")
    public ResponseEntity<ExecutionStatusResponse> getExecutionStatus(@PathVariable Long executionId) {
        return ResponseEntity.ok(executionService.getExecutionStatus(executionId));
    }
}
