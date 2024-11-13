package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectLike;
import com.hwarrk.repository.ProjectLikeRepository;
import com.hwarrk.repository.ProjectLikeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProjectLikeServiceImpl implements ProjectLikeService {

    private final EntityFacade entityFacade;
    private final ProjectLikeRepository projectLikeRepository;
    private final ProjectLikeRepositoryCustom projectLikeRepositoryCustom;

    @Override
    public void likeProject(Long memberId, Long projectId, LikeType likeType) {
        Member member = entityFacade.getMember(memberId);
        Project project = entityFacade.getProject(projectId);
        Optional<ProjectLike> optionalProjectLike = projectLikeRepository.findByMemberAndProject(member, project);

        switch (likeType) {
            case LIKE -> handleLike(optionalProjectLike, member, project);
            case CANCEL -> handleCancel(optionalProjectLike);
        }
    }

    @Override
    public SliceRes getMyLikedProjectCards(Long loginId, Long lastProjectLikeId, Pageable pageable) {
        List<ProjectLike> projectLikes = projectLikeRepositoryCustom.getProjectLikes(loginId, lastProjectLikeId, pageable);

        List<ProjectRes> projectResList = projectLikes.stream()
                .map(projectLike -> ProjectRes.mapEntityToRes(projectLike.getProject()))
                .collect(Collectors.toCollection(ArrayList::new));

        SliceCustomImpl sliceCustom = new SliceCustomImpl(projectLikes, projectResList, pageable);

        return SliceRes.mapSliceCustomToSliceRes(sliceCustom);
    }

    private void handleCancel(Optional<ProjectLike> optionalProjectLike) {
        optionalProjectLike.ifPresentOrElse(
                projectLikeRepository::delete,
                () -> {
                    throw new GeneralHandler(ErrorStatus.PROJECT_LIKE_NOT_FOUND);
                }
        );
    }

    private void handleLike(Optional<ProjectLike> optionalProjectLike, Member member, Project project) {
        optionalProjectLike.ifPresent(projectLike -> {
            throw new GeneralHandler(ErrorStatus.PROJECT_LIKE_CONFLICT);
        });

        projectLikeRepository.save(new ProjectLike(member, project));
    }
}
