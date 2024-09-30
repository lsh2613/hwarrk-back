package com.hwarrk.entity;

public enum ProjectStatus {

    COMPLETE("종료"), ONGOING("진행 중");

    private final String name;

    ProjectStatus(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return this == COMPLETE;
    }
}
