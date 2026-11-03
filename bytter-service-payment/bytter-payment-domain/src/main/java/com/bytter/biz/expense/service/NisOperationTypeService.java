package com.bytter.biz.expense.service;
import com.bytter.biz.expense.dao.NisOperationTypeDao;
import com.bytter.biz.expense.entity.NisOperationType;
import com.bytter.biz.expense.vo.NisOperationTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 操作类型表
 * @date 2017-06-01 14:40:32
 */
@Service("nisOperationTypeService")
@Transactional
public class NisOperationTypeService extends BaseService<NisOperationType, String> {
	@Autowired
	private NisOperationTypeDao nisOperationTypeDao;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisOperationType entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(NisOperationType entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisOperationType entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisOperationType entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisOperationType, String> getEntityDao() {
		return nisOperationTypeDao;
	}
	/**
	 * 根据业务类型获取操作类型列表
	 *
	 * @param busType
	 * @return
	 */
	public List<NisOperationTypeVO> listByBusType(String busType) {
		return nisOperationTypeDao.listByBusType(busType);
	}
	/**
	 * 根据业务类型和操作类型名称查询数据
	 * zuoy 2017-08-15
	 *
	 * @param busType    业务类型
	 * @param opTypeName 操作类型名称
	 * @return NisOperationType
	 */
	public NisOperationType getByBusTypeAndOpTypeName(String busType, String opTypeName) {
		return this.nisOperationTypeDao.getByBusTypeAndOpName(busType, opTypeName);
	}
	/**
	 * 根据操作类型名称获取操作类型
	 *
	 * @param opTypeName
	 * @return
	 */
	public NisOperationType getByOpTypeName(String opTypeName) {
		return this.nisOperationTypeDao.getUniqueBy("opTypeName", opTypeName);
	}
}
