package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>出票融资申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-12 <br>
 */
public interface IBillPoolEmitFinaApplyService {
	
	/**
	 * <b>功能：</b>获取待发送融资申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaApplyVO> queryEmitFinaByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：</b>获取等待结果融资申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaApplyVO> queryEmitFinaResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改融资申请的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updatePoolEmitFinaApply(PoolEmitFinaApplyVO paramsVo);
	/**
	 * <b>功能：保存出票融资的合同</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param conVo 合同要素
	 * @return
	 */
	void savePoolEmitFinaCon(PoolEmitFinaConVO conVo);
	
	/**
	 * <b>功能：</b>获取待发送重新生成出票融资合同数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaConVO> queryResetPoolEmitFinaConByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：</b>获取重新生成出票融资合同等待结果数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaConVO> queryResetPoolEmitFinaConResultByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：修改重新生成出票融资合同数据指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updateResetPoolEmitFinaCon(PoolEmitFinaConVO paramsVo);
	
	/**
	 * <b>功能：</b>获取待发送签收确认出票融资合同数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaConVO> querySignOkPoolEmitFinaConByBifCode(String bifCode,String reCode);
	/**
	 * <b>功能：</b>获取签收确认出票融资合同等待结果数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<PoolEmitFinaConVO> querySignOkPoolEmitFinaConResultByBifCode(String bifCode,String reCode);
	
	/**
	 * <b>功能：修改签收确认出票融资合同数据指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	void updateSignOkPoolEmitFinaCon(PoolEmitFinaConVO paramsVo);
	
}
