package com.bytter.bill.domain.bill.service;
// import com.bytter.system.api.dto.BtBankAccDTO;
// import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAcceptApplyVO;
import com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.ExternalCorpBankAccDetailDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
// import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
// import com.bytter.system.api.dto.BtBankAccConstant;
// import com.bytter.system.api.dto.BisBifInitDTO;
/**
* @Title: Service
* @Description: 票据流水信息
* @author onlineGenerator
* @date 2017-06-21 15:36:09
* @version 1.0
*
*/
@Service("abAccrecordsService")
@Transactional
public class AbAccrecordsService extends BaseService<AbAccrecords,String> {
	@Autowired
	private AbAccrecordsDao abAccrecordsDao;
	@Autowired
	private AbConfigService abConfigService;
	@Autowired
	private AbBtBankAccService abBtBankAccService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBisBifInitService abBisBifInitService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbDiscountService abDiscountService;
	@DubboReference(check = false)
	private IExternalCorpService iExternalCorpService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	@DubboReference(check = false)
	private ICalendarService calendarService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 判断资金计划科目代码是否匹配票据业务
	 *
	 * @param business 业务
	 * @return 是否匹配
	 */
	public boolean itemCodeIsMatchBusiness(String business, String itemCode) {
		String recItem = abConfigService.getBillConfig(business).getValue();
		String[] recItemArr = recItem.split(";");
		for (String item : recItemArr) {
			if (itemCode.startsWith(item)) {
				return false;
			}
		}
		return true;
	}
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(AbAccrecords entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据IDS获取流水表对象信息
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> listAbAccrecordsByIds(String[] ids){
		AbAccrecordsVO accrecordsVo=new AbAccrecordsVO();
		accrecordsVo.setIdArray(ids);
		return abAccrecordsDao.listAbAccrecordsByIds(accrecordsVo);
	}
	/**
	 * 根据IDS获取流水表对象信息
	 * @param accrecordsVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> listAbAccrecordsByIds(AbAccrecordsVO accrecordsVo){
		return abAccrecordsDao.listAbAccrecordsByIds(accrecordsVo);
	}
	/**
	* 根据id删除 逻辑删除状态为-2
	* @param id
	*/
 	public void delete(String id,ICurrentUser userVo){
 		if(StringUtils.isNotEmpty(id)){
 			String[] ids={id};
 			int size=batchUpdate(ids, AccebillConstant.AB_BILL_BUS_STATUS_2, null,null,userVo);
 			if(size==0){
 	 			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
 			}
 		}else{
 			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
 		}
 	}
	/**
	 * 根据上笔流水Id修改状态
	 * @param lastId
	 * @param userVo
	 * @param status
	 */
 	public void updateStatusByLastId(String lastId,ICurrentUser userVo,Integer status){
		if(StringUtils.isNotEmpty(lastId)){
			Map<String, Object> params=new HashMap<>();
			params.put("lastId", lastId);
			if(userVo!=null){
				params.put("updateName", userVo.getUsername());
				params.put("updateBy", userVo.getLoginName());
			}
			params.put("updateDate", new Date());
			params.put("status",status);
			abAccrecordsDao.updateStatusByLastId(params);
		}else{
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
	}
	/**
	 * 根据流水Id修改状态
	 * @param id
	 * @param userVo
	 * @param status
	 */
 	public void updateStatusById(String id,ICurrentUser userVo,Integer status){
		if(StringUtils.isNotEmpty(id)){
			Map<String, Object> params=new HashMap<>();
			params.put("id", id);
			if(userVo!=null){
				params.put("updateName", userVo.getUsername());
				params.put("updateBy", userVo.getLoginName());
			}
			params.put("updateDate", new Date());
			params.put("status",status);
			abAccrecordsDao.updateStatusById(params);
		}else{
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
	}
 	/**
 	 * 批量修改操作
 	 * @param ids 流水表ID
 	 * @param validSign 有效标志
 	 * @param userVo 用户标记
 	 * @return
 	 */
 	public int batchUpdate(String[] ids,String validSign,ICurrentUser userVo){
 		AbAccrecordsVO accrecordsVO=new AbAccrecordsVO();
		accrecordsVO.setValidSign(validSign);
		accrecordsVO.setIdArray(ids);
 		return batchUpdate(accrecordsVO,userVo);
 	}
	/**
	 * 批量修改操作（重载方法，解决收票登记批量提交时，流水表出现opt_status字段清空的问题）
	 * @param ids 流水表ID
	 * @param statusNew 流水表新状态
	 * @param statusOld 流水表旧状态
	 * @param userVo 用户对象
	 * @return 修改记录条数
	 */
	public int batchUpdate(String[] ids,Integer statusNew,Integer statusOld, ICurrentUser userVo){
		AbAccrecordsVO accrecordsVO=new AbAccrecordsVO();
		accrecordsVO.setIdArray(ids);
		accrecordsVO.setStatusNew(statusNew);
		accrecordsVO.setStatusOld(statusOld);
		return batchUpdate(accrecordsVO,userVo);
	}
 	/**
 	 * 批量修改操作
 	 * @param ids 流水表ID
 	 * @param statusNew 流水表新状态
 	 * @param statusOld 流水表旧状态
 	 * @param validSign 有效标志
 	 * @return
 	 */
	public int batchUpdate(String[] ids,Integer statusNew,Integer statusOld,String validSign,ICurrentUser userVo){
		AbAccrecordsVO accrecordsVO=new AbAccrecordsVO();
		/*String[] newArr = new String[ids.length];
		boolean clearFlg = false;
 		for(int i = 0; i < ids.length; i++){
 			String id = ids[i];
			if(!clearFlg){
				clearFlg = id.contains("&clearFlg") ? true : false;
			}
			id = ids[i].replaceAll("&clearFlg","");
			newArr[i] = id;
		}*/

		accrecordsVO.setIdArray(ids);
		accrecordsVO.setStatusNew(statusNew);
		accrecordsVO.setStatusOld(statusOld);
		if(statusNew==AccebillConstant.AB_BILL_BUS_STATUS95){
			accrecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else{
			//审批打回没有生成指令，所以清空指令
			if(AccebillConstant.AB_BILL_BUS_STATUS_2.equals(statusNew)){
				accrecordsVO.setOptStatusNew(" ");
			}
 			/*if(clearFlg){

			}*//*else {
				accrecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS19);
			}*/
			accrecordsVO.setValidSign(validSign);
		}

		int relNum = batchUpdate(accrecordsVO,userVo);
		return relNum;
	}
	/**
	 * 根据id删除 逻辑删除状态为-2  开票登记删除专用
	 * @param id
	 */
	public int deleteByBriefIdIs001(String id,ICurrentUser userVo){
		int size = 0;
		if(StringUtils.isNotEmpty(id)){
			String[] ids={id};
			size = batchDeleteByBriefIdIs001(ids,userVo);
			if(size==0){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		}else{
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		return size;
	}
	/**
	 * 批量修改操作  开票登记删除专用
	 * @param ids 流水表ID
	 * @return
	 */
	public int batchDeleteByBriefIdIs001(String[] ids,ICurrentUser userVo){
		if(StringUtils.isEmpty(ids)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		params.put("statusNew", AccebillConstant.AB_BILL_BUS_STATUS_2);
		if(userVo!=null){
			params.put("updateName", userVo.getUsername());
			params.put("updateBy", userVo.getLoginName());
		}
		params.put("updateDate", new Date());
		return abAccrecordsDao.batchDeleteByBriefIdIs001(params);
	}
	/**
	 * 票据兑付确认专用
	 * @param ids 流水表ID
	 * @param newStatus 流水的修改状态
	 * @param oldStatus 流水修改前的状态
	 * @return
	 */
	public int batchUpdateStatusByTrack(String[] ids,Integer newStatus,Integer oldStatus,ICurrentUser userVo){
		if(StringUtils.isEmpty(ids)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", ids);
		params.put("statusNew", newStatus);
		params.put("statusOld", oldStatus);
		if(userVo!=null){
			params.put("updateName", userVo.getUsername());
			params.put("updateBy", userVo.getLoginName());
		}
		params.put("updateDate", new Date());
		return abAccrecordsDao.batchUpdateStatusByTrack(params);
	}
 	/**
 	 * 批量修改操作
 	 * @param accRecordsVO 流水VO信息
 	 * @return
 	 */
	public int batchUpdate(AbAccrecordsVO accRecordsVO,ICurrentUser userVo){
		if(StringUtils.isEmpty(accRecordsVO.getIdArray())){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params=new HashMap<>();
		params.put("ids", accRecordsVO.getIdArray());
		if(accRecordsVO.getStatusNew()!=null){
			params.put("statusNew", accRecordsVO.getStatusNew());
		}
		if(accRecordsVO.getStatusOld()!=null){
			params.put("statusOld", accRecordsVO.getStatusOld());
		}
		if(accRecordsVO.getOptStatusNew()!=null){
			params.put("optStatusNew", accRecordsVO.getOptStatusNew());
		}
		if(accRecordsVO.getReturnCode()!=null){
			params.put("returnCode", accRecordsVO.getReturnCode());
		}
		if(accRecordsVO.getReturnMsg()!=null){
			params.put("returnMsg", accRecordsVO.getReturnMsg());
		}
		if(accRecordsVO.getOptStatusOld()!=null){
			params.put("optStatusOld", accRecordsVO.getOptStatusOld());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getValidSign())){
			params.put("validSign", accRecordsVO.getValidSign());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getCdtRatgs())){
			params.put("cdtRatgs", accRecordsVO.getCdtRatgs());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getBusGlobalId())){
			params.put("busGlobalId", accRecordsVO.getBusGlobalId());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getMsgId())){
			params.put("msgId", accRecordsVO.getMsgId());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getId())){
			params.put("id", accRecordsVO.getId());
		}
		if(userVo!=null){
			params.put("updateName", userVo.getUsername());
			params.put("updateBy", userVo.getLoginName());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getCreateBy())){
			params.put("createBy", accRecordsVO.getCreateBy());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getCreateName())){
			params.put("createName", accRecordsVO.getCreateName());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getRmk())){
			params.put("rmk", accRecordsVO.getRmk());
		}
		if(accRecordsVO.getInsideEndorseAutoRegSign() != null){
			params.put("insideEndorseAutoRegSign", accRecordsVO.getInsideEndorseAutoRegSign());
		}
		if(StringUtils.isNotEmpty(accRecordsVO.getExcId())){
			params.put("excId", accRecordsVO.getExcId());
		}
		params.put("updateDate", new Date());

		abAccrecordsDao.flush();
		abAccrecordsDao.clear();
		return abAccrecordsDao.batchUpdate(params);
	}
 	/**
 	 * 批量修改validSign操作
 	 * @param ids 流水表ID
 	 * @param validSign 有效标志
 	 * @return
 	 */
 	public int batchUpdateValidSign(String[] ids,String validSign,ICurrentUser userVo){
 		return batchUpdate(ids, null, AccebillConstant.AB_BILL_BUS_STATUS95, validSign,userVo);
 	}
	/**
	 * 更新申请日期及业务办理日期
	 * @param id 流水id
	 * @return
	 */
	public int updateApplyDateById(String id,ICurrentUser userVo){
		if(StringUtils.isEmpty(id)){
			return 0;
		}
		Map<String, Object> params=new HashMap<>();
		params.put("id", id);
		params.put("applyDate", new Date());
		params.put("businessDate", new Date());
		params.put("updateDate", new Date());
		if(userVo!=null){
			params.put("updateName", userVo.getUsername());
			params.put("updateBy", userVo.getLoginName());
		}
		return abAccrecordsDao.updateApplyDateById(params);
	}
 	/**
 	 * 修改validSign操作
 	 * @param id 流水表ID
 	 * @param validSign 有效标志
 	 * @return
 	 */
 	public int updateValidSign(String id,String validSign,ICurrentUser userVo){
			String[] ids={id};
 		return batchUpdateValidSign(ids, validSign,userVo);
 	}
 	/**
 	 * 批量修改Status操作
 	 * @param ids 流水表ID
 	 * @param statusNew 流水表新状态
 	 * @param statusOld 流水表旧状态
 	 * @return
 	 */
 	public int batchUpdateStatus(String[] ids,Integer statusNew,Integer statusOld,ICurrentUser userVo){
		return batchUpdate(ids, statusNew, statusOld, null,userVo);
 	}
 	/**
 	 * 批量删除
 	 * @param ids
 	 * @param statusOld 老状态
 	 */
 	public int batchDelete(String[] ids,Integer statusOld,ICurrentUser userVo){
 		//业务处理状态： 已删除
 		int num = batchUpdate(ids,AccebillConstant.AB_BILL_BUS_STATUS_2,statusOld,null,userVo);
 		return num;
 	}
 	/**
	* 批量删除
	* @param ids
	* statusOld老状态 默认为审批中11
	*/
	public int batchDelete(String[] ids,ICurrentUser userVo){
		//业务处理状态： 已删除
		int num = batchUpdate(ids,AccebillConstant.AB_BILL_BUS_STATUS_2,AccebillConstant.AB_BILL_BUS_STATUS11,null,userVo);
		return num;
	}
	/**
	 * 批量提交开票登记流水信息
 	 * @param ids 流水表ID
	 */
	public int batchSubmit(String[] ids,ICurrentUser userVo){
		int num = batchUpdateStatus(ids,AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS0,userVo);
		return num;
	}
	/**
	 * 批量审批开票登记流水信息
	 * @param ids
	 * @param status 流水表新状态(审批完成待记账94业务完成95、审批打回-1、审批打回删除-2)
	 */
	public int batchApprove(String[] ids,Integer status,ICurrentUser userVo){
		int num = batchUpdateStatus(ids,status,null,userVo);
		return num;
	}
	/**
	* 审批流水信息
	* @param id
 	 * @param status 流水表新状态(业务完成95、审批打回-1、审批打回删除-2)
	*/
	public int approve(String id,Integer status,ICurrentUser userVo){
		String[] ids={id};
		return batchApprove(ids, status,userVo);
	}
	/**
	 * 批量审批通过或者指令打回完成以后修改前一笔流水的有效状态为  isPassOK为true值2 为false值1
	 * @param id
	 * @param isPassOK 审批通过标志 true为审批通过，false为指令打回
	 */
	public int updatAppPass(String id,boolean isPassOK,ICurrentUser userVo){
		String[] ids={id};
		return batchAppPass(ids,isPassOK,userVo);
	}
	/**
	* 批量审批通过或者指令打回完成以后修改前一笔流水的有效状态为  isPassOK为true值2 为false值1
	* @param ids
	* @param isPassOK 审批通过标志 true为审批通过，false为指令打回
	*/
	public int batchAppPass(String[] ids,boolean isPassOK,ICurrentUser userVo){
		String validSign=isPassOK?AccebillConstant.AB_BILL_VALID_SIGN_2:AccebillConstant.AB_BILL_VALID_SIGN_1;
		int num = batchUpdateValidSign(ids,validSign,userVo);
		return num;
	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(AbAccrecords entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbAccrecords entity,ICurrentUser userVo){
		accrecordsInit(entity, false,userVo);
		super.updateEntity(entity);
		abAccrecordsDao.flush();
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(AbAccrecords entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<AbAccrecords, String> getEntityDao() {
		return abAccrecordsDao;
	}
	/**
	* 批量新增流水信息
	* 发送指令为空时候：自动设置是否发送指令 但指令需要发送时根据账户自动设置发送人的信息（接收人信息需要其他业务自己补充完整）
	* 自动设置流水的操作状态
	* @param accrecords 票据流水信息
	*/
	public boolean saveAccrecords(AbAccrecords accrecords,ICurrentUser userVo){
		List<AbAccrecords> accrecordsList=new ArrayList<AbAccrecords>();
		accrecordsList.add(accrecords);
		int reSize=batchSaveAccrecords(accrecordsList,userVo);
		if(reSize!=1){
			return false;
		}
		abAccrecordsDao.flush();
		return true;
	}
 	/**
	* 批量新增流水信息
	* 发送指令为空时候：自动设置是否发送指令 但指令需要发送时根据账户自动设置发送人的信息（接收人信息需要其他业务自己补充完整）
	* 自动设置流水的操作状态
	* @param accrecordsList 票据流水表集合
	*/
	public int batchSaveAccrecords(List<AbAccrecords> accrecordsList,ICurrentUser userVo){
		for(AbAccrecords info:accrecordsList){
			accrecordsInit(info, true,userVo);
		}
		int num = abAccrecordsDao.batchSave(accrecordsList);
		return num;
	}
	/**
	 * 新增或者修改时数据的值初始化
	 * @param info 票据流水信息
	 * @param isSave 是否新增
	 */
	@Transactional(readOnly = true)
	public void accrecordsInit(AbAccrecords info,boolean isSave,ICurrentUser userVo){
		Date todayDay=new Date();
		//判断主表存不存在
		AbVoucherinfo voucherInfo = null;
		if(StringUtils.isNotEmpty(info.getVid())){
			voucherInfo=this.abVoucherinfoService.get(info.getVid(), true);
			if(voucherInfo!=null){
				info.setBillFlag(voucherInfo.getBillFlag());
				info.setLastCirculateCode(voucherInfo.getEbillBlCrclIndc());
				info.setLastBlBsnTpcd(voucherInfo.getBlBsnTpcd());
			}
		}
		BtBankAccDTO bankAccInfo=getBtBankAccInfo(info.getReqAcctId());
		if((AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(info.getBillFlag()))
				|| AbAccebillUtils.isBillTypeTP(voucherInfo!=null?voucherInfo.getBillType():null))
				&&info.getSendSign()==null){
			info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
		}
		//如果业务办理日期为空则取创建日期
		if(info.getBusinessDate()==null&&info.getApplyDate()!=null){
			info.setBusinessDate(info.getApplyDate());
//			info.setBusinessDate(DateUtils.getDateYMD(info.getApplyDate()));
		}
		if(info.getSendSign()==null){
			AbVoucherinfo vouInfo=info.getVouInfo();
			if(vouInfo==null){
				if(StringUtils.isNotEmpty(info.getVid())){
					vouInfo=abVoucherinfoService.get(info.getVid(), true);
					info.setVouInfo(vouInfo);
				}
			}
			//针对开票如果填了票据号则属于离线电票、收票登记、质押收回、贴现收回、 第三方票据类型也是属于离线票据
			if(vouInfo!=null
					&&StringUtils.isNotEmpty(vouInfo.getOrigNo())
					&&AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())
					||AccebillConstant.BRIEF_STATUS_6001.equals(info.getBriefId())
					||AccebillConstant.BRIEF_STATUS_6019.equals(info.getBriefId())
					||AccebillConstant.BRIEF_STATUS_6212.equals(info.getBriefId())
					||AbAccebillUtils.isBillTypeTP(vouInfo!=null?vouInfo.getBillType():null)){
				info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
			}else if(StringUtils.isNotEmpty(info.getReqAcctId())
					&&bankAccInfo!=null
					&& BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccInfo.getCirculationMode())
					&&BtBankAccConstant.ELECTRICBILL_YES.equals(bankAccInfo.getElectricBill())
					&&AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(info.getBillFlag()))){
				info.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
				info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
			}else{
				info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
			}
		}else if(AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())){
			info.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
		}
		CorpDTO corpInfo=null;
		String bifCode= "";
		if(bankAccInfo!=null && StringUtils.isNotEmpty(bankAccInfo.getCorpId())){
			corpInfo=abCorpService.get(bankAccInfo.getCorpId());
//			if(!AccebillConstant.BRIEF_STATUS_6001.equals(info.getBriefId())){
				info.setReqNm(bankAccInfo.getAccName());
				info.setReqAcctsvcr(bankAccInfo.getBankCode());
				// 冗余业务申请人账号ID
				info.setReqBankAccId(bankAccInfo.getId());
//			}else if(StringUtils.isEmpty(info.getRecNm())){
//				info.setRecNm(bankAccInfo.getAccName());
//				info.setRecAcctsvcr(bankAccInfo.getBankCode());
//			}
			bifCode=bankAccInfo.getBifCode();
			BisBifInitDTO bifInfo=abBisBifInitService.getObjectById(bifCode);
			if(bifInfo!=null){
				info.setBifCode(bifInfo.getBifCode());
			}
		}else{
			AbVoucherinfoVice viceInfo=info.getVouViceInfo();
			if(viceInfo==null && StringUtils.isNotEmpty(info.getVvid())){
				viceInfo = abVoucherinfoViceService.get(info.getVvid());
				if(StringUtils.isNotEmpty(viceInfo.getCorpId())){
					corpInfo=abCorpService.get(viceInfo.getCorpId());
				}
			}
		}
		if(corpInfo!=null){
//			if(!AccebillConstant.BRIEF_STATUS_6001.equals(info.getBriefId())){
				if(StringUtils.isEmpty(info.getReqNm())){
					info.setReqNm(corpInfo.getName());
				}
			info.setReqSocNo(StringUtils.isEmpty(corpInfo.getSocCode()) ? corpInfo.getSocCode() : info.getReqSocNo());
				info.setReqCmonid(getCommId(corpInfo));
				String commId=corpInfo.getAccountLicence();
				if(AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())
						&&(StringUtils.isEmpty(commId)||!(commId.length()==10||commId.length()==18))){
//					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_COMMIDFALSE);
				}
//			}else{
//				if(StringUtils.isEmpty(info.getRecNm())){
//					info.setReqNm(corpInfo.getName());
//				}
//				info.setReqCmonid(getCommId(corpInfo));
//			}
		}
		if(info.getApplyDate()==null) {
			info.setApplyDate(todayDay);
		}
		info.setReqRole(getRole(info.getReqNm(), info.getReqAcctId(), null));
		info.setRecRole(getRole(info.getRecNm(), info.getRecAcctId(), null));
		//设置接收人统一社会信用代码
		if(StringUtils.isEmpty(info.getRecSocNo())){
			if(StringUtils.isNotEmpty(info.getRecAcctId())){
				BtBankAccDTO recBankAccInfo=getBtBankAccInfo(info.getReqAcctId());
				if(recBankAccInfo != null){
					CorpDTO recCorpDto = abCorpService.get(recBankAccInfo.getCorpId());
					info.setRecSocNo(StringUtils.isEmpty(info.getRecSocNo()) ? (recCorpDto == null ? null : recCorpDto.getSocCode()) : info.getRecSocNo());
				}else {
					DataTablesInput dataTablesInput = new DataTablesInput();
					Map<String, Object> map = new HashMap<>();
					map.put("externalAcc",info.getRecAcctId());
					dataTablesInput.setFormdata(map);
					ExternalCorpBankAccDetailDto dto = iExternalCorpService.listByExternalDetailForBill(dataTablesInput);
					if(ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getSocCode())){
							info.setRecSocNo(dto.getSocCode());
					}
				}
			}
		}
		//如果新票业务，但是业务银行接口不支持新票据功能，设置不发送指令
		if(StringUtils.isNotBlank(info.getBifCode())){
			AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.getDataByBifCode(bifCode);
			if("1".equals(voucherInfo.getNewStmIssuofpprInd()) && (abBillBankAutoDtl == null || "0".equals(abBillBankAutoDtl.getNewBillFlg()))){
				info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
			}
		}
		if(AccebillConstant.AB_BILL_SEND_SIGN_Y.equals(info.getSendSign())
				&&!String.valueOf(info.getBriefId()).startsWith("6")){
			//写入接口需要发送的消息号 每次修改重新生成新的消息号
			info.setMsgId(AbAccebillUtils.getMsgId());
			if(StringUtils.isEmpty(info.getOptStatus())||AccebillConstant.AB_BILL_OPT_STATUS_2.equals(info.getOptStatus())) {
				info.setOptStatus(getOtherOptStatus());
			}
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(info.getBillFlag()))
					&& !AbAccebillUtils.isBillTypeTP(abVoucherinfoService.get(info.getVid()).getBillType())){
				if(bankAccInfo==null){
		 			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLNO);
				}else if(!BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccInfo.getCirculationMode())){
		 			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISBILLONLINE);
				}
				//写入接口需要发送的消息号 每次修改重新生成新的消息号
	//			if(StringUtils.isEmpty(info.getMsgId())){
	//				info.setMsgId(AbAccebillUtils.getMsgId());
	//			}
				//流水表写入外部系统票据ID
				if(StringUtils.isEmpty(info.getBillId())&&(StringUtils.isNotEmpty(info.getLastid()) || "023".equals(info.getBriefId()))){
					AbAccrecords lastInfo = null;
					if(StringUtils.isNotEmpty(info.getLastid())){
						lastInfo=this.get(info.getLastid(), true);
					}
					if("023".equals(info.getBriefId())){
						lastInfo = abAccrecordsDao.getBillByVouchid(info.getVid(),"022");
					}
					if(lastInfo!=null&&StringUtils.isNotEmpty(lastInfo.getBillId())){
						info.setBillId(lastInfo.getBillId());
					}
				}
			}
		}else{
			if (!(AccebillConstant.BRIEF_STATUS_013.equals(info.getBriefId())) ) {
				info.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
			}
		}
		//业务办理完成是否选择完成,为是则不需要发送指令
		if(AccebillConstant.BUSINESS_IS_OVER_YES.equals(info.getBusinessIsOver())){
			info.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
			info.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
		}
		if(isSave){
			if(info.getCreateDate()==null){
				info.setCreateDate(new Date());
			}
			if(!AccebillConstant.AB_BILL_ISCREATEBY_NULL.equals(info.getIsCreateByNull())){
				if(StringUtils.isEmpty(info.getCreateBy())&&userVo!=null){
					info.setCreateBy(userVo.getLoginName());
				}
				if(StringUtils.isEmpty(info.getCreateName())&&userVo!=null){
					info.setCreateName(userVo.getUsername());
				}
			}
		}else{
			info.setUpdateDate(todayDay);
			if(userVo!=null){
				info.setUpdateBy(userVo.getLoginName());
				info.setUpdateName(userVo.getUsername());
			}
			if(StringUtils.isEmpty(info.getCreateBy())&&userVo!=null){
				info.setCreateBy(userVo.getLoginName());
			}
			if(StringUtils.isEmpty(info.getCreateName())&&userVo!=null){
				info.setCreateName(userVo.getUsername());
			}
		}
		//设置每笔流水的所属库箱标记
		if(StringUtils.isEmpty(info.getEnterStockOld())&&StringUtils.isNotEmpty(info.getVvid())){
			AbVoucherinfoVice viceInfo=abVoucherinfoViceService.get(info.getVvid(),true);
			if(viceInfo!=null&&StringUtils.isNotEmpty(viceInfo.getEnterStock())){
				info.setEnterStockOld(viceInfo.getEnterStock());
			}
		}
		//针对有新增流水表数据的操作且存在原流水的必须保证原流水有效
		if(isSave && StringUtil.isNotEmpty(info.getLastid())
				&& (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_011.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_6011.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())
				||AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId()))){
			AbAccrecords lastInfo=this.get(info.getLastid(),true);
			if(lastInfo==null||
					(!AccebillConstant.AB_BILL_VALID_SIGN_1.equals(lastInfo.getValidSign())
					&& !AccebillConstant.BRIEF_STATUS_6201.equals(lastInfo.getBriefId())
					&& !AccebillConstant.BRIEF_STATUS_6202.equals(lastInfo.getBriefId())
					&& !AccebillConstant.BRIEF_STATUS_6203.equals(lastInfo.getBriefId())
					&& !AccebillConstant.BRIEF_STATUS_6204.equals(lastInfo.getBriefId())
					&& !AccebillConstant.BRIEF_STATUS_014.equals(lastInfo.getBriefId()))){
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		}
	}
	/**
	 * 获取提交的默认流水状态
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getOtherOptStatus(){
		String isEAutoInstruct  =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISEAUTOINSTRUCT).toFormatString();
		if("1".equals(isEAutoInstruct)){
			return AccebillConstant.AB_BILL_OPT_STATUS16;
		} else{
			return AccebillConstant.AB_BILL_OPT_STATUS15;
		}
	}
	/**
	 * 根据单位信息获取组织机构代码
	 * @param corpInfo
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getCommId(CorpDTO corpInfo){
		String commId=corpInfo.getAccountLicence();
//		123456789012345678
//		90123456-7
		if(StringUtils.isNotEmpty(commId)&&commId.length()==18){
			commId=commId.substring(8,16)+"-"+commId.charAt(16);
		}
		return commId;
	}
	/**
	 * 根据接收人和账户判断所属角色类型
	 * @param name
	 * @param bankAcc
	 * @param bankName
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getRole(String name,String bankAcc,String bankName){
		if(StringUtils.isEmpty(name)){
			return null;
		}
		// 参与者类别RC00接入行RC01企业RC02人民银行RC03被代理行RC04被代理财务公司RC05接入财务公司
		if("0".equals(bankAcc)){
			if(StringUtils.isNotEmpty(name) && name.indexOf("财务有限")!=-1){
				return AccebillConstant.AB_BILL_ROLE_FC;
			}else if(StringUtils.isNotEmpty(name) && name.indexOf("中国人民银行")==0){
				return AccebillConstant.AB_BILL_ROLE_CB;
//			}else if(name.equals(bankName)){
//				return AccebillConstant.AB_BILL_ROLE_BANK;
//			}else{
//				return AccebillConstant.AB_BILL_ROLE_AGENCY_BANK;
//			}
			}else{
				return AccebillConstant.AB_BILL_ROLE_BANK;
			}
		}else{
			if(StringUtils.isNotEmpty(name) &&  name.indexOf("财务有限")!=-1){
				return AccebillConstant.AB_BILL_ROLE_AGENCY_FC;
			}else{
				return AccebillConstant.AB_BILL_ROLE_CORP;
			}
		}
	}
	/**
	 * 根据账号获取账户信息 如果不存在抛异常
	 * @param bankAcc 银行账号
	 * @return 账户信息
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccDTO(String bankAcc){
		return  getBtBankAccInfo(bankAcc);
//		if(bankAccDTO==null){
//			// throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
//			bankAccDTO = getBankAccByAcc(bankAcc);
//		}
//		return bankAccDTO;
	}
//
//	public static List<BtBankAccDTO> getList(){
//		JSONArray json = JSONUtil.readJSONArray(new File("C:\\pzc\\public\\T6\\挡板文件\\bankAccList.json"), StandardCharsets.UTF_8);
//		return json.toList(BtBankAccDTO.class);
//	}
//	public static BtBankAccDTO getBankAccByAcc(String bankAcc){
//		List<BtBankAccDTO> bankAccList = getList();
//		List<BtBankAccDTO> collect = bankAccList.stream().filter(bankAccDto -> StrUtil.equals(bankAccDto.getBankAcc(),bankAcc)).collect(Collectors.toList());
//		return ObjectUtil.isNotEmpty(collect) ? collect.get(0) : null;
//	}
	/**
	 * 根据账号获取账户信息 如果不存在返回null
	 * @return
	 */
	@Transactional(readOnly = true)
	public BtBankAccDTO getBtBankAccInfo(String bankAcc){
		if(StringUtils.isEmpty(bankAcc)){
			return null;
		}
		BtBankAccDTO bankAccDTO=null;
		BtBankAccDTO params=new BtBankAccDTO();
		params.setBankAcc(bankAcc);
		params.setElectricBill(BtBankAccConstant.ELECTRICBILL_YES);
		List<BtBankAccDTO> bankAccList= abBtBankAccService.getBankAccList(params);
		if(bankAccList!=null&&!bankAccList.isEmpty()){
			bankAccDTO=bankAccList.get(0);
		}
//		if (bankAccDTO == null){
//			return getBankAccByAcc(bankAcc);
//		}
		return bankAccDTO;
	}
	/**
	 * 根据lastIds设置有效标志为1(有效)的数据设置为2（结束）
	 * @return 返回修改条数
	 */
	public int setAbAccrecordsValidSign2ByLastIdAndValidSign1(String[] lastIds,ICurrentUser userVo){
 		if(StringUtils.isEmpty(lastIds)){
 			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
 		}
		Map<String,Object> params=new HashMap<>();
		params.put("lastIds", lastIds);
		if(userVo!=null){
	 		params.put("updateName", userVo.getUsername());
	 		params.put("updateBy", userVo.getLoginName());
		}
 		params.put("updateDate", new Date());
		return abAccrecordsDao.setAbAccrecordsValidSign2ByLastIdAndValidSign1(params);
	}
	/**
	 * 根据流水ID设置记账唯一ID
	 * @param id 流水ID
	 * @param gusGlobalId 记账唯一ID
	 * @return 返回修改成功数量
	 */
	public int setBusGlobalIdById(String id,String gusGlobalId,ICurrentUser userVo){
		AbAccrecordsVO accrecordsVO=new AbAccrecordsVO();
		String[] idArray={id};
		accrecordsVO.setIdArray(idArray);
		accrecordsVO.setBusGlobalId(gusGlobalId);
		return batchUpdate(accrecordsVO,userVo);
	}
	/**
	 * 根据签收表id获取流水信息
	 * @param recId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> listAbAccrecordsByRecId(String recId){
		return abAccrecordsDao.listAbAccrecordsByRecId(recId);
	}
	/**
	 * 根据lastid获取流水信息
	 * @param lastid
	 * @return List<AbAccrecords>
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> getAbAccrecordsbyLastid(String lastid){
		return abAccrecordsDao.getAbAccrecordsbyLastid(lastid);
	}
	/**
	 * 根据id或者pdId获取流水信息,创建时间升序
	 * @param id
	 * @param pdId 票据池明细id
	 * @return List<AbAccrecords>
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecordsVO> getAbAccrecordsbyIdOrPdId(String id,String pdId){
		List<AbAccrecordsVO> list = abAccrecordsDao.getAbAccrecordsbyIdOrPdId(id,pdId);
		Optional.ofNullable(list).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE","AB_BILL_FLAG", "AB_BRIEF_STATUS", "AB_BILL_INSOPTSTATUS");
			for (AbAccrecordsVO bill : listData){
				this.injectValues(bill ,listDictMap);
			}
		});
		return list;
	}

	private void injectValues(AbAccrecordsVO bill, Map<String, Map<String, DictDTO>> listDictMap){
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
		// 开出方式
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing+"")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});

		// 业务状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 指令状态
		Optional.ofNullable(bill.getInoptStatus()).ifPresent(inoptStatus -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(inoptStatus)))
					.ifPresent(dict -> bill.setInstructStatusName(dict.getName()));
		});

		//出票标志: 1 新系统出票 、0 老系统出票
		Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
					.ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
		});
		//是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
		Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
					.ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
		});

	}
	/**
	 * 获取需要设置余额的电票业务
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecordsVO> getElectBusBillInfoByBal(){
		return abAccrecordsDao.getElectBusBillInfoByBal();
	}
	/**
	 * 设置质押未解除已到期截止票据业务
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecordsVO> getPledgeBusBillInfoByBal(){
		return abAccrecordsDao.getPledgeBusBillInfoByBal();
	}
	/**
	 * 获取下拨数据流水信息
	 * @param trid 下拨数据流水信息
	 * @return 返回下拨数据流水信息
	 */
	@Transactional(readOnly = true)
	public AbAccrecords getTransferAccrecordsInfoByTrid(String trid){
		if(StringUtils.isEmpty(trid)){
			return null;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("trid", trid);
		List<AbAccrecords> trList=abAccrecordsDao.getAbAccrecordsbyParams(params);
		if(trList!=null&&!trList.isEmpty()){
			return trList.get(0);
		}
		return null;
	}
	/**
	 * 获取下拨数据流水信息
	 * @param ids 下拨数据流水信息
	 * @return 返回下拨数据流水信息
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecords> getAccrecordsInfoByIds(String[] ids){
		if(StringUtils.isEmpty(ids)){
			return null;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("ids", ids);
		List<AbAccrecords> infoList=abAccrecordsDao.getAbAccrecordsbyParams(params);
		return infoList;
	}
	/**
	 * 根据传递流水ID获取本单位此流水前上一笔此票据流水Id信息
	 * @param recordId  流水ID
	 * @return 上一笔流水信息
	 */
	public String getUpBusinessByRecordId(String recordId){
		String upRecordId=abAccrecordsDao.getUpBusinessByRecordId(recordId);
		return  upRecordId;
	}
	/**
	 * 先根据电票账号获取单位信息，账号为空再根据单位名称获取单位信息
	 * @param bankAcc 电票账号
	 * @param corpName 单位名称
	 * @return 单位信息
	 */
	public CorpDTO getCorpInfo(String bankAcc,String corpName){
		CorpDTO otherCorpInfo=null;
		if(StringUtils.isNotEmpty(bankAcc)){
			BtBankAccDTO bankAccInfo=this.getBtBankAccInfo(bankAcc);
			if(bankAccInfo!=null){
				otherCorpInfo=abCorpService.get(bankAccInfo.getCorpId());
			}else if(StringUtils.isNotEmpty(corpName)){
				otherCorpInfo=abCorpService.getCorpIdByName(corpName);
			}
		}else if(StringUtils.isNotEmpty(corpName)){
			otherCorpInfo=abCorpService.getCorpIdByName(corpName);
		}
		return otherCorpInfo;
	}
	@Transactional(readOnly = true)
	public AbAccrecords getAbAccrebyLastid(String lastid) {
		return abAccrecordsDao.getAbAccrebyLastid(lastid);
	}
	@Transactional(readOnly = true)
	public AbAccrecords getBillByVid(String vvid,String briefId) {
		return abAccrecordsDao.getBillByVid(vvid,briefId);
	}
	@Transactional(readOnly = true)
	public AbAccrecords getBillByvid(String vid,String briefId) {
		return abAccrecordsDao.getBillByVouchid(vid,briefId);
	}
	/**
	 * 根据从表id和票据池交易明细id获取流水信息  创建日期降序
	 * @param vvid 从表id
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbAccrecords getBillByPdIdAndVvId(String vvid,String pdId) {
		if(StringUtils.isEmpty(vvid) || StringUtils.isEmpty(pdId)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		return abAccrecordsDao.getBillByPdIdAndVvId(vvid,pdId,true);
	}
	/**
	 * 通过从表Id获取对应业务类型最近一笔正常出去业务的流水
	 * @param vvid
	 * @param briefId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbAccrecords getLastAbAccrecordsByParams(String vvid,String briefId) {
		return abAccrecordsDao.getLastAbAccrecordsByParams(vvid, briefId);
	}
	/**
	 * 根据从表id和票据池交易明细id获取流水信息  创建日期升序
	 * @param vvid 从表id
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbAccrecords getBillByPdIdAndVvIdOrderByAsc(String vvid,String pdId) {
		if(StringUtils.isEmpty(vvid) || StringUtils.isEmpty(pdId)){
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
		}
		return abAccrecordsDao.getBillByPdIdAndVvId(vvid,pdId,false);
	}
	/**
     * 修改流水信息
     * @param refMap
     * @param listIds
     * @param userVo
     * @return
     */
    public int updateRecordByPdId(Map<String,Object> refMap,List<String> listIds, ICurrentUser userVo){
        Map<String, Object> params=new HashMap<String, Object>();
        if(refMap.containsKey("status")){
        	params.put("status", refMap.get("status"));
        }
        if(refMap.containsKey("optStatus")){
        	params.put("optStatus", refMap.get("optStatus"));
        }
        if(refMap.containsKey("updateId")){
        	params.put("updateId", refMap.get("updateId"));
        }
        if(refMap.containsKey("validSign")){
        	params.put("validSign", refMap.get("validSign"));
        }
        if(refMap.containsKey("sid")){
        	params.put("sid", refMap.get("sid"));
        }
        if(refMap.containsKey("sendSign")){
        	params.put("sendSign", refMap.get("sendSign"));
        }
        if(refMap.containsKey("returnCode")){
        	params.put("returnCode", refMap.get("returnCode"));
        }
		if(refMap.containsKey("pdId")){
			params.put("pdId", refMap.get("pdId"));
		}
        if(refMap.containsKey("returnMsg")){
        	params.put("returnMsg", refMap.get("returnMsg"));
        }
        params.put("updateTime", new Date());
        return abAccrecordsDao.updateRecordByParams(params, listIds);
    }
	/**
	 * 根据票据池交易明细pdId修改流水状态
	 * @param refMap
	 * @param userVo
	 * @return
	 */
	public int updateStatusByPdId(Map<String,Object> refMap, ICurrentUser userVo){
		Map<String, Object> params=new HashMap<String, Object>();
		if(refMap.containsKey("pdId") && StringUtils.isNotEmpty(refMap.get("pdId"))){
			params.put("pdId", refMap.get("pdId"));
		}else{
			return 0;
		}
		if(refMap.containsKey("status")){
			params.put("status", refMap.get("status"));
		}
		if(refMap.containsKey("optStatus")){
			params.put("optStatus", refMap.get("optStatus"));
		}
		if(refMap.containsKey("validSign")){
			params.put("validSign", refMap.get("validSign"));
		}
		if(userVo!=null){
			params.put("updateName", userVo.getUsername());
			params.put("updateBy", userVo.getLoginName());
		}
		params.put("updateDate", new Date());
		return abAccrecordsDao.updateStatusByPdId(params);
	}
	/**
	 * 获取线上票据应付未结清、应收持有业务数据
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecordsVO> getAbAccrecordsVOByHold(boolean isExpire) {
		List<AbAccrecordsVO> recordList = abAccrecordsDao.getAbAccrecordsVOByHold(isExpire);
		if (ObjectUtil.isNotEmpty(recordList)) {
			Set<String> bankAccList = recordList.stream().filter(info->StringUtils.isNotEmpty(info.getBankAcc())).map(AbAccrecordsVO::getBankAcc).collect(Collectors.toSet());
            if(CollectionUtil.isEmpty(bankAccList)){
                return CollectionUtil.newArrayList();
            }
			// 过滤掉 electric_bill='1' and circulation_mode='1'的账户数据
			List<Map<String, Object>> bankModelList = abBtBankAccService.getBankModelByBankAcc(CollectionUtil.newArrayList(bankAccList));
            if(CollectionUtil.isEmpty(bankModelList)){
                return CollectionUtil.newArrayList();
            }
			recordList = recordList.stream().filter(record -> bankModelList.stream()
							.anyMatch(target -> ObjectUtil.equal(record.getBankAcc(), Convert.toStr(target.get("bankAcc")))))
					.collect(Collectors.toList());
		}
		return recordList;
	}

	/**
	 * 获取线上票据应付未结清、应收持有业务数据
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbAccrecordsVO> getAbAccrecordsVOByHold(){
		return getAbAccrecordsVOByHold(false);
	}
	/**
	 * 取消，业务回滚
	 * @param aids 流水表IDs
	 */
	public void backBillBrief(String aids, ICurrentUser userVo) {
		String[] abAccrecordsIds = aids.split(",");
		// 修改当前流水为有效
		this.batchUpdateValidSign(abAccrecordsIds, AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
		// 根据lastId删除流水
		this.deleteByLastId(abAccrecordsIds);
	}
	/**
	 * 根据数据生成流水并将业务改为完成
	 * @param aids 流水表ID
	 * @param userVo 收款人
	 *
	 */
	public void completeAccrecord(String aids, ICurrentUser userVo) {
		String[] abAccrecordsIds = aids.split(",");
		AbAccrecords abAccrecordsOld = null;// 当前流水
		for (int i = 0; i < abAccrecordsIds.length; i++) {
			abAccrecordsOld = this.get(abAccrecordsIds[i],true);
			if(!EmptyUtils.isEmpty(abAccrecordsOld)){
				//线上清算不做贴现到账处理
				if(AccebillConstant.AB_STTLMMK_Y.equals(abAccrecordsOld.getSttlmmk())){
					continue;
				}
			}
			// 设置新流水信息
			AbAccrecords abAccrecordsNew = new AbAccrecords();
			this.setAbAccrecordsNew(abAccrecordsOld, abAccrecordsNew);
			// 保存新流水
			this.saveAccrecords(abAccrecordsNew, userVo);
			// 生成贴现表
			if(AccebillConstant.BRIEF_STATUS_6012.equals(abAccrecordsNew.getBriefId())) {
				AbDiscount discountNew = new AbDiscount();
				// 上一笔贴现信息
				AbDiscount discountLast = abDiscountService.getAbDiscountByRecordId(abAccrecordsOld.getId());
				discountNew.setRealMoney(discountLast.getRealMoney());
				discountNew.setBuyerinterestMoney(discountLast.getBuyerinterestMoney());
				discountNew.setInterestRates(discountLast.getInterestRates());
				discountNew.setInterest(discountLast.getInterest());
				discountNew.setRpdmk(discountLast.getRpdmk());
				discountNew.setSttlmmk(discountLast.getSttlmmk());
				discountNew.setRecordId(abAccrecordsNew.getId());
				abDiscountService.save(discountNew);
			}
			abBillNotificationService.completeMsg(abAccrecordsNew,userVo);
			abVoucherinfoViceService.updateBackBriefIdIsNull(abAccrecordsOld.getVvid());// 从表清除打回标记
			// 票据当前流水改为作废
			this.updatAppPass(abAccrecordsNew.getLastid(), true, userVo);
			// 保存日志信息
			abLogService.saveAbLog(abAccrecordsOld.getVvid(), abAccrecordsNew.getId(), abAccrecordsNew.getBriefId(),
					AccebillConstant.ABLOG_LOGTYPE_BATCHSUCCE, AccebillConstant.LANG_LOG_APP_SUCC, userVo);
		}
	}
	/**
	 * 根据数据生成新流水
	 * @param abAccrecordsOld 老流水
	 * @param abAccrecordsNew 新流水
	 *
	 */
	public void setAbAccrecordsNew(AbAccrecords abAccrecordsOld, AbAccrecords abAccrecordsNew) {
		abAccrecordsNew.setVid(abAccrecordsOld.getVid());// 票据主表ID
		abAccrecordsNew.setVvid(abAccrecordsOld.getVvid());// 票据从表ID
		abAccrecordsNew.setLastid(abAccrecordsOld.getId());// 上条流水ID
		switch (abAccrecordsOld.getBriefId()){
			case AccebillConstant.BRIEF_STATUS_020:
				abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6020);
				break;
			case AccebillConstant.BRIEF_STATUS_021:
				abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6020);
				break;
			case AccebillConstant.BRIEF_STATUS_011:
				abAccrecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6012);
				break;
		}
		abAccrecordsNew.setApplyDate(new Date());// 申请日期
		abAccrecordsNew.setBusinessDate(new Date());// 业务归属日期
		abAccrecordsNew.setCreateDate(new Date());// 创建日期
		abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		abAccrecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
		abAccrecordsNew.setReqAcctId(abAccrecordsOld.getRecAcctId());// 业务发起人账号
		//业务接收人信息
		abAccrecordsNew.setRecAcctId(abAccrecordsOld.getRecAcctId());
		abAccrecordsNew.setRecAcctsvcr(abAccrecordsOld.getRecAcctsvcr());
		abAccrecordsNew.setRecNm(abAccrecordsOld.getRecNm());
		abAccrecordsNew.setRecRole(abAccrecordsOld.getRecRole());
		abAccrecordsNew.setInsideSing(abAccrecordsOld.getInsideSing());
	}
	/**
	 * 根据vvid删除票据流水
	 * @param vvid
	 * @return
	 */
	public int deleteByVvId(String vvid){
		if(StringUtils.isNotEmpty(vvid)){
			return abAccrecordsDao.deleteByVvId(vvid);
		}
		return 0;
	}
	/**
	 * 根据lastId删除流水
	 * 用于收款流水恢复数据时使用
	 * @param
	 * @return
	 */
	public void deleteByLastId(String[] lastIds){
		if(StringUtils.isNotEmpty(lastIds)){
			abAccrecordsDao.deleteByLastId(lastIds);
		}
	}
	/**
	 * 根据流水查询票据号
	 * 用于收款流水认领
	 * @param aid 流水Id(必填)
	 * @return
	 */
	public AbEmitBillDTO selectBillInfo(String aid) {
		if(StringUtils.isEmpty(aid)){
			return null;
		}
		AbAccrecords abAccrecords = this.get(aid);
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
		return BeanUtils.copyNewInstanceProperties(abVoucherinfo, AbEmitBillDTO.class);
	}
	/**
	 * 根据签收表ID和流水表ID获取最近的票据流水表ID
	 * @param recid 签收表ID
	 * @param acid 需要去除的流水表ID
	 * @return
	 */
	public String getListidByRecordIdAndAcid(String recid,String acid){
		return abAccrecordsDao.getListidByRecordIdAndAcid(recid,acid);
	}
	public List<AbAccrecordsVO> abPoolRecordList(String ids){
		   Map<String,Object> params = new HashMap<String, Object>();
		   params.put("ids", Arrays.asList(ids.split(",")));
		List<AbAccrecordsVO> recordList = abAccrecordsDao.abPoolRecordList(params);
//		left join BT_BANK_ACC ba on ba.bank_acc = vv.bank_acc
		//通过 bank_acc 查询 BT_BANK_ACC 表获取 circulation_Mode 信息回填记录
		if (ObjectUtil.isNotNull(recordList)){
			List<String> queryBankAccList = recordList.stream().map(AbAccrecordsVO::getBankAcc).collect(Collectors.toList());
			List<Map<String, Object>> bankModelList = abBtBankAccService.getBankModelByBankAcc(queryBankAccList);
			Map<String, String> circulationModeMap = Maps.newHashMap();
			if (ObjectUtil.isNotEmpty(bankModelList)){
				for (Map<String, Object> objectMap : bankModelList) {
					if(ObjectUtil.isNotNull(objectMap.get("bankAcc"))){
						circulationModeMap.put(String.valueOf(objectMap.get("bankAcc")), String.valueOf(objectMap.get("circulationMode")));
					}
				}
			}
			if (!circulationModeMap.isEmpty()){
				for (AbAccrecordsVO record : recordList) {
					if (StringUtils.isNotBlank(circulationModeMap.get(record.getBankAcc()))) {
						record.setCirculationMode(circulationModeMap.get(record.getBankAcc()));
					}
				}
			}
		}
		return recordList;
	}
	/**
	 * 根据从表id获取所有流水id
	 * @param vvid 从表id
	 * @return 流水i
	 */
	public List<AbAccrecordsVO> listAbAccrecordsIdByVvId(String vvid){
		if(StringUtils.isNotEmpty(vvid)){
			return abAccrecordsDao.listAbAccrecordsIdByVvId(vvid);
		}
		return null;
	}
	/**
	 * 根据从表id和流水表状态获取所有流水id
	 * @param vvid 从表id
	 * @param status 流水表状态
	 * @return 流水i
	 */
	public List<AbAccrecordsVO> listAbAccrecordsIdByVvIdAndStatus(String vvid,Integer status){
		if(StringUtils.isNotEmpty(vvid)){
			return abAccrecordsDao.listAbAccrecordsIdByVvIdAndStatus(vvid,status);
		}
		return null;
	}
	/**
	 * 根据票据号银行接口为工商银行、线上外部背书业务完成的数据
	 * @param origNo 票据号
	 * @return 流水信息
	 */
	public List<AbAccrecordsVO> getAbAccrecordsVOByOrigNo(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn){
		if(StringUtils.isNotEmpty(origNo)){
			return abAccrecordsDao.getAbAccrecordsVOByOrigNo(origNo,sbbllRngStrtSn,sbbllRngEndSn);
		}
		return null;
	}

	/**
	 * 修改票据主表id为vid的应收票据流水6001的validSign为2
	 *
	 * @param vid 票据主表id
	 */
	public void updateWithEndorse(String vid) {
		abAccrecordsDao.updateWithEndorse(vid);
	}

	/**
	 * 定时任务查询待生成需求单的承兑到期
	 * @return
	 */
	public List<AbAccrecordsDTO> listTaskBillForNisDemand() {
		Date beginDate = DateUtils.getDateYMD();
		ConfigDTO dto = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITWARNDAYNUM);
		Integer day =  dto == null ? 10 : dto.toInteger(10);
		//查询指定天数工作日的日期
		Date endDate=calendarService.getWorkDay(beginDate,day);
		return abAccrecordsDao.listTaskBillForNisDemand(endDate);
	}

	/**
	 * 放在调用方法里的最后面调用
	 * @param id
	 * @param userVo
	 * @return TODO
	 */
	@Transactional
	public void updateApprover(String id, ICurrentUser userVo) {
		abAccrecordsDao.flush();
		abAccrecordsDao.clear();
		abAccrecordsDao.updateApprover(id,userVo);
	}
}
