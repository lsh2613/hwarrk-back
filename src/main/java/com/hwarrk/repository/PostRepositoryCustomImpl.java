package com.hwarrk.repository;

import static com.hwarrk.entity.QPost.post;
import static com.hwarrk.entity.QPostLike.postLike;
import static com.hwarrk.entity.QProject.project;
import static com.hwarrk.entity.QRecruitingPosition.recruitingPosition;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.PostFilterType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.common.dto.dto.QPostWithLikeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PostWithLikeDto> findFilteredPost(PositionType positionType, WayType wayType, SkillType skillType,
                                                  PostFilterType filterType, String keyWord, long memberId) {

        JPAQuery<PostWithLikeDto> query = queryFactory.select(new QPostWithLikeDto(
                        post,
                        Expressions.booleanTemplate("CASE WHEN {0} IS NOT NULL THEN true ELSE false END",
                                postLike)
                ))
                .from(post)
                .join(post.project, project).fetchJoin()
                .leftJoin(post.postLikes, postLike).fetchJoin()
                .where(containsKeyWord(keyWord));

        // TODO -> EMPTY 가 아닌 경우 이슈 발생
        if (positionType != PositionType.EMPTY_POSITION) {
            query.join(post.positions, recruitingPosition)
                    .where(recruitingPosition.position.eq(positionType));
        }

        if (wayType != WayType.NONE) {
            query.where(project.way.eq(wayType));
        }

        if (skillType != SkillType.NONE) {
            query.where(post.skills.contains(skillType));
        }

        if (filterType == PostFilterType.TRENDING) {
            query.orderBy(post.views.desc());
        }

        if (filterType == PostFilterType.LATEST) {
            query.orderBy(post.createdAt.desc());
        }

        if (filterType == PostFilterType.FAVORITE) {
            query.where(postLike.member.id.eq(memberId));
        }

        return query.distinct().fetch();
    }

    private BooleanExpression containsKeyWord(String keyWord) {
        if (Optional.ofNullable(keyWord).isPresent() && !keyWord.trim().isEmpty()) {
            return post.title.containsIgnoreCase(keyWord);
        }
        return Expressions.TRUE;
    }

    @Override
    public List<PostWithLikeDto> findPostsBySkillsAndPositions(List<SkillType> skills, List<PositionType> positions,
                                                               Long memberId) {
        return queryFactory.select(new QPostWithLikeDto(
                        post,
                        Expressions.booleanTemplate(
                                "CASE WHEN {0} IS NOT NULL AND {1} = {2} THEN true ELSE false END",
                                postLike, postLike.member.id, memberId)
                ))
                .from(post)
                .leftJoin(post.postLikes, postLike).fetchJoin()
                // TODO 주석 한 부분 해제 시 문제 발생
//                .join(post.positions, recruitingPosition)
                .where(post.skills.any().in(skills))
//                        .and(recruitingPosition.position.in(positions)))
                .distinct()
                .fetch();

    }
}
