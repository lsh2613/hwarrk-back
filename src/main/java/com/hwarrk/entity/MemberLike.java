package com.hwarrk.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER_LIKE")
public class MemberLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Builder
    public MemberLike(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
    }

    public void addFromMember(Member fromMember) {
        this.fromMember = fromMember;
        if (!fromMember.getSentLikes().contains(this)) {
            fromMember.addSentLike(this);
        }
    }

    public void addToMember(Member toMember) {
        this.toMember = toMember;
        if (!toMember.getReceivedLikes().contains(this)) {
            toMember.addReceivedLike(this);
        }
    }
}
