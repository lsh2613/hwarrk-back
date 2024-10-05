package com.hwarrk.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PROJECT_LIKE")
public class ProjectLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectLike(Member member, Project project) {
        this.member = member;
        this.project = project;
    }

    public void addMember(Member member) {
        this.member = member;
        member.addProjectLike(this);
    }

    public void addProject(Project project) {
        this.project = project;
        project.addProjectLike(this);
    }
}
