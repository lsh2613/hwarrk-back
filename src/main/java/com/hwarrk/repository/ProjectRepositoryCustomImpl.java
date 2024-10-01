package com.hwarrk.repository;

import static com.hwarrk.entity.QMember.member;
import static com.hwarrk.entity.QPosition.position;
import static com.hwarrk.entity.QPost.post;
import static com.hwarrk.entity.QProject.project;
import static com.hwarrk.entity.QProjectLike.projectLike;
import static com.hwarrk.entity.QRecruitingPosition.recruitingPosition;

import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.entity.Project;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Project> findFilteredProjects(RecruitingType recruitingType, ProjectFilterType filterType,
                                              String keyWord, Long memberId) {

        JPQLQuery<Project> query = queryFactory.selectFrom(project)
                .join(project.post, post)
                .where(post.recruitingType.eq(recruitingType));

        switch (filterType) {
            case TRENDING:
                // TODO: 인기 급상승 기준 추가
                break;

            case LATEST:
                query.orderBy(project.startDate.desc());
                break;

            case FAVORITE:
                query.join(project.projectLikes, projectLike)
                        .on(projectLike.member.id.eq(memberId));
                break;

            default:
                throw new IllegalArgumentException("프로젝트 허브 필터링 조건이 올바르지 않습니다.");
        }

        return query.fetch();
    }


    @Override
    public List<Project> findRecommendedProjects(Long memberId) {
        return queryFactory.selectFrom(project)
                .join(project.post, post)
                .join(post.positions, recruitingPosition)
                .join(member).on(member.id.eq(memberId))
                .join(member.positions, position)
                .where(
                        recruitingPosition.position.eq(position.positionType)
                        // TODO skill 이 1개 이상 일치 로직 필요
                )
                .fetch();
    }
}
