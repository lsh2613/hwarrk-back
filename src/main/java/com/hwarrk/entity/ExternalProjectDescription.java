package com.hwarrk.entity;

import com.hwarrk.common.constant.PositionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "EXTERNAL_PROJECT_DESCRIPTION")
public class ExternalProjectDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "external_project_description_id")
    private Long id;

    private String name;

    private String domain;

    private LocalDate startDate;

    private LocalDate endDate;

    private PositionType positionType;

    private String subject;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ExternalProjectDescription(String name, String domain, LocalDate startDate, LocalDate endDate, PositionType positionType, String subject, String description, ProjectStatus projectStatus, Member member) {
        this.name = name;
        this.domain = domain;
        this.startDate = startDate;
        this.endDate = endDate;
        this.positionType = positionType;
        this.subject = subject;
        this.description = description;
        this.projectStatus = projectStatus;
        this.member = member;
    }
}
