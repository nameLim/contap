package com.project.contap.common.util;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatMessageRepository;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessUserStatusJob {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final FriendRepository friendRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TapRepository tapRepository;

    // 초, 분, 시, 일, 월, 주 순서
    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void perform() throws Exception {
        List<User> oldUsers = userRepository.findByModifiedDtBeforeAndAndUserStatusEquals(LocalDateTime.now().minusMonths(1), UserStatusEnum.INACTIVE);
        for(User user: oldUsers){
            List<Card> cards =  cardRepository.findAllByUser(user);
            cardRepository.deleteAll(cards);
            List<Friend> friends = friendRepository.getallmyFriend(user);
            List<String> roomIds = new ArrayList<>();
            for(Friend friend : friends)
            {
                if(roomIds.contains(friend.getRoomId())){
                    roomIds.remove(friend.getRoomId());
                }
                else{
                    roomIds.add(friend.getRoomId());
                    List<ChatMessage> msg = chatMessageRepository.findAllByRoomId(friend.getRoomId());
                    chatMessageRepository.deleteAll(msg);
                    chatRoomRepository.deleteRoomInfo(friend.getYou().getEmail(),friend.getMe().getEmail(),friend.getRoomId());
                }
            }
            friendRepository.deleteAll(friends);
            List<Tap> taps = tapRepository.getMyTaps(user);
            tapRepository.deleteAll(taps);
            chatRoomRepository.deleteUser(user.getEmail());
        }
        userRepository.saveAll(oldUsers);
    }

}
