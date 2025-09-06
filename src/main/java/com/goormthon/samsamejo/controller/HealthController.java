package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseDto<Void> health() {
        return ResponseDto.ok(null);
    }
}
