package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.common.util.PageUtil;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.hwarrk.repository.MemberLikeRepository;
import com.hwarrk.repository.MemberLikeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberLikeServiceImpl implements MemberLikeService {

    private final MemberLikeRepository memberLikeRepository;
    private final MemberLikeRepositoryCustom memberLikeRepositoryCustom;
    private final EntityFacade entityFacade;

    @Override
    public void likeMember(Long loginId, Long memberId, LikeType likeType) {
        Member fromMember = entityFacade.getMember(loginId);
        Member toMember = entityFacade.getMember(memberId);

        Optional<MemberLike> optionalMemberLike = memberLikeRepository.findByFromMemberAndToMember(fromMember, toMember);

        switch (likeType) {
            case LIKE -> handleLike(optionalMemberLike, fromMember, toMember);
            case CANCEL -> handleCancel(optionalMemberLike);
        }
    }

    @Override
    public SliceRes getLikedMemberSlice(Long memberId, Long lastMemberLikeId, Pageable pageable) {
        SliceCustomImpl likedMemberSlice = memberLikeRepositoryCustom.getLikedMemberSlice(memberId, lastMemberLikeId, pageable);

        return SliceRes.mapSliceCustomToSliceRes(likedMemberSlice, MemberRes::mapEntityToRes);
    }

    private void handleCancel(Optional<MemberLike> optionalMemberLike) {
        optionalMemberLike.ifPresentOrElse(
                memberLikeRepository::delete,
                () -> {
                    throw new GeneralHandler(ErrorStatus.MEMBER_LIKE_NOT_FOUND);
                }
        );
    }

    private void handleLike(Optional<MemberLike> optionalMemberLike, Member fromMember, Member toMember) {
        optionalMemberLike.ifPresent(memberLike -> {
            throw new GeneralHandler(ErrorStatus.MEMBER_LIKE_CONFLICT);
        });

        memberLikeRepository.save(new MemberLike(fromMember, toMember));
    }
}
