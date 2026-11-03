package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>保证金申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-17 <br>
 */
public interface IBillPoolBailService {
	
	/**
	 * <b>功能：</b>获取待发送保证金存入申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBailVO> queryBailInByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果保证金存入数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBailVO> queryBailInResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改保证金存入的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolBailInApply(PoolBailVO paramsVo);
	/**
	 * <b>功能：</b>获取待发送保证金提取申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBailVO> queryBailOutByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果保证金提取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolBailVO> queryBailOutResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改保证金提取的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolBailOut(PoolBailVO paramsVo);
}
