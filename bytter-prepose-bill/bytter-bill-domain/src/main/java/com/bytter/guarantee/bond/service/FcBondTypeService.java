package com.bytter.guarantee.bond.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.guarantee.bond.dao.FcBondDao;
import com.bytter.guarantee.bond.dao.FcBondTypeDao;
import com.bytter.guarantee.bond.entity.FcBondType;
import com.bytter.guarantee.vo.FcBondTypeVO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
* @Title: Service
* @Description: 保证金类型维护列表
*
*/
@Service("fcBondTypeService")
@Transactional
public class FcBondTypeService extends BaseService<FcBondType,String> {
	@Autowired
	private FcBondTypeDao fcBondTypeDao;
	@Autowired
	private FcBondDao fcBondDao;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 获取类型table
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<FcBondTypeVO> bondTypeList(DataTablesInput dataTablesInput){
		return fcBondTypeDao.fcBondTypeList(dataTablesInput);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcBondType entity,String userId){
		Boolean isOk = fcBondTypeDao.queryBondTypeByCode(entity.getBondTypeCode(),entity.getId());
		if(!isOk){
			throw new BusinessException(FcBondMessageEnum.FCBOND_BONDTYPE_NOT_NULL);
		}
		entity.setCreateBy(userId);
		entity.setCreateDate(new Date());
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcBondType entity,String userId){
		Boolean isOk = fcBondTypeDao.queryBondTypeByCode(entity.getBondTypeCode(),entity.getId());
		if(!isOk){
			throw new BusinessException(FcBondMessageEnum.FCBOND_BONDTYPE_NOT_NULL);
		}
		entity.setUpdateBy(userId);
		entity.setUpdateDate(new Date());
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcBondType entity,String userId){
		Boolean isOk = fcBondTypeDao.queryBondTypeByCode(entity.getBondTypeCode(),entity.getId());
		if(!isOk){
			throw new BusinessException(FcBondMessageEnum.FCBOND_BONDTYPE_NOT_NULL);
		}

		entity.setUpdateBy(userId);
		entity.setUpdateDate(new Date());
		super.saveOrUpdate(entity);
	}
	/**
	 * 根据id查询类型详情
	 * @param id
	 * @return
	 */
	public FcBondTypeVO getBondType(String id){
		FcBondTypeVO vo = fcBondTypeDao.getBondType(id);
		String validSignName = dictService.get("valid_sign",vo.getValidSign());
		vo.setValidSignName(validSignName);
		return vo;
	}
	/**
	 * 获取有效保证金类型
	 */
	public List<FcBondTypeVO> getAllBondType(){
		return fcBondTypeDao.getAllBondType();
	}
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcBondType entity){
		Boolean isOk = fcBondDao.getBondByBondTypeId(entity.getId());
		if (!isOk) {
			throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
		}
		super.delete(entity.getId());
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	@Override
	public BaseDao<FcBondType, String> getEntityDao() {
		return fcBondTypeDao;
	}
	
}
