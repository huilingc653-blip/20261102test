package com.bytter.biz.dataexchange.basic.service;
import com.bytter.biz.dataexchange.basic.dao.ErpDynamicDataDao;
import com.bytter.biz.dataexchange.basic.dao.ErpDynamicDataTypeDao;
import com.bytter.biz.dataexchange.basic.entity.ErpDynamicData;
import com.bytter.biz.dataexchange.basic.entity.ErpDynamicDataType;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataListVO;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataTypeVO;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataVO;
import com.bytter.check.constant.DataexchangeConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: 自定义数据映射
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
*/
@Service("erpDynamicDataService")
@Transactional
public class ErpDynamicDataService extends BaseService<ErpDynamicData,String> implements IUserExcelVerifyHandler<ErpDynamicDataVO> {
	@Autowired
	private ErpTypeService erpTypeService;
	
	@Autowired
	private ErpDynamicDataDao erpDynamicDataDao;
	@Autowired
	private ErpDynamicDataTypeDao erpDynamicDataTypeDao;
	
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	public BaseDao<ErpDynamicData, String> getEntityDao() {
		return erpDynamicDataDao;
	}
	/**
	 * 获取所有自定义数据类型
	 * @return 返回数据集合
	 */
	@Transactional(readOnly = true)
    public List<ErpDynamicDataTypeVO> getAllDynamicDataType() {
		List<ErpDynamicDataType> typeList=erpDynamicDataTypeDao.getAllDynamicDataType();
		List<ErpDynamicDataTypeVO> list = BeanUtils.copyListProperties(typeList, ErpDynamicDataTypeVO.class);
        return list;
    }
	
	/**
	 *  获取所有自定义数据类型MAP信息
	 * @return
	 */
	public Map<String,ErpDynamicDataTypeVO> getDynamicDataTypeMap(){
		Map<String,ErpDynamicDataTypeVO> dynamicDataType=new HashMap<>();
		List<ErpDynamicDataTypeVO> infoList=getAllDynamicDataType();
		if(!ListUtils.isNullOrEmpty(infoList)){
			for(ErpDynamicDataTypeVO info:infoList){
				dynamicDataType.put(info.getDataTypeCode(), info);
			}
		}
		return dynamicDataType;
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
		return erpDynamicDataDao.getDataTables(dataTablesInput);
	}
	/**
	 * 新增修改操作
	 * @param erpBasicDataList
	 * @param user
	 */
	public void saveOrUpdateErpDynamicData(ErpDynamicDataListVO erpBasicDataList, ICurrentUser user){
		if(erpBasicDataList!=null&&!ListUtils.isNullOrEmpty(erpBasicDataList.getDataList())){
			List<ErpDynamicDataVO> dataList=erpBasicDataList.getDataList();
			for(ErpDynamicDataVO info:dataList){
				info.setTypeCode(erpBasicDataList.getTypeCode());
				info.setDataTypeId(erpBasicDataList.getDataTypeId());
				//对数据类型Id为空时判断新增操作
				if(StringUtils.isEmpty(info.getDataTypeId())){
					if(StringUtils.isNotEmpty(erpBasicDataList.getDataTypeCode())){
						Map<String,ErpDynamicDataTypeVO> dataTypeMap=getDynamicDataTypeMap();
						if(dataTypeMap.containsKey(erpBasicDataList.getDataTypeCode())){
							info.setDataTypeId(dataTypeMap.get(erpBasicDataList.getDataTypeCode()).getId());
						}else{
							info.setDataTypeCode(erpBasicDataList.getDataTypeCode());
							info.setDataTypeName(erpBasicDataList.getDataTypeName());
							ErpDynamicDataType datatype=saveErpDynamicDataType(info, user);
							info.setDataTypeId(datatype.getId());
						}
					}
				}
				saveOrUpdateErpDynamicData(info, user);
			}
		}
	}
	
