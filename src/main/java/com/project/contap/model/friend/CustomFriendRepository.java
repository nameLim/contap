package com.project.contap.model.friend;

import com.project.contap.model.user.User;

public interface CustomFriendRepository {
    Boolean checkFriend(User me, User you);
}
