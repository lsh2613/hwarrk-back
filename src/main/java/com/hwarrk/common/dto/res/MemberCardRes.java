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
public class MemberCardRes {
    private long memberId;
    private String image;
    private String nickname;
    private CareerInfoRes careerInfoRes;
    private Double embers;
    private MemberStatus status;
    private String introduction;
    private boolean isLiked;

    public MemberCardRes(CareerInfoRes careerInfoRes) {
        this.careerInfoRes = careerInfoRes;
        this.embers = 0.0;
    }

    public static MemberCardRes mapEntityToRes(Member member, CareerInfoRes careerInfoRes, boolean liked) {
        return MemberCardRes.builder()
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
        MemberCardRes memberCardRes = (MemberCardRes) o;
        return isLiked == memberCardRes.isLiked && Objects.equals(memberId, memberCardRes.memberId)
                && Objects.equals(image, memberCardRes.image) && Objects.equals(nickname,
                memberCardRes.nickname) && Objects.equals(careerInfoRes, memberCardRes.careerInfoRes)
                && Objects.equals(embers, memberCardRes.embers) && status == memberCardRes.status
                && Objects.equals(introduction, memberCardRes.introduction);
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
