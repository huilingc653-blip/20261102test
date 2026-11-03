package com.bytter.ras.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytter.ras.api.constant.RasConstant;
import com.bytter.system.api.IConfigService;



/**
 * @Title: Service
 * @Description: 营收稽核系统参数操业务处理类
 * @author onlineGenerator
 * @date 2019-04-29 15:43:05
 * @version 1.0
 *
 */
@Service("rasConfigService")
public class RasConfigService {
	@Autowired(required=false)
	private IConfigService configService;

	/**
	 * 营收稽核系统参数配置：historyDay 判断历史天数
	 * @return
	 */
	public int getHisMoveDay(){
		return configService.getConfig(RasConstant.RAS_CONFIG_TYPE, RasConstant.RAS_CONFIG_HISTORY_MOVE_DAY).toInteger(30);
	}

	/**
	 * 营收稽核系统参数配置：revenueRollDay 营收滚动天数 
	 * @return
	 */
	public int getRevenueRollDay(){
		return configService.getConfig(RasConstant.RAS_CONFIG_TYPE, RasConstant.RAS_CONFIG_REVENUE_ROLL_DAY).toInteger(0);
	}
	
	/**
	 * 营收稽核系统参数配置：receivedRollDay 实收滚动天数
	 * @return
	 */
	public int getReceivedRollDay(){
		return configService.getConfig(RasConstant.RAS_CONFIG_TYPE, RasConstant.RAS_CONFIG_RECEIVED_ROLL_DAY).toInteger(0);
	}

	/**
	 * 营收稽核系统参数配置：defaultAuditUser 自动稽核操作人
	 * @return
	 */
	public String getDefaultAuditUser(){
		return configService.getConfig(RasConstant.RAS_CONFIG_TYPE, RasConstant.RAS_CONFIG_DEFAULT_AUDIT_USER).getValue();
	}
	
}