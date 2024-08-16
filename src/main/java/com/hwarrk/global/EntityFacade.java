package com.hwarrk.global;

import com.hwarrk.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.global.common.exception.GeneralHandler;
import com.hwarrk.member.entity.Member;
import com.hwarrk.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class EntityFacade {
    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        Optional<Member> memberById = memberRepository.findById(memberId);
        if (memberById.isEmpty())
            throw new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND);
        return memberById.get();
    }

}
