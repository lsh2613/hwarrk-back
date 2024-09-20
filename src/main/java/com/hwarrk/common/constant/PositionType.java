package com.hwarrk.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    PM("PM"), PO("PO"), SERVICE_PLANNER("서비스 기획자"),
    GRAPHIC_DESIGNER("그래픽 디자이너"), UXUI_DESIGNER("UX/UI Designer"), THREE_D_DESIGNER("3D 디자이너"), MOTION_DESIGNER("모션 디자이너"),
    IOS("IOS"), ANDROID("안드로이드"), WEB("웹"),
    BACKEND("백엔드"),
    INFRA("인프라");

    private final String name;
}
