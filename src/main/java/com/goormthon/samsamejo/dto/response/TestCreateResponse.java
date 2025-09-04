package com.goormthon.samsamejo.dto.response;

import com.goormthon.samsamejo.domain.Test;

public record TestCreateResponse(
        Long id,
        String testValue
) {

    public static TestCreateResponse from(Test test) {
        return new TestCreateResponse(test.getId(), test.getTestValue());
    }
}
