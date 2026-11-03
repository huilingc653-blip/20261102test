package com.bytter.bill.domain.bill.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbEmitBillSupplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatCpUrationDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 开票补录
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLSUPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLSUPPLY)
@Service("abEmitBillSupplyService")
@Transactional
public class AbEmitBillSupplyService extends BaseService<AbEmitBillSupplyVo, String>
		implements IAccebillBaseService, IWorkFlowListener, IExcelVerifyHandler<AbEmitBillSupplyVo> {
	@Autowired
	private AbEmitBillSupplyDao abEmitBillSupplyDao;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbReceiveBillService abReceiveBillService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbNisBillHeadService abNisBillHeadService;
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbEmitBillService abEmitBillService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbExternalCorpService abExternalCorpService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbFcBondHookService abFcBondHookService;
	@Autowired
	private AbGuaranteeService abGuaranteeService;
	@Autowired
	private AbRatCpUrationService abRatCpUrationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	/**
	 * 开票补录列表查询
	 *
	 * @param input
	 * @return
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTables(DataTablesInput input) {
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_ABEMITBILLSUPPLY_QUERY,input.getCurrentUserId());
		input.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, abVoucherinfoViceService.isEmitMake());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_ABEMITBILLSUPPLY_QUERY,corpIds);
		DataTablesOutput<AbEmitBillSupplyVo> dataTablesUnion = abEmitBillSupplyDao.getDataTablesUnion(input, corpAuthList);
		if (ObjectUtil.isNotEmpty(dataTablesUnion.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_ABEMITBILLSUPPLY_STATUS", "AB_BILL_SEND_SIGN");
			for (AbEmitBillSupplyVo billSupplyVo : dataTablesUnion.getData()) {
				injectValues(billSupplyVo, listDictMap);
			}
		}
		return dataTablesUnion;
	}
	private void injectValues(AbEmitBillSupplyVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质：10 纸票、11 电票
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 开出方式：0  对外开出；1 对内自开
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
		// 开票数据来源
		Optional.ofNullable(bill.getSource()).ifPresent(source -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_EMITBILL_SOURCE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(source)))
					.ifPresent(dict -> bill.setSourceName(dict.getName()));
		});
		// 不得转让标记：EM00可再转让EM01不得转让
		Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
					.ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
		});
		// 业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成    AB_BILL_EMITBILL_STATUS AB_BILL_ABEMITBILLSUPPLY_STATUS
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ABEMITBILLSUPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是；
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});

		//业务标志
		Optional.ofNullable(bill.getBriefId()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSBRIEFID");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});

	}
	/**
	 * 根据条件查询开票登记信息，用于补录
	 *
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbEmitBillSupplyVo getAbEmitBillSupplyVoById(String aid, Model model) {
		AbEmitBillSupplyVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		if (abAccrecords != null) {
			vo = new AbEmitBillSupplyVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setBailType(abVoucherinfoVice.getBailType());
			vo.setBailAcc(abVoucherinfoVice.getBailAcc());
			vo.setIsOpenRat(abVoucherinfoVice.getIsOpenRat());
			vo.setBailmodel(abVoucherinfoVice.getBailModel());
			vo.setBailmoney(abVoucherinfoVice.getBailMoney());
			vo.setBailrate(abVoucherinfoVice.getBailRate());
			vo.setBailContract(abVoucherinfoVice.getBailContract());
			vo.setBailpercent(abVoucherinfoVice.getBailPercent());
			vo.setIsHangGuarantee(abVoucherinfoVice.getBearModel());
			vo.setBriefId(abAccrecords.getBriefId());
			vo.setBankTypeName(StringUtils.isNotEmpty(abAccrecords.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(abAccrecords.getVvid()):null);
			vo.setAid(aid);
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getCorpId())) {
				CorpDTO corpDTO = abCorpService.get(abVoucherinfoVice.getCorpId());
				vo.setCorpName(corpDTO.getName());
				vo.setCorpCode(corpDTO.getCode());
			}
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getApplyCorpId())) {
				CorpDTO corpDTO = abCorpService.get(abVoucherinfoVice.getApplyCorpId());
				vo.setApplyCorpName(corpDTO.getName());
				vo.setApplyCorpCode(corpDTO.getCode());
			}
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getBusCorpId())) {
				vo.setBusCorpName(abCorpService.get(abVoucherinfoVice.getBusCorpId()).getName());
			}
			//纸票且开出方式对内时或第三方票据类型时获取收票人单位用于加载收票人账号
			if((AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf (vo.getBillFlag()))
					|| AbAccebillUtils.isBillTypeTP(vo.getBillType()))
					&& AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (vo.getInsideSing ()))){
				String corpId = "";
				CorpDTO corpInfo = abCorpService.getCorpIdByName (vo.getPayee ());
				if(corpInfo != null){
					//收票人是单位名称的情况
					corpId = corpInfo.getId ();
				}else{
					//收票人通过账户名称获取
					BtBankAccDTO bank = new BtBankAccDTO();
					bank.setAccName (vo.getPayee());
					List<BtBankAccDTO> list = abBtBankAccService.getBankAccList(bank);
					if(list!=null && !list.isEmpty ()){
						corpId = list.get (0).getCorpId ();
					}
				}
				model.addAttribute ("payeeCorpId",corpId);
			}
			// 电票且非第三方票据类型时获取账户是否离线(用于离线电票判断)
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf (vo.getBillFlag()))
					&& StringUtils.isNotEmpty(vo.getPayeracc())
					&& !AbAccebillUtils.isBillTypeTP(vo.getBillType())) {
				try {
					BtBankAccDTO btBankAccDTO = abEmitBillService.getBtBankAccDTO(vo.getPayeracc());
					if (btBankAccDTO != null && model != null) {
						model.addAttribute("isOnline", btBankAccDTO.getCirculationMode());
					}
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 出票人账号不存在
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST);
					} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
							.equals(e.getIMessageEnum())) {
						// 出票人账号不是票据专户
						throw new BusinessException(
								AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			} else {
				if (model != null) {
					// 纸票默认为离线
					model.addAttribute("isOnline", BtBankAccConstant.CIRCULATIONMODE_0);
				}
			}
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_EMITBILL_STATUS", "sf_yn","AB_BILL_INSBRIEFID","AB_BILL_ABACCRECORDS_EMITSTTLMMK", "AB_BILL_SEND_SIGN"
					, "ACCEBILL_EMIT_TYPE", "AB_BILL_BAIL_TYPE");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	/**
	 * 根据条件查询开票登记信息，用于补录（前后端分离专用）
	 *
	 * 前后端分离场景下 Controller 没有 Model，原方法中通过 Model 携带的
	 * payeeCorpId/isOnline 直接放入 VO 返回。
	 *
	 * @param aid
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbEmitBillSupplyVo getAbEmitBillSupplyVoById(String aid) {
		AbEmitBillSupplyVo vo = null;
		AbAccrecords abAccrecords = abAccrecordsService.get(aid, true);
		if (abAccrecords != null) {
			vo = new AbEmitBillSupplyVo();
			BeanUtils.copyNotNullProperties(abVoucherinfoService.get(abAccrecords.getVid(), true), vo);
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
			BeanUtils.copyNotNullProperties(abVoucherinfoVice, vo);
			BeanUtils.copyNotNullProperties(abAccrecords, vo);
			vo.setBailType(abVoucherinfoVice.getBailType());
			vo.setBailAcc(abVoucherinfoVice.getBailAcc());
			vo.setIsOpenRat(abVoucherinfoVice.getIsOpenRat());
			vo.setBailmodel(abVoucherinfoVice.getBailModel());
			vo.setBailmoney(abVoucherinfoVice.getBailMoney());
			vo.setBailrate(abVoucherinfoVice.getBailRate());
			vo.setBailContract(abVoucherinfoVice.getBailContract());
			vo.setBailpercent(abVoucherinfoVice.getBailPercent());
			vo.setIsHangGuarantee(abVoucherinfoVice.getBearModel());
			vo.setBriefId(abAccrecords.getBriefId());
			vo.setBankTypeName(StringUtils.isNotEmpty(abAccrecords.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(abAccrecords.getVvid()):null);
			vo.setAid(aid);
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getCorpId())) {
				CorpDTO corpDTO = abCorpService.get(abVoucherinfoVice.getCorpId());
				vo.setCorpName(corpDTO.getName());
				vo.setCorpCode(corpDTO.getCode());
			}
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getApplyCorpId())) {
				CorpDTO corpDTO = abCorpService.get(abVoucherinfoVice.getApplyCorpId());
				vo.setApplyCorpName(corpDTO.getName());
				vo.setApplyCorpCode(corpDTO.getCode());
			}
			if (StringUtils.isNotEmpty(abVoucherinfoVice.getBusCorpId())) {
				vo.setBusCorpName(abCorpService.get(abVoucherinfoVice.getBusCorpId()).getName());
			}
			//纸票且开出方式对内时或第三方票据类型时获取收票人单位用于加载收票人账号
			if((AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf (vo.getBillFlag()))
					|| AbAccebillUtils.isBillTypeTP(vo.getBillType()))
					&& AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (vo.getInsideSing ()))){
				String corpId = "";
				CorpDTO corpInfo = abCorpService.getCorpIdByName (vo.getPayee ());
				if(corpInfo != null){
					//收票人是单位名称的情况
					corpId = corpInfo.getId ();
				}else{
					//收票人通过账户名称获取
					BtBankAccDTO bank = new BtBankAccDTO();
					bank.setAccName (vo.getPayee());
					List<BtBankAccDTO> list = abBtBankAccService.getBankAccList(bank);
					if(list!=null && !list.isEmpty ()){
						corpId = list.get (0).getCorpId ();
					}
				}
				vo.setPayeeCorpId(corpId);
			}
			// 电票且非第三方票据类型时获取账户是否离线(用于离线电票判断)
			if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf (vo.getBillFlag()))
					&& StringUtils.isNotEmpty(vo.getPayeracc())
					&& !AbAccebillUtils.isBillTypeTP(vo.getBillType())) {
				try {
					BtBankAccDTO btBankAccDTO = abEmitBillService.getBtBankAccDTO(vo.getPayeracc());
					if (btBankAccDTO != null) {
						vo.setIsOnline(btBankAccDTO.getCirculationMode());
					}
				} catch (BusinessException e) {
					if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
						// 出票人账号不存在
						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST);
					} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
							.equals(e.getIMessageEnum())) {
						// 出票人账号不是票据专户
						throw new BusinessException(
								AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL);
					} else {
						throw new BusinessException(e.getIMessageEnum());
					}
				}
			} else {
				// 纸票默认为离线
				vo.setIsOnline(BtBankAccConstant.CIRCULATIONMODE_0);
			}
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_EMITBILL_STATUS", "sf_yn","AB_BILL_INSBRIEFID","AB_BILL_ABACCRECORDS_EMITSTTLMMK", "AB_BILL_SEND_SIGN"
					, "ACCEBILL_EMIT_TYPE", "AB_BILL_BAIL_TYPE");
			injectValues(vo, listDictMap);
		}
		return vo;
	}
	/**
	 * 开票补录提交
	 *
	 * @param abEmitBillSupplyVo
	 */
	public void makeupSave(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser userVo) {
		checkData(abEmitBillSupplyVo);// 补录要素检查
		/** 补录票据主表信息 */
		AbVoucherinfo abVoucherinfovo = getAbVoucherinfo(abEmitBillSupplyVo, userVo);
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abEmitBillSupplyVo.getVid());
		BeanUtils.copyNotNullProperties(abVoucherinfovo, abVoucherinfo,"sbbllRngStrtSn","sbbllRngEndSn");
		abVoucherinfoService.saveOrUpdateAbVoucherinfo(abVoucherinfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE0, userVo);
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abEmitBillSupplyVo.getVvid());
		if (StringUtils.isNotEmpty(abEmitBillSupplyVo.getAttachmentId())) {
			// 补录添加附件
			abVoucherinfoVice.setAttachmentId(abEmitBillSupplyVo.getAttachmentId());
		}
		abVoucherinfoVice.setRatmodel(abEmitBillSupplyVo.getRatmodel());
		//判断挂银行授信
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillSupplyVo.getRatmodel())){
			abVoucherinfoVice.setRatBankCorpType(abEmitBillSupplyVo.getRatBankCorpType());//银行授信单位类型
			abVoucherinfoVice.setRatBankCorpId(abEmitBillSupplyVo.getRatBankCorpId());//银行授信单位CorpId
			abVoucherinfoVice.setRatBankType(abEmitBillSupplyVo.getRatBankType());//授信银行类型
		}else{
			abVoucherinfoVice.setRatBankCorpType(null);//银行授信单位类型
			abVoucherinfoVice.setRatBankCorpId(null);//银行授信单位CorpId
			abVoucherinfoVice.setRatBankType(null);//授信银行类型
		}
		abVoucherinfoVice.setCreditCode(abEmitBillSupplyVo.getCreditCode());
		//补录摘要，备注信息
		abVoucherinfoVice.setAbs(abEmitBillSupplyVo.getAbs());
		abVoucherinfoVice.setRmk(abEmitBillSupplyVo.getRmk());
		//保证金合同
		abVoucherinfoVice.setBailContract(abEmitBillSupplyVo.getBailContract());
		//保证金账户
		abVoucherinfoVice.setBailAcc(abEmitBillSupplyVo.getBailAcc());
		//保证金类型
		abVoucherinfoVice.setBailType(abEmitBillSupplyVo.getBailType());
		abVoucherinfoVice.setIsOpenRat(abEmitBillSupplyVo.getUrationDTO()==null?null:abEmitBillSupplyVo.getUrationDTO().getIsOpenRat());
		abVoucherinfoVice.setBailModel(abEmitBillSupplyVo.getBailmodel());
		abVoucherinfoVice.setBearModel(abEmitBillSupplyVo.getIsHangGuarantee());//是否挂接担保
		abVoucherinfoVice.setInsideRatModel(abEmitBillSupplyVo.getIsHangCredit());//是否挂接内部授信
		abVoucherinfoVice.setInsideCreditCode(abEmitBillSupplyVo.getInsideCreditCode());//内部授信代码
		abVoucherinfoVice.setEmitType(abEmitBillSupplyVo.getEmitType());//开票方式
		abVoucherinfoVice.setEmitGuandisSign(abEmitBillSupplyVo.getEmitGuandisSign());//是否开票保贴
		abVoucherinfoVice.setBusPersonnel(abEmitBillSupplyVo.getBusPersonnel());//业务人员
		abVoucherinfoViceService.update(abVoucherinfoVice);
		AbAccrecords abAccrecords = getAbAccrecords(abEmitBillSupplyVo, userVo);
		abAccrecords.setLastid(abEmitBillSupplyVo.getAid());
		// 新增开票补录流水
		abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
		abAccrecordsService.saveAccrecords(abAccrecords, userVo);
		// 开票登记流水改为删除状态
		int num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), true, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		// 从表box_status改为10
		num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
				AccebillConstant.AB_BOX_STATUS_Y, userVo);
		if (num != 1) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		//保存客商信息,省市信息为空表明没有修改过客商信息，则不需要更新客商信息
		if(AccebillConstant.AB_BILL_TYPE_MERCH.equals (String.valueOf (abEmitBillSupplyVo.getBillType ()))
				&&StringUtils.isNotEmpty (abEmitBillSupplyVo.getAcceptProvince ())){
			saveExternalCorpDTO(abEmitBillSupplyVo);
		}
		// 执行资金计划
		fbsBudgetProcess(abEmitBillSupplyVo, userVo);
		//新增挂接内部授信处理流程
		if(null!=abEmitBillSupplyVo.getIsHangCredit()){
			abEmitBillSupplyVo.setRatCpUrationDTO(new RatCpUrationDTO());
			if(!StringUtils.isBlank(abEmitBillSupplyVo.getCpUrationId())){
				abEmitBillSupplyVo.getRatCpUrationDTO().setCpUrationId(abEmitBillSupplyVo.getCpUrationId());
			}
			abEmitBillSupplyVo.getRatCpUrationDTO().setCpRatCode(abEmitBillSupplyVo.getCpRatCode());
			abEmitBillSupplyVo.getRatCpUrationDTO().setCpRatItemName(abEmitBillSupplyVo.getCpUrationId());
			abEmitBillSupplyVo.getRatCpUrationDTO().setCpRatItemCode(abEmitBillSupplyVo.getCpRatItemCode());
			abEmitBillSupplyVo.getRatCpUrationDTO().setCurCode(abEmitBillSupplyVo.getUrationDTO().getCurCode());
		}
		//挂保证金授信业务处理流程
		if(AccebillConstant.AB_BILL_RATMODEL_Y.equals(abEmitBillSupplyVo.getRatmodel())
				&& null!=abEmitBillSupplyVo.getUrationDTO()
				&& StringUtils.isNotEmpty(abEmitBillSupplyVo.getUrationDTO().getRatCode())
				&& !" ".equals(abEmitBillSupplyVo.getUrationDTO().getRatCode())
				&& StringUtils.isNotBlank(abEmitBillSupplyVo.getUrationDTO().getIsOpenRat())){
			abEmitBillSupplyVo.getUrationDTO().setBondMoney(abEmitBillSupplyVo.getBailmoney());//设置保证金金额
			abEmitBillSupplyVo.getUrationDTO().setBailRate(abEmitBillSupplyVo.getBailpercent());//占用保证金比例
			abEmitBillSupplyVo.getUrationDTO().setBondCurId(abEmitBillSupplyVo.getUrationDTO().getCurCode());//占用币别
			abEmitBillSupplyVo.getUrationDTO().setCurCode(abEmitBillSupplyVo.getUrationDTO().getCurCode());//占用币别
			abEmitBillSupplyVo.getUrationDTO().setBillMoney(abEmitBillSupplyVo.getBillMoney());//业务金额
		}
		// 挂接授信
		abAccrecords.setRatUrationDTO(abEmitBillSupplyVo.getUrationDTO());
		abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_SUPPLY,userVo);
		//挂接保证金
		abFcBondHookService.addOrUpateBailBySupply(abEmitBillSupplyVo,userVo, FcBondConstant.STATUE_WAIT_CHECK);
		// 挂接担保
		abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_SUPPLY,abEmitBillSupplyVo,userVo);
		abAccrecords.setRatCpUrationDTO(abEmitBillSupplyVo.getRatCpUrationDTO());
		//挂接内部授信
		abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_SUPPLY,abAccrecords,userVo);
		abBillNotificationService.completeMsg(abAccrecords,userVo);
		abVoucherinfoViceService.updateBackBriefIdIsNull(abEmitBillSupplyVo.getVvid());// 从表清除打回标记
		// 启动工作流
		AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
		BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
		BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
		 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLSUPPLY, abAccrecords.getId(), variables,
				abVoucherinfoVice.getApplyCorpId(),userVo.getId());
		abLogService.saveAbLog(abEmitBillSupplyVo.getVvid(), abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6101,
				AccebillConstant.ABLOG_LOGTYPE_SUBMIT, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
	}
	/**
	 * 开票补录批量完成
	 *
	 * @param vids
	 *            票据主表ids
	 * @param vvids
	 *            票据从表ids
	 * @param aids
	 *            流水表ids 1、批量提交时，系统开启挂预算，通过流水查询是否有预算信息，没有则提示要挂预算
	 *            2、批量提交时，系统开启挂授信，则判断RAT_MODEL是否挂授信,如果为空或者要挂授信，则CREDIT_CODE
	 *            授信代码不能为空(为空提示要挂授信)
	 */
	public void batchMakeup(String vids, String vvids, String aids, ICurrentUser userVo) {
		String[] abVoucherinfoIds = vids.split(",");
		String[] abVoucherinfoViceIds = vvids.split(",");
		String[] abAccrecordsIds = aids.split(",");
		checkData(abVoucherinfoIds, abVoucherinfoViceIds);// 补录要素检查
		AbAccrecords abAccrecords = null;
		AbAccrecords abAccrecordsOld = null;
		AbVoucherinfo abVoucherinfo = null;
		AbVoucherinfoVice abVoucherinfoVice = null;
		boolean isBudget = abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001);// 系统是否开启资金计划
		boolean isRelateRat = abRatUrationService.isRelateRat();// 系统是否开启挂授信
		// 批量新增补录流水:VALID_SIGN=1 STATUS=(审批=11否则95-开票补录完成) BRIEF_ID=6101
		for (int i = 0; i < abAccrecordsIds.length; i++) {
			abAccrecords = new AbAccrecords();
			abAccrecordsOld = abAccrecordsService.get(abAccrecordsIds[i]);
			abVoucherinfo = abVoucherinfoService.get(abVoucherinfoIds[i]);
			abVoucherinfoVice = abVoucherinfoViceService.get(abVoucherinfoViceIds[i], true);
			// 资金计划校验 (付款单生成票据的资金计划无需校验)
			if (isBudget && StringUtils.isBlank (abVoucherinfoVice.getVoucherId ())) {
				List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = abFbsBudgetProcessService
						.getBudgetProcessByBillNoForBill(BudgetConstant.BUSINESS_NAME_EMIT,
								BudgetConstant.PROCESS_SYSNAME_AB, abVoucherinfoViceIds[i]);
				if (fbsBudgetProcessDTOList == null || fbsBudgetProcessDTOList.isEmpty()) {
					// {0}单号的票据没有选择资金计划，请编辑后再提交!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ISBUDGET_ISNOTEXIST,
							abVoucherinfoVice.getAppNo());
				}
			}
			// 授信校验 (根据前端控制，系统目前只有银票支持挂授信)
			if (isRelateRat && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf (abVoucherinfo.getBillType ()))
					&& !AccebillConstant.AB_RATMODEL_SING_N.equals(abVoucherinfoVice.getRatmodel())
					&& StringUtils.isEmpty(abVoucherinfoVice.getCreditCode())) {
				// {0}单号的票据没有编辑授信信息，请编辑后再提交!
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ISRELATERAT_ISNOTEXIST,
						abVoucherinfoVice.getAppNo());
			}
			abAccrecords.setLastid(abAccrecordsIds[i]);
			abAccrecords.setLastbillcode(abVoucherinfo.getBillStatus());
			abAccrecords.setInsideSing(abAccrecordsOld.getInsideSing());// 内部业务标志
			abAccrecords.setBankendrsmtmk(abAccrecordsOld.getBankendrsmtmk());// 是否可转让
			abAccrecords.setApplyDate(new Date());// 申请日期
			abAccrecords.setBusinessDate(abAccrecordsOld.getBusinessDate());// 业务归属日期
			abAccrecords.setAbs(abAccrecordsOld.getAbs());// 摘要
			abAccrecords.setRmk(abAccrecordsOld.getRmk());// 备注
			abAccrecords.setCreditCode(abAccrecordsOld.getCreditCode());// 授信代码
			abAccrecords.setBailAcc(abAccrecordsOld.getBailAcc());// 保证金账户
			abAccrecords.setBailmoney(abAccrecordsOld.getBailmoney());// 保证金
			abAccrecords.setBailpercent(abAccrecordsOld.getBailpercent());// 保证金比例
			abAccrecords.setBailrate(abAccrecordsOld.getBailrate());// 保证金利率
			abAccrecords.setFeemoney(abAccrecordsOld.getFeemoney());// 手续费
			// abAccrecords.setAdr(abAccrecordsOld.getAdr());//保证人地址
			abAccrecords.setCdtRatgs(abVoucherinfo.getPayerCdtRatgs());// 开票信用等级
			abAccrecords.setCdtRatgGgcy(abVoucherinfo.getPayerCdtRatgGgcy());// 开票评级机构
			abAccrecords.setCdtRatgDuedt(abVoucherinfo.getPayerCdtRatgDuedt());// 开票评级到期日
			// abAccrecords.setReqRole(reqRole);//业务申请人类别
			abAccrecords.setReqNm(abVoucherinfo.getPayer());// 业务申请人
			// abAccrecords.setReqCmonid(abVoucherinfo.getPayer());//业务申请人组织机构代码
			abAccrecords.setReqAcctId(abVoucherinfo.getPayeracc());// 业务申请人账号
			abAccrecords.setReqAcctsvcr(abVoucherinfo.getPayerbankno());// 业务申请人开户行号
			// abAccrecords.setReqAcctsvcrAg(abVoucherinfo.getPayerbankno());//业务申请人承接行号
			// abAccrecords.setRecRole(abVoucherinfo.getRmk());//业务接收人类别
			abAccrecords.setRecNm(abVoucherinfo.getPayee());// 业务接收人名称
			// abAccrecords.setRecCmonid(abVoucherinfo.getPayee());//业务接收人组织机构代码
			abAccrecords.setRecAcctId(abVoucherinfo.getPayeeacc());// 业务接收人账号
			abAccrecords.setRecAcctsvcr(abVoucherinfo.getPayeebankno());// 业务接收人开户行号
			// abAccrecords.setRecAcctsvcrAg(abVoucherinfo.getPayee());//业务接收人承接行号
			abAccrecords.setCreateName(userVo.getUsername());// 创建人名称
			abAccrecords.setCreateBy(userVo.getLoginName());// 创建人登录名称
			abAccrecords.setCreateDate(new Date());// 创建日期
			abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
			abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
			abAccrecords.setBriefId(AccebillConstant.BRIEF_STATUS_6101);
			abAccrecords.setVid(abVoucherinfoIds[i]);
			abAccrecords.setVvid(abVoucherinfoViceIds[i]);
			// abAccrecords.setAbs("storage");//入库标识
			abAccrecordsService.saveAccrecords(abAccrecords, userVo);
			// 开票登记流水改为删除状态
			int num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), true, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为10
			num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			//挂接保证金
			//abFcBondHookService.updateFcBondApply(abVoucherinfoVice,userVo,FcBondConstant.STATUE_WAIT_CHECK);
			abBillNotificationService.completeMsg(abAccrecords,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoViceIds[i]);// 从表清除打回标记
			// 启动工作流
			AbAccrecordsVO variables = new AbAccrecordsVO();// 审批数据
			BeanUtils.copyNotNullProperties(abVoucherinfo, variables);// 票据信息填充到审批对象
			BeanUtils.copyNotNullProperties(abAccrecords, variables);// 票据流水信息填充到审批对象
			 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLSUPPLY, abAccrecords.getId(), variables,
					abVoucherinfoVice.getApplyCorpId(),userVo.getId());
		}
		abLogService.batchSaveAbLog(vvids.split(","), aids.split(","), AccebillConstant.BRIEF_STATUS_6101,
				AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
	}
	/**
	 * 开票登记执行资金计划
	 */
	protected void fbsBudgetProcess(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser userVo) {
		String[] itemCode = abEmitBillSupplyVo.getItemCode();
		String[] itemName = abEmitBillSupplyVo.getItemName();
		BigDecimal[] processMoney = abEmitBillSupplyVo.getProcessMoney();
		FbsBudgetProcessDTO fbsBudgetProcessDTO = null;
		FbsBudgetProcessDTO fbsBudgetProcessOld = null;
		List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();// 预算占用
		/**判断是否挂资金计划*/
		// 判断付款方式是否挂资金计划 1、票据模块配置开启挂接资金计划(控制层判断) 2、预算科目维护不为空 3、当前单位是否要挂预算
		boolean isBudget = abVoucherinfoViceService.isBudget(abEmitBillSupplyVo.getCorpId(), AccebillConstant.BRIEF_STATUS_001)
				&& StringUtils.isNotEmpty(abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001))
				&& itemCode != null;
		if( StringUtils.isBlank (abEmitBillSupplyVo.getVoucherId ()) && !isBudget ) {
			return;//非付款生成的票据且开票登记不挂预算
		}
		/**删除原来的资金计划*/
		if(StringUtils.isNotBlank (abEmitBillSupplyVo.getVoucherId ())){
			//付款应付票据的资金计划
			List<FbsBudgetProcessDTO> oldList = this.abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
					(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,abEmitBillSupplyVo.getVoucherId ());
			if(oldList!=null && !oldList.isEmpty ()){
				fbsBudgetProcessOld = oldList.get (0);
				// 修改时，先删除之前的计划执行数据
				this.abFbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
						BudgetConstant.PROCESS_SYSNAME_NIS, fbsBudgetProcessOld.getBillNo ());
			}
		}else{
			// 修改时，先删除之前的计划执行数据
			abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
					BudgetConstant.PROCESS_SYSNAME_AB, abEmitBillSupplyVo.getVvid());
		}
		if(itemCode!=null){
			/**写入资金计划*/
			for (int i = 0; i < itemCode.length; i++) {
				fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				if(fbsBudgetProcessOld!=null){
					BeanUtils.copyNotNullProperties(fbsBudgetProcessOld, fbsBudgetProcessDTO);
					fbsBudgetProcessDTO.setId (null);
				}else{
					fbsBudgetProcessDTO.setCorpId(abEmitBillSupplyVo.getCorpId());// 单位id
					fbsBudgetProcessDTO.setProcessDate(abEmitBillSupplyVo.getBusinessDate());// 执行日期
					fbsBudgetProcessDTO.setBillNo(abEmitBillSupplyVo.getAppNo());// 业务单据号
					fbsBudgetProcessDTO.setBillId(abEmitBillSupplyVo.getVvid());//流水表id
//					BtBankAccDTO bankAccInfo = null;
//					try {
//						bankAccInfo = abEmitBillService.getBtBankAccDTO(abEmitBillSupplyVo.getPayeracc());
//						if (bankAccInfo != null) {
					fbsBudgetProcessDTO.setCurId(abVoucherinfoViceService.getCurIdByCNY());// 币别
//						}
//					} catch (BusinessException e) {
//						if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
//							// 出票人账号不存在
//							throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST);
//						} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
//								.equals(e.getIMessageEnum())) {
//							// 出票人账号不是票据专户
//							throw new BusinessException(
//									AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL);
//						} else {
//							throw new BusinessException(e.getIMessageEnum());
//						}
//					}
					fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_EMIT);// 执行业务名称
					fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
				}
//				fbsBudgetProcessDTO.setExchangeRate(abEmitBillSupplyVo.getExchangeRate()!=null?abEmitBillSupplyVo.getExchangeRate()[i]:null);
//				fbsBudgetProcessDTO.setProcessCurMoney(abEmitBillSupplyVo.getProcessCurMoney()!=null?abEmitBillSupplyVo.getProcessCurMoney()[i]:null);
				fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
				//添加内转判断标识 innerSign  1-内转 0-非内转 开出方式对内，同一个网点下的成员单位 判定为内转
				CorpDTO corpInfo = abCorpService.getCorpIdByName (abEmitBillSupplyVo.getPayee ());
				String payeeCorpId = null;
				if(corpInfo!=null){
					payeeCorpId = corpInfo.getId ();
				}
				if(AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (abEmitBillSupplyVo.getInsideSing ()))
						&& payeeCorpId != null
						&& abCorpService.isTheSameNetByCorpId(abEmitBillSupplyVo.getCorpId (),payeeCorpId)){
					fbsBudgetProcessDTO.setInnerSign ("1");
				}else{
					fbsBudgetProcessDTO.setInnerSign ("0");
				}
				fbsBudgetProcessDTO.setProcessCurMoney(null!=abEmitBillSupplyVo && null!=abEmitBillSupplyVo.getProcessCurMoney()?abEmitBillSupplyVo.getProcessCurMoney()[i]:new BigDecimal(0.00));
				fbsBudgetProcessDTO.setExchangeRate(null!=abEmitBillSupplyVo && null!=abEmitBillSupplyVo.getExchangeRate()?abEmitBillSupplyVo.getExchangeRate()[i]:new BigDecimal(0.00));
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);// 科目代码
				fbsBudgetProcessDTO.setItemName(itemName[i]);// 科目名称
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);// 执行金额
				fbsBudgetProcessDTO.setCreateBy(userVo.getLoginName());// 创建人登录名称
				fbsBudgetProcessDTO.setCreateDate(new Date());// 创建日期
				list.add(fbsBudgetProcessDTO);
			}
		}
		if (!list.isEmpty()) {
			abFbsBudgetProcessService.writeBudgetProcess(list,userVo);
		}
	}
	@Override
	public BaseDao<AbEmitBillSupplyVo, String> getEntityDao() {
		return null;
	}
	/**
	 * 商票补录时，如果客商信息有修改则保存客商信息
	 *
	 * @param abEmitBillSupplyVo
	 * @return AbVoucherinfo
	 */
	protected void saveExternalCorpDTO(AbEmitBillSupplyVo abEmitBillSupplyVo){
		ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
		externalCorpDTO.setName (abEmitBillSupplyVo.getAcceptor ());//客商名称
		externalCorpDTO.setCorpId (abEmitBillSupplyVo.getCorpId ());//客商所属单位id
		externalCorpDTO.setProvince (abEmitBillSupplyVo.getAcceptProvince ());//所在省
		externalCorpDTO.setCity (abEmitBillSupplyVo.getAcceptCity ());//所在市
		externalCorpDTO.setSourceSystem("accebill");//数据来源
		//客商账号
		List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
		ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
		externalCorpBankAccDTO.setExternalAcc(abEmitBillSupplyVo.getAcceptoracc ());//客商银行账号
		externalCorpBankAccDTO.setBankType(abEmitBillSupplyVo.getAcceptBankTypeId ());//银行类别
		externalCorpBankAccDTO.setBank(abEmitBillSupplyVo.getAcceptorBankName ());//银行名称
		externalCorpBankAccDTO.setBankSourceCode(abEmitBillSupplyVo.getAcceptorbankno ());//联行号
		externalCorpBankAccDTO.setAccProvince(abEmitBillSupplyVo.getAcceptProvince ());//省份
		externalCorpBankAccDTO.setAccCity(abEmitBillSupplyVo.getAcceptCity ());//城市
		externalCorpBankAccDTO.setIsDefaultAcc("0");//首选银行：1是 0否
		externalCorpBankAccList.add(externalCorpBankAccDTO);
		externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
		abExternalCorpService.saveIfNotExist(externalCorpDTO);
	}
	/**
	 * 根据提交参数生成AbVoucherinfo 票据主表对象
	 *
	 * @param abEmitBillSupplyVo
	 * @return AbVoucherinfo
	 */
	protected AbVoucherinfo getAbVoucherinfo(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser userVo) {
		AbVoucherinfo entity = new AbVoucherinfo();
		entity.setOrigNo(abEmitBillSupplyVo.getOrigNo());// 票据号码
		entity.setPayeracc(abEmitBillSupplyVo.getPayeracc());// 出票人账号
		entity.setPayerBankName(abEmitBillSupplyVo.getPayerBankName());// 出票人开户银行名称
		entity.setPayerbankno(abEmitBillSupplyVo.getPayerbankno());// 出票人开户银行行号
		entity.setPayeeacc(abEmitBillSupplyVo.getPayeeacc());// 收票人账号
		entity.setPayeeBankName(abEmitBillSupplyVo.getPayeeBankName());// 出票人开户银行名称
		entity.setPayeebankno(abEmitBillSupplyVo.getPayeebankno());// 出票人开户银行行号
		entity.setStartDate(abEmitBillSupplyVo.getStartDate());// 出票日期
		entity.setEndDate(abEmitBillSupplyVo.getEndDate());// 到期日期
		entity.setAcceptor(abEmitBillSupplyVo.getAcceptor());// 承兑人
		entity.setAcceptoracc(abEmitBillSupplyVo.getAcceptoracc());// 承兑人账号
		entity.setAcceptorbankno(abEmitBillSupplyVo.getAcceptorbankno());// 承兑人开户行行号
		entity.setAcceptorBankName(abEmitBillSupplyVo.getAcceptorBankName());// 承兑人开户行名称
		entity.setAcceptorbankaddress(abEmitBillSupplyVo.getAcceptorbankaddress());// 承兑人开户行地址
		entity.setAcontractNo(abEmitBillSupplyVo.getAcontractNo());// 承兑合同号
		entity.setAccptncagrmtnb(abEmitBillSupplyVo.getAccptncagrmtnb());// 承兑协议编号
		entity.setAinvcnb(abEmitBillSupplyVo.getAinvcnb());// 发票号
		entity.setPayerCdtRatgs(abEmitBillSupplyVo.getPayerCdtRatgs());// 开票信用等级
		entity.setPayerCdtRatgGgcy(abEmitBillSupplyVo.getPayerCdtRatgGgcy());// 开票评级机构
		entity.setPayerCdtRatgDuedt(abEmitBillSupplyVo.getPayerCdtRatgDuedt());// 开票评级到期日
		entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
		entity.setUpdateName(userVo.getUsername());// 修改人名称
		entity.setUpdateBy(userVo.getLoginName());// 修改人登录名称
		entity.setUpdateDate(new Date());// 修改日期
		entity.setNewStmIssuofpprInd(abEmitBillSupplyVo.getNewStmIssuofpprInd());// 出票标志
		entity.setBlgrdbagcrclPrmtInd(abEmitBillSupplyVo.getBlgrdbagcrclPrmtInd());
		return entity;
	}
	/**
	 * 根据提交参数生成AbAccrecords 票据流水表对象
	 *
	 * @param abEmitBillSupplyVo
	 * @return AbAccrecords
	 */
	protected AbAccrecords getAbAccrecords(AbEmitBillSupplyVo abEmitBillSupplyVo, ICurrentUser userVo) {
		AbAccrecords entity = new AbAccrecords();
		entity.setVid(abEmitBillSupplyVo.getVid());// 票据主表ID
		entity.setVvid(abEmitBillSupplyVo.getVvid());// 票据从表ID
		// 此处需要添加审批判断
		entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_6101);// 业务类型
		entity.setInsideSing(abEmitBillSupplyVo.getInsideSing());// 内部业务标志
		entity.setBankendrsmtmk(abEmitBillSupplyVo.getBanendrsmtmk());// 是否可转让
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(abEmitBillSupplyVo.getBusinessDate());// 业务归属日期
		entity.setAbs(abEmitBillSupplyVo.getAbs());// 摘要
		entity.setRmk(abEmitBillSupplyVo.getRmk());// 备注
		entity.setCreditCode(abEmitBillSupplyVo.getCreditCode());// 授信代码
		entity.setBailAcc(abEmitBillSupplyVo.getBailAcc());// 保证金账户
		entity.setBailmoney(abEmitBillSupplyVo.getBailmoney());// 保证金
		entity.setBailpercent(abEmitBillSupplyVo.getBailpercent());// 保证金比例
		entity.setBailrate(abEmitBillSupplyVo.getBailrate());// 保证金利率
		entity.setFeemoney(abEmitBillSupplyVo.getFeemoney());// 手续费
		// entity.setAdr(abEmitBillSupplyVo.getAdr());//保证人地址
		entity.setCdtRatgs(abEmitBillSupplyVo.getPayerCdtRatgs());// 开票信用等级
		entity.setCdtRatgGgcy(abEmitBillSupplyVo.getPayerCdtRatgGgcy());// 开票评级机构
		entity.setCdtRatgDuedt(abEmitBillSupplyVo.getPayerCdtRatgDuedt());// 开票评级到期日
		entity.setBillFlag(abEmitBillSupplyVo.getBillFlag());// 流通介质
		entity.setLastbillcode(abEmitBillSupplyVo.getBillStatus());
		entity.setLastid(abEmitBillSupplyVo.getAid());
		// entity.setReqRole(reqRole);//业务申请人类别
		entity.setReqNm(abEmitBillSupplyVo.getPayer());// 业务申请人
		// entity.setReqCmonid(abEmitBillSupplyVo.getPayer());//业务申请人组织机构代码
		entity.setReqAcctId(abEmitBillSupplyVo.getPayeracc());// 业务申请人账号
		entity.setReqAcctsvcr(abEmitBillSupplyVo.getPayerbankno());// 业务申请人开户行号
		// entity.setReqAcctsvcrAg(abEmitBillSupplyVo.getPayerbankno());//业务申请人承接行号
		// entity.setRecRole(abEmitBillSupplyVo.getRmk());//业务接收人类别
		entity.setRecNm(abEmitBillSupplyVo.getPayee());// 业务接收人名称
		// entity.setRecCmonid(abEmitBillSupplyVo.getPayee());//业务接收人组织机构代码
		entity.setRecAcctId(abEmitBillSupplyVo.getPayeeacc());// 业务接收人账号
		entity.setRecAcctsvcr(abEmitBillSupplyVo.getPayeebankno());// 业务接收人开户行号
		// entity.setRecAcctsvcrAg(abEmitBillSupplyVo.getPayee());//业务接收人承接行号
		entity.setCreateName(userVo.getUsername());// 创建人名称
		entity.setCreateBy(userVo.getLoginName());// 创建人登录名称
		entity.setCreateDate(new Date());// 创建日期
		return entity;
	}
	/**
	 * 票据补录完成后对内部自开的票据做入库处理(新增应收票据暂存数据)
	 *
	 * @return
	 */
	protected boolean batchStorage(String vids, String vvids, String aids, ICurrentUser userVo) {
		AbReceiveBillVO abReceiveBillVO = new AbReceiveBillVO();
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(vvids);// 原票据从表信息
		AbVoucherinfoVice abVoucherinfoViceNew = new AbVoucherinfoVice();// 待入库票据从表信息
		AbAccrecords abAccrecordsNew = new AbAccrecords();// 待入库流水表信息
//		BtBankAccDTO bankAccInfo = null;
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(vids);// 票据主信息
		AbAccrecords abAccrecords = abAccrecordsService.get(aids);// 票据流水
//		AbAccrecords abAccrecordLast = abAccrecordsService.get(abAccrecords.getLastid());//上一笔流水
		/** 对内自开的纸票以及对内自开离线模式的电票入库 */
		if (AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abVoucherinfoVice.getInsideSing()))) {
			BtBankAccDTO payeeBankAcc = abAccrecordsService.getBtBankAccInfo(abVoucherinfo.getPayeeacc());
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))
					&&payeeBankAcc!=null
					&&BtBankAccConstant.CIRCULATIONMODE_1.equals(payeeBankAcc.getCirculationMode())
					&& !AbAccebillUtils.isBillTypeTP(abVoucherinfo.getBillType())){
				return false;
			}
