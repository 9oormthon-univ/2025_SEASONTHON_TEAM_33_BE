// src/main/java/com/goormthon/samsamejo/service/ArchiveService.java
package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Essay;
import com.goormthon.samsamejo.dto.response.ArchiveEssayDetailResponse;
import com.goormthon.samsamejo.dto.response.ArchiveEssayFeedbackResponse;
import com.goormthon.samsamejo.dto.response.ArchiveEssayResponse;
import com.goormthon.samsamejo.dto.response.ArchiveResponse;
import com.goormthon.samsamejo.repository.EssayRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchiveService {

    private final EssayRepository essayRepository;

    public ArchiveService(EssayRepository essayRepository) {
        this.essayRepository = essayRepository;
    }

    /**
     * 특정 유저의 아카이브 목록 조회
     */
    public ArchiveResponse getArchives(Long userId, String search, Pageable pageable) {
        Page<Essay> essays;

        if (search == null || search.isBlank()) {
            essays = essayRepository.findAllByUser_Id(userId, pageable);
        } else {
            essays = essayRepository.findByUser_IdAndContentContainingOrUser_IdAndQuestion_ContentContaining(
                    userId, search,
                    userId, search,
                    pageable
            );
        }

        // 전체 재사용 횟수 합산
        int totalReuseCount = essays.stream()
                .mapToInt(e -> essayRepository.countByQuestion_IdAndContent(
                        e.getQuestion().getId(),
                        e.getContent()
                ))
                .sum();

        // 지원 기업 수 (distinct companyName)
        long applyCompanyCount = essays.stream()
                .flatMap(e -> essayRepository.findAllByQuestion_IdAndContent(
                                e.getQuestion().getId(),
                                e.getContent()
                        ).stream()
                        .map(e2 -> e2.getUserRecruitment().getRecruitment().getCompanyName()))
                .distinct()
                .count();

        // Essay DTO 변환 (reuseCount, applyCompanies 포함)
        List<ArchiveEssayResponse> essayResponses = essays.stream()
                .map(e -> ArchiveEssayResponse.fromEntity(e, essayRepository))
                .collect(Collectors.toList());

        return ArchiveResponse.builder()
                .savedEssayCount((int) essays.getTotalElements())
                .reuseCount(totalReuseCount)
                .recuitmentApplyCount((int) applyCompanyCount)
                .essays(essayResponses)
                .pageInfo(ArchiveResponse.PageInfo.from(essays))
                .build();
    }

    public ArchiveEssayDetailResponse getArchiveDetail(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다. essayId=" + essayId));

        return ArchiveEssayDetailResponse.fromEntity(essay, essayRepository);
    }

    /**
     * 특정 답변의 AI 피드백 조회
     */
    public List<ArchiveEssayFeedbackResponse> getEssayFeedbacks(Long essayId) {
        Essay essay = essayRepository.findById(essayId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다. essayId=" + essayId));

        return List.of(ArchiveEssayFeedbackResponse.fromEntity(essay));
    }
}
