package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.bill.api.bill.dto.AbVoucherinfoVODTO;
import com.bytter.bill.api.bill.dto.GuaContractBillDTO;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.GuaContractBillVO;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Service
* @Description: 票据主信息从表
* @author onlineGenerator
* @date 2017-06-21 15:50:03
* @version 1.0
*
*/
@Service("abVoucherinfoViceService")
@Transactional
public class AbVoucherinfoViceService extends BaseService<AbVoucherinfoVice,String> {
	@Autowired
	private AbVoucherinfoViceDao abVoucherinfoViceDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbFbsBudgetProcessService abFbsBudgetProcessService;
	@Autowired
	private AbBtCurrencyService abBtCurrencyService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbBtBankAccService btBankAccService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@Resource(name = "guaContractOccupServieImpl")
	private IGuaContractOccupServie contractOccupServie;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(AbVoucherinfoVice entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbVoucherinfoVice entity){
		// 冗余数据
		redundancy(entity);
		super.updateEntity(entity);
		abVoucherinfoViceDao.flush();
	}
	public BaseDao<AbVoucherinfoVice, String> getEntityDao() {
		return abVoucherinfoViceDao;
	}
	/**
	 * 冗余票据主信息从表数据。
	 *
	 * @param entity 票据主信息从表数据。
	 */
	private void redundancy(AbVoucherinfoVice entity){
		// 冗余持票单位信息
		Optional.ofNullable(entity.getCorpId()).ifPresent(corpId->{
			CorpDTO corpDTO = abCorpService.get(corpId);
			Optional.of(corpDTO).ifPresent(corp->{
				entity.setCorpCode(corp.getCode());
				entity.setCorpName(corp.getName());
				entity.setCorpType(corp.getType());
				entity.setNetId(corp.getNetId());
			});
		});
		// 前手名称（冗余）
		if(StringUtils.isNotBlank(entity.getCnAccrecords())){
			CorpDTO corpDTO = abCorpService.getCorpIdByName(entity.getCnAccrecords().trim());
			Optional.ofNullable(corpDTO).ifPresent(corp->{
				entity.setCnAccrecordsName(corp.getName());
				entity.setCnAccrecords(corp.getId());
			});
		}
		// 申请单位（冗余）
		Optional.ofNullable(entity.getApplyCorpId()).ifPresent(corpId->{
			CorpDTO corpDTO = abCorpService.get(corpId);
			Optional.ofNullable(corpDTO).ifPresent(corp->{
				entity.setApplyCorpCode(corp.getCode());
				entity.setApplyCorpName(corp.getName());
			});
		});
		// 业务单位名称（冗余）
		if (StringUtils.isNotBlank(entity.getBusCorpId())){
			CorpDTO corpDTO = abCorpService.get(entity.getBusCorpId());
			Optional.ofNullable(corpDTO).ifPresent(corp->{
				entity.setBusCorpName(corp.getName());
			});
		}
	}
	/**
	* 票据从表统一新增方法
	* @param entity 票据从表信息
	*/
	public void saveVoucherinfoVice(AbVoucherinfoVice entity, ICurrentUser userVo) {
// 		AbVoucherinfoVice oldInfo=abVoucherinfoViceDao.getVoucherinfoViceByVidAndCorpId(entity.getVid(),entity.getCorpId(),entity.getType(),null);
// 		if(oldInfo==null){
		if(StringUtils.isNotEmpty(entity.getBankAcc())&&
				(StringUtils.isEmpty(entity.getBankCode())
						||StringUtils.isEmpty(entity.getBifCode()))
				&&StringUtils.isNotBlank (entity.getBankAcc())){
			BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccDTO(entity.getBankAcc());
			if(bankAccInfo == null){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO);
			}
			if (StringUtils.isEmpty(entity.getBankCode())) {
				entity.setBankCode(bankAccInfo.getBankCode());
			}
			if (StringUtils.isEmpty(entity.getBankName())) {
				entity.setBankName(bankAccInfo.getBankName());
			}
			if (StringUtils.isEmpty(entity.getBifCode())) {
				entity.setBifCode(bankAccInfo.getBifCode());
			}
			// 冗余持票账户银行类别（冗余）
			if (StringUtils.isEmpty(entity.getBankTypeId())) {
				entity.setBankTypeId(bankAccInfo.getBankTypeId());
			}
			if (StringUtils.isEmpty(entity.getTypeName())) {
				entity.setTypeName(bankAccInfo.getBankTypeName());
			}
		}
		if (StringUtils.isEmpty(entity.getValidSign())) {
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		}
		if (entity.getBoxStatus() == null) {
			entity.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
		}
		if (entity.getIsSetCreateBy() && userVo != null) {
			//接口生成数据时，不设置创建人
			entity.setCreateBy(userVo.getLoginName());
			entity.setCreateName(userVo.getUsername());
		}
		//判断票据单位是否为空，为空则设置为申请单位
		if (StringUtils.isEmpty(entity.getCorpId()) && StringUtils.isNotEmpty(entity.getApplyCorpId())) {
			entity.setCorpId(entity.getApplyCorpId());
		}
		//判断申请单位是否为空，为空则设置为票据单位
		if (StringUtils.isEmpty(entity.getApplyCorpId()) && StringUtils.isNotEmpty(entity.getCorpId())) {
			entity.setApplyCorpId(entity.getCorpId());
		}
		if(StringUtils.isEmpty(entity.getBankAcc())){
			entity.setBankAcc(null);
		}
		// 冗余相关数据
		redundancy(entity);
		entity.setCreateDate(new Date());
		abVoucherinfoViceDao.save(entity);
		abVoucherinfoViceDao.flush();
	}
	/**
	 * 审批打回或指令打回时设置从表打回业务类型
	 * @param id 从表ID
	 * @param briefId 被修改的打回业务类型值
	 * @param acid 被修改的打回业务流水ID
	 * @return 修改成功的笔数
	 */
	public int updateBackBriefId(String id, String briefId, String acid){
		if(StringUtils.isEmpty(id)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		return abVoucherinfoViceDao.updateBackBriefId(id, briefId, acid);
	}
//	/**
//	 * 审批打回或指令打回时设置从表打回业务类型
//	 * @param id 从表ID
//	 * @param briefId 被修改的打回业务类型值
//	 * @param acid 被修改的打回业务流水ID
//	 * @return 修改成功的笔数
//	 */
//	public int updateBackBriefId(String id, String briefId){
//		if(StringUtils.isEmpty(id)){
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
//		}
//		return abVoucherinfoViceDao.updateBackBriefId(id, briefId, null);
//	}
	/**
	 * 重新做业务时设置从表打回业务类型为空
	 * 此方法一定要在推送消息处理了以后再调用
	 * @param id 从表ID
	 * @return 修改成功的笔数
	 */
	public int updateBackBriefIdIsNull(String id){
		return updateBackBriefId(id, "", "");
	}
	/**
	 * 根据id修改票据从表所属库存信息
	 * @param id 票据从表ID
	 * @param enterStockNew 修改为新的所属库存值（10单位库存11中心代管12银行托管）
	 * @return 修改成功的笔数
	 */
	public int updateEnterStock(String id,String enterStockNew,ICurrentUser userVo){
		String[] ids={id};
		return batchUpdateEnterStock(ids, enterStockNew,null,userVo);
	}
	/**
	 * 根据id修改票据从表所属库存信息
	 * @param id 票据从表ID
	 * @param enterStockNew 修改为新的所属库存值（10单位库存11中心代管12银行托管）
	 * @param poolStatus 票据池状态空或0池外1池内2入池在途3出池在途
	 * @return 修改成功的笔数
	 */
	public int updateEnterStock(String id,String enterStockNew,String poolStatus,ICurrentUser userVo){
		String[] ids={id};
		return batchUpdateEnterStock(ids, enterStockNew,null,poolStatus,userVo);
	}
	/**
	 * 根据ids批量修改票据从表所属库存信息
	 * @param ids 票据从表ID
	 * @param enterStockNew 修改为新的所属库存值（10单位库存11中心代管12银行托管）
	 * @param enterStockOld 根据对应旧所属库存处理所属库存值修改（10单位库存11中心代管12银行托管）
	 * @return 修改成功的笔数
	 */
	public int batchUpdateEnterStock(String[] ids,String enterStockNew,String enterStockOld,ICurrentUser userVo){
		return batchUpdateEnterStock(ids, enterStockNew, enterStockOld, null, userVo);
	}
	/**
	* 根据ids批量修改票据从表所属库存信息
	 * @param ids 票据从表ID
	 * @param enterStockNew 修改为新的所属库存值（10单位库存11中心代管12银行托管）
	 * @param enterStockOld 根据对应旧所属库存处理所属库存值修改（10单位库存11中心代管12银行托管）
	 * @param poolStatus 票据池状态空或0池外1池内2入池在途3出池在途
	 * @return 修改成功的笔数
	*/
	public int batchUpdateEnterStock(String[] ids,String enterStockNew,String enterStockOld,String poolStatus,ICurrentUser userVo){
		if(ids==null||ids.length==0||enterStockNew==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		if(userVo!=null){
			params.put("updateBy", userVo.getLoginName());
			params.put("updateName", userVo.getUsername());
		}
		params.put("updateDate", new Date());
		params.put("enterStockNew", enterStockNew);
		if(enterStockOld!=null){
			params.put("enterStockOld", enterStockOld);
		}
		if(poolStatus!=null){
			params.put("poolStatus", poolStatus);
		}
		int num = abVoucherinfoViceDao.batchUpdateEnterStock(params);
		return num;
	}
	/**
	 * 修改validsign
	 * @param id 票据从表id
	 * @param validSignNew 新状态
	 * @return
	 */
	public int updateValidSign(String id,String validSignNew,ICurrentUser userVo){
		String[] ids={id};
		return batchUpdateValidSign(ids, validSignNew,null,userVo);
	}
	/**
	 * 根据从表ID设置批次号为空
	 * @param id 票据从表id
	 * @return
	 */
	public int updateBatchNoIsNull(String id,ICurrentUser userVo){
		String[] ids={id};
		Map<String, Object> params=new HashMap<>();
		params.put("isNullBatchNo",true);
		if(userVo!=null){
			params.put("updateId", userVo.getId());
		}
		params.put("updateDate", new Date());
		params.put("ids", ids);
		int num = abVoucherinfoViceDao.batchUpdate(params);
		return num;
	}
	/**
	 * 批量修改 validsign
	 * @param ids 从表id
	 * @param validSignNew 新状态
	 * @param validSignOld 旧状态
	 * @return
	 */
	public int batchUpdateValidSign(String[] ids,String validSignNew,String validSignOld,ICurrentUser userVo){
		if(ids==null||ids.length==0||validSignNew==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		if(userVo!=null){
			params.put("updateBy", userVo.getLoginName());
			params.put("updateName", userVo.getUsername());
		}
		params.put("updateDate", new Date());
		params.put("validSignNew", validSignNew);
		if(validSignOld!=null){
			params.put("validSignOld", validSignOld);
		}
		List<String> vidList = new ArrayList<>();
		int num = 0;
		for(String vvid : ids){
			AbVoucherinfoVice vice = abVoucherinfoViceDao.get(vvid);
			AbVoucherinfo info = abVoucherinfoService.get(vice.getVid());
			//不拆票交易特殊处理
			if(vice.getPayLockMoney() == null || vice.getPayLockMoney().compareTo(info.getBillMoney()) == 0){
				vice.setValidSign(validSignNew);
				vice.setUpdateDate(new Date());
				vice.setUpdateBy(userVo == null ? vice.getUpdateBy() : userVo.getUsername());
				abVoucherinfoViceDao.saveOrUpdate(vice);
				num++;
				continue;
			}
			vidList.add(vvid);
		}
		ids = vidList.toArray(new String[vidList.size()]);
		int num2 = 0;
		if(ids != null && ids.length > 0){
			num2 = abVoucherinfoViceDao.batchUpdateValidSign(params);
		}
		return num+num2;
	}
	/**
	 *列表显示票据信息
	 *@param input 查询条件入参
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<AbVoucherinfoViceVO> listAccebillInfoDataTable(DataTablesInput input){
        DataTablesOutput<AbVoucherinfoViceVO> output;
		String resource=input.getFormdata().get("resource")==null?null:String.valueOf(input.getFormdata().get("resource"));
		List<String> corpAuthList=null;
		if(StringUtils.isNotEmpty(resource)){
			corpAuthList=abCorpService.getUserCorpIds(resource,input.getCurrentUserId());
		}
		//权限控制类型  1-单位级 0-账号级
		String controlType = abConfigService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (CommonConstant.NO.equals(controlType) && abConfigService.isElectricBill()) {
			//设置账号授权控制
			String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
			List<String> accountGrantList = accountGrantService.listByMultiVal(input.getCurrentUserId(), corpIds, resource,
					AccountGrantConstant.ONLINE_BANK_TYPE + "," + AccountGrantConstant.OFFLINE_BANK_TYPE,
					AccountGrantConstant.TRANSFER_ACCOUNT);
			input.getFormdata().put("accountGrantList", accountGrantList);
		}
		if ("1".equals(input.getFormdata().get("isSelect"))){
			String ids=input.getFormdata().get("ids")==null?null:String.valueOf(input.getFormdata().get("ids"));
			input.getFormdata().put("selectIds",StringUtils.isEmpty(ids)?"空":ids);
		}
	    //应付票据：开票补录审批通过的票据
        if ("1".equals(input.getFormdata().get("isEmitBill"))){
            input.getFormdata().remove("isEmitBill");
            input.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, isEmitMake());
            output = abVoucherinfoViceDao.listAccebillEmitInfoDataTable(input,corpAuthList);
        }else{
            output = abVoucherinfoViceDao.listAccebillInfoDataTable(input,corpAuthList);
        }
		Optional.ofNullable(output.getData()).ifPresent(listData -> {
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
					, "AB_BILL_SEARCHFOR_STATUS", "AB_BRIEF_STATUS", "AB_BILL_INSOPTSTATUS", "AB_BAIL_TYPE", "AB_BILL_SOURCE_QUERY", "AB_BILL_SEND_SIGN"
					, "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS", "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS"
					, "AB_BILL_POOL_STATUS", "AB_POOL_INOUT_TYPE","AB_BANEM","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_ABRECEIVEBILL_SOURCE","sf_yn");
			for (AbVoucherinfoViceVO voucher : listData) {
				voucher.injectValues(listDictMap);
			}
		});
        return output;
	}
	
	

	/**
	 * 根据前端传递数据获取需要显示的票据信息
	 * @param reVo
	 * @return
	 */
	public List<AbVoucherinfoViceVO> getAccebillDataViewList(AbVoucherinfoViceVO reVo){
		List<AbVoucherinfoViceVO> infoList=listAccebillInfoView(reVo);
		//查询票据池
		List<AbVoucherinfoViceVO> infoList2=listAccebillInfoViewByPoolId(reVo);
		if (!CollectionUtils.isEmpty(infoList2)) {
			infoList.addAll(infoList2);
		}
		Map<String, BigDecimal> balanceMap=new HashMap<>();
		//如果有设置支付金额查看就需要设置付款设置的金额
		if(StrUtil.isNotEmpty(reVo.getId())
				&& StrUtil.isNotEmpty(reVo.getBillBalance()) ){
			String[] ids=StringUtils.split(reVo.getId(),",");
			String[] billBalances=StringUtils.split(reVo.getBillBalance(),",");
			if(ids.length == billBalances.length){
				for(int i=0;i<ids.length;i++) {
					balanceMap.put(ids[i],new BigDecimal(billBalances[i]));
				}
			}
		}
		if(CollectionUtil.isNotEmpty(infoList)) {
			infoList.forEach(entity -> {
				if (balanceMap.containsKey(entity.getId())) {
					entity.setPayMoney(balanceMap.get(entity.getId()));
				} else if (entity.getPayMoney() == null) {
					entity.setPayMoney(entity.getBillMoney());
				}
			});
		}
		return infoList;
	}
	
	/**
	 * 根据流水IDS查看票据信息
	 *
	 * @param reVo 单位持票信息VO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbVoucherinfoViceVO> listAccebillInfoView(AbVoucherinfoViceVO reVo) {
		if(reVo==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}else if(StringUtils.isEmpty(reVo.getId())){
			reVo.setId("");
			return null;
		}
		String[] ids=StringUtils.split(reVo.getId(),",");
		List<AbVoucherinfoViceVO> list=abVoucherinfoViceDao.listAccebillInfoViewByIds(ids,reVo.getPayId());
				// 添加冗余数据
				Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
						, "AB_BILL_SEARCHFOR_STATUS", "AB_BRIEF_STATUS", "AB_BILL_INSOPTSTATUS", "AB_BAIL_TYPE", "AB_BILL_SOURCE_QUERY", "AB_BILL_SEND_SIGN"
						, "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS", "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BRIEF_STATUS"
						, "AB_BILL_POOL_STATUS", "AB_POOL_INOUT_TYPE","AB_BANEM","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_ABRECEIVEBILL_SOURCE");
				for (AbVoucherinfoViceVO voucher : list) {
					voucher.injectValues(listDictMap);
				}
		return list;
	}
	/**
	 * 根据票据池明细流水IDS查看票据信息
	 *
	 * @param reVo 单位持票信息VO
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbVoucherinfoViceVO> listAccebillInfoViewByPoolId(AbVoucherinfoViceVO reVo) {
		if(reVo==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}else if(StringUtils.isEmpty(reVo.getId())){
			reVo.setId("");
			return null;
		}
		String[] ids=StringUtils.split(reVo.getId(),",");
		return abVoucherinfoViceDao.listAccebillInfoViewByPoolId(ids,reVo.getPayId());
	}
    /**
     * 修改从表付款状态和付款单id
     * @param billAccerIds 流水ids
     * @param isPay 付款状态: 1付过款   0或空则没付过款
     * @param voucherId 付款单id
     * @param userVo 当前用户
	 * @param  isBudget 是否挂预算（true：则释放票据的原有的预算 false:则不预算票据原有的预算）
     */
	public int batchUpdateBillIsPayAndVoucherId(String[] billAccerIds,String isPay,String voucherId,ICurrentUser userVo,boolean isBudget){
		List<AbVoucherinfoViceVO>  vvInfo = abVoucherinfoViceDao.listAccebillInfoViewByIds(billAccerIds,null);
		String[] ids= new String[vvInfo.size()];
		for(int i=0;i<vvInfo.size();i++){
			ids[i]=vvInfo.get(i).getVvid();
			//预算处理
			if(isBudget){
				//释放付款单预算
					/*List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNoForBill(
							BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,vvInfo.get(i).getVoucherId ());
					if(list!=null && !list.isEmpty ()){
						String billNo = list.get (0).getBillNo ();
						if(StringUtils.isNotBlank (billNo)){
							fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
									BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
						}
					}*/
					// 释放预算
					abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
							BudgetConstant.PROCESS_SYSNAME_AB, vvInfo.get(i).getVvid());
				}
            }
            Map<String, Object> params=new HashMap<>();
            params.put("ids", ids);
            params.put("isPay",isPay);
            params.put("voucherId", voucherId);
            if(userVo!=null){
                params.put("updateBy", userVo.getLoginName());
                params.put("updateName", userVo.getUsername());
            }
            params.put("updateDate", new Date());
            int num = 0;
			if(StringUtils.isNotEmpty (ids)) {
				num = abVoucherinfoViceDao.batchUpdateBillIsPayAndVoucherId(params);
			}
            if(num>0){
                //abLogService.batchSaveAbLog(ids, billAccerIds, briefId, logType, msg, userVo);
            }
            return num;
    }
	/**
	 * 批量修改票据从表处理状态
	 * @param ids 票据从表ID是
	 * @param boxStatus 修改为对的处理状态值（10处理中，11处理完成）
	 * @return 修改成功的笔数
	 */
	public int batchUpdateBoxStatus(String[] ids,Integer boxStatus,ICurrentUser userVo){
		return batchUpdate(ids, boxStatus, null,userVo);
	}
	/**
	 * 修改票据从表处理状态
	 * @param id 票据主表ID是
	 * @param boxStatus 修改为对的处理状态值（10处理中，11处理完成）
	 * @return 修改成功的笔数
	 */
	public int updateBoxStatus(String id,Integer boxStatus,ICurrentUser userVo){
		String[] ids={id};
		return batchUpdateBoxStatus(ids, boxStatus,userVo);
	}
	/**
	 * 根据id修改票据从表信息
	 * @param id 票据从表ID是
	 * @param boxStatusNew 修改为对的处理状态值（10处理中，11处理完成）
	 * @param boxStatusOld 根据对应旧状态处理状态值修改（10处理中，11处理完成）
	 * @return 修改成功的笔数
	 */
	public int update(String id,Integer boxStatusNew,Integer boxStatusOld,ICurrentUser userVo){
		String[] ids={id};
		int num = batchUpdate(ids, boxStatusNew, boxStatusOld,userVo);
		return num;
	}
	/**
	* 根据ids批量修改票据从表信息
	 * @param ids 票据从表ID
	 * @param boxStatusNew 修改为对的处理状态值（10处理中，11处理完成）
	 * @param boxStatusOld 根据对应旧状态处理状态值修改（10处理中，11处理完成）
	 * @return 修改成功的笔数
	*/
	public int batchUpdate(String[] ids,Integer boxStatusNew,Integer boxStatusOld,ICurrentUser userVo){
		if(ids==null||ids.length==0||boxStatusNew==null){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		if(userVo!=null){
			params.put("updateBy", userVo.getLoginName());
			params.put("updateName", userVo.getUsername());
		}
		params.put("updateDate", new Date());
		params.put("boxStatusNew", boxStatusNew);
		if(boxStatusOld!=null){
			params.put("boxStatusOld", boxStatusOld);
		}
		int num = abVoucherinfoViceDao.batchUpdate(params);
		return num;
	}

	/**
	 * 根据ids批量修改从表数据
	 * @param params
	 * @return
	 */
	public int batchUpdate(Map<String,Object> params){
		int num = abVoucherinfoViceDao.batchUpdate(params);
		return num;
	}

	/**
	 * 根据id提交修改处理标记为处理中
	 * @param id
	 */
	public int submit(String id,ICurrentUser userVo){
		String[] ids={id};
		int num = batchUpdate(ids,AccebillConstant.AB_BOX_STATUS_N,AccebillConstant.AB_BOX_STATUS_Y,userVo);
		return num;
	}
	/**
	 * 根据id提交修改处理标记为处理完成
	 * @param id
	 */
	public int submitIsEnd(String id,ICurrentUser userVo){
		String[] ids={id};
		int num = batchUpdate(ids,AccebillConstant.AB_BOX_STATUS_Y,AccebillConstant.AB_BOX_STATUS_N,userVo);
		return num;
	}
	/**
	* 根据ids批量修改处理标记为处理中
	* @param ids
	*/
	public int batchSubmit(String[] ids,ICurrentUser userVo){
		int num = batchUpdate(ids,AccebillConstant.AB_BOX_STATUS_N,AccebillConstant.AB_BOX_STATUS_Y,userVo);
		return num;
	}
	/**
	 * 根据票据主信息ID和单位ID获取从表数据
	 * @param vid 票据主表id
	 * @param corpId 单位id
	 * @param type 从表类型 0应收1应付
	 * @return
	 */
	public AbVoucherinfoVice getVoucherinfoViceByVidAndCorpId(String vid,String corpId,String type,String bankAcc){
		return abVoucherinfoViceDao.getVoucherinfoViceByVidAndCorpId(vid,corpId,type,bankAcc);
	}
	/**
	 * 根据票据号和所属单位ID检查是否已经收入过此票据
	 * @param origNo 票据号
	 * @param corpId 所属单位
	 * @return 已收入返回true 或者 未收入返回false
	 */
	@Transactional(readOnly = true)
	public boolean checkPayeeOrigNo(String origNo,String corpId){
		return abVoucherinfoViceDao.checkPayeeOrigNo(origNo, corpId);
	}
	/**
	 * 是否挂接预算系统
	 * @param briefId 业务流水 001开票，6001库存登记，010背书
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isBudget(String briefId){
		if(abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).toBoolean()){
			if(StringUtils.isNotEmpty(getBudgetItem(briefId))){
				return true;
			}
		}
		return isBudget(null, briefId);
	}
	/**
	 * 根据业务获取预算科目
	 * @param briefId 业务流水 001开票，6001库存登记，010背书
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getBudgetItem(String briefId){
		String name="";
		if(AccebillConstant.BRIEF_STATUS_001.equals(briefId)){
			name=AccebillConstant.AB_CONFIG_EMITITEM;
		}else if(AccebillConstant.BRIEF_STATUS_6001.equals(briefId)){
			name=AccebillConstant.AB_CONFIG_RECITEM;
		}else if(AccebillConstant.BRIEF_STATUS_010.equals(briefId)){
			name=AccebillConstant.AB_CONFIG_ENDORSEITEM;
		}else if(AccebillConstant.BRIEF_STATUS_011.equals(briefId)){
			name=AccebillConstant.AB_CONFIG_DISCOUNTITEM;
		}else if(AccebillConstant.BRIEF_STATUS_020.equals(briefId)){
			name=AccebillConstant.AB_CONFIG_COLLECTIONITEM;
		}
		String budgetItem=abConfigService.getBillConfig(name).toString();
		return budgetItem;
	}
	/**
	 * 是否挂接预算系统
	 *
	 * @param corpId  单位ID
	 * @param briefId 业务流水 001开票，6001库存登记，010背书
	 * @return 是否挂接预算
	 */
	@Transactional(readOnly = true)
	public boolean isBudget(String corpId, String briefId) {
		if (StringUtils.isNotEmpty(corpId)
				&& isBudget(briefId)) {
			String[] corpIds = corpId.split(",");
			for (String id : corpIds) {
				if (!abFbsBudgetProcessService.corpCtlBudget(id)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 是否启用线上清算
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isEOnlinePay(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEONLINEPAY).toBoolean();
	}
	/**
	 * 是否自动提示承兑
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isEAutoAccept(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOACCEPT).toBoolean();
	}
	/**
	 * 是否自动提示收票
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isEAutoInvoice(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOINVOICE).toBoolean();
	}
	/**
	 * 自动提示付款清算方式
	 * @return
	 */
	@Transactional(readOnly = true)
	public String isEAutoPaySttlmmk(String id){
		AbVoucherinfo vice = abVoucherinfoService.get(id, true);
		return vice.getBillType().equals(Integer.valueOf(AccebillConstant.AB_BILL_TYPE_BANK))
				?abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOPAYSTTLMMK).getValue()
				:abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOPAYSTTLMMK_MERCH).getValue();
	}
	/**
	 * 是否自动提示付款
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isEAutoPay(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOPAY).toBoolean();
	}
	/**
	 * 自动提示付款提前天数
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer getEAutoPayDayNum(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOPAYDAYNUM).toInteger(10);
	}
	/**
	 * 获取本位币ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getCurIdByNatual(){
		BtCurrencyDTO btCurrencyDTO =abBtCurrencyService.getNatualCurrency();
		if(!EmptyUtils.isEmpty(btCurrencyDTO)){
			return btCurrencyDTO.getId();
		}else{
			return null;
		}
	}
	/**
	 * 获取人民币ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getCurIdByCNY(){
		BtCurrencyDTO btCurrencyDTO =abBtCurrencyService.getRMBCurrency();
		if(!EmptyUtils.isEmpty(btCurrencyDTO)){
			return btCurrencyDTO.getId();
		}else{
			return null;
		}
	}
	/**
	 * 自开票据到期提醒天数
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer getEmitWarnDayNum(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITWARNDAYNUM).toInteger(10);
	}
	/**
	 * 库存票据到期提醒天数
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer getReserveWarnDayNum(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_RESERVEWARNDAYNUM).toInteger(10);
	}
	/**
	 * 0-否,1-是 是否客商白名单统一管理
	 * @return
	 */
	@Transactional(readOnly = true)
	public Integer getExternalCorpNum(){
		return abConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).toInteger(0);
	}
	/**
	 * 不启用贴现申请功能直接贴现办理
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isNotDiscountApply(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISNOTDISCOUNTAPPLY).toBoolean(true);
	}
	/**
	 * 开票是否需要补录
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isEmitMake(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEMITMAKE).toBoolean(true);
	}
	/**
	 * 获取纸票票据金额最大额度
	 * @return 金额
	 */
	@Transactional(readOnly = true)
	public BigDecimal getMaxBillMoney(){
		String maxBillMoney =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_MAXBILLMONEY).toString();
		BigDecimal maxBillMoneyVal = BigDecimal.ZERO;
		if (StringUtils.isNotEmpty(maxBillMoney)) {
			maxBillMoneyVal = new BigDecimal(maxBillMoney);
		}
		return maxBillMoneyVal;
	}
	/**
	 * 票据入池业务修改
	 */
	/**
     * 批量修改操作
     * @param vvid 从表ID
     * @param poolStatus 票据池状态
     * @param userVo 用户
     * @return
     */
    public int updatePoolSatatus(String vvid,String poolStatus,ICurrentUser userVo){
        Map<String, Object> params=new HashMap<String, Object>();
        String[] ids = {vvid};
        params.put("ids", ids);
        if(poolStatus!=null){
            params.put("poolStatus", poolStatus);
        }
        if(userVo!=null){
            params.put("updateId", userVo.getId());
        }
        params.put("updateDate", new Date());
        return abVoucherinfoViceDao.batchUpdate(params);
    }
	/**
	 * 未挂接担保数据查询
	 * @param input 查询条件入参
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<AbBillPayAndReceVO> listGuaranteeDataTable(DataTablesInput input) {
		DataTablesOutput<AbBillPayAndReceVO> dataTablesOutput = abVoucherinfoViceDao.listGuaranteeDataTable(input, isEmitMake());
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY","AB_BILL_TYPE","AB_BILL_FLAG"
					,"AB_BRIEF_STATUS","ACCEBILL_EMIT_TYPE","sf_yn","AB_BILL_STATUS","AB_BILL_SEND_SIGN");
			for(AbBillPayAndReceVO bill : listData){
				injectValues(bill, listDictMap);
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbBillPayAndReceVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
		// 转让方式
		Optional.ofNullable(bill.getTransferWay()).ifPresent(transferWay -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TRANSFERWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(transferWay)))
					.ifPresent(dict -> bill.setTransferWayName(dict.getName()));
		});
		// 票据类型
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag+"")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 业务类型
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 开票方式
		Optional.ofNullable(bill.getEmitType()).ifPresent(emitType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_EMIT_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitType)))
					.ifPresent(dict -> bill.setEmitTypeName(dict.getName()));
		});
		// 是否开票保贴
		Optional.ofNullable(bill.getEmitGuandisSign()).ifPresent(emitGuandisSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitGuandisSign)))
					.ifPresent(dict -> bill.setEmitGuandisSignName(dict.getName()));
		});
		// 开票完成
		Optional.ofNullable(bill.getEmitOpenOk()).ifPresent(emitOpenOk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitOpenOk)))
					.ifPresent(dict -> bill.setEmitOpenOkName(dict.getName()));
		});
		// 是否已付款
		Optional.ofNullable(bill.getIsPay()).ifPresent(isPay -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isPay)))
					.ifPresent(dict -> bill.setIsPayName(dict.getName()));
		});
		// 票据状态
		Optional.ofNullable(bill.getBillStatus()).ifPresent(billStatus -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billStatus)))
					.ifPresent(dict -> bill.setBillStatusName(dict.getName()));
		});
		// 是否发送指令标志
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
	}
	/**
	 * 修改从表信息为不挂接担保合同
	 * @param id 票据从表id
	 * @param isBearModel 是否挂接担保合同
	 * @param user
	 * @return
	 */
	public void updateGuarantee(String id, String isBearModel, ICurrentUser user){
		if(StringUtils.isNotEmpty(id)){
			AbVoucherinfoVice abVoucherinfoVice = this.get(id);
			abVoucherinfoVice.setBearModel(isBearModel);
			abVoucherinfoVice.setUpdateDate(new Date());
			abVoucherinfoVice.setUpdateName(user.getUsername());
			this.update(abVoucherinfoVice);
		}
	}
	/**
	 * 获取挂接授信的票据主表ID集合
	 * @return
	 */
	public List<String> getBillIdListByRat() {
		return abVoucherinfoViceDao.getBillIdListByRat();
	}
    /**
     * 根据单位id获取此单位中是否存在应收应付的票据
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidBillNumByCorpId(String corpId,String type){
        String sql="select count(*) from ab_voucherinfo_vice where 1=1 and corp_id=:corpId and TYPE=:type";
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("type",type);
        return ((Number)abVoucherinfoViceDao.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
	/**
	 * 根据票据主信息ID和排除从表id获取是否存在从表数据
	 * @param vid 票据主表id
	 * @param vvid 从表id
	 * @return
	 */
	public List<AbVoucherinfoVice> getVoucherinfoViceByVidAndVvid(String vid,String vvid){
		if(StringUtils.isNotEmpty(vid) && StringUtils.isNotEmpty(vvid)) {
			return abVoucherinfoViceDao.getVoucherinfoViceByVidAndVvid(vid, vvid);
		}
		return null;
	}
	/**
	 * 根据vvid物理删除票据从表数据
	 * @param vvid
	 * @return
	 */
	public int deleteByVvId(String vvid){
		if(StringUtils.isNotEmpty(vvid)){
			return abVoucherinfoViceDao.deleteByVvId(vvid);
		}
		return 0;
	}
	/**
	 * 背面信息是否启用降序
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isBillBackDesc(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_BILLBACKDESC).toBoolean();
	}
	/**
	 * 是否贴现到账确认 0否1是
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isDiscountConfirm(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISDISCOUNTCONFIRM).toBoolean();
	}
	/**
	 * 新增查询银行账户信息
	 */
	public String findBankTypeNameWith(String vvid){
		if(StringUtils.isBlank(vvid)){
			return null;
		}
		AbVoucherinfoVice vice =this.get(vvid,true);
		if(StringUtils.isNotEmpty(vice) && !StringUtils.isBlank(vice.getBankAcc())){
			BtBankAccDTO bank = btBankAccService.getBankAccInfo(vice.getBankAcc());
			return StringUtils.isNotEmpty(bank)?bank.getBankTypeName():null;
		}
		return  null;
	}
	/**
	 * 是否贴现到账确认 0否1是
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isCollectionApplyConfirm(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCOLLECTIONAPPLYCONFIRM).toBoolean();
	}
	/**
	 * 是否支持同名跨行背书
	 * @return
	 */
	public boolean isCounterpartEndorse(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCOUNTERPARTENDORSE).toBoolean();
	}
	/**
	 * 是否允许重复背书
	 * @return
	 */
	public String isRepeatEndorse(){
		return abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISREPEATENDORSE).toString();
	}
	/**
	 * 根据票据认领id数组修改对应从表信息的票据认领数据
	 * @param claimIdArr 票据认领id数组
	 */
	public void updateByClaimArr(String[] claimIdArr) {
		abVoucherinfoViceDao.updateByClaimArr(claimIdArr);
	}
    /**
     * 通过id数组修改票据认领中间表id和状态
     *
     * @param vvidArr     从表id数组
     * @param claimId     中间表id
     * @param claimStatus 认领状态
     */
    public void updateClaimByIds(String[] vvidArr, String claimId, String claimStatus) {
        abVoucherinfoViceDao.updateClaimByIds(vvidArr, claimId, claimStatus);
    }

    public int updateFieldById(AbVoucherinfoVODTO voucherinfoVODTO){
		return abVoucherinfoViceDao.updateFieldById(voucherinfoVODTO);
	}
    /**
     * 应收补录修改业务单位、业务人员
     *
     * @param abVoucherinfoVice 签收补录vo
     */
	public void updateBusCorpSupply(AbVoucherinfoViceVO abVoucherinfoVice) {
		abVoucherinfoViceDao.updateBusCorpSupply(abVoucherinfoVice);
	}
	/**
	 * 修改是否签收补录完成
	 *
	 * @param vvid         从表id
	 * @param isSignSupply 是否签收补录完成
	 */
    public void updateIsSignSupply(String vvid, int isSignSupply) {
		abVoucherinfoViceDao.updateIsSignSupply(vvid, isSignSupply);
    }
	public DataTablesOutput<GuaContractBillVO> getGuaForBill(DataTablesInput input, ICurrentUser user) {
		String sysCode = input.getFormdata().get("eqs_sysCode").toString();
		String startDate = input.getFormdata().get("ged_useDate").toString();
		String endDate = input.getFormdata().get("led_useDate").toString();
		String status = input.getFormdata().get("eqg_status").toString();
		String eqs_contractId = input.getFormdata().get("eqs_contractId") == null ? "":input.getFormdata().get("eqs_contractId").toString();
		input.setFormdata(null);
		//获取挂接授信的业务id集合和封装参数
		List<GuaContractOccupDTO> guaList = contractOccupServie.getGua(sysCode, startDate, endDate, status,eqs_contractId);
		List<String> corpIds = abCorpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
		if (EmptyUtils.isEmpty(guaList)) {
			return new DataTablesOutput<>();
		}
		Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
		List<String> businessIdList = new ArrayList<>();
		guaList.forEach(item -> {
			guaMap.put(item.getBusinessId(), item);
			businessIdList.add(item.getBusinessId());
		});
		//组装参数
		DataTablesOutput<GuaContractBillVO> dataTablesOutput = abVoucherinfoViceDao.getGuaForBill(input, corpIds, businessIdList);
		if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
			for (GuaContractBillVO item : dataTablesOutput.getData()) {
				if (guaMap.containsKey(item.getId())) {
					item.setStatus(guaMap.get(item.getId()).getStatus());
					item.setUseDate(guaMap.get(item.getId()).getUseDate());
				}
			}
		}
		return dataTablesOutput;
	}
	/**
	 * 获取所有挂接担保的票据数据
	 */
	public List<GuaContractBillDTO> getGuaForbill() {
		return abVoucherinfoViceDao.getGuaForbill();
	}
	
	/**
	 *
	 * 付款挂接票据锁定金额操作
	 * @param params aid 流水表ID，addMoney增加金额，subtractMoney减少金额
	 * @return
	 */
	public int updatePayLockMoney(Map<String,Object> params) {
		if(CollectionUtil.isEmpty(params)
				||!params.containsKey("aid")){
			return 0;
		}
		return abVoucherinfoViceDao.updatePayLockMoney(params);
	}

	public List<AbVoucherinfoVice> listByIdList(List<String> ids){
		return abVoucherinfoViceDao.listByIdList(ids);
	}

	public int updatePayLockMoneyByIdAndMoney(String id, BigDecimal oldMoney,BigDecimal newMoney,String updateBy){
		int num = abVoucherinfoViceDao.updatePayLockMoneyByIdAndMoney(id,oldMoney,newMoney,updateBy);
		abVoucherinfoViceDao.flush();
		abVoucherinfoViceDao.clear();
		return num;
	}

	/**
	* @Description 根据从表id获取票据号
	* @author zhous
	* @date 2024/8/20 16:07
	*/
	public String getOrigNoByVid(String vId) {
		return abVoucherinfoViceDao.getOrigNoByVid(vId);
	}
}
