package com.project.contap.common.util;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessUserStatusJob {
    private final UserRepository userRepository;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *")
    public void perform() throws Exception {
        List<User> oldUsers = userRepository.findByModifiedDtBeforeAndAndUserStatusEquals(LocalDateTime.now().minusMonths(1), UserStatusEnum.INACTIVE);
        for(User user: oldUsers){
            //회원 정보 삭제 해야할 곳
            user.setUserStatus(UserStatusEnum.WITHDRAWN);
        }


        userRepository.saveAll(oldUsers);
        User.userCount -= oldUsers.size();
    }

}
