package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Test;
import com.goormthon.samsamejo.dto.common.PageInfoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record TestsReadResponse(
        List<TestReadResponse> testReadResponses,
        PageInfoDto pageInfo
) {

    public static TestsReadResponse from(Page<Test> tests) {
        return new TestsReadResponse(tests.stream().map(TestReadResponse::from).toList(), PageInfoDto.from((tests)));
    }
}
