package com.goormthon.samsamejo.dto.common;

import org.springframework.data.domain.Page;

public record PageInfoDto(
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static <T> PageInfoDto from(Page<T> page) {
        return new PageInfoDto(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}

