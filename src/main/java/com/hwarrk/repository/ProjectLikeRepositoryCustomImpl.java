package com.hwarrk.repository;

import com.hwarrk.entity.ProjectLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hwarrk.entity.QProject.project;
import static com.hwarrk.entity.QProjectLike.projectLike;

@Repository
@RequiredArgsConstructor
public class ProjectLikeRepositoryCustomImpl implements ProjectLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProjectLike> getProjectLikes(Long memberId, Long lastProjectLikeId, Pageable pageable) {
        return jpaQueryFactory
                .select(projectLike)
                .from(projectLike)
                .join(projectLike.project, project).fetchJoin()
                .where(
                        ltProjectLikeId(lastProjectLikeId),
                        eqMemberId(memberId)
                )
                .orderBy(projectLike.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return projectLike.member.id.eq(memberId);
    }

    private BooleanExpression ltProjectLikeId(Long lastProjectLikeId) {
        return lastProjectLikeId == null ? null : projectLike.id.lt(lastProjectLikeId);
    }
}
