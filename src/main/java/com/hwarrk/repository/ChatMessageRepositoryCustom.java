package com.hwarrk.repository;

import java.time.LocalDateTime;

public interface ChatMessageRepositoryCustom {
    int decreaseUnreadCount(Long chatRoomId, LocalDateTime lastEntryTime);
}
