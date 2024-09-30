package com.hwarrk.entity;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.exception.GeneralHandler;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void updateProject(Project project) {
        this.name = project.getName();
        this.step = project.getStep();
        this.domain = project.getDomain();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.way = project.getWay();
        this.area = project.getArea();
        this.subject = project.getSubject();
        this.image = project.getImage();
        this.description = project.getDescription();
    }

    public boolean isProjectLeader(Long loginId) {
        if (!leader.isSameId(loginId)) {
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
        }
        return true;
    }
}
