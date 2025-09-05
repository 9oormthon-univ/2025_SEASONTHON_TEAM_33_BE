// src/main/java/com/goormthon/samsamejo/service/RecruitmentService.java
package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Recruitment;
import com.goormthon.samsamejo.dto.response.RecruitmentApiResponse;
import com.goormthon.samsamejo.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentFetchLogService fetchLogService; // 로그 저장 서비스
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${recruitment.api.public.key}")
    private String apiKey;

    @Value("${recruitment.api.public.url}")
    private String apiUrl;

    /**
     * 채용 공고 목록 조회 (검색 + 정렬 + 필터링 + 페이징)
     */
    public Page<Recruitment> getRecruitments(
            String keyword,
            String order,
            String location,
            String career,
            String category,
            int page,
            int size
    ) {
        // 정렬 조건
        Sort sort = switch (order == null ? "" : order.toLowerCase()) {
            case "deadlineasc" -> Sort.by("deadline").ascending();
            case "deadlinedesc" -> Sort.by("deadline").descending();
            default -> Sort.by("created_at").descending();
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        // career 변환
        List<String> careers = expandCareer(career);

        return recruitmentRepository.findByConditionsNative(
                keyword,
                location,
                careers == null ? List.of() : careers,
                careers == null ? 0 : careers.size(),
                category,
                pageable
        );
    }

    private List<String> expandCareer(String career) {
        if (career == null) return null;
        return switch (career) {
            case "신입" -> List.of("NEW", "BOTH");
            case "경력" -> List.of("EXPERIENCED", "BOTH");
            case "신입+경력" -> List.of("BOTH");
            default -> null;
        };
    }

    /**
     * 채용 공고 상세 조회
     */
    public Optional<Recruitment> getRecruitmentById(Long id) {
        return recruitmentRepository.findById(id);
    }

    /**
     * 채용 공고 저장 (직접 입력 or API 결과)
     */
    public Recruitment saveRecruitment(Recruitment recruitment) {
        return recruitmentRepository.save(recruitment);
    }

    /**
     * 채용 공고 삭제
     */
    public void deleteRecruitment(Long id) {
        recruitmentRepository.deleteById(id);
    }

    /**
     * 공공데이터포털 API 호출 → DB 저장 → 로그 기록
     * @return 저장된 건수
     */
    public int fetchAndSaveFromApi() {
        int pageNo = 1;
        int numOfRows = 30; // 가능하면 최대치
        int totalSaved = 0;
        int maxLimit = 30;

        try {
            while (totalSaved < maxLimit) {
                String url = apiUrl + "/list"
                        + "?serviceKey=" + apiKey
                        + "&pageNo=" + pageNo
                        + "&numOfRows=" + numOfRows
                        + "&resultType=json";

                RecruitmentApiResponse response = restTemplate.getForObject(url, RecruitmentApiResponse.class);

                if (response == null || response.getResult() == null || response.getResult().isEmpty()) {
                    break; // 더 이상 데이터 없음
                }

                List<Recruitment> recruitments = response.getResult().stream()
                        .map(this::toEntity)
                        .toList();

                // Upsert 로직 적용
                totalSaved += saveOrUpdateRecruitments(recruitments);
                pageNo++;
            }

            fetchLogService.saveLog("SUCCESS", "API", totalSaved, 0, 0, "정상 저장");
            return totalSaved;
        } catch (Exception e) {
            fetchLogService.saveLog("FAILED", "API", 0, 0, 0, e.getMessage());
            throw e;
        }
    }

    /**
     * API 응답 Item → Recruitment 엔티티 변환
     */
    private Recruitment toEntity(RecruitmentApiResponse.Item item) {
        return Recruitment.builder()
                .externalId(String.valueOf(item.getAnnouncementId())) // 공공데이터포털 고유 ID
                .companyName(item.getCompanyName())     // 기관명
                .title(item.getTitle())                 // 공고 제목
                .employmentType(item.getEmploymentType()) // 채용 유형
                .description(item.getDescription())     // 설명
                .requirements(item.getRequirements())   // 지원 자격
                .location(item.getLocation())           // 근무지
                .experienceLevel(convertCareer(item.getCareer())) // 경력 구분
                .deadline(item.getDeadline())           // 마감일
                .url(item.getUrl())                     // 원본 URL
                .category(item.getNcsCdNmLst())         // NCS 분류명
                .skills(List.of())                      // 기본은 빈 리스트
                .build();
    }

    /**
     * Upsert (중복 방지 저장)
     */
    private int saveOrUpdateRecruitments(List<Recruitment> recruitments) {
        int count = 0;
        for (Recruitment r : recruitments) {
            Optional<Recruitment> existing = recruitmentRepository.findByExternalId(r.getExternalId());
            if (existing.isPresent()) {
                existing.get().updateFrom(r); // update 메서드 구현 (Recruitment 엔티티에 있음)
            } else {
                recruitmentRepository.save(r);
                count++;
            }
        }
        return count;
    }

    // ===== 변환 메서드 =====
    private Recruitment.ExperienceLevel convertCareer(String career) {
        if (career == null) return null;
        return switch (career) {
            case "신입" -> Recruitment.ExperienceLevel.NEW;
            case "경력" -> Recruitment.ExperienceLevel.EXPERIENCED;
            case "신입+경력" -> Recruitment.ExperienceLevel.BOTH;
            default -> null;
        };
    }
}
