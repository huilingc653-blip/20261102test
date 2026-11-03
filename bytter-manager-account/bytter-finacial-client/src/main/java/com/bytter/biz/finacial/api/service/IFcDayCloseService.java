package com.bytter.biz.finacial.api.service;
import java.util.Calendar;
import java.util.Date;
/**
 * 网点日结接口
 * @author liyh
 * @since 2017-8-16
 */
public interface IFcDayCloseService {
	/**
	 * 功能介绍 : 初始化网点日结日期。
	 * @param netId 网点I
	 * @param closeDate 日结日期
	 * @param userId 用户
	 */
	public void initNetDayClose(String netId, Date closeDate, String userId);
	/**
	 * 功能介绍 : 初始化网点日结日期。
	 * @param netId 网点I
	 * @param userId 用户
	 */
	public void initNetDayClose(String netId, String userId);
	
	/**
	 * 功能介绍 : 获取网点的已日结日期。
	 * @param netId 网点Id
	 * @return 日结日期   
	 */
	public Date getDayCloseFinalDate(String netId);
	/**
	 * 功能：初始化资金池主体日结日期 
	 * @param netId 网点Id
	 * @param userId 用户
	 */
	public void initPoolNetCloseDate(String netId, String userId);
	/**
	 * 获取资金池主体的日期日期 
	 * @param netId 网点Id
	 */
	public Date getPoolDayCloseDate(String netId);
	/**
	 * 获取网点下一个月结日期
	 * @param netId
	 * @return
	 */
	Calendar getMonthCloseDate(String netId);
	
}
