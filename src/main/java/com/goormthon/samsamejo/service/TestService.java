package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Test;
import com.goormthon.samsamejo.dto.response.TestCreateResponse;
import com.goormthon.samsamejo.dto.response.TestReadResponse;
import com.goormthon.samsamejo.dto.response.TestsReadResponse;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public TestCreateResponse create(String testValue) {
        Test test = Test.builder()
                .testValue(testValue)
                .build();

        Test savedTest = testRepository.save(test);
        return TestCreateResponse.from(savedTest);
    }

    public TestReadResponse read(Long id) {
        Test test = testRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.INTERNAL_SERVER_ERROR));
        return TestReadResponse.from(test);
    }

    public TestsReadResponse readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("testValue").descending());
        Page<Test> tests = testRepository.findAll(pageable);
        return TestsReadResponse.from(tests);
    }
}
