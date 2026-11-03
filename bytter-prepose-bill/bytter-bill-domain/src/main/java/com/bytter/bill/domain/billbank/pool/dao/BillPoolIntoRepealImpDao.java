package com.bytter.bill.domain.billbank.pool.dao;
import com.bytter.bill.domain.billbank.dao.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据池入池撤销申请操作DAO<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2021-11-29 <br>
 */
@SuppressWarnings("rawtypes")
@Repository("billPoolIntoRepealImpDao")
public class BillPoolIntoRepealImpDao extends BaseDao {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 根据参数判断是否有数据
	 * @param params 参数
	 * @return 是否有数据
	 */
	@SuppressWarnings("unchecked")
	public boolean isAbPoolIntoRepeal(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select count(*) as num from AB_POOL_INTO_REPEAL where 1=1 ");
		if(params.containsKey("origNo")){
			sql.append(" and ORIG_NO=:origNo ");
		}
		if(params.containsKey("transDate")){
			sql.append(" and TRANS_DATE=:transDate ");
		}
		if(params.containsKey("validSign")){
			sql.append(" and VALID_SIGN=:validSign ");
		}
		List<Map<String, Object>> mapList = this.listBySQL(sql.toString(), params);
		if(mapList!=null&&mapList.size()>0){
			int num=Integer.parseInt(String.valueOf(mapList.get(0).get("num")));
			if(num>0){
				return true;
			}
		}
		return false;
	}
	/**
	 * 根据撤销id修改入池撤销是否有效
	 * @param rid 撤销id
	 * @param validSign 是否有效
	 * @return 结果数量
	 */
	@SuppressWarnings("unchecked")
	public int updatePoolIntoRepealByRid(String rid,String validSign){
		StringBuffer sql = new StringBuffer("update AB_POOL_INTO_REPEAL set VALID_SIGN=:validSign where id=:rid ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("validSign", validSign);
		params.put("rid", rid);
		return this.batchSQLExecute(sql.toString(), params);
	}
	
}
