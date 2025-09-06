package com.goormthon.samsamejo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormthon.samsamejo.domain.*;
import com.goormthon.samsamejo.dto.request.RecruitmentEssayWriteRequest;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EssayService {

    private final EssayRepository essayRepository;
    private final QuestionRepository questionRepository;
    private final UsersRepository userRepository;
    private final UserRecruitmentRepository userRecruitmentRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 자기소개서 작성/수정
     * @param userId        작성자 유저 ID (JWT에서 추출)
     * @param recruitmentId 채용 공고 ID
     * @param request       사용자 작성 요청
     */
    @Transactional
    public void writeEssays(Long userId, Long recruitmentId, RecruitmentEssayWriteRequest request) {
        // 유저 확인
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_USER));

        // 채용 공고 확인
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_RECRUITMENT));

        // 기존 자기소개서(UserRecruitment) 확인 or 새로 생성
        UserRecruitment userRecruitment;
        if (request.getUserRecruitmentId() != null) {
            userRecruitment = userRecruitmentRepository.findById(request.getUserRecruitmentId())
                    .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_RECRUITMENT));
        } else {
            userRecruitment = UserRecruitment.builder()
                    .user(user)
                    .recruitment(recruitment)
                    .build();
            userRecruitmentRepository.save(userRecruitment);
        }

        // 문항별 답변 저장
        request.getEssays().forEach(essayReq -> {
            Question question = questionRepository.findById(essayReq.getQuestionId())
                    .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_QUESTION));

            Essay essay = essayRepository.findByUser_IdAndQuestion_Id(userId, essayReq.getQuestionId())
                    .orElse(null);

            if (essay == null) {
                essay = Essay.builder()
                        .user(user)
                        .userRecruitment(userRecruitment)
                        .question(question)
                        .content(essayReq.getEssayContent())
                        .tags(convertTagsToJson(essayReq.getTags()))
                        .build();
            } else {
                essay.setContent(essayReq.getEssayContent());
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
