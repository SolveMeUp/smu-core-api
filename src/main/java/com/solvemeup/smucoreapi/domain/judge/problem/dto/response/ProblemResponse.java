package com.solvemeup.smucoreapi.domain.judge.problem.dto.response;

import com.solvemeup.smucoreapi.domain.judge.problem.entity.DataType;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.FunctionParameter;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.Problem;
import com.solvemeup.smucoreapi.domain.judge.problem.entity.SampleCase;
import com.solvemeup.smucoreapi.domain.user.entity.User;

import java.util.List;

public record ProblemResponse(
        Long id,
        AuthorResponse author,
        String title,
        String description,
        String constraints,
        int timeLimitMillis,
        int memoryLimitKilobytes,
        String functionName,
        List<FunctionParameter> parameters,
        DataType returnType,
        int difficulty,
        int submissionCount,
        int solvedUserCount,
        List<SampleCaseResponse> sampleCases
) {
    public record AuthorResponse(Long id, String nickname) {
        public static AuthorResponse from(User user) {
            return new AuthorResponse(user.getId(), user.getNickname());
        }
    }

    public record SampleCaseResponse(int orderIndex, String argumentsJson, String expectedOutputJson) {
        public static SampleCaseResponse from(SampleCase sampleCase) {
            return new SampleCaseResponse(
                    sampleCase.getOrderIndex(),
                    sampleCase.getArgumentsJson(),
                    sampleCase.getExpectedOutputJson()
            );
        }
    }

    public static ProblemResponse from(Problem problem, List<SampleCase> sampleCases) {
        return new ProblemResponse(
                problem.getId(),
                AuthorResponse.from(problem.getAuthor()),
                problem.getTitle(),
                problem.getDescription(),
                problem.getConstraints(),
                problem.getTimeLimitMillis(),
                problem.getMemoryLimitKilobytes(),
                problem.getFunctionName(),
                problem.getParameters(),
                problem.getReturnType(),
                problem.getDifficulty(),
                problem.getSubmissionCount(),
                problem.getSolvedUserCount(),
                sampleCases.stream()
                        .map(SampleCaseResponse::from)
                        .toList()
        );
    }
}
