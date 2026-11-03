package com.bytter.check.dao;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareCheckType;
import com.bytter.check.vo.FcCompareCheckTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: Dao
* @Description: FC_COMPARE_CHECK_TYPE
* @author onlineGenerator
* @date 2018-01-03 14:14:11
* @version 1.0
*
*/
@Repository
public class FcCompareCheckTypeDao extends BaseDao<FcCompareCheckType,String>{
	
	/**
	 * <b>功能介绍：</b>根据核对标准ID删除明细<br>
	 * <b>日期：</b>2018年1月3日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkTypeId
	 */
	public void deleteCheckTypeDtl(String checkTypeId){
		String sql = "delete from FC_CHECK_TYPE_DTL where check_type_id = ? ";
		createSQLQuery(sql, checkTypeId).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>根据核对标准ID查询所有核对标准<br>
	 * <b>日期：</b>2018年1月3日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getcheckTypesBycheckTypeId(String checkTypeId){
		String sql = "select dtl.check_type from fc_check_type_dtl dtl where dtl.check_type_id = ? ";
		return createSQLQuery(sql, checkTypeId).list();
	}
	
	/**
	 * <b>功能介绍：</b>查询所有有效核对标准<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public List<FcCompareCheckTypeVO> getCheckTypeList(){
		Map<String,Object> params = new HashMap<String,Object>();
		String sql = "select t.id,t.check_type_name from fc_compare_check_type t where t.valid_sign = :validSign and t.status!= :status order by t.sort asc ";
		params.put("validSign", FinacialConstant.VALID_TRUE);
		params.put("status", CommonConstant.STATUE_DELETE);
		return listBySQLAliasToBean(sql, params, FcCompareCheckTypeVO.class);
	}
}
