package com.bytter.biz.wage.service;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bytter.biz.bdlinks.service.impl.BisExcServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.netbank.service.BisBifRuleService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.limit.dto.PaymentLimitBatchDetailDto;
import com.bytter.biz.limit.exception.PaymentLimitBatchExceededException;
import com.bytter.biz.limit.exception.PaymentLimitExceededException;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.biz.payment.api.constant.WageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IErpPayInfoService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.wage.dao.NisWageDao;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.IWorkFlowSubmitVerifyService;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitContextDto;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitResultDto;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BisBifRuleDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;

import cn.hutool.core.lang.UUID;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: NIS_WAGE
 * @date 2018-01-08 17:19:13
 */
@WorkFlow(NisWorkFlowConstant.NIS_WAGE)
@Service("nisWageService")
@DubboService(interfaceClass = IWorkFlowListener.class,group = NisWorkFlowConstant.NIS_WAGE)
@Transactional
public class NisWageService extends BaseService<NisWage, String> implements IWorkFlowListener, IExcelVerifyHandler<NisWageVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService, IWorkFlowSubmitVerifyService {
	@Autowired
	private NisWageDao nisWageDao;
	@Autowired
	private NisWageDetailService nisWageDetailService;
	@Autowired
	private NisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IInputBankInfoService btBankService;
	@Autowired
	private NisMessageService nisMessageService;
	@Autowired
	private EnterpriseLimitControlService enterpriseLimitControlService;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private BisBifRuleService bisBifRuleService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@Autowired
	private NisCorpBusService nisCorpBusService;
	@DubboReference(check = false)
	private IErpPayInfoService erpPayInfoService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IBtBankAccService bankAccService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IFbsItemService fbsItemService;
	@DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IBtSwiftInfoService btSwiftInfoService;
    @DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private BisExcServiceImpl bisExcServiceImpl;

	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisWage entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(NisWage entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisWage entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisWage entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisWage, String> getEntityDao() {
		return nisWageDao;
	}

	public DataTablesOutput<NisWageVO> getDataTablesUnionNew(DataTablesInput dataTablesInput, ICurrentUser user) {
		DataTablesOutput<NisWageVO> outPut = getDataTablesUnion(dataTablesInput, user);
		doOutPutData(outPut);
		return outPut;
	}

	/**
	 * 查询代发工资单
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<NisWageVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource = "";
		String opType = (String) dataTablesInput.getFormdata().get("opType");
		//账号权限
		String accAuthSql = "";
		//代发工资制单
		if ("list".equals(opType)) {
//			if(dataTablesInput.getFormdata().get("eqi_status") == null || StrUtil.isBlank(dataTablesInput.getFormdata().get("eqi_status").toString())){
//				dataTablesInput.getFormdata().put("ini_status", Arrays.asList(new Integer[]{0,-1}));
//			}
			resource = com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_WAGE_NISWAGE_TOUCHQUERY;
			sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null or n.create_by = '')");
			sql.append(" and n.status in (" + com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP + "," + com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK + ")");
		} else if ("listDeal".equals(opType)) {
			//代发工资结果异常处理
			DataTablesOutput out = this.getDataTablesDeal(dataTablesInput, user);
			out = this.setJoinField(out, opType);
			List<NisWageVO> list = out.getData();
			setNum(list);
			return out;
		} else {
			resource = com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_WAGE_NISWAGE_QUERY;
            //权限控制类型  1-单位级 2-账号级
            String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
            if (!CommonConstant.YES.equals(controlType)) {
                //设置账号授权控制
                List<String> accountGrantIdList = accountGrantService.listByMultiValAccIds(user.getId(), com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_WAGE_NISWAGE_QUERY, null,
                        AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE,
                        AccountGrantConstant.QUERY_DETAIL);
                List<String> bankAccGrantList = null;
                List<BtBankAccDTO> accountGrantList = bankAccService.getBankAccNotUnionByIdList(accountGrantIdList);
                if (ListUtils.isNotNullOrEmpty(accountGrantList)) {
                    bankAccGrantList = new ArrayList<>();
                    for (int i = 0; i < accountGrantList.size(); i++) {
                        BtBankAccDTO accDTO = accountGrantList.get(i);
                        bankAccGrantList.add(accDTO.getBankAcc());
                    }
                }
                accAuthSql = SqlUtil.getInSql(bankAccGrantList, "n.payer_Acc", true);
            }
		}
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		if(StringUtils.isNotBlank(accAuthSql)){
			sql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput<NisWageVO> out = this.nisWageDao.getDataTablesUnion(dataTablesInput, sql.toString());
		out = this.setJoinField(out, opType);
		if("query".equals(opType)){
			List<NisWageVO> list = out.getData();
			setNum(list);
		}
		return out;
	}

	/**
	 * 列表数据处理数据处理
	 *
	 * @param outPut
	 */
	public void doOutPutData(DataTablesOutput outPut) {
		List<NisWageVO> resultList = outPut.getData();
		if (EmptyUtils.isEmptyList(resultList)) {
			return;
		}
		List<Integer> statusList = resultList.stream().filter(resultDO -> !EmptyUtils.isEmpty(resultDO.getStatus())).map(NisWageVO::getStatus).distinct().collect(Collectors.toList());
		if (EmptyUtils.isEmptyList(statusList)) {
			return;
		}
		List<String> typeList = new ArrayList<>();
		for (Integer status : statusList) {
			typeList.add(status.toString());
		}
		Map<String, String> dictMap = dictService.getNameByTypeCodesAndTypeGroupCode(typeList, "nisBillhead.status");
		for (NisWageVO result : resultList) {
			if (EmptyUtils.isEmpty(result.getStatus())) {
				continue;
			}
			if (dictMap.containsKey(result.getStatus().toString())) {
				result.setStatusName(dictMap.get(result.getStatus().toString()));
			}
		}
	}

	/**
	 * 设置费用报销的关联字段
	 * @param output
	 * @param opType
	 * @return
	 */
	public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
		if(!EmptyUtils.isEmpty(output.getData())) {
			List<NisWageVO> list = output.getData();
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
			// 落地限额处理结果文案：批量取一次，供前后端分离接口直接展示 landingLimitResultName
			Map<String, DictDTO> landingResultDict = dictService.listMap(EnterpriseLimitControlService.LANDING_RESULT_DICT);
			for (NisWageVO vo:list) {
				// 未涉及落地（值为 null）时字典取不到，名称留空
				vo.setLandingLimitResultName(landingResultDict.getOrDefault(vo.getLandingLimitResult(), DictDTO.EMPTY).getName());
				if (corpMap.containsKey(vo.getCorpId())) {
					vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
				}
				if (curMap.containsKey(vo.getCurId())) {
					vo.setCurName(curMap.get(vo.getCurId()).getCurName());
				}
				BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(vo.getPayerAcc(),vo.getCurId());
				if(!EmptyUtils.isEmpty(bisAccBalDTO)){
					BigDecimal balance = !EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal()) ? bisAccBalDTO.getAvailBal() : bisAccBalDTO.getBal();
					vo.setBalance(balance);
				}else{
					vo.setBalance(BigDecimal.ZERO);
				}
			}
			if("query".equals(opType)){
			}
		}
		return output;
	}
	/**
	 * 查询代发工资结果异常处理
	 *
	 * @param dataTablesInput
	 * @return
	 * @author zuoy 2018-06-20
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesDeal(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		sql.append(" and n.create_by ='").append(user.getLoginName()).append("'");
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_WAGE_NISWAGE_DEALQUERY, user.getId()), "n.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput out = nisWageDao.getDataTablesDeal(dataTablesInput, sql.toString());
		List<NisWageVO> list = out.getData();
		setNum(list);
		return out;
	}
	/**
	 * 设置成功笔数
	 * @param list
	 */
	public void setNum(List<NisWageVO> list){
		for(NisWageVO nisWageVO : list){
			//成功总笔数
			Integer successNum = 0;
			//成功金额
			BigDecimal successWages = BigDecimal.ZERO;
			//失败总笔数
			Integer failNum = 0;
			//失败金额
			BigDecimal failWages = BigDecimal.ZERO;
			//待确认总笔数
			Integer waitNum = 0;
			List<NisWageDetailVO> nisWageDetailVOList = nisWageDetailService.listByWageId(nisWageVO.getId());
			for(NisWageDetailVO vo : nisWageDetailVOList){
				BigDecimal wages = BigDecimal.ZERO;
				if (StringUtils.isNotEmpty(vo.getWagesVerify())) {
					try {
						wages = new BigDecimal(new String(Base64Util.decodeFromString(vo.getWagesVerify()), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if(com.bytter.biz.bdlink.api.constant.BdlinkConstant.SUCCESS.equals(vo.getVoucherStat())){
					successNum ++;
					successWages = successWages.add(wages);
				}
				if(com.bytter.biz.bdlink.api.constant.BdlinkConstant.BANK_FAILED.equals(vo.getVoucherStat()) || com.bytter.biz.bdlink.api.constant.BdlinkConstant.INTERFACE_FAILED.equals(vo.getVoucherStat())){
					failNum ++;
					failWages = failWages.add(wages);
				}
				if(com.bytter.biz.bdlink.api.constant.BdlinkConstant.WAIT_RESULT.equals(vo.getVoucherStat())){
					waitNum ++;
				}
			}
			nisWageVO.setSuccessNum(successNum);
			nisWageVO.setFailNum(failNum);
			nisWageVO.setWaitNum(waitNum);
			nisWageVO.setSuccessWages(successWages);
			nisWageVO.setFailWages(failWages);
		}
	}
	/**
	 * 根据代发工资ID获取代发工资信息
	 *
	 * @param id
	 * @return
	 */
	public NisWageVO getByIdUnion(String id) {
		NisWageVO nisWageVO = this.nisWageDao.getByIdUnion(id);
		if (nisWageVO != null) {
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
			if (corpMap.containsKey(nisWageVO.getCorpId())) {
				nisWageVO.setCorpName(corpMap.get(nisWageVO.getCorpId()).getName());
			}
			if (curMap.containsKey(nisWageVO.getCurId())) {
				nisWageVO.setCurName(curMap.get(nisWageVO.getCurId()).getCurName());
			}
			BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisWageVO.getPayerAcc(), nisWageVO.getCurId());
			if (bal != null) {
				BigDecimal balance = !EmptyUtils.isEmpty(bal.getAvailBal()) ? bal.getAvailBal() : bal.getBal();
				nisWageVO.setBalance(balance);
			}
			if (nisWageVO.getBalance() == null) {
				nisWageVO.setBalance(BigDecimal.ZERO);
			}
			//设置付款账号相关信息
			if (StringUtils.isNotBlank(nisWageVO.getPayerAcc())) {
				BtBankAccDTO payerAcc = bankAccService.getBankAccInfoByBankAcc(nisWageVO.getPayerAcc());
				if (payerAcc != null) {
					nisWageVO.setPayerBank(payerAcc.getBankName());
					nisWageVO.setPayerBankCode(payerAcc.getBankCode());
					nisWageVO.setPayerProv(payerAcc.getProv());
					nisWageVO.setPayerCity(payerAcc.getCity());
					nisWageVO.setBankTypeId(payerAcc.getBankTypeId());
				}
			}
		}
		return nisWageVO;
	}
	/**
	 * 根据代发工资单号获取代发工资单（落地限额处理单回调按业务单号定位单据用）
	 *
	 * @param wageNo 代发工资单号
	 * @return
	 */
	public NisWage getByWageNo(String wageNo) {
		return nisWageDao.getByWageNo(wageNo);
	}
	/**
	 * 功能介绍 : 是否挂资金计划。<br>
	 * <b>日期：</b>2018年3月29日<br>
	 *
	 * @version 1.0
	 * @author liyh
	 */
	public boolean isBudgetSel() {
		//挂资金计划参数
		String budgetSel = configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BUDGETSEL).getValue();
		if (CommonConstant.VALID_SIGN_YES.equals(budgetSel)) {
			//挂资金计划
			return true;
		}
		return false;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(NisWageVO vo) {
		List<String> msgList = new ArrayList<>();
		//判断单位在系统中是否存在
        if(StringUtils.isEmpty(vo.getWageTitle())){
            msgList.add("nis.netbank.message.wagetitle.null");
        }
        if(StringUtils.isEmpty(vo.getPayerAcc())){
            msgList.add("nis.pay.message.payeracc.null");
        }
        if(StringUtils.isEmpty(vo.getCurName())){
            msgList.add("fin.paramconfig.message.curid.notnull");
        }
        BtBankAccDTO bankAcc = null;
        String bankCode = "";
        String city = "";
        CorpDTO corp =null;
        if(StringUtils.isEmpty(vo.getCorpName())){
            msgList.add("cms.rat.registercorpname.null");
        }else {
             corp = this.corpService.getCorpIdByName(vo.getCorpName());
            if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                corp = new CorpDTO();
                msgList.add("nis.netbank.message.corp.null");
            } else {
                vo.setCorpId(corp.getId());
                //判断是否是重复的工资册标题
                if(this.isCheckWageTitle(vo.getCorpId(), vo.getWageTitle(), null)){
                    msgList.add("nis.wage.message.wagetitle.unique");
                }
            }
        }

		//校验币别
        if(StringUtils.isNotEmpty(vo.getCurName())){
            String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
            if (EmptyUtils.isBlank(curId)) {
                msgList.add("nis.netbank.message.cur.null");
            } else {
                if(corp!=null&&StringUtils.isNotEmpty(corp.getUserAccountCode())&&StringUtils.isNotEmpty(vo.getPayerAcc())){
                    bankAcc = nisBillheadService.isExistBankAcc(corp.getUserAccountCode(), vo.getPayerAcc(), curId);
                }
                if (EmptyUtils.isEmpty(bankAcc)) {
                    msgList.add("nis.netbank.message.btbankacc.null");
                } else {
                    vo.setCurId(curId);
                    vo.setPayerAccName(bankAcc.getAccName());
                    vo.setBifCode(bankAcc.getBifCode());
                    bankCode = bankAcc.getBankCode();
                    city = bankAcc.getCity();
                }
            }
        }else {
         msgList.add("cms.precative.curid.not.null");
        }

		//工资总额不能为空且必须大于0
		if (EmptyUtils.isEmpty(vo.getWages())) {
			msgList.add("nis.netbank.message.wages.null");
		} else {
			if (vo.getWages().compareTo(BigDecimal.ZERO) <= 0) {
				msgList.add("nis.netbank.message.wages.mismatch");
			} else {
				vo.setWages(new BigDecimal(NumberUtil.formatNumeric(vo.getWages(), "#0.00")));
			}
		}


		//工资发放日期
		if (vo.getWageDay() == null) {
			msgList.add("nis.netbank.message.wageday.null");
		} else {
			if (vo.getWageDay().compareTo(DateUtils.getDateYMD()) < 0) {
				msgList.add("nis.netbank.message.wageday.mismatch");
			}
		}
		//期望支付日期
		if (vo.getWishPayday() == null) {
			vo.setWishPayday(DateUtils.getDateYMD());
		} else {
			if (vo.getWageDay() != null&&(vo.getWishPayday().compareTo(DateUtils.getDateYMD()) < 0 || vo.getWishPayday().compareTo(vo.getWageDay()) < 0)) {
				msgList.add("nis.wage.message.wishpayday.mismatch");
			}
		}

		//状态默认0-暂存
		vo.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP);
		boolean isBudgetSel = this.isBudgetSel();
		//判断是否挂计划,计划科目校验
		if (isBudgetSel && StringUtils.isNotEmpty(vo.getCorpId())) {
			Boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(vo.getCorpId());
			//判断单位是否上报计划
			if (reportBudget) {
				if (EmptyUtils.isEmpty(vo.getItemCodeImp())) {
					msgList.add("nis.assign.message.itemcode.null");
				} else {
					FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCodeImp());
					if (EmptyUtils.isEmpty(fbsItemDTO)) {
						//判断计划科目是否存在
						msgList.add("nis.assign.message.bustype.itemcode.null");
					} else {
						vo.setItemCode(new String[]{fbsItemDTO.getItemCode()});
						vo.setItemName(new String[]{fbsItemDTO.getItemName()});
						vo.setProcessMoney(new BigDecimal[]{vo.getWages()});
						vo.setIsBudget(isBudgetSel?CommonConstant.YES:CommonConstant.NO);
					}
				}
			}
		}
		//获取接口业务规则
		BisBifRuleDTO bisBifRule = null;
		if (StringUtils.isNotBlank(vo.getBifCode())) {
			bisBifRule = bisBifRuleService.findByBifId(vo.getBifCode());
			if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
			//	throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL);
                msgList.add("接口代码没有维护业务规则，请联系管理员");
			}
		}
		//明细校验
		BigDecimal wagesToatl = BigDecimal.ZERO;
		if (EmptyUtils.isEmptyList(vo.getNisWageDetailList())) {
			msgList.add("nis.netbank.message.dtl.null");
		} else {
		    //证件类型
            Map<String,DictDTO> dictMap = dictService.listMap("idType");
			HashMap<String, String> bankAccMap = new HashMap<>();
			for (NisWageDetailVO dtlVo : vo.getNisWageDetailList()) {
				List<String> cList = new ArrayList<>();
				//金额不能为空且必须大于0
                if(EmptyUtils.isEmpty(dtlVo.getPurpose())){
                    cList.add("nis.wage.message.purpose.null");
                }
                if(EmptyUtils.isEmpty(dtlVo.getBankAcc())){
                    cList.add("nis.netbank.message.bankacc.null");
                }
                if(EmptyUtils.isEmpty(dtlVo.getAccName())){
                    cList.add("nis.netbank.message.accname.null");
                }
				if (dtlVo.getWages() == null) {
					cList.add("nis.wage.message.wages.null");
				} else {
					if (dtlVo.getWages().compareTo(BigDecimal.ZERO) <= 0) {
						cList.add("nis.wage.message.wages.mismatch");
					} else {
						dtlVo.setWages(new BigDecimal(NumberUtil.formatNumeric(dtlVo.getWages(), "#0.00")));
						wagesToatl = wagesToatl.add(dtlVo.getWages());
						try {
							dtlVo.setWagesVerify(Base64Util.encodeToString((dtlVo.getWages().toString()).getBytes("UTF-8")));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
				//校验证件类型
                if(StringUtils.isNotBlank(dtlVo.getIdType())){
                    if(!dictMap.containsKey(dtlVo.getIdType())){
                        cList.add("nis.netbank.message.idType.format");
                    }
                }
				//校验跨行标志
				if (StringUtils.isNotBlank(dtlVo.getDifBank())) {
					if (!"同行".equals(dtlVo.getDifBank())
							&& !"他行".equals(dtlVo.getDifBank())) {
						cList.add("nis.netbank.message.difbank.format");
					}
				}
				if(StringUtils.isNotBlank(dtlVo.getAreaSign())) {
					//校验地域标志
					if (!"同城".equals(dtlVo.getAreaSign())
							&& !"异地".equals(dtlVo.getAreaSign())) {
						cList.add("nis.netbank.message.areasign.format");
					}
				}
				//判断开户行在系统中是否存在
				if (!EmptyUtils.isBlank(dtlVo.getBankCode())) {
					InputBankInfoDTO btBankDTO = this.btBankService.getByBankCodeAndImport(dtlVo.getBankCode());
					if (EmptyUtils.isEmpty(btBankDTO)) {
						//校验是否存在境外开户行
						BtSwiftInfoDTO swiftInfoDTO = btSwiftInfoService.getByCode(dtlVo.getBankCode());
						if(swiftInfoDTO == null){
							cList.add("nis.netbank.message.bank.null");
						} else {
							dtlVo.setBankCode(swiftInfoDTO.getSwiftCode());
							dtlVo.setBankName(swiftInfoDTO.getBankName());
						}
					} else {
						//开户行名称不为空且与开户行代码对应的名称不一致
						if (!EmptyUtils.isBlank(StringUtils.trim(dtlVo.getBankName())) && !StringUtils.trim(dtlVo.getBankName()).equals(btBankDTO.getBankName())) {
							cList.add("nis.netbank.message.bank.mismatch");
						} else {
							dtlVo.setBankCode(btBankDTO.getBankCode());
							dtlVo.setBankName(btBankDTO.getBankName());
							dtlVo.setProv(btBankDTO.getProv());
							dtlVo.setCity(btBankDTO.getCity());
						}
					}
				} else {
					if (EmptyUtils.isBlank(dtlVo.getBankName())) {
						//跨行 or 同行且同行代发必填开户行行号
						if (!"同行".equals(dtlVo.getDifBank())
								|| ("同行".equals(dtlVo.getDifBank())
								&& bisBifRule != null && CommonConstant.YES.equals(bisBifRule.getIsMustBankCode()))) {
							//部分银行同行也需要开户行信息（民生银行）
							cList.add("nis.netbank.message.bankcode.null");
						}
						cList.add("开户行行号与开户行名称不能同时为空！");
					} else {
						InputBankInfoDTO btBankDTO = this.btBankService.getByBankNameAndImport(dtlVo.getBankName());
						if (EmptyUtils.isEmpty(btBankDTO)) {
							cList.add("开户行名称不存在！");
						} else {
							dtlVo.setBankCode(btBankDTO.getBankCode());
							dtlVo.setBankName(btBankDTO.getBankName());
							dtlVo.setProv(btBankDTO.getProv());
							dtlVo.setCity(btBankDTO.getCity());
						}
					}
				}

				//校验“跨行标志”是否如实
				String difBank = dtlVo.getDifBank();
				String dtlBankCode = dtlVo.getBankCode();
				if (StringUtils.isNotBlank(difBank)) {
					if("同行".equals(difBank)){
						if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
							if (!bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
								cList.add("nis.netbank.message.difBank.error");
							}
						}
					}else if("他行".equals(difBank)){
						if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
							if (bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
								cList.add("nis.netbank.message.difBank.error");
							}
						}
					}
				} else {
					// 跨行标识为空时，系统自动判断
					if(EmptyUtils.isNotEmpty(bankCode) && EmptyUtils.isNotEmpty(dtlBankCode)) {
						if (bankCode.substring(0, 3).equals(dtlBankCode.substring(0, 3))) {
							// 同行
							dtlVo.setDifBank("同行");
						} else {
							// 他行
							dtlVo.setDifBank("他行");
						}
					}
				}

				//校验“地域标志”是否如实
				String areaSign = dtlVo.getAreaSign();
				if (StringUtils.isNotBlank(areaSign)) {
					if("同城".equals(areaSign)){
						if(EmptyUtils.isNotEmpty(dtlVo.getCity()) && EmptyUtils.isNotEmpty(city)) {
							if (!dtlVo.getCity().equals(city)) {
								cList.add("地域标志有误");
							}
						}
					}else if("异地".equals(areaSign)){
						if(EmptyUtils.isNotEmpty(dtlVo.getCity()) && EmptyUtils.isNotEmpty(city)) {
							if (dtlVo.getCity().equals(city)) {
								cList.add("地域标志有误");
							}
						}
					}
				} else {
					// 地域标志为空时，系统自动判断
					if (EmptyUtils.isNotEmpty(dtlVo.getCity()) && EmptyUtils.isNotEmpty(city)) {
						if (dtlVo.getCity().equals(city)) {
							dtlVo.setAreaSign("同城");
						} else {
							dtlVo.setAreaSign("异地");
						}
					}
				}

				//收款账号+收款金额+用途 相同则当成重复收款
				String bankAccInfo = dtlVo.getBankAcc()+dtlVo.getWages()+dtlVo.getPurpose();
				if(bankAccMap.containsKey(bankAccInfo)){
					cList.add("nis.wage.message.bankacc.repeat");
				}else{
					bankAccMap.put(bankAccInfo, bankAccInfo);
				}
				if (!EmptyUtils.isEmptyList(cList)) {
					dtlVo.addErrorMsg(cList);
				}
			}
		}
		//工资总额与实发工资总额校验
		if (vo.getWages() != null && vo.getWages().compareTo(wagesToatl) != 0) {
			msgList.add("nis.netbank.message.moneysum.mismatch");
		}
		//增加付款单位、付款账号、发放日期、工资总额”四项不能重复
		if (nisWageDao.isCheckRepeat(vo)) {
			msgList.add("nis.netbank.message.check.repeat");
		}
		//业务种类检验
		boolean flag = false;
		if (EmptyUtils.isNotEmpty(vo.getBusCode()) && EmptyUtils.isNotEmpty(corp.getId())) {
			NisBusType nisBusType = nisBusTypeService.findBusType(vo.getBusCode());
			if(nisBusType != null){
				//校验业务单位是否分配该业务种类
				List<NisCorpBus> list = nisCorpBusService.getByBusCorp(nisBusType.getId());
				if (EmptyUtils.isEmptyList(list)) {
					msgList.add("nis.netbank.message.bustype.mismatch");
				}else{
					for(NisCorpBus cb : list){
						if (!EmptyUtils.isEmpty(corp)) {
							if(corp.getId().equals(cb.getCorpId())){
								flag = true;
								break;
							}
						}
					}
				}
			}
			if(flag){
				//单位存在该业务类别
				vo.setBusType(nisBusType.getId());
			}else{
				msgList.add("nis.netbank.message.bustype.mismatch");
			}
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<NisWageVO> list) {
		// TODO Auto-generated method stub
	}
	/**
	 * 导入
	 *
	 * @param list
	 */
	public String saveImport(List<NisWageVO> list, ICurrentUser user) {
		List<String> billNos = new ArrayList<>();
		//判断是否挂计划,计划科目校验
		if (this.isBudgetSel()) {
			HashMap<String, FbsBudgetProcessDTO> map = new HashMap<>();
			for (NisWageVO nisWageVO : list) {
				//判断单位是否上报计划
				if (CommonConstant.YES.equals(nisWageVO.getIsBudget()) && !EmptyUtils.isEmpty(nisWageVO.getItemCode()) && StringUtils.isNotBlank(nisWageVO.getItemCode()[0])) {
					String key = nisWageVO.getCorpId()+nisWageVO.getItemCode()[0]+nisWageVO.getWishPayday();
					FbsBudgetProcessDTO dto = null;
					if(map.containsKey(key)){
						dto = map.get(key);
						dto.setProcessMoney(dto.getProcessMoney().add(nisWageVO.getWages()));
						map.put(key, dto);
					}else{
						dto = new FbsBudgetProcessDTO();
						dto.setCorpId(nisWageVO.getCorpId());
						dto.setItemCode(nisWageVO.getItemCode()[0]);
						dto.setProcessMoney(nisWageVO.getWages());
						dto.setProcessDate(nisWageVO.getWishPayday());
						map.put(key, dto);
					}
				}
			}
			//资金计划余额校验
			List<FbsBudgetProcessDTO> listBudget = new ArrayList<>();
			for(String key:map.keySet()){
				listBudget.add(map.get(key));
			}
			List<FbsBudgetProcessDTO> listfbp = new ArrayList<FbsBudgetProcessDTO>();
			for (FbsBudgetProcessDTO processDTO : listBudget) {
				FbsBudgetProcessDTO fbptfVo = new FbsBudgetProcessDTO();
				fbptfVo.setCorpId(processDTO.getCorpId());
				fbptfVo.setItemCode(processDTO.getItemCode());
				fbptfVo.setProcessDate(processDTO.getProcessDate());
				listfbp.add(fbptfVo);
			}
			//计划执行科目余额控制方式
			String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
			//优化
			long l1 = System.currentTimeMillis();
			System.out.println("l1="+l1);
			List<FbsBudgetProcessCommonDTO> dtos = fbsBudgetService.getFbsBudgetProcessByItemCodeForList(listfbp);
			long l2 = System.currentTimeMillis();
			System.out.println("l2="+l2);
			for (FbsBudgetProcessDTO processDTO : listBudget) {
				//判断科目是否受控
				if (CommonConstant.YES.equals(processDTO.getIsControl())) {

					for(FbsBudgetProcessCommonDTO dto : dtos) {
						//计划执行规则为5-先执行后控制时无需校验
						if (!BudgetConstant.PROCESS_RULE_5.equals(dto.getProcessRule())) {

							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
								//受本科目控制
								if (processDTO.getProcessMoney().doubleValue() > dto.getItemMoney().doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE);
								}
							}
							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
								//受上级科目控制
								if (processDTO.getProcessMoney().doubleValue() > dto.getSuperItemMoney().doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE);
								}
							}
							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
								//受大科目控制
								if (processDTO.getProcessMoney().doubleValue() > dto.getTopItemMoney().doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE);
								}
							}
						}
					}
				}
			}
			long l3 = System.currentTimeMillis();
			System.out.println("l3="+l3);
		}
		long l4 = System.currentTimeMillis();
		System.out.println("l4="+l4);
		List<NisWageVO> listNisWage = new ArrayList<NisWageVO>();
		for (NisWageVO vo : list) { // ?
			NisWageVO nisWage = new NisWageVO();
			BeanUtils.copyNotNullProperties(vo, nisWage);
			listNisWage.add(nisWage);
		}
		billNos.addAll(this.batchSaveNisWage(listNisWage, user));
		long l5 = System.currentTimeMillis();
		System.out.println("l5="+l5);
		return billNos.stream().collect(Collectors.joining(","));
	}
	/**
	 * 保存
	 *
	 * @param nisWage
	 */
	public String saveNisWage(NisWageVO nisWage, ICurrentUser user) {
		//判断是否是重复的工资册标题
		if(this.isCheckWageTitle(nisWage.getCorpId(), nisWage.getWageTitle(), null)){
			throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGETITLE_UNIQUE);
		}
		NisWage entity = new NisWage();
		BeanUtils.copyNotNullProperties(nisWage, entity);
		entity.setWageNo(SequenceSingleton.getSequence(SequenceEnum.NIS_WAGE));
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		this.save(entity); //保存到 代发工资总表
		nisWage.setId(entity.getId());
		nisWage.setWageNo(entity.getWageNo());
		this.writeBudgetProcess(nisWage, user); //写入执行计划
		//保存代发工资明细
		for (NisWageDetailVO dtlVo : nisWage.getNisWageDetailList()) {
			NisWageDetailVO nisWageDetail = new NisWageDetailVO();
			BeanUtils.copyNotNullProperties(dtlVo, nisWageDetail);
			nisWageDetail.setWageId(entity.getId());
			if ("同行".equals(nisWageDetail.getDifBank())) {
				nisWageDetail.setDifBank("0");
			} else if ("他行".equals(nisWageDetail.getDifBank())) {
				nisWageDetail.setDifBank("1");
			}
			if ("同城".equals(nisWageDetail.getAreaSign())) {
				nisWageDetail.setAreaSign("0");
			} else if ("异地".equals(nisWageDetail.getAreaSign())) {
				nisWageDetail.setAreaSign("1");
			}
			nisWageDetailService.saveNisWageDetail(nisWageDetail, user); //保存到代发工资明细表
		}
		//生成日志
		nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, "", NisMessageConstant.BUS_TYPE_WAGE, user);
		return entity.getWageNo();
	}

	/**
	 * 批量保存
	 *
	 * @param nisWages
	 */
	public List<String> batchSaveNisWage(List<NisWageVO> nisWages, ICurrentUser user) {

		if(CollectionUtils.isEmpty(nisWages)) {
			return new ArrayList<String>();
		}
		List<String> listWageNo = new ArrayList<String>();
		List<NisWageDetailVO> listNisWageDetailVO = new ArrayList<NisWageDetailVO>();
		List<NisWage> nisWageList = new ArrayList<NisWage>();
		List<NisMessage> nisMessageList = new ArrayList<NisMessage>();

		for(NisWageVO nisWage : nisWages) {
		//判断是否是重复的工资册标题
			if(this.isCheckWageTitle(nisWage.getCorpId(), nisWage.getWageTitle(), null)){
				throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGETITLE_UNIQUE);
			}
			NisWage entity = new NisWage();
			BeanUtils.copyNotNullProperties(nisWage, entity);
			entity.setWageNo(SequenceSingleton.getSequence(SequenceEnum.NIS_WAGE));
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
			String id = UUID.randomUUID().toString().substring(0, 19)+new Date().getTime();
			entity.setId(id);
			nisWageList.add(entity);
			nisWage.setId(entity.getId());
			nisWage.setWageNo(entity.getWageNo());
			//保存代发工资明细
			for (NisWageDetailVO dtlVo : nisWage.getNisWageDetailList()) {
				NisWageDetailVO nisWageDetail = new NisWageDetailVO();
				BeanUtils.copyNotNullProperties(dtlVo, nisWageDetail);
				nisWageDetail.setWageId(entity.getId());
				if ("同行".equals(nisWageDetail.getDifBank())) {
					nisWageDetail.setDifBank("0");
				} else if ("他行".equals(nisWageDetail.getDifBank())) {
					nisWageDetail.setDifBank("1");
				}
				if ("同城".equals(nisWageDetail.getAreaSign())) {
					nisWageDetail.setAreaSign("0");
				} else if ("异地".equals(nisWageDetail.getAreaSign())) {
					nisWageDetail.setAreaSign("1");
				}
				listNisWageDetailVO.add(nisWageDetail);
			}
			listWageNo.add(entity.getWageNo());
			//组装日志信息
			NisMessage msg = new NisMessage();
			msg.setBillNo(entity.getId());
			msg.setEnrolDate(new Date());
			msg.setMessageType(NisMessageConstant.MESSAGE_TYPE_TEMP);
			msg.setMessage("");
			msg.setBusType(NisMessageConstant.BUS_TYPE_WAGE);
			msg.setOp(user.getLoginName());
			msg.setOpName(user.getUsername());
			nisMessageList.add(msg);
		}
		this.batchSave(nisWageList,1000);//保存到代发工资表
		if(CollectionUtils.isNotEmpty(nisWageList)) {
			//List<NisWageVO>  listTranseferNisWageVO  = BeanUtils.copyListProperties(nisWages, NisWageVO.class);
			this.writeBatchBudgetProcess(nisWages, user); //写入执行计划 nisWageList
		}
		nisWageDetailService.saveBatchNisWageDetail(listNisWageDetailVO, user); //保存到代发工资明细表
		nisMessageService.saveBatchMessage(nisMessageList);//保存生成日志
		return listWageNo;
	}
	/**
	 * 代发工资删除
	 *
	 * @param id
	 * @param user
	 */
	public void deleteNisWage(String id, ICurrentUser user,String chargeBackType) {
		NisWage nisWage = this.get(id);
		//暂存和编辑状态才可以删除
		if (nisWage != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisWage.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisWage.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisWage.getWageNo());
		}
		//判断是否是ERP过来的数据，如果是则反写退单类型
		if(StringUtils.isNotBlank(nisWage.getErpId()) ){
			erpPayInfoService.updateErpPayInfo(nisWage.getErpId(),chargeBackType);
		}
		//删除单据，控制原状态为0-暂存/-1打回
		Integer[] oldStatus={ com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP, com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK};
		NisWageVO vo = new NisWageVO();
		BeanUtils.copyNotNullProperties(nisWage, vo);
		vo.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_DEL);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		this.updateNisWageStatus(vo, oldStatus);
		//删除资金计划
		fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS, nisWage.getWageNo());
		//生成日志
		nisMessageService.saveMessage(nisWage.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, "", NisMessageConstant.BUS_TYPE_WAGE, user);
		//完成消息提醒
		notificationService.complete(NotificationConstant.NETBANK.WAGE_APPLY, nisWage.getId(), user.getId());
	}
	/**
	 * 代发工资批量删除
	 *
	 * @param ids
	 * @param user
	 */
	public void batchDelNisWage(String ids, ICurrentUser user,String chargeBackType) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			this.deleteNisWage(id, user,chargeBackType);
		}
	}
	/**
	 * 代发工资批量提交
	 *
	 * @param ids
	 * @param user
	 */
	public void batchSubmitNisWage(String ids, ICurrentUser user) {
		String budgetSel = configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BUDGETSEL).getValue();
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		RuntimeException firstFailure = null;
		List<LimitHitDetailDto> limitHitDetails = new ArrayList<>();
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			try {
				// 每张工资单使用独立事务提交和校验限额；当前单失败只回滚自身，并继续处理后续单据。
				transactionTemplate.execute(status -> {
					submitBatchNisWageItem(id, user, budgetSel);
					return null;
				});
			} catch (RuntimeException exception) {
				String wageNo = resolveBatchWageNo(id);
				if (firstFailure == null) {
					firstFailure = exception;
				}
				if (exception instanceof PaymentLimitExceededException) {
					LimitHitDetailDto detail = ((PaymentLimitExceededException) exception).getDetail();
					if (detail != null) {
						limitHitDetails.add(PaymentLimitBatchDetailDto.of(
								"table.nisWage.wageNo", wageNo, detail));
					}
				}
				logger.warn("批量提交代发工资单失败，继续处理后续单据，工资单编号：{}，失败原因：{}",
						wageNo, exception.getMessage());
			}
		}
		if (firstFailure instanceof PaymentLimitExceededException && !limitHitDetails.isEmpty()) {
			throw new PaymentLimitBatchExceededException(limitHitDetails);
		}
		if (firstFailure != null) {
			throw firstFailure;
		}
	}
	/**
	 * 代发工资编辑
	 *
	 * @param nisWage
	 * @param user
	 */
	public void updateNisWage(NisWageVO nisWage, ICurrentUser user) {
		//校验代发工资总额与代发工资明细汇总金额是否一致
		//工资总额与实发工资总额校验
		if (nisWage.getWages() != null && nisWage.getWages().compareTo(nisWageDetailService.getSumWages(nisWage.getId())) != 0) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_SUMWAGES_MISMATCH, nisWage.getWageNo());
		}
		NisWage entity = this.get(nisWage.getId(), true);
		//暂存和编辑状态才可以提交
		if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getWageNo());
		}
		//判断是否是重复的工资册标题
		if(this.isCheckWageTitle(nisWage.getCorpId(), nisWage.getWageTitle(), nisWage.getId())){
			throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGETITLE_EXIST, entity.getWageNo());
		}
		//增加付款单位、付款账号、发放日期、工资总额”四项不能重复
		if (nisWageDao.isCheckRepeat(nisWage)) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SERIALNOERP_REPEAT, entity.getWageNo());
		}
		//BeanUtils.copyNotNullProperties(nisWage, entity);
		if (EmptyUtils.isBlank(nisWage.getCreateBy())) {
			nisWage.setCreateBy(user.getLoginName());
		}
		if (nisWage.getCreateDate() == null) {
			nisWage.setCreateDate(DateUtils.getDate());
		}
		nisWage.setUpdateBy(user.getLoginName());
		nisWage.setUpdateDate(DateUtils.getDate());
		Integer[] oldStatus = { com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP, com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK };
		this.updateNisWageStatus(nisWage, oldStatus);
		//写入计划执行
		this.writeBudgetProcess(nisWage, user);
		BeanUtils.copyNotNullProperties(nisWage, entity);
		//更新代发工资明细表的身份证信息
		//nisWageDetailService.updateIdNo(nisWage.getId());
		//启动审批
		if (com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			// 工资册进入待审批时先按最新工资总额校验并占用限额，覆盖编辑后提交和打回重提。
			occupyEnterpriseLimitForWage(entity, user);
			//生成日志  若未挂审批流程,审批通过的状态与提交审批的
			nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT, "", NisMessageConstant.BUS_TYPE_WAGE, user);
			//调用审批后方法
			 getStartHandler().handler(NisWorkFlowConstant.NIS_WAGE, entity.getId(), entity, entity.getCorpId(), user.getId());
		} else {
			//生成日志
			nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, "", NisMessageConstant.BUS_TYPE_WAGE, user);
		}
		//完成消息提醒
		notificationService.complete(NotificationConstant.NETBANK.WAGE_APPLY, nisWage.getId(), user.getId());
	}
	/**
	 * 写入计划执行
	 *
	 * @param nisWage
	 * @param user
	 */
	private void writeBudgetProcess(NisWageVO nisWage, ICurrentUser user) {
		if (!EmptyUtils.isEmptyArray(nisWage.getItemCode())) {
			//写入之前先删除
			fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS, nisWage.getWageNo());
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			for (int i = 0; i < nisWage.getItemCode().length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(nisWage, user);
				fbsBudgetProcessDTO.setItemCode(nisWage.getItemCode()[i]);
				fbsBudgetProcessDTO.setItemName(nisWage.getItemName()[i]);
				fbsBudgetProcessDTO.setProcessMoney(nisWage.getProcessMoney()[i]);
				if(nisWage.getExchangeRate()!=null) {
					fbsBudgetProcessDTO.setExchangeRate(nisWage.getExchangeRate()[i]);
					fbsBudgetProcessDTO.setProcessCurMoney(nisWage.getProcessCurMoney()[i]);
				}
				list.add(fbsBudgetProcessDTO);
			}
			this.fbsBudgetProcessService.writeBudgetProcess(list, user);
		} else {
			//计划执行状态同步更新（批量提交）
			this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS,
					nisWage.getWageNo(), nisWage.getStatus(), user);
		}
	}

	/**
	 * 批量写入计划执行
	 *
	 * @param nisWageList
	 * @param user
	 */
	private void writeBatchBudgetProcess(List<NisWageVO> nisWageList, ICurrentUser user) {
		List<com.bytter.biz.budget.spi.vo.NisWageVO> list = BeanUtils.copyListProperties(nisWageList, com.bytter.biz.budget.spi.vo.NisWageVO.class);
		fbsBudgetProcessService.writeBatchBudgetProcess(list,user);
	}
	/**
	 * 构建计划执行
	 *
	 * @param nisWage
	 * @param user
	 * @return
	 */
	private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(NisWageVO nisWage, ICurrentUser user) {
		FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
		fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
		fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
		fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
		fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
		fbsBudgetProcessDTO.setStatus(nisWage.getStatus());
		fbsBudgetProcessDTO.setBillNo(nisWage.getWageNo());
		fbsBudgetProcessDTO.setBillId(nisWage.getId());
		fbsBudgetProcessDTO.setProcessDate(nisWage.getWishPayday());
		fbsBudgetProcessDTO.setCorpId(nisWage.getCorpId());
		fbsBudgetProcessDTO.setCurId(nisWage.getCurId());
		fbsBudgetProcessDTO.setPayerAcc(nisWage.getPayerAcc());
		fbsBudgetProcessDTO.setPurpose(nisWage.getAddWord());
		fbsBudgetProcessDTO.setWishPayday(nisWage.getWishPayday());
		fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_WAGE);
		fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
		return fbsBudgetProcessDTO;
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		NisWage nisWage = this.get(workFlowResult.getBusinessKey(), true);
		boolean pass = Boolean.TRUE.equals(workFlowResult.isPass());
		// 落地限额（银行端限额服务）：企业端审批通过后，仅对"审批通过后会生成线上银行支付指令"（线上接口规则在线
		// 且付款账户在线）的代发工资判断是否达到落地限额（见 isLandingLimitReached）；命中则 markApprovePassForLanding
		// 把单据<b>提前置为审批通过（status 11→95）</b>，但"审批通过的内容"（分批生成指令/资金计划置通过）<b>暂不执行</b>
		// ——延后到内管端落地限额审批通过后的回调 completeTranApprove(->approvePassEnd) 才真正执行，落地标识
		// LANDING_LIMIT_RESULT 置 0（待执行）作幂等依据（银行未通过前不动资金计划、不发指令）。
		if (pass
				&& isOnlineInstruction(nisWage)
				&& isLandingLimitReached(nisWage)) {
			// 企业端审批已通过（workflow 已 complete）：此处把单据提前置为 95（内容不执行，见 markApprovePassForLanding），
			// 等内管端审批通过回调后由 approvePassEnd 执行分批生成指令等收尾内容（回调前单据为 95 且 LANDING_LIMIT_RESULT=0）。
			markApprovePassForLanding(nisWage, user);
			nisMessageService.saveMessage(nisWage.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT,
					"已提交银行端落地限额审批，审批通过后自动生效", NisMessageConstant.BUS_TYPE_WAGE, user);
			return;
		}
		this.approvePassEnd(nisWage, pass, user);
	}
	/**
	 * 审批通过后处理
	 *
	 * @param nisWage
	 * @param result
	 * @param user
	 */
	public void approvePassEnd(NisWage nisWage, boolean result, ICurrentUser user) {
		this.approvePassEnd(nisWage, result, user, false);
	}
	/**
	 * 审批通过/打回收尾实现（内部方法，含落地回调"单据已提前置 95"入口）。
	 *
	 * @param alreadyApproved 单据是否已处于审批通过（status=95，落地限额回调入口）：
	 *                        是则终态 CAS 旧状态白名单仅 {95}；否则与 flowCompleted 入口一致（{11}）。
	 *                        内容执行与 {@link #approvePassEnd(NisWage, boolean, ICurrentUser)} 完全相同。
	 */
	private void approvePassEnd(NisWage nisWage, boolean result, ICurrentUser user, boolean alreadyApproved) {
		NisWageVO vo = new NisWageVO();
		BeanUtils.copyNotNullProperties(nisWage, vo);
		logger.info("审批代发工资开始时间："+DateUtils.getDate().getTime());
		if (!result) {
			//更新审批状态
			vo.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK);
		} else {
			logger.info("代发工资审批通过单号："+vo.getWageNo());
			logger.info("状态："+vo.getStatus());
			//更新审批状态
			vo.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK);
			//指令状态默认待发送
			vo.setVoucherStat(com.bytter.biz.bdlink.api.constant.BdlinkConstant.PRE_SEND);
			//判断接口代码是否为空
			if (EmptyUtils.isEmpty(nisWage.getBifCode())) {
				throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, nisWage.getPayerAcc());
			}
			//获取接口业务规则
			BisBifInitDTO bifCode = bisBifInitService.getObjectById(nisWage.getBifCode());
			BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(nisWage.getBifCode());
			if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
				throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, bifCode.getBifCode(), bifCode.getName());
			}
			logger.info("接口代码是否在线！---"+bisBifRule.getIsBankOnline());
			//接口代码在线则发送指令
			if (CommonConstant.YES.equals(bisBifRule.getIsBankOnline())) {
				//获取代发工资明细（过滤已交易成功的明细）
				List<NisWageDetailVO> listDetail = nisWageDetailService.listByWageIdNotFailure(nisWage.getId());
				logger.info("获取代发工资明细，工资Id:【{}】，明细数量:【{}】", nisWage.getId(), (listDetail != null ? listDetail.size() : 0));
				if (!EmptyUtils.isEmptyList(listDetail)) {
					//发送数
					int sendNum = bisBifRule.getSendNum() == null ? 300 : bisBifRule.getSendNum();
					BigDecimal totalMoney = BigDecimal.ZERO;
					StringBuilder sb = new StringBuilder();
					String totalStr = "";
					for (int i = 0; i < listDetail.size(); i++) {
						int b = (1 + i) % sendNum;
						BigDecimal wages = null;
						wages = new BigDecimal(new String(Base64Util.decodeFromString(listDetail.get(i).getWagesVerify()), StandardCharsets.UTF_8));
						totalMoney = totalMoney.add(wages);
						sb.append(listDetail.get(i).getId()).append(CommonConstant.COMMA_SPLIT);
						if (b == 0) {
							sb.append(";");
							totalStr += totalMoney + ";";
							totalMoney = BigDecimal.ZERO;
						}else if (i == listDetail.size() - 1) {
							sb.append(";");
							totalStr += totalMoney + ";";
						}
					}
					logger.info("发送数：" + sendNum + "，发送明细：" + totalStr);
					String[] totalAr = totalStr.split(";");
					String[] wageDetailAttr = sb.toString().split(";");
					for (int j = 0; j < totalAr.length; j++) {
						NisWage entity = new NisWage();
						BeanUtils.copyNotNullProperties(nisWage, entity);
						entity.setWages(new BigDecimal(totalAr[j]));
						logger.info("代发工资审批通过单号："+entity.getWageNo());
						logger.info("生成指令之前状态："+entity.getStatus());
						//代发工资按批次生成指令
						String voucherNo = this.saveBisExc(entity, user);
						logger.info("生成指令结束状态："+entity.getStatus());
						// 更新工资明细的指令单号
						String[] wageDetailIds = wageDetailAttr[j].split(CommonConstant.COMMA_SPLIT);
						for (String wageDetailId : wageDetailIds) {
							//更新明细的交易状态为3-准备发送
							NisWageDetail wageDetail = this.nisWageDetailService.get(wageDetailId);
							NisWageDetailVO detailVO = new NisWageDetailVO();
							BeanUtils.copyNotNullProperties(wageDetail, detailVO);
							detailVO.setVoucherNo(voucherNo);
							detailVO.setVoucherStat(com.bytter.biz.bdlink.api.constant.BdlinkConstant.PRE_SEND);
							detailVO.setUpdateBy(user.getLoginName());
							detailVO.setUpdateDate(DateUtils.getDate());
							logger.info("更新明细开始状态："+detailVO.getVoucherStat());
							this.nisWageDetailService.updateNisWageDetailVoucherStat(detailVO);
							logger.info("更新明细结束状态："+detailVO.getVoucherStat());
						}
					}
				}else{
					logger.error("代发工资审批中断，无明细，不生成指令！");
					throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_DETAIL_FAIL_EXC, nisWage.getWageNo());
				}
			}
		}
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		//审批单据，更新原状态控制为待审批（落地回调入口单据已是 95，白名单仅 {95}）
		Integer[] oldStatus = alreadyApproved
				? new Integer[]{com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK}
				: new Integer[]{com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK};
		logger.info("更新代发工资主单开始状态："+vo.getStatus());
		this.updateNisWageStatus(vo, oldStatus);
		logger.info("更新代发工资主单结束状态："+vo.getStatus());
		if (!result && !alreadyApproved) {
			// 企业端审批退回后工资单回到制单环节，释放本次付款尝试的限额占用。
			this.releaseEnterpriseLimitForWage(nisWage.getId(), user);
		}
		Integer messageType = result ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		//生成日志
		nisMessageService.saveMessage(nisWage.getId(), messageType, "", NisMessageConstant.BUS_TYPE_WAGE, user);
		//计划执行状态同步更新
		this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS,
				nisWage.getWageNo(), vo.getStatus(), user);
		logger.info("审批代发工资结束时间："+DateUtils.getDate().getTime());
	}
	/**
	 * 落地限额处理结果回调收尾（银行端限额服务 -> 企业端）：
	 * 按处理结果完成审批通过（pass=true）/ 打回（pass=false）收尾。
	 * 与 flowCompleted 的区别：不再判断落地限额（处理单已生成，直接按结果收尾）。
	 * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
	 * <p>
	 * 落地预置单据（markApprovePassForLanding 已提前置 95、审批内容未执行）回调时，按 LANDING_LIMIT_RESULT=0 识别：
	 * 先把落地标识条件翻转（仅 result=0 可翻）为 A/R 作为并发互斥闸门——翻转成功者才收尾；失败（0 行）说明
	 * 已被并发回调收尾，抛状态变更异常（单据已 95 且 result 非 0，重试将落入 handler 幂等分支，不会双收尾）。
	 * 翻转成功后单据已是 95，直接以"已提前置 95"入口调 approvePassEnd（终态 CAS 白名单 {95}），不再假复位 95→11。
	 */
	public void completeTranApprove(String businessId, boolean pass, ICurrentUser user) {
		NisWage entity = this.get(businessId, true);
		if (entity == null) {
			throw new ParameterException("代发工资单据不存在: " + businessId);
		}
		if (user == null) {
			user = buildFallbackUser(entity);
		}
		if (isLandingPending(entity)) {
			// 落地标识条件翻转（仅 LANDING_LIMIT_RESULT=0 可翻）作互斥闸门：0 行 = 已被并发回调翻转收尾，中止
			if (nisWageDao.updateLandingLimitResultIfPending(entity.getId(),
					pass ? EnterpriseLimitControlService.LANDING_RESULT_AGREE
					     : EnterpriseLimitControlService.LANDING_RESULT_REJECT) < 1) {
				throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getWageNo());
			}
			// 单据已是 95：以"已提前置 95"入口直接执行收尾内容
			this.approvePassEnd(entity, pass, user, true);
		} else {
			this.approvePassEnd(entity, pass, user, false);
		}
	}
	/**
	 * 是否"线上生成银行支付指令"的代发工资（落地限额仅对该类单据生效，线下不走）。
	 * 口径与 {@link #approvePassEnd(NisWage, boolean, ICurrentUser)} 通过分支发指令条件同源：
	 * 接口业务规则在线（isBankOnline=YES 才进入发指令分支）且付款账户在线
	 * （离线账户 {@link #saveBisExc(NisWage, ICurrentUser)} 不生成指令）。若线上口径后续变化，在此处同步调整。
	 */
	private boolean isOnlineInstruction(NisWage nisWage) {
		if (nisWage == null) {
			return false;
		}
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(nisWage.getBifCode());
		if (bisBifRule == null || !CommonConstant.YES.equals(bisBifRule.getIsBankOnline())) {
			return false;
		}
		BtBankAccDTO payerAcc = btBankAccService.getBankAccByBankAccAndCurId(nisWage.getPayerAcc(), nisWage.getCurId());
		return payerAcc != null && !CommonConstant.NO.equals(payerAcc.getIsOnline());
	}
	/**
	 * 落地校验：代发工资是否达到落地限额。由内管判断是否需要发起落地审批。
	 * context == null（非线上/账户不在线等）= 不落地，返回 false。
	 * 内管判断失败（服务不可用/返回异常）时抛错中止审批收尾，不放行。
	 */
	private boolean isLandingLimitReached(NisWage nisWage) {
		PaymentLimitContextDto ctx = buildEnterpriseLimitContext(nisWage, getCurrentUser());
		if (ctx == null) {
			return false;
		}
		PaymentLimitResultDto<Boolean> result = enterpriseLimitControlService.isLandingApprovalRequired(ctx);
		// 落地预判断失败码统一映射企业端限额消息枚举，避免降级为未知错误。
		EnterpriseLimitControlService.requireSuccess(result);
		if (result.getData() == null) {
			throw new BusinessException("代发工资落地限额判断失败: 内管未返回判断结果");
		}
		return result.getData();
	}
	/**
	 * 落地限额命中：企业端审批通过（workflow 已 complete）即把代发工资单<b>提前置为审批通过（status 11→95）</b>，
	 * 并记更新人/更新时间；但"审批通过的内容"（分批生成指令/资金计划置通过等）<b>暂不执行</b>，延后到内管端落地限额
	 * 审批通过回调 {@link #completeTranApprove}（->approvePassEnd）再真正执行。
	 * <p>
	 * 状态更新守卫与 {@link #approvePassEnd} 同源（同一 VO 装配 + 同一旧状态白名单 {STATUE_WAIT_CHECK}）——仅当单据
	 * 当前仍为 11 待审批才翻转；并发下状态已变则抛状态变更异常（NIS_PAY_MESSAGE_STATUS_CHANGE），实现"按原先的状态"更新。
	 */
	private void markApprovePassForLanding(NisWage nisWage, ICurrentUser user) {
		NisWageVO vo = new NisWageVO();
		BeanUtils.copyNotNullProperties(nisWage, vo);
		// 提前置为审批通过，但内容（指令/资金计划）不执行，待回调 approvePassEnd
		vo.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		// 旧状态白名单与 approvePassEnd flowCompleted 入口一致（仅 11 待审批；落地回调入口为 {95}，见 completeTranApprove）
		Integer[] oldStatus = {com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK};
		this.updateNisWageStatus(vo, oldStatus);
		// 落地标识=待执行（内容未跑，等内管回调）。updateNisWageStatus 的 HQL 不含此列，单独落库。
		nisWageDao.updateLandingLimitResult(nisWage.getId(), EnterpriseLimitControlService.LANDING_RESULT_PENDING);
	}
	/**
	 * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
	 */
	private ICurrentUser buildFallbackUser(NisWage entity) {
		String loginName = StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy();
		UserDTO user = new UserDTO();
		user.setId(loginName);
		user.setLoginName(loginName);
		user.setUsername(loginName);
		return user;
	}
	/**
	 * 组装代发工资落地限额上下文（提交给内管 isLandingApprovalRequired 判定）。
	 * 仅线上付款（付款账户在线、金额>0）返回 ctx；否则返回 null（= 不落地）。
	 */
	private PaymentLimitContextDto buildEnterpriseLimitContext(NisWage entity, ICurrentUser user) {
		if (entity == null || StringUtils.isBlank(entity.getId())
				|| entity.getWages() == null || entity.getWages().compareTo(BigDecimal.ZERO) <= 0) {
			return null;
		}
		// 前置账户在线校验：仅线上付款账户才会产生落地限额校验；离线/账号不存在则视为不落地
		BtBankAccDTO payerAcc = btBankAccService.getBankAccByBankAccAndCurId(entity.getPayerAcc(), entity.getCurId());
		if (payerAcc == null || !CommonConstant.YES.equals(payerAcc.getIsOnline())) {
			return null;
		}
		PaymentLimitContextDto context = new PaymentLimitContextDto();
		context.setTenantId(resolveEnterpriseLimitTenantId(user));
		context.setSourceBizType(EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE);
		context.setSourceBizId(entity.getId());
		context.setBusinessNo(entity.getWageNo());
		context.setUnitId(entity.getCorpId());
		context.setAccountId(payerAcc.getId());
		context.setAccountMask(entity.getPayerAcc());
		// 限额跨服务契约传企业端原币种 ID，管理端按 ID 完成规则匹配和汇率折算。
		context.setCurrencyId(entity.getCurId());
		context.setAmount(entity.getWages());
		context.setBusinessDate(entity.getWageDay() != null ? entity.getWageDay() : entity.getWishPayday());
		context.setBizStatus(String.valueOf(entity.getStatus()));
		context.setOperatorId(user == null ? null : user.getId());
		context.setOperatorName(user == null ? null : user.getUsername());
		return context;
	}

	private boolean isLandingPending(NisWage entity) {
		return entity != null
				&& com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(entity.getStatus())
				&& EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult());
	}

	/**
	 * 优先使用线程/Dubbo租户上下文，兼容后台任务用户对象未携带tenantId的场景。
	 */
	private String resolveEnterpriseLimitTenantId(ICurrentUser user) {
		String tenantId = UserContextHelper.helper().getTenantId();
		return StringUtils.isNotBlank(tenantId) || user == null ? tenantId : user.getTenantId();
	}

	/**
	 * 付款指令生成
	 *
	 * @param nisWage
	 * @param user
	 */
	public String saveBisExc(NisWage nisWage, ICurrentUser user) {
		//获取付款账号
		BtBankAccDTO payerAcc = btBankAccService.getBankAccByBankAccAndCurId(nisWage.getPayerAcc(), nisWage.getCurId());
		//离线账号不生成指令
		if (payerAcc == null || CommonConstant.NO.equals(payerAcc.getIsOnline())) {
			return "";
		}
		//生成付款指令
		BisExcDTO bisExc = new BisExcDTO();
		bisExc.setCbsBillId(nisWage.getId());
		bisExc.setCbsBillNo(nisWage.getWageNo());
		bisExc.setCorpId(nisWage.getCorpId());
		bisExc.setReqDate(DateUtils.getDate());
		//获取系统结算方式 - 代发工资
		FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_48);
		//付款方信息
		bisExc.setPayerAcc(payerAcc.getBankAcc());
		bisExc.setPayerAccName(payerAcc.getAccName());
		bisExc.setPayerBank(payerAcc.getBankName());
		bisExc.setPayerProv(payerAcc.getProv());
		bisExc.setPayerCity(payerAcc.getCity());
		bisExc.setCurId(nisWage.getCurId());
		bisExc.setPayMoney(nisWage.getWages());
		bisExc.setVoucherTypeId(voucherType.getId());
		bisExc.setBifCode(payerAcc.getBifCode());
		bisExc.setWishPayday(nisWage.getWishPayday());
		bisExc.setTransMode(com.bytter.biz.bdlink.api.constant.BdlinkConstant.TRANS_MODE_PRIVATE);
		bisExc.setIsQuick(com.bytter.biz.bdlink.api.constant.NetbankConstant.ISQUICK_0);
		bisExc.setPurpose(nisWage.getAddWord());
		bisExc.setAddWord(nisWage.getAddWord());
		bisExc.setVoucherFrom(com.bytter.biz.bdlink.api.constant.BdlinkConstant.FROM_NIS_WAGE);
		bisExc.setBusOriType(com.bytter.biz.bdlink.api.constant.BdlinkConstant.BUSORITYPE_30);
		bisExc.setCmdType(com.bytter.biz.bdlink.api.constant.BdlinkConstant.CMDTYPE_AUTO);
		bisExc.setVoucherStat(com.bytter.biz.bdlink.api.constant.BdlinkConstant.PRE_SEND);
		// todo:数据转换
		BisExcDTO bisExcDTO = BeanUtils.copyNewInstanceProperties(bisExc, BisExcDTO.class);
		logger.info("代发工资指令单号生成前："+bisExcDTO.getCbsBillNo());
		return this.bisExcServiceImpl.addBisExcForNisWage(bisExcDTO, user);
	}
	/**
	 * 打回代发工资
	 *
	 * @param id
	 * @param user
	 */
	public void backNisWage(String id, ICurrentUser user) {
		// 设置工资为打回状态
		NisWageVO wage = this.getByIdUnion(id);
		//审批通过状态才可以打回
		if (wage != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(wage.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, wage.getWageNo());
		}
		//校验代发工资明细是否存在等待结果的数据，如果存在不允许打回
		List<NisWageDetailVO> listWaitResult = this.nisWageDetailService.listByWageIdWaitResult(id);
		if (EmptyUtils.isNotEmpty(listWaitResult)) {
			throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGEDETAIL_WAITRESULT, wage.getWageNo());
		}
		wage.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK);
		wage.setUpdateBy(user.getLoginName());
		wage.setUpdateDate(DateUtils.getDate());
		Integer[] oldStatus = { com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK };
		this.updateNisWageStatus(wage, oldStatus);
		// 设置指令交易状态 指令无效
		List<BisExcDTO> bisExcDTOList = this.bisExcService.getBisExcByCbsBillNoForNisWage(wage.getWageNo(), com.bytter.biz.bdlink.api.constant.BdlinkConstant.FROM_NIS_WAGE);
		for(BisExcDTO bisExcDTO : bisExcDTOList){
			//存在指令状态不为成功，不为失败，也不为指令无效的情况，不允许打回，避免指令已做重发
			if(!com.bytter.biz.bdlink.api.constant.BdlinkConstant.SUCCESS.equals(bisExcDTO.getVoucherStat())
					&& !com.bytter.biz.bdlink.api.constant.BdlinkConstant.BANK_FAILED.equals(bisExcDTO.getVoucherStat())
					&& !com.bytter.biz.bdlink.api.constant.BdlinkConstant.INTERFACE_FAILED.equals(bisExcDTO.getVoucherStat())
					&& !com.bytter.biz.bdlink.api.constant.BdlinkConstant.INVALIDATE.equals(bisExcDTO.getVoucherStat())){
				throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_WAGEDETAIL_WAITHANDLE, wage.getWageNo());
			}
		    //交易成功的指令无需修改状态
		    if(!com.bytter.biz.bdlink.api.constant.BdlinkConstant.SUCCESS.equals(bisExcDTO.getVoucherStat())) {
                bisExcDTO.setVoucherStat(com.bytter.biz.bdlink.api.constant.BdlinkConstant.INVALIDATE);
                this.bisExcService.updateBisExc(bisExcDTO, user);
            }
		}
		// 工资单业务打回后结算限额：全失败释放，部分成功仅保留成功金额，失败部分随差额释放。
		this.settleEnterpriseLimitAfterBack(id, user);
		//生成日志
		nisMessageService.saveMessage(wage.getId(), NisMessageConstant.MESSAGE_TYPE_DEAL_BACK, "", NisMessageConstant.BUS_TYPE_WAGE, user);
	}
	/**
	 * 获取审批列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(NisWorkFlowConstant.NIS_WAGE, dataTablesInput);
		List<String> businessIdList = workFlowBusiness.getBusinessIdList();
		String businessSql = "";
		if(!EmptyUtils.isEmpty(businessIdList)){
			businessSql = SqlUtil.getInSql(businessIdList, "n.id", true);
		}
		DataTablesOutput dp = this.nisWageDao.dataTableApprove(dataTablesInput, businessSql);
		dp = this.setJoinField(dp, "query");
		// T24090014838 付款办理优化：付款办理，付款查询，代发工资审批页面，鼠标光标移动到付款账户列时，显示付款账户可用余额
		this.getAccountAvailBal(dp.getData());
		return workFlowBusiness.merge(dp);
	}

	/**
	 * 设置账户可用余额
	 *
	 * @param resultList 返回列表集合
	 */
	public void getAccountAvailBal(List<NisWageVO> resultList) {
		resultList.forEach(nisWage -> {
			BigDecimal availBal = BigDecimal.ZERO;
			BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisWage.getPayerAcc(), nisWage.getCurId());
			if (bal != null) {
				availBal = bal.getAvailBal();
			}
			nisWage.setAvailBal(availBal);
		});
	}

	/**
	 * 获取费用报销信息
	 *
	 * @param id
	 * @return
	 */
	public NisWageVO getByUnion(String id) {
		NisWageVO nisWageVO = this.nisWageDao.getByIdUnion(id);
		Map<String, CorpDTO> corpMap = corpService.getCorpMap();
		Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
		if (corpMap.containsKey(nisWageVO.getCorpId())) {
			nisWageVO.setCorpName(corpMap.get(nisWageVO.getCorpId()).getName());
		}
		if (curMap.containsKey(nisWageVO.getCurId())) {
			nisWageVO.setCurName(curMap.get(nisWageVO.getCurId()).getCurName());
		}
		//设置付款账号相关信息
		if (!StringUtil.isBlank(nisWageVO.getPayerAcc())) {
			BtBankAccDTO payerAcc = bankAccService.getBankAccInfoByBankAcc(nisWageVO.getPayerAcc());
			if (payerAcc != null) {
				nisWageVO.setPayerBank(payerAcc.getBankName());
				nisWageVO.setPayerBankCode(payerAcc.getBankCode());
				nisWageVO.setPayerProv(payerAcc.getProv());
				nisWageVO.setPayerCity(payerAcc.getCity());
				nisWageVO.setBankTypeId(payerAcc.getBankTypeId());
			}
		}
		return nisWageVO;
	}
	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		NisWage nisWage = this.get(workFlowResult.getBusinessKey(), true);
		nisWage.setStatus(AllocationConstant.STATUE_TEMP);
		nisWage.setUpdateBy(user.getLoginName());
		nisWage.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(nisWage);
		// 企业端撤回已使工资单恢复暂存，释放提交审批时产生的限额占用。
		this.releaseEnterpriseLimitForWage(nisWage.getId(), user);
		//计划执行状态同步更新
		this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS, nisWage.getWageNo(), nisWage.getStatus(), user);
		//生成日志
		nisMessageService.saveMessage(nisWage.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_WAGE, user);
	}
	/**
	 * 判断是否是重复的工资册标题
	 * @param corpId
	 * @param wageTitle
	 * @return
	 */
	public boolean isCheckWageTitle(String corpId, String wageTitle, String id){
		return nisWageDao.isCheckWageTitle(corpId, wageTitle, id);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的收款单据，不允许禁用！
			int num = this.nisWageDao.getWageCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_WAGE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_WAGE_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 带原状态更新代发工资单
	 * @param vo 代发工资对象
	 * @param oldStatus 原状态控制
	 * @return
	 */
	public void updateNisWageStatus(NisWageVO vo, Integer[] oldStatus) {
		int result = this.nisWageDao.updateNisWageStatus(vo, oldStatus);
		if(result<1){
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getWageNo());
		}
	}

    /**
	 * 工作流审批校验
	 *
	 * @param params       Map<流程分类KEY, 业务ID列表>
	 * @param workflowPass 审批操作标识：1，同意；3，打回上一级；4，打回制单处。
	 * @param isNeedVerify 是否要校验工作流审批：true表示要校验；false表示不用校验。用户在看到校验失败提示信息后，仍然确定提交审批，则isNeedVerify为false。
	 * @throws WorkFlowVerifyException
	 */
	@Override
	public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {
		if (isNeedVerify) {            // 模拟校验审批
			if ("1".equals(workflowPass)) {
				//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
				String bankaccOver = configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BANKACCOVER_APPROVE).getValue();
				if("0".equals(bankaccOver)){
					return;
				}
				List<String> ids = params.get(NisWorkFlowConstant.NIS_WAGE);
				//账户余额（批量提交校验账户余额)
				HashMap<String, BigDecimal> balMap = new HashMap<>();
				for(String id:ids){
					NisWageVO nisWage = this.getByIdUnion(id);
					//校验账户余额
					BigDecimal curBal = nisWage.getBalance();
					if(balMap.containsKey(nisWage.getPayerAcc())){
						curBal = balMap.get(nisWage.getPayerAcc());
					}
					if(nisWage.getWages().compareTo(curBal)>0){
						if("1".equals(bankaccOver)){
							//1-只提醒不控制
							throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_CONFIRM_BALANCE, WorkFlowConstant.VERITY_STATUS_CONFIRM);
						}else if("2".equals(bankaccOver)){
							//2-控制
							throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_BALANCE, WorkFlowConstant.VERITY_STATUS_ALERT);
						}
					}
					curBal = curBal.subtract(nisWage.getWages());
					balMap.put(nisWage.getPayerAcc(), curBal);
				}
			}
		}
	}
	/**
	 * 根据代发工资ID获取代发工资信息
	 *
	 * @param idList
	 * @return
	 */
	public List<NisWageVO> listByIds(List<String> idList, boolean searchDetail) {
		List<NisWageVO> listWageList = this.nisWageDao.listByIds(idList);
		if (searchDetail) {
			for (NisWageVO vo : listWageList) {
				vo.setNisWageDetailList(this.nisWageDetailService.listByWageId(vo.getId()));
			}
		}
		return listWageList;
	}
	/**
	 * 写入标题头
	 *
	 * @param sheet
	 * @param head
	 * @throws Exception
	 */
	public void setHeadCell(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle) throws Exception {
		HSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getCenterCellStyle(workbook, true));
			cell.setCellValue(head[i]);
		}
	}
	/**
	 * 获取居中样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean ed) {
		return initStyle(workbook, HorizontalAlignment.CENTER, ed);
	}
	/**
	 * 获取靠右样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook, Boolean ed) {
		return initStyle(workbook, HorizontalAlignment.RIGHT, ed);
	}
	/**
	 * 获取靠左样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getLeftCellStyle(HSSFWorkbook workbook, Boolean ed) {
		return initStyle(workbook, HorizontalAlignment.LEFT, ed);
	}
	/**
	 * 获取一个初始的样式
	 */
	public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(align);
		style.setLocked(EmptyUtils.isEmpty(locked) || locked ? true : locked);
		return style;
	}
	/**
	 * 导出所选明细
	 *
	 * @param request
	 * @param response
	 */
	public void exportDtl(HttpServletRequest request, HttpServletResponse response, String ids, String opType, ICurrentUser user) {
		String fileName = "代发工资查询";
		if ("list".equals(opType)) {
			fileName = "代发工资制单";
		} else if ("listDeal".equals(opType)) {
			fileName = "代发工资结果异常处理";
		}
		String codedFileName = fileName;
		try {
			if (ServletUtils.isIE(request)) {
				codedFileName = URLEncoder.encode(codedFileName, "UTF8");
			} else {
				codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
		response.setHeader("Pragma", "No-cache");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			//获取选择的代发工资数据
			List<String> idList = new ArrayList<>();
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
				idList.add(id);
			}
			List<NisWageVO> listWageVo = this.listByIds(idList, true);
			//是否显示明细金额
			String wagesVerify = configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_WAGESVERIFY).getValue();
			//制单人显示
			String creator = configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_CREATOR).getValue();
			int corpRow = 0;
			HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle leftCellStyle = getLeftCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = getRightCellStyle(workbook, false);
			for(NisWageVO wage:listWageVo) {
				HSSFRow rowCorp = sheet.createRow(corpRow);
				//工资标题
				HSSFCell corpCell0 = rowCorp.createCell(0);
				corpCell0.setCellStyle(rightCellStyle);
				corpCell0.setCellValue("工资标题：");
				HSSFCell corpCell1 = rowCorp.createCell(1);
				corpCell1.setCellStyle(leftCellStyle);
				corpCell1.setCellValue(wage.getWageTitle());
				//工资单号
				HSSFCell corpCell2 = rowCorp.createCell(2);
				corpCell2.setCellStyle(rightCellStyle);
				corpCell2.setCellValue("工资单号：");
				HSSFCell corpCell3 = rowCorp.createCell(3);
				corpCell3.setCellStyle(leftCellStyle);
				corpCell3.setCellValue(wage.getWageNo());
				// 单位
				HSSFCell corpCell4 = rowCorp.createCell(4);
				corpCell4.setCellStyle(rightCellStyle);
				corpCell4.setCellValue("单位名称：");
				HSSFCell corpCell5 = rowCorp.createCell(5);
				corpCell5.setCellStyle(leftCellStyle);
				corpCell5.setCellValue(wage.getCorpName());
				// 组装标题头
				String[] head = new String[]{"编号", "工号", "银行账号", "账户名", "开户行行号", "开户行名称", "跨行标识", "地域标识", "工资用途", "交易状态", "银行返回时间", "交易前返回信息", "交易后返回信息"};
				if(CommonConstant.YES.equals(wagesVerify)
						|| (CommonConstant.YES.equals(creator) && user.getLoginName().equals(wage.getCreateBy()))) {
					head = new String[]{"编号", "工号", "银行账号", "账户名", "开户行行号", "开户行名称", "实发工资", "跨行标识", "地域标识", "工资用途", "交易状态", "银行返回时间", "交易前返回信息", "交易后返回信息"};
				}
				this.setHeadCell(workbook, sheet, head, corpRow + 1);
				int index = corpRow + 2;
				// 组装数据
				//跨行标志
				Map<String, DictDTO> difBankListMap = dictService.listMap("nisBillhead.difBank");
				//地域标志
				Map<String, DictDTO> areaSignListMap = dictService.listMap("nisBillhead.areaSign");
				//指令状态
				Map<String, DictDTO> voucherStatListMap = dictService.listMap("bisExc.voucherStat");
				for(NisWageDetailVO dtl:wage.getNisWageDetailList()) {
					int cellNo = 0;
					HSSFRow row = sheet.createRow(index);
					// 编号
					HSSFCell cell0 = row.createCell(cellNo++);
					cell0.setCellStyle(centerCellStyle);
					cell0.setCellValue(dtl.getWageDtlNo());
					// 工号
					HSSFCell cell1 = row.createCell(cellNo++);
					cell1.setCellStyle(centerCellStyle);
					cell1.setCellValue(dtl.getWorkNo());
					// 银行账号
					HSSFCell cell2 = row.createCell(cellNo++);
					cell2.setCellStyle(leftCellStyle);
					cell2.setCellValue(dtl.getBankAcc());
					// 账户名
					HSSFCell cell3 = row.createCell(cellNo++);
					cell3.setCellStyle(leftCellStyle);
					cell3.setCellValue(dtl.getAccName());
					// 开户行行号
					HSSFCell cell4 = row.createCell(cellNo++);
					cell4.setCellStyle(leftCellStyle);
					cell4.setCellValue(dtl.getBankCode());
					// 开户行名称
					HSSFCell cell5 = row.createCell(cellNo++);
					cell5.setCellStyle(leftCellStyle);
					cell5.setCellValue(dtl.getBankName());
					// 实发工资控制三种情况导出可以展示明细金额：
					// 1.参数是否显示明细金额设置为‘是’；
					// 2.参数制单人显示设置为'是'，且代发工资制单人为当前操作用户；
					// 3.参数制单人显示设置为'是'，且代发工资制单人为空，且当前导出为代发工资制单菜单（兼容ERP单据）；
					if(CommonConstant.YES.equals(wagesVerify)
							|| (CommonConstant.YES.equals(creator) && user.getLoginName().equals(wage.getCreateBy()))
							|| (CommonConstant.YES.equals(creator) && "list".equals(opType) && StringUtils.isBlank(wage.getCreateBy()))
					) {
						HSSFCell cell6 = row.createCell(cellNo++);
						cell6.setCellStyle(rightCellStyle);
						cell6.setCellValue(NumberUtil.formatNumeric(new BigDecimal(new String(Base64Util.decodeFromString(dtl.getWagesVerify()), "UTF-8"))));
					}
					// 跨行标识
					String difBankName = "";
					if(!EmptyUtils.isEmpty(difBankListMap.get(dtl.getDifBank()))){
						difBankName = difBankListMap.get(dtl.getDifBank()).getName();
					}
					HSSFCell cell7 = row.createCell(cellNo++);
					cell7.setCellStyle(centerCellStyle);
					cell7.setCellValue(difBankName);
					// 地域标识
					String areaSignName = "";
					if(!EmptyUtils.isEmpty(areaSignListMap.get(dtl.getAreaSign()))){
						areaSignName = areaSignListMap.get(dtl.getAreaSign()).getName();
					}
					HSSFCell cell8 = row.createCell(cellNo++);
					cell8.setCellStyle(centerCellStyle);
					cell8.setCellValue(areaSignName);
					// 工资用途
					HSSFCell cell9 = row.createCell(cellNo++);
					cell9.setCellStyle(leftCellStyle);
					cell9.setCellValue(dtl.getPurpose());
					// 交易状态
					String voucherStatName = "";
					if(!EmptyUtils.isEmpty(voucherStatListMap.get(dtl.getVoucherStat()))){
						voucherStatName = voucherStatListMap.get(dtl.getVoucherStat()).getName();
					}
					HSSFCell cell10 = row.createCell(cellNo++);
					cell10.setCellStyle(leftCellStyle);
					cell10.setCellValue(voucherStatName);
					// 银行返回时间
					HSSFCell cell11 = row.createCell(cellNo++);
					cell11.setCellStyle(leftCellStyle);
					cell11.setCellValue(DateUtils.format(dtl.getReturnTime(), DateUtils.PATTERN_CLASSICAL));
					// 交易前返回信息
					HSSFCell cell12 = row.createCell(cellNo++);
					cell12.setCellStyle(leftCellStyle);
					cell12.setCellValue(dtl.getFirstReturnMsg());
					// 交易后返回信息
					HSSFCell cell13 = row.createCell(cellNo++);
					cell13.setCellStyle(leftCellStyle);
					cell13.setCellValue(dtl.getNextReturnMsg());
					index++;
				}
				corpRow = corpRow + wage.getNisWageDetailList().size() + 3;
			}
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 获取ERP待处理单据
	 * @return
	 */
	public List<NisWageVO> listErpTodoBill() {
		return nisWageDao.listErpTodoBill();
	}

	public int updateFromBisExcChange(BisExcChangeMsg bisExcChangeMsg) {
		return nisWageDao.updateVoucherState(bisExcChangeMsg.getCbsBillId(), bisExcChangeMsg.getVoucherStat());
	}

	public void occupyEnterpriseLimitForWage(NisWage entity, ICurrentUser user) {
		PaymentLimitContextDto context = buildEnterpriseLimitContext(entity, user);
		if (context != null) {
			// 在线付款账户且工资金额等信息有效时，以工资主单为来源交由内管校验并占用。
			EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.checkAndOccupy(context));
		}
	}

	public void releaseEnterpriseLimitForWage(String wageId, ICurrentUser user) {
		if (StringUtils.isBlank(wageId)) {
			return;
		}
		// 按工资主单ID释放限额，供删除、打回等结束当前付款尝试的业务入口调用。
		EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
				EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE, wageId, user == null ? null : user.getId()));
	}

	public void markEnterpriseLimitSuccess(String wageId, ICurrentUser user) {
		if (StringUtils.isBlank(wageId)) {
			return;
		}
		List<NisWageDetailVO> details = nisWageDetailService.listByWageId(wageId);
		if (EmptyUtils.isEmptyList(details)) {
			return;
		}
		for (NisWageDetailVO detail : details) {
			if (!com.bytter.biz.bdlink.api.constant.BdlinkConstant.SUCCESS.equals(detail.getVoucherStat())) {
				return;
			}
		}
		NisWage entity = this.get(wageId, true);
		if (buildEnterpriseLimitContext(entity, user) == null) {
			return;
		}
		// 所有明细均已交易成功后，将工资主单在途占用转为成功用量，保留期间统计。
		EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.markSuccess(resolveEnterpriseLimitTenantId(user),
				EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE, wageId));
	}

	/**
	 * 代发工资业务打回后结算支付限额。
	 *
	 * <p>全失败时释放整单占用；部分成功时按成功明细金额缩减原占用并转为成功用量。
	 * 仍有非终态明细时不调整限额，避免银行结果尚未确定就提前释放。</p>
	 *
	 * @param wageId 工资主单ID
	 * @param user 当前操作人
	 */
	public void settleEnterpriseLimitAfterBack(String wageId, ICurrentUser user) {
		if (StringUtils.isBlank(wageId)) {
			return;
		}
		NisWage entity = this.get(wageId, true);
		List<NisWageDetailVO> details = nisWageDetailService.listByWageId(wageId);
		if (entity == null || EmptyUtils.isEmptyList(details)) {
			return;
		}
		BigDecimal successAmount = BigDecimal.ZERO;
		for (NisWageDetailVO detail : details) {
			String voucherStat = detail.getVoucherStat();
			if (com.bytter.biz.bdlink.api.constant.BdlinkConstant.SUCCESS.equals(voucherStat)) {
				String amountText = new String(Base64Util.decodeFromString(detail.getWagesVerify()), StandardCharsets.UTF_8);
				successAmount = successAmount.add(new BigDecimal(amountText));
				continue;
			}
			boolean failure = com.bytter.biz.bdlink.api.constant.BdlinkConstant.BANK_FAILED.equals(voucherStat)
					|| com.bytter.biz.bdlink.api.constant.BdlinkConstant.INTERFACE_FAILED.equals(voucherStat)
					|| com.bytter.biz.bdlink.api.constant.BdlinkConstant.INVALIDATE.equals(voucherStat);
			if (!failure) {
				return;
			}
		}
		PaymentLimitContextDto context = buildEnterpriseLimitContext(entity, user);
		if (context == null) {
			return;
		}
		if (successAmount.compareTo(BigDecimal.ZERO) == 0) {
			// 所有工资明细均失败后释放整张工资单的在途占用。
			EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
					EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE, wageId, user == null ? null : user.getId()));
			return;
		}
		if (successAmount.compareTo(entity.getWages()) > 0) {
			throw new BusinessException("代发工资成功明细金额不能大于工资总额");
		}
		if (successAmount.compareTo(entity.getWages()) == 0) {
			return;
		}
		context.setAmount(successAmount);
		// 部分成功时按成功明细金额缩减占用并转成功用量，失败金额随差额释放。
		EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.retainSuccessfulAmount(context));
	}

	public List<NisWage> getWageTileList(){
		return nisWageDao.getWageTileList();
	}

	/**
	 * 在当前独立事务中提交一张代发工资单，供批量提交逐单隔离成功与失败结果。
	 *
	 * @param id 工资单ID
	 * @param user 当前操作人
	 * @param budgetSel 资金计划挂接开关
	 */
	private void submitBatchNisWageItem(String id, ICurrentUser user, String budgetSel) {
		NisWageVO nisWage = this.getByIdUnion(id);
		//暂存和编辑状态才可以提交
		if (nisWage != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisWage.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisWage.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisWage.getWageNo());
		}
		// 挂接资金计划
		Boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisWage.getCorpId());
		if (CommonConstant.YES.equalsIgnoreCase(budgetSel)
				&& CommonConstant.YES.equals(reportBudget)) {
			List list = this.fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS, nisWage.getWageNo());
			if (EmptyUtils.isEmptyList(list)) {
				throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BUDGETSEL);
			}
		}
		nisWage.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK);
		this.updateNisWage(nisWage, user);
		//完成消息提醒
		notificationService.complete(NotificationConstant.NETBANK.WAGE_APPLY, nisWage.getId(), user.getId());
	}

	/**
	 * 查询批量提交失败工资单的业务编号；查询异常时返回未知，避免失败提示影响后续单据处理。
	 *
	 * @param id 工资单ID
	 * @return 工资单编号
	 */
	private String resolveBatchWageNo(String id) {
		try {
			NisWageVO wage = this.getByIdUnion(id);
			return wage == null || StringUtils.isBlank(wage.getWageNo()) ? "未知" : wage.getWageNo();
		} catch (RuntimeException exception) {
			logger.warn("批量提交失败后查询工资单编号异常", exception);
			return "未知";
		}
	}
}
