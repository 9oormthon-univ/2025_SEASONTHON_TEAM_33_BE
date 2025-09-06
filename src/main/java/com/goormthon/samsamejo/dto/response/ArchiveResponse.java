package com.goormthon.samsamejo.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ArchiveResponse {
    private int savedEssayCount;        // 저장된 답변 수
    private int reuseCount;             // 전체 재사용 횟수
    private int recuitmentApplyCount;   // 지원 기업 수
    private List<ArchiveEssayResponse> essays;
    private PageInfo pageInfo;

    @Getter
    @Builder
    public static class PageInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;

        public static PageInfo from(Page<?> page) {
            return PageInfo.builder()
                    .page(page.getNumber() + 1)
                    .size(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .build();
        }
    }
}
