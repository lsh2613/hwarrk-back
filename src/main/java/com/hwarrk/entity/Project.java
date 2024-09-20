package com.hwarrk.entity;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member leader;

    private String name;

    private StepType step;

    private String domain;

    private String startDate;

    private String endDate;

    private WayType way;

    private String area;

    private String subject;

    private String description;

    private String image;

    private boolean isVisible;

    @Builder
    public Project(String name, String description, Member leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }
}
