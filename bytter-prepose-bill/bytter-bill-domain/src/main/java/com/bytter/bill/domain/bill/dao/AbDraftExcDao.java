package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDraftExc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class AbDraftExcDao extends BaseDao<AbDraftExc,String>{
	/**
	 * 根据票据号+账号 查询状态不等于（-1，19）时指令表数据
	 */
	@SuppressWarnings("unchecked")
	public List<AbDraftExc> queryAbDraftExcByParam(String origNo,String payerAcc,String msgType, String sbbllRngStrtSn, String sbbllRngEndSn) {
		StringBuffer hql = new StringBuffer("FROM AbDraftExc WHERE status not in (-1,19) and operDate >:todayDate");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("todayDate", DateUtils.getDateYMD());
		if(!StringUtils.isBlank(origNo)) {
			hql.append(" AND origNo = :origNo");
			param.put("origNo", origNo);
		}
		if(!StringUtils.isBlank(sbbllRngStrtSn)) {
			hql.append(" AND sbbllRngStrtSn = :sbbllRngStrtSn");
			param.put("sbbllRngStrtSn", sbbllRngStrtSn);
		}
		if(!StringUtils.isBlank(sbbllRngEndSn)) {
			hql.append(" AND sbbllRngEndSn = :sbbllRngEndSn");
			param.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		if(!StringUtils.isBlank(payerAcc)) {
			hql.append(" AND payerAcc = :payerAcc");
			param.put("payerAcc",  payerAcc);
		}
		if(!StringUtils.isBlank(msgType)) {
			hql.append(" AND msgType = :msgType");
			param.put("msgType",  msgType);
		}
		return this.listByHQL(hql.toString(), param);
	}
	/**
	 * 查询是否线上
	* @Title: getIsSendSign
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param vid
	* @param @return    参数
	* @return int    返回类型
	* @throws
	 */
	public int getIsSendSign(String vid){
		StringBuffer hql = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		hql.append(" From AbAccrecords t where 1=1 ");
		if(!StringUtils.isBlank(vid)) {
			hql.append(" AND t.vid = :vid");
			param.put("vid", vid);
		}
		hql.append(" order by applyDate DESC");
	    @SuppressWarnings("unchecked")
		List<AbAccrecords> abAccrecordsList = this.listByHQL(hql.toString(), param);
	    if(abAccrecordsList.size()>0){
	    	if(abAccrecordsList.get(0).getSendSign().equals("1")){
	    		return 1;
	    	}
	    }
	    return 0;
	}
}
