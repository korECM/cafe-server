package zip.cafe.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @Operation(summary = "헬스 체크", description = "헬스 체크용 엔드포인트")
    @GetMapping("/health")
    fun health(): ResponseEntity<String> {
        return ok("Hello")
    }
}
