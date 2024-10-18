package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostWithLikeDto {

    private Post post;
    private boolean isLiked;

    @QueryProjection
    public PostWithLikeDto(Post post, boolean isLiked) {
        this.post = post;
        this.isLiked = isLiked;
    }
}
