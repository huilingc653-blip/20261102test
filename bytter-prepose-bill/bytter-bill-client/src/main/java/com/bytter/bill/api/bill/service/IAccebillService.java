package com.bytter.bill.api.bill.service;
import com.bytter.bill.api.bill.dto.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 票据公用接口Service
 * @author luop
 * 2017-08-25 17:14:11
 */
public interface IAccebillService {
	/**
	 * 应收票据批量锁定
	 * @param abAccrecordsIds 票据流水IDs
	 */
	void batchLock(String[] abAccrecordsIds,ICurrentUser userVo);
	
	/**
	 * 应收票据批量解锁
	 * @param abAccrecordsIds 票据流水IDs
	 */
	void batchUnLock(String[] abAccrecordsIds,ICurrentUser userVo);
	/**
	 * 根据id判断是否是票据池明细数据
	 * @param id 流水id
	 * @return 是否是票据池数据
	 */
	boolean isPoolDtl(String id);
	/**
	 * 应收票据批量占用背书申请流水
	 * @param abAccrecordsIds 票据流水IDs
	 * @param userVo 操作用户
	 */
	void batchHoldEndorse(String[] abAccrecordsIds,ICurrentUser userVo);
	/**
	 * 应收票据批量解除占用背书申请流水
	 * @param abAccrecordsIds 票据流水IDs
	 * @param userVo 操作用户
	 */
	void batchUnHoldEndorse(String[] abAccrecordsIds,ICurrentUser userVo);
	
	/**
	 * 应收票据批量背书
	 * @param abAccrecordsIds
	 *  票据流水IDs
	 * @param pledgeeName     票据背入人
	 * @param pledgeeAcc      背入人账号
	 * @param pledgeeBankCode 背入人开户行号
	 * @param businessDate      业务归属日期
	 * @param insideSing      内部业务标志  true:内部背书,false:对外背书
	 * @param userVo      操作人
	 */
	String batchEndorse(String[] abAccrecordsIds, String pledgeeName, String pledgeeAcc, String pledgeeBankCode,String payId,String billBalances,Date businessDate, boolean insideSing,ICurrentUser userVo);
	/**
	 * 修改从表付款状态和付款单id
	 * @param billAccerIds 流水ids
	 * @param isPay 付款状态: 1付过款   0或空则没付过款
	 * @param voucherId 付款单id
	 * @param userVo 当前用户
	 * @param  isBudget 是否挂预算（true：则释放票据原有的预算 false:则不预算票据原有的预算）
	 */
	void updateBillPayInfo(String[] billAccerIds,String isPay,String voucherId,ICurrentUser userVo,boolean isBudget);
	/**
	 * 应付开票暂存
	 * @param abEmitBillDTO 应付开票暂存参数
	 */
	void saveAbEmitBill(AbEmitBillDTO abEmitBillDTO, ICurrentUser userVo);

	/**
	 * 应付开票暂存
	 * @param voDTO 应付开票暂存参数
	 */
	AbEmitBillVoDTO saveAbEmitBillNew(AbEmitBillVoDTO voDTO, ICurrentUser userVo);

