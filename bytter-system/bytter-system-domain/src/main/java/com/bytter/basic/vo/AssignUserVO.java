package com.bytter.basic.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据用户分配帐号数据
 */
@Data
public class AssignUserVO implements Serializable {


    private String accountId;
    private String accountType;
    private String accountCorpId;
    private String userIds;

    private String accountCorp;
    private String innerAccountCorp;
    private String userId;

}
