package com.solvemeup.smucoreapi.domain.problem.controller;

import com.solvemeup.smucoreapi.domain.submission.dto.request.SubmissionCreateRequest;
import com.solvemeup.smucoreapi.domain.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final SubmissionService submissionService;

    @PostMapping("/{problemId}/submissions")
    public ResponseEntity<Void> submit(@AuthUserId Long userId,
                                       @PathVariable Long problemId,
                                       @RequestBody SubmissionCreateRequest request) {
        submissionService.submit(userId, problemId, request);
        return ResponseEntity.noContent().build();
    }
}
