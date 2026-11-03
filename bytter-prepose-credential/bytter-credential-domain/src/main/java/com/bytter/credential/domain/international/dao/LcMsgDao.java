package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: CMS_PROCESS_MSG
* @author onlineGenerator
* @date 2017-09-14 14:32:39
* @version 1.0
*
*/
@Repository
public class LcMsgDao extends BaseDao<LcMessage,String>{
	/**
	 * <b>功能介绍：</b>查询所有日志<br>
	 * @version 1.0
	 * @param billCode 多个用逗号隔开
	 * @return
	 */
	public DataTablesOutput listMsgData(String billCode,DataTablesInput input){
		String[] billCodes = billCode.split(",");
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < billCodes.length; i++) {
			codes.add(billCodes[i]);
		}
		StringBuilder sql = new StringBuilder("select  create_By,message_Type,message , create_Date from lc_message  ");
		sql.append(" where 1=1  and  ");
		sql.append(getSQLIn(codes, 900, "bill_no"));
		sql.append(" order by create_Date desc ");
		//return listByHQL(hql.toString(), null);
		return getDynamicBySQL(input, sql.toString(), LcMessage.class, "", false);
	}
	/**
	 * 获取业务数据ID
	 *
	 * @param billCode
	 * @return
	 */
	public List<Map<Object, Object>>  getIdByBillCode(String billCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select v.ID,v.TA from (  ");
		sql.append(" select ID,'LC_ENROL' as TA  from LC_ENROL where LC_NO=:billCode ");
		sql.append(" union ");
		sql.append(" select ID,'LC_BILL_ARRIVAL' as TA from  LC_BILL_ARRIVAL  where ARRIVAL_CODE=:billCode ");
		sql.append(" union ");
		sql.append(" select ID,'LC_MORTGAGE' as TA from LC_MORTGAGE where MORTGAGE_NO=:billCode ");
				sql.append(" ) v ");
		Map<String, Object> map = new HashMap<>();
		map.put("billCode", billCode);
		List<Map<Object, Object>> list = this.listBySQL(sql.toString(), map);
		if (list != null && list.size() > 0){
			return list;
		}else {
			return null;
		}
	}
}
