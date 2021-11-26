package com.project.contap.controller;

import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.common.util.RandomNumberGeneration;
import com.project.contap.exception.ContapException;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.FrontRequestCardDto;
import com.project.contap.service.ContapService;
import com.project.contap.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {
    @Value("${logging.module.version}")
    private String version;

    private final UserRepository userRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TapRepository tapRepository;
    private final MainService mainService;
    private final ContapService contapService;
    private final ImageService imageService;
    @Autowired
    public TestController(
            UserRepository userRepository,
            HashTagRepositoty hashTagRepositoty,
            CardRepository cardRepository,
            PasswordEncoder passwordEncoder,
            FriendRepository friendRepository,
            ChatRoomRepository chatRoomRepository,
            ChatMessageRepository chatMessageRepository,
            TapRepository tapRepository,
            MainService mainService,
            ContapService contapService,
            ImageService imageService
    ) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.friendRepository = friendRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.tapRepository = tapRepository;
        this.mainService = mainService;
        this.contapService = contapService;
        this.imageService = imageService;
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
        hashs.add(new HashTag("Spring",0));
        hashs.add(new HashTag("Spring Boot",0));
        hashs.add(new HashTag("GO",0));
        hashs.add(new HashTag("React",0));
        hashs.add(new HashTag("React Native",0));
        hashs.add(new HashTag("Flutter",0));
        hashs.add(new HashTag("Node.js",0));
        hashs.add(new HashTag("Python",0));
        hashs.add(new HashTag("C++",0));
        hashs.add(new HashTag("C",0));
        hashs.add(new HashTag("C#",0));
        hashs.add(new HashTag("Angular",0));
        hashs.add(new HashTag("Vue.js",0));
        hashs.add(new HashTag("Express",0));
        hashs.add(new HashTag("Django",0));
        hashs.add(new HashTag("Next.js",0));
        hashs.add(new HashTag("SQL",0));
        hashs.add(new HashTag("Nest.js",0));
        hashs.add(new HashTag("Java",0));
        hashs.add(new HashTag("HTML CSS",0));
        hashs.add(new HashTag("TypeScript",0));
        hashs.add(new HashTag("Android Studio",0));
        hashs.add(new HashTag("Ruby",0));
        hashs.add(new HashTag("JavaScript",0));
        hashs.add(new HashTag("Swift",0));
        hashs.add(new HashTag("Assembly",0));
        hashs.add(new HashTag("PHP",0));
        hashs.add(new HashTag("Nuxt.js",0));
        hashs.add(new HashTag("Flask",0));
        hashs.add(new HashTag("JQuery",0));
        hashs.add(new HashTag("Figma",0));
        hashs.add(new HashTag("After Effects",0));
        hashs.add(new HashTag("Illustrator",0));
        hashs.add(new HashTag("Sketch",0));
        hashs.add(new HashTag("Adobe XD",0));
        hashs.add(new HashTag("Photoshop",0));
        hashs.add(new HashTag("Proto.io",0));
        hashs.add(new HashTag("AutoCAD",0));
        hashs.add(new HashTag("Premiere Pro",0));
        hashs.add(new HashTag("Zeplin",0));
        hashs.add(new HashTag("영화감상",1));
        hashs.add(new HashTag("독서",1));
        hashs.add(new HashTag("헬스",1));
        hashs.add(new HashTag("인테리어",1));
        hashs.add(new HashTag("여행",1));
        hashs.add(new HashTag("스포츠",1));
        hashs.add(new HashTag("요리",1));
        hashs.add(new HashTag("카페투어",1));
        hashs.add(new HashTag("맛집탐방",1));
        hashs.add(new HashTag("공예",1));
        hashs.add(new HashTag("드로잉",1));
        hashs.add(new HashTag("게임",1));
        hashs.add(new HashTag("피규어",1));
        hashs.add(new HashTag("자동차",1));
        hashs.add(new HashTag("산책",1));
        hashs.add(new HashTag("뷰티",1));
        hashs.add(new HashTag("디자인",1));
        hashs.add(new HashTag("아이돌",1));
        hashs.add(new HashTag("테크",1));
        hashs.add(new HashTag("반려동물",1));
        hashs.add(new HashTag("스포츠관람",1));
        hashs.add(new HashTag("쇼핑",1));
        hashs.add(new HashTag("사진찍기",1));
        hashs.add(new HashTag("춤",1));
        hashs.add(new HashTag("악기연주",1));
        hashs.add(new HashTag("코딩",1));
        hashs.add(new HashTag("전시회",1));
        hashs.add(new HashTag("보컬",1));
        hashs.add(new HashTag("뮤지컬",1));
        hashs.add(new HashTag("글쓰기",1));
        hashs.add(new HashTag("등산",1));
        hashs.add(new HashTag("레저",1));
        hashs.add(new HashTag("음악감상",1));
        hashs.add(new HashTag("콘서트",1));
        hashs.add(new HashTag("패션",1));
        hashs.add(new HashTag("재태크",1));
        hashs.add(new HashTag("파이낸스",1));
        hashs.add(new HashTag("애니메이션",1));
        hashs.add(new HashTag("웹툰",1));
        hashs.add(new HashTag("SNS",1));

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
            Friend fir = Friend.builder().me(user1).you(user2).roomId(roomId).newFriend(1).build();
            Friend sec = Friend.builder().me(user2).you(user1).roomId(roomId).newFriend(1).build();
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
                    chatRoomRepository.whendeleteFriend(friend.getRoomId(),friend.getYou().getEmail(),friend.getMe().getEmail());
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

    @GetMapping("/dotappp/{userId}")
    public void performmm(@PathVariable Long userId) throws Exception
    {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return;
        String pw = passwordEncoder.encode("commonpw"); // 패스워드 암호화
        for(int i = 0 ; i < 100 ; i++)
        {

            User user1 = userRepository.findByEmail(String.format("useremail%d@gmail.com",i)).orElse(null);
            if(user1 == null) {
                user1 = User.builder()
                        .email(String.format("useremail%d@gmail.com", i))
                        .pw(pw)
                        .userName(String.format("userName%d", i))
                        .field(i % 3)
                        .userStatus(UserStatusEnum.ACTIVE).build();
                user1 = userRepository.save(user1);
            }
            mainService.dotap(user1,user.getId(),"우석님을위한API");
        }
    }

    @GetMapping("/mytapok/{userId}")
    public void performnmggm(@PathVariable Long userId) throws Exception
    {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null)
            return;
        List<Tap> taps= tapRepository.findAllByReceiveUser(user);
        for(Tap tap : taps)
        {
            contapService.rapAccept(tap.getId(),user.getEmail());
        }

    }

    @GetMapping("/autoPushTest")
    public String autoPushTest() throws Exception
    {
        return String.format("Project Version : %s", version);

    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy자동배포 확인222222";
    }

    @PostMapping("/autoPushImageTest")
    public void autoPushImageTest(@ModelAttribute FrontRequestCardDto frontRequestCardDto) throws Exception
    {
        System.out.println("Hi");
        imageService.upload(frontRequestCardDto.getProfile(),"static", "");
    }

}

