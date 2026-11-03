package com.bytter.ras.utils;

import java.util.List;
import java.util.Map;

import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.service.RasAuditTaskService;
import com.bytter.core.base.entity.ICurrentUser;

public class RasThreadUtil implements Runnable{
	
	private RasAuditTaskService auditTaskService;
	
	private Map<String,Object> params;
	
	private List<RasAuditStrategy> auditStrategyList;
	
	private ICurrentUser user;
	
	public RasThreadUtil(){
		
	}

	public RasThreadUtil(RasAuditTaskService auditTaskService,List<RasAuditStrategy> auditStrategyList, Map<String,Object> params,ICurrentUser user){
		this.auditTaskService=auditTaskService;
		this.auditStrategyList=auditStrategyList;
		this.params=params;
		this.user=user;
	}
	
	@Override
	public void run() {
		auditTaskService.executeStrategyByPayTypeAndStrategy(auditStrategyList, params, user);
	}
}
