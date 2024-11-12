package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
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
    public CustomApiResponse<SliceRes<NotificationRes>> getNotifications(@AuthenticationPrincipal Long loginId,
                                                                         @RequestParam Long lastNotificationId,
                                                                         @PageableDefault Pageable pageable) {
        SliceRes<NotificationRes> res = notificationService.getNotifications(loginId, lastNotificationId, pageable);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "알림 읽기", description = "NotificationBindingType에 맞는 bindingId가 반환")
    @GetMapping("{notificationId}")
    public CustomApiResponse readNotification(@AuthenticationPrincipal Long loginId,
                                              @PathVariable Long notificationId) {
        notificationService.readNotification(loginId, notificationId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "모든 알림 읽기")
    @PatchMapping
    public CustomApiResponse readNotifications(@AuthenticationPrincipal Long loginId) {
        notificationService.readNotifications(loginId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "안 읽은 알림 갯수 조회")
    @GetMapping("/unread")
    public CustomApiResponse<Integer> countUnreadNotifications(@AuthenticationPrincipal Long loginId) {
        Integer cnt = notificationService.countUnreadNotifications(loginId);
        return CustomApiResponse.onSuccess(cnt);
    }

}
