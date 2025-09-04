package com.goormthon.samsamejo.dto.request;

import jakarta.validation.constraints.NotNull;

public record TestCreateRequest(
        @NotNull
        String testValue
) {
}
