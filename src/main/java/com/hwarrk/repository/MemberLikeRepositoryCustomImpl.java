package com.hwarrk.repository;

import com.hwarrk.entity.MemberLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hwarrk.entity.QMember.member;
import static com.hwarrk.entity.QMemberLike.memberLike;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberLikeRepositoryCustomImpl implements MemberLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberLike> getMemberLikeSliceInfo(Long memberId, Long lastMemberLikeId, Pageable pageable) {
        return jpaQueryFactory
                .select(memberLike)
                .from(memberLike)
                .join(memberLike.toMember, member).fetchJoin()
                .where(
                        ltMemberLikeId(lastMemberLikeId),
                        eqFromMemberId(memberId)
                )
                .orderBy(memberLike.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression eqFromMemberId(Long memberId) {
        return memberLike.fromMember.id.eq(memberId);
    }

    private BooleanExpression ltMemberLikeId(Long lastMemberLikeId) {
        return lastMemberLikeId == null ? null : memberLike.id.lt(lastMemberLikeId);
    }
}
