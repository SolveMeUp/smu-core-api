package com.solvemeup.smucoreapi.domain.judge.submission.controller;

import com.solvemeup.smucoreapi.domain.judge.submission.dto.request.SubmissionRequest;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.dto.response.SubmissionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.submission.service.SubmissionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Submission", description = "제출·채점 요청 및 결과 조회 API")
@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(
            summary = "제출 (비동기 채점)",
            description = """
                    풀이 코드를 제출해 채점을 요청한다. **채점 결과는 즉시 반환되지 않는다.**

                    응답은 202(Accepted) + submissionId 뿐이며, 채점은 백그라운드(메시지 큐)에서 진행된다.
                    채점 결과를 받으려면 반환된 submissionId로 `GET /api/submissions/{submissionId}`를 폴링해야 한다.""")
    @PostMapping
    public ResponseEntity<SubmissionResponse> submit(@AuthUserId Long userId,
                                                     @RequestBody SubmissionRequest request) {
        return ResponseEntity.accepted().body(submissionService.submit(userId, request));
    }

    @Operation(
            summary = "채점 상태·결과 조회 (폴링)",
            description = """
                    submissionId로 채점 상태와 결과를 조회한다. 채점 완료까지 주기적으로 호출(폴링)하는 용도다.

                    status 값:
                    - `PENDING`: 아직 채점 중. verdict는 아직 확정되지 않았다. 잠시 후 다시 호출한다.
                    - `DONE`: 채점 완료. verdict가 확정되며, 틀린 경우 failedCase에 실패한 케이스 정보가 채워진다.""")
    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionStatusResponse> getSubmissionStatus(@PathVariable Long submissionId) {
        return ResponseEntity.ok(submissionService.getSubmissionStatus(submissionId));
    }
}
