package com.hwarrk.repository;

import static com.hwarrk.entity.QMember.member;
import static com.hwarrk.entity.QPosition.position;
import static com.hwarrk.entity.QPost.post;
import static com.hwarrk.entity.QProject.project;
import static com.hwarrk.entity.QProjectLike.projectLike;
import static com.hwarrk.entity.QRecruitingPosition.recruitingPosition;

import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.Project;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<Project> findFilteredProjects(RecruitingType recruitingType, ProjectFilterType filterType,
                                               String keyWord, Long memberId, Pageable pageable) {

        JPQLQuery<Project> query = queryFactory.selectFrom(project)
                .join(project.post, post)
                .where(post.recruitingType.eq(recruitingType));

        if (filterType == ProjectFilterType.TRENDING) {
            // TODO: 인기 급상승 기준 추가
        }

        if (filterType == ProjectFilterType.LATEST) {
            query.orderBy(project.createdAt.desc());
        }

        if (filterType == ProjectFilterType.FAVORITE) {
            query.join(project.projectLikes, projectLike)
                    .on(projectLike.member.id.eq(memberId));
        }

        List<Project> projects = query
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(projects, pageable, PageUtil.hasNextPage(projects, pageable));
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
