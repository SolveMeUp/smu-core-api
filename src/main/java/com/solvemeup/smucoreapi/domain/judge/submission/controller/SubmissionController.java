package com.solvemeup.smucoreapi.domain.judge.submission.controller;

import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmitSolutionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResultResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmitSolutionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmitSolutionResponse> submit(@AuthUserId Long userId,
                                                         @RequestBody SubmitSolutionRequest request) {
        return ResponseEntity.accepted().body(submissionService.submit(userId, request));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResultResponse> getResult(@PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getResult(submissionId));
    }
}
