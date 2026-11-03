package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>额度申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-17 <br>
 */
public interface IBillPoolLimitApplyService {
	
	/**
	 * <b>功能：</b>获取待发送额度申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolLimitVO> queryLimitApplyByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolLimitVO> queryLimitApplyResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改额度的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolLimitApply(PoolLimitVO paramsVo);
	/**
	 * <b>功能：</b>获取需要查询复核数据的票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息
	 */
	List<PoolBaseVO> queryCheckPoolByBifCode(String bifCode);
	/**
	 * <b>功能：对获取的待复核数据进行入库（业务中数据不替换，有失败数据和已完成数据进行替换）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void savePoolLimitApplyCheck(List<PoolLimitVO> paramsVo);
	/**
	 * <b>功能：</b>获取待发送额度申请复核数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolLimitVO> queryLimitApplyCheckByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果额度申请复核数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolLimitVO> queryLimitApplyCheckResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改额度申请复核的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolLimitApplyCheck(PoolLimitVO paramsVo);
}
