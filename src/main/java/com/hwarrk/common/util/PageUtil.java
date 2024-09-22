package com.hwarrk.common.util;

import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtil {

    public static Boolean hasNextPage(List<?> content, Pageable pageable) {
        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }

    public static <T> T getLastElement(List<T> list) {
        if (list.isEmpty()) return null;

        T t = list.get(list.size() - 1);

        return t;
    }

}
