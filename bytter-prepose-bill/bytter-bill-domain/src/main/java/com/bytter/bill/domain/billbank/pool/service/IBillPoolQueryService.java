package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.domain.billbank.pool.vo.*;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据池查询服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
public interface IBillPoolQueryService {
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息
	 */
	List<PoolBaseVO> queryPoolByBifCode(String bifCode);
	
	/**
	 * <b>功能：</b>获取指定银行类型的出入池登记薄查询待返回票据池和账号<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-03-16 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息及发起人账号信息
	 */
	List<PoolBaseVO> queryPoolAndReqAccByBifCode(String bifCode);
	
	/**
	 * <b>功能：</b>获取对于银行类型需要的票据账户<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息
	 */
	List<VAccListVo> queryBillAccByBifCode(String bifCode);
	/**
	 * <b>功能：</b>获取对应银行类型需要的票据池额度查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolLimitExcByBifCode(String bifCode);
	/**
	 * <b>功能：对获取的票据池和额度进行入库（无则新增有则修改）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolLimitInfo(List<PoolLimitInfoVO> paramsVo);
	/**
	 * <b>功能：对获取的票据池额度明细进行入库）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2021-03-04 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolLimitDtlInfo(List<PoolLimitVO> paramsVo);
	/**
	 * <b>功能：</b>获取对应银行类型需要的票据池额度交易明细查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolLimitDtlExcByBifCode(String bifCode);
	/**
	 * <b>功能：</b>获取对应银行类型需要的票据池出入池查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolInOutExcByBifCode(String bifCode);
	
	/**
	 * <b>功能：对获取的出入池账簿进行入库（先根据交易日期和出入池方式进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolInOutInfo(List<PoolBillVoucherVO> paramsVo);
	/**
	 * <b>功能：对获取的质押托管转换进行入库（先根据交易日期进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolPdtstInfo(List<PoolBillVoucherVO> paramsVo);
	/**
	 * <b>功能：对获取的票据池出票明细进行入库（先根据交易日期进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolEmitInfo(List<PoolEmitFinaApplyVO> paramsVo);
	
	/**
	 * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolExcByBifCode(String bifCode);
	
	/**
	 * <b>功能：</b>获取对应银行类型和查询类型需要的票据池查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @param queryType 查询类型代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolExcByBifCode(String bifCode,String queryType);
	/**
	 * <b>功能：</b>获取对应银行类型需要的票据池保证金明细查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolBailExcByBifCode(String bifCode);
	
	/**
	 * <b>功能：对获取的保证金交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolBailInfo(List<PoolBailVO> paramsVo);
	/**
	 * <b>功能：</b>获取对应银行类型需要的票据池在池查询指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池查询指令信息
	 */
	List<PoolQueryExcVO> queryPoolHoldExcByBifCode(String bifCode);
	/**
	 * <b>功能：对获取的在池交易明细进行入库（先根据申请日期进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolHoldInfo(List<PoolBillVoucherVO> paramsVo);
	
	/**
	 * <b>功能：对获取的提示付款账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-19 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolIsPayInfo(List<PoolBillVoucherVO> paramsVo);
	/**
	 * <b>功能：对获取的出票融资申请账簿进行入库（先根据交易日期和电纸票标识进行删除然后再新增）</b><br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2020-04-14 <br>
	 * @param paramsVo 数据的信息
	 * @return
	 */
	void savePoolFinaApplyInfo(List<PoolBillVoucherVO> paramsVo);
	/**
	 * <b>功能：</b>根据查询业务ID修改票据池查询指令状态<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-11-12 <br>
	 * @param busId 指令id
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回信息
	 * @return 修改成功还是失败 true成功false失败
	 */
	boolean updatePoolQueryExc(String busId,String reCode,String reMsg);
	
	/**
	 * <b>功能：</b>保存保证金账户信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-8-4 <br>
	 * @param paramsVo 数据的信息
	 * @return 修改成功还是失败 true成功false失败
	 */
	void saveAbPoolBailInfo(List<PoolBailVO> paramsVo);
}
