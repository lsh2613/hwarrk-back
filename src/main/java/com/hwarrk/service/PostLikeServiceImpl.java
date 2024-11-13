package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.PostRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.PostLike;
import com.hwarrk.repository.PostLikeRepository;
import com.hwarrk.repository.PostLikeRepositoryCustom;
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
public class PostLikeServiceImpl implements PostLikeService {

    private final EntityFacade entityFacade;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeRepositoryCustom postLikeRepositoryCustom;

    @Override
    public void likePost(Long memberId, Long postId, LikeType likeType) {
        Member member = entityFacade.getMember(memberId);
        Post post = entityFacade.getPost(postId);
        Optional<PostLike> optionalPostLike = postLikeRepository.findByMemberAndPost(member, post);

        switch (likeType) {
            case LIKE -> handleLike(optionalPostLike, member, post);
            case CANCEL -> handleCancel(optionalPostLike);
        }
    }

    @Override
    public SliceRes getMyLikedPostCards(Long loginId, Long lastPostLikeId, Pageable pageable) {
        List<PostLike> postLikes = postLikeRepositoryCustom.getPostLikes(loginId, lastPostLikeId, pageable);

        List<PostRes> postResList = postLikes.stream()
                .map(postLike -> PostRes.mapEntityToRes(postLike.getPost()))
                .collect(Collectors.toCollection(ArrayList::new));

        SliceCustomImpl sliceCustom = new SliceCustomImpl(postLikes, postResList, pageable);

        return SliceRes.mapSliceCustomToSliceRes(sliceCustom);
    }

    private void handleCancel(Optional<PostLike> optionalPostLike) {
        optionalPostLike.ifPresentOrElse(
                postLikeRepository::delete,
                () -> {
                    throw new GeneralHandler(ErrorStatus.POST_LIKE_NOT_FOUND);
                }
        );
    }

    private void handleLike(Optional<PostLike> optionalPostLike, Member member, Post post) {
        optionalPostLike.ifPresent(postLike -> {
            throw new GeneralHandler(ErrorStatus.POST_LIKE_CONFLICT);
        });

        postLikeRepository.save(new PostLike(member, post));
    }
}

