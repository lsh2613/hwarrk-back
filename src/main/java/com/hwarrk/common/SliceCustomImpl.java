package com.hwarrk.common;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@Getter
public class SliceCustomImpl extends SliceImpl {
    private Long lastElementId;

    public SliceCustomImpl(List content, Pageable pageable, boolean hasNext, Long lastElementId) {
        super(content, pageable, hasNext);
        this.lastElementId = lastElementId;
    }
}
