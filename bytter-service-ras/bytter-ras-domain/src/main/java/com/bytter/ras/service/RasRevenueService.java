package com.bytter.ras.service;

import com.bytter.ras.dao.RasReceivedDao;
import com.bytter.ras.dao.RasRevenueDao;
import com.bytter.ras.entity.RasRevenueDtl;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.RasImportConfVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核营收数据Service
 * @date 2019/5/15 13:39
 */
@Service("rasRevenueService")
@Transactional
public class RasRevenueService extends BaseService<RasRevenueDtl, String> 
implements ICustomVerifyHandler<RasRevenueDtlVO>,ICorpDeleteVerifyService{
	
    @Autowired
    public RasRevenueDao rasRevenueDao;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired
    private RasShopGrantService rasShopGrantService;
    @Autowired
    private  RasImportConfService rasImportConfService;
    @Autowired
    private  RasLogService rasLogService;
    @Autowired
    private  RasShopService rasShopService;
    @Autowired(required=false)
    private  IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    public RasReceivedDao rasReceivedDao;
    
    @Override
    public BaseDao<RasRevenueDtl, String> getEntityDao() {
        return rasRevenueDao;
    }
    
    /**
     * 营收数据查询列表加载
     * @param dataTablesInput
     */
    @SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String grantType,String resource) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(resource,dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(grantType, dataTablesInput.getCurrentUser());
        return rasRevenueDao.getDataTablesUnion(dataTablesInput,corpAuthList,shopAuthList);
    }

    /**
     * 根据ids批量修改营收表数据状态
     * @param ids ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废0待取认）
     * @param statusOld 根据对应旧状态处理状态值修改
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] aids,Integer statusNew,Integer statusOld,ICurrentUser userVo){
        if(aids==null||aids.length==0||statusNew==null){
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params=new HashMap<>();
        params.put("aids", aids);
        if(userVo!=null){
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if(statusOld!=null){
            params.put("statusOld", statusOld);
        }
        int num = rasRevenueDao.batchUpdate(params);
        return num;
    }
    
    /**
     * 营收数据自动稽核状态操作
     * @param params
     * @return
     */
    public int autoOpAuditStatusControl(Map<String, Object> params){
    	params.put(RasConstant.RAS_AUTOTASK_OPKEY, "1");
    	return rasRevenueDao.batchUpdate(params);
    }

    /**
     * 根据条件获取营收表数据
     * @param params
     * @return
     */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public List<RasRevenueDtlVO> getAllRasRevenueDtlVOInfo(Map<String,Object> params){
    	return rasRevenueDao.getAllRasRevenueDtlVOInfo(params);
    }
	
    /**
     * 根据参数获取稽核数据
     * @param params
     * @return
     */
    public List<RasRevenueDtl> getRasRevenueDtlByParams(Map<String,Object> params){
    	return rasRevenueDao.getRasRevenueDtlByParams(params);
    }
    

    /**
     * 根据参数获取稽核明细汇总数据
     * @param params
     * @return
     */
    public List<RasRevenueDtl> getRasRevenueDtlSumByParams(Map<String,Object> params){
    	return rasRevenueDao.getRasRevenueDtlSumByParams(params);
    }

    /**
	 * 
	* @Title: batchOperation  
	* @Description: TODO(批量作废/单条作废)  
	* @param @param ids
	* @param @param operate
	* @param @param userVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson batchOperation(String ids,ICurrentUser userVo){
		AjaxJson reJson = new AjaxJson();
		Map<String,Object> params =null;
		if(StringUtils.isNotEmpty(ids)){
			try{
				String[] idArray=StringUtils.split(ids,",");
				for(int i=0;i<idArray.length;i++){
					RasRevenueDtl rasRevenueDtl =rasRevenueDao.get(idArray[i]);
					params = new HashMap<String,Object>();
					if(rasRevenueDtl.getIsSum()!=null
						&& rasRevenueDtl.getIsSum()==1){//已汇总数据 把同一天的转换成待稽核
						params.put("ids", idArray[i]);
						if(rasRevenueDao.tovoid(params)>0){
							params.put("sameDate", rasRevenueDtl.getTransDate());
							params.put("payType", rasRevenueDtl.getPayType());
							params.put("shopId", rasRevenueDtl.getShopId());
							if(rasRevenueDao.reduction(params)>0){ 
								reJson.setMsg(RasConstant.COMMON_MESSAGE_SUCCESS);
								reJson.setSuccess(true);
								rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_RETURN,RasConstant.RAS_CANCELLATION_MESSAGE_SUCESS, userVo);
							}
						}
					}else{
						params.put("ids", idArray[i]);
						if(rasRevenueDao.tovoid(params)>0){
							reJson.setMsg(RasConstant.COMMON_MESSAGE_SUCCESS);
							reJson.setSuccess(true);
							rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_RETURN,RasConstant.RAS_CANCELLATION_MESSAGE_SUCESS, userVo);
						}
					}
				}
			}catch(Exception e){
				reJson.setMsg(RasConstant.COMMON_MESSAGE_OPERATE_FAIL);
				reJson.setSuccess(false);
			}
		}
		return reJson;
	}
    
	
	/**
	 * 
	* @Title: saveImportRasRevenueDtl  
	* @Description: TODO(导入营收数据方法)  
	* @param @param list
	* @param @param userVo    参数  
	* @return void    返回类型  
	* @throws
	 */
	public AjaxJson saveImportRasRevenueDtl(List<RasRevenueDtlVO> list, ICurrentUser userVo){
		AjaxJson reJson = new AjaxJson();
		for (int i=0;i<list.size();i++) {
			RasRevenueDtlVO rasRevenueDtlVO = list.get(i);
			RasRevenueDtl entity=new RasRevenueDtl();
			BeanUtils.copyProperties(rasRevenueDtlVO, entity);
			entity.setTransDate(rasRevenueDtlVO.getTransConvertDate());
			//营收默认待稽核
			entity.setStatus(1);//默认待稽核
			entity.setCreateBy(userVo.getLoginName());
			entity.setCreateDate(new Date());
			entity.setCreateName(userVo.getUsername());
			entity.setUpdateBy(userVo.getLoginName());
			entity.setUpdateDate(new Date());
			entity.setUpdateName(userVo.getUsername());
			rasRevenueDao.save(entity);
			reJson.setSuccess(true);
			rasLogService.saveRasLog(entity.getId(), RasConstant.RAS_LOGTYPE_IMP,RasConstant.RAS_BANK_SAVE_IMPORT, userVo);
		}
		return reJson;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(RasRevenueDtlVO obj,
			Object... objs) {

		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<RasRevenueDtlVO> list, Object... objs) {
		ICurrentUser userVo =(ICurrentUser) objs[0];
		if (list != null && !list.isEmpty()) {
			//缴款方式临时处理
			final List<String> dicList = dictService.listDictNameByDictGroup(RasConstant.RAS_OTHER_CARD_DICT);
			final List<DictDTO> listDictDTO = dictService.list(RasConstant.RAS_OTHER_CARD_DICT);
			//门店值临时存储
			List<RasShopVO> shopCeleList = rasShopService.hisRasShop(userVo.getId());
			//重复值临时存储
//			List<RasRevenueDtlVO> rasRevenueCeleList = new ArrayList<RasRevenueDtlVO>();
			for (RasRevenueDtlVO info : list) {
				List<String> errorMsgList = new ArrayList<String>();
				if(!StringUtils.isBlank(info.getShopName())){
					RasShopVO rasShopVO = null;
					if(null!=shopCeleList && shopCeleList.size()>0){
						for(RasShopVO shopVo:shopCeleList){
							if(shopVo.getName().equals(info.getShopName())
									||shopVo.getCode().equals(info.getShopName())){
								rasShopVO=shopVo;
								continue;
							}
						}
					}
					//判断门店信息
					if(null==rasShopVO){
						errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_NOT_EXIST);
					}else{
						//判断门店是否禁用
						if(rasShopVO.getvSign().equals("0"))
							errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_VALIDSIGN_ISNOT);
						else
							//判断账户是否对改账户是否有稽核操作权限
							if(rasShopVO.getIsAudit().equals("1")){
								info.setShopId(rasShopVO.getId());
							}else{
								//没有稽核权限
								errorMsgList.add(RasConstant.RAS_IMPORT_SHOP_NOT_POWER);
							}
					}
				}else{ 
					errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOPNAME_ISNOTNULL);
				}
				
				//交易时间
				if(null==info.getTransConvertDate()){
					errorMsgList.add(RasConstant.RAS_IMPORT_TRANSDATE_NOTNULL);
				}else{
					info.setStrTransDate(RasUtils.formatToYYYYMMDDStr(info.getTransConvertDate()));
				}
				//营收金额
				if(null==info.getMoney()){
					errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_MONEY_NOTNULL);
				}
				//缴付方式
				if(StringUtils.isBlank(info.getPayTypeName())){
					errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EMPTY);
				}else{
					if(!StringUtils.isBlank(info.getPayTypeName())){
						if(!dicList.contains(info.getPayTypeName())){
							errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EXIST);	
						}else{
							for(DictDTO dictDTO :listDictDTO){
								if(dictDTO.getName().equals(info.getPayTypeName())){
									info.setPayType(dictDTO.getCode());//转换类型
									continue;
								}
							}
						}
					}
				}
				//判断重复
