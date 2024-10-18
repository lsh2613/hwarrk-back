package com.hwarrk.common.constant;

import java.util.Optional;

public enum SkillType {

    JAVA,
    SPRING,
    JPA,
    MYBATIS,
    PYTHON,
    DJANGO,
    FAST_API,
    NODE_JS,
    AWS,
    JAVASCRIPT,
    TYPESCRIPT,
    VERCEL,
    NEXT_JS,
    NEST_JS,
    REACT_JS,
    VUE_JS,
    REDUX,
    RECOIL,
    FIGMA,
    SKETCH,
    PROTO_PIE,
    PHOTOSHOP,
    ILLUSTRATION,
    FRAMER,
    BLENDER,
    AFTER_EFFECTS,
    NONE;

    public static SkillType findType(String skill) {
        if (Optional.ofNullable(skill).isEmpty()) {
            return NONE;
        }
        try {
            return SkillType.valueOf(skill.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 스킬에 맞는 SkillType이 없습니다: " + skill);
        }
    }
}
