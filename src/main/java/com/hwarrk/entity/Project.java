package com.hwarrk.entity;

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

    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member leader;

    @Builder
    public Project(String name, String description, Member leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }
}
