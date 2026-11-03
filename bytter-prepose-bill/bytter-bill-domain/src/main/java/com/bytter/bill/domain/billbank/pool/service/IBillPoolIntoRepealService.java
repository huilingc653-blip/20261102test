package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据入池撤销服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2021-11-25 <br>
 */
public interface IBillPoolIntoRepealService {
	/**
	 * <b>功能：对获取的可入池撤消数据进行入库（先根据票据号、交易日期判断是否存在数据再新增）</b><br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 数据的信息
	 */
	void savePoolIntoRepealInfo(List<PoolBillVoucherVO> paramsVo);
	
	/**
	 * <b>功能：</b>获取待发送入池撤销申请数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 获取待发送入池撤销申请数据
	 */
	List<PoolBillVoucherVO> queryIntoRepealByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果票据入池撤销数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 等待结果票据入池撤销数据
	 */
	List<PoolBillVoucherVO> queryIntoRepealResultByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：修改票据入池撤销的指令状态</b><br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolIntoRepeal(PoolBillVoucherVO paramsVo);
}
