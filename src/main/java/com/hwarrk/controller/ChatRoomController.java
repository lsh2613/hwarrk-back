package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.dto.res.ChatRoomCreateRes;
import com.hwarrk.common.dto.res.ChatRoomRes;
import com.hwarrk.entity.ChatRoom;
import com.hwarrk.entity.ChatRoomMember;
import com.hwarrk.entity.Member;
import com.hwarrk.repository.ChatRoomRepository;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "채팅 방")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 생성",
            description = "메시지 조회 시 CHAT_ROOM4041을 반환받으면 이 API를 호출하여 채팅방을 개설")
    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다")
    @PostMapping("/members/{memberId}")
    public CustomApiResponse<ChatRoomCreateRes> createChatRoom(@AuthenticationPrincipal Long loginId,
                                                               @PathVariable Long memberId) {
        chatRoomService.createChatRoom(loginId, memberId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "참여 중인 채팅방 모두 조회")
    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다")
    @GetMapping
    public CustomApiResponse<List<ChatRoomRes>> getChatRooms(@AuthenticationPrincipal Long loginId) {
        return CustomApiResponse.onSuccess(chatRoomService.getChatRooms(loginId));
    }
}
