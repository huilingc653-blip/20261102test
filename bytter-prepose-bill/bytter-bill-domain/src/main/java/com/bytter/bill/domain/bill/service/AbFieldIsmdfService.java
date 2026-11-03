package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbFieldIsmdfDao;
import com.bytter.bill.domain.bill.entity.AbFieldIsmdf;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Entity
 * @Description: 票据业务字段是否可以修改配置Service
 * @author zhengg
 * @date 2020-04-26 14:29:10
 * @version V1.0   
 *
 */
@Service("abFieldIsmdfService")
@Transactional
public class AbFieldIsmdfService extends BaseService<AbFieldIsmdf, String> {
	@Autowired
	private AbFieldIsmdfDao abFieldIsmdfDao;
	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void deleteEntity(AbFieldIsmdf entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * 
	 * @param id
	 */
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(AbFieldIsmdf entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(AbFieldIsmdf entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(AbFieldIsmdf entity) {
		super.saveOrUpdate(entity);
	}
	public BaseDao<AbFieldIsmdf, String> getEntityDao() {
		return abFieldIsmdfDao;
	}
	
	/**
	 * 根据busId删除数据
	 * @param busId 业务ID
	 */
	public int deleteAbFieldIsmdfByBusId(String busId){
		return abFieldIsmdfDao.deleteAbFieldIsmdfByBusId(busId);
	}
	
	/**
	 * 根据传递测试删除数据
	 * @param busId 业务ID
	 * @param fieldCode 业务数据字段属性
	 */
	public int deleteAbFieldIsmdf(String busId,String fieldCode){
		Map<String,Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(busId)){
			params.put("busId", busId);
		}
		if(StringUtils.isNotEmpty(fieldCode)){
			params.put("fieldCode", fieldCode);
		}
		return abFieldIsmdfDao.deleteAbFieldIsmdfByParams(params);
	}
	
	/**
	 * 新增一笔配置不可以修改信息
	 * @param busId 业务ID
	 * @param fieldCode 业务数据字段属性
	 * @param userVo 用户信息
	 */
	public void saveAbFieldIsmdf(String busId, String fieldCode, ICurrentUser userVo){
		saveAbFieldIsmdf(busId, fieldCode, AccebillConstant.AB_BILL_MDFSIGN_0, userVo);
	}
	/**
	 * 新增一笔配置是否可以修改信息
	 * @param busId 业务ID
	 * @param fieldCode 业务数据字段属性
	 * @param mdfSign 修改标记：1是0否
	 * @param userVo 用户信息
	 */
	public void saveAbFieldIsmdf(String busId, String fieldCode, String mdfSign, ICurrentUser userVo){
		if(StringUtils.isEmpty(mdfSign)){
			mdfSign=AccebillConstant.AB_BILL_MDFSIGN_0;
		}
		String createBy=null;
		if(userVo!=null){
			createBy=userVo.getId();
		}
		abFieldIsmdfDao.save(new AbFieldIsmdf(busId, fieldCode, mdfSign, null, createBy));
	}
	
	/**
	 * 根据业务Id获取字段和是否修改标记的map信息
	 * @param busId
	 * @return 字段和是否修改标记的map信息（字段1=1，字段2=0）
	 */
	@Transactional(readOnly = true)
	public Map<String,Object> getFieldIsmdfInfo(String busId){
		Map<String,Object> fieldIsmdfMap=new HashMap<>();
		List<AbFieldIsmdf> list=abFieldIsmdfDao.getAbFieldIsmdfByBusId(busId);
		if(ListUtils.isNotNullOrEmpty(list)){
			for(AbFieldIsmdf info:list){
				if(AccebillConstant.AB_BILL_MDFSIGN_0.equals(info.getMdfSign())){
					fieldIsmdfMap.put(info.getFieldCode(), true);
				}
			}
		}
		return fieldIsmdfMap;
	}
	
	/**
	 * 对字段进行不可编辑初始化处理
	 * @param busId 业务ID
	 * @param model 返回前端属性
	 */
	@Transactional(readOnly = true)
	public void initFieldIsmdfInfo(String busId, Map model){
		Map<String,Object> fieldIsmdfMap=getFieldIsmdfInfo(busId);
		if(fieldIsmdfMap!=null || !fieldIsmdfMap.isEmpty()){
			model.put("ismdf", fieldIsmdfMap);
		}
	}
	
}
