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
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.dao.BtAccNatureDao;
import com.bytter.system.entity.BtAccNature;
import com.bytter.system.vo.BtAccNatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @Description 账户性质维护
 * @date 2017.9.14
 */
@Service("btAccNatureService")
@Transactional
public class BtAccNatureService extends BaseService<BtAccNature, String> {
	@Autowired
	private BtAccNatureDao btAccNatureDao;
	@Autowired
	private IBtBankAccService btBankAccServiceI;
	@Autowired
	private Ii18nService i18nServiceI;
	@Autowired
	private IDictService dictServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtAccNature entity) {
		super.deleteEntity(entity);
		//删除多语言
		i18nServiceI.deleteBusinessBaseContext(entity.getId());
	}
	public DataTablesOutput<BtAccNatureVO> dataTable(DataTablesInput dataTablesInput){
		DataTablesOutput<BtAccNatureVO> result=new DataTablesOutput<>();
		DataTablesOutput<BtAccNature> dataTablesOutput = this.getDataTables(dataTablesInput);
		BeanUtils.copyProperties(dataTablesOutput, result);
		List<BtAccNature> data = dataTablesOutput.getData();
		if(EmptyUtils.isNotEmpty(data)){
			Map<String, DictDTO> dictMap = dictServiceI.listMap("valid_sign");
			List<BtAccNatureVO> btAccNatureVO = 	BeanUtil.copyToList(data,BtAccNatureVO.class);;
			for(BtAccNatureVO vo :btAccNatureVO){
				vo.setNatureNameShow(vo.getNatureName());
				if(EmptyUtils.isNotEmpty(vo.getValidSign())){
					DictDTO dto=dictMap.get(vo.getValidSign());
					if(dto!=null){
						vo.setValidSignName(dictMap.get(vo.getValidSign()).getName());
					}
				}
			}
			result.setData(btAccNatureVO);
		}
		return result;
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
	public BtAccNatureVO getVO(String id){
		BtAccNature btAccNature = super.get(id);
		BtAccNatureVO btAccNatureVO = BeanUtils.copyNewInstanceProperties(btAccNature, BtAccNatureVO.class);
		btAccNatureVO.setValidSignName(dictServiceI.get("sf_yn",btAccNatureVO.getValidSign() ));
//		inject(btAccNatureVO);
		return btAccNatureVO;
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BtAccNature entity, String locale) {
		super.saveEntity(entity);
		//新增当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getNatureName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(BtAccNature entity, String locale) {
		super.saveOrUpdate(entity);
		//更新当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getNatureName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void deleteBtAccNature(BtAccNature entity) {
		// 判断账户属性信息是否被占用
		boolean flag = this.btBankAccServiceI.isUsedBaseData("natureId", entity.getId());
		if (flag) {
			throw new ParameterException(NetbankMessageEnum.NIS_ACCATTR_MESSAGE_ATTRIBUTEID_USE, entity.getNatureName());
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
			BtAccNature btAccNature = this.get(id);
			this.deleteBtAccNature(btAccNature);
		}
	}
	@Override
	public BaseDao<BtAccNature, String> getEntityDao() {
		return btAccNatureDao;
	}
	/**
	 * 根据编码获取账户性质
	 *
	 * @param natureCode
	 * @return
	 */
	public BtAccNature findByCode(String natureCode) {
		return btAccNatureDao.findByCode(natureCode);
	}
	/**
	 * 获取有效的账户属性
	 *
	 * @return
	 */
	public List<BtAccNatureVO> getValidAccNature() {
		List<BtAccNatureVO> btAccNatureVO = BeanUtils.copyListProperties(btAccNatureDao.getValidAccNature(), BtAccNatureVO.class);
//		inject(btAccNatureVO);

		return btAccNatureVO;
	}
	/**
	 * 判断币别编码是否重复
	 *
	 * @param id
	 * @param natureCode
	 * @return
	 */
	public boolean isRepeatCurCode(String id, String natureCode) {
		// 根据编码查询匹配的科目性质列表
		List<BtAccNature> sameCodeList = btAccNatureDao.listBy("natureCode", natureCode);
		if (EmptyUtils.isEmpty(sameCodeList)) {
			return false;
		}
		// 新增场景，只要有同编码记录，直接重复
		if (StringUtils.isBlank(id)) {
			return true;
		}
		// 编辑场景：判断列表里是否存在【不是当前这条id】的记录
		return sameCodeList.stream()
				.anyMatch(record -> !id.equals(String.valueOf(record.getId())));
	}
}
