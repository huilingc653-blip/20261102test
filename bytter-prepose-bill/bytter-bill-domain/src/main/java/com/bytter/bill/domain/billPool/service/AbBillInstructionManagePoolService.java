package com.bytter.bill.domain.billPool.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.dao.AbBillInstructionManagePoolDao;
import com.bytter.bill.domain.billPool.vo.AbBillInstructionManagePoolVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.bill.domain.billbank.pool.service.*;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.Base64Util;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @Title: Service
 * @Description: 票据池指令处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Service("abBillInstructionManagePoolService")
@Transactional
public class AbBillInstructionManagePoolService extends BaseService<AbBillInstructionManagePoolVO, String> {
	@Autowired
	private AbBillInstructionManagePoolDao abBillInstructionManagePool;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private IBillPoolBailService billPoolBailService;
	@Autowired
	private IBillPoolLimitApplyService billPoolLimitApplyService;
	@Autowired
	private IBillPoolLimitAllotService billPoolLimitAllotService;
	@Autowired
	private IBillPoolEmitFinaApplyService billPoolEmitFinaApplyService;
	@Autowired
	private BillPoolLimitImpService billPoolLimitImpService;
	@Autowired
	private AbLogService abLogService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	@Override
	public BaseDao<AbBillInstructionManagePoolVO, String> getEntityDao() {
		return abBillInstructionManagePool;
	}
	/**
	 * 指令批量成功操作
	 * 
	 * @param instructionVo
	 */
	public void batchSucce(AbBillInstructionManagePoolVO instructionVo, ICurrentUser userVo) {
		String[] ids = StringUtils.split(instructionVo.getIds(), ",");
		String[] types = StringUtils.split(instructionVo.getType(), ",");
		String[] limitTypes = StringUtils.split(instructionVo.getLimitType(), ",");
		if (StringUtils.isEmpty(ids)) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		for(int i=0;i<ids.length;i++){
			String id=ids[i];
			//保证金指令处理
			if(AbPoolConstant.EXC_BUSTYPE_BAIL.equals(instructionVo.getBusType())){
				String type=types[i];
				PoolBailVO paramsVo=new PoolBailVO();
				paramsVo.setBusId(id);
				paramsVo.setOptStatus(instructionVo.getOpType());
				if(AbPoolConstant.BAIL_TYPE_IN.equals(type)){
					billPoolBailService.updatePoolBailInApply(paramsVo);
				}else if(AbPoolConstant.BAIL_TYPE_OUT.equals(type)){
					billPoolBailService.updatePoolBailOut(paramsVo);
				}
			}else if(AbPoolConstant.EXC_BUSTYPE_LIMIT.equals(instructionVo.getBusType())){
				String limitType=limitTypes[i];
				//额度管理操作
				PoolLimitVO paramsVo=new PoolLimitVO();
				paramsVo.setBusId(id);
				paramsVo.setOptStatus(instructionVo.getOpType());
				if(AbPoolConstant.EXC_LIMITTYPE_APPLY.equals(limitType)){
					billPoolLimitApplyService.updatePoolLimitApply(paramsVo);
					if(AbPoolConstant.AB_POOL_OPT_STATUS18.equals(paramsVo.getOptStatus())){
//						进行内部复核数据写入
						List<PoolLimitVO> addParamsVo=new ArrayList<>();
						PoolLimitVO addInfo=billPoolLimitImpService.queryLimitById(AbPoolConstant.EXC_LIMITTYPE_APPLY, id);
						addInfo.setBankOrgCode(addInfo.getApplyBankOrgCode());
						addInfo.setBankOrgName(addInfo.getApplyCorpName());
						addParamsVo.add(addInfo);
						billPoolLimitApplyService.savePoolLimitApplyCheck(addParamsVo);
					}
				}else if(AbPoolConstant.EXC_LIMITTYPE_APPLYCHECK.equals(limitType)){
					billPoolLimitApplyService.updatePoolLimitApplyCheck(paramsVo);
				}else if(AbPoolConstant.EXC_LIMITTYPE_ALLOT.equals(limitType)){
					billPoolLimitAllotService.updatePoolLimitAllot(paramsVo);
					if(AbPoolConstant.AB_POOL_OPT_STATUS18.equals(paramsVo.getOptStatus())){
	//					进行内部复核数据写入
						List<PoolLimitVO> addParamsVo=new ArrayList<>();
						PoolLimitVO addInfo=billPoolLimitImpService.queryLimitById(AbPoolConstant.EXC_LIMITTYPE_ALLOT, id);
						addInfo.setBankOrgCode(addInfo.getApplyBankOrgCode());
						addInfo.setBankOrgName(addInfo.getApplyCorpName());
						addParamsVo.add(addInfo);
						billPoolLimitAllotService.savePoolLimitAllotCheck(addParamsVo);
					}
				}else if(AbPoolConstant.EXC_LIMITTYPE_ALLOTCHECK.equals(limitType)){
					billPoolLimitAllotService.updatePoolLimitAllotCheck(paramsVo);
				}
			}else if(AbPoolConstant.EXC_BUSTYPE_CON.equals(instructionVo.getBusType())){
				//合同指令操作
				String type=types[i];
				PoolEmitFinaConVO paramsVo=new PoolEmitFinaConVO();
				paramsVo.setBusId(id);
				paramsVo.setOptStatus(instructionVo.getOpType());
				if(AbPoolConstant.CON_STATUS_1.equals(type)){
					billPoolEmitFinaApplyService.updateSignOkPoolEmitFinaCon(paramsVo);
				}else if(AbPoolConstant.CON_STATUS_2.equals(type)){
					billPoolEmitFinaApplyService.updateResetPoolEmitFinaCon(paramsVo);
					if(AbPoolConstant.AB_POOL_OPT_STATUS19.equals(paramsVo.getOptStatus())){
						//完成以后重新写入一笔合同数据
						PoolEmitFinaConVO conVo=new PoolEmitFinaConVO();
						conVo.setConId(id);
						conVo.setConName("重新生成测试合同.txt");
						conVo.setConInf(Base64Util.encodeToString("重新生成测试合同,新合同信息内容".getBytes()));
						billPoolEmitFinaApplyService.savePoolEmitFinaCon(conVo);
					}
				}
			}
		}
	}
	
