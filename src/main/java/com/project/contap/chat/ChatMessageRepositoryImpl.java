package com.project.contap.chat;

import com.project.contap.model.card.dto.QCardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class ChatMessageRepositoryImpl implements CostomChatMessageRepository{
    private final JPAQueryFactory queryFactory;

    public ChatMessageRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public List<ChatMessage> findMessage(String roomId, Long startId)
    {
        QChatMessage qChatMessage = QChatMessage.chatMessage;
        if(startId <= 0)
            return  queryFactory
                    .select(qChatMessage)
                    .from(qChatMessage)
                    .orderBy(qChatMessage.id.desc())
                    .offset(0).limit(15)
                    .fetch();
        else
            return  queryFactory
                    .select(qChatMessage)
                    .from(qChatMessage)
                    .where(qChatMessage.id.lt(startId))
                    .orderBy(qChatMessage.id.desc())
                    .offset(0).limit(15)
                    .fetch();
    }
}
