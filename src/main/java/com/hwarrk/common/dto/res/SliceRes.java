package com.hwarrk.common.dto.res;

import com.hwarrk.common.SliceCustomImpl;
import lombok.Builder;

import java.util.List;

@Builder
public record SliceRes<R>(
        List<R> content,
        Long lastElementId,
        Boolean hasNext
) {

    public static <R> SliceRes<R> mapSliceCustomToSliceRes(SliceCustomImpl slice) {
        return SliceRes.<R>builder()
                .content(slice.getContent())
                .lastElementId(slice.getLastElementId())
                .hasNext(slice.hasNext())
                .build();
    }
}
