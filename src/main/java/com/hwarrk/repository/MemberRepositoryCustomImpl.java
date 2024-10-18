package com.hwarrk.repository;

import static com.hwarrk.entity.QCareer.career;
import static com.hwarrk.entity.QDegree.degree;
import static com.hwarrk.entity.QMember.member;
import static com.hwarrk.entity.QMemberLike.memberLike;
import static com.hwarrk.entity.QPortfolio.portfolio;
import static com.hwarrk.entity.QPosition.position;
import static com.hwarrk.entity.QProjectDescription.projectDescription;
import static com.hwarrk.entity.QSkill.skill;

import com.hwarrk.common.constant.FilterType;
import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.dto.dto.ContentWithTotalDto;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.QMemberWithLikeDto;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.entity.Member;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hwarrk.entity.QCareer.career;
import static com.hwarrk.entity.QDegree.degree;
import static com.hwarrk.entity.QExternalProjectDescription.externalProjectDescription;
import static com.hwarrk.entity.QMember.member;
import static com.hwarrk.entity.QMemberLike.memberLike;
import static com.hwarrk.entity.QMemberReview.memberReview;
import static com.hwarrk.entity.QPortfolio.portfolio;
import static com.hwarrk.entity.QPosition.position;
import static com.hwarrk.entity.QProjectDescription.projectDescription;
import static com.hwarrk.entity.QSkill.skill;

@Repository
@AllArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member getMyProfile(Long memberId) {
        Member fetchedMember = queryFactory
                .selectFrom(member)
                .leftJoin(member.portfolios, portfolio)
                .leftJoin(member.positions, position)
                .leftJoin(member.skills, skill)
                .leftJoin(member.degrees, degree)
                .leftJoin(member.careers, career)
                .leftJoin(member.projectDescriptions, projectDescription)
                .leftJoin(member.externalProjectDescriptions, externalProjectDescription)
                .where(eqMemberId(memberId))
                .fetchOne();

        return fetchedMember;
    }

    @Override
    public MemberWithLikeDto getMemberProfileRes(Long fromMemberId, Long toMemberId) {
        MemberWithLikeDto res = queryFactory
                .selectDistinct(new QMemberWithLikeDto(
                        member,
                        memberLike.isNotNull()
                ))
                .from(member)
                .leftJoin(member.portfolios, portfolio)
                .leftJoin(member.positions, position)
                .leftJoin(member.skills, skill)
                .leftJoin(member.degrees, degree)
                .leftJoin(member.careers, career)
                .leftJoin(member.projectDescriptions, projectDescription)
                .leftJoin(member.externalProjectDescriptions, externalProjectDescription)
                .leftJoin(member.receivedReviews, memberReview)
                .leftJoin(memberLike)
                .on(memberLike.fromMember.id.eq(fromMemberId).and(memberLike.toMember.id.eq(member.id)))
                .where(eqMemberId(toMemberId))
                .fetchOne();

        return res;
    }

    @Override
    public ContentWithTotalDto getFilteredMemberPage(Long memberId, ProfileCond cond, Pageable pageable) {
        List<MemberWithLikeDto> content = queryFactory
                .select(new QMemberWithLikeDto(
                        member,
                        memberLike.isNotNull()
                ))
                .from(member)
                .leftJoin(member.positions, position)
                .leftJoin(member.skills, skill)
                .leftJoin(member.careers, career)
                .leftJoin(memberLike)
                .on(memberLike.fromMember.id.eq(memberId).and(memberLike.toMember.id.eq(member.id)))
                .where(
                        positionFilter(cond.positionType()),
                        skillFilter(cond.skillType()),
                        memberStatusFilter(cond.memberStatus()),
                        keywordFilter(cond.keyword())
                )
                .orderBy(filterType(cond.filterType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        long total = queryFactory
                .select(member.countDistinct())
                .from(member)
                .join(member.positions, position)
                .join(member.skills, skill)
                .where(
                        positionFilter(cond.positionType()),
                        skillFilter(cond.skillType()),
                        memberStatusFilter(cond.memberStatus()),
                        keywordFilter(cond.keyword())
                )
                .fetchOne();

        return new ContentWithTotalDto(content, total);
    }

    private static BooleanExpression eqMemberId(Long memberId) {
        return member.id.eq(memberId);
    }


    private OrderSpecifier<?> filterType(FilterType filterType) {
        switch (filterType) {
            case LATEST -> member.updatedAt.desc();
            case HOTTEST -> member.views.desc();
        }
        return member.createdAt.desc();
    }

    private BooleanExpression positionFilter(PositionType positionType) {
        return positionType != null ? position.positionType.eq(positionType) : null;
    }

    private BooleanExpression skillFilter(SkillType skillType) {
        return skillType != null ? skill.skillType.eq(skillType) : null;
    }

    private BooleanExpression memberStatusFilter(MemberStatus memberStatus) {
        return memberStatus != null ? member.memberStatus.eq(memberStatus) : null;
    }

    private BooleanExpression keywordFilter(String keyword) {
        return keyword != null ? member.nickname.containsIgnoreCase(keyword) : null;
    }

    @Override
    public List<MemberWithLikeDto> findRecommendedMembers(List<SkillType> skills, Long memberId) {
        return queryFactory.select(new QMemberWithLikeDto(
                        member,
                        memberLike.isNotNull()
                ))
                .from(member)
                .join(member.skills, skill)
                .on(skill.skillType.in(skills))
                .leftJoin(memberLike)
                .on(memberLike.fromMember.id.eq(memberId).and(memberLike.toMember.id.eq(member.id)))
                .orderBy(member.embers.desc())
                .fetch();
    }
}
