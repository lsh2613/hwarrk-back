package com.hwarrk.common;

import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.HasId;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class SliceCustomImpl {
    private List content;
    private boolean hasNext;
    private Long lastElementId;

    /**
     * @param target  : No-offset 방식을 사용하기 위해 LastElementId를 뽑아낼 List
     * @param content : Dto로 반환될 res
     */
    public <E extends HasId> SliceCustomImpl(List<E> target, List content, Pageable pageable) {
        this.hasNext = PageUtil.hasNextPage(target.size(), pageable);
        if (hasNext) {
            target.remove(pageable.getPageSize());
            content.remove(pageable.getPageSize());
        }
        this.content = content;
        this.lastElementId = target.isEmpty() ? null : PageUtil.getLastElement(target).getId();
    }

    public boolean hasNext() {
        return hasNext;
    }
}
