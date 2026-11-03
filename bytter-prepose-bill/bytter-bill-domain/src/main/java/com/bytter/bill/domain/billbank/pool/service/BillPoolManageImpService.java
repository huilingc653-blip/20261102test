package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolManageyImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolDtl;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolHoldBook;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolIntoRepealImpService;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.service.IEDraftExcBillService;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据池操作服务类<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-28 <br>
 */
@Service("billPoolManageImpService")
@Transactional
public class BillPoolManageImpService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillPoolManageyImpDao billPoolManageyImpDao;
	@Autowired
	private BillPoolImpService billPoolImpService;
	@Autowired
	private BillPoolDtlImpService billPoolDtlImpService;
	@Autowired
	private BillPoolBankExcImpService billPoolBankExcImpService;
	@Autowired
	private BillPoolQueryExcImpService billPoolQueryExcImpService;
	@DubboReference(check = false)
	private IEDraftExcBillService eDraftExcBillService;
	@Autowired
	private BillPoolIntoRepealImpService billPoolIntoRepealImpService;
	
	@Autowired
	private BillBankDao billBankDao;
	/**
	 * <b>功能：</b>根据查询参数获取票据池操作明细数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * 
	 * @param briefId
	 *            明细表业务类型
	 * @param bifCode
	 *            银行接口代码
	 * @param optStatus
	 *            指令状态
	 * @param reCode
	 *            银行返回代码
	 * @param busId
	 *            业务ID
	 * @return
	 */
	public List<PoolBillVoucherVO> queryPoolBillVoucherInfo(String briefId,
			String optStatus, String bifCode, String reCode, String busId) {
		return billPoolManageyImpDao.queryPoolBillVoucherInfo(briefId,
				optStatus, bifCode, reCode, busId,null);
	}
	
	/**
	 * <b>功能：</b>根据查询参数获取票据池操作明细数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * 
	 * @param briefId
	 *            明细表业务类型
	 * @param bifCode
	 *            银行接口代码
	 * @param optStatus
	 *            指令状态
	 * @param reCode
	 *            银行返回代码
	 * @param busId
	 *            业务ID
	 * @param poolPledgeStatus 质押转托管确认状态:-1质押转托管申请失败、 0质押转托管确认失败、1质押转托管确认在途
	 * @return
	 */
	public List<PoolBillVoucherVO> queryPoolBillVoucherInfo(String briefId,
			String optStatus, String bifCode, String reCode, String busId, String poolPledgeStatus) {
		return billPoolManageyImpDao.queryPoolBillVoucherInfo(briefId,
				optStatus, bifCode, reCode, busId,poolPledgeStatus);
	}
	/**
	 * <b>功能：</b>获取待发送数据-根据查询参数获取票据池操作明细数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * 
	 * @param briefId
	 *            明细表业务类型
	 * @param bifCode
	 *            银行接口代码
	 * @param optStatus
	 *            指令状态
	 * @param reCode
	 *            银行返回代码
	 * @param busId
	 *            业务ID
	 * @return
	 */
	public List<PoolBillVoucherVO> queryApplyByBifCode(String briefId,
			String bifCode, String reCode) {
		return queryPoolBillVoucherInfo(briefId, BillPoolConstant.optStatus_16,
				bifCode, reCode, null);
	}
	/**
	 * <b>功能：</b>获取待返回结果数据-根据查询参数获取票据池操作明细数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * 
	 * @param briefId
	 *            明细表业务类型
	 * @param bifCode
	 *            银行接口代码
	 * @param optStatus
	 *            指令状态
	 * @param reCode
	 *            银行返回代码
	 * @param busId
	 *            业务ID
	 * @return
	 */
	public List<PoolBillVoucherVO> queryApplyResultByBifCode(String briefId,
			String bifCode, String reCode) {
		return queryPoolBillVoucherInfo(briefId, BillPoolConstant.optStatus_18,
				bifCode, reCode, null);
	}
	
	/**
	 * <b>功能：中国银行质押转托管确认修改票据池的指令状态 和明细流水状态</b><br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-11-16 <br>
	 * 
	 * @param paramsVo  修改数据的参数
	 * @return
	 */
	public void updatePoolPdTstPledgeConfirm(String briefId, PoolBillVoucherVO paramsVo) {
		if (StringUtil.isNotEmpty(paramsVo.getBusId())) {
			BillAbAccrecords info = billPoolDtlImpService.queryAccrecordsById(paramsVo.getBusId());
			if(info != null &&  BillPoolConstant.BIF_CODE_BOC.equals(info.getBifCode())
					&& StringUtil.isNotEmpty(paramsVo.getPoolPledgeStatus()) ){
				//判断质押转托管确认状态:-1质押转托管申请失败 0质押转托管确认失败 1质押转托管确认在途
				if(BillPoolConstant.POOL_PLEDGE_STATUS_0.equals(paramsVo.getPoolPledgeStatus()) || BillPoolConstant.POOL_PLEDGE_STATUS_1.equals(paramsVo.getPoolPledgeStatus())){
					String statusString = BillPoolConstant.POOL_PLEDGE_STATUS_0.equals(paramsVo.getPoolPledgeStatus())?"质押转托管确认失败":"质押转托管确认在途";
					String reMsg = StringUtil.isNotEmpty(paramsVo.getReMsg())?paramsVo.getReMsg():"中国银行质押转申请返回结果："+statusString;
					billPoolDtlImpService.updatePoolPledgeStatusByBusId(paramsVo.getBusId(), paramsVo.getPoolPledgeStatus(), reMsg);
				}else if(BillPoolConstant.POOL_PLEDGE_STATUS_FAIL.equals(paramsVo.getPoolPledgeStatus())){
					this.updatePoolApply(briefId, paramsVo);
				}
			}else{
				this.updatePoolApply(briefId, paramsVo);
			}
		}
	}
	
	/**
	 * <b>功能：修改票据池的指令状态 和明细流水状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-28 <br>
	 * 
	 * @param paramsVo
	 *            修改数据的参数
	 * @return
	 */
	public void updatePoolApply(String briefId, PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePoolApply: briefId=" + briefId
					+ ",PoolBillVoucherVO=" + paramsVo.toString());
		}
		if (StringUtil.isNotEmpty(paramsVo.getBusId())) {
			BillAbAccrecords info = billPoolDtlImpService
					.queryAccrecordsById(paramsVo.getBusId());
			if (info != null
					&& (StringUtil.isNotEmpty(paramsVo.getOptStatus()) || StringUtil
							.isNotEmpty(paramsVo.getReCode()))) {
				if (BillPoolConstant.BRIEF_TYPE_IN_DEL.equals(briefId)) {// 入池申请成功的终止需要删除
					if (BillPoolConstant.optStatus_1.equals(paramsVo
							.getOptStatus())) {
						// 发送失败需要重新发送，直到发送成功
						paramsVo.setOptStatus(BillPoolConstant.optStatus_16);
						paramsVo.setReCode(BillPoolConstant.RE_CODE_3);
					} else if (BillPoolConstant.optStatus_19.equals(paramsVo
							.getOptStatus())) {
						billPoolDtlImpService.updateAccrecordsByBusId(
								info.getId(), BillPoolConstant.optStatus_2,
								BillPoolConstant.RE_CODE_F, "退回成功！",paramsVo.getBillId());
					}
					billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
				} else if (!paramsVo.getOptStatus().equals(info.getOptStatus())
						|| !paramsVo.getReCode().equals(info.getReturnCode())) {
					billPoolDtlImpService.updateAccrecordsByBusId(
							paramsVo.getBusId(), paramsVo.getOptStatus(),
							paramsVo.getReCode(), paramsVo.getReMsg(),paramsVo.getBillId());
					AbPoolDtl poolDtl = billPoolDtlImpService
							.queryPoolDtlById(info.getPdId());
					if (logger.isDebugEnabled()) {
						logger.debug("updatePoolApply: briefId=" + briefId
								+ ",BillAbAccrecords=" + info);
					}
					// 失败-1或者发送成功18的时候设置票据池明细指令状态
					if (StringUtil.isNotEmpty(info.getPdId())
							&& (BillPoolConstant.optStatus_1.equals(paramsVo
									.getOptStatus()) || BillPoolConstant.optStatus_18
									.equals(paramsVo.getOptStatus()))) {
						billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
								info.getPdId(), paramsVo.getOptStatus());
						// 发送银行成功以后设置银行发送流水和申请流水
						if (BillPoolConstant.optStatus_18.equals(paramsVo
								.getOptStatus())) {
							paramsVo.setBusId(info.getPdId());
							billPoolBankExcImpService
									.updateBillPoolBankExc(paramsVo);
						}
						// if(poolDtl!=null
						// &&StringUtil.isNotEmpty(poolDtl.getLastid())
						// &&BillPoolConstant.optStatus_1.equals(info.getOptStatus())){//判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行失败
						// billPoolDtlImpService.updatePoolDtlOptStatusByBusId(poolDtl.getLastid(),
						// BillPoolConstant.optStatus_1);
						// }
					}
					if (BillPoolConstant.optStatus_19.equals(paramsVo
							.getOptStatus())) {
						// 质押托管质押业务流水完成即状态成功即可
						if (BillPoolConstant.BRIEF_TYPE_TO_PLEDGE
								.equals(briefId)
								|| BillPoolConstant.BRIEF_TYPE_TO_HOSTING
										.equals(briefId)
								|| BillPoolConstant.BRIEF_TYPE_PLEDGE_HOSTING
										.equals(briefId)) {
							billPoolBankExcImpService
									.updateBillPoolBankExc(paramsVo);
							billPoolDtlImpService
									.updatePoolDtlOptStatusByBusId(
											info.getPdId(),
											paramsVo.getOptStatus());
						} else if (BillPoolConstant.BIF_CODE_CIB.equals(info.getBifCode())
								| (poolDtl != null
										&& BillPoolConstant.BIF_CODE_ABC
												.equals(info.getBifCode()) && poolDtl
										.getInoutType().equals(
												BillPoolConstant.INOUTTYPE_OUT))) {
							// 1、兴业银行纸票入池完成以后修改入池状态为在途 票据池明细表设置为已完成
							// 2、兴业银行出池申请质押解除出差为电票 池状态为出池状态，质押申请出差为纸票 吃状态为出差在途
							String poolStatus = BillPoolConstant.POOL_STATUS_2;
							if (BillPoolConstant.BRIEF_TYPE_OUT_REPLEDGE
									.equals(briefId)) {
								poolStatus = BillPoolConstant.POOL_STATUS_0;
							} else if (BillPoolConstant.BRIEF_TYPE_OUT
									.equals(briefId)) {
								poolStatus = BillPoolConstant.POOL_STATUS_3;
							}
							// 农行托管不做移存处理
							if (BillPoolConstant.BIF_CODE_ABC.equals(info
									.getBifCode())
									&& BillPoolConstant.BRIEF_TYPE_IN
											.equals(briefId)) {
								poolStatus = BillPoolConstant.POOL_STATUS_1;
							}
							// 兴业银行贴现、背书业务性先出池申请以后自动进进行贴现和背书申请业务
							if (poolDtl != null
									&& (BillPoolConstant.BRIEF_TYPE_ENDORSE
											.equals(poolDtl.getBriefId()) || BillPoolConstant.BRIEF_TYPE_DISCOUNT
											.equals(poolDtl.getBriefId()))) {
								BillAbAccrecords infoNew = initBillAbAccrecords(
										info, true, poolDtl.getBriefId(),
										poolDtl.getId(), poolDtl.getBpId());
								billPoolManageyImpDao.save(infoNew);
							} else {
								billPoolDtlImpService
										.updateVoucherinfoViceByBusId(
												info.getVvid(),
												poolStatus,
												AccebillConstant.AB_BOX_STATUS_Y);
								billPoolDtlImpService
										.updatePoolDtlOptStatusByBusId(
												info.getPdId(),
												paramsVo.getOptStatus());
								billPoolBankExcImpService
										.updateBillPoolBankExc(paramsVo);
							}
						} else if (BillPoolConstant.BIF_CODE_CCB.equals(info
								.getBifCode())
								| BillPoolConstant.BIF_CODE_CZB.equals(info
										.getBifCode())
								| BillPoolConstant.BIF_CODE_CITIC.equals(info
										.getBifCode())
								| BillPoolConstant.BIF_CODE_BOC.equals(info
										.getBifCode())
								| (poolDtl != null
										&& BillPoolConstant.BIF_CODE_ABC
												.equals(info.getBifCode()) && poolDtl
										.getInoutType().equals(
												BillPoolConstant.INOUTTYPE_IN))) {
							if (BillPoolConstant.BRIEF_TYPE_IN.equals(briefId)) {
								if (poolDtl != null) {
									// 建行入池申请完成以后根据入池发送质押或者托管生成对应的流水信息
									// 农行入池申请完成以后根据入池发送质押或者托管生成对应的流水信息
									String brifed = null;
									if (BillPoolConstant.BIF_CODE_ABC.equals(info.getBifCode())
											| BillPoolConstant.BIF_CODE_CZB.equals(info.getBifCode())) {
										brifed = BillPoolConstant.BRIEF_TYPE_INPLEDGE;
									} else if (BillPoolConstant.BIF_CODE_CCB.equals(info.getBifCode())
											| BillPoolConstant.BIF_CODE_BOC.equals(info.getBifCode())) {
										brifed = BillPoolConstant.BRIEF_TYPE_INOP;
									}
									BillAbAccrecords infoNew;
									//中信银行直接入池成功，生成质押申请业务流水
									if(BillPoolConstant.BIF_CODE_CITIC.equals(info.getBifCode()) || BillPoolConstant.BIF_CODE_BOC.equals(info.getBifCode())){
										// 如果是票据池入池过来的质押就需要把池状态变化和进行入池完成操作
										billPoolDtlImpService.updateVoucherinfoViceByBusId(
												info.getVvid(),
												BillPoolConstant.POOL_STATUS_1,
												AccebillConstant.AB_BOX_STATUS_Y);
										billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
												info.getPdId(), BillPoolConstant.optStatus_19);
										//修改流水表status:签收成功 19
										billBankDao.updateAccrecordsInfoByRecordId(info.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, paramsVo.getReCode(), paramsVo.getReMsg(),paramsVo.getBillId());
										//修改票据状态为质押已签收
										billBankDao.updateVoucherInfoByVoucherId(info.getVid(), null, AccebillConstant.AB_BILL_STATUS_180006,"0","0");
										
									}else{
										infoNew = initBillAbAccrecords(
												info, false, brifed, null, null);
										billPoolManageyImpDao.save(infoNew);
									}
								}
							} else if (BillPoolConstant.BRIEF_TYPE_OUT_REPLEDGE
									.equals(briefId)) {
								if (poolDtl != null) {
									// 建行质押解除出差申请完成以后还需要生成出池申请的流水信息
									BillAbAccrecords infoNew = initBillAbAccrecords(
											info, false,
											BillPoolConstant.BRIEF_TYPE_OUT,
											null, null);
									billPoolManageyImpDao.save(infoNew);
								}
							} else if (BillPoolConstant.BRIEF_TYPE_INOP_HOSTING
									.equals(briefId)
									|| BillPoolConstant.BRIEF_TYPE_INOP_PLEDGE
											.equals(briefId)
									|| BillPoolConstant.BRIEF_TYPE_OUT
											.equals(briefId)) {
								// 1、建行托管入池和质押入池业务完成
								// 票据池明细表设置为已完成，把电票数据变成在池状态纸票为入池在途
								// 2、建行出池申请纸票为为出差在途 电票为已出池
								String poolStatus = BillPoolConstant.POOL_STATUS_2;
								if (BillPoolConstant.BRIEF_TYPE_OUT
										.equals(briefId)) {
									poolStatus = BillPoolConstant.POOL_STATUS_3;
								}
								BillAbVoucherinfo voucherInfo = billPoolDtlImpService
										.getBillAbVoucherinfoById(info.getVid());
								if (voucherInfo != null
										&& AccebillConstant.AB_BILL_FLAG_ELECT
												.equals(String
														.valueOf(voucherInfo
																.getBillFlag()))) {
									if (BillPoolConstant.BRIEF_TYPE_OUT
											.equals(briefId)) {
										poolStatus = BillPoolConstant.POOL_STATUS_0;
									} else {
										poolStatus = BillPoolConstant.POOL_STATUS_1;
									}
								}
								// 中信、中国银行贴现、背书业务性先出池申请以后自动进进行贴现和背书申请业务
								if ((BillPoolConstant.BIF_CODE_CITIC.equals(info.getBifCode())
										| BillPoolConstant.BIF_CODE_BOC.equals(info.getBifCode()))
									&& poolDtl != null
									&& (BillPoolConstant.BRIEF_TYPE_ENDORSE
												.equals(poolDtl.getBriefId()) || BillPoolConstant.BRIEF_TYPE_DISCOUNT
												.equals(poolDtl.getBriefId()))) {
									BillAbAccrecords infoNew = initBillAbAccrecords(
											info, false, poolDtl.getBriefId(),
											poolDtl.getId(), poolDtl.getBpId());
									billPoolManageyImpDao.save(infoNew);
								}else{
									billPoolDtlImpService.updateVoucherinfoViceByBusId(
											info.getVvid(),poolStatus,AccebillConstant.AB_BOX_STATUS_Y);
									billPoolDtlImpService.updatePoolDtlOptStatusByBusId(info.getPdId(),paramsVo.getOptStatus());
									billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
								}
								if (poolDtl != null
										&& BillPoolConstant.BRIEF_TYPE_IN
												.equals(poolDtl.getBriefId())
										&& StringUtil.isNotEmpty(poolDtl
												.getLastid())) {// 判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行完成
									billPoolDtlImpService
											.updatePoolDtlOptStatusByBusId(
													poolDtl.getLastid(),
													BillPoolConstant.optStatus_19);
								}
							}
						}else{
							//其他银行
							if (BillPoolConstant.BRIEF_TYPE_IN.equals(briefId)) {
								if (poolDtl != null) {
									// 入池申请完成则入池完成
									if (BillPoolConstant.BRIEF_TYPE_IN.equals(poolDtl
											.getBriefId())) {
										// 池状态变化和进行入池完成操作
										billPoolDtlImpService.updateVoucherinfoViceByBusId(
												info.getVvid(),
												BillPoolConstant.POOL_STATUS_1,
												AccebillConstant.AB_BOX_STATUS_Y);
										if (poolDtl != null
												&& StringUtil.isNotEmpty(poolDtl
												.getLastid())) {// 判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行完成
											billPoolDtlImpService
													.updatePoolDtlOptStatusByBusId(
															poolDtl.getLastid(),
															BillPoolConstant.optStatus_19);
										}
									}
									billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
											info.getPdId(), BillPoolConstant.optStatus_19);
								}
							}else if(BillPoolConstant.BRIEF_TYPE_OUT.equals(briefId)){
								if (poolDtl != null) {
									// 入池申请完成则入池完成
									if (BillPoolConstant.BRIEF_TYPE_OUT.equals(poolDtl
											.getBriefId())) {
										// 池状态变化和进行出池完成操作
										billPoolDtlImpService.updateVoucherinfoViceByBusId(
												info.getVvid(),
												BillPoolConstant.POOL_STATUS_0,
												AccebillConstant.AB_BOX_STATUS_Y);
										if (poolDtl != null
												&& StringUtil.isNotEmpty(poolDtl
												.getLastid())) {// 判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行完成
											billPoolDtlImpService
													.updatePoolDtlOptStatusByBusId(
															poolDtl.getLastid(),
															BillPoolConstant.optStatus_19);
										}
										//平安银行出池申请完成自动收入质押解除签收
										if(BillPoolConstant.BIF_CODE_PINGAN.equals(info.getBifCode())){
											BillAbAccrecords infoNew = initBillAbAccrecords(
													info, false, AccebillConstant.BRIEF_STATUS_9019,
													null, null);
											infoNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
											infoNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
											billPoolManageyImpDao.save(infoNew);
										}
									}
									billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
											info.getPdId(), BillPoolConstant.optStatus_19);
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * <b>功能：入池撤销修改票据池的指令状态 和明细流水状态</b><br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * 
	 * @param paramsVo
	 *            修改数据的参数
	 * @return
	 */
	public void updatePoolApplyRepeal(String briefId, PoolBillVoucherVO paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("updatePoolApplyRepeal: briefId=" + briefId
					+ ",PoolBillVoucherVO=" + paramsVo.toString());
		}
		if (StringUtil.isNotEmpty(paramsVo.getBusId())) {
			BillAbAccrecords info = billPoolDtlImpService
					.queryAccrecordsById(paramsVo.getBusId());
			if (info != null
					&& (StringUtil.isNotEmpty(paramsVo.getOptStatus()) || StringUtil
							.isNotEmpty(paramsVo.getReCode()))) {
				if (!paramsVo.getOptStatus().equals(info.getOptStatus())
						|| !paramsVo.getReCode().equals(info.getReturnCode())) {
					billPoolDtlImpService.updateAccrecordsByBusId(
							paramsVo.getBusId(), paramsVo.getOptStatus(),
							paramsVo.getReCode(), paramsVo.getReMsg(),paramsVo.getBillId());
					AbPoolDtl poolDtl = billPoolDtlImpService
							.queryPoolDtlById(info.getPdId());
					if (logger.isDebugEnabled()) {
						logger.debug("updatePoolApplyRepeal: briefId="
								+ briefId + ",BillAbAccrecords="
								+ info);
					}
					// 失败-1或者发送成功18的时候设置票据池明细指令状态
					if (StringUtil.isNotEmpty(info.getPdId())
							&& (BillPoolConstant.optStatus_1.equals(paramsVo
									.getOptStatus()) || BillPoolConstant.optStatus_18
									.equals(paramsVo.getOptStatus()))) {
						billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
								info.getPdId(), paramsVo.getOptStatus());
						// 发送银行成功以后设置银行发送流水和申请流水
						if (BillPoolConstant.optStatus_18.equals(paramsVo
								.getOptStatus())) {
							paramsVo.setBusId(info.getPdId());
							billPoolBankExcImpService
									.updateBillPoolBankExc(paramsVo);
						}
					}
					if (BillPoolConstant.optStatus_19.equals(paramsVo
							.getOptStatus())) {
						if (poolDtl != null) {
							if (BillPoolConstant.BRIEF_TYPE_IN_REPEAL
									.equals(briefId)) {
								if (poolDtl != null) {
									// 入池撤销成功回滚入池申请数据
									billPoolDtlImpService
											.updatePoolDtlOptStatusByBusId(
													info.getPdId(),
													paramsVo.getOptStatus());
									// 修改入池撤销数据为不可用
									billPoolIntoRepealImpService
											.updatePoolIntoRepealByRid(
													poolDtl.getRepealId(),
													AccebillConstant.AB_BILL_VALID_SIGN_2);
									billPoolBankExcImpService
											.updateBillPoolBankExc(paramsVo);
									if (StringUtil.isNotEmpty(info.getLastid())) {
										BillAbAccrecords lastInfo = billPoolDtlImpService
												.queryAccrecordsById(info
														.getLastid());
										if (lastInfo != null) {
											// 修改入池申请流水为撤回且不可用
											billPoolDtlImpService
													.updateAccrecordsByBusId(
															lastInfo.getId(),
															BillPoolConstant.optStatus_3,
															AccebillConstant.AB_BILL_VALID_SIGN_2);
											// 修改入池申请明细为撤回且不可用
											billPoolDtlImpService
													.updatePoolDtlOptStatusByBusId(
															lastInfo.getPdId(),
															BillPoolConstant.optStatus_3,
															AccebillConstant.AB_BILL_VALID_SIGN_2);
											billPoolBankExcImpService
													.updateBillPoolBankExc(paramsVo);
											// 修改入池申请上一笔流水为可用
											billPoolDtlImpService
													.updateAccrecordsByBusId(
															lastInfo.getLastid(),
															AccebillConstant.AB_BILL_VALID_SIGN_1);
											// 修改从表池状态为池外
											billPoolDtlImpService
													.updateVoucherinfoViceByBusId(
															lastInfo.getVvid(),
															BillPoolConstant.POOL_STATUS_0,
															AccebillConstant.AB_BOX_STATUS_Y);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * <b>功能：针对质押解除以后是否进行相应业务申请操作</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-02 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void unPledgeAutoPoolOp(String accId) {
		if (!BillPoolConstant.IS_RUN_POOL) {
			return;
		}
		BillAbAccrecords info = billPoolDtlImpService
				.queryAccrecordsById(accId);
		if (info != null) {
			if (StringUtil.isNotEmpty(info.getPdId())) {
				AbPoolDtl poolDtl = billPoolDtlImpService.queryPoolDtlById(info
						.getPdId());
				if (poolDtl != null
						&& !BillPoolConstant.BRIEF_TYPE_OUT.equals(poolDtl
								.getBriefId())) {// 通过是否有票据池ID判断是否自动进行业务操作
					if (StringUtil.isNotEmpty(poolDtl.getBpId())
							&& BillPoolConstant.optStatus_19.equals(info
									.getOptStatus())) {
						// 查询是否持有质押解除签收流水，否新增一般质押解除签收流水持有，是则往下走业务
						BillAbAccrecords holdAcc = billPoolDtlImpService
								.queryHoldAccrecords(info.getVid(), null,
										info.getReqAcctId());
						if (holdAcc != null) {
							// 修改有效标记为已使用
							billPoolDtlImpService.updateAccrecordsByBusId(
									holdAcc.getId(),
									AccebillConstant.AB_BILL_VALID_SIGN_2);
						}
						BillAbAccrecords infoUnPledge = null;
						if (holdAcc == null
								|| !AccebillConstant.BRIEF_STATUS_9019
										.equals(holdAcc.getBriefId())) {
							// 新增质押解除签收的流水
							infoUnPledge = initBillAbAccrecords(info, true,
									AccebillConstant.BRIEF_STATUS_9019, null,
									poolDtl.getBpId());
							infoUnPledge
									.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
							infoUnPledge
									.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
							infoUnPledge
									.setReturnCode(BillPoolConstant.RE_CODE_1);
							infoUnPledge.setPdId(null);
							billPoolManageyImpDao.save(infoUnPledge);
						} else {
							infoUnPledge = holdAcc;
						}
						BillAbVoucherinfo voucherInfo = billPoolDtlImpService
								.getBillAbVoucherinfoById(info.getVid());
						if (voucherInfo == null) {
							return;
						}
						// 1、写入对应业务流水表
						String briefId = poolDtl.getBriefId();
						boolean isPoolPledgee = BillPoolConstant.BRIEF_TYPE_DISCOUNT
                                .equals(briefId);
                        BillAbAccrecords infoNew = initBillAbAccrecords(info,
								isPoolPledgee, briefId, poolDtl.getId(),
								poolDtl.getBpId());
						infoNew.setLastid(infoUnPledge.getId());
						billPoolManageyImpDao.save(infoNew);
						// 票据池状态为池外
						billPoolDtlImpService.updateVoucherinfoViceByBusId(
								info.getVvid(), BillPoolConstant.POOL_STATUS_0,
								AccebillConstant.AB_BOX_STATUS_Y);
					}
				} else if (poolDtl != null
						&& BillPoolConstant.BRIEF_TYPE_OUT.equals(poolDtl
								.getBriefId())) {
					// 查询是否持有质押解除签收流水，否新增一般质押解除签收流水持有，是则往下走业务
					BillAbAccrecords holdAcc = billPoolDtlImpService
							.queryHoldAccrecords(info.getVid(), null,
									info.getReqAcctId());
					BillAbAccrecords infoUnPledge = null;
					if (holdAcc == null) {
						// 新增质押解除签收的流水
						infoUnPledge = initBillAbAccrecords(info, true,
								AccebillConstant.BRIEF_STATUS_9019, null,
								poolDtl.getBpId());
						infoUnPledge
								.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
						infoUnPledge.setReturnCode(BillPoolConstant.RE_CODE_1);
						infoUnPledge.setPdId(null);
						billPoolManageyImpDao.save(infoUnPledge);
					}
					if (BillPoolConstant.BIF_CODE_CCB.equals(info.getBifCode())
							| BillPoolConstant.BIF_CODE_ABC.equals(info
									.getBifCode())) {
						// 建行质押解除出差申请完成以后还需要生成出池申请的流水信息
						BillAbAccrecords infoNew = initBillAbAccrecords(info,
								false, BillPoolConstant.BRIEF_TYPE_OUT, null,
								null);
						billPoolManageyImpDao.save(infoNew);
					} else {
						billPoolDtlImpService.updateVoucherinfoViceByBusId(
								info.getVvid(), BillPoolConstant.POOL_STATUS_0,
								AccebillConstant.AB_BOX_STATUS_Y);
						billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
								info.getPdId(), BillPoolConstant.optStatus_19);
					}
				}
			}
		}
	}
	/**
	 * <b>功能：针对同意签收是否有自动入池判断，是则进行自动入池申请操作</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-02 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void signAutoPoolOp(String accId) {
		if (!BillPoolConstant.IS_RUN_POOL) {
			return;
		}
		BillAbAccrecords info = billPoolDtlImpService
				.queryAccrecordsById(accId);
		if (logger.isDebugEnabled()) {
			logger.debug("BillAbAccrecords=" + info.toString());
		}
		if (info != null && StringUtil.isNotEmpty(info.getPdId())) {
			AbPoolDtl poolDtl = billPoolDtlImpService.queryPoolDtlById(info
					.getPdId());
			if (logger.isDebugEnabled()) {
				logger.debug("poolDtl=" + poolDtl.toString());
			}
			if (poolDtl != null) {// 通过是否有票据池ID判断是否自动入池
				if (StringUtil.isNotEmpty(poolDtl.getBpId())
						&& BillPoolConstant.optStatus_19.equals(info
								.getOptStatus())) {
					BillAbVoucherinfo voucherInfo = billPoolDtlImpService
							.getBillAbVoucherinfoById(info.getVid());
					if (voucherInfo == null) {
						return;
					}
					// 1、写入票据池明细表入池申请流水（电票默认都是质押入池）
					AbPoolDtl poolDtlNew = new AbPoolDtl();
					poolDtlNew.setBpId(poolDtl.getBpId());
					poolDtlNew.setOptStatus(BillPoolConstant.optStatus_16);
					poolDtlNew.setLastid(poolDtl.getId());
					poolDtlNew.setInoutType(BillPoolConstant.INOUTTYPE_IN);
					poolDtlNew.setBriefId(BillPoolConstant.BRIEF_TYPE_IN);
					poolDtlNew.setVvId(poolDtl.getVvId());
					poolDtlNew.setCreateTime(new Date());
					poolDtlNew.setOrigNo(voucherInfo.getOrigNo());
					poolDtlNew.setStatus(poolDtl.getStatus());
					poolDtlNew.setValidSign(poolDtl.getValidSign());
					billPoolManageyImpDao.save(poolDtlNew);
					// 2、写入流水表
					String briefId = "";
					// 兴业生成质押申请流水
					if (BillPoolConstant.BIF_CODE_CIB.equals(info.getBifCode())
							|| BillPoolConstant.BIF_CODE_CZB.equals(info
									.getBifCode())
							|| BillPoolConstant.BIF_CODE_CITIC.equals(info
											.getBifCode())) {
						briefId = BillPoolConstant.BRIEF_TYPE_INPLEDGE;
					} else if (BillPoolConstant.BIF_CODE_CCB.equals(info
							.getBifCode())
							|| BillPoolConstant.BIF_CODE_ABC.equals(info
									.getBifCode())) {
						// 建行 农行生成入池申请流水
						briefId = BillPoolConstant.BRIEF_TYPE_IN;
					}
					BillAbAccrecords infoNew = initBillAbAccrecords(info, true,
							briefId, poolDtlNew.getId(), poolDtl.getBpId());
					billPoolManageyImpDao.save(infoNew);
					// 修改入池为在途
					billPoolDtlImpService.updateVoucherinfoViceByBusId(
							info.getVvid(), null,
							AccebillConstant.AB_BOX_STATUS_N);
					// 修改签收流水为失效
					billPoolDtlImpService.updateAccrecordsByBusId(info.getId(),
							AccebillConstant.AB_BILL_VALID_SIGN_2);
				} else {// 不需要自动入池票据池签收流水自动完成
				// billPoolDtlImpService.updatePoolDtlOptStatusByBusId(info.getPdId(),
				// BillPoolConstant.optStatus_19);
					String optStatus = BillPoolConstant.optStatus_19;
					if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info
							.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS18
									.equals(info.getOptStatus())) {
						optStatus = BillPoolConstant.optStatus_18;
					} else if (AccebillConstant.AB_BILL_OPT_STATUS_1
							.equals(info.getOptStatus())) {
						optStatus = BillPoolConstant.optStatus_1;
					}
					billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
							info.getPdId(), optStatus);
				}
			}
		}
	}
	/**
	 * 重新初始化票据流水数据信息
	 * 
	 * @param info
	 *            旧票据流水表
	 * @param isPoolPledgee
	 *            是否设置接收人为池银行
	 * @param briefId
	 *            业务类型
	 * @param pdId
	 *            票据池明细ID
	 * @param bpId
	 *            票据池ID
	 * @return
	 */
	public BillAbAccrecords initBillAbAccrecords(BillAbAccrecords info,
			boolean isPoolPledgee, String briefId, String pdId, String bpId) {
		BillAbAccrecords infoNew = new BillAbAccrecords();
		BeanUtils.copyNotNullProperties(info, infoNew);
		infoNew.setRecid(null);
		infoNew.setId(null);
		if (StringUtil.isNotEmpty(pdId)) {
			infoNew.setPdId(pdId);
		}
		infoNew.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
		infoNew.setBriefId(briefId);
		if (isPoolPledgee) {
			PoolBaseVO poolInfo = billPoolImpService.queryPoolById(bpId);
			infoNew.setRecAcctId(poolInfo.getPledgeeAcc());
			infoNew.setRecNm(poolInfo.getPledgeeName());
			infoNew.setRecAcctsvcr(poolInfo.getPledgeeBankNo());
		}
		infoNew.setLastid(info.getId());
		infoNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		infoNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS16);
		infoNew.setReturnCode(BillPoolConstant.RE_CODE_3);
		infoNew.setReturnMsg("票据池接口返回结果自动生成");
		infoNew.setApplyDate(new Date());
		infoNew.setCreateDate(new Date());
		infoNew.setCreateName(null);
		infoNew.setCreateBy(null);
		infoNew.setUpdateDate(null);
		infoNew.setUpdateName(null);
		infoNew.setUpdateBy(null);
		return infoNew;
	}
	
	
	/**
	 * <b>功能：质押申请业务对方签收完成，进行票据池流水变化操作处理</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-02 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void pledgePoolOp(String accId) {
		accebillBusPoolOp(accId);
	}
	/**
	 * <b>功能：贴现申请业务对方签收完成，进行票据池流水变化操作处理</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-03 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void discountPoolOp(String accId) {
		accebillBusPoolOp(accId);
	}
	/**
	 * <b>功能：背书申请业务对方签收完成，进行票据池流水变化操作处理</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-03 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void endorsePoolOp(String accId) {
		accebillBusPoolOp(accId);
	}
	/**
	 * <b>功能：提示付款申请业务对方签收完成，进行票据池流水变化操作处理</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-03 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void presentationPoolOp(String accId) {
		accebillBusPoolOp(accId);
	}
	/**
	 * 进行统一的票据业务票据池功能处理 <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-04 <br>
	 * 
	 * @param accId
	 *            票据流水表ID
	 * @return
	 */
	public void accebillBusPoolOp(String accId) {
		if (!BillPoolConstant.IS_RUN_POOL) {
			return;
		}
		BillAbAccrecords info = billPoolDtlImpService
				.queryAccrecordsById(accId);
		if (info != null
				&& (AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_010.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_9003.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_9010.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_9012.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_9019.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_011.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_011.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_018.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_019.equals(info
								.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_020.equals(info
								.getBriefId()) || AccebillConstant.BRIEF_STATUS_021
							.equals(info.getBriefId()))) {
			if (StringUtil.isNotEmpty(info.getPdId())) {
				AbPoolDtl poolDtl = billPoolDtlImpService.queryPoolDtlById(info
						.getPdId());
				if (poolDtl != null) {
					if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(info
							.getOptStatus())) {
						if (BillPoolConstant.BRIEF_TYPE_IN.equals(poolDtl
								.getBriefId())) {
							// 如果是票据池入池过来的质押就需要把池状态变化和进行入池完成操作
							billPoolDtlImpService.updateVoucherinfoViceByBusId(
									info.getVvid(),
									BillPoolConstant.POOL_STATUS_1,
									AccebillConstant.AB_BOX_STATUS_Y);
							if (poolDtl != null
									&& StringUtil.isNotEmpty(poolDtl
											.getLastid())) {// 判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行完成
								billPoolDtlImpService
										.updatePoolDtlOptStatusByBusId(
												poolDtl.getLastid(),
												BillPoolConstant.optStatus_19);
							}
						}
						billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
								info.getPdId(), info.getOptStatus());
					} else {
						String optStatus = BillPoolConstant.optStatus_1;
						if (AccebillConstant.AB_BILL_OPT_STATUS18.equals(info
								.getOptStatus())) {
							optStatus = BillPoolConstant.optStatus_18;
						}
						billPoolDtlImpService.updatePoolDtlOptStatusByBusId(
								info.getPdId(), optStatus);
						if (poolDtl != null
								&& BillPoolConstant.BRIEF_TYPE_IN
										.equals(poolDtl.getBriefId())
								&& BillPoolConstant.optStatus_1
										.equals(optStatus)) {
							// 修改质押申请之前的业务流水失效避免继续可以做业务
							// 修改前一笔流水为不可用可用
							if (StringUtil.isNotEmpty(info.getLastid())) {
								billPoolDtlImpService.updateAccrecordsByBusId(
										info.getLastid(),
										AccebillConstant.AB_BILL_VALID_SIGN_2);
							}
							// 判断入池或者出差票据池明细表是否有上一个业务ID有则对上一个业务进行失败
							if (StringUtil.isNotEmpty(poolDtl.getLastid())) {
								billPoolDtlImpService
										.updatePoolDtlOptStatusByBusId(
												poolDtl.getLastid(), optStatus);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 根据查询到的持有入池票据进行写入票据池明细表和票据流水表操作 <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-04 <br>
	 * 
	 * @param addList
	 *            持有票据池流水
	 * @return
	 */
	public void addPoolDtlOp(List<AbPoolHoldBook> addList) {
		// 判断单位票据池明细表是否持有此票据。是则不做处理否则进行新增票据池明细表
		for (AbPoolHoldBook book : addList) {
			if (StringUtil.isEmpty(book.getBpId())
					|| StringUtil.isEmpty(book.getCorpId())
					|| StringUtil.isEmpty(book.getOrigNo())) {
				continue;
			}
			// 1、判断票据池明细表是否有可用的
			// 通过池ID和票据号以及单位获取有效票据池明细表（托管入池、托管质押转换）数据
			AbPoolDtl poolDtl = billPoolDtlImpService.getAbPoolDtlHoldByParams(
					book.getBpId(), book.getCorpId(), book.getOrigNo());
			if (poolDtl == null) {// 取不到池内有效数据进行写入池功能
				poolDtl = initAbPoolDtl(book);
				// 1判断有无从表数据无则新增从表数据
				opVoucherinfoVice(poolDtl, book);
				if (StringUtil.isEmpty(poolDtl.getVvId())) {
					continue;
				}
				// 2保存票据池明细表数据
				billPoolManageyImpDao.save(poolDtl);
				// 3判断承兑汇票是否有持有流水有则修改此流水为已作废
				opBillHoldOldAcc(poolDtl, book);
				// 4新增一笔入池申请票据流水
				BillAbAccrecords accInfo = initBillAbAccrecords(poolDtl, book);
				billPoolManageyImpDao.save(accInfo);
				billPoolManageyImpDao.flush();
				// 5添加正面和背面信息查询
				eDraftExcBillService.saveEDraftExcBill(accInfo.getId(),
						EDraftConstant.MSG_TYPE_BILLINFO);
				eDraftExcBillService.saveEDraftExcBill(accInfo.getId(),
						EDraftConstant.MSG_TYPE_BACKINFO);
			}
			// 2、修改从表池状态为池内
			billPoolDtlImpService.updateVoucherinfoViceByBusId(
					poolDtl.getVvId(), BillPoolConstant.POOL_STATUS_1,
					AccebillConstant.AB_BOX_STATUS_Y);
		}
	}
	/**
	 * 根据入池明细写一个出池记录 <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2021-03-24 <br>
	 * 
	 * @param inPoolDtl
	 *            入池流水
	 * @param outDate
	 *            出池流水
	 * @return
	 */
	public void addOutPoolDtlOp(AbPoolDtl inPoolDtl, Date outDate) {
		if (inPoolDtl != null
				&& outDate != null
				&& outDate.getTime() > inPoolDtl.getCreateTime().getTime()
				&& AccebillConstant.AB_BILL_VALID_SIGN_1.equals(inPoolDtl
						.getValidSign())) {
			AbPoolDtl poolDtl = new AbPoolDtl();
			poolDtl.setVid(inPoolDtl.getVid());
			poolDtl.setVvId(inPoolDtl.getVvId());
			poolDtl.setBpId(inPoolDtl.getBpId());
			poolDtl.setBriefId(BillPoolConstant.BRIEF_TYPE_OUT);
			poolDtl.setCreateTime(outDate);
			poolDtl.setUpdateTime(new Date());
			poolDtl.setInoutType(inPoolDtl.getInoutType());
			poolDtl.setOptStatus(BillPoolConstant.optStatus_19);
			poolDtl.setOrigNo(inPoolDtl.getOrigNo());
			poolDtl.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			poolDtl.setValidSign(BillPoolConstant.VALID_SIGN_1);
			// 1保存票据池出池明细表数据
			billPoolManageyImpDao.save(poolDtl);
			// 2新增一笔出池申请票据流水
			BillAbAccrecords infoNew = new BillAbAccrecords();
			infoNew.setVid(poolDtl.getVid());
			infoNew.setVvid(poolDtl.getVvId());
			// infoNew.setReqAcctId(inPoolDtl.getReqAcc());
			// infoNew.setReqNm(inPoolDtl.getReqName());
			// infoNew.setReqAcctsvcr(inPoolDtl.getReqBankNo());
			infoNew.setPdId(poolDtl.getId());
			infoNew.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			infoNew.setBriefId(BillPoolConstant.BRIEF_TYPE_OUT);
			infoNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			PoolBaseVO poolInfo = billPoolImpService.queryPoolById(poolDtl
					.getBpId());
			if (poolInfo != null) {
				infoNew.setRecAcctId(poolInfo.getPledgeeAcc());
				infoNew.setRecNm(poolInfo.getPledgeeName());
				infoNew.setRecAcctsvcr(poolInfo.getPledgeeBankNo());
			}
			infoNew.setLastid(poolDtl.getAccLastid());
			infoNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			infoNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
			infoNew.setReturnCode(BillPoolConstant.RE_CODE_1);
			infoNew.setReturnMsg(null);
			infoNew.setApplyDate(new Date());
			infoNew.setBusinessDate(poolDtl.getCreateTime());
			infoNew.setCreateDate(new Date());
			infoNew.setCreateName(null);
			infoNew.setCreateBy(null);
			infoNew.setUpdateDate(null);
			infoNew.setUpdateName(null);
			infoNew.setUpdateBy(null);
			billPoolManageyImpDao.save(infoNew);
			// 3设置从表票据池池外
			billPoolDtlImpService.updateVoucherinfoViceByBusId(
					inPoolDtl.getVvId(), BillPoolConstant.POOL_STATUS_0,
					AccebillConstant.AB_BOX_STATUS_Y);
			// 4设置入池流水失效
			billPoolDtlImpService.updatePoolDtlValidSignByBusId(
					inPoolDtl.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
		}
	}
	/**
	 * 判断有无从表数据无则新增从表数据
	 * 
	 * @param book
	 */
	private void opVoucherinfoVice(AbPoolDtl poolDtl, AbPoolHoldBook book) {
		BillAbVoucherinfo voucherInfo = billPoolDtlImpService
				.getBillVoucherInfoByOrigNo(poolDtl.getOrigNo());
		BillAbVoucherinfoVice vice = billPoolDtlImpService
				.getVoucherinfoViceByVidAndCorpId(voucherInfo.getId(),
						book.getCorpId(), book.getVoucherVO().getReqAcc());
		if (vice == null) {
			// 不存在从表数据进行新增从表数据
			vice = initBillAbVoucherinfoVice(voucherInfo, poolDtl, book);
			billPoolManageyImpDao.save(vice);
		}
		poolDtl.setVvId(vice.getId());
		poolDtl.setVid(voucherInfo.getId());
	}
	/**
	 * 判断承兑汇票是否有持有流水有则修改此流水为已作废
	 * 
	 * @param book
	 */
	private void opBillHoldOldAcc(AbPoolDtl poolDtl, AbPoolHoldBook book) {
		BillAbAccrecords accInfo = billPoolDtlImpService.queryHoldAccrecords(
				poolDtl.getVid(), poolDtl.getVvId(), null);
		if (accInfo != null) {// 存在持有进行作废处理
			poolDtl.setAccLastid(accInfo.getId());
			// 进行作废
			billPoolDtlImpService.updateAccrecordsByBusId(accInfo.getId(),
					AccebillConstant.AB_BILL_VALID_SIGN_2);
		}
	}
	/**
	 * 初始化票据池明细数据
	 * 
	 * @param book
	 * @return
	 */
	private AbPoolDtl initAbPoolDtl(AbPoolHoldBook book) {
		AbPoolDtl poolDtl = new AbPoolDtl();
		poolDtl.setBpId(book.getBpId());
		poolDtl.setBriefId(BillPoolConstant.BRIEF_TYPE_IN);
		poolDtl.setCreateTime(book.getCreateTime());
		poolDtl.setUpdateTime(book.getUpdateTime());
		poolDtl.setInoutType(book.getInoutType());
		poolDtl.setOptStatus(BillPoolConstant.optStatus_19);
		poolDtl.setOrigNo(book.getOrigNo());
		poolDtl.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
		poolDtl.setValidSign(BillPoolConstant.VALID_SIGN_1);
		return poolDtl;
	}
	/**
	 * 初始化票据入池明细数据
	 * 
	 * @param book
	 * @return
	 */
	private BillAbAccrecords initBillAbAccrecords(AbPoolDtl poolDtl,
			AbPoolHoldBook book) {
		BillAbAccrecords infoNew = new BillAbAccrecords();
		infoNew.setVid(poolDtl.getVid());
		infoNew.setVvid(poolDtl.getVvId());
		infoNew.setReqAcctId(book.getVoucherVO().getReqAcc());
		infoNew.setReqNm(book.getVoucherVO().getReqName());
		infoNew.setReqAcctsvcr(book.getVoucherVO().getReqBankNo());
		infoNew.setPdId(poolDtl.getId());
		infoNew.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
		infoNew.setBriefId(BillPoolConstant.BRIEF_TYPE_IN);
		infoNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
		PoolBaseVO poolInfo = billPoolImpService.queryPoolById(poolDtl
				.getBpId());
		if (poolInfo != null) {
			infoNew.setRecAcctId(poolInfo.getPledgeeAcc());
			infoNew.setRecNm(poolInfo.getPledgeeName());
			infoNew.setRecAcctsvcr(poolInfo.getPledgeeBankNo());
		}
		infoNew.setLastid(poolDtl.getAccLastid());
		infoNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		infoNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
		infoNew.setReturnCode(BillPoolConstant.RE_CODE_1);
		infoNew.setReturnMsg(null);
		infoNew.setApplyDate(new Date());
		infoNew.setBusinessDate(poolDtl.getCreateTime());
		infoNew.setCreateDate(new Date());
		infoNew.setCreateName(null);
		infoNew.setCreateBy(null);
		infoNew.setUpdateDate(null);
		infoNew.setUpdateName(null);
		infoNew.setUpdateBy(null);
		return infoNew;
	}
	/**
	 * 初始化票据入池明细数据
	 * 
	 * @param book
	 * @return
	 */
	private BillAbVoucherinfoVice initBillAbVoucherinfoVice(
			BillAbVoucherinfo voucherInfo, AbPoolDtl poolDtl,
			AbPoolHoldBook book) {
		BillAbVoucherinfoVice vice = new BillAbVoucherinfoVice();
		vice.setBankAcc(book.getVoucherVO().getReqAcc());
		vice.setCorpId(book.getCorpId());
		vice.setApplyCorpId(book.getCorpId());
		vice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
		vice.setPoolStatus(BillPoolConstant.POOL_STATUS_1);
		vice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
		vice.setCreateDate(new Date());
		vice.setVid(voucherInfo.getId());
		vice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		return vice;
	}
	/**
	 * <b>功能：</b>根据池单位ID和池id和查询指令类型新增指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-21 <br>
	 * 
	 * @param bpId
	 *            池id
	 * @param corpId
	 *            单位ID
	 * @param queryType
	 *            查询指令类型：票据池额度01\出入池登记簿02\在池登记簿03\保证金交易明细04
	 */
	public void saveBillPoolQueryExc(String bpId, String corpId,
			String queryType) {
		billPoolQueryExcImpService
				.saveBillPoolQueryExc(bpId, corpId, queryType);
	}
	/**
	 * 获取默认第一个的顶级单位
	 * 
	 * @return
	 */
	public PoolBaseVO getTopLevelCorp() {
		return billPoolQueryExcImpService.getTopLevelCorp();
	}
}
