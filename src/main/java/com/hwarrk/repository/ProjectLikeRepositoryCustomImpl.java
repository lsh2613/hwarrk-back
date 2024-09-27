package com.hwarrk.repository;

import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.Project;
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
    public SliceCustomImpl getLikedProjectSlice(Long memberId, Long lastProjectLikeId, Pageable pageable) {
        List<ProjectLike> projectLikes = getProjectLikes(memberId, lastProjectLikeId, pageable);

        boolean hasNext = PageUtil.hasNextPage(projectLikes, pageable);

        List<Project> likedProjects = projectLikes.stream()
                .map(ProjectLike::getProject)
                .toList();

        return new SliceCustomImpl(likedProjects, pageable, hasNext, PageUtil.getLastElement(projectLikes).getId());
    }

    private List<ProjectLike> getProjectLikes(Long memberId, Long lastProjectLikeId, Pageable pageable) {
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
