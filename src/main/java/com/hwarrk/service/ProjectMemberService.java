package com.hwarrk.service;

import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.ProjectRes;

import java.util.List;

public interface ProjectMemberService {
    List<MemberRes> getMembersInProject(Long memberId, Long projectId);
    List<ProjectRes> getMyProjects(Long memberId);
    void removeProjectMember(Long loginId, Long projectId, Long memberId);
}
