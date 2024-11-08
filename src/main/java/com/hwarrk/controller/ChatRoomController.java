package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.res.ChatRoomCreateRes;
import com.hwarrk.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 생성",
            description = "메시지 조회 시 CHAT_ROOM4041을 반환받으면 이 API를 호출하여 채팅방을 개설",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4001", description = "사용자를 찾을 수 없습니다"),
            })
    @PostMapping("/members/{memberId}")
    public CustomApiResponse<ChatRoomCreateRes> createChatRoom(@AuthenticationPrincipal Long loginId,
                                                               @PathVariable Long memberId) {
        chatRoomService.createChatRoom(loginId, memberId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "참여 중인 채팅방 모두 조회",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4001", description = "사용자를 찾을 수 없습니다"),
            })
    @GetMapping
    public CustomApiResponse getChatRooms(@AuthenticationPrincipal Long loginId) {
        return CustomApiResponse.onSuccess(chatRoomService.getChatRooms(loginId));
    }

    @Operation(summary = "채팅방 접속",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4001", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "CHAT_ROOM4041", description = "채팅방을 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "CHAT_ROOM_MEMBER4041", description = "채팅방 참가자를 찾을 수 없습니다")
            })
    @PostMapping("/{chatRoomId}/entry")
    public CustomApiResponse enterChatRoom(@AuthenticationPrincipal Long loginId,
                                           @PathVariable Long chatRoomId) {
        chatRoomService.enterChatRoom(loginId, chatRoomId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "채팅방 나가기",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4001", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "CHAT_ROOM4041", description = "채팅방을 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "CHAT_ROOM_MEMBER4041", description = "채팅방 참가자를 찾을 수 없습니다")
            })
    @PostMapping("/{chatRoomId}/exit")
    public CustomApiResponse exitChatRoom(@AuthenticationPrincipal Long loginId,
                                          @PathVariable Long chatRoomId) {
        chatRoomService.exitChatRoom(loginId, chatRoomId);
        return CustomApiResponse.onSuccess();
    }
}
