package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.FcVoucherTypeDao;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 付款方式维护维护
 * @date 2017.9.14
 */
@Service("fcVoucherTypeService")
@Transactional
public class FcVoucherTypeService extends BaseService<FcVoucherType, String> {
	@Autowired
	private FcVoucherTypeDao fcVoucherTypeDao;
	@Autowired
	private BtAssignPayWayService btAssignPayWayService;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FcVoucherType entity) {
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
	public void save(FcVoucherType entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FcVoucherType entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcVoucherType entity) {
		super.saveOrUpdate(entity);
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void deleteFcVoucherType(FcVoucherType entity) {
		if (CommonConstant.YES.equalsIgnoreCase(entity.getIsSystem())) {
			throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_IS_SYSTEM, entity.getVoucherName());
		}
		boolean flag = this.btAssignPayWayService.isUsedVoucherCode(entity.getId());
		if (flag) {
			throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_VOUCHERID_USE, entity.getVoucherName());
		}
		this.deleteEntity(entity);
		//删除多语言
		ii18nService.deleteBusinessBaseContext(entity.getId());
	}
	/**
	 * 批量删除对象
	 *
	 * @param ids
	 */
	public void batchDelete(String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			FcVoucherType entity = this.get(id);
			if ("1".equalsIgnoreCase(entity.getIsSystem())) {
				throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_IS_SYSTEM, entity.getVoucherName());
			}
			boolean flag = this.btAssignPayWayService.isUsedVoucherCode(entity.getId());
			if (flag) {
				throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_VOUCHERID_USE, entity.getVoucherName());
			}
		}
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			FcVoucherType fcVoucherType = this.get(id);
			this.deleteEntity(fcVoucherType);
			//删除多语言
			ii18nService.deleteBusinessBaseContext(fcVoucherType.getId());
		}
	}
	@Override
	public BaseDao<FcVoucherType, String> getEntityDao() {
		return fcVoucherTypeDao;
	}
	/**
	 * 根据编码获取付款方式
	 *
	 * @param voucherCode
	 * @return
	 */
	public FcVoucherType findByCode(String voucherCode) {
		return fcVoucherTypeDao.findByCode(voucherCode);
	}
	/**
	 * <b>功能：</b>查询所有有效供付款方式分配的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForAssign() {
		return fcVoucherTypeDao.getFcVoucherTypeForAssign();
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<FcVoucherTypeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        return fcVoucherTypeDao.getDataTablesUnion(dataTablesInput);
	}
	/**
	 * 根据ID获取付款方式信息
	 *
	 * @param id
	 * @return
	 */
	public FcVoucherTypeVO getByIdUnion(String id) {
		return fcVoucherTypeDao.getByIdUnion(id);
	}

    public FcVoucherTypeVO getByIdUnionById(String id) {
        return fcVoucherTypeDao.getByIdUnionById(id);
    }
	/**
	 * 根据ID获取付款方式信息
	 *
	 * @param id
	 * @return
	 */
	public FcVoucherType getById(String id) {
		return fcVoucherTypeDao.getById(id);
	}
	/**
	 * 根据系统结算方式获取对应的初始化的付款方式
	 *
	 * @param sysVoucherCode
	 * @return
	 */
	public FcVoucherTypeVO getSystemBySysVoucherCode(String sysVoucherCode) {
		return fcVoucherTypeDao.getSystemBySysVoucherCode(sysVoucherCode);
	}
	public List<FcVoucherTypeVO> getFcVoucherTypeVOBySysVoucherCodeS(String[] sysVoucherCodes) {
		return fcVoucherTypeDao.getFcVoucherTypeVOBySysVoucherCodeS(sysVoucherCodes);
	}
	/**
	 * 根据编码获取付款方式
	 *
	 * @param voucherCode
	 * @return
	 */
	public FcVoucherTypeDTO getFcVoucherTypeByCode(String voucherCode) {
		return fcVoucherTypeDao.getFcVoucherTypeByCode(voucherCode);
	}
	
	/**
	 * 根据付款方式名称获取付款方式
	 *
	 * @param voucherName
	 * @return
	 */
	public FcVoucherType getByVoucherName(String voucherName) {
		return fcVoucherTypeDao.getUniqueBy("voucherName", voucherName);
	}
	/**
	 * 根据付款方式名称获取付款方式
	 *
	 * @param voucherName
	 * @return
	 */
	public FcVoucherTypeDTO getDtoByVoucherName(String voucherName) {
		FcVoucherType fcVoucherType = fcVoucherTypeDao.getUniqueBy("voucherName", voucherName);
		FcVoucherTypeDTO  dto = new FcVoucherTypeDTO();
		if(fcVoucherType != null) {
			BeanUtils.copyProperties(fcVoucherType, dto); 
		}
		return dto;
	}
	
	/**
	 * 根据系统结算方式获取有效的付款方式信息
	 *
	 * @param sysVoucherCode
	 * @return
	 */
	public List<FcVoucherTypeVO> listBySysVoucherCode(String sysVoucherCode) {
		return fcVoucherTypeDao.listBySysVoucherCode(sysVoucherCode);
	}
	/**
	 * <b>功能：</b>查询所有有效、记账的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getFcVoucherType() {
		return fcVoucherTypeDao.getFcVoucherType();
	}
	/**
	 * 查询所有有效的付款方式
	 *
	 * @return
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeAll() {
		return fcVoucherTypeDao.getValidFcVoucherType();
	}
	/**
	 * <b>功能：</b>查询所有有效的付款方式<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getValidFcVoucherType() {
		return fcVoucherTypeDao.getValidFcVoucherType();
	}
	public Map<String ,FcVoucherTypeDTO> getValidFcVoucherTypeMap() {
		List<FcVoucherTypeDTO> voucherTypelist = fcVoucherTypeDao.getValidFcVoucherType();
		Map<String ,FcVoucherTypeDTO> voucherTypeMap=new HashMap<>();
		for(FcVoucherTypeDTO voucherType:voucherTypelist){
			voucherTypeMap.put(voucherType.getVoucherType(),voucherType);
		}
		return voucherTypeMap;
	}
	public Map<String ,FcVoucherTypeDTO> getFcVoucherTypeIdMap() {
		List<FcVoucherTypeDTO> voucherTypelist = fcVoucherTypeDao.getValidFcVoucherType();
		Map<String ,FcVoucherTypeDTO> voucherTypeMap=new HashMap<>();
		for(FcVoucherTypeDTO voucherType:voucherTypelist){
			voucherTypeMap.put(voucherType.getId(),voucherType);
		}
		return voucherTypeMap;
	}
	/**
	 * <b>功能：</b>查询所有有效供付款方式分配的付款方式(允许挂预算的)<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public List<FcVoucherTypeDTO> getVoucherTypeForBudget() {
		return fcVoucherTypeDao.getVoucherTypeForBudget();
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式(用于ERP付款转移的)
	 * @return
	 */
	public List<FcVoucherTypeDTO> getVoucherTypeForErp() {return fcVoucherTypeDao.getVoucherTypeForErp();}
	/**
	 * 保存付款方式
	 * @param entity
	 * @param locale
	 */
	public void saveFcVoucherType(FcVoucherType entity, String locale) {
		this.save(entity);
		//新增当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getVoucherName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 更新付款方式
	 * @param entity
	 * @param locale
	 */
	public void updateFcVoucherType(FcVoucherType entity, String locale) {
		//设置无效时校验已使用
		if(CommonConstant.NO.equals(entity.getValidSign())) {
			boolean flag = this.btAssignPayWayService.isUsedVoucherCode(entity.getId());
			if (flag) {
				throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_VOUCHERID_USE_NO_VALIDSIGN, entity.getVoucherName());
			}
		}
		this.saveOrUpdate(entity);
		//更新当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getVoucherName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式（内部账号流水查询）
	 * @return
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForReport() {return fcVoucherTypeDao.getFcVoucherTypeForReport();}
	/**
	 * 查询所有有效的结算业务相关的付款方式
	 * @return
	 */
	public List<FcVoucherTypeDTO> getFcVoucherTypeForSettlement() {return fcVoucherTypeDao.getFcVoucherTypeForSettlement();}
	/**
	 * <b>功能：</b>查询所有有效且生成指令的的付款方式<br>
	 * <b>日期：</b> 2022-07-27 <br>
	 */
	public List<FcVoucherTypeDTO> getValidVoucherTypeForExc() {
		return fcVoucherTypeDao.getValidVoucherTypeForExc();
	}
	public List<FcVoucherTypeDTO> getFcVoucherTypeByIds(List<String> voucherIds) {
		return fcVoucherTypeDao.getFcVoucherTypeByIds(voucherIds);
	}

	public List<FcVoucherTypeDTO> getFcVoucherTypeLikeName(String voucherName) {
		return fcVoucherTypeDao.getFcVoucherTypeLikeName(voucherName);
	}
}
