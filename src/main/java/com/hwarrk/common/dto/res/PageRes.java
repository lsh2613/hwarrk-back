package com.hwarrk.common.dto.res;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Builder
public record PageRes<R>(
        List<R> content,
        long totalElements,
        int totalPages,
        boolean isLast
) {
    // 'E'ntity -> 'R'esponse로 변환하는 제네릭
    public static <E, R> PageRes<R> mapPageToPageRes(Page<E> page, Function<E, R> mapper) {
        return PageRes.<R>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLast(page.isLast())
                .build();
    }
}
