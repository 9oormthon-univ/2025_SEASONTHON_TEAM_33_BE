package com.goormthon.samsamejo.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EssayRequest {
    private Long questionId;       // 질문 ID
    private String essayContent;   // 답변 내용
    private List<String> tags;     // 태그 (빈 배열 허용)
}
