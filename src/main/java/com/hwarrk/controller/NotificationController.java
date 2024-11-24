package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.NotificationRes;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "모든 알림 조회", description = "알림마다 이동해야 되는 페이지가 존재. 각 알림에는 NotificationBindingType에 맞는 bindingId가 반환")
    @GetMapping
    public CustomApiResponse<SliceRes<NotificationRes>> getNotifications(@AuthenticationPrincipal Long loginId,
                                                                         @RequestParam(required = false) Long lastNotificationId,
                                                                         @PageableDefault Pageable pageable) {
        SliceRes<NotificationRes> res = notificationService.getNotifications(loginId, lastNotificationId, pageable);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "알림 읽기", description = "알림에 대응되는 페이지로 이동하기 전에 알림 읽기 API를 호출")
    @ApiResponse(responseCode = "NOTIFICATION4041", description = "알림을 찾을 수 없습니다")
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
