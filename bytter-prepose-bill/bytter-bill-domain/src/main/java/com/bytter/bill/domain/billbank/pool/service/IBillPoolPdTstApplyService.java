package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据质押托管转换申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
public interface IBillPoolPdTstApplyService {
	
	/**
	 * <b>功能：</b>获取待发送票据质押托管转换申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstApplyByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据质押托管转换数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstApplyResultByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：修改票据质押托管转换的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolPdTstApply(PoolBillVoucherVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送质押托管转换质押申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstPledgeByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果质押托管转换质押申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstPledgeResultByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：修改票据质押托管转换质押申请的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolPdTstPledge(PoolBillVoucherVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送质押托管转换票据质押解除数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryRemovePledgeByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果质押托管转换票据质押解除数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryRemovePledgeResultByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：修改票据质押托管转换质押解除的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolRemovePledge(PoolBillVoucherVO paramsVo);
	
	/**
	 * <b>功能：</b>获取质押托管转换待确认失败数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2022-11-16 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstPledgeConfirmFailByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>获取质押托管转换待确认等待结果数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2022-11-16 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBillVoucherVO> queryPdTstPledgeConfirmResultByBifCode(String bifCode, String reCode);
	
	
}
