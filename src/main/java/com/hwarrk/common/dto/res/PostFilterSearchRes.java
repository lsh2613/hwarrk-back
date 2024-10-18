package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class PostFilterSearchRes {

    private long postId;
    private String title;
    private String name;
    private StepType stepType;
    private boolean isLiked;
    private List<RecruitingPositionDto> recruitingPositionDtoList;

    public static PostFilterSearchRes createRes(PostWithLikeDto postWithLikeDto) {
        PostFilterSearchRes postFilterSearchRes = new PostFilterSearchRes();

        Post post = postWithLikeDto.getPost();
        postFilterSearchRes.postId = post.getId();
        postFilterSearchRes.title = post.getTitle();

        Project project = post.getProject();
        postFilterSearchRes.name = project.getName();
        postFilterSearchRes.stepType = project.getStep();
        postFilterSearchRes.isLiked = postFilterSearchRes.isLiked();

        postFilterSearchRes.recruitingPositionDtoList = post.getPositions()
                .stream()
                .map(RecruitingPositionDto::mapEntityToDto)
                .toList();

        return postFilterSearchRes;
    }
}
