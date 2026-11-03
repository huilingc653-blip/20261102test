package com.bytter.cashVoucher.service;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.cashVoucher.dao.BvVouEmployInfoDao;
import com.bytter.cashVoucher.entity.BvVouEmployInfo;
import com.bytter.cashVoucher.vo.BvVouEmployInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: 凭证使用
* @author onlineGenerator
* @date 2017-06-09 09:06:03
* @version 1.0
*
*/
@Service("bvVouEmployInfoService")
@Transactional
public class BvVouEmployInfoService extends BaseService<BvVouEmployInfo,String> {
	@Autowired
	private BvVouEmployInfoDao bvVouEmployInfoDao;
	@Autowired
	private BvLogService bvLogService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvVouEmployInfo entity){
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
	 * 根据凭证明细id获取凭证使用信息
	 * @param voudtlId
	 */
	@Transactional(readOnly = true)
	public BvVouEmployInfoVo getDataByVoudtlId(String voudtlId){
		Map<String,Object> params = new HashMap<> ();
		params.put("voudtlId", voudtlId);
		return bvVouEmployInfoDao.getDataByVoudtlId(params);
	}
	/**
	* 保存
	* @param entity
	* @param dataSource 数据来源:01 手动录入，02打印占用';
	*/
 	public boolean save(BvVouEmployInfo entity,String dataSource,ICurrentUser userVo){
 		entity.setDataSource(dataSource);
 		entity.setCreateBy(userVo.getLoginName());
 		entity.setCreateDate(new Date());
 		entity.setCreateName(userVo.getUsername());
		super.saveEntity(entity);
		boolean flag = bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_ADD, BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
 		return flag;
 	}
 	
 	/**
 	 * 批量保存凭证使用记录
 	 * @param bvVouEmployInfoList
 	 */
 	public boolean batchSaveBvVouEmployInfo(List<BvVouEmployInfo> bvVouEmployInfoList,ICurrentUser userVo){
 		for(BvVouEmployInfo bvVouEmployInfo:bvVouEmployInfoList){
 			if(!save(bvVouEmployInfo,BlankvouConstant.BVVOUEMPLOYINFO_DATASOURCE_PRINT,userVo)){
 				return false;
 			}
 		}
 		return true;
 	}
 	
	/**
	* 更新
	* @param entity
	*/
	public void update(BvVouEmployInfo entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvVouEmployInfo entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<BvVouEmployInfo, String> getEntityDao() {
		return bvVouEmployInfoDao;
	}
}
