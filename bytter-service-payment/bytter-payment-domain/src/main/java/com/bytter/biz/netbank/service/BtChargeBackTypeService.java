package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.BtChargeBackTypeDao;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.vo.BtChargeBackTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 退单类型表
 * @date 2017-05-17 14:26:13
 */
@Service("btChargeBackTypeService")
@Transactional
public class BtChargeBackTypeService extends BaseService<BtChargeBackType, String> {
	@Autowired
	private BtChargeBackTypeDao btChargeBackTypeDao;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	/**
	 * 根据退单ID获取退单信息
	 *
	 * @param id
	 * @return
	 */
	public BtChargeBackType getById(String id) {
		return btChargeBackTypeDao.getById(id);
	}
	/**
	 * 查询退单类型列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtChargeBackTypeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		return btChargeBackTypeDao.getDataTablesUnion(dataTablesInput);
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtChargeBackType entity) {
		super.deleteEntity(entity);
		//删除多语言
		ii18nService.deleteBusinessBaseContext(entity.getId());
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
	public void save(BtChargeBackType entity, String locale) {
		updateIsDefault(entity);
		super.saveEntity(entity);
		//新增当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getBusTypeName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtChargeBackType entity) {
		updateIsDefault(entity);
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtChargeBackType entity) {
		updateIsDefault(entity);
		super.saveOrUpdate(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BtChargeBackType entity, String locale) {
		updateIsDefault(entity);
		super.saveOrUpdate(entity);
		//更新当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getBusTypeName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	@Override
	public BaseDao<BtChargeBackType, String> getEntityDao() {
		return btChargeBackTypeDao;
	}
	/**
	 * 根据编码获取退单类型
	 *
	 * @param busType
	 * @return
	 */
	public BtChargeBackType findByCode(String busType) {
		return btChargeBackTypeDao.findByCode(busType);
	}
	/**
	 * 获取有效的退单类型
	 *
	 * @return
	 */
	public List<BtChargeBackTypeVO> getValidChargeBackType() {
		return btChargeBackTypeDao.getValidChargeBackType();
	}
	/**
	 * 根据退单ID获取退单信息
	 *
	 * @param id
	 * @return
	 */
	public BtChargeBackTypeVO getByIdUnion(String id) {
		return btChargeBackTypeDao.getByIdUnion(id);
	}
	/**
	 * 判断是否存在多个默认退单类型，若存在则将旧退单类型的"默认设置"改为否
	 *
	 * @param entity
	 */
	public void updateIsDefault(BtChargeBackType entity) {
		// 判断退单类型是否为"默认设置"
		if (CommonConstant.YES.equals(entity.getIsDefault())) {
			// 查询为"默认设置"的退单类型
			List<BtChargeBackType> list = this.btChargeBackTypeDao.listBy("isDefault", "1");
			for (BtChargeBackType oldBtChargeBackType : list) {
				if (!oldBtChargeBackType.getId().equals(entity.getId())) {
					// 将旧退单类型的"默认设置"改为否
					oldBtChargeBackType.setIsDefault("0");
					this.btChargeBackTypeDao.save(oldBtChargeBackType);
				}
			}
		}
	}
	/**
	 * 获取默认退单类型
	 * @return
	 */
	public BtChargeBackType findByDef() {
		return btChargeBackTypeDao.findByDef();
	}
}
