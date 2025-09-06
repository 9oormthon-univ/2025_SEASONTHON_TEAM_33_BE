package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Essay;
import com.goormthon.samsamejo.domain.Question;
import com.goormthon.samsamejo.domain.UserRecruitment;
import com.goormthon.samsamejo.dto.request.ResumeUpdateRequest;
import com.goormthon.samsamejo.dto.response.ResumeDetailResponse;
import com.goormthon.samsamejo.dto.response.ResumeListResponse;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.repository.EssayRepository;
import com.goormthon.samsamejo.repository.QuestionRepository;
import com.goormthon.samsamejo.repository.UserRecruitmentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final UserRecruitmentRepository userRecruitmentRepository;
    private final QuestionRepository questionRepository;
    private final EssayRepository essayRepository;

    /**
     * 자기소개서 목록 조회 (요약)
     */
    public ResumeListResponse getUserResumes(Long userId) {
        List<UserRecruitment> userRecruitments = userRecruitmentRepository.findAllByUser_Id(userId);

        List<ResumeListResponse.ResumeSummary> total = userRecruitments.stream().map(ur -> {
            Long recruitmentId = ur.getRecruitment().getId();

            // 전체 문항 수
            List<Question> questions = questionRepository.findByRecruitment_Id(recruitmentId);
            int questionCount = questions.size();

            // 작성된 답변 수 (현재 유저가 작성한 Essay만 카운트)
            List<Essay> essays = ur.getEssays().stream()
                    .filter(e -> e.getUser().getId().equals(userId))
                    .toList();
            int essayCount = essays.size();

            // 진행률
            int progress = questionCount == 0 ? 0 : (essayCount * 100 / questionCount);
            String status = (progress == 100) ? "COMPLETE" : "PROGRESS";

            // 마지막 수정일 (Essay.updatedAt 중 가장 최근 값, 없으면 UserRecruitment.createdAt)
            LocalDate modifiedAt = essays.stream()
                    .map(e -> e.getUpdatedAt() != null
                            ? e.getUpdatedAt().toLocalDate()
                            : e.getCreatedAt().toLocalDate())
                    .max(Comparator.naturalOrder())
                    .orElse(ur.getCreatedAt().toLocalDate());

            return ResumeListResponse.ResumeSummary.builder()
                    .userRecruitmentId(ur.getId())
                    .company(ur.getRecruitment().getCompanyName())
                    .title(ur.getRecruitment().getTitle())
                    .writeStatus(status)
                    .progress(progress)
                    .essayCount(essayCount)
                    .questionCount(questionCount)
                    .modifiedAt(modifiedAt)
                    .build();
        }).collect(Collectors.toList());

        List<ResumeListResponse.ResumeSummary> complete = total.stream()
                .filter(r -> r.getWriteStatus().equals("COMPLETE"))
                .collect(Collectors.toList());

        List<ResumeListResponse.ResumeSummary> progressing = total.stream()
                .filter(r -> r.getWriteStatus().equals("PROGRESS"))
                .collect(Collectors.toList());

        return ResumeListResponse.builder()
                .totalUserRecruitments(total)
                .completeUserRecruitments(complete)
                .progressingUserRecruitments(progressing)
                .build();
    }

    /**
     * 특정 자기소개서 상세 조회
     */
    @Transactional(readOnly = true)
    public ResumeDetailResponse getUserResumeDetail(Long userId, Long userRecruitmentId) {
        // UserRecruitment 확인
        UserRecruitment userRecruitment = userRecruitmentRepository.findById(userRecruitmentId)
                .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_USER_RECRUITMENT));

        // 본인 소유 검사
        if (!userRecruitment.getUser().getId().equals(userId)) {
            throw new RestException(ErrorCode.FORBIDDEN); // 권한 없음
        }

        Long recruitmentId = userRecruitment.getRecruitment().getId();

        // 해당 공고의 질문들
        List<Question> questions = questionRepository.findByRecruitment_Id(recruitmentId);

        // 답변 매핑
        List<ResumeDetailResponse.QuestionWithEssay> questionDtos = questions.stream()
                .map(q -> {
                    Essay essay = userRecruitment.getEssays().stream()
                            .filter(e -> e.getQuestion().getId().equals(q.getId()))
                            .findFirst()
                            .orElse(null);

                    return ResumeDetailResponse.QuestionWithEssay.builder()
                            .questionId(q.getId())
                            .questionContent(q.getContent())
                            .minLength(q.getMinLength())
                            .maxLength(q.getMaxLength())
                            .required(q.getRequired())
                            .essayId(essay != null ? essay.getId() : null)
                            .essayContent(essay != null ? essay.getContent() : "")
                            .tags(essay != null ? essay.getTagsAsList() : List.of())
                            .build();
                })
                .toList();

        return ResumeDetailResponse.builder()
                .recruitmentId(recruitmentId)
                .questions(questionDtos)
                .build();
    }

    @Transactional
    public void updateUserResume(Long userId, Long userRecruitmentId, ResumeUpdateRequest request) {
        UserRecruitment userRecruitment = userRecruitmentRepository.findById(userRecruitmentId)
                .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_USER_RECRUITMENT));

        if (!userRecruitment.getUser().getId().equals(userId)) {
            throw new RestException(ErrorCode.FORBIDDEN);
        }

        for (ResumeUpdateRequest.EssayUpdateRequest essayReq : request.getEssays()) {
            // 질문 확인
            Question question = questionRepository.findById(essayReq.getQuestionId())
                    .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_QUESTION));

            Essay essay;
            if (essayReq.getEssayId() != null) {
                // 기존 답변 수정
                essay = essayRepository.findById(essayReq.getEssayId())
                        .orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND_QUESTION));

                essay.setContent(essayReq.getEssayContent());
                essay.setTagsFromList(essayReq.getTags());
            } else {
                // 새 답변 추가
                essay = Essay.builder()
                        .user(userRecruitment.getUser())
                        .userRecruitment(userRecruitment)
                        .question(question)
                        .content(essayReq.getEssayContent())
                        .build();
                essay.setTagsFromList(essayReq.getTags());
            }
            essayRepository.save(essay);
        }
    }
}
