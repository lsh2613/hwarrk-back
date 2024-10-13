package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.entity.Member;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberRes {
    private long memberId;
    private String image;
    private String nickname;
    private CareerInfoRes careerInfoRes;
    private Double embers;
    private MemberStatus status;
    private String introduction;
    private boolean isLiked;

    public MemberRes(CareerInfoRes careerInfoRes) {
        this.careerInfoRes = careerInfoRes;
        this.embers = 0.0;
    }

    public static MemberRes mapEntityToRes(Member member, CareerInfoRes careerInfoRes) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .careerInfoRes(careerInfoRes)
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .build();
    }

    public static MemberRes mapEntityToRes(Member member, CareerInfoRes careerInfoRes, boolean liked) {
        return MemberRes.builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .careerInfoRes(careerInfoRes)
                .embers(member.getEmbers())
                .status(member.getMemberStatus())
                .introduction(member.getIntroduction())
                .isLiked(liked)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberRes memberRes = (MemberRes) o;
        return isLiked == memberRes.isLiked && Objects.equals(memberId, memberRes.memberId)
                && Objects.equals(image, memberRes.image) && Objects.equals(nickname,
                memberRes.nickname) && Objects.equals(careerInfoRes, memberRes.careerInfoRes)
                && Objects.equals(embers, memberRes.embers) && status == memberRes.status
                && Objects.equals(introduction, memberRes.introduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, image, nickname, careerInfoRes, embers, status, introduction, isLiked);
    }

    @Override
    public String toString() {
        return "MemberRes{" +
                "memberId=" + memberId +
                ", image='" + image + '\'' +
                ", nickname='" + nickname + '\'' +
                ", careerInfoRes=" + careerInfoRes +
                ", embers=" + embers +
                ", status=" + status +
                ", introduction='" + introduction + '\'' +
                ", isLiked=" + isLiked +
                '}';
    }
}
