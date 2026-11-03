package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据入池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
public interface IBillPoolIntoApplyService {
	
	/**
	 * <b>功能：</b>获取待发送票据入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoApplyByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoApplyResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改票据入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolIntoApply(PoolBillVoucherVO paramsVo);
	
//
//	/**
//	 * <b>功能：</b>获取待发送再次票据入池申请数据<br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param bifCode 银行接口代码
//	 * @param reCode 银行返回代码
//	 * @return
//	 */
//	List<PoolBillVoucherVO> queryAgainIntoApplyByBifCode(String bifCode,String reCode);
//	
//	/**
//	 * <b>功能：</b>获取等待结果再次票据入池数据<br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param bifCode 银行接口代码
//	 * @param reCode 银行返回代码
//	 * @return
//	 */
//	List<PoolBillVoucherVO> queryAgainIntoApplyResultByBifCode(String bifCode,String reCode);
//
//	/**
//	 * <b>功能：修改再次票据入池的指令状态</b><br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param paramsVo 修改数据的参数
//	 * @return
//	 */
//	void updatePoolAgainIntoApply(PoolBillVoucherVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送票据托管入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoManagedByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据托管入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoManagedResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改票据托管入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolIntoManaged(PoolBillVoucherVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送票据质押入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoPledgeByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据质押入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryIntoPledgeResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改票据质押入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolIntoPledge(PoolBillVoucherVO paramsVo);
	
	/**
	 * <b>功能：</b>获取待发送删除票据数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryDeleteBillByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果删除票据数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryDeleteBillResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改删除票据的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updateDeleteBill(PoolBillVoucherVO paramsVo);
}
