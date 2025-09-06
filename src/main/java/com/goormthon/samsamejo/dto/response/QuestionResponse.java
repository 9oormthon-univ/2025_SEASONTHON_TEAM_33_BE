package com.goormthon.samsamejo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private Long questionId;       // 질문 ID
    private String content;        // 질문 내용
    private Integer minLength;     // 최소 글자 수
    private Integer maxLength;     // 최대 글자 수
    private Boolean required;      // 필수 여부
}
