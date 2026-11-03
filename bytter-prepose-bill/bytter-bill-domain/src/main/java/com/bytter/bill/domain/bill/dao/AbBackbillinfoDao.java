package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBackbillinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据背面信息
* @author onlineGenerator
* @date 2017-06-21 15:36:23
* @version 1.0
*/
@Repository
public class AbBackbillinfoDao extends BaseDao<AbBackbillinfo,String>{
	/**
	 * 根据票据号获取票据背面信息
	 * @param origNo
	 * @param isDesc 是否降序
	 * @return
	 */
	public List<AbBackbillinfo> listAbBackbillinfoByOrigNo(String origNo,boolean isDesc,String sbbllRngStrtSn, String sbbllRngEndSn){
		StringBuilder hql=new StringBuilder("from AbBackbillinfo A where A.origNo=:origNo");
		
		Map<String,Object> params=new HashMap<>();
		if(sbbllRngStrtSn != null && sbbllRngEndSn != null && (sbbllRngStrtSn + sbbllRngEndSn).length() == 24){
			hql = hql.append(" and A.sbbllRngStrtSn=:sbbllRngStrtSn and A.sbbllRngEndSn=:sbbllRngEndSn");
			params.put("sbbllRngStrtSn", sbbllRngStrtSn);
			params.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		if(isDesc){
			hql.append(" order by A.bussdt desc,A.msgid desc,A.msdt desc,A.id desc");
		}else{
			hql.append("  order by A.bussdt,A.msgid,A.msdt,A.id");
		}
		params.put("origNo", origNo);
		List<AbBackbillinfo> list=this.listByHQL(hql.toString(), params);
		return list;
	}
	/**
	 * 根据票据号获取票据背面信息
	 * @param info
	 * @param bustype 业务类型
	 * @param isDesc 是否降序
	 * @return
	 */
	public List<AbBackbillinfo> listAbBackbillinfoByOrigNoAndsbbllRngSn(AbVoucherinfo info, boolean isDesc, String bustype){
		StringBuilder hql=new StringBuilder("from AbBackbillinfo A where A.origNo=:origNo");
		Map<String,Object> params=new HashMap<>();
		if(info.getSbbllRngStrtSn() != null && info.getSbbllRngStrtSn() != null && (info.getSbbllRngStrtSn() + info.getSbbllRngEndSn()).length() == 24){
			hql = hql.append(" and A.sbbllRngStrtSn=:sbbllRngStrtSn and A.sbbllRngEndSn=:sbbllRngEndSn");
			params.put("sbbllRngStrtSn", info.getSbbllRngStrtSn());
			params.put("sbbllRngEndSn", info.getSbbllRngStrtSn());
		}
		if (StringUtils.isNotBlank(bustype)){
			String[] split = bustype.split(",");
			StringBuilder strB = new StringBuilder();
			for (String s : split) {
				strB.append("'").append(s).append("'").append(",");
			}
			hql.append(" and A.bustype in (").append(strB.substring(0, strB.length()-1)).append(")");
		}
		if(info.getPayeeacc() != null){
			hql.append(" and A.recAcctId ='"+info.getPayeeacc() +"'");
		}
		if(info.getPayeebankno() != null){
			hql.append(" and A.recAcctsvcr ='"+info.getPayeebankno() +"'");
		}
		if(isDesc){
			hql.append(" order by A.bussdt desc,A.msgid desc,A.msdt desc,A.id desc");
		}else{
			hql.append("  order by A.bussdt,A.msgid,A.msdt,A.id");
		}
		params.put("origNo", info.getOrigNo());
		List<AbBackbillinfo> list=this.listByHQL(hql.toString(), params);
		return list;
	}
}
