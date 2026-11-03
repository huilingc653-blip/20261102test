package com.bytter.system.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CorpUserResourceDTO {

    private String userId;

    private String loginName;

    private String username;

    private String corpId;

    private String corpCode;

    private String corpName;

    private String resourceId;

    private Date assignTime;


}
