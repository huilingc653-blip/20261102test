package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.entity.EDraftExcBill;
import com.bytter.bill.domain.billbank.vo.BillAbBackbillinfoVo;
import com.bytter.bill.domain.billbank.vo.EDraftExcBillVo;
import com.bytter.bill.domain.billbank.vo.PaymentCreditVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>指令查询表（支付信用指令，背面信息指令)服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2018-04-19 <br>
 */
public interface IEDraftExcBillService {
	EDraftExcBill saveEDraftExcBill(EDraftExcBill excBill);
    /**
     *
     * <b>方法描述：</b>支付信用 ，查询待发送指令业务接口：根据消息类型（支付信用类型01）查询状态等于16的指令数据。<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param bifCode 银行接口代码
     */
	 List<EDraftExcBillVo> queryEDraftExcBillPaymentCredit(String bifCode);

	/**  *
	 * 通过id查询指令
	 * @param id
	 * @return
	 */
	EDraftExcBill queryEDraftExcBillById(String id);
    /**
     * <b>功能：</b>支付信用--成功修改指令状态为19，删除原本支付信用数据，新增支付信用数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param paymentCredit 支付信用实体
     */
	void updateEDraftExcSuccessStatusAndAddPaymentCredit(PaymentCreditVo paymentCredit);
    /**
     *
     * <b>方法描述：</b>背面信息 ，查询待发送指令业务接口：根据消息类型（背面信息类型02）查询状态等于16的指令数据。<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-20 <br>
     * @param bifCode 银行接口代码
     */
	 List<EDraftExcBillVo> queryEDraftExcBillBackInfo(String bifCode);
	 
	 
	 /**
	  * <b>功能：</b>背面信息--成功修改指令状态为19，删除原本数据，新增背面信息数据<br>
	  * <b>作者：</b>郑鸿杰<br>
	  * <b>日期：</b> 2018-04-20 <br>
	  * @param BillAbBackbillinfoList 背面集合信息 
	 */
	void updateEDraftExcSuccessStatusAndAddBackInfo(List<BillAbBackbillinfoVo> BillAbBackbillinfoList);
	
    /**
     * <b>功能：</b>失败修改指令状态为-1<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param origNo 票据号
     * @param acctId  持票账号
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
	void updateEDraftExcFailStatusByOrigNoAndAcctId(String origNo, String acctId, String returnCode, String returnMsg,String sbbllRngStrtSn, String sbbllRngEndSn);
	
	
	
	/**
	 * <b>功能：</b>根据指令流水ID修改返利代码和返回原因<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-19 <br>
	 * @param id 指令表ID
	 * @param returnCode 返回代码
	 * @param returnMsg  返回消息
	 */
	void updateEDraftExcById(String id, String returnCode, String returnMsg);
	
	/**
	 * <b>功能：</b>根据指令流水ID修改状态为指令重新发送<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-19 <br>
	 * @param id 指令表ID
	 */
	void updateEDraftExcRepeatById(String id);
	 /**
	 * 
	 * <b>方法描述：</b>正面信息 ，查询待发送指令业务接口：根据消息类型（背面信息类型03）查询状态等于16的指令数据。<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-07-11 <br>
	 * @param bifCode 银行接口代码
	 */
	 List<EDraftExcBillVo> queryEDraftExcBillInfo(String bifCode);
	 
	 /**
	  * <b>功能：</b>正面信息--成功修改指令状态为19，修改票据正面信息数据<br>
	  * <b>作者：</b>郑刚<br>
	  * <b>日期：</b> 2018-07-11 <br>	  
	  * @param 正面集合信息 
	  */
	void updateEDraftExcSuccessStatusAndAddBillInfo(VoucherInfoVo voucherInfo);
	
	/**
	 * 新增查询指令
	 * @param bifCode 银行接口代码
	 * @param recordId 对应查询流水号
	 * @param msgType 对应查询类型 （支付信用01，背面信息02，正面信息03）
	 */
	void saveEDraftExcBill(String recordId, String msgType);
}
