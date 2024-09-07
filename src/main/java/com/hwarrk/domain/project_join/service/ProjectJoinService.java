package com.hwarrk.domain.project_join.service;

import com.hwarrk.domain.project_join.dto.req.ProjectJoinApplyReq;
import com.hwarrk.domain.project_join.dto.req.ProjectJoinDecideReq;
import com.hwarrk.global.page.PageRes;
import org.springframework.data.domain.Pageable;

public interface ProjectJoinService {
    void applyJoin(Long memberId, ProjectJoinApplyReq groupJoinApplyReq);

    void decide(Long loginId,Long projectJoinId, ProjectJoinDecideReq projectJoinDecideReq);

    PageRes getProjectJoins(Long loginId, Long projectJoinId, Pageable pageable);

    PageRes getMyProjectJoins(Long loginId, Pageable pageable);
}