//				if(!StringUtils.isBlank(info.getShopId())
//					&& !StringUtils.isBlank(info.getSerialNo())
//					&& !StringUtils.isBlank(info.getOrderNo())
//					&& null!=info.getTransConvertDate()	
//					&& null!=info.getMoney()
//					&& !StringUtils.isBlank(info.getPayType())){
//					boolean layConFlag=true;
//					if(null!=rasRevenueCeleList && rasRevenueCeleList.size()>0){
//						for(RasRevenueDtlVO revenueDtlVO:rasRevenueCeleList){
//							if(info.getShopId().equals(revenueDtlVO.getShopId())
//								&&info.getSerialNo().equals(revenueDtlVO.getSerialNo())
//								&&info.getOrderNo().equals(revenueDtlVO.getOrderNo())
//								&&info.getTransConvertDate().equals(revenueDtlVO.getTransConvertDate())
//								&&info.getMoney().equals(revenueDtlVO.getMoney())
//								&&info.getPayType().equals(revenueDtlVO.getPayType())){
//									layConFlag=false;
//									errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_REPEAT_EXIST);
//								continue;
//							}
//						}
//						
//					}
//					if(layConFlag){
//						rasRevenueCeleList.add(info);
//						//判断数据是否有重复
//						boolean flag =rasRevenueDao.getIsRepeatByRasRevenueDtlVO(info);
//						if(flag){
//							errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_REPEAT_EXIST);	
//						}
//					}
//					
//				}
				info.addErrorMsg(errorMsgList);
			}
