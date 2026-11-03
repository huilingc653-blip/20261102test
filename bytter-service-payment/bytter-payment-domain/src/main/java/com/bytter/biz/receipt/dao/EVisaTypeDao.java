package com.bytter.biz.receipt.dao;
import java.util.HashMap;
import java.util.List;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import com.bytter.biz.receipt.entity.EVisaType;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
* @Title: Dao
* @Description: 签章类型维护
* @author onlineGenerator
* @date 2017-06-05 10:52:28
* @version 1.0
*
*/
@Repository
public class EVisaTypeDao extends BaseDao<EVisaType,String>{
	/**
	 * 根据id,状态判断是作废还是恢复单据
	 * @param id
	 * @param opType 0 作废 1 恢复
	 */
	public void updateOperate(String id,String opType){
		if("0".equals(opType)){
			createQuery("update EVisaType set validSign = 0 where id =?0",id).executeUpdate();
		}else{
			createQuery("update EVisaType set validSign = 1 where id =?0",id).executeUpdate();
		}
	}
	
	/**
	 * 根据网点查找已维护过的回单类型
	 * @param netId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> findBillType(String netId){
		return createQuery("select billType from EVisaType where netId =?0",netId).list();
	}
	/**
	 * 根据id查找回单类型
	 * @param id
	 * @return
	 */
	public EVisaType getById(String id){
		StringBuilder sql=new StringBuilder();
		sql.append("select * from e_visa_type e");
		sql.append(" where e.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, EVisaType.class);
	}
	/**
	 * 根据id查找回单类型
	 * @param id
	 * @return
	 */
	public EVisaTypeVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder();
		sql.append("select e.id as id,e.net_id as netId,e.bill_type as billType,e.bill_name as billName,e.file_name as fileName,");
		sql.append(" e.pic_width as picWidth,e.pic_height as picHeight,e.valid_sign as validSign from e_visa_type e");
		sql.append(" where e.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, EVisaTypeVO.class);
	}
	
	/**
	 * 查询电子签章类型列表
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput findListBysql(DataTablesInput input,String authSql){
		StringBuilder sql = new StringBuilder();
		sql.append("select e.id as id,e.net_id as netId,e.bill_type as billType,e.bill_name as billName,e.pic_height as picHeight,");
		sql.append(" e.pic_width as picWidth,e.valid_sign as validSign,e.file_name as fileName from e_visa_type e where 1=1 ");
		if(StringUtils.isNotEmpty(authSql)){
			sql.append(authSql);
		}
	    return getDynamicBySQL(input, sql.toString(), EVisaTypeVO.class, "e", false);
	}
	public EVisaTypeVO getVOByParams(String billType,String netId,String validSign){
		StringBuilder sql=new StringBuilder();
		sql.append("select e.id as id,e.net_id as netId,e.bill_type as billType,e.bill_name as billName,e.file_name as fileName,");
		sql.append(" e.pic_width as picWidth,e.pic_height as picHeight,e.valid_sign as validSign from e_visa_type e");
		sql.append(" where e.bill_type=:billType and e.net_id=:netId and e.valid_sign=:validSign");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billType", billType);
		params.put("netId", netId);
		params.put("validSign", validSign);
		return getBySQLAliasToBean(sql.toString(), params, EVisaTypeVO.class);
	}
}
