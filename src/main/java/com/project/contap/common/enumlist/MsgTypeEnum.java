package com.project.contap.common.enumlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MsgTypeEnum {
    CHAT_BOTH(0,""),
    CHAT_EITHER_LOGINON(1,""),
    CHAT_EITHER_LOGOFF(2,""),
    SEND_TAP(3,"탭요청을보냈어요"),
    REJECT_TAP(4,"탭요청을 거절했네요!"),
    ACCEPT_TAP(5,"탭요청을 수락했네요!");

    private final int value;
    private final String msg;
}
