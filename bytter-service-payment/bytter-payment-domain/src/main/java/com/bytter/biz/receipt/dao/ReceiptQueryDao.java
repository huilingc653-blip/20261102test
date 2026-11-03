package com.bytter.biz.receipt.dao;

import com.bytter.core.base.AbstractDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
/**
* @author zhengg
* @Title: Dao
* @Description: 票据签章处理dao
* @date 2017-08-22 17:34:11
* @version 1.0
*
*/
@SuppressWarnings("rawtypes")
@Repository
public class ReceiptQueryDao extends AbstractDao{


	/**
	 * 根据orderNo查询委托收款数据
	 * @param orderNo
	 * @return
	 */
	public Integer getCgsBillNum(String orderNo){
		int num=0;
		StringBuilder sql=new StringBuilder();
		sql.append("select count(*) as num from CGS_BILL where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty("orderNo")){
			params.put("orderNo", orderNo);
			sql.append(" and order_no=:orderNo ");
		}else{
			return 0;
		}
		
		@SuppressWarnings("unchecked")
		Map reInfo=this.getBySQL(sql.toString(), params);
		if(reInfo.containsKey("num")){
			num=Integer.parseInt(String.valueOf(reInfo.get("num")));
		}
		if(reInfo.containsKey("NUM")){
			num=Integer.parseInt(String.valueOf(reInfo.get("NUM")));
		}
		return num;
	}
}
