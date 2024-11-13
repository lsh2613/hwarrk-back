package com.hwarrk.entity;

import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.exception.GeneralHandler;
import com.querydsl.core.annotations.QueryInit;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Enumerated(value = EnumType.STRING)
    private StepType step;

    private String domain;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    private WayType way;

    private String area;

    private String subject;

    private String description;

    private String image;

    private boolean isVisible;

    private long views;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @QueryInit("*")
    @OneToOne(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Post post;

    @QueryInit("*")
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 10)
    private Set<ProjectMember> projectMembers = new LinkedHashSet<>();

    @QueryInit("*")
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 10)
    private Set<ProjectJoin> projectJoins = new LinkedHashSet<>();

    @QueryInit("*")
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 10)
    private Set<ProjectLike> projectLikes = new LinkedHashSet<>();

    public Project(Long id, String name, Member leader, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.startDate = startDate;
    }

    public Project(String name, Member leader, LocalDate startDate) {
        this.name = name;
        this.leader = leader;
        this.startDate = startDate;
    }

    public Project(WayType way) {
        this.way = way;
    }

    @Builder
    public Project(String name, String description, Member leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.projectMembers = new LinkedHashSet<>();
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

    public Project(Long id) {
        this.id = id;
    }

    public void updateProject(Project project, String imageUrl) {
        this.name = project.getName();
        this.step = project.getStep();
        this.domain = project.getDomain();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.way = project.getWay();
        this.area = project.getArea();
        this.subject = project.getSubject();
        this.image = imageUrl;
        this.description = project.getDescription();
    }

    public void addLeader(Member member) {
        this.leader = member;
    }

    public void addPost(Post post) {
        this.post = post;
    }

    public void addProjectJoin(ProjectJoin projectJoin) {
        if (Optional.ofNullable(this.projectJoins).isEmpty()) {
            this.projectJoins = new LinkedHashSet<>();
        }
        this.projectJoins.add(projectJoin);
    }

    public void addProjectLike(ProjectLike projectLike) {
        if (Optional.ofNullable(projectLikes).isEmpty()) {
            projectLikes = new LinkedHashSet<>();
        }
        this.projectLikes.add(projectLike);
    }

    public boolean isProjectLeader(Long loginId) {
        if (!leader.isSameId(loginId)) {
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_REQUIRED);
        }
        return true;
    }

    public void completeProject() {
        this.projectStatus = ProjectStatus.COMPLETE;
    }

    public boolean isComplete() {
        if (!projectStatus.isComplete()) {
            throw new GeneralHandler(ErrorStatus.PROJECT_INCOMPLETE);
        }
        return true;
    }

    public void incrementViews() {
        this.views += 1;
    }
}
