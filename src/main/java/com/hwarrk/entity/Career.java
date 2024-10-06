package com.hwarrk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Setter
@Entity
@Getter
@Table(name = "CAREER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;
    @Column(nullable = false)
    private String company;
    private String domain; //직군
    private String job; //직무
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Career(String company, String domain, String job, LocalDate startDate, LocalDate endDate, String description,
                  Member member) {
        this.company = company;
        this.domain = domain;
        this.job = job;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.member = member;
    }

    public Career(String company, LocalDate startDate, LocalDate endDate, Member member) {
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }

    public Period calculateExperience() {
        return Period.between(startDate, endDate);
    }
}
