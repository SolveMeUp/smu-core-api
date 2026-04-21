package com.solvemeup.smucoreapi.domain.judge.problem.controller;

import com.solvemeup.smucoreapi.domain.judge.run.service.RunService;
import com.solvemeup.smucoreapi.domain.judge.run.dto.request.RunRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmitSolutionRequest;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.RunResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResultResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmitSolutionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final RunService runService;
    private final SubmissionService submissionService;

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<SubmitSolutionResponse> submit(@AuthUserId Long userId,
                                                         @PathVariable Long problemId,
                                                         @RequestBody SubmitSolutionRequest request) {
        return ResponseEntity.accepted().body(submissionService.submit(userId, problemId, request));
    }

    @GetMapping("/{problemId}/submissions/{submissionId}/result")
    public ResponseEntity<SubmissionResultResponse> getResult(@PathVariable Long problemId,
                                                              @PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getResult(submissionId));
    }

    @PostMapping("/{problemId}/executions")
    public ResponseEntity<RunResponse> execute(@AuthUserId Long userId,
                                               @PathVariable Long problemId,
                                               @RequestBody RunRequest request) {
        return ResponseEntity.accepted().body(runService.execute(userId, problemId, request));
    }
}
