package com.hwarrk.entity;

public enum CareerType {

    ENTRY_LEVEL("신입"), EXPERIENCE("경력");

    private final String name;

    CareerType(String name) {
        this.name = name;
    }
}
