package com.hwarrk.entity;

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
public class Post extends BaseEntity {

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

    @Builder
    public Post(Project project, Member member, List<RecruitingPosition> positions, String title, String body, Integer views, Integer likes, boolean isVisible) {
        this.project = project;
        this.member = member;
        this.positions = positions;
        this.title = title;
        this.body = body;
        this.views = views;
        this.likes = likes;
        this.isVisible = isVisible;
    }
}
