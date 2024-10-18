package com.hwarrk.common.dto.res;

import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPostRes {

    private long postId;
    private String title;
    private String name;
    private String stepType;
    private List<RecruitingPositionDto> recruitingPositionDtos;

    public static MyPostRes mapEntityToRes(Post post) {
        MyPostRes myPostRes = new MyPostRes();
        myPostRes.postId = post.getId();
        myPostRes.title = post.getTitle();

        Project project = post.getProject();
        myPostRes.name = project.getName();
        myPostRes.stepType = project.getStep().name();

        myPostRes.recruitingPositionDtos = post.getPositions()
                .stream()
                .map(RecruitingPositionDto::mapEntityToDto)
                .toList();

        return myPostRes;
    }
}
