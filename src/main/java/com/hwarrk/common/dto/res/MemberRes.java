package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.Career;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.Period;
import java.util.Comparator;
import java.util.List;

@Builder
public record MemberRes(
        Long memberId,
        String image,
        String nickname,
        String career,
        Double embers,
        MemberStatus status,
        String description,
        boolean isLiked
) {

    public static MemberRes mapEntityToRes(Member member) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .career(getRepresentativeCareer(member.getCareers()))
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .description(member.getIntroduction())
                .build();
    }

    @QueryProjection
    public MemberRes(Member member, MemberLike memberLike) {
        this(
                member.getId(),
                member.getImage(),
                member.getNickname(),
                getRepresentativeCareer(member.getCareers()),
                member.getEmbers(),
                member.getMemberStatus(),
                member.getIntroduction(),
                isLiked(memberLike)
        );
    }

    public static String getRepresentativeCareer(List<Career> careers) {
        if (careers == null || careers.isEmpty())
            return "경력 없음";

        int totalYears = getTotalYears(careers);
        String mostRecentCompany = getMostRecentCompany(careers);

        return String.format("%d년차 %s", totalYears, mostRecentCompany);
    }

    private static String getMostRecentCompany(List<Career> careers) {
        return careers.stream()
                .max(Comparator.comparing(Career::getEndDate))
                .map(Career::getCompany)
                .get();
    }

    private static int getTotalYears(List<Career> careers) {
        return careers.stream()
                .mapToInt(career -> Period.between(career.getStartDate(), career.getEndDate()).getYears())
                .sum();
    }


    private static boolean isLiked(MemberLike memberLike) {
        return memberLike == null ? false : true;
    }
}
