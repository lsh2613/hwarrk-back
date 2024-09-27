package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.entity.Post;
import lombok.Builder;

import java.util.List;

@Builder
public record PostRes(
        List<PositionType> recruitingPositions,
        String title,
        String projectName,
        String projectStep
) {
    static public PostRes mapEntityToRes(Post post) {
        return PostRes.builder()
                .recruitingPositions(
                        post.getPositions().stream()
                                .map(position -> position.getPosition())
                                .toList()
                )
                .projectName(post.getProject().getName())
                .build();
    }
}
