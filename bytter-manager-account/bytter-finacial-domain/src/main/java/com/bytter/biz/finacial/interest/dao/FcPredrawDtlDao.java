package com.bytter.biz.finacial.interest.dao;

import com.bytter.biz.finacial.interest.entity.FcPredrawDtl;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlVo;
import com.bytter.biz.finacial.interest.vo.InterestFactSheetVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
* @Title: 利息管理--预提明细查询表 
* @Description: FC_PREDRAW_DTL
* @author zhengxn
* @date 2017-06-28 13:55:58
* @version 1.0
*
*/
@Repository
public class FcPredrawDtlDao extends BaseDao<FcPredrawDtl,String>{
	
	/**预提明细查询表 列表 
	 * <b>日期：</b>2017年6月24日<br>
	 * @version 1.0
	 * @author zhengxn
	 */
	public DataTablesOutput listAll(DataTablesInput input,List<String> accounts){
		StringBuilder sql = new StringBuilder();
		sql.append("select p.account as account, p.order_no as orderNo, p.interest_time as interestTime,i.interest_start_date as interestStartDate,i.bln_inst_date as blnInstDate,");
		sql.append("i.interest_rate as interestRate,p.interest as interest,r.strike_date as strikeDate, r.strike_interest as strikeInterest");
		sql.append(" from fc_predraw_dtl p left join fc_interest_dtl i on  i. predraw_dtl_id = p.id  And i.interest_sign = '0'");
		sql.append(" left join fc_predraw_rush_dtl r on r.predraw_dtl_id = p.id and r.status = 95 ");
		sql.append(" where 1=1 ");
		sql.append(" and ").append(getSQLIn(accounts, 900, "p.CORP_ID"));
		return getDynamicBySQL(input, sql.toString(), FcPredrawDtlVo.class,"",false);
	}
	
	/**利息预提情况表 列表 
	 * <b>日期：</b>2017年10月10日<br>
	 * @version 1.0
	 * @author zhengxn
	 */
	
	public DataTablesOutput queryFactSheet(DataTablesInput input,List<String> accounts){
		StringBuilder sql = new StringBuilder();
		/*sql.append(" select a.corp_id as corpId,c.name as corpName,a.blnctr_acc as blnctrAcc,a.acc_name as accName,a.acc_type as accType,");
		sql.append(" p.Interest_time as interestTime,i.interest_start_date as interestStartDate,i.bln_inst_date as blnInstDate,");
		sql.append("i.days as days,i.interest_type as interestType,i.interest_rate as interestRate,i.interest_sign as interestSign,i.interest as interest");
		sql.append(" from fc_predraw_dtl p,fc_interest_dtl i,fc_acc a,sys_corp c");
		sql.append(" where i.predraw_dtl_id = p.id");
		sql.append(" and i.account = a.blnctr_acc");
		sql.append(" and a.corp_id = c.id and a.pool_sign != '1'");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));*/
		sql.append(" select i.account as blnctrAcc, ");
		sql.append(" p.Interest_time as interestTime,i.interest_start_date as interestStartDate,i.bln_inst_date as blnInstDate,");
		sql.append("i.days as days,i.interest_type as interestType,i.interest_rate as interestRate,i.interest_sign as interestSign,i.interest as interest");
		sql.append(" from fc_predraw_dtl p,fc_interest_dtl i ");
		sql.append(" where i.predraw_dtl_id = p.id");
		sql.append(" and ").append(getSQLIn(accounts, 900, "i.account"));
		return getDynamicBySQL(input, sql.toString(), InterestFactSheetVo.class,"",false);
	}
	
	/*public Map<String, Object> queryFactSheet(FcPredrawDtlVo vo){
		Map<String,Object> result = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.corp_id,c.name as corpName,a.blnctr_acc as account,a.acc_name as accName,a.acc_type,");//差利率类型，批量计息时没保存，没有这个字段
		sql.append(" p.Interest_time,i.interest_start_date,i.bln_inst_date,i.days,i.interest_type,i.interest_rate,p.interest");
		sql.append(" from fc_predraw_dtl p,fc_interest_dtl i,fc_acc a,sys_corp c");
		sql.append(" where i.predraw_dtl_id = p.id");
		sql.append(" and i.account = a.blnctr_acc");
		sql.append(" and a.corp_id = c.id");
		if(EmptyUtils.isNotEmpty(vo.getNetId()) && !" ".equals(vo.getNetId())){
			sql.append(" and a.net_id = :netId");
		}
		if(EmptyUtils.isNotEmpty(vo.getCorpId()) && !" ".equals(vo.getCorpId())){
			sql.append(" and a.corp_id = :corpId");
		}
		if(EmptyUtils.isNotEmpty(vo.getCurId()) && !" ".equals(vo.getCurId())){
			sql.append(" and p.cur_code = :curCode");
		}
		if(EmptyUtils.isNotEmpty(vo.getAccType()) && !" ".equals(vo.getAccType())){
			sql.append(" and a.acc_type = :accType");
		}
		if(EmptyUtils.isNotEmpty(vo.getAccount())){//账号他们还没写好，现在取不出来
			sql.append(" and account = :account");
		}
		String geDate = "";
		String leDate = "";
		if(vo.getGe_interestTime() != null || vo.getLe_interestTime() != null){
			geDate=DateUtils.formatDate(vo.getGe_interestTime(), "yyyy-MM-dd");
			leDate=DateUtils.formatDate(vo.getLe_interestTime(), "yyyy-MM-dd");
			sql.append(" and p.Interest_time>=BT_TO_DATE(:geDate,'yyyy-mm-dd') and p.Interest_time<=BT_TO_DATE(:leDate,'yyyy-mm-dd')");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("netId",vo.getNetId() );
		params.put("corpId",vo.getCorpId());
		params.put("curCode",vo.getCurId());
		params.put("accType",vo.getAccType());
		//params.put("account",vo.getAccount());
		params.put("geDate",geDate);
		params.put("leDate",leDate);
		List<FcPredrawDtlVo>  res = listBySQLAliasToBean(sql.toString(),params,FcPredrawDtlVo.class);
		
		FcPredrawDtlVo data = new FcPredrawDtlVo();
		BigDecimal sumInterest = new BigDecimal(0); 
		for (FcPredrawDtlVo fcPredrawDtlVo : res) {
			sumInterest.add(fcPredrawDtlVo.getInterest()==null?new BigDecimal(0):fcPredrawDtlVo.getInterest());
		}
		data.setInterest(sumInterest);
		data.setCorpName("合计");
		res.add(data);
		
		if(res.isEmpty()){
			result.put("res", new ArrayList());
		}else{
			result.put("size", res.size());
			result.put("res", res);
		}
		
		return result;
	}*/
	/**
	 * 根据币别id得到名称
	 * 功能介绍。<br>
	 * <b>日期：</b>2017年10月10日<br>
	 * @version 1.0
	 * @author zhengxn 
	 */
	public String getCurNameById(String currencyId) {
	        String sql = "select b.cur_name  from Bt_Currency b where id =:currencyId";
	        return (String) createSQLQuery(sql).setString("currencyId", currencyId).uniqueResult();
	}
}
