package com.project.contap.dto;

import lombok.Data;

@Data
public class DefaultRsp {
    private String msg = "";
    public DefaultRsp (String msg)
    {
        this.msg = msg;
    }
}
