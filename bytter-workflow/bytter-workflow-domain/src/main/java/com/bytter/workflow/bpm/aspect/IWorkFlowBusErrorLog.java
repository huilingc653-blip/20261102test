package com.bytter.workflow.bpm.aspect;

import com.bytter.core.base.entity.ICurrentUser;

import java.util.Map;

/**
 * create by LuoYJ on 2026/3/6.
 */
public interface IWorkFlowBusErrorLog {

    void save(ICurrentUser user,String cateKey, String businessKey, Boolean isPass, String errorMsg);

    Map<String,Object> getById(String id);
}
