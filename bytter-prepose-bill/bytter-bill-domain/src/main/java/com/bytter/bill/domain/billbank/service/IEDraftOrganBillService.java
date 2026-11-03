package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.vo.EDraftOrganBillVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>机构服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-09-02 <br>
 */
public interface IEDraftOrganBillService {
	
	/**
	 * 
	 * <b>方法描述：</b>根据机构类型或机构名称或机构号查询机构信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-08-16 <br>
	 * @param organType 机构类型
	 * @param organName 机构名称
	 * @param organId 机构号
	 */
	List<EDraftOrganBillVo> queryEDraftOrganByParam(String organType, String organName, Long organId);
	
	/**
	 *
	 * <b>方法描述：</b>根据机构名称查询机构号<br>
	 * <b>作者：</b>zhengg<br>
	 * <b>日期：</b> 2018-04-11 <br>
	 */
	String queryEDraftOrganIdByOrganName(String organName);
	/**
	 * 
	 * <b>方法描述：</b>删除指定机构类型的数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-08-16 <br>
	 * @param organType
	 */
	void deleteByType(String organType);
	
	/**
	 * 
	 * <b>方法描述：</b>删除所有数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-08-16 <br>
	 */
	void deleteAll();
	
	/**
	 * 
	 * <b>方法描述：</b>添加数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-08-16 <br>
	 */
	void addAll(List<EDraftOrganBillVo> eDraftOrganBillList);
}
