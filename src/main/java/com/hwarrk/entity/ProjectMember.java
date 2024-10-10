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
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PROJECT_MEMBER")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PositionType position;

    public ProjectMember(Member member, Project project, PositionType position) {
        this.member = member;
        this.project = project;
        this.position = position;
    }

    public void addMember(Member member) {
        this.member = member;
        member.addProjectMember(this);
    }

    @Override
    public String toString() {
        return "ProjectMember{" +
                "id=" + id +
                ", memberId=" + (member != null ? member.getId() : null) + // member ID 추가
                ", projectId=" + (project != null ? project.getId() : null) + // project ID 추가
                ", position=" + position +
                '}';
    }
}