	/**
	 * 付款办理开票
	 * @param vids
	 * @param vvids
	 * @param aids
	 * @param userVo
	 */
	void payOpenBillBatchSubmit(String vids, String vvids, String aids, ICurrentUser userVo);
	/**
	 * ERP应付开票暂存
	 * @param abEmitBillDTO 应付开票暂存参数
	 */
	void saveAbEmitBillERP(AbEmitBillDTO abEmitBillDTO);
	/**
	 * ERP应付票据线下兑付确认
	 * @param pay
	 */
	void cashingConfirmationERP(List<AbEmitBillDTO> pay);
	/**
	 * ERP应收票据登记暂存
	 * @param abReceiveBillDTO 应收票据登记暂存参数
	 */
	void saveAbReceiveBillERP(AbReceiveBillDTO abReceiveBillDTO);
	/**
	 * ERP应收票据批量背书
	 * @param
	 */
	void endorseERP(List<AbBillEndorseDTO> abBillEndorseDTOList);
	/**
	 * ERP应收票据批量质押申请
	 * @param
	 */
	void pledgeApplyERP(List<AbBillPledgeApplyDTO> abBillPledgeApplyDTOList);
	/**
	 * ERP应收票据批量质押申请
	 * @param
	 */
	void discountApplyERP(List<AbBillDiscountApplyDTO> abBillDiscountApplyDTOList);
	/**
	 * ERP应收票据批量托收申请
	 * @param
	 */
	void collectionApplyERP(List<AbBillCollectionApplyDTO> abBillCollectionApplyDTOList);
	/**
	 * 获取票据到期可以发起托收申请或逾期托收申请的票据
	 */
	//List<AbBillCollectionApplyVoDTO> getAbBillCollectionApplyVoDTOList();
	/**
	 * 发起托收申请接口
	 * @param aid 票据流水id
	 */
	//void saveAbBillCollectionApply(String aid);
	
	
	/**
	 * 手动记账查询数据接口
	 * @param input 前台查询条件
	 * @param accbookType 记账类型
	 * @param accbookStatus 记账状态 0新增1修改2复核 AccebillConstant.BILLACCBOOK_STATUS_
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getBillAccbookDataTables(DataTablesInput input,String accbookType,String accbookStatus,ICurrentUser user);
	
	/**
	 * 手动记账处理
	 * @param buId 流水ID
	 * @param accbookType 记账类型
	 * @param opType 操作类型 -1记账打回，0复核中，1记账成功  AccebillConstant.BILLACCBOOK_OPTYPE_
	 * @param busGlobalId 记账全局业务ID
	 * @param opMsg  打回信息 不必填
	 * @return
	 */
	public void billAccbookOp(String buId,String accbookType,String opType,String busGlobalId, String opMsg,ICurrentUser userVo);
	/**
	 * 根据票据号获取票据信息
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillAccbookInfoDTO getAbVoucherinfoVOById(String id);
	/**
	 * 根据recordId获取唯一票据贴现信息
	 * @param recordId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbDiscountDTO getAbDiscountByRecordId(String recordId);
	public AbAccrecordsDTO getAbAccrebyLastid(String lastId);
	public String getFcAccCorpId(String netId,String corpId,String otherCorpId,String sysRaceCode,FcVoucherEntryDefineDTO voucherDtl);
	
	/**
	 * 根据流水IDS查看票据信息
	 *
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbVoucherinfoViceDTO> listAccebillInfoByIds(String aids,String payId);
	/**
	 * 修改从表信息为不挂接担保合同
	 * @param id 票据从表id
	 * @param isBearModel 是否挂接担保合同 1为挂接，0为不挂接
	 * @param user
	 * @return
	 */
	void updateGuarantee(String id, String isBearModel, ICurrentUser user);
	/**
	 * 取消，业务回滚
	 */
	void backBillBrief(String id, ICurrentUser userVo);
	/**
	 * 根据数据生成流水并将业务改为完成
	 * @param id 流水表ID
	 *
	 */
	void completeAccrecord(String id, ICurrentUser userVo);
	/**
	 * 根据单位id查询票据待记账数据
	 * @param corpId 单位id
	 * @return true 有待记账数据  false 无待记账数据
	 */
	boolean checkToBeBookByCorpId(String corpId);
	
	/**
	 * 指定日期到期金额
	 * @param userId 用户ID(必填)
	 * @param resource 资源(必填)
	 * @param date 日期 (选填)
	 * @return
	 */
	BigDecimal appointeDayMoney(String userId,String resource,Date date);
	/**
	 * 根据流水查询票据号
	 * 用于收款流水认领
	 * @param aid 流水Id(必填)
	 * @return
	 */
	AbEmitBillDTO selectBillInfo(String aid);
	/**待记账查询*/
	List<AbBillAccbookInfoDTO> queryTallyAbAccbook(String accbookType);
	
