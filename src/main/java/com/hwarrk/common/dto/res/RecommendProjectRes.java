package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.StepType;
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

    public static RecommendProjectRes createRes(Project project) {
        RecommendProjectRes recommendProjectRes = new RecommendProjectRes();
        Post post = project.getPost();
        recommendProjectRes.postId = post.getId();
        recommendProjectRes.title = post.getTitle();
        recommendProjectRes.image = project.getImage();
        recommendProjectRes.name = project.getName();
        recommendProjectRes.stepType = project.getStep();
        return recommendProjectRes;
    }
}
