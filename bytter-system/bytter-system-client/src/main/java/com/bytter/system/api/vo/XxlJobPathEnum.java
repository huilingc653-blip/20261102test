package com.bytter.system.api.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * XXL管理后台API地址。
 *
 * @author zhaohc
 */
@AllArgsConstructor
@Getter
public enum XxlJobPathEnum {
    LOGIN("/login", "登录"),
    ADD("/jobinfo/add", "添加Job"),
    UPDATE("/jobinfo/update", "更新Job"),
    REMOVE("/jobinfo/remove", "删除Job"),
    PAGE_JOB("/jobinfo/pageList", "查询Job"),
    PAGE_GROUP("/jobgroup/pageList", "查询Job组"),
	GET_JOB("/jobinfo/loadById", "恩据ID查询Job");
	
    private final String path;
    private final String desc;
  
}