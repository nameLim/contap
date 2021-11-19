package com.project.contap.common.util;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessUserStatusJob {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 초, 분, 시, 일, 월, 주 순서
//    @Scheduled(cron = "0 0 4 * * *")
    @Scheduled(cron = "10 * * * * *")
    @Transactional
    public void perform() throws Exception {
        List<User> oldUsers = userRepository.findByModifiedDtBeforeAndAndUserStatusEquals(LocalDateTime.now().minusSeconds(10), UserStatusEnum.INACTIVE);

        for(User user: oldUsers){
            //회원 정보 삭제 해야할 곳
            cardRepository.deleteAll(user.getCards());
//            friendRepository.deleteAllByMe_Id(user.getId());
//            friendRepository.deleteAllByYou_Id(user.getId());
            friendRepository.deleteAllByMe_IdOrYou_Id(user.getId());
            userRepository.delete(user);



            //ChatMessage 도 삭제해야함.
//            private String roomId;
//            private String message;
//            private String writer;
//            private String reciever;
//            private int type = 0;  // 0 : 둘다 // 1 : 1명만이고 나머지는 로그인 상태 //2 : 한명만이고 나머지는 로그아웃 상태
//            private String sessionId;
//            private String writerSessionId;
            ChatMessage chatMessage = new ChatMessage();
//            chatRoomRepository.delete(chat);
        }
    }

}
