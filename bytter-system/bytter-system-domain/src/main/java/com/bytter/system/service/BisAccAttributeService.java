package com.bytter.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.dao.BisAccAttributeDao;
import com.bytter.system.entity.BisAccAttribute;
import com.bytter.system.vo.BisAccAttributeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author huangx
 * @Description 账户属性维护
 * @date 2017.9.14
 */
@Service("bisAccAttributeService")
@Transactional
public class BisAccAttributeService extends BaseService<BisAccAttribute, String> {
	@Autowired
	private BisAccAttributeDao bisAccAttributeDao;
//	@Autowired(required=false)
//	private BtBankAccService btBankAccService;
	@Autowired
	private IBtBankAccService btBankAccServiceI;
	@Autowired
	private Ii18nService i18nServiceI;
	public DataTablesOutput<BisAccAttributeVO> dataTable(DataTablesInput dataTablesInput){
		DataTablesOutput<BisAccAttributeVO> result=new DataTablesOutput<>();
		DataTablesOutput<BisAccAttribute> dataTablesOutput = this.getDataTables(dataTablesInput);
		BeanUtils.copyProperties(dataTablesOutput, result);
		if(EmptyUtils.isNotEmpty(dataTablesOutput.getData())){
			List<BisAccAttributeVO>  bisAccAttributeVO=	BeanUtil.copyToList(dataTablesOutput.getData(),BisAccAttributeVO.class);
			for(BisAccAttributeVO vo :bisAccAttributeVO){
				vo.setAttributeNameShow(vo.getAttributeName());
			}
			result.setData(bisAccAttributeVO);
		}
		return result;
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BisAccAttribute entity) {
		super.deleteEntity(entity);
		//删除多语言
		i18nServiceI.deleteBusinessBaseContext(entity.getId());
	}
	/**
	 * 判断账户属性是否在使用中
	 *
	 * @return
	 */
	public boolean queryUse(String attributeId) {
		return this.bisAccAttributeDao.queryUse(attributeId);
	}
	/**
	 * 获取VO对象
	 * @param id
	 * @return
	 */
	public BisAccAttributeVO getVO(String id) {
		BisAccAttributeVO accAttributeVO = new BisAccAttributeVO();
		BisAccAttribute accAttribute = bisAccAttributeDao.get(id);
		BeanUtils.copyNotNullProperties(accAttribute, accAttributeVO);
		return accAttributeVO;
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
		//删除多语言
		i18nServiceI.deleteBusinessBaseContext(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BisAccAttribute entity, String locale) {
		entity.setAttributeDes(entity.getForeignDes() + entity.getSameDes() + entity.getSuperDes());
		super.saveEntity(entity);
		//新增当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getAttributeName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BisAccAttribute entity,String locale) {
		entity.setAttributeDes(entity.getForeignDes() + entity.getSameDes() + entity.getSuperDes());
		super.saveOrUpdate(entity);
		//更新当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getAttributeName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void deleteBisAccAttribute(BisAccAttribute entity) {
		// 判断账户属性信息是否被占用
		boolean flag = this.btBankAccServiceI.isUsedBaseData("attributeId", entity.getId());
		if (flag) {
			throw new ParameterException(NetbankMessageEnum.NIS_ACCNATURE_MESSAGE_NATUREID_USE, entity.getAttributeName());
		}
		this.deleteEntity(entity);
		//删除多语言
		i18nServiceI.deleteBusinessBaseContext(entity.getId());
	}
	/**
	 * 批量删除对象
	 *
	 * @param ids
	 */
	public void batchDelete(String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			BisAccAttribute bisAccAttribute = this.get(id);
			this.deleteBisAccAttribute(bisAccAttribute);
		}
	}
	@Override
	public BaseDao<BisAccAttribute, String> getEntityDao() {
		return bisAccAttributeDao;
	}
	/**
	 * 根据编码获取账户属性
	 *
	 * @param attributeCode
	 * @return
	 */
	public BisAccAttribute findByCode(String attributeCode) {
		return bisAccAttributeDao.findByCode(attributeCode);
	}
	/**
	 * 获取有效的账户属性
	 *
	 * @return
	 */
	public List<BisAccAttributeVO> getValidAttribute() {
		List<BisAccAttributeVO> bisAccAttributeVO = BeanUtils.copyListProperties(bisAccAttributeDao.getValidAttribute(), BisAccAttributeVO.class);
		//inject(bisAccAttributeVO);
		return bisAccAttributeVO;
	}
	/**
	 * 获取可对外付款的账户属性
	 * @return
	 */
	public List<BisAccAttributeVO> getAttriButeForEign(){
		List<BisAccAttributeVO> bisAccAttributeVO = BeanUtils.copyListProperties(bisAccAttributeDao.getValidAttribute(), BisAccAttributeVO.class);
		//inject(bisAccAttributeVO);
		return bisAccAttributeVO;
	}
}
