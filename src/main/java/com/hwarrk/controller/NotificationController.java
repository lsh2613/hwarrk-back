package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "모든 알림 조회", description = "첫 조회는 lastNotificationId = 0으로 호출")
    @GetMapping
    public CustomApiResponse getNotifications(@AuthenticationPrincipal Long loginId,
                                              @RequestParam Long lastNotificationId,
                                              @PageableDefault Pageable pageable) {
        SliceRes<NotificationRes> res = notificationService.getNotifications(loginId, lastNotificationId, pageable);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "알림 읽기", description = "NotificationBindingType에 맞는 bindingId가 반환",
            responses = {
                    @ApiResponse(responseCode = "COMMON200", description = "성공, isRead=true로 업데이트"),
                    @ApiResponse(responseCode = "MEMBER4031", description = "사용자에게 권한이 없습니다")
            })
    @GetMapping("{notificationId}")
    public CustomApiResponse readNotification(@AuthenticationPrincipal Long loginId,
                                              @PathVariable Long notificationId) {
        NotificationRes res = notificationService.readNotification(loginId, notificationId);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "모든 알림 읽기")
    @PatchMapping
    public CustomApiResponse readNotifications(@AuthenticationPrincipal Long loginId) {
        notificationService.readNotifications(loginId);
        return CustomApiResponse.onSuccess();
    }


}
