package com.hwarrk.common.util;

import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtil {

    public static Boolean hasNextPage(int contentSize, Pageable pageable) {
        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        return contentSize > pageable.getPageSize();
    }

    public static <T> T getLastElement(List<T> list) {
        if (list.isEmpty()) return null;

        T t = list.get(list.size() - 1);

        return t;
    }

}
