package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendProjectRes {

    private long postId;
    private List<PositionType> positionTypes = new ArrayList<>();
    private String title;
    private String image;
    private String name;
    private StepType stepType;
    private boolean isLiked;

    public static RecommendProjectRes createRes(ProjectWithLikeDto projectWithLikeDto) {
        RecommendProjectRes recommendProjectRes = new RecommendProjectRes();
        Project project = projectWithLikeDto.getProject();
        Post post = project.getPost();

        recommendProjectRes.postId = post.getId();
        recommendProjectRes.title = post.getTitle();
        recommendProjectRes.image = project.getImage();
        recommendProjectRes.name = project.getName();
        recommendProjectRes.stepType = project.getStep();
        recommendProjectRes.isLiked = projectWithLikeDto.isLiked();

        return recommendProjectRes;
    }
}
