package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "RECRUITING_POSITION")
public class RecruitingPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiting_position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private PositionType position;

    private Integer cnt; // 모집 인원

    public RecruitingPosition(Post post, PositionType position, Integer cnt) {
        this.post = post;
        this.position = position;
        this.cnt = cnt;
    }

    public RecruitingPosition(PositionType position, Integer cnt) {
        this.position = position;
        this.cnt = cnt;
    }

    public static RecruitingPosition create(String positionType, int cnt) {
        RecruitingPosition recruitingPosition = new RecruitingPosition();
        recruitingPosition.position = PositionType.findType(positionType);
        recruitingPosition.cnt = cnt;
        return recruitingPosition;
    }

    public void addPost(Post post) {
        this.post = post;
        post.addRecruitingPosition(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecruitingPosition that = (RecruitingPosition) o;
        return position == that.position && Objects.equals(cnt, that.cnt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, cnt);
    }
}
