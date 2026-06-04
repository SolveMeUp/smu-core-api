package com.solvemeup.smucoreapi.domain.judge.submission.controller;

import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionStatusResponse;
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
    public ResponseEntity<SubmissionResponse> submit(@AuthUserId Long userId,
                                                     @RequestBody SubmissionRequest request) {
        return ResponseEntity.accepted().body(submissionService.submit(userId, request));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionStatusResponse> getSubmissionStatus(@PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionStatus(submissionId));
    }
}
