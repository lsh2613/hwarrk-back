package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
