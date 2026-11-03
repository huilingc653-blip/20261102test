package com.bytter.cashVoucher.service;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.cashVoucher.dao.BvVoucherTypeDao;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.entity.BvVoutypeDataField;
import com.bytter.cashVoucher.entity.BvVoutypeScene;
import com.bytter.cashVoucher.enums.ViewModel;
import com.bytter.cashVoucher.enums.VoucherViewEnum;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @Title: Service
 * @Description: 凭证类型维护
 * @author onlineGenerator
 * @date 2017-05-25 14:20:46
 * @version 1.0
 *
 */
@Service("bvVoucherTypeService")
@Transactional
public class BvVoucherTypeService extends BaseService<BvVoucherType,String> {
	
	@Autowired
	private BvVoucherTypeDao bvVoucherTypeDao;
	
	@Autowired
	private BvVouInputService bvVouInputService;
	
	@Autowired
	private BvVoutypeDataFieldService bvVoutypeDataFieldService;
	
	@Autowired
	private BvVoutypeSceneService bvVoutypeSceneService;
	
	@Autowired
	private BvLogService bvLogService;
	
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	
	public BaseDao<BvVoucherType, String> getEntityDao() {
		return bvVoucherTypeDao;
	}
	
	/**
	 * 查询所有凭证类型                                                   
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherType> queryVoucherType(){
		return  bvVoucherTypeDao.queryVoucherType();
	}
	
	/**
	 * 查询所有凭证类型                                                   
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherTypeVO> queryVoucherTypeVO(){
	     return entityToVO(queryVoucherType());
	}
	
	/**
	 * 查询所有有效凭证类型                                                   
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherType> queryVoucherTypeValid(){
		return  bvVoucherTypeDao.queryVoucherTypeValid();
	}
	
	/**
	 * 查询所有有效凭证类型                                                   
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherTypeVO> queryVoucherTypeVOValid(){
	    return entityToVO(queryVoucherTypeValid());
	}
	
	public List<BvVoucherTypeVO> entityToVO(List<BvVoucherType> list){
		List<BvVoucherTypeVO> voList=null;
		if(ListUtils.isNotNullOrEmpty(list)){
			voList=BeanUtils.copyListProperties(list, BvVoucherTypeVO.class);
			// inject(voList);
		}
		return voList;
	}
	
	/**
	 * 获取需要入库且有效的凭证类型                                              
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherType> queryVoucherTypeValidByInbox(){
		return bvVoucherTypeDao.queryVoucherTypeValidByInbox();
	}
	
	/**
	 * 获取需要入库且有效的凭证类型                                              
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoucherTypeVO> queryVoucherTypeVOValidByInbox(){
		return entityToVO(queryVoucherTypeValidByInbox());
	}
	/**
	 * 获取有效的凭证类型 Map                                      
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String,BvVoucherType> getVoucherTypeMapValid(){
		List<BvVoucherType> vouTypeInfo=queryVoucherTypeValidByInbox();
		Map<String,BvVoucherType> vouTypeMap=new HashMap<>();
		if(!ListUtils.isNullOrEmpty(vouTypeInfo)){
			for(BvVoucherType info:vouTypeInfo){
				vouTypeMap.put(info.getName(), info);
			}
		}
		return vouTypeMap;
	}
	  	
	/**
	 * 判断输入的表或视图是否符合要求                                           
	 * @param tableName
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isExistTable(String tableName) {
		return bvVoucherTypeDao.isExistTable(tableName);
	}
	
	/**
	 * 判断字段在表或视图中是否存在                                     
	 * @param columnName tableName 
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isExistColumn(String columnName, String tableName) {
		// 获取对应的表
		// 根据视图的数据来源来获取视图所属的模块
		ViewModel viewModel = VoucherViewEnum.getModelByView(tableName);
		VoucherViewEnum voucherEnum = VoucherViewEnum.getEnumByView(tableName);
		Optional.ofNullable(viewModel).ifPresent(model->{
			switch (model) {
				case VIEW_PAYEE:
					// 从【凭证模块】获取凭证打印数据
					// listData.set(listBvPrintVouOpByVoucher(input, selectField, corpAuthSql, params, isOrder.get()));
					break;
				case VIEW_NIS:
					// 从【结算模块】获取凭证打印数据
					System.out.println("从【结算模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
					// listData.set(new DataTablesOutput<>());
					break;
				case VIEW_BILL:
					// 从【票据模块】获取凭证打印数据
					System.out.println("从【票据模块】获取凭证打印数据>>>>>>>>>>>>>>>>>");
					// listData.set(new DataTablesOutput<>());
					break;
				default:
					System.out.println("当前模块业务尚未实现：model = " + model.getDesc());
					break;
			}
		});
		return bvVoucherTypeDao.isExistColumn(columnName,tableName);
	}
	
	/**
	 * 根据条件查询凭证种类信息
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listVoucherType(DataTablesInput dataTablesInput) {
		return bvVoucherTypeDao.listVoucherType(dataTablesInput);
	}
	
	/**
	 * 根据id查询凭证种类信息
	 * @param
	 * @return
	 */
	@Transactional(readOnly = true)
	public BvVoucherTypeVO getVoucherTypeById(String id){
		return bvVoucherTypeDao.getVoucherTypeById(id);
	}
	
