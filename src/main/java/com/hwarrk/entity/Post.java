package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitingPosition> positions = new ArrayList<>();

    private String title;

    private String body;

    private Integer views;

    private Integer likes;

    private boolean isVisible;

    public void addRecruitingPosition(RecruitingPosition recruitingPosition) {
        if (positions == null) positions = new ArrayList<>();
        positions.add(recruitingPosition);
        recruitingPosition.setPost(this);
    }

    public void addRecruitingPositions(List<RecruitingPosition> recruitingPositions) {
        if (positions == null) positions = new ArrayList<>();
        positions.forEach(this::addRecruitingPosition);
    }

}
