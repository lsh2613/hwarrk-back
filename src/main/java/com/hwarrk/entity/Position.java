package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "POSITION")
public class Position implements MemberAssignable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PositionType positionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Position(PositionType positionType, Member member) {
        this.positionType = positionType;
        this.member = member;
    }
}