	/**
	 * 加载测试票据池保证金表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillInstructionManageInfoTestPoolBail(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST,dataTablesInput.getCurrentUserId()), "pb.corp_id", true);
		return abBillInstructionManagePool.listAbBillInstructionManageTestPoolBail(dataTablesInput,corpAuthSql);
	}
	
	/**
	 * 加载测试票据池额度表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillInstructionManageInfoTestPoolLimit(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST,dataTablesInput.getCurrentUserId()), "APLA.Corp_Id", true);
		DataTablesOutput<AbPoolLimitAllotVO> dataTablesOutput = abBillInstructionManagePool.listAbBillInstructionManageTestPoolLimit(dataTablesInput, corpAuthSql);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMIT_OP_TYPE", "AB_POOL_LIMT_TYPE", "AB_POOL_OPT_STATUS",
					"AB_POOL_STATUS", "AB_POOL_CHECK_TYPE");
			listData.forEach(item->{
				Optional.ofNullable(listDictMap).ifPresent(listDict -> {
					Optional.ofNullable(item.getType()).flatMap(type -> Optional.ofNullable(listDictMap.get("AB_POOL_LIMIT_OP_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(type)))).ifPresent(dict -> item.setTypeName(dict.getName()));
					Optional.ofNullable(item.getLimtType()).flatMap(limtType -> Optional.ofNullable(listDictMap.get("AB_POOL_LIMT_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(limtType)))).ifPresent(dict -> item.setLimtTypeName(dict.getName()));
					Optional.ofNullable(item.getOptStatus()).flatMap(optStatus -> Optional.ofNullable(listDictMap.get("AB_POOL_OPT_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus)))).ifPresent(dict -> item.setOptStatusName(dict.getName()));
					Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listDictMap.get("AB_POOL_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(status.toString())))).ifPresent(dict -> item.setStatusName(dict.getName()));
					Optional.ofNullable(item.getCheckType()).flatMap(checkType -> Optional.ofNullable(listDictMap.get("AB_POOL_CHECK_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(checkType)))).ifPresent(dict -> item.setCheckTypeName(dict.getName()));
				});
			});
		});
		return dataTablesOutput;
	}
	
	/**
	 * 加载测试票据池管理表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillInstructionManageInfoTestPoolCon(DataTablesInput dataTablesInput) {
		String corpAuthSql = SqlUtil.getInSql(
				abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST,dataTablesInput.getCurrentUserId()), "vv.corp_id", true);
		return abBillInstructionManagePool.listAbBillInstructionManageTestPoolCon(dataTablesInput,corpAuthSql);
	}
	/**
	 * 根据参数修改对应表状态
	 * @param type 业务类型
	 * @param id 修改数据id
	 * @param status 状态
	 * @param userVo 操作用户
	 */
	public void operation(String type, String id, String status, ICurrentUser userVo){
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(status)) {
			//表名称
			String tableName = null;
			//日志类型
			String logType = "";
			//日志消息
			String logMsg = "";
			Map<String,Object> params = new HashMap<>();
			params.put("id", id);
			if(AbPoolConstant.AB_POOL_OPT_STATUS16.equals(status)){
				params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
				logType = AccebillConstant.ABLOG_LOGTYPE_INSSEND;
				logMsg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
			}
			if(AbPoolConstant.AB_POOL_OPT_STATUS_2.equals(status)){
				params.put("status", Integer.valueOf(AbPoolConstant.AB_POOL_OPT_STATUS_2));
				logType = AccebillConstant.ABLOG_LOGTYPE_CANCELLATION;
				logMsg = AccebillConstant.LANG_LOG_DELETE_SUCC;
			}
			if (userVo != null) {
				params.put("updateId", userVo.getId());
			}
			params.put("updateTime", new Date());
			switch (type) {
				//申请
				case AbPoolConstant.EXC_LIMITTYPE_APPLY:
					tableName = "AbPoolLimitApply";
					break;
				//申请核准
				case AbPoolConstant.EXC_LIMITTYPE_APPLYCHECK:
					tableName = "AbPoolLimitApplyCheck";
					break;
				//分配
				case AbPoolConstant.EXC_LIMITTYPE_ALLOT:
					tableName = "AbPoolLimitAllot";
					break;
				//分配核准
				case AbPoolConstant.EXC_LIMITTYPE_ALLOTCHECK:
					tableName = "AbPoolLimitAllotCheck";
					break;
				//票据池保证金存入
				case AbPoolConstant.EXC_BUSTYPE_BAIL:
					tableName = "AbPoolBailDtl";
					break;
				default:
					tableName = null;
			}
			if (StringUtils.isNotEmpty(tableName)) {
				abBillInstructionManagePool.updateStatusByParams(tableName, params);
				//添加日志
				abLogService.saveAbLog(id, id, null, logType, logMsg, userVo);
			}
		}
	}
}
