package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cashVoucher.dao.BvPrintFormatDtlDao;
import com.bytter.cashVoucher.entity.BvPrintFormatDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
* @Title: Service
* @Description: 凭证打印格式明细
* @author onlineGenerator
* @date 2017-05-31 15:26:09
* @version 1.0
*
*/
@Service("bvPrintFormatDtlService")
@Transactional
public class BvPrintFormatDtlService extends BaseService<BvPrintFormatDtl,String> {
	@Autowired
	private BvPrintFormatDtlDao bvPrintFormatDtlDao;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvPrintFormatDtl entity){
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
 	public void save(BvPrintFormatDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BvPrintFormatDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvPrintFormatDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<BvPrintFormatDtl, String> getEntityDao() {
		return bvPrintFormatDtlDao;
	}
	/**
	 * 根据打印格式主信息ID
	 * 获取打印格式打印明细信息
	 *
	 * @return list集合
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatDtl> listPrintFmtDtlInfoByPrintId(String printId) {
		if(StringUtils.isBlank(printId)||"undefined".equals(printId)){
			return new ArrayList<BvPrintFormatDtl>();
		}
		List<BvPrintFormatDtl> dtlList = bvPrintFormatDtlDao.listPrintFmtDtlInfoByPrintId(printId);
		// 从sys获取字典数据
		if (ObjectUtil.isNotEmpty(dtlList)) {
			Map<String, DictDTO> typeDic = dictService.listMap("bv_val_print_type");
			Map<String, DictDTO> fmtDic = dictService.listMap("bv_val_print_fmt");
			dtlList.forEach(dtl -> {
				if (ObjectUtil.isNotEmpty(dtl.getValPrintType()) && ObjectUtil.isNotEmpty(typeDic)) {
					Optional.ofNullable(typeDic.get(dtl.getValPrintType())).ifPresent(dict -> dtl.setValPrintTypeName(dict.getName()));
				}
				if (ObjectUtil.isNotEmpty(dtl.getValPrintFmt()) && ObjectUtil.isNotEmpty(fmtDic)) {
					Optional.ofNullable(fmtDic.get(dtl.getValPrintFmt())).ifPresent(dict -> dtl.setValPrintFmtName(dict.getName()));
				}
			});
		}
		return dtlList;
	}
	
	/**
	 * 批量新增凭证打印打印明细规则
	 */
	public void batchBvPrintFormatDtl(List<BvPrintFormatDtl> listprintMR,String printId) {
		if(StringUtils.isNotBlank(printId)&&!"undefined".equals(printId)){
			for(BvPrintFormatDtl entity:listprintMR){
				entity.setPrintId(printId);
			}
		}
		batchBvPrintFormatDtl(listprintMR);
	}
	
	/**
	 * 批量新增凭证打印打印明细规则
	 */
	public void batchBvPrintFormatDtl(List<BvPrintFormatDtl> listprintMR) {
		for(BvPrintFormatDtl entity:listprintMR){
			bvPrintFormatDtlDao.save(entity);
		}
	}
	
	/**
	 * 批量删除数据
	 * @return
	 */
	public boolean batchDeleteByPrintIds(String[] pinrtIds){
		if(pinrtIds!=null&&pinrtIds.length>0){
			Map<String, Object> params=new HashMap<>();
			params.put("pinrtIds", pinrtIds);
			int num=bvPrintFormatDtlDao.batchDeleteByPrintIds(params);
			if(num>0){
				return true;
			}
		}
		return true;
	}
	/**
	 * 根据套打格式ID获取可以打印明细信息
	 * @param printId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvPrintFormatDtl> getIsPrintFmtInfoByPrintId(String printId){
		return bvPrintFormatDtlDao.getIsPrintFmtInfoByPrintId(printId);
	}
}
