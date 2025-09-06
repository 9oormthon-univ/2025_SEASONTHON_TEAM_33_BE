package com.goormthon.samsamejo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResumeUpdateRequest {
    private List<EssayUpdateRequest> essays;

    @Getter
    @Setter
    public static class EssayUpdateRequest {
        private Long questionId;      // 질문 ID
        private Long essayId;         // 답변 ID (수정 시 필요, 신규면 null)
        private String essayContent;  // 답변 내용
        private List<String> tags;    // 태그 리스트
    }
}
