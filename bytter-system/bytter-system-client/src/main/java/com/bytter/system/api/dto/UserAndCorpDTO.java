package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAndCorpDTO implements Serializable {

    //这个 id是 userId
    private String id;
    private String username;
    private String corpId;
    private String corpName;

}
