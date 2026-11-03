package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
/**
 * 票据业务底层接口
 * @author zhengg
 *
 */
public interface IAccebillBaseService {
	
	/**
	 * 通过流水ID获取返回信息
	 * @param aid
	 * @return
	 */
	AbVoucherinfoVO getAbBillInfoById(String aid);
}
