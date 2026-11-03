package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.dao.FcChargeTypeDao;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeType;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeTypeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Service("fcChargeTypeService")
@Transactional
public class FcChargeTypeService extends BaseService<FcChargeType,String> {
	@Autowired
	private FcChargeTypeDao fcChargeTypeDao;
	
	@Autowired
	private ChargeTypeBankTypeRelService chargeTypeBankTypeRelService;
	
	@Autowired
	private FcChargeTypeDtlService fcChargeTypeDtlService;
	
	@DubboReference(check = false)
	private IBankTypeService iBankTypeService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcChargeType entity){
		super.deleteEntity(entity);
	}
	
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 		deleteChargeBankByChargeId(id);//删除关联银行类型
 		deleteChargeDtlsByChargeId(id);//删除关联明细
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcChargeType fcChargeType){
 	}
 	
 	/**
 	 * <b>功能介绍:查询列表数据</b><br>
 	 * <b>日期：</b>2017年6月15日<br>
 	 * <b>作者：</b>caozx<br>
 	 * @version 1.0
 	 */
 	@SuppressWarnings("rawtypes")
 	@Transactional(readOnly = true)
	public DataTablesOutput listFcChargeType(DataTablesInput input){
 		return fcChargeTypeDao.listFcChargeType(input);
 	}
 	
 	
 	/**
 	 * <b>功能介绍:</b>保存费用类型<br>
 	 * <b>日期：</b>2017年6月15日<br>
 	 * <b>作者：</b>caozx<br>
 	 * @version 1.0
 	 */
 	public void saveFcChargeType(FcChargeTypeVo fcChargeTypeVo,String loginName){
		if(findByCode(fcChargeTypeVo.getChargeTypeCode())!=null){//CHARGE_TYPE_CODE唯一
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_CHARGE_TYPE_CODE_UNIQUE);
		}
