package com.solvemeup.smucoreapi.domain.judge.execution.controller;

import com.solvemeup.smucoreapi.domain.judge.execution.dto.request.ExecutionRequest;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.dto.response.ExecutionStatusResponse;
import com.solvemeup.smucoreapi.domain.judge.execution.service.ExecutionService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Execution", description = "코드 실행(예제 테스트) 요청·결과 조회 API")
@RestController
@RequestMapping("/api/executions")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @Operation(
            summary = "코드 실행 요청 (비동기)",
            description = """
                    제출한 코드를 예제 케이스로 실행하도록 요청한다. **결과는 즉시 반환되지 않는다.**

                    응답은 202(Accepted) + executionId 뿐이며, 실제 실행은 백그라운드(메시지 큐)에서 진행된다.
                    실행 결과를 받으려면 반환된 executionId로 `GET /api/executions/{executionId}`를 폴링해야 한다.""")
    @PostMapping
    public ResponseEntity<ExecutionResponse> execute(@AuthUserId Long userId,
                                                     @RequestBody ExecutionRequest request) {
        return ResponseEntity.accepted().body(executionService.execute(userId, request));
    }

    @Operation(
            summary = "실행 상태·결과 조회 (폴링)",
            description = """
                    executionId로 실행 상태와 결과를 조회한다. 실행 완료까지 주기적으로 호출(폴링)하는 용도다.

                    status 값:
                    - `RUNNING`: 아직 실행 중. results는 비어 있다. 잠시 후 다시 호출한다.
                    - `DONE`: 실행 완료. results에 케이스별 판정(verdict)·입출력·시간/메모리가 채워진다.""")
    @GetMapping("/{executionId}")
    public ResponseEntity<ExecutionStatusResponse> getExecutionStatus(@PathVariable Long executionId) {
        return ResponseEntity.ok(executionService.getExecutionStatus(executionId));
    }
}
