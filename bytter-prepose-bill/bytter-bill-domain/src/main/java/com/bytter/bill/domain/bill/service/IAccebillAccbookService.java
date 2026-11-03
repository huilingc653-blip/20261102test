package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.entity.ICurrentUser;
/**
 * 票据业务底层接口
 * @author zhengg
 *
 */
public interface IAccebillAccbookService extends IAccebillBaseService {
	/**
	 * 票据业务完成操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	public void busOverOp(AbAccrecords info, String logType, String appMsg, ICurrentUser userVo);
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param appMsg 日志信息
	 * @param userVo 操作用户
	 */
	public void busRepulseOp(AbAccrecords info,String logType, String appMsg, ICurrentUser userVo);
	
}