//			//this.inject(list);
		}
	}

	/**
	 * 解析文件内容
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ImportCustomRules getImportCustomRules(Object... obj) {
		ImportCustomRules customRules = new ImportCustomRules();
		Map<String, Object> params =(Map<String, Object>)obj[0];
		String suffix = String.valueOf(params.get("suffix"));//文件后缀名
		RasImportConfVO queryVo=new RasImportConfVO();
		queryVo.setFileType(suffix);
		RasImportConfVO rasImportConfVO 
			= rasImportConfService.queryRasImportConfVO(queryVo, RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE);
		if(null==rasImportConfVO){
			throw new ParameterException(RasMessageEnum.RAS_IMPORT_REVENUE_NOT_CONFIGURED_EXIST);
		}
	
		List<Map<String,String>> rasImportConfVOList 
			= rasImportConfService.getTableInfo(queryVo, RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE);
		//存在列头
		if("csv".equalsIgnoreCase(suffix) 
				|| "xls".equalsIgnoreCase(suffix)
				|| "xlsx".equalsIgnoreCase(suffix)){
			Map<String,String> coluMap =new HashMap<String,String>();
			for(Map<String, String> lineTopMap:rasImportConfVOList){
				if(lineTopMap.get("value")!=null){
					coluMap.put(lineTopMap.get("required").toString().equals("true")
							?"*"+lineTopMap.get("nameCNY").toString():lineTopMap.get("nameCNY").toString(),lineTopMap.get("value"));
				}
			}
			int startRow=rasImportConfVO.getStartLine();
			//设置起始行
			customRules.setStartRow(startRow);
			//设置无列标题
			customRules.setHead(false);
			//设置列标题
			customRules.setColumnPositio(coluMap);
		}else{
			throw new ParameterException(RasMessageEnum.RAS_IMPORT_REVENUE_NOT_CONFIGURED_EXIST);
		}
		customRules.setSuffix(suffix);
		return customRules;
	}

	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		 //禁用划分细则
		 if(StringUtils.isNotEmpty(corpId)){
			 List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<CorpVerifyErrorDTO>();
	           //单位有营收信息，不允许删除！
	           int num=rasRevenueDao.getValidRevenueDtlByCorpId(corpId);
	           if(num>0){
	               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
	                       SysConfigConstant.Module.RAS,
	                       SysConfigConstant.BusType.RAS_REVENUE,
	                       multiLangServiceI.getLang(CorpVerifyMessageConstant.RAS_VALID_RASREVENUE_ERROR)));
	           }
	           
	           //单位有实收信息，不允许删除！
	           num=rasReceivedDao.getValidReceivedByCorpId(corpId);
	           if(num>0){
	               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
	                       SysConfigConstant.Module.RAS,
	                       SysConfigConstant.BusType.RAS_ECEIVED,
	                       multiLangServiceI.getLang(CorpVerifyMessageConstant.RAS_VALID_RASRECEIVED_ERROR)));
	           }
			 
			  if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
	               return verifyErrorDTOS;
	           }
		 }
		return null;
	}
}
