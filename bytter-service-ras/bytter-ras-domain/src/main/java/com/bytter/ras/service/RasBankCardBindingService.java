package com.bytter.ras.service;

import java.util.*;

import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasBankCardBindingDao;
import com.bytter.ras.entity.RasBankCardBinding;
import com.bytter.ras.vo.RasBankCardBindingVO;
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
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;

/**
 * 
* @Title:  营收稽核银行卡绑定处理类  
* @Package RasBankCardBindingService   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-5 下午5:55:04   
 */
@Service("rasBankCardBindingService")
@Transactional
public class RasBankCardBindingService extends BaseService<RasBankCardBinding, String>
 	implements IExcelVerifyHandler<RasBankCardBindingVO>, DictInjectProvider {

	@Autowired
	private RasBankCardBindingDao rasBankCardBindingDao;
	
	@Autowired(required=false)
	private ICorpService corpService;
	
	@Autowired
	private RasLogService rasLogService;
	
    @Autowired
    private RasShopService rasShopService;
    
    @Autowired(required=false)
    private IBtBankAccService iBtBankAccService;
    
    @Autowired
    private RasShopGrantService rasShopGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
	/**
	 * 
	* @Title: getDataTablesUnion  
	* @Description: TODO(营收稽核银行卡查询列表)  
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
		return rasBankCardBindingDao.getDataTablesUnion(input,corpAuthList,shopAuthList);
	}
	
	
	/**
	* @Title: baseSaveOrUpdate  
	* @Description: TODO(保存或修改营收稽核银行卡)  
	* @param @param bvPayeeCheque
	* @param @param operate
	* @param @param userVo    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public synchronized AjaxJson baseSaveOrUpdate(RasBankCardBindingVO rasBankCardBindingVo,String operate,ICurrentUser userVo){
		RasBankCardBinding rasBankCardBinding = new RasBankCardBinding();
		AjaxJson reJson = new AjaxJson();
		//判断是否重复
		if(rasBankCardBindingDao.checkRasBankCardBindingNoUnique(rasBankCardBindingVo,operate)){
			reJson.setMessageEnum(RasMessageEnum.RAS_RASBANKCARDBINDING_MESSAGE_REPEAT);
			reJson.setSuccess(false);
			return reJson;
		}
		rasBankCardBinding.setCorpId(rasBankCardBindingVo.getCorpId());
		rasBankCardBinding.setShopId(rasBankCardBindingVo.getShopId());
		rasBankCardBinding.setIsVirtualAcc(rasBankCardBindingVo.getIsVirtualAcc());
		rasBankCardBinding.setAccId(rasBankCardBindingVo.getAccId());
		rasBankCardBinding.setAccNo(rasBankCardBindingVo.getAccNo());
		rasBankCardBinding.setAccName(rasBankCardBindingVo.getAccName());
		rasBankCardBinding.setAccBankName(rasBankCardBindingVo.getAccBankName());
		rasBankCardBinding.setEntityAccId(rasBankCardBindingVo.getEntityAccId());
		rasBankCardBinding.setEntityAccNo(rasBankCardBindingVo.getEntityAccNo());
		rasBankCardBinding.setRmk(rasBankCardBindingVo.getRmk());
		rasBankCardBinding.setCreateBy(userVo.getLoginName());
		//新增
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_SAVE)
				||operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT)){
			rasBankCardBinding.setValidSign(RasConstant.RAS_VALID_SIGN_1);
			rasBankCardBinding.setCreateDate(new Date());
			rasBankCardBinding.setCreateName(userVo.getUsername());
			rasBankCardBindingDao.save(rasBankCardBinding);
			if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT))
				rasLogService.saveRasLog(rasBankCardBinding.getId(), RasConstant.RAS_LOGTYPE_IMP,RasConstant.RAS_BANK_SAVE_IMPORT, userVo);
			else
				rasLogService.saveRasLog(rasBankCardBinding.getId(), RasConstant.RAS_LOGTYPE_ADD,RasConstant.RAS_BANK_SAVE_SUCC, userVo);
		} 
		//修改
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_UPDATE)){
			rasBankCardBinding.setId(rasBankCardBindingVo.getId());
			rasBankCardBinding.setValidSign(rasBankCardBindingVo.getValidSign());
			rasBankCardBinding.setUpdateBy(userVo.getLoginName());
			rasBankCardBinding.setUpdateDate(new Date());
			rasBankCardBinding.setUpdateName(userVo.getUsername());
			rasBankCardBindingDao.update(rasBankCardBinding);
			rasLogService.saveRasLog(rasBankCardBinding.getId(), RasConstant.RAS_LOGTYPE_UPDATE,RasConstant.RAS_BANK_UPDATE_SUCC, userVo);
		}
		reJson.setMessageEnum(RasMessageEnum.RAS_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
	}
	
	/**
	 * 
	* @Title: getRasBankCardBindingSign  
	* @Description: TODO(单条营收稽核银行卡详细信息)  
	* @param @param id
	* @param @return    参数  
	* @return RasBankCardBindingVO    返回类型  
	* @throws
	 */
	@Transactional(readOnly = true)
    public RasBankCardBindingVO getRasBankCardBindingSign(String id) {
        if (!StringUtils.isEmpty(id)) {
            List<RasBankCardBindingVO>  rasBankCardBindingVo = rasBankCardBindingDao.getRasBankCardBindingSign(id);
            if(null!=rasBankCardBindingVo && rasBankCardBindingVo.size()>0){
                return rasBankCardBindingVo.get(0);
            }
        }
        return null;
    }
	
	
	/**
	 * 
	* @Title: saveImport  
	* @Description: TODO(保存导入的数据)  
	* @param @param list
	* @param @param userVo
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public boolean saveImport(List<RasBankCardBindingVO> list, ICurrentUser userVo) {
		for (int i=0;i<list.size();i++) {
			RasBankCardBindingVO rasBankCardBindingVO = list.get(i);
			RasBankCardBinding entity=new RasBankCardBinding();
			BeanUtils.copyProperties(rasBankCardBindingVO, entity);
			boolean flag = rasBankCardBindingDao.checkRasBankCardBindingNoUnique(rasBankCardBindingVO, RasConstant.RAS_BANK_OPERATE_STATUS_SAVE);
			if(flag){
				throw new BusinessException(RasMessageEnum.RAS_RASBANKCARDBINDING_MESSAGE_REPEAT,String.valueOf(i+1));
			}
			this.baseSaveOrUpdate(rasBankCardBindingVO, RasConstant.RAS_BANK_OPERATE_STATUS_IMPORT, userVo);
		}
		return true;
	}
	
	/**
	 * 
	* @Title: batchOperation  
	* @Description: TODO(批量启用/禁用操作)  
	* @param @param rasBankCardBindingVo
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
						rasBankCardBindingDao.updateValidSign(idArray[i], RasConstant.RAS_VALID_SIGN_1);
						rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_START,RasConstant.RAS_BANK_UPDATE_START, userVo);
					}else{ 
						rasBankCardBindingDao.updateValidSign(idArray[i], RasConstant.RAS_VALID_SIGN_0);
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


	@Override
	public ExcelVerifyHanlderResult verifyHandler(RasBankCardBindingVO obj) {
		return null;
	}


	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<RasBankCardBindingVO> list) {
		if (list != null && !list.isEmpty()) {
			List<String> errorMsgList = null;
            CorpDTO corpDTO = null;
            RasShopVO rasShopVO = null;
            for (RasBankCardBindingVO info : list) {
                errorMsgList = new ArrayList<>();
                /** 单位代码和单位名称校验 */
                if (StringUtils.isEmpty(info.getCorpCode())
                        && StringUtils.isEmpty(info.getCorpName())) {
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
                //单位不为空检测门店信息
                if (StringUtils.isNotEmpty(info.getCorpId())) {
                    /** 门店编码和门店名称校验 */
                    if (StringUtils.isEmpty(info.getShopCode())
                            && StringUtils.isEmpty(info.getShopName())) {
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
                            rasShopVO = rasShopService.getRasShopByName(info.getShopName(), info.getCorpId());
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
				
				/** 判断虚拟账户 **/
				if(!StringUtils.isBlank(info.getIsVirtualAccName())){
					if(info.getIsVirtualAccName().equals(RasConstant.RAS_BANK_BANKCARD_ISVIRTUALACC_STATUS_OK))
						info.setIsVirtualAcc(RasConstant.RAS_VALID_SIGN_1);
					else
						info.setIsVirtualAcc(RasConstant.RAS_VALID_SIGN_0);
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ISVIRTUALACC_EMPTY);
				}
				
				BtBankAccDTO btbankDto =null;
				/** 银行账号 **/
				if(!StringUtils.isBlank(info.getAccNo())){
					btbankDto =iBtBankAccService.getBankAccInfo(info.getAccNo());
					if(null!=btbankDto&&null!=btbankDto.getId()){
						info.setAccId(btbankDto.getId());
					}else{
						errorMsgList.add(RasConstant.RAS_RASBANKCARDBINDING_BANKNO_NEGATION);	
					}
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_BANKNO_EMPTY);	
				}
				
				/** 账户名称 **/
				if(!StringUtils.isBlank(info.getAccName())){
					if(null!=btbankDto&&null!=btbankDto.getAccName() && btbankDto.getAccName().equals(info.getAccName())){
						info.setAccName(btbankDto.getAccName());
					}else{
						errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCNAME_NOTLIKE);	
					}
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCNAME_EMPTY);	
				}
			
				/** 开户行名称 **/
				if(!StringUtils.isBlank(info.getAccBankName())){
					if(null!=btbankDto&&null!=btbankDto.getBankName() && btbankDto.getBankName().equals(info.getAccBankName())){
						info.setAccBankName(btbankDto.getBankName());
					}else{
						errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCBANKNAME_NOTLIKE);	
					}
				}else{
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ACCBANKNAME_EMPTY);	
				}
				
				/** 关联实体账号 **/
				if(null!=info.getIsVirtualAcc() && info.getIsVirtualAcc().equals(RasConstant.RAS_VALID_SIGN_1)){
					if(!StringUtils.isBlank(info.getEntityAccNo())){//判断是否存在实体账户
						BtBankAccDTO entityDto =null;
						if(null!=btbankDto){
							entityDto =iBtBankAccService.getBankAccInfo(info.getEntityAccNo());
							if(entityDto!=null 
									&&!StringUtils.isBlank(btbankDto.getEntityAccId())
									&&!StringUtils.isBlank(entityDto.getId()) 
									&&btbankDto.getEntityAccId().equals(entityDto.getId())){
								 info.setEntityAccId(entityDto.getId());
								 info.setEntityAccNo(info.getEntityAccNo());
							}else{
								errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ENTITYACCNO_NOTLIKE);
							}
						}else{
							errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ENTITYACCNO_NOTLIKE);	
						}
					}else{
						errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_ENTITYACCNO_EMPTY);	
					}
				}
				/** 判断是否存在重复的绑定 **/
				if(rasBankCardBindingDao.checkRasBankCardBindingNoUnique(info,RasConstant.RAS_BANK_OPERATE_STATUS_SAVE)){
					errorMsgList.add(RasConstant.RAS_RASBANKCARDBINDING_MESSAGE_REPEAT);
				}
				info.addErrorMsg(errorMsgList);
			}
			//this.inject(list);
		}
	}


	/**
	 * 根据参数获取稽核银行卡绑定数据库实体类
	 * @param id
	 * @param accNo 银行账户
	 * @param entityAccNo 实体账号
	 * @return
	 */
	public List<RasBankCardBinding> getRasBankCardBindingByParams(String id,String accNo,String entityAccNo){
		Map<String, Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(id)){
			params.put("id", id);
		}
		if(StringUtils.isNotEmpty(accNo)){
			params.put("accNo", accNo);
		}
		if(StringUtils.isNotEmpty(entityAccNo)){
			params.put("entityAccNo", entityAccNo);
		}
		return rasBankCardBindingDao.getRasBankCardBindingByParams(params);
	}

	@Override
	public BaseDao<RasBankCardBinding, String> getEntityDao() {
		return null;
	}

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
