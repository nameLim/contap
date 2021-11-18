package com.project.contap.controller;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatMessageRepository;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.model.card.Card;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.ContapService;
import com.project.contap.service.MainService;
import com.project.contap.common.util.RandomNumberGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class TestController {
    private final UserRepository userRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TapRepository tapRepository;
    @Autowired
    public TestController(
            UserRepository userRepository,
            HashTagRepositoty hashTagRepositoty,
            CardRepository cardRepository,
            PasswordEncoder passwordEncoder,
            FriendRepository friendRepository,
            ChatRoomRepository chatRoomRepository,
            ChatMessageRepository chatMessageRepository,
            TapRepository tapRepository
    ) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendRepository = friendRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.tapRepository = tapRepository;
    }

    @GetMapping("/forclient1/{id}") // 한유저가쓴 카드 모두조회
    List<Card> forclient1(
            @PathVariable Long id
    ) throws ContapException {
        return null;
    }

    @GetMapping("/forclient2/{id}") // 한유저가쓴 카드 모두조회
    User forclient2(
            @PathVariable Long id
    ) throws ContapException {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient3/{id}") // 한유저가쓴 카드 모두조회
    User forclient3(
            @PathVariable String id
    ) throws ContapException {
        User user = userRepository.findByEmail(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient4/{id}") // 한유저가쓴 카드 모두조회
    Card forclient4(
            @PathVariable Long id
    ) throws ContapException {
        Card card = cardRepository.findById(id).orElse(null);
        return card;
    }

    @GetMapping("/HashSet") // 한유저가쓴 카드 모두조회
    void forclient4() throws ContapException {
        HashTag check = hashTagRepositoty.findById(1L).orElse(null);
        if(check != null)
            return;
        List<HashTag> hashs = new ArrayList<>();
        hashs.add(new HashTag("Flutter",0));
        hashs.add(new HashTag("제플린",0));
        hashs.add(new HashTag("프로크리에이트",0));
        hashs.add(new HashTag("파워포인트",0));
        hashs.add(new HashTag("React Native",0));
        hashs.add(new HashTag("React",0));
        hashs.add(new HashTag("Node.js",0));
        hashs.add(new HashTag("Vue.js",0));
        hashs.add(new HashTag("Python",0));
        hashs.add(new HashTag("C++",0));
        hashs.add(new HashTag("Angular",0));
        hashs.add(new HashTag("애프터이펙트",0));
        hashs.add(new HashTag("Go",0));
        hashs.add(new HashTag("C#",0));
        hashs.add(new HashTag("TypeScript",0));
        hashs.add(new HashTag("SQL",0));
        hashs.add(new HashTag("MySQL",0));
        hashs.add(new HashTag("JSP",0));
        hashs.add(new HashTag("Django",0));
        hashs.add(new HashTag("FastAPI",0));
        hashs.add(new HashTag("PostgreSQL",0));
        hashs.add(new HashTag("프리미어",0));
        hashs.add(new HashTag("NestJS",0));
        hashs.add(new HashTag("PMO",0));
        hashs.add(new HashTag("EEO",0));
        hashs.add(new HashTag("FCC",0));
        hashs.add(new HashTag("QFD",0));
        hashs.add(new HashTag("VR",0));
        hashs.add(new HashTag("Zemax",0));
        hashs.add(new HashTag("WAN",0));
        hashs.add(new HashTag("Java",0));
        hashs.add(new HashTag("JavaScript",0));
        hashs.add(new HashTag("피그마",0));
        hashs.add(new HashTag("스케치",0));
        hashs.add(new HashTag("오토캐드",0));
        hashs.add(new HashTag("스케치업",0));
        hashs.add(new HashTag("포토샵",0));
        hashs.add(new HashTag("일러스트레이터",0));
        hashs.add(new HashTag("어도비XD",0));
        hashs.add(new HashTag("인디자인",0));
        hashs.add(new HashTag("밥먹기",1));
        hashs.add(new HashTag("가만히 있기",1));
        hashs.add(new HashTag("뛰기",1));
        hashs.add(new HashTag("지오캐싱",1));
        hashs.add(new HashTag("걷기",1));
        hashs.add(new HashTag("숨쉬기",1));
        hashs.add(new HashTag("종이접기",1));
        hashs.add(new HashTag("피겨 스케이팅",1));
        hashTagRepositoty.saveAll(hashs);
    }

    @GetMapping("/UserSet") // 한유저가쓴 카드 모두조회
    void userSet() throws ContapException {
        User check = userRepository.findById(1L).orElse(null);
        if(check != null)
            return;
        String pw = passwordEncoder.encode("commonpw"); // 패스워드 암호화
        for(int i = 0 ; i < 50 ; i++)
        {
            User user = User.builder()
                    .email(String.format("useremail%d@gmail.com",i))
                    .pw(pw)
                    .userName(String.format("userName%d",i))
                    .field(i%3)
                    .userStatus(UserStatusEnum.ACTIVE).build();
            userRepository.save(user);
        }
    }

    @GetMapping("/UserHashSet")
    void userHashSet() throws ContapException
    {
        for(Long i = 1L ; i <= 50L ; i++)
        {
            User user=userRepository.findById(i).orElse(null);
            HashTag ht1 = hashTagRepositoty.getById(new Long(RandomNumberGeneration.randomRange(1,40)));//
            HashTag ht2 = hashTagRepositoty.getById(new Long(RandomNumberGeneration.randomRange(41,48)));//
            user.getTags().clear();
            user.getTags().add(ht1);
            user.getTags().add(ht2);
            user.setHashTagsString("@"+ht1.getName() + "@_@"+ht2.getName()+"@");
            userRepository.save(user);
        }
    }

    @GetMapping("/testdeleteuser/{userId}")
    void auserHashSet( @PathVariable Long userId ) throws ContapException
    {
        User user = userRepository.findById(userId).orElse(null);
        userRepository.delete(user);
    }

    @GetMapping("/CardSet")
    void CardSet() throws ContapException
    {
        for(Long i = 1L ; i <= 50L ; i++)
        {
            User user=userRepository.findById(i).orElse(null);
            user.getCards().clear();
            for(int j = 0 ; j <3;j++)
            {
                Card card = Card.builder()
                        .user(user)
                        .cardOrder(new Long(j+1))
                        .title(String.format("title%d_%d",i,j))
                        .content(String.format("content%d_%d",i,j))
                        .link(String.format("link%d_%d",i,j))
                        .build();

                user.getCards().add(cardRepository.save(card));
            }
            userRepository.save(user);
        }
    }
    @GetMapping("/friend")
    void friendSet() throws ContapException
    {
        for(Long i = 1L ; i <= 30L ; i = i+2)
        {
            User user1=userRepository.findById(i).orElse(null);
            User user2=userRepository.findById(i+1).orElse(null);
            String roomId = UUID.randomUUID().toString();
            Friend fir = Friend.builder().me(user1).you(user2).roomId(roomId).build();
            Friend sec = Friend.builder().me(user2).you(user1).roomId(roomId).build();
            friendRepository.save(fir);
            friendRepository.save(sec);
            chatRoomRepository.whenMakeFriend(roomId,user1.getEmail(),user2.getEmail());
        }
    }


    @Transactional
    @GetMapping("/friend/{userId}")
    public void perform(@PathVariable Long userId) throws Exception {
        User user2 = userRepository.findById(userId).orElse(null);
        List<User> oldUsers = new ArrayList<>();
        oldUsers.add(user2);
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
            user.setUserStatus(UserStatusEnum.WITHDRAWN);
        }
        userRepository.delete(user2);
    }
}

