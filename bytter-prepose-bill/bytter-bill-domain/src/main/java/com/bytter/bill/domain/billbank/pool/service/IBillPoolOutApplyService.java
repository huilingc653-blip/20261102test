package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据出池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
public interface IBillPoolOutApplyService {
	
	/**
	 * <b>功能：</b>获取待发送票据出池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryOutApplyByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据出池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryOutApplyResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改票据出池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolOutApply(PoolBillVoucherVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送票据质押解除出池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryRemovePledgeByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据质押解除出池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryRemovePledgeResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改票据质押解除出池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolRemovePledge(PoolBillVoucherVO paramsVo);
	
}
