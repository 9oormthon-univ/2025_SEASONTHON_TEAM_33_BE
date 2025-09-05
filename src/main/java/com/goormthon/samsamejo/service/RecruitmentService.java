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
        String url = apiUrl + "/list"
                + "?serviceKey=" + apiKey
                + "&pageNo=1"
                + "&numOfRows=10"
                + "&resultType=json";

        try {
            RecruitmentApiResponse response = restTemplate.getForObject(url, RecruitmentApiResponse.class);

            if (response == null || response.getResult() == null) {
                throw new RuntimeException("API 응답이 올바르지 않습니다.");
            }

            List<RecruitmentApiResponse.Item> items = response.getResult();

            // Item → Recruitment 변환
            List<Recruitment> recruitments = items.stream()
                    .map(item -> Recruitment.builder()
                            .companyName(item.getCompanyName())     // 기관명
                            .title(item.getTitle())                 // 공고명
                            .employmentType(item.getEmploymentType())    // 채용분야
                            .description(item.getDescription())     // 설명
                            .requirements(item.getRequirements())   // 자격요건
                            .location(item.getLocation())           // 근무지
                            .experienceLevel(convertCareer(item.getCareer()))
                            .deadline(item.getDeadline())           // 마감일
                            .url(item.getUrl())                     // 원본 URL
                            .category(item.getNcsCdNmLst())         // NCS 분류
                            .skills(List.of())                      // 기본은 빈 리스트
                            .build())
                    .toList();

            recruitmentRepository.saveAll(recruitments);

            fetchLogService.saveLog("SUCCESS", "API",
                    recruitments.size(), 0, 0,
                    "정상 저장");

            return recruitments.size();
        } catch (Exception e) {
            fetchLogService.saveLog("FAILED", "API",
                    0, 0, 0,
                    e.getMessage());
            throw e;
        }
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
