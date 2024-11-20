package com.hwarrk.repository;

import com.hwarrk.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {
    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    int countByChatRoomIdAndCreatedAtAfter(Long chatRoomId, LocalDateTime lastEntryTime);

    boolean existsByChatRoomIdAndCreatedAtAfter(Long chatRoomId, LocalDateTime lastEntryTime);
}
