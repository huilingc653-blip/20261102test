package com.bytter.biz.dataexchange.basic.service;
import com.bytter.biz.dataexchange.basic.dao.ErpTypeDao;
import com.bytter.biz.dataexchange.basic.entity.ErpType;
import com.bytter.biz.dataexchange.basic.vo.ErpTypeVO;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: 接口类型
* @author onlineGenerator
* @date 2017-12-13 14:53:07
* @version 1.0
*
*/
@Service("erpTypeService")
@Transactional
public class ErpTypeService extends BaseService<ErpType,String> {
	@Autowired
	private ErpTypeDao erpTypeDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(ErpType entity){
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
 	public void save(ErpType entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(ErpType entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(ErpType entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<ErpType, String> getEntityDao() {
		return erpTypeDao;
	}
	public ErpType getErpTypeById(String id) {
		ErpTypeVO vo = new ErpTypeVO();
		vo.setId(id);
		return erpTypeDao.getErpType(vo);
	}
	public ErpType getErpType(ErpTypeVO vo) {
		return erpTypeDao.getErpType(vo);
	}
	public ErpTypeVO getErpTypeVOById(String id) {
		ErpTypeVO vo = new ErpTypeVO();
		vo.setId(id);
		return erpTypeDao.getErpTypeVO(vo);
	}
	public ErpTypeVO getErpTypeVO(ErpTypeVO vo) {
		return erpTypeDao.getErpTypeVO(vo);
	}
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
		 return erpTypeDao.getDataTables(dataTablesInput);
	}
	/**
	 * 保存
	 * @param erpTypeVO
	 */
	public void save(ErpTypeVO erpTypeVO, ICurrentUser user ){
		//检查编码重复问题
		if (isExistMulByCode(erpTypeVO.getTypeCode(), CommonConstant.SUBMIT_ADD,erpTypeVO.getId())) {
			throw new ParameterException(DataExchangeMessageEnum.EXCHANGE_MESSAGE_TYPECODE_REQUIRED_UNIQUE);
		}
		ErpType erpType = new ErpType();
		BeanUtils.copyNotNullProperties(erpTypeVO,erpType);
		erpType.setCreateBy(user.getLoginName());
		erpType.setCreateDate(DateUtils.getDate());
		this.saveEntity(erpType);
	}
	/**
	 * 更新
	 * @param erpTypeVO
	 */
	public void update(ErpTypeVO erpTypeVO, ICurrentUser user){
		//检查编码重复问题
		if (isExistMulByCode(erpTypeVO.getTypeCode(), CommonConstant.SUBMIT_MODIFY,erpTypeVO.getId())) {
			throw new ParameterException(DataExchangeMessageEnum.EXCHANGE_MESSAGE_TYPECODE_REQUIRED_UNIQUE);
		}
		ErpType erpType = this.getErpTypeById(erpTypeVO.getId());
		BeanUtils.copyNotNullProperties(erpTypeVO, erpType);
		// 修改人信息
		erpType.setUpdateBy(user.getLoginName());
		erpType.setUpdateDate(DateUtils.getDate());
		this.merge(erpType);
	}
	/**
	 * 判断是否存在多条
	 * @param typeCode
	 * @param opType
	 * @param id
	 * @return
	 */
	public boolean isExistMulByCode(String typeCode,String opType,String id){
		boolean flag = false;
		List<ErpType> list =erpTypeDao.listBy("typeCode",typeCode);
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
	 * 查询类型列表
	 * @return
	 */
	public List<ErpTypeVO> listErpType(){
		List<ErpType> typeList=getEntityDao().listAll();
		List<ErpTypeVO> list = null;
		if(!ListUtils.isNullOrEmpty(typeList)){
			list= BeanUtils.copyListProperties(typeList, ErpTypeVO.class);
		}
		return list;
	}
	
	/**
	 * 获取接口类型MAP信息
	 * @return
	 */
	public Map<String,String> getErpTypeMap(){
		Map<String,String> erpTypeMap=new HashMap<>();
		List<ErpTypeVO> infoList=listErpType();
		if(!ListUtils.isNullOrEmpty(infoList)){
			for(ErpTypeVO info:infoList){
				erpTypeMap.put(info.getTypeCode(), info.getTypeName());
			}
		}
		return erpTypeMap;
	}
	public ErpTypeVO getErpTypeByCode(String code){
		ErpTypeVO vo=new ErpTypeVO();
		vo.setTypeCode(code);
		return this.erpTypeDao.getErpTypeVO(vo);
	}
}
