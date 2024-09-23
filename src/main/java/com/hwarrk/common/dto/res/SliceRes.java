package com.hwarrk.common.dto.res;

import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.HasId;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

@Builder
public record SliceRes<R>(
        List<R> content,
        Long lastElementId,
        Boolean hasNext
) {
    // 'E'ntity -> 'R'esponse로 변환하는 제네릭
    public static <E extends HasId, R> SliceRes<R> mapSliceToSliceRes(Slice<E> slice, Function<E, R> mapper) {
        return SliceRes.<R>builder()
                .content(slice.getContent().stream().map(mapper).toList())
                .lastElementId(PageUtil.getLastElement(slice.getContent()).getId())
                .hasNext(slice.hasNext())
                .build();
    }

    public static <E, R> SliceRes<R> mapSliceCustomToSliceRes(SliceCustomImpl slice, Function<E, R> mapper) {
        return SliceRes.<R>builder()
                .content(slice.getContent().stream().map(mapper).toList())
                .lastElementId(slice.getLastElementId())
                .hasNext(slice.hasNext())
                .build();
    }
}
