package com.hwarrk.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private LocalDate startDate;
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

    public Period calculateExperience() {
        return Period.between(startDate, endDate);
    }
}
