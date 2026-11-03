package com.bytter.biz.dataexchange.basic.service;
import com.bytter.biz.dataexchange.basic.dao.ErpDataTypeDao;
import com.bytter.biz.dataexchange.basic.entity.ErpDataType;
import com.bytter.biz.dataexchange.basic.entity.ErpType;
import com.bytter.biz.dataexchange.basic.vo.ErpDataTypeVO;
import com.bytter.biz.dataexchange.basic.vo.ErpTypeVO;
import com.bytter.check.dto.ErpDataTypeDTO;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ISystemForeignService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: 数据类型
* @author onlineGenerator
* @date 2017-12-13 15:29:54
* @version 1.0
*
*/
@Service("erpDataTypeService")
@Transactional
public class ErpDataTypeService extends BaseService<ErpDataType,String> {
	@Autowired
	private ErpDataTypeDao erpDataTypeDao;
	@DubboReference(check = false)
	private ISystemForeignService systemForeignService;
	@DubboReference
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(ErpDataType entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(ErpDataType entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(ErpDataType entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(ErpDataType entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<ErpDataType, String> getEntityDao() {
		return erpDataTypeDao;
	}
	public ErpDataType getErpTypeById(String id) {
		ErpDataType vo = new ErpDataType();
		vo.setId(id);
		return erpDataTypeDao.getErpDataType(vo);
	}
	public ErpDataType getErpType(ErpDataType vo) {
		return erpDataTypeDao.getErpDataType(vo);
	}
	public ErpDataTypeVO getErpTypeVOById(String id) {
		ErpDataTypeVO vo = new ErpDataTypeVO();
		vo.setId(id);
		return  erpDataTypeDao.getErpDataTypeVO(vo);
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		DataTablesOutput output = erpDataTypeDao.getDataTablesUnion(dataTablesInput);
		return output;
	}
	/**
	 * 保存
	 * @param erpDataTypeVO
	 */
	public void save(ErpDataTypeVO erpDataTypeVO, ICurrentUser user){
		erpDataTypeVO.setCode(StringUtils.upperCase(erpDataTypeVO.getCode()));
		//检查编码重复问题
		if (isExistMulByCode(erpDataTypeVO.getCode(), CommonConstant.SUBMIT_ADD,erpDataTypeVO.getId())) {
			throw new ParameterException(DataExchangeMessageEnum.EXCHANGE_MESSAGE_CODE_REQUIRED_UNIQUE);
		}
		//检查数据有效
		validExecute(erpDataTypeVO);
		ErpDataType erpDataType = new ErpDataType();
		BeanUtils.copyNotNullProperties(erpDataTypeVO,erpDataType);
		erpDataType.setCreateBy(user.getLoginName());
		erpDataType.setCreateDate(DateUtils.getDate());
		this.saveEntity(erpDataType);
	}
	/**
	 * 更新
	 * @param erpDataTypeVO
	 */
	public void update(ErpDataTypeVO erpDataTypeVO, ICurrentUser user){
		erpDataTypeVO.setCode(StringUtils.upperCase(erpDataTypeVO.getCode()));
		//检查编码重复问题
		if (isExistMulByCode(erpDataTypeVO.getCode(), CommonConstant.SUBMIT_MODIFY,erpDataTypeVO.getId())) {
			throw new ParameterException(DataExchangeMessageEnum.EXCHANGE_MESSAGE_CODE_REQUIRED_UNIQUE);
		}
		//检查数据有效
		validExecute(erpDataTypeVO);
		ErpDataType erpDataType = this.getErpTypeById(erpDataTypeVO.getId());
		BeanUtils.copyNotNullProperties(erpDataTypeVO, erpDataType);
		// 修改人信息
		erpDataType.setUpdateBy(user.getLoginName());
		erpDataType.setUpdateDate(DateUtils.getDate());
		this.merge(erpDataType);
	}
	/**
	 * 判断是否存在多条
	 * @param code
	 * @param opType
	 * @param id
	 * @return
	 */
	public boolean isExistMulByCode(String code,String opType,String id){
		boolean flag = false;
		List<ErpDataType> list =erpDataTypeDao.listByCode(code);
		if(list!=null){
			if(CommonConstant.SUBMIT_ADD.equals(opType) && list.size()>0){
				flag = true;
			}else if(CommonConstant.SUBMIT_MODIFY.equals(opType)){
				if(list.size() >0 ){
					if(!id.equals(list.get(0).getId())){
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 检查
	 * @param erpDataTypeVO
	 */
	private void validExecute(ErpDataTypeVO erpDataTypeVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ").append(erpDataTypeVO.getKeyField()).append(",")
				.append(erpDataTypeVO.getValueField())
				.append(" from ").append(erpDataTypeVO.getTableName());
		if (StringUtils.isNotEmpty(erpDataTypeVO.getQueryCondition())) {
			sb.append(" where ").append(erpDataTypeVO.getQueryCondition());
		}
		try {
			systemForeignService.listBySQL(sb.toString(), null);
		} catch (Exception e) {
			logger.info("数据类型维护异常:" + erpDataTypeVO.toString());
			throw new ParameterException(DataExchangeMessageEnum.EXCHANGE_MESSAGE_PARAMS);
		}
	}
	/**
	 * 查询类型列表
	 * @param validSign
	 * @return
	 */
	public List<ErpDataType> listErpDataType(String validSign){
		List<ErpDataType> list = getEntityDao().listBy("validSign",validSign);
		return list;
	}
	public ErpDataType getErpDataTypeByCode(String code){
		return getEntityDao().getUniqueBy("code",code);
	}
	public List<ErpDataTypeDTO> listErpDataTypeByValidSign(String validSign) {
		List<ErpDataType> list = this.listErpDataType(validSign);
		return BeanUtils.copyListProperties(list,ErpDataTypeDTO.class);
	}
	public ErpDataTypeDTO getErpDataTypeDTOByCode(String code) {
		ErpDataType erpDataType = this.getErpDataTypeByCode(code);
		if(!StringUtils.isNotEmpty(erpDataType)) return null;
		ErpDataTypeDTO typeDTO = new ErpDataTypeDTO();
		BeanUtils.copyProperties(erpDataType, typeDTO);
		return typeDTO;
	}
}
