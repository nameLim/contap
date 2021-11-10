package com.project.contap.model.tap;

import com.project.contap.model.user.User;

public interface CustomTapRepository {
    Tap checkReceiveTap(User receiver, User sender);
    Boolean checkSendTap(User receiver, User sender);
}