	/**
	 * 校验输入的凭证种类代码是否重复
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean checkVoucherTypeCode(String opType,String code,String id) {
		int size=bvVoucherTypeDao.checkVoucherTypeCode(opType, code, id);
		if(size>0){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 修改凭证种类信息
	 */
	public void modifyBvVoucherType(BvVoucherTypeVO  bvVoucherTypeVO ,String userCode,String userName,ICurrentUser userVo){
		BvVoucherType entity=bvVoucherTypeVO.getBvVoucherType();
		entity.setUpdateBy(userCode);
		entity.setUpdateName(userName);
		entity.setUpdateDate(new Date());
		bvVoucherTypeDao.update(entity);
		bvVoucherTypeVO.setId(entity.getId());
		//取值字段明细，先删除再新增
		bvVoutypeDataFieldService.batchDeleteByVouTid(entity.getId());
		//打印场景，先删除再新增
		bvVoutypeSceneService.batchDeleteByVouTid(entity.getId());
		List<BvVoutypeDataField> entityList=bvVoucherTypeVO.getBvVoutypeDataFieldList();
		if(entityList!=null&&!entityList.isEmpty()){						
			bvVoutypeDataFieldService.batchBvVoutypeDataField(bvVoucherTypeVO.getBvVoutypeDataFieldList(),bvVoucherTypeVO);
		}
		List<BvVoutypeScene> entitySceneList=bvVoucherTypeVO.getBvVoutypeSceneList();
		if(entitySceneList!=null&&!entitySceneList.isEmpty()){						
			bvVoutypeSceneService.batchBvVoutypeScene(entitySceneList,bvVoucherTypeVO);
		}
		if(ii18nService!=null){
			ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(entity.getId(), bvVoucherTypeVO.getLocale(), entity.getName()));
		}
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_UPDATE, BlankvouConstant.LANG_LOG_UPDATE_SUCC,userVo);
	}
	
	/**
	 * 新增凭证种类信息
	 */
	public void saveBvVoucherType(BvVoucherTypeVO  bvVoucherTypeVO ,String userCode,String userName,ICurrentUser userVo){
		BvVoucherType entity=bvVoucherTypeVO.getBvVoucherType();
		entity.setCreateBy(userCode);
		entity.setCreateName(userName);
		entity.setCreateDate(new Date());
		bvVoucherTypeDao.save(entity);
		bvVoucherTypeVO.setId(entity.getId());
		//新增取值字段明细
		List<BvVoutypeDataField> entityList=bvVoucherTypeVO.getBvVoutypeDataFieldList();
		if(entityList!=null&&!entityList.isEmpty()){			
			bvVoutypeDataFieldService.batchBvVoutypeDataField(bvVoucherTypeVO.getBvVoutypeDataFieldList(),bvVoucherTypeVO);
		}
		List<BvVoutypeScene> entitySceneList = bvVoucherTypeVO.getBvVoutypeSceneList();
		if(entitySceneList != null && !entitySceneList.isEmpty()){
			bvVoutypeSceneService.batchBvVoutypeScene(entitySceneList,bvVoucherTypeVO);
		}else {
			throw new ParameterException("参数异常，请联系管理员");
		}
		if(ii18nService!=null){
			ii18nService.saveOrUpdateBusinessBaseContext(new I18nDTO(entity.getId(), bvVoucherTypeVO.getLocale(), entity.getName()));
		}
		bvLogService.saveBvLog(entity.getId(), BlankvouConstant.BVLOG_LOGTYPE_ADD, BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
		
	}
	
	/**
	 * 删除凭证类型维护
	 * @param id
	 * @return
	 */
	public boolean deleteBvVoucherType(String id,ICurrentUser userVo) {
		if(StringUtils.isBlank(id)){
			throw new BusinessException(BlankvouMessageEnum.BLANDVOU_MESSAGE_ERROR_VALISNOTNULL);
		}
		BvVoucherType bvVoucherType =bvVoucherTypeDao.get(id);
		boolean flag =bvVouInputService.checkBvVoucherType(id); 
		if(!flag){
			throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ISUSED_BANDELETE);
		}
		if (flag) {
			bvVoucherType.setValidSign("2");
			bvVoucherTypeDao.update(bvVoucherType);
			bvLogService.saveBvLog(bvVoucherType.getId(), BlankvouConstant.BVLOG_LOGTYPE_DELETE,BlankvouConstant.LANG_LOG_SAVE_SUCC,userVo);
		}
		return flag;
	}
	
	/**
	 * 批量删除凭证类型维护
	 * @param ids
	 * @return
	 */
	public boolean batchDeleteBvVoucherType(String ids,ICurrentUser userVo) {
		if (StringUtils.isBlank(ids)) {
			throw new BusinessException(BlankvouMessageEnum.BLANDVOU_MESSAGE_ERROR_VALISNOTNULL);
		}
		boolean flag = true;
		String[] idArrays = ids.split(",");
		//获取无法删除数据行数
		for (int i=0;i<idArrays.length;i++) {
			// 若存在凭证类型被使用，不允许删除
			if (!bvVouInputService.checkBvVoucherType(idArrays[i])) {
				throw new BusinessException(BlankvouMessageEnum.BLANKVOU_MESSAGE_ISUSED_BANBATCHDELETE,"["+(i+1)+"]");
			}
		}
		if (flag) {
			for (String id : idArrays) {
				BvVoucherType bvVoucherType = bvVoucherTypeDao.get(id);
				bvVoucherType.setValidSign("2");
				bvVoucherTypeDao.update(bvVoucherType);
			}
			bvLogService.batchSaveBvLog(idArrays, BlankvouConstant.BVLOG_LOGTYPE_DELETE,BlankvouConstant.LANG_LOG_BATCHDELETE_SUCC,userVo);
		}
		return flag;
	}
	
}
