package com.hwarrk.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@RequiredArgsConstructor
@EnableMongoRepositories("com.hwarrk.repository")
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext mongoMappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); //  '_class' 필드를 제거
        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, MappingMongoConverter mappingMongoConverter) {
        return new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
    }
}
/*
@Query("{ 'chatRoomId': ?0, 'unreadCnt': 1, 'createdAt': { $gt: ?1 } }")
@Update("{ '$inc': { 'unreadCnt': -1 } }")  // MongoDB에서 올바른 문법
void decreaseUnreadCount(Long chatRoomId, LocalDateTime lastEntryTime);

@Autowired
private MongoTemplate mongoTemplate;

public void decreaseUnreadCount(Long chatRoomId, LocalDateTime lastEntryTime) {
    Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId)
            .and("unreadCnt").is(1)
            .and("createdAt").gt(lastEntryTime));
    Update update = new Update().inc("unreadCnt", -1);
    mongoTemplate.updateFirst(query, update, ChatMessage.class);
}
 */
