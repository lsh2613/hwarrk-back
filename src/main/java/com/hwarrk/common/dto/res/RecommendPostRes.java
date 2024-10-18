package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendPostRes {

    private long postId;
    private String title;
    private String name;
    private StepType stepType;
    private boolean isLiked;
    private List<RecruitingPositionDto> recruitingPositionDtoList;

    public static RecommendPostRes createRes(PostWithLikeDto postWithLikeDto) {
        RecommendPostRes recommendProjectRes = new RecommendPostRes();

        Post post = postWithLikeDto.getPost();
        recommendProjectRes.postId = post.getId();
        recommendProjectRes.title = post.getTitle();

        Project project = post.getProject();
        recommendProjectRes.name = project.getName();
        recommendProjectRes.stepType = project.getStep();
        recommendProjectRes.isLiked = postWithLikeDto.isLiked();

        recommendProjectRes.recruitingPositionDtoList = post.getPositions()
                .stream()
                .map(RecruitingPositionDto::mapEntityToDto)
                .toList();

        return recommendProjectRes;
    }
}