//			try {
//				bankAccInfo = abEmitBillService.getBtBankAccDTO(abAccrecords.getReqAcctId());
//			} catch (BusinessException e) {
//				if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
//					// 出票人账号不存在
//					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTEXIST);
//				} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST
//						.equals(e.getIMessageEnum())) {
//					// 出票人账号不是票据专户
//					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYERACC_ISNOTELECTRICBILL);
//				} else {
//					throw new BusinessException(e.getIMessageEnum());
//				}
//			}
			abVoucherinfoViceNew = getAbVoucherinfoVice(abVoucherinfo, abAccrecords);
			if(abVoucherinfoViceNew==null){
				return false;
			}
			//判断此票据号收款人是否已入库存，已入库存不需要自动写入库存 zhengg 20180619
			if(abVoucherinfoViceService.checkPayeeOrigNo(abVoucherinfo.getOrigNo(), abVoucherinfoViceNew.getCorpId())){
				return false;
			}
			abAccrecordsNew = getAbAccrecordsNew(abVoucherinfo, abAccrecords);
//			if (BtBankAccConstant.equals(bankAccInfo.getCirculationMode())) {
//			if (AccebillConstant.AB_BILL_SEND_SIGN_N.equals(bankAccInfo.getCirculationMode())) {
//				if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))) {
//					// 账号在线模式，只入库纸票
//					abReceiveBillVO.setAbVoucherInfo(abVoucherinfo);
//					abReceiveBillVO.setAbVoucherinfoVice(abVoucherinfoViceNew);
//					abReceiveBillVO.setAbAccrecords(abAccrecordsNew);
//					abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, AccebillConstant.BILL_OPERATE_SAVE,
//							userVo);
//					abBillNotificationService.sendMsg(abReceiveBillVO.getId());
//				}
//			} else {
			// 离线电票和纸票进行入库操作
			abReceiveBillVO.setAbVoucherInfo(abVoucherinfo);
			abReceiveBillVO.setAbVoucherinfoVice(abVoucherinfoViceNew);
			abReceiveBillVO.setAbAccrecords(abAccrecordsNew);
			abReceiveBillService.registerAbReceiveBill(abReceiveBillVO, AccebillConstant.BILL_OPERATE_SAVE, userVo);
			abBillNotificationService.sendMsg(abReceiveBillVO.getId());
