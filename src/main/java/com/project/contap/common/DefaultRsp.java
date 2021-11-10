package com.project.contap.common;

import lombok.Data;

@Data
public class DefaultRsp {
    private String msg = "";
    public DefaultRsp (String msg)
    {
        this.msg = msg;
    }
}
