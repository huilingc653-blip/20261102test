package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.BtPayTypeDao;
import com.bytter.biz.netbank.entity.BtPayType;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author huangx
 * @Description 系统结算方式
 * @date 2017.9.14
 */
@Service("btPayTypeService")
@Transactional
public class BtPayTypeService extends BaseService<BtPayType, String> {
	@Autowired
	private BtPayTypeDao btPayTypeDao;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtPayType entity) {
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
	public void save(BtPayType entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtPayType entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtPayType entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BtPayType, String> getEntityDao() {
		return btPayTypeDao;
	}
	/**
	 * 获取有效的系统结算方式
	 *
	 * @return
	 */
	public List<BtPayTypeVO> getValidPayType() {
		return btPayTypeDao.getValidPayType();
	}
}
