package com.hwarrk.domain.project_join.service;

import com.hwarrk.domain.project_join.dto.req.ProjectJoinApplyReq;
import com.hwarrk.domain.project_join.dto.req.ProjectJoinDecideReq;
import com.hwarrk.domain.project_join.dto.res.ProjectJoinPageRes;
import org.springframework.data.domain.Pageable;

public interface ProjectJoinService {
    void applyJoin(Long memberId, ProjectJoinApplyReq groupJoinApplyReq);

    void decide(Long loginId,Long projectJoinId, ProjectJoinDecideReq projectJoinDecideReq);

    ProjectJoinPageRes getProjectJoins(Long loginId, Long projectJoinId, Pageable pageable);

    ProjectJoinPageRes getMyProjectJoins(Long loginId, Pageable pageable);
}
