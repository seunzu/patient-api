package com.inspline.patient_api.global.slow;

import com.inspline.patient_api.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SlowController {

    @GetMapping("/slow")
    public ResponseEntity<ApiResponse<String>> slow(
            @RequestParam(defaultValue = "5") int delay) throws InterruptedException {
        log.info("[Slow] Request started. delay={}s", delay);
        Thread.sleep(delay * 1000L);
        log.info("[Slow] Request completed. delay={}s", delay);
        return ResponseEntity.ok(ApiResponse.ok("완료. delay=" + delay + "s"));
    }
}