//			}
			// 入库成功后，需要收票,故需锁定 当前从表 box_status改为10
			abVoucherinfoViceService.update(abAccrecordsNew.getVvid(), AccebillConstant.AB_BOX_STATUS_N,
					AccebillConstant.AB_BOX_STATUS_Y, userVo);
		}
		return true;
	}
	/**
	 * 根据票据主表和流水表生成从表入库数据
	 */
	protected AbVoucherinfoVice getAbVoucherinfoVice(AbVoucherinfo abVoucherinfo, AbAccrecords abAccrecords) {
		AbVoucherinfoVice abVoucherinfoVice = new AbVoucherinfoVice();
//		BtBankAccDTO btBankAccDTO = null;
//		try {
//			btBankAccDTO = abEmitBillService.getBtBankAccDTO(abVoucherinfo.getPayeeacc());
//		} catch (BusinessException e) {
//			if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL.equals(e.getIMessageEnum())) {
//				// 收票人账号不存在
//				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTEXIST);
//			} else if (AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ELECTRICBILL_ISNOTEXIST.equals(e.getIMessageEnum())) {
//				// 收票人账号不是票据专户
//				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_PAYEEACC_ISNOTELECTRICBILL);
//			} else {
//				throw new BusinessException(e.getIMessageEnum());
//			}
//		}
//		abVoucherinfoVice.setCorpId(btBankAccDTO.getCorpId());// 持票单位(收票人变为持票单位)----通过收票人账号获取单位id
		CorpDTO recCorp=abAccrecordsService.getCorpInfo(abVoucherinfo.getPayeeacc(), abVoucherinfo.getPayee());
		if(recCorp==null){
			return null;
		}
		abVoucherinfoVice.setCorpId(recCorp.getId());// 票据单位(收票人变为票据单位)----通过收票人账号获取单位id
		abVoucherinfoVice.setApplyCorpId(recCorp.getId());// 申请单位（与票据单位一致）
		abVoucherinfoVice.setBusCorpId(recCorp.getId());// 业务单位（与票据单位一致）
		abVoucherinfoVice.setBankAcc(abVoucherinfo.getPayeeacc());// 持票单位账号
		abVoucherinfoVice.setBankCode(abVoucherinfo.getPayeebankno());// 持票单位银行行号
		abVoucherinfoVice.setBankName(abVoucherinfo.getPayeeBankName());// 持票单位银行名称
		abVoucherinfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);// 应收票据
		abVoucherinfoVice.setEnterStock(AccebillConstant.AB_ENTER_STOCK_CORP);// 所属库存:10单位库存11中心代管12银行托管
		abVoucherinfoVice.setBillSource(AccebillConstant.AB_BILL_SOURCE_REC);// 票据来源：10
		// 收票；11
		// 背书；12
		// 质押
		abVoucherinfoVice.setCnAccrecords(abVoucherinfo.getPayer());// 前手(出票人变为前手)
