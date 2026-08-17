package com.solvemeup.smucoreapi.domain.judge.problem.controller;

import com.solvemeup.smucoreapi.domain.judge.problem.dto.response.ProblemResponse;
import com.solvemeup.smucoreapi.domain.judge.problem.dto.response.ProblemSummaryResponse;
import com.solvemeup.smucoreapi.domain.judge.problem.service.ProblemService;
import com.solvemeup.smucoreapi.global.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Problem", description = "문제 조회 API")
@Validated
@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<PageResponse<ProblemSummaryResponse>> getProblems(
            @RequestParam(defaultValue = "0") @Min(0) @Max(100000) int page,
            @RequestParam(defaultValue = "20") @Min(10) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(PageResponse.from(problemService.getProblems(pageable)));
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> getProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemService.getProblem(problemId));
    }
}
