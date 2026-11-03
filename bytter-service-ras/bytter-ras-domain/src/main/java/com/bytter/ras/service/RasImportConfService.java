package com.bytter.ras.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.ras.dao.RasImportConfDao;
import com.bytter.ras.dao.RasImportConfDtlDao;
import com.bytter.ras.entity.RasImportConf;
import com.bytter.ras.entity.RasImportConfDtl;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.RasImportConfDtlVO;
import com.bytter.ras.vo.RasImportConfVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;

/**
 * 
* @Title:  营收数据导入配置Service
* @Package com.bytter.ras.service   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-16 上午10:58:29   
* @version V1.0
 */
@Service("rasImportConfService")
@Transactional
public class RasImportConfService extends BaseService<RasImportConf, String>
	implements IExcelVerifyHandler<RasImportConfVO>{
	
	@Autowired
	private RasImportConfDao rasImportConfDao;
	
	@Autowired
	private RasLogService rasLogService;
	
	@Autowired
	private RasImportConfDtlService rasImportConfDtlService;
	
	@Autowired
	private RasImportConfDtlDao confDtlDao;
	
	/**
	 * 
	* @Title: getTableInfo  
	* @Description: TODO(获取表内容信息 TXT类型除外)  
	* @param @param fileType
	* @param @return    参数  
	* @return List<?>    返回类型  
	* @throws
	 */
	public List<Map<String,String>> getTableInfo(RasImportConfVO rasImportConfVO,String operate){
		if(!StringUtils.isBlank(rasImportConfVO.getId()) || !StringUtils.isBlank(rasImportConfVO.getFileType())){
			List<RasImportConfVO> tableList = this.getRasImportConfVOByFileType(rasImportConfVO,operate);
			if(null!=tableList && tableList.size()>0){
				Map<String,String> valueMap=new HashMap<String,String>();
				for(RasImportConfVO info:tableList){
					valueMap.put(info.getFieldKey(), info.getStartVal());
				}
				List<Map<String,String>> reList=operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED)?
						RasUtils.getReceivedFieldConfList():RasUtils.getRevenueFieldConfList();
				for(Map<String,String> info:reList){
					if(valueMap.containsKey(info.get("key"))){
						info.put("value", valueMap.get(info.get("key")));
					}
				}
				return reList;
			}
			List<Map<String,String>> defaultList = operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED)?
					RasUtils.getReceivedFieldConfList():RasUtils.getRevenueFieldConfList();
			for(Map<String,String> info:defaultList){
				info.put("value", "");
			}
			return defaultList;
		}
		return null;
	}
	
	//获取TXT业务类型数据 取结束的的业务数据
	public List<Map<String,String>> getTableInfo(RasImportConfVO rasImportConfVO){
		if(!StringUtils.isBlank(rasImportConfVO.getId()) || !StringUtils.isBlank(rasImportConfVO.getFileType())){
			List<RasImportConfVO> tableList = this.getRasImportConfVOByFileType(rasImportConfVO,RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
			if(null!=tableList && tableList.size()>0){
				Map<String,String> valueMap=new HashMap<String,String>();
				if(!StringUtils.isBlank(rasImportConfVO.getFileType()) 
					&& rasImportConfVO.getFileType().equals("txt")
					&& rasImportConfVO.getIsFormat().equals("0")){
					for(RasImportConfVO info:tableList){
						valueMap.put(info.getFieldKey(), info.getEndVal());
					}
				}
				List<Map<String,String>> reList = RasUtils.getReceivedFieldConfList();
				for(Map<String,String> info:reList){
					if(valueMap.containsKey(info.get("key"))){
						info.put("value", valueMap.get(info.get("key")));
					}
				}
				return reList;
			}
			List<Map<String,String>> defaultList = RasUtils.getReceivedFieldConfList();
			for(Map<String,String> info:defaultList){
				info.put("value", "");
			}
			return defaultList;
		}
		return null;
	}
	
	/**
	 * 
	* @Title: queryRasImportConfVO  
	* @Description: TODO(返回模板配置信息)  
	* @param @return    参数  
	* @return RasImportConfVO    返回类型  
	* @throws
	 */
	public RasImportConfVO queryRasImportConfVO(RasImportConfVO rasImportConfVO,String operate){
		if(!StringUtils.isBlank(rasImportConfVO.getFileType())||!StringUtils.isBlank(rasImportConfVO.getId())){
			List<RasImportConfVO> tableList = rasImportConfDao.getRasImportConfVOByFileType(rasImportConfVO,operate);
			if(null!=tableList && tableList.size()>0){
				return tableList.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 
	* @Title: getRasImportConfVOByFileType  
	* @Description: TODO(根据类型查询已存在的导入配置信息)  
	* @param @param fileType
	* @param @param operate
	* @param @return    参数  
	* @return List<RasImportConfVO>    返回类型  
	* @throws
	 */
	public List<RasImportConfVO>  getRasImportConfVOByFileType(RasImportConfVO rasImportConfVO,String operate){
		List<RasImportConfVO> tableList = rasImportConfDao.getRasImportConfVOByFileType(rasImportConfVO,operate);
		return tableList;
	}
	
	/**
	 * 
	* @Title: getRasImportConfVOList  
	* @Description: TODO(根据业务类型，查询存在的配置业务数据)  
	* @param @param operate
	* @param @return    参数  
	* @return List<RasImportConfVO>    返回类型  
	* @throws
	 */
	public List<RasImportConfVO> getRasImportConfVOList(String operate){
		List<RasImportConfVO> tableList = rasImportConfDao.getRasImportConfVOByoperate(null,operate);
		if(null!=tableList && tableList.size()>0){
			return tableList;
		}
		return null;
	}
	
	
	/**
	 * 
	* @Title: delRasImportConfVOByid  
	* @Description: TODO(删除实收配置数据)  
	* @param @param id
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson delRasImportConfVOByid(String id){
		AjaxJson reJson =new AjaxJson();
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("ircId", id);
		rasImportConfDao.batchUpdate(params);
		confDtlDao.batchUpdate(params);
		reJson.setSuccess(true);
		return reJson;
	}
	
	
	/**
	 * 
	* @Title: baseSaveOrUpdate  
	* @Description: TODO(营收稽核导入配置)  
	* @param @param rasOtherCardBindingVO
	* @param @param operate
	* @param @param userVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	public AjaxJson baseSaveOrUpdate(RasImportConfVO rasImportConfVO,String operate,ICurrentUser userVo){
		AjaxJson reJson =new AjaxJson();
		RasImportConf rasImportConf = new RasImportConf();
		RasImportConfDtl rasImportConfDtl = null;
		rasImportConf.setCreateBy(userVo.getLoginName());
		rasImportConf.setCreateDate(new Date());
		rasImportConf.setCreateName(userVo.getUsername());
		rasImportConf.setFileType(rasImportConfVO.getFileType());
		rasImportConf.setStartLine(rasImportConfVO.getStartLine()==null?1:rasImportConfVO.getStartLine());
		//营收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE)){
			rasImportConf.setType(RasConstant.RAS_CLAIM_TYPE_0.toString());
		}
		//实收
		if(operate.equals(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED)){
			rasImportConf.setDateSource(rasImportConfVO.getDateSource());
			rasImportConf.setPayType(rasImportConfVO.getPayType());
			rasImportConf.setIsFormat(rasImportConfVO.getIsFormat());
			rasImportConf.setTxtFormat(rasImportConfVO.getTxtFormat());
			rasImportConf.setType(RasConstant.RAS_CLAIM_TYPE_1.toString());
		}
		List<RasImportConfVO> tableList = this.getRasImportConfVOByFileType(rasImportConfVO,operate);
		if(null!=tableList&&tableList.size()>0){
			rasImportConf.setId(tableList.get(0).getId());
		}
		if(StringUtils.isBlank(rasImportConf.getId())){
			rasImportConfDao.save(rasImportConf);
		}else{
			rasImportConf.setUpdateBy(userVo.getLoginName());
			rasImportConf.setCreateDate(new Date());
			rasImportConf.setCreateName(userVo.getUsername());
			rasImportConfDao.saveOrUpdate(rasImportConf);
		}
		Map<String,Object> params =new HashMap<String,Object>();
		params.put("ircId", rasImportConf.getId());
		confDtlDao.batchUpdate(params);
		for(int i=1;i<rasImportConfVO.getDtlList().size();i++){
			rasImportConfDtl = new RasImportConfDtl();
			RasImportConfDtlVO conFigDtlVo =rasImportConfVO.getDtlList().get(i);
			rasImportConfDtl.setIcId(rasImportConf.getId());
			rasImportConfDtl.setFieldKey(conFigDtlVo.getFieldKey());
			rasImportConfDtl.setStartVal(conFigDtlVo.getStartVal());
			rasImportConfDtl.setEndVal(conFigDtlVo.getEndVal());
			confDtlDao.save(rasImportConfDtl);
		}
		reJson.setMessageEnum(RasMessageEnum.RAS_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
	}

	/**
	 * 
	* @Title: getTxtModelAnalysis  
	* @Description: TODO(导入TXT解析模板配置处理)  
	* @param @param rasImportConfVO
	* @param @param operate
	* @param @return    参数  
	* @return List<Map<String,String>>    返回类型  
	* @throws
	 */
	public List<Map<String,String>> getTxtModelAnalysis(RasImportConfVO rasImportConfVO,String operate){
		List<RasImportConfVO> tableList = this.getRasImportConfVOByFileType(rasImportConfVO,operate);
		Map<String,String> valueMap=new HashMap<String,String>();
		for(RasImportConfVO info:tableList){
			valueMap.put(info.getFieldKey(),rasImportConfVO.getIsFormat().equals("0")
					?info.getStartVal()+"-"+info.getEndVal():info.getStartVal());
		}
		List<Map<String,String>> reList=RasUtils.getReceivedFieldConfList();
		for(Map<String,String> info:reList){
			if(valueMap.containsKey(info.get("key"))){
				info.put("value", valueMap.get(info.get("key")));
			}
		}
		return reList;
	}
	
	@Override
	public ExcelVerifyHanlderResult verifyHandler(RasImportConfVO arg0) {
		return null;
	}

	@Override
	public void verifyHandler(List<RasImportConfVO> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseDao<RasImportConf, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