	/**
	 * 凭证  V20070049新增
	 * @param aid
	 * @return
	 */
	public AbAccrecordsDTO abAccBookInfo(String aid,String briefId);
	/**
	 * 电票签收接口
	 * origNo 票据号码
	 * signmsgType 签收类型 （002承兑签收、003收票签收、010背书签收、012回购式贴现赎回签收、
     * 				018质押签收、019质押解除签收、020提示付款签收、021逾期提示付款签收）
	 * signUpFlag SU00同意签收、SU01拒收
	 * bankAcc签收人账号
	 * dshnrcd 拒付理由
	 * 			DC00与自己有直接债权债务关系的持票人未履行约定义务；
	 * 			DC01持票人以欺诈、偷盗或者胁迫等手段取得票据；
	 * 			DC02持票人明知有欺诈、偷盗或者胁迫等情形，出于恶意取得票据；
	 * 			DC03持票人明知债务人与出票人或者持票人的前手之间存在抗辩事由而取得票据；
	 * 			DC04持票人因重大过失取得不符合《票据法》规定的票据；
	 * 			DC05超过提示付款期；
	 * 			DC06被法院冻结或收到法院止付通知书；
	 * 			DC07票据未到期；
	 * 			DC08商业承兑汇票承兑人账户余额不足。
	 * 			DC09其他（必须注明:填写到拒付理由）
	 * dshnrrsn 拒付理由备注
	 */
	void billSignOp(AbReceivebizDTO abReceivebiz);
	/**
	 * 保存票据挂接付款金额中间表数据
	 *
	 * @param abBillPayMiddleDTO 票据挂接付款金额中间表实体类
	 */
	void saveBillPayMiddle(AbBillPayMiddleDTO abBillPayMiddleDTO);
	/**
	 * 根据票据从表id获取票据付款中间表集合
	 *
	 * @param vvid 票据从表id
	 * @param payId 付款表id
	 * @return 据付款中间表集合
	 */
	List<AbBillPayMiddleDTO> getAbBillPayMiddleList(String vvid, String payId);
	/**
	 * 根据付款表id修改票据付款中间表状态
	 *
	 * @param payId 付款表id
	 * @param status 新状态
	 */
	void updateAbBillPayMiddleStatus(String payId, Integer status);
	/**
	 * 根据付款单id获取对应票据的所有未删除票据付款中间表集合
	 *
	 * @param payId 付款单id
	 * @return 据付款中间表集合
	 */
	List<AbBillPayMiddleDTO> getAbBillPayMiddleListByPayId(String payId);
	/**
	 * 根据票据付款中间表id修改余额
	 *
	 * @param id      票据付款中间表id
	 * @param balance 新增余额值
	 */
	void updateAbBillPayMiddleBalance(String id, double balance);
	/**
	 * 根据付款id删除对应的票据付款中间表数据
	 *
	 * @param payId 付款id
	 */
	void deleteAbBillPayMiddleByPayId(String payId);
	/**
	 * 根据票据从表id、付款单id删除对应的票据付款中间表数据
	 *
	 * @param billId 票据从表id
	 * @param payId  付款单id
	 */
	void delBillPayMid(String billId, String payId);
	/**
	 * 根据流水表id获取对应流水表信息
	 *
	 * @param id 流水表id
	 * @return 流水表信息
	 */
	List<AbAccrecordsDTO> getAccrecordsById(String id);
	/**
	 * 新增中间表数据
	 * @param buId 业务id
	 * @param middleType 中间表类型
	 */
	void saveBillMiddle(String buId,String middleType,ICurrentUser userVo);
	/**
	 * 获取挂接授信的票据主表ID集合
	 * @return
	 */
	List<String> getBillIdListByRat();
	/**
	 * 应收票据并且未付款时，查询挂接的所有票据是否存在为新系统出票并且分包
	 *
	 * @param vids 票据主表主键id字符串
	 * @return 1：是，0：否：
	 */
	String getNewBillAndSplitTicketFlag(String vids);
	/**
	 * 定时任务查询待生成需求单的承兑到期
	 * @return
	 */
	List<AbAccrecordsDTO> listTaskBillForNisDemand();


	/**
	 * 付款挂接票据锁定金额增加
	 * @param nisBillDtoList 付款关联签收表LISt
	 * @return
	 */
	int batchPayLockMoneyAdd(List<NisBillDTO> nisBillDtoList);

	/**
	 * 付款挂接票据锁定金额减少
	 * @param nisBillDtoList 付款关联签收表LISt
	 * @return
	 */
	int batchPayLockMoneySubtract(List<NisBillDTO> nisBillDtoList);

	void updateBillAccByid(AbAccrecordsDTO dto, String ids,ICurrentUser user);
	void deleteOpenBillInfo(String aid,String reason, ICurrentUser userVo);
	AbEmitBillVoDTO getAbEmitBillVoByAid(String aid);
	Map dealBillInfo(String aid);
	List<AbBillBankAutoDtlDTO> listAbBillBankAutoDtlByParams(Map<String,Object> params);
}
