package com.common.base.model;

import java.io.Serializable;

public class AuthUrlVo implements Serializable {

    public String authUrl;
    public int mode;
    public boolean checked;
    public String name;
    public int version;

    public AuthUrlVo(String authUrl, int mode,int version) {
        this.authUrl = authUrl;
        this.mode = mode;
        this.version = version;
        if (mode == DevelopModeEnum.AuthUrlMode.开发.mode){
            this.name = "开发";
        }else {
            this.name = DevelopModeEnum.AuthUrlMode.values()[mode].name();
        }
    }

    @Override
    public String toString() {
        return "AuthUrlVo{" +
                "authUrl='" + authUrl + '\'' +
                ", mode=" + mode +
                ", checked=" + checked +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
