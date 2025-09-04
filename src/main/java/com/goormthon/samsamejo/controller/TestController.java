package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.dto.ResponseDto;
import com.goormthon.samsamejo.dto.request.TestCreateRequest;
import com.goormthon.samsamejo.dto.response.TestCreateResponse;
import com.goormthon.samsamejo.dto.response.TestReadResponse;
import com.goormthon.samsamejo.dto.response.TestsReadResponse;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/")
    public String test() {
        return "test response";
    }

    @GetMapping("/responsedto")
    public ResponseDto<String> responseDtoTest() {
        return ResponseDto.ok("test response");
    }

    @GetMapping("/exception")
    public void exceptionTest() {
        throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/create")
    public ResponseDto<TestCreateResponse> createTest(@RequestBody TestCreateRequest testCreateRequest) {
        return ResponseDto.ok(testService.create(testCreateRequest.testValue()));
    }

    @GetMapping("/find/{id}")
    public ResponseDto<TestReadResponse> readTest(@PathVariable Long id) {
        return ResponseDto.ok(testService.read(id));
    }

    @GetMapping("/all")
    public ResponseDto<TestsReadResponse> readTest() {
        return ResponseDto.ok(testService.readAll(0, 1));
    }
}
