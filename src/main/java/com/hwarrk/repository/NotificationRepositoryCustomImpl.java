package com.hwarrk.repository;

import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.Notification;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hwarrk.entity.QNotification.notification;


@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Notification> getNotificationsSlice(Long memberId, Long lastNotificationId, Pageable pageable) {
        List<Notification> notifications = getNotifications(memberId, lastNotificationId, pageable);
        Boolean hasNext = PageUtil.hasNextPage(notifications, pageable);
        return new SliceImpl<>(notifications, pageable, hasNext);
    }

    private List<Notification> getNotifications(Long memberId, Long lastNotificationId, Pageable pageable) {
        return queryFactory
                .selectFrom(notification)
                .where(
                        ltNotificationId(lastNotificationId),
                        eqFollowingMemberId(memberId)
                )
                .orderBy(notification.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression eqFollowingMemberId(Long memberId) {
        return notification.member.id.eq(memberId);
    }

    private BooleanExpression ltNotificationId(Long lastNotificationId) {
        return lastNotificationId == null ? null : notification.id.lt(lastNotificationId);
    }
}