	/**
	 * 新增或者修改自定义数据映射信息
	 * @param erpDynamicDataVO
	 * @param user
	 */
	public void saveOrUpdateErpDynamicData(ErpDynamicDataVO erpDynamicDataVO, ICurrentUser user){
		ErpDynamicData info=null;
		if(StringUtils.isEmpty(erpDynamicDataVO.getId())){
			info=new ErpDynamicData();
			info.setCreateBy(user.getId());
			info.setCreateDate(new Date());
		}else{
			info=erpDynamicDataDao.get(erpDynamicDataVO.getId(), true);
			info.setUpdateBy(user.getId());
			info.setUpdateDate(new Date());
		}
		BeanUtils.copyNotNullProperties(erpDynamicDataVO, info);
		erpDynamicDataDao.saveOrUpdate(info);
	}
	/**
	 * 新增或者修改自定义数据映射信息
	 * @param erpDynamicData
	 * @param userVo
	 * @return
	 */
	public ErpDynamicDataType saveErpDynamicDataType(ErpDynamicDataVO erpDynamicData, ICurrentUser userVo){
		ErpDynamicDataType info=new ErpDynamicDataType();
		info.setDataTypeCode(erpDynamicData.getDataTypeCode());
		info.setDataTypeName(erpDynamicData.getDataTypeName());
		info.setCreateBy(userVo.getId());
		info.setCreateDate(new Date());
		erpDynamicDataTypeDao.save(info);
		return info;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(ErpDynamicDataVO obj, Object... user) {
		return null;
	}
	@Override
	@Transactional(readOnly = true)
	public void verifyHandler(List<ErpDynamicDataVO> list, Object... user) {
		Map<String,String> erpTypeMap=erpTypeService.getErpTypeMap();
		Map<String,ErpDynamicDataTypeVO> dataTypeMap=getDynamicDataTypeMap();
		if(!ListUtils.isNullOrEmpty(list)){
			for(ErpDynamicDataVO info:list){
				List<String> errorList = new ArrayList<>();
				if(StringUtils.isEmpty(info.getTypeCode())){
					errorList.add(DataexchangeConstant.LANG_ERROR_TYPECODE_ISNOTNULL);
				}else{
					if(!erpTypeMap.containsKey(info.getTypeCode())){
						errorList.add(DataexchangeConstant.LANG_ERROR_TYPECODE_ISNOTIN);
					}else{
						info.setTypeName(erpTypeMap.get(info.getTypeCode()));
					}
				}
				
				if(StringUtils.isEmpty(info.getDataTypeCode())){
					errorList.add(DataexchangeConstant.LANG_ERROR_DATATYPECODE_ISNOTNULL);
				}else{
					if(!dataTypeMap.containsKey(info.getDataTypeCode())){
						if(StringUtils.isEmpty(info.getDataTypeName())){
							errorList.add(DataexchangeConstant.LANG_ERROR_DATATYPENAME_ISNOTNULL);
						}else{
							dataTypeMap.put(info.getDataTypeCode(), new ErpDynamicDataTypeVO(info.getDataTypeCode(),info.getDataTypeName()));
						}
					}else{
						info.setDataTypeName(dataTypeMap.get(info.getDataTypeCode()).getDataTypeName());
					}
				}
				if(StringUtils.isEmpty(info.getInnerCode())){
					errorList.add(DataexchangeConstant.LANG_ERROR_INNERCODE_ISNOTNULL);
				}
//				if(StringUtils.isEmpty(info.getExternalCode())){
//					errorList.add(DataexchangeConstant.LANG_ERROR_EXTERNALCODE_ISNOTNULL);
//				}
				info.addErrorMsg(errorList);
			}
		}
		
		
	}
	
	/**
	 * 导入自定义数据对应信息
	 * @param list 导入vo信息
	 * @return 处理结果
	 */
	public boolean saveImport(List<ErpDynamicDataVO> list, ICurrentUser userVo) {
		Map<String,ErpDynamicDataTypeVO> dataTypeMap=getDynamicDataTypeMap();
		for (ErpDynamicDataVO info : list) {
			if(!dataTypeMap.containsKey(info.getDataTypeCode())){
				ErpDynamicDataType datatype=saveErpDynamicDataType(info, userVo);
				dataTypeMap.put(info.getDataTypeCode(), new ErpDynamicDataTypeVO(datatype.getId(),datatype.getDataTypeCode(),datatype.getDataTypeName()));
			}
			if(dataTypeMap.get(info.getDataTypeCode())!=null){
				info.setDataTypeId(dataTypeMap.get(info.getDataTypeCode()).getId());
				saveOrUpdateErpDynamicData(info, userVo);
			}
		}
		return true;
	}
	
	/**
	 * 进行批量删除操作
	 * @param ids
	 * @param userVo
	 * @return
	 */
	public boolean batchDelete(String ids, ICurrentUser userVo){
		if(StringUtils.isNotEmpty(ids)){
			String[] idArr= StringUtils.split(ids,",");
			int size=erpDynamicDataDao.batchDelete(idArr);
			if(size>0){
				return true;
			}
		}
		return false;
	}
	/**
	 * 进行批量删除操作
	 * @param typeCode
	 * @param dataTypeId
	 * @return
	 */
	public List<ErpDynamicDataVO> getDynamicDataByMap(String typeCode,String dataTypeId){
		List<ErpDynamicDataVO> listInfo= erpDynamicDataDao.getErpDynamicDataVOList(typeCode, dataTypeId);
		return listInfo;
	}
	/**
	 * 获取自定义数据映射
	 *
	 * @param typeCode     对应接口类型
	 * @param dateTypeCode 对应数据类型
	 * @return
	 * @author zuoy 2018-05-17
	 */
	public List<ErpDynamicDataVO> listErpDynamicDataByTypeCode(String typeCode, String dateTypeCode) {
		return this.erpDynamicDataDao.listErpDynamicDataByTypeCode(typeCode, dateTypeCode);
	}
	
}
