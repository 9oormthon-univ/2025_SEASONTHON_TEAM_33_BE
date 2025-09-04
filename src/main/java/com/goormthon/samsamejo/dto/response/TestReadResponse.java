package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Test;

public record TestReadResponse(
        Long id,
        String testValue
) {

    public static TestReadResponse from(Test test) {
        return new TestReadResponse(test.getId(), test.getTestValue());
    }
}
