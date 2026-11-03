package com.bytter.erp.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.erp.dto.AbVoucherinfoDTO;
import com.bytter.erp.dto.NisBillheadDTO;
import com.bytter.erp.dto.VNisPayBillDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 付款接口类
 * @author huangx
 * @since 2017-09-02 09:49:44
 */
public interface INisBillheadService {
	/**
	 * 更新付款单（票据反写接口）
	 * 状态：-1-打回；95-审批通过；99-票据完成
	 * @param id 付款单ID
	 * @param status 状态
	 * @param msg 信息
	 */
	void updateNisBillhead(String id, Integer status, String msg, ICurrentUser user);
	/**
	 * 更新付款单（退款勾兑）
	 * @param id 付款单ID
	 * @param status 状态
	 * @param msg 信息
	 */
	void updateNisBillheadForRefundBlend(String id, Integer status, String msg, String bankSerialNo, ICurrentUser user);
	/**
	 * 记账作废反写账务状态（记账作废使用）
	 * @param billNo
	 * @param status
	 * @param msg
	 * @param tallySign
	 * @param user
	 */
	void updateNisBillheadForInvalidFin(String billNo, Integer status, String msg, String tallySign, ICurrentUser user);
	/**
	 * 更新下拨账号（记账反写接口）
	 * @param billNo 付款单号
	 * @param downAccId 下拨账号Id
	 */
	void updateNisBillheadDownAccId(String billNo, String downAccId);
	/**
	 * 更新接口代码（记账反写接口）
	 * @param billNo 付款单号
	 * @param bankAcc 银行账号
	 */
	void updateNisBillheadForindividual(String billNo, String bankAcc);
	/**
	 * 付款单生成（外模块生成付款单）
	 * @param entity
	 */
	void saveNisBillhead(NisBillheadDTO entity, ICurrentUser user);
	/**
	 * 根据外部单号获取付款单信息
	 * @param erpId
	 * @return 付款单信息集合
	 */
	List<NisBillheadDTO> listNisBillheadByErpId(String erpId);
	/**
	 * 手工记账待记账的付款单
	 * @return 付款单信息
	 */
	DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
	/**
	 * 根据单号获取付款信息
	 * @param billNo
	 */
	NisBillheadDTO getNisBillheadByBillNo(String billNo);
	/**
	 * 根据id获取付款信息
	 * @param id
	 */
	NisBillheadDTO getNisBillheadById(String id);
	/**
     * 修改单据-记账打回使用 zhengxn
     * @param nisBillheadDTO
     * @param user
     */
    void updateNisBillheadByDto(NisBillheadDTO nisBillheadDTO, ICurrentUser user);
	/**
	 * <br>
	 * <b>功能：</b>查询付款计划（企e金服接口）<br>
	 * <b>作者：</b>黄茜<br>
	 * <b>日期：</b>2018-11-25<br>
	 * @param corpflag
	 * @param corpCode
	 * @return 付款金额
	 */
	BigDecimal queryNisBillhead(String corpflag, String corpCode, Date wishPayday);
	/**
	 * 外系统根据时间获取付款单
	 * @param beginDate
	 * @param endDate
	 * @return 付款单集合
	 */
	List<NisBillheadDTO> listNisBillheadByTime(String beginDate, String endDate);
	/**
	 * Erp同步接口-批量保存
	 *
	 * @param list
	 */
	void syncBatchSave(List<NisBillheadDTO> list);
	/**
	 * Erp同步接口-批量删除
	 *
	 * @param list
	 */
	void syncBatchDelete(List<NisBillheadDTO> list);
	/**
	 * Erp接口-根据erpId获取付款单交易状态信息
	 * @param erpId
	 * @return 交易状态
	 */
	String getVoucherStatByErpId(String erpId, String billFrom);
	/**
	 * 判断账号付款是否使用中
	 *
	 * @param blnctrAcc
	 * @return  是否使用中
	 */
	boolean isExistBlnctrAcc(String blnctrAcc);
	/**
	 * 根据银行流水获取付款单
	 * @param dataTablesInput
	 * @return 付款单信息
	 */
	DataTablesOutput listNisBillheadByDtl(DataTablesInput dataTablesInput);
	/**
	 * 付款明细勾对。查询所有未勾对的付款单
	 * @return 付款单信息
	 */
	List<VNisPayBillDTO> listForDtl(Date beginDate, Date endDate);
	/**
	 * 更新付款单银行流水号（付款明细勾兑返写银行流水号）
	 * @param id
	 * @param bankSerialNo
	 */
	void updateNisBillheadForBankSerialNo(String id, String bankSerialNo);
	/**
	 * 根据银行流水获取付款单（退款勾兑）
	 * @param dataTablesInput
	 * @return
	 */
	DataTablesOutput listNisBillheadByRefundblending(DataTablesInput dataTablesInput);
	/**
	 * 退款勾兑打回
	 * @param id
	 * @param user
	 * @param backType
	 */
	void backNisBillhead(String id, ICurrentUser user, String backType);
	/**
	 * 根据银行流水号得到付款单
	 * @param bankSerialNo
	 * @return
	 */
	List<NisBillheadDTO> listNisBillheadByBankSerialNo(String bankSerialNo);
	/**
	 * Erp接口（WebService）获取付款单交易状态信息
	 * @param serialNoErp  外部系统id
	 * @return
	 */
	List<Map<Object,Object>> getVoucherStatByWs(String serialNoErp);
	/**
	 * erp标准接口保存
	 * @param dto
	 */
	void saveErpNisBillhead(NisBillheadDTO dto);
	/*限额begin*/
	/**
	 * 根据付款账号和币别获取付款单的付款金额总和
	 * @param bankAcc 付款账号
	 * @param curId 币别
	 * @return
	 */
	BigDecimal getSum(String bankAcc, String curId, String startTime, String endTime);
	/**
	 * 根据付款账号和币别获取付款单的付款金额总和
	 * @param corpId 单位
	 * @param curId 币别
	 * @return
	 */
	BigDecimal getSumForCorp(String corpId, String curId, String startTime, String endTime);
	/**
	 * 根据id查询数据
	 * @param id 数据项Id
	 * @return
	 */
	NisBillheadDTO getMoneyById(String id);
	/*限额end*/
	/**
	 * 待记账查询
	 */
	List<NisBillheadDTO> queryTallyUnions();
	/**
	 * 根据外部单号获取付款单信息
	 * @param erpId
	 * @return 付款单信息集合
	 */
	NisBillheadDTO getNisBillheadByErpId(String erpId);
	/**
	 * 根据单号删除付款信息
	 * @param id
	 * @param user
	 */
	void deteleNisbillHeadById(String id, ICurrentUser user);
	/**
	 * 票据支付失败处理
	 * @param id 付款单ID
	 * @param failureList
	 * @param successList
	 */
	void backNisBillheadForBill(String id, List<AbVoucherinfoDTO> failureList, List<AbVoucherinfoDTO> successList);
	/**
	 * 票据支付成功处理
	 * @param id 付款单ID
	 */
	void successNisBillheadForBill(String id);
	/**
	 * 获取ERP待处理单据
	 * @return
	 */
	List<NisBillheadDTO> listErpTodoBill();
}
