package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.StepType;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.RecruitingPosition;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyProjectRes {

    private long projectId;
    private String name;
    private StepType stepType;
    private List<PositionRes> positionResList = new ArrayList<>();

    public static MyProjectRes createRes(Project project) {
        MyProjectRes myProjectRes = new MyProjectRes();
        myProjectRes.projectId = project.getId();
        myProjectRes.name = project.getName();
        myProjectRes.stepType = project.getStep();
        myProjectRes.positionResList = project.getPost().getPositions()
                .stream()
                .map(PositionRes::createRes)
                .toList();
        return myProjectRes;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    static class PositionRes {
        private PositionType positionType;
        private int cnt;

        public static PositionRes createRes(RecruitingPosition position) {
            PositionRes positionRes = new PositionRes();
            positionRes.positionType = position.getPosition();
            positionRes.cnt = position.getCnt();
            return positionRes;
        }
    }
}
