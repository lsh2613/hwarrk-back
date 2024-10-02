package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PROJECT_JOIN")
public class ProjectJoin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_join_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PositionType positionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public ProjectJoin(PositionType positionType) {
        this.positionType = positionType;
    }

    public void addProject(Project project) {
        this.project = project;
        project.addProjectJoin(this);
    }
}
