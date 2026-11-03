package com.bytter.biz.expense.service;
import com.bytter.biz.expense.dao.NisBusinessTypeDao;
import com.bytter.biz.expense.entity.NisBusinessType;
import com.bytter.biz.expense.vo.NisBusinessTypeVO;
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
 * @Description: 业务类型表
 * @date 2017-06-01 14:42:30
 */
@Service("nisBusinessTypeService")
@Transactional
public class NisBusinessTypeService extends BaseService<NisBusinessType, String> {
	@Autowired
	private NisBusinessTypeDao nisBusinessTypeDao;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisBusinessType entity) {
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
	public void save(NisBusinessType entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisBusinessType entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisBusinessType entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisBusinessType, String> getEntityDao() {
		return nisBusinessTypeDao;
	}
	/**
	 * 根据业务类型名称查询
	 * zuoy 2017-08-15
	 *
	 * @param busTypeName 业务类型名称
	 * @return NisBusinessType
	 */
	public NisBusinessType getByBusTypeName(String busTypeName) {
		return this.nisBusinessTypeDao.getBusTypeName(busTypeName);
	}
	public List<NisBusinessTypeVO> listAllBusType() {
		return nisBusinessTypeDao.listAllBusType();
	}
}
