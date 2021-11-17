package com.project.contap.chat;

import java.util.List;

public interface CostomChatMessageRepository {
    List<ChatMessage> findMessage(String roomId,Long startId);
}
