package com.hwarrk.entity;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PROJECT")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    private String name;

    private StepType step;

    private String domain;

    private LocalDate startDate;

    private LocalDate endDate;

    private WayType way;

    private String area;

    private String subject;

    private String description;

    private String image;

    private boolean isVisible;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToOne(mappedBy = "project", fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Project(String name, String description, Member leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }

    @Builder
    public Project(String name, StepType step, String domain, LocalDate startDate, LocalDate endDate, WayType way,
                   String area, String subject, String image, String description) {
        this.name = name;
        this.step = step;
        this.domain = domain;
        this.startDate = startDate;
        this.endDate = endDate;
        this.way = way;
        this.area = area;
        this.subject = subject;
        this.image = image;
        this.description = description;
    }
}