//		abVoucherinfoVice.setAbs(abAccrecords.getAbs());
//		abVoucherinfoVice.setRmk(abAccrecords.getRmk());
		abVoucherinfoVice.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_Y));// 内部业务标识
		abVoucherinfoVice.setIsSetCreateBy(false);
		return abVoucherinfoVice;
	}
	/**
	 * 根据票据主表和流水表生成流水入库数据
	 */
	protected AbAccrecords getAbAccrecordsNew(AbVoucherinfo abVoucherinfo, AbAccrecords abAccrecords) {
		AbAccrecords abAccrecordsNew = new AbAccrecords();
		abAccrecordsNew.setBusinessDate(new Date());// 业务归属日期
		abAccrecordsNew.setBankendrsmtmk(abVoucherinfo.getBanendrsmtmk());// 不得转让标记：EM00
		// 可再转让；EM01
		// 不得转让
		abAccrecordsNew.setCnAccrecords(abVoucherinfo.getPayer());// 前手
//		abAccrecordsNew.setAbs(abAccrecords.getAbs());
//		abAccrecordsNew.setRmk(abAccrecords.getRmk());
		abAccrecordsNew.setBillFlag(abAccrecords.getBillFlag());// 流通介质
		abAccrecordsNew.setLastid(abAccrecords.getId());
		abAccrecordsNew.setLastbillcode(abVoucherinfo.getBillStatus());
//		abAccrecordsNew.setReqNm(abAccrecords.getReqNm());// 业务申请人
//		abAccrecordsNew.setReqAcctId(abAccrecords.getReqAcctId());// 业务申请人账号
//		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务申请人开户行号
//
//		abAccrecordsNew.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
//		abAccrecordsNew.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
//		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
		abAccrecordsNew.setReqNm(abAccrecords.getRecNm());// 业务申请人账号
		abAccrecordsNew.setReqAcctId(abAccrecords.getRecAcctId());// 业务申请人账号
		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());
		abAccrecordsNew.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
		abAccrecordsNew.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
		abAccrecordsNew.setBillSource(AccebillConstant.AB_BILL_SOURCE_REC);
		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);// 设置流水创建人为空
		return abAccrecordsNew;
	}
	/**
	 * 补录时数据检查 票据号码、出票日期、到期日期、出票人账号、承兑人、承兑人账号 不能为空
	 *
	 * @return
	 */
	protected void checkData(AbEmitBillSupplyVo abEmitBillSupplyVo) {
		// 票据号重复验证
		if (abVoucherinfoService.checkOrigNo(abEmitBillSupplyVo.getCorpId(), abEmitBillSupplyVo.getOrigNo(),
				abEmitBillSupplyVo.getVvid(),AccebillConstant.AB_VOUCHERINFO_OP_TYPE0)) {
			// 该票据号已经存在,请检查!
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ORIGNO_REPEAT);
		}
		// 票据号
		if (StringUtils.isEmpty(abEmitBillSupplyVo.getOrigNo())) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ORIGNOISNULL);
		}
		// 出票日期
		if (abEmitBillSupplyVo.getStartDate() == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_STARTDATEISNULL);
		}
		// 到期日期
		if (abEmitBillSupplyVo.getEndDate() == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ENDDATEISNULL);
		}
		// 出票人账号 (纸票或第三方票据类型时的账号为空 2018/3/5 修改)
		boolean flag = !AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillSupplyVo.getBillFlag()))
				&& !AbAccebillUtils.isBillTypeTP(abEmitBillSupplyVo.getBillType());
		if (StringUtils.isEmpty(abEmitBillSupplyVo.getPayeracc()) && flag) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_PAYERACCISNULL);
		}
		// 承兑人
		if (StringUtils.isEmpty(abEmitBillSupplyVo.getAcceptor())) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORISNULL);
		}
		// 承兑人账号 (纸票的商票允许账号为空 2018/3/5 修改)
		if (StringUtils.isEmpty(abEmitBillSupplyVo.getAcceptoracc()) && flag) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORACCISNULL);
		}
	}
	/**
	 * 批量补录时数据检查 票据号码、出票日期、到期日期、出票人账号、承兑人、承兑人账号 不能为空
	 *
	 * @return
	 */
	protected void checkData(String[] vids, String[] vvids) {
		AbVoucherinfo abVoucherinfo = new AbVoucherinfo();
		AbVoucherinfoVice abVoucherinfoVice = new AbVoucherinfoVice();
		for (int i = 0; i < vids.length; i++) {
			abVoucherinfo = abVoucherinfoService.get(vids[i]);
			abVoucherinfoVice = abVoucherinfoViceService.get(vvids[i]);
			// 票据号
			if (StringUtils.isNotEmpty(abVoucherinfo.getOrigNo())) {
				// 票据号重复验证
				if (abVoucherinfoService.checkOrigNo(abVoucherinfoVice.getCorpId(), abVoucherinfo.getOrigNo(),
						abVoucherinfoVice.getId(),AccebillConstant.AB_VOUCHERINFO_OP_TYPE0)) {
					// 票据号已经存在,请检查!
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ORIGNO_REPEAT);
				}
			} else {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ORIGNOISNULL);
			}
			// 出票日期
			if (abVoucherinfo.getStartDate() == null) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_STARTDATEISNULL);
			}
			// 到期日期
			if (abVoucherinfo.getEndDate() == null) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ENDDATEISNULL);
			}
			// 出票人账号 (纸票的商票允许账号为空 2018/3/5 修改)
			//2018/6/15  只要是纸票的，出票人账号可为空
			boolean flag = !AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abVoucherinfo.getBillFlag()))
					&& !AbAccebillUtils.isBillTypeTP(abVoucherinfo.getBillType());
			/*&&AccebillConstant.AB_BILL_TYPE_MERCH.equals (String.valueOf (abVoucherinfo.getBillType ()))*/
			if (StringUtils.isEmpty(abVoucherinfo.getPayeracc()) && flag) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_PAYERACCISNULL);
			}
			// 承兑人
			if (StringUtils.isEmpty(abVoucherinfo.getAcceptor())) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORISNULL);
			}
			// 承兑人账号
			if (StringUtils.isEmpty(abVoucherinfo.getAcceptoracc()) && flag) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_ABEMITBILL_ACCEPTORACCISNULL);
			}
		}
	}
	/**
	 * 审批
	 *
	 * @param aid
	 * @param isPassOK
	 *            true:审批通过，false打回
	 */
	public void approve(String aid, String appMsg, boolean isPassOK, ICurrentUser userVo) {
		AbAccrecords abAccrecords = abAccrecordsService.get(aid,true);
		int num = 0;
		if (isPassOK) {
			/** 审批通过 */
			// 补录流水表status改为95 validSing改为1
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为11
			abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
					AccebillConstant.AB_BOX_STATUS_N, userVo);
			// 入库
			batchStorage(abAccrecords.getVid(), abAccrecords.getVvid(), aid, userVo);
//			/** 占用预算 11改为95 */
			fbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
					BudgetConstant.PROCESS_SYSNAME_AB, abAccrecords.getVvid(), BudgetConstant.STATUE_CHECK,userVo);
			// 付款单付款完成
			if (abVoucherinfoViceService.get(abAccrecords.getVvid()) != null) {
				String voucherId = abVoucherinfoViceService.get(abAccrecords.getVvid()).getVoucherId();
				if (StringUtils.isNotEmpty(voucherId)) {
					// 99 付款完成
					abNisBillHeadService.updateNisBillhead(voucherId, NetbankConstant.STATUE_BILL_COMPLETE, null,userVo);
				}
			}
			// 占用预算
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get (abAccrecords.getVvid (),true);
			if(StringUtils.isNotBlank (abVoucherinfoVice.getVoucherId ())){
				List<FbsBudgetProcessDTO> list = this.abFbsBudgetProcessService.getBudgetProcessByBillNoForBill
						(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,abVoucherinfoVice.getVoucherId ());
				if(list!=null && !list.isEmpty ()){
					abFbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,BudgetConstant.PROCESS_SYSNAME_NIS,
							list.get (0).getBillNo (), BudgetConstant.STATUE_CHECK,userVo);
				}
			}else{
				abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
						BudgetConstant.PROCESS_SYSNAME_AB, abAccrecords.getVvid(), BudgetConstant.STATUE_CHECK,userVo);
			}
			// 挂接银行授信
			abRatUrationService.opRatUration(abAccrecords, AccebillConstant.BILL_OPERATE_APPOK,userVo);
			// 挂接内部授信
			abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_APPOK, abAccrecords, userVo);
			//挂接保证金 占用保证金
			//abFcBondHookService.updateFcBondApply(abVoucherinfoVice,userVo,FcBondConstant.STATUE_APPLY);
			// 挂接担保
			AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBearVoByAid(abAccrecords.getId());
			abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_APPOK,abEmitBillVo,userVo);
		} else {
			/** 打回 */
			// 补录流水表status改为-2
			num = abAccrecordsService.approve(aid, AccebillConstant.AB_BILL_BUS_STATUS_2, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 开票登记流水恢复为有效
			num = abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表box_status改为11
			num = abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
					AccebillConstant.AB_BOX_STATUS_N, userVo);
			if (num != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
			// 从表添加打回标记
			abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), AccebillConstant.BRIEF_STATUS_6101,
					abAccrecords.getId());
			// 预算为在途状态，此处不做状态改变
			// fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_EMIT,
			// BudgetConstant.PROCESS_SYSNAME_AB, abAccrecords.getVvid());
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get (abAccrecords.getVvid (),true);
			//挂接保证金 打回保证金
			//abFcBondHookService.updateFcBondApply(abVoucherinfoVice,userVo,FcBondConstant.STATUE_BACK);
		}
		String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
		abLogService.saveAbLog(abAccrecords.getVvid(), aid, AccebillConstant.BRIEF_STATUS_001, logType, appMsg, userVo);
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLSUPPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		DataTablesOutput<AbEmitBillSupplyVo> dp = abEmitBillSupplyDao.dataTableApprove(dataTablesInput, businessList);
		if (ObjectUtil.isNotEmpty(dp.getData())) {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_EMITBILL_SOURCE", "AB_BANEM", "AB_BILL_ABEMITBILLSUPPLY_STATUS", "AB_BILL_SEND_SIGN");
			for (AbEmitBillSupplyVo billSupplyVo : dp.getData()) {
				injectValues(billSupplyVo, listDictMap);
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 工作流启动回调方法(无需关注)
	 */
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODOAuto-generated method stub
	}
	/**
	 * 审批回调方法
	 *
	 * @param workFlowResult
	 * processDefinitionKey
	 *            工作流类型key
	 * businessKey
	 *            业务类型key
	 * result
	 *            审批结果:true审批通过，false审批打回
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
		approve(workFlowResult.getBusinessKey(), appMsg, workFlowResult.isPass(), workFlowResult.getUser());
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbEmitBillSupplyVo obj) {
		return null;
	}
	@Override
	public void verifyHandler(List<AbEmitBillSupplyVo> list) {
		if (list != null && !list.isEmpty()) {
			List<String> errorMsgList = null;
			BtBankAccDTO btBankAccDTO = null;
			Calendar calendar = null;
			AbEmitBillSupplyVo abEmitBillSupplyVoOld = null;
			int count = 0;
			boolean isOnline = false;// 是否离线标记
			for (AbEmitBillSupplyVo abEmitBillSupplyVo : list) {
				errorMsgList = new ArrayList<>();
				if (StringUtils.isNotEmpty(abEmitBillSupplyVo.getAppNo())) {
					/** 开票登记单号校验 */
					abEmitBillSupplyVoOld = abEmitBillSupplyDao
							.getAbVoucherinfoViceByAppNo(abEmitBillSupplyVo.getAppNo());
					if (abEmitBillSupplyVoOld == null) {
						errorMsgList.add(AccebillConstant.BILL_ACCEBILL_APPNO_ISNOTEXIST);
						abEmitBillSupplyVo.addErrorMsg(errorMsgList);
						continue;
					} else {
						// 判断电票且非第三方票据类型时是否离线(以开票登记时的出票人账号来判断)
						if (AccebillConstant.AB_BILL_FLAG_ELECT
								.equals(String.valueOf(abEmitBillSupplyVoOld.getBillFlag()))
								&& !AbAccebillUtils.isBillTypeTP(abEmitBillSupplyVoOld.getBillType())) {
							btBankAccDTO = abEmitBillService.getBtBankAccDTOExcel(abEmitBillSupplyVoOld.getPayeracc());
							if (BtBankAccConstant.CIRCULATIONMODE_0.equals(btBankAccDTO.getCirculationMode())) {
								isOnline = true;
							}
						}
					}
					// 纸票或离线电票或第三方票据
					if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abEmitBillSupplyVoOld.getBillFlag()))
							|| isOnline || AbAccebillUtils.isBillTypeTP(abEmitBillSupplyVoOld.getBillType())) {
						/** 日期校验:开票日期，到期日期 */
						if (abEmitBillSupplyVo.getStartDate() == null) {
							abEmitBillSupplyVo.setStartDate(abEmitBillSupplyVoOld.getStartDate());
						}
						if (abEmitBillSupplyVo.getEndDate() == null) {
							abEmitBillSupplyVo.setEndDate(abEmitBillSupplyVoOld.getEndDate());
						}
						if (abEmitBillSupplyVo.getStartDate() != null && abEmitBillSupplyVo.getEndDate() != null) {
							if (abEmitBillSupplyVo.getStartDate().getTime() >= abEmitBillSupplyVo.getEndDate()
									.getTime()) {
								errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_COMSTARTTOEND);
							}
							if (abEmitBillSupplyVo.getBusinessDate() != null && (abEmitBillSupplyVo.getBusinessDate()
									.getTime() < abEmitBillSupplyVo.getStartDate().getTime()
									|| abEmitBillSupplyVo.getStartDate().getTime() > abEmitBillSupplyVo.getEndDate()
									.getTime())) {
								errorMsgList.add(AccebillConstant.BILL_ACCEBILL_ABRECEIVEBILL_CHECKBUSINESSDATE);
							}
							calendar = Calendar.getInstance();
							calendar.setTime(abEmitBillSupplyVo.getStartDate());
							if (AccebillConstant.AB_BILL_FLAG_PAPER
									.equals(String.valueOf(abEmitBillSupplyVoOld.getBillFlag()))) {
								calendar.add(Calendar.MONTH, 6);
								if (calendar.getTime().getTime() < abEmitBillSupplyVo.getEndDate().getTime()) {
									errorMsgList.add(AccebillConstant.BILL_ACCEBILL_BILLFLAG_10);
								}
							} else {
								calendar.add(Calendar.YEAR, 1);
								if (calendar.getTime().getTime() < abEmitBillSupplyVo.getEndDate().getTime()) {
									errorMsgList.add(AccebillConstant.BILL_ACCEBILL_BILLFLAG_11);
								}
							}
						}
						/** 票据号码校验 */
						if (StringUtils.isEmpty(abEmitBillSupplyVo.getOrigNo())) {
							errorMsgList.add(AccebillConstant.LANG_ERROR_ORIGNO_ISNOTNULL);
						}
						/** 出票人账号校验 */
						// 出票人账号如果没有填写，则无需校验，取开票登记的数据即可
						if (StringUtils.isNotEmpty(abEmitBillSupplyVo.getPayeracc())) {
							if(btBankAccDTO == null){
								btBankAccDTO = abEmitBillService.getBtBankAccDTOExcel(abEmitBillSupplyVo.getPayeracc());
							}
							if (btBankAccDTO != null) {
								if (StringUtils.isNotEmpty(abEmitBillSupplyVoOld.getCorpId())
										&& abEmitBillSupplyVoOld.getCorpId().equals(btBankAccDTO.getCorpId())) {
									abEmitBillSupplyVo.setPayer(btBankAccDTO.getAccName());
									abEmitBillSupplyVo.setPayerBankName(btBankAccDTO.getBankName());
									abEmitBillSupplyVo.setPayerbankno(btBankAccDTO.getBankCode());
								} else {
									errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERCORP_MATCHERROR);
								}
							} else {
								errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERACC_ISNOTEXIST);
							}
						}
						BeanUtils.copyNotNullProperties(abEmitBillSupplyVo, abEmitBillSupplyVoOld);
					} else {
						// 电票
						/** 业务办理日期校验 */
						if (abEmitBillSupplyVoOld.getStartDate() != null
								&& abEmitBillSupplyVoOld.getEndDate() != null) {
							if (abEmitBillSupplyVo.getBusinessDate() != null && (abEmitBillSupplyVo.getBusinessDate()
									.getTime() < abEmitBillSupplyVoOld.getStartDate().getTime()
									|| abEmitBillSupplyVo.getBusinessDate().getTime() >= abEmitBillSupplyVoOld
									.getEndDate().getTime())) {
								errorMsgList.add(AccebillConstant.BILL_ACCEBILL_ABRECEIVEBILL_CHECKBUSINESSDATE);
							}
						}
						// 票据号
						abEmitBillSupplyVoOld.setOrigNo(abEmitBillSupplyVo.getOrigNo());
						// 业务办理日期
						abEmitBillSupplyVoOld.setBusinessDate(abEmitBillSupplyVo.getBusinessDate());
						// 承兑合同号
						abEmitBillSupplyVoOld.setAcontractNo(abEmitBillSupplyVo.getAcontractNo());
						// 承兑协议编号
						abEmitBillSupplyVoOld.setAccptncagrmtnb(abEmitBillSupplyVo.getAccptncagrmtnb());
						// 开票信用等级
						abEmitBillSupplyVoOld.setPayerCdtRatgs(abEmitBillSupplyVo.getPayerCdtRatgs());
						// 开票评级机构
						abEmitBillSupplyVoOld.setPayerCdtRatgGgcy(abEmitBillSupplyVo.getPayerCdtRatgGgcy());
						// 开票评级到期日
						abEmitBillSupplyVoOld.setPayerCdtRatgDuedt(abEmitBillSupplyVo.getPayerCdtRatgDuedt());
						// 保证人地址
						// abEmitBillSupplyVoOld.setAdr
						// (abEmitBillSupplyVo.getAdr ());
						// 发票号
						abEmitBillSupplyVoOld.setAinvcnb(abEmitBillSupplyVo.getAinvcnb());
						// 保证金比例
						abEmitBillSupplyVoOld.setBailpercent(abEmitBillSupplyVo.getBailpercent());
						// 保证金
						abEmitBillSupplyVoOld.setBailmoney(abEmitBillSupplyVo.getBailmoney());
						// 保证金利率
						abEmitBillSupplyVoOld.setBailrate(abEmitBillSupplyVo.getBailrate());
						// 保证金账户
						abEmitBillSupplyVoOld.setBailAcc(abEmitBillSupplyVo.getBailAcc());
						// 手续费
						abEmitBillSupplyVoOld.setFeemoney(abEmitBillSupplyVo.getFeemoney());
						// 摘要
						abEmitBillSupplyVoOld.setAbs(abEmitBillSupplyVo.getAbs());
						// 备注
						abEmitBillSupplyVoOld.setRmk(abEmitBillSupplyVo.getRmk());
					}
					abEmitBillSupplyVoOld.addErrorMsg(errorMsgList);
					list.set(count, abEmitBillSupplyVoOld);
					count++;
				}
			}
		}
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<AbEmitBillSupplyVo> list, ICurrentUser userVo) {
		if (list != null && !list.isEmpty()) {
			BigDecimal num = new BigDecimal(100);
			for (AbEmitBillSupplyVo abEmitBillSupplyVo : list) {
				if (abEmitBillSupplyVo.getBailpercent() != null) {
					abEmitBillSupplyVo.setBailpercent(
							abEmitBillSupplyVo.getBailpercent().divide(num, 6, BigDecimal.ROUND_HALF_UP));// 保证金比例
				}
				if (abEmitBillSupplyVo.getBailrate() != null) {
					abEmitBillSupplyVo
							.setBailrate(abEmitBillSupplyVo.getBailrate().divide(num, 6, BigDecimal.ROUND_HALF_UP));// 保证金比率
				}
				this.makeupSave(abEmitBillSupplyVo, userVo);
			}
		}
	}
	@Override
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		return this.getAbEmitBillSupplyVoById(aid);
	}
	/**
	 * 开票套打回调实现
	 *
	 * @param dataId
	 * @param vouNo
	 */
	public void printComplete(String dataId, String vouNo, ICurrentUser userVo) {
		if (StringUtils.isEmpty(dataId)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		AbVoucherinfoVice billVice = abVoucherinfoViceService.get(dataId, true);
		if (billVice == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
		}
		AbVoucherinfo billInfo = abVoucherinfoService.get(billVice.getVid(), true);
		if (billInfo == null) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
		}
		billInfo.setOrigNo(vouNo);
		abLogService.saveAbLog(dataId, null, AccebillConstant.BRIEF_STATUS_6101, AccebillConstant.ABLOG_LOGTYPE_PRINT,
				"套打修改凭证号【" + vouNo + "】", userVo);
	}
}
