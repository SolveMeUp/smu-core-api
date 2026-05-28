package com.solvemeup.smucoreapi.domain.judge.problem.controller;

import com.solvemeup.smucoreapi.domain.judge.execution.service.ExecutionService;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionCreateRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionCreateRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionCreateResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionCreateResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ExecutionService executionService;
    private final SubmissionService submissionService;

    @PostMapping("/{problemId}/executions")
    public ResponseEntity<ExecutionCreateResponse> createExecution(@AuthUserId Long userId,
                                                                   @PathVariable Long problemId,
                                                                   @RequestBody ExecutionCreateRequest request) {
        return ResponseEntity.accepted().body(executionService.createExecution(userId, problemId, request));
    }

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<SubmissionCreateResponse> createSubmission(@AuthUserId Long userId,
                                                                     @PathVariable Long problemId,
                                                                     @RequestBody SubmissionCreateRequest request) {
        return ResponseEntity.accepted().body(submissionService.createSubmission(userId, problemId, request));
    }
}
