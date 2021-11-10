package com.project.contap.common.enumlist;

public enum MsgTypeEnum {
    CHAT_BOTH(0),
    CHAT_EITHER_LOGINON(1),
    CHAT_EITHER_LOGOFF(2),
    SEND_TAP(3),
    REJECT_TAP(4),
    ACCEPT_TAP(5);

    private final int value;
    MsgTypeEnum(int value) {
        this.value = value;
    }
    public  int getValue() {
        return  value;
    }
}
