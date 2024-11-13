package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.SliceCustomImpl;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.hwarrk.repository.MemberLikeRepository;
import com.hwarrk.repository.MemberLikeRepositoryCustom;
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
    public SliceRes<MemberCardRes> getMyLikedMemberCards(Long memberId, Long lastMemberLikeId, Pageable pageable) {
        List<MemberLike> memberLikes = memberLikeRepositoryCustom.getMemberLikes(memberId, lastMemberLikeId, pageable);

        List<MemberCardRes> memberCardResList = memberLikes.stream()
                .map(memberLike -> {
                    Member member = memberLike.getToMember();
                    CareerInfoRes careerInfoRes = CareerInfoRes.mapEntityToRes(member.loadCareer());
                    return MemberCardRes.mapEntityToRes(member, careerInfoRes, true);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        SliceCustomImpl sliceCustom = new SliceCustomImpl(memberLikes, memberCardResList, pageable);

        return SliceRes.mapSliceCustomToSliceRes(sliceCustom);
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
