package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.cashVoucher.dao.BvVoutypeDataFieldDao;
import com.bytter.cashVoucher.entity.BvVoutypeDataField;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.cashVoucher.vo.BvVoutypeDataFieldVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: Service
 * @Description: BV_VOUTYPE_DATA_FIELD
 * @author onlineGenerator
 * @date 2017-05-26 20:50:38
 * @version 1.0
 *
 */
@Service("bvVoutypeDataFieldService")
@Transactional
public class BvVoutypeDataFieldService extends BaseService<BvVoutypeDataField, String> {
	
	@Autowired
	private BvVoutypeDataFieldDao bvVoutypeDataFieldDao;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 删除对象
	 * @param entity
	 */
	@Override
	public void deleteEntity(BvVoutypeDataField entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(BvVoutypeDataField entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(BvVoutypeDataField entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BvVoutypeDataField entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BvVoutypeDataField, String> getEntityDao() {
		return bvVoutypeDataFieldDao;
	}
	
	/**
	 * 根据vouTid获取有效的取值字段明细信息  
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoutypeDataField> listVoutypeDataFieldValidByVouTid(String vouTid) {
		return bvVoutypeDataFieldDao.listVoutypeDataFieldValidByVouTid(vouTid);
	}
  /**
   * 根据vouTid获取有效的取值字段明细信息  
   * @return
   */
	@Transactional(readOnly = true)
	public List<BvVoutypeDataFieldVO> listVoutypeDataFieldVOValidByVouTid(String vouTid) {
		return entityToVO(listVoutypeDataFieldValidByVouTid(vouTid));
	}
	public List<BvVoutypeDataFieldVO> entityToVO(List<BvVoutypeDataField> list){
		List<BvVoutypeDataFieldVO> voList=null;
		if(ListUtils.isNotNullOrEmpty(list)){
			voList=BeanUtils.copyListProperties(list, BvVoutypeDataFieldVO.class);
//			inject(voList);
			for(BvVoutypeDataFieldVO voInfo:voList){
				voInfo.setFieldName(multiLangServiceI.getLang(voInfo.getFieldName()));
			}
		}
		return voList;
	}
	/**
	 * 根据条件查询凭证种类信息
	 * @param opType
	 * @param vouTid
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvVoutypeDataFieldVO> listBvVoutypeDataField(String opType, String vouTid) {
		List<BvVoutypeDataFieldVO> listInfo = new ArrayList<BvVoutypeDataFieldVO>();
		BvVoutypeDataFieldVO idEntity = new BvVoutypeDataFieldVO();
		BvVoutypeDataFieldVO corpIdEntity = new BvVoutypeDataFieldVO();
		idEntity.setFieldCode("id");
//		idEntity.setFieldName("数据唯一id");
		idEntity.setFieldName(BlankvouConstant.BV_OPID_LONG_KEY);
		idEntity.setValidSign("1");
		idEntity.setIsShow("1");
		corpIdEntity.setFieldCode("corp_id");
//		corpIdEntity.setFieldName("对应单位id");
		corpIdEntity.setFieldName(BlankvouConstant.BV_OPCORPID_LONG_KEY);
		corpIdEntity.setValidSign("1");
		corpIdEntity.setIsShow("1");
		// this.inject(idEntity);
		// this.inject(corpIdEntity);
		if ("add".equals(opType)) {			
			listInfo.add(idEntity);
			listInfo.add(corpIdEntity);
		}
		if("view".equals(opType) && StringUtils.isNotBlank(vouTid)){
			listInfo=bvVoutypeDataFieldDao.listBvVoutypeDataFieldByVouTid(vouTid);
		}
		if("update".equals(opType) && StringUtils.isNotBlank(vouTid)){
			List<BvVoutypeDataFieldVO> initInfo = bvVoutypeDataFieldDao.listBvVoutypeDataField(vouTid);
			//取值数据如果为空,添加初始化数据
			if(initInfo==null||(initInfo!=null&&initInfo.isEmpty())){
            initInfo=new ArrayList<BvVoutypeDataFieldVO>();
            initInfo.add(idEntity);
            initInfo.add(corpIdEntity);
			}
			listInfo.addAll(initInfo);	
		}
		// todo:从SYS远程获取sys_dict_group的数据
		Map<String, DictDTO> listMap = dictService.listMap("bv_voutype_field_datatype");
		if (ObjectUtil.isNotEmpty(listMap) && ObjectUtil.isNotEmpty(listInfo)) {
			listInfo.forEach(data -> {
				if (ObjectUtil.isNotEmpty(data.getDataType())) {
					Optional.ofNullable(listMap.get(data.getDataType())).ifPresent(dict -> data.setDataTypeName(dict.getName()));
				}
			});
		}
		return listInfo;
	}
  
	/**
	 * 批量删除凭证种类取值字段明细
	 */
	public void batchDeleteByVouTid(String vouTid) {
		bvVoutypeDataFieldDao.deleteByVouTid(vouTid);
		
	}
	/**
	 * 批量新增凭证种类取值字段明细
	 */
	public void batchBvVoutypeDataField(List<BvVoutypeDataField> entityList,BvVoucherTypeVO  bvVoucherTypeVO) {
		//添加操作人代码，名称，时间
		int count=entityList.size();
		BvVoutypeDataField opCodeEntity= new BvVoutypeDataField();
		BvVoutypeDataField opNameEntity= new BvVoutypeDataField();
		BvVoutypeDataField opDateEntity= new BvVoutypeDataField();
		int opCodeNum=count;
		int opNameNum=count+1;
		int opDateNum=count+2;
		opCodeEntity.setFieldCode("popopcode");
		opCodeEntity.setFieldName("操作人代码");
		// opCodeEntity.setFieldName(BlankvouConstant.BV_OPCODE_LONG_KEY);
		opCodeEntity.setValidSign("1");
		opCodeEntity.setIsShow("0");
		opCodeEntity.setOrdernum(opCodeNum);
		
		opNameEntity.setFieldCode("popopname");
		opNameEntity.setFieldName("操作人名称");
		// opNameEntity.setFieldName(BlankvouConstant.BV_OPNAME_LONG_KEY);
		opNameEntity.setValidSign("1");
		opNameEntity.setIsShow("0");
		opNameEntity.setOrdernum(opNameNum);
		
		opDateEntity.setFieldCode("popopdate");	
		opDateEntity.setFieldName("操作时间");
		// opDateEntity.setFieldName(BlankvouConstant.BV_OPDATE_LONG_KEY);
		opDateEntity.setValidSign("1");
		opDateEntity.setIsShow("0");
		opDateEntity.setOrdernum(opDateNum);
		entityList.add(opCodeEntity);
		entityList.add(opNameEntity);
		entityList.add(opDateEntity);
		for (int i=0;i<entityList.size();i++) { 
			BvVoutypeDataField entity = entityList.get(i);
			entity.setVouTid(bvVoucherTypeVO.getId());
			entity.setOrdernum(i+1);
			bvVoutypeDataFieldDao.save(entity);
		}
	}
}
