package com.bytter.bill.domain.billbank.service;
import java.util.List;
import com.bytter.bill.domain.billbank.vo.*;
/**
 * <br>
 * <b>功能：</b>票据查询服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2018-01-05 <br>
 */
public interface IBillQueryService {
	
	
	/**
	 * <b>功能：</b>根据银行代码查询已完成，不是作废的，最大流水id的数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-01-05 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	List<VoucherInfoVo> queryBillByBifCode(String bifCode);
	/**
	 * <b>功能：</b>查询银行账号持有票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	List<VAccListVo> queryVAccListVoHoldBill(String bifCode);
	/**
	 * <b>功能：</b>查询银行账号持有票据(票据池)<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-29 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	List<VAccListVo> queryVAccListVoHoldBillPool(String bifCode);
	
	/**
	 * <b>功能：</b>查询待签收业务类型<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2019-6-4 <br>
	 * @param bifCode 银行接口代码
	 * @return 账户和对应查询业务
	 */
	public List<VAccListVo> queryVAccListVoSignBill(String bifCode);
}
