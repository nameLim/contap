package com.project.contap.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthorityEnum {
    CANT_OTHER_READ(Authority.CANT_OTHER_READ), // 다른 사용자 뒷면 볼 수 없음
    CAN_OTHER_READ(Authority.CAN_OTHER_READ); // 다른 사용자 뒷면 볼 수 있음

    private final int authority;

    public int getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final int CANT_OTHER_READ = 1;
        public static final int CAN_OTHER_READ = 2;
    }

}
