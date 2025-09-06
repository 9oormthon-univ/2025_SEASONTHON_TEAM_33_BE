package com.goormthon.samsamejo.dto.request.auth;

import com.goormthon.samsamejo.domain.type.Education;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UpdateUserInfoRequest(
        @Schema(description = "이름", example = "홍길동")
        @NotBlank(message = "이름은 필수 입력입니다.")
        @Min(1)
        String name,

        @Schema(description = "이메일", example = "test1234@naevr.com")
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 적합하지 않습니다.")
        String email,

        @Schema(description = "전화번호", example = "01012345678")
        @NotBlank(message = "전화번호는 필수 입력입니다.")
        @Pattern(regexp = "^\\d{11}$")
        String phoneNumber,

        @NotNull(message = "학력은 필수 입력입니다.")
        Education education,

        String major,

        List<String> interestFields,

        List<String> skills
) {
}
