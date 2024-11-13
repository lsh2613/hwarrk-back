package com.hwarrk.service;

import com.hwarrk.common.constant.JoinDecide;
import com.hwarrk.common.dto.req.ProjectJoinApplyReq;
import com.hwarrk.common.dto.req.ProjectJoinDecideReq;
import com.hwarrk.common.dto.res.PageRes;
import org.springframework.data.domain.Pageable;

public interface ProjectJoinService {
    void applyJoin(Long memberId, ProjectJoinApplyReq groupJoinApplyReq);

    void decide(Long loginId,Long projectJoinId, JoinDecide joinDecide);

    PageRes getProjectJoins(Long loginId, Long projectId, Pageable pageable);

    PageRes getMyProjectJoins(Long loginId, Pageable pageable);
}