//		if(fcChargeTypeVo.getChargeTypeDtls().size()==0){//明细不能为空
//			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_CHARGE_DTLS_NULL);
//		}
 		FcChargeType fcChargeType = new FcChargeType();
 		BeanUtils.copyProperties(fcChargeTypeVo, fcChargeType);
 		fcChargeType.setCreateBy(loginName);
 		fcChargeType.setCreateTime(new Date());
 		fcChargeTypeDao.save(fcChargeType);//保存明细
 		if(fcChargeTypeVo.getBankTypeRels()!=null){
 			chargeTypeBankTypeRelService.batchBankTypeRel(fcChargeTypeVo.getBankTypeRels(), fcChargeType.getId());//批量添加关联银行
 		}
 		if(fcChargeTypeVo.getChargeTypeDtls()!=null){
 			fcChargeTypeDtlService.batchAddFcChargeTypeDtl(fcChargeTypeVo.getChargeTypeDtls(), fcChargeType.getId());//批量添加明细
 		}
 		
 	}
 	
 	/**
 	 * <b>功能介绍:修改费用类型</b><br>
 	 * <b>日期：</b>2017年6月15日<br>
 	 * <b>作者：</b>caozx<br>
 	 * @version 1.0
 	 */
 	public void updateFcChargeType(FcChargeTypeVo fcChargeTypeVo,String loginName){
 		if(isExistsByCode(fcChargeTypeVo.getId(),fcChargeTypeVo.getChargeTypeCode())){//CHARGE_TYPE_CODE唯一
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_CHARGE_TYPE_CODE_UNIQUE);
		}
 		FcChargeType fcChargeType = this.get(fcChargeTypeVo.getId(),true);
 		fcChargeType.setUpdateBy(loginName);
 		fcChargeType.setUpdateTime(new Date());
 		BeanUtils.copyNotNullProperties(fcChargeTypeVo, fcChargeType);
 		if(EmptyUtils.isEmpty(fcChargeTypeVo.getCharge())){
 			fcChargeType.setCharge(null);
 		}
 		super.saveOrUpdate(fcChargeType);//更新操作
 		deleteChargeBankByChargeId(fcChargeType.getId());//删除关联银行类型
 		deleteChargeDtlsByChargeId(fcChargeType.getId());//删除关联明细
 		if(fcChargeTypeVo.getBankTypeRels()!=null){//先删除再重新新增
 			chargeTypeBankTypeRelService.batchBankTypeRel(fcChargeTypeVo.getBankTypeRels(), fcChargeType.getId());
 		}
 		if(fcChargeTypeVo.getChargeTypeDtls()!=null){
 			fcChargeTypeDtlService.batchAddFcChargeTypeDtl(fcChargeTypeVo.getChargeTypeDtls(), fcChargeType.getId());
 		}
 		
 	}
 	
 	/**
 	 * <b>功能介绍:删除费用类型</b><br>
 	 * <b>日期：</b>2017年6月15日<br>
 	 * <b>作者：</b>caozx<br>
 	 * @version 1.0
 	 */
 	public void deleteFcChargeType(FcChargeType entity){
 		if(isUsed(entity.getId())){//判断是否被引用
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_CHARGE_TYPE_USE);
		}
 		fcChargeTypeDao.delete(entity);
 		deleteChargeBankByChargeId(entity.getId());//删除关联银行类型
 		deleteChargeDtlsByChargeId(entity.getId());//删除关联明细
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcChargeType entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcChargeType entity,FcChargeType chargeType){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcChargeType, String> getEntityDao() {
		return fcChargeTypeDao;
	}
	
	/**
	 * 功能介绍:根据编码获取费用类型<br>
	 * <b>日期：</b>2017年5月27日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@Transactional(readOnly = true)
	public FcChargeType findByCode(String chargeCode){
		return fcChargeTypeDao.findByCode(chargeCode);
	}
	
	/**
	 * <b>功能介绍：</b>修改时编号校验<br>
	 * <b>日期：</b>2017年9月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @param chargeCode
	 * @return
	 */
	public boolean isExistsByCode(String id,String chargeCode){
		return fcChargeTypeDao.isExistsByCode(id,chargeCode);
	}
	
	/**
	 * 功能介绍:根据费用id查询费用明细<br>
	 * <b>日期：</b>2017年5月25日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public List<FcChargeTypeDtl> findByChargeTypeDtls(String id){
		return fcChargeTypeDao.findByChargeTypeDtls(id);
	}
	
	/**
	 * 功能介绍:根据id查找银行类型<br>
	 * <b>日期：</b>2017年5月25日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public List<String> findByBankTypeByChargeId(String id){
		return fcChargeTypeDao.findByBankTypeByChargeId(id);
	}
	
	/**
	 * <b>功能介绍：</b>根据id查找银行类型名称<br>
	 * <b>日期：</b>2017年9月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankTypes
	 * @return
	 */
	public List<String> findByBankTypeNameByBankTypeID(List<String> bankTypes){
//		return fcChargeTypeDao.findByBankTypeNameByBankTypeID(bankTypes);
		return iBankTypeService.getBankTypeNameByIds(bankTypes);
	}
	
	
	/**
	 * 功能介绍:删除关联银行类型<br>
	 * <b>日期：</b>2017年5月23日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteChargeBankByChargeId(String id){
		fcChargeTypeDao.deleteChargeBankByChargeId(id);
	}
	/**
	 * 功能介绍:删除相关明细<br>
	 * <b>日期：</b>2017年5月23日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteChargeDtlsByChargeId(String id){
		fcChargeTypeDao.deleteChargeDtlsByChargeId(id);
	}
	
	/**
	 * 功能介绍:费用是否被引用<br>
	 * <b>日期：</b>2017年5月27日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public boolean isUsed(String id){
		return fcChargeTypeDao.isUsed(id);
	}
	
	/**
	 * <b>功能介绍:取有效银行类别</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public List<BankTypeDTO> getBankTypeValid(){
		return iBankTypeService.listByValidSign("1");
	}
	
	/**
	 * 功能介绍:根据id获取费用详情<br>
	 * <b>日期：</b>2017年08月24日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcChargeTypeVo findById(String id){
		return fcChargeTypeDao.findById(id);
	}
}
