package com.hwarrk.common.constant;

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
    AFTER_EFFECTS;

    public static SkillType findType(String skill) {
        try {
            return SkillType.valueOf(skill.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 스킬에 맞는 SkillType이 없습니다: " + skill);
        }
    }
}
