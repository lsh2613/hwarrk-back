package com.hwarrk.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Entity
@Getter
@Table(name = "CAREER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career implements MemberAssignable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;
    private String company;
    private String domain; //직군
    private String job; //직무
    private String startDate;
    private String endDate;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Career(String company, String domain, String job, String startDate, String endDate, String description, Member member) {
        this.company = company;
        this.domain = domain;
        this.job = job;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.member = member;
    }
}
