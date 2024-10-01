package com.hwarrk.entity;

import com.hwarrk.common.constant.RecruitingType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "POST")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String body;

    private Integer views;

    private Integer likes;

    private boolean isVisible;

    @Enumerated(EnumType.STRING)
    private RecruitingType recruitingType;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecruitingPosition> positions = new ArrayList<>();

    public void addRecruitingPosition(RecruitingPosition recruitingPosition) {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        positions.add(recruitingPosition);
        recruitingPosition.setPost(this);
    }

    public void addRecruitingPositions(List<RecruitingPosition> recruitingPositions) {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        positions.forEach(this::addRecruitingPosition);
    }

    @Builder
    public Post(Project project, Member member, List<RecruitingPosition> positions, String title, String body,
                Integer views, Integer likes, boolean isVisible) {
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
