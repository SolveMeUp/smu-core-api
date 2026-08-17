package com.solvemeup.smucoreapi.domain.judge.problem.dto.response;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;

public record ProblemSummaryResponse(
        Long id,
        String title,
        int difficulty,
        int solvedUserCount,
        double acceptanceRate
) {
    public static ProblemSummaryResponse from(Problem problem) {
        return new ProblemSummaryResponse(
                problem.getId(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getSolvedUserCount(),
                acceptanceRate(problem.getSolvedCount(), problem.getSubmissionCount())
        );
    }

    private static double acceptanceRate(int solvedCount, int submissionCount) {
        if (submissionCount == 0) {
            return 0.0;
        }
        return Math.round(solvedCount * 10000.0 / submissionCount) / 100.0;
    }
}
