package com.hwarrk.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisChatUtil {
    private final RedisTemplate<Long, Long> memberChatRoomMap;
    private final RedisTemplate<String, Integer> activeCntInChatRoom;

    static final String prefix = "chat-room-";

    public void setMemberInChatRoom(Long memberId, Long chatRoomId) {
        ValueOperations<Long, Long> valueOperations = memberChatRoomMap.opsForValue();
        valueOperations.set(memberId, chatRoomId);
    }

    public Long getChatRoomId(Long memberId) {
        ValueOperations<Long, Long> valueOperations = memberChatRoomMap.opsForValue();
        return valueOperations.get(memberId);
    }

    public void deleteMemberInChatRoom(Long memberId) {
        memberChatRoomMap.delete(memberId);
    }

    public void incrementActiveCnt(Long key) {
        ValueOperations<String, Integer> valueOperations = activeCntInChatRoom.opsForValue();
        valueOperations.increment(prefix + key);
    }

    public void decrementActiveCnt(Long key) {
        ValueOperations<String, Integer> valueOperations = activeCntInChatRoom.opsForValue();
        if (valueOperations.decrement(prefix + key) <= 0)
            activeCntInChatRoom.delete(prefix + key);
    }

    public Integer getActiveCntInChatRoom(Long key) {
        ValueOperations<String, Integer> valueOperations = activeCntInChatRoom.opsForValue();
        return valueOperations.get(prefix + key);
    }
}
