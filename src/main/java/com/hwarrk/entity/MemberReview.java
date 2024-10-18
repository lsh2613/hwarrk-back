package com.hwarrk.entity;

import com.hwarrk.common.constant.MemberReviewTag;
import com.hwarrk.common.constant.MemberReviewTagType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER_REVIEW")
public class MemberReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private MemberReviewTag tag;

    @Enumerated(EnumType.STRING)
    private MemberReviewTagType tagType;

    @Builder
    public MemberReview(Project project, Member fromMember, Member toMember, MemberReviewTag tag, MemberReviewTagType tagType) {
        this.project = project;
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.tag = tag;
        this.tagType = tagType;
    }

    public void addToMember(Member member) {
        this.toMember = member;
        if (!member.getReceivedReviews().contains(this)) {
            member.addReceivedReviews(this);
        }
    }

}
