package com.hwarrk.repository;

import com.hwarrk.entity.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public int decreaseUnreadCount(Long chatRoomId, LocalDateTime lastEntryTime) {
        Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
                .and("createdAt").gt(lastEntryTime)
                .and("unreadCnt").is(1));

        Update update = new Update().inc("unreadCnt", -1);
        UpdateResult result = mongoTemplate.updateMulti(query, update, ChatMessage.class);

        return (int) result.getModifiedCount();
    }
}
