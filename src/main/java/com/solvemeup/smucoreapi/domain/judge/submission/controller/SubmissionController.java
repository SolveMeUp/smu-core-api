package com.solvemeup.smucoreapi.domain.judge.submission.controller;

import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionDetailResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionDetailResponse> getSubmissionDetail(@AuthUserId Long userId,
                                                                        @PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionDetail(userId, submissionId));
    }
}
