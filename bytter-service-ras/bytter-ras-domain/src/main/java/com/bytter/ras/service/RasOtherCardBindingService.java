package com.bytter.ras.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasOtherCardBindingDao;
import com.bytter.ras.entity.RasOtherCardBinding;
import com.bytter.ras.vo.RasOtherCardBindingVO;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;

/**
 * 
* @Title:  营收稽核第三方账户绑定服务类   
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-6 下午4:20:08   
 */
@Service("rasOtherCardBindingService")
@Transactional
public class RasOtherCardBindingService extends BaseService<RasOtherCardBinding, String>
	implements IExcelVerifyHandler<RasOtherCardBindingVO> {

	@Autowired(required=false)
	private ICorpService corpService;
	
	@Autowired
	private RasLogService rasLogService;
	
    @Autowired
    private RasShopService rasShopService;
    
	@Autowired
	private RasOtherCardBindingDao rasOtherCardBindingDao;
	
    @Autowired
    private RasShopGrantService rasShopGrantService;
	/**
	 * 
	* @Title: getDataTablesUnion  
	* @Description: TODO(营收稽核第三方账户绑定查询列表)  
	* @param @param input
	* @param @param userVo
	* @param @param opQueryType
	* @param @return    参数  
	* @return DataTablesOutput    返回类型  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,String grantType,String resource) {
		 //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(resource,input.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(grantType, input.getCurrentUser());
		return rasOtherCardBindingDao.getDataTablesUnion(input,corpAuthList,shopAuthList);
	}
	
	/**
	 * 
	* @Title: baseSaveOrUpdate  
	* @Description: TODO(保存营收稽核第三方账户绑定信息)  
	* @param @param rasOtherCardBindingVO
	* @param @param operate
	* @param @param userVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson baseSaveOrUpdate(RasOtherCardBindingVO rasOtherCardBindingVO,String operate,ICurrentUser userVo){
		RasOtherCardBinding rasOtherCardBinding = new RasOtherCardBinding();
		AjaxJson reJson = new AjaxJson();
		if(isBindPayType(rasOtherCardBindingVO,operate)){
			reJson.setMessageEnum(RasMessageEnum.RAS_RASOTHERCARDBINDING_MESSAGE_REPEAT);
			reJson.setSuccess(false);
			return reJson;
		}
		rasOtherCardBinding.setCorpId(rasOtherCardBindingVO.getCorpId());
		rasOtherCardBinding.setShopId(rasOtherCardBindingVO.getShopId());
		rasOtherCardBinding.setAccNo(rasOtherCardBindingVO.getAccNo());
		rasOtherCardBinding.setAccName(rasOtherCardBindingVO.getAccName());
		rasOtherCardBinding.setPayType(rasOtherCardBindingVO.getPayType());
		rasOtherCardBinding.setRmk(rasOtherCardBindingVO.getRmk());
		//新增
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_SAVE)
				||operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT)){
			rasOtherCardBinding.setValidSign(RasConstant.RAS_VALID_SIGN_1);
			rasOtherCardBinding.setCreateBy(userVo.getLoginName());
			rasOtherCardBinding.setCreateDate(new Date());
			rasOtherCardBinding.setCreateName(userVo.getUsername());
			rasOtherCardBindingDao.save(rasOtherCardBinding);
			if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT))
				rasLogService.saveRasLog(rasOtherCardBinding.getId(), RasConstant.RAS_LOGTYPE_IMP,RasConstant.RAS_BANK_SAVE_IMPORT, userVo);
			else
				rasLogService.saveRasLog(rasOtherCardBinding.getId(), RasConstant.RAS_LOGTYPE_ADD,RasConstant.RAS_BANK_SAVE_SUCC, userVo);
		} 
		//修改
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)){
			rasOtherCardBindingVO.setId(rasOtherCardBindingVO.getId());
			rasOtherCardBindingVO.setValidSign(rasOtherCardBindingVO.getValidSign());
			rasOtherCardBindingVO.setUpdateBy(userVo.getLoginName());
			rasOtherCardBindingVO.setUpdateDate(new Date());
			rasOtherCardBindingVO.setUpdateName(userVo.getUsername());
			rasOtherCardBindingDao.updateRasOtherCardBinding(rasOtherCardBindingVO);
			rasLogService.saveRasLog(rasOtherCardBinding.getId(), RasConstant.RAS_LOGTYPE_UPDATE,RasConstant.RAS_BANK_UPDATE_SUCC, userVo);
		}
		reJson.setMessageEnum(RasMessageEnum.RAS_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
	}
	
	/**
	 * 
	* @Title: getRasOtherCardBindingVOSign  
	* @Description: TODO(查询第三方绑定单条详细信息)  
	* @param @param id
	* @param @return    参数  
	* @return RasOtherCardBindingVO    返回类型  
	* @throws
	 */
	@Transactional(readOnly = true)
    public RasOtherCardBindingVO getRasOtherCardBindingVOSign(String id) {
        if (!StringUtils.isEmpty(id)) {
            List<RasOtherCardBindingVO>  rasOtherCardBindingVO = rasOtherCardBindingDao.getRasOtherCardBindingVoSign(id);
            if(null!=rasOtherCardBindingVO && rasOtherCardBindingVO.size()>0){
                return rasOtherCardBindingVO.get(0);
            }
        }
        return null;
    }
	
	/**
	 * 
	* @Title: batchOperation  
	* @Description: TODO(批量启用/禁用)  
	* @param @param ids
	* @param @param operate
	* @param @param userVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson batchOperation(String ids,String operate,ICurrentUser userVo){
		AjaxJson reJson = new AjaxJson();
		if(StringUtils.isNotEmpty(ids)){
			try{
				String[] idArray=StringUtils.split(ids,",");
				for(int i=0;i<idArray.length;i++){
					if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_OPEN)){
						rasOtherCardBindingDao.updateValidSign(idArray[i], RasConstant.RAS_VALID_SIGN_1);
						rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_START,RasConstant.RAS_BANK_UPDATE_START, userVo);
					}else{ 
						rasOtherCardBindingDao.updateValidSign(idArray[i], RasConstant.RAS_VALID_SIGN_0);
						rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_STOP,RasConstant.RAS_BANK_UPDATE_ENABLE, userVo);
					}
				}
				reJson.setMessageEnum(RasMessageEnum.RAS_MESSAGE_SUCCESS);
				reJson.setSuccess(true);
			}catch(Exception e){
				reJson.setMessageEnum(RasMessageEnum.RAS_MESSAGE_ERROR);
				reJson.setSuccess(false);
			}
		}
		return reJson;
	}

	/**
	 * 根据参数获取第三方账号绑定数据
	 * @param params
	 * @return
	 */
	public List<RasOtherCardBinding> getRasOtherCardBindingByParams(Map<String,Object> params){
		return rasOtherCardBindingDao.getRasOtherCardBindingByParams(params);
	}

	/**
	 * 
	* @Title: saveImport  
	* @Description: TODO(第三方账户信息绑定)  
	* @param @param list
	* @param @param userVo
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public boolean saveImport(List<RasOtherCardBindingVO> list, ICurrentUser userVo){
		for (int i=0;i<list.size();i++) {
			RasOtherCardBindingVO rasOtherCardBindingVO = list.get(i);
			RasOtherCardBinding entity=new RasOtherCardBinding();
			BeanUtils.copyProperties(rasOtherCardBindingVO, entity);
			boolean flag = isBindPayType(rasOtherCardBindingVO,RasConstant.RAS_BANK_OPERATE_STATUS_SAVE);
			if(flag){
				throw new BusinessException(RasMessageEnum.RAS_RASOTHERCARDBINDING_MESSAGE_REPEAT,String.valueOf(i+1));
			}
			this.baseSaveOrUpdate(rasOtherCardBindingVO, RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT, userVo);
		}
		return true;
	}
	
	@Override
	public ExcelVerifyHanlderResult verifyHandler(RasOtherCardBindingVO obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<RasOtherCardBindingVO> list) {
		if (list != null && !list.isEmpty()) {
			final List<String> dicList = rasOtherCardBindingDao.queryDictList(RasConstant.RAS_OTHER_CARD_DICT);
			List<String> errorMsgList = null;
			CorpDTO corpDTO = null;
			RasShopVO rasShopVO = null;
			for (RasOtherCardBindingVO info : list) {
				errorMsgList = new ArrayList<String>();
				/** 单位代码和单位名称校验 */
				if (StringUtils.isEmpty(info.getCorpCode()) && StringUtils.isEmpty(info.getCorpName())) {
					errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
				} else {
					if (StringUtils.isNotEmpty(info.getCorpCode())) {
						// 根据所属单位代码获取单位信息
						corpDTO = corpService.getCorpByCode(info.getCorpCode());
						if (corpDTO != null) {
							if (StringUtils.isNotEmpty(info.getCorpName())
									&& !info.getCorpName().equals(corpDTO.getName())) {
								errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORPNAME_ISNOT);
							} else {
								info.setCorpName(corpDTO.getName());
							}
						} else {
							errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE__CORPCODE_ERROR);
						}
					} else {
						// 只填单位名称
						corpDTO = corpService.getCorpIdByName(info.getCorpName());
						if (corpDTO == null) {
							errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_CORPID_ISNOTEXIST);
						} else {
							info.setCorpCode(corpDTO.getCode());
						}
					}
				}
				/** 获取corpId */
				if (corpDTO != null) {
					info.setCorpId(corpDTO.getId());
				}
				// 单位不为空检测门店信息
				if (StringUtils.isNotEmpty(info.getCorpId())) {
					/** 门店编码和门店名称校验 */
					if (StringUtils.isEmpty(info.getShopCode()) && StringUtils.isEmpty(info.getShopName())) {
						errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_CODEANDNAME_NOTNULL);
					} else {
						if (StringUtils.isNotEmpty(info.getShopCode())) {
							// 根据门店编码和单位id获取门店信息
                            rasShopVO = rasShopService.getRasShopByCode(info.getShopCode(), info.getCorpId());
								// 判断门店是否启用
							if (rasShopVO != null) {
								if (RasConstant.RAS_VALID_SIGN_1.equals(rasShopVO.getvSign())) {
									if (StringUtils.isNotEmpty(info.getShopName())
											&& !info.getShopName().equals(rasShopVO.getName())) {
										errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_CODNAME_ISNOT);
									} else {
										info.setShopName(rasShopVO.getName());
									}
								} else {
									errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_VALIDSIGN_ISNOT);
								}
							} else {
								errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPCODE_ERROR);
							}
						} else {
							// 只填名门店名称
							// 根据门店名称获取门店信息
							rasShopVO = rasShopService.getRasShopByName(
									info.getShopName(), info.getCorpId());
							if (rasShopVO == null) {
								errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTEXIST);
							} else {
								info.setShopCode(rasShopVO.getCode());
							}
						}
					}
					/** 获取门店Id */
					if (rasShopVO != null) {
						info.setShopId(rasShopVO.getId());
					}
				}
				/** 缴费方式 **/
				if(!StringUtils.isBlank(info.getPayTypeName())){
					if(!dicList.contains(info.getPayTypeName())){
					  errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EXIST);	
					}else{
						info.setPayType(rasOtherCardBindingDao.queryDictList(info.getPayTypeName(), RasConstant.RAS_OTHER_CARD_DICT));//转换类型
					}
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EMPTY);	
				}
				/** 账号 **/
				if(!StringUtils.isBlank(info.getAccNo())){
					/** 账号名称**/
					if(!StringUtils.isBlank(info.getAccName())){
						/** 判断是否存在重复的绑定 **/
						if(isBindPayType(info,RasConstant.RAS_BANK_OPERATE_STATUS_SAVE)){
							errorMsgList.add(RasConstant.RAS_RASOTHERCARDBINDING_MESSAGE_REPEAT);
						}
					}else{
						errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCBANKNAME_EMPTY);	
					}
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCNO_EMPTY);	
				}
				info.addErrorMsg(errorMsgList);
			}
			//this.inject(list);
		}
	}

	public boolean isBindPayType(RasOtherCardBindingVO rasOtherCardBindingVO,String operate){
		//判断该账号是否被绑定
		if(rasOtherCardBindingDao.checkRasOtherCardBindingNoUnique(rasOtherCardBindingVO,operate)){
			return true;
		}
		//判断除了当前门店之外时候还有其它的绑定
		RasOtherCardBindingVO accBindFceVo = new RasOtherCardBindingVO();
		accBindFceVo.setId(rasOtherCardBindingVO.getId()==null?null:rasOtherCardBindingVO.getId());
		accBindFceVo.setShopId(rasOtherCardBindingVO.getShopId()==null?null:rasOtherCardBindingVO.getShopId());
		accBindFceVo.setAccNo(rasOtherCardBindingVO.getAccNo());
		if(rasOtherCardBindingDao.checkRasOtherCardBindingNoUnique(accBindFceVo,RasConstant.RAS_BANK_OPERATE_STATUS_OPEN)){
			return true;
		}
		return false;
	}
	
	@Override
	public BaseDao<RasOtherCardBinding, String> getEntityDao() {
		return null;
	}

}
