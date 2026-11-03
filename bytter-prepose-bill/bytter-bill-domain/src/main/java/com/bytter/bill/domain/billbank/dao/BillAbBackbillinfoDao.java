package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.entity.BillAbBackbillinfo;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据背面信息
* @author onlineGenerator
* @date 2017-09-02
* @version 1.0
*
*/
@Repository
public class BillAbBackbillinfoDao extends BaseDao<BillAbBackbillinfo,String>{
	/**
	 * 
	 * <b>方法描述：</b>根据票据号码删除数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
/*	public void deleteByOrigNo(String origNo) {
		String hql = " DELETE BillAbBackbillinfo WHERE origNo=:origNo";
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("origNo", origNo);
		this.batchExecute(hql,params);
	}*/
	/**
	 * 根据票号和子票区间号删除数据
	 * @param origNo 票号
	 * @param sbbllRngStrtSn 子票区间开始序号
	 * @param sbbllRngEndSn 子票区间结束序号
	 */
	public void deleteByOrigNoAndRngNo(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn) {
		String hql = " DELETE BillAbBackbillinfo WHERE origNo=:origNo and sbbllRngStrtSn=:sbbllRngStrtSn and sbbllRngEndSn=:sbbllRngEndSn";
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("origNo", origNo);
		params.put("sbbllRngStrtSn", StringUtils.isBlank(sbbllRngStrtSn) ? "0" : sbbllRngStrtSn);
		params.put("sbbllRngEndSn", StringUtils.isBlank(sbbllRngEndSn) ? "0" : sbbllRngEndSn);
		this.batchExecute(hql,params);
	}
	@SuppressWarnings("unchecked")
	/*public List<BillAbBackbillinfo> getBillBackInfoByOrigNo(String origNo) {
		String hql = " FROM BillAbBackbillinfo b WHERE b.origNo = :origNo ORDER BY b.bussdt asc,b.id asc";
//		String hql = " FROM BillAbBackbillinfo b WHERE b.origNo = :origNo ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("origNo", origNo);
		return this.listByHQL(hql, params);
	}*/
	/**
	 * 根据票号和子票区间号获取数据
	 * @param origNo 票号
	 * @param sbbllRngStrtSn 子票区间开始序号
	 * @param sbbllRngEndSn 子票区间结束序号
	 */
	public List<BillAbBackbillinfo> getBillBackInfoByOrigNoAndRngNo(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn) {
		String hql = " FROM BillAbBackbillinfo b WHERE b.origNo = :origNo and sbbllRngStrtSn=:sbbllRngStrtSn and sbbllRngEndSn=:sbbllRngEndSn ORDER BY b.bussdt asc,b.id asc";
//		String hql = " FROM BillAbBackbillinfo b WHERE b.origNo = :origNo ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("origNo", origNo);
		params.put("sbbllRngStrtSn", StringUtils.isBlank(sbbllRngStrtSn) ? "0" : sbbllRngStrtSn);
		params.put("sbbllRngEndSn", StringUtils.isBlank(sbbllRngEndSn) ? "0" : sbbllRngEndSn);
		return this.listByHQL(hql, params);
	}
	
}
