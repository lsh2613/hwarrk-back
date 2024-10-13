package com.hwarrk.common.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    PM("PM"), PO("PO"), SERVICE_PLANNER("서비스 기획자"),
    GRAPHIC_DESIGNER("그래픽 디자이너"), UXUI_DESIGNER("UX/UI Designer"), THREE_D_DESIGNER("3D 디자이너"), MOTION_DESIGNER(
            "모션 디자이너"),
    IOS("IOS"), ANDROID("안드로이드"), FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    INFRA("인프라");

    private final String name;

    public static PositionType findType(String positionType) {
        return Arrays.stream(values()).filter(v -> v.name.equals(positionType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포지션 타입 입니다."));
    }
}
