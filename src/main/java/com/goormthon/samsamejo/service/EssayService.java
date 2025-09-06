package com.goormthon.samsamejo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormthon.samsamejo.domain.Essay;
import com.goormthon.samsamejo.domain.Question;
import com.goormthon.samsamejo.dto.request.RecruitmentEssayWriteRequest;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.repository.EssayRepository;
import com.goormthon.samsamejo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EssayService {

    private final EssayRepository essayRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 자기소개서 작성/수정
     * @param recruitmentId 채용 공고 ID
     * @param request 사용자 작성 요청
     */
    @Transactional
    public void writeEssays(Long recruitmentId, /* Long userId, */ RecruitmentEssayWriteRequest request) {
        // TODO: User 연동 시 활성화
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_USER));

        request.getEssays().forEach(essayReq -> {
            Question question = questionRepository.findById(essayReq.getQuestionId())
                    .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_QUESTION));

            String content = essayReq.getEssayContent();
            if (content == null || content.isBlank()) {
                throw new RestException(ErrorCode.INVALID_INPUT_VALUE);
            }

            // 현재는 user 연동이 없으므로 question 기준으로만 에세이를 가져옴
            Essay essay = question.getEssays().isEmpty() ? null : question.getEssays().get(0);

            if (essay == null) {
                essay = Essay.builder()
                        .question(question)
                        .content(content)
                        .tags(convertTagsToJson(essayReq.getTags()))
                        .build();
            } else {
                essay.setContent(content);
                essay.setTags(convertTagsToJson(essayReq.getTags()));
            }

            essayRepository.save(essay);
        });
    }

    /**
     * 태그 리스트를 JSON 문자열로 변환
     */
    private String convertTagsToJson(java.util.List<String> tags) {
        if (tags == null) return "[]";
        try {
            return objectMapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
