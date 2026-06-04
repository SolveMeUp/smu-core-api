package com.solvemeup.smucoreapi.domain.judge.run.controller;

import com.solvemeup.smucoreapi.domain.judge.run.dto.request.RunRequest;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.RunResponse;
import com.solvemeup.smucoreapi.domain.judge.run.dto.response.RunStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.run.service.RunService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runs")
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    @PostMapping
    public ResponseEntity<RunResponse> execute(@AuthUserId Long userId,
                                               @RequestBody RunRequest request) {
        return ResponseEntity.accepted().body(runService.execute(userId, request));
    }

    @GetMapping("/{runId}")
    public ResponseEntity<RunStatusResponse> getRunStatus(@PathVariable Long runId) {
        return ResponseEntity.ok(runService.getRunStatus(runId));
    }
}
