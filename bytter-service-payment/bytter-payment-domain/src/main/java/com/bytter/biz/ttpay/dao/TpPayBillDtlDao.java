package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillDtlVO;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: DAO
 * @Description: 商户信息对账单明细Dao
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
@Repository
public class TpPayBillDtlDao extends BaseDao<T,String>{
	
	/**
	 * 
	* @Title: 商户信息流水查询信息查询列表  
	* @param  input
	* @return DataTablesOutput    返回类型  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,List<String> corpAuthList){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" d.id,d.appId,d.mchId,d.serialNo,d.trandNo,d.payAcc,d.trandTime,d.trandMoney,m.corp_id as corpId,m.PAY_TYPE as payType ");
		sql.append(" from ").append(TtPayUtil.getAllFieldSql()).append(" d ");
		sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.var_value_str = d.").append(TtPayUtil.APPID).append(" ");
		sql.append(" left join tp_merchant m on m.id = mv.mch_id and d.payType = m.PAY_TYPE ");
		//sql.append(" left join sys_corp c on c.id=m.corp_id ");
		sql.append(" where 1=1 and m.valid_sign = :validSign ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("appIds", TtPayConstant.APPIDS);
		params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
		if(corpAuthList==null||corpAuthList.size()<1){
			sql.append(" and 1!=1 ");
		}else{
			sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
		}
		return getDynamicBySQL(input, sql.toString(),TpPayBillDtlVO.class,"d", false,params);
	}
	
	/**
	 * 
	 * @Title: 商户信息对账单明细信息查询列表  
	 * @param  input
	 * @return DataTablesOutput    返回类型  
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDtlViewDataTablesUnion(DataTablesInput input){
		String payType=String.valueOf(input.getFormdata().get("payType"));
		String billDateStr=String.valueOf(input.getFormdata().get("billDate"));
		String appId=String.valueOf(input.getFormdata().get("appId"));
		String listSql=TtPayUtil.getTableListFieldSql(payType);
		if(listSql==null){
			return null;
		}
		StringBuilder sql=new StringBuilder(listSql);
		if (StringUtils.isNotEmpty(appId)) {
			sql.append(" and APP_ID = '" + appId + "'");
		}
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(billDateStr)){
			Date billDate=DateUtils.str2Date(billDateStr, DateUtils.date_sdf());
			params.put("beginTransTime", billDate);
			params.put("endTransTime", DateUtils.addDay(billDate, 1));
		}
		return getDynamicMapBySQL(input, sql.toString(),"", false,params);
	}
	
	/**
	 * 
	* @Title: 查询商户信息交易流水明细
	* @param id
	* @param payType
	* @return 明细数据
	* @throws
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getPayBillDtlInfoByIdOrPayType(String id,String payType){
		String listSql=TtPayUtil.getTableViewFieldSql(payType);
		if(listSql==null||StringUtils.isEmpty(id)){
			return null;
		}
		StringBuilder sql=new StringBuilder(listSql);
		sql.append(" and id=:id");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("id", id);
		List list = this.listMapBySQL(sql.toString(), params);
		if(list!=null){
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}
	/**
	 * 查看交易的全部退款数据
	 * @param dtlId 交易流水id
	 * @return 退款数据
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TpRefundOrderVO> listRefundInfo(String dtlId){
		StringBuilder sql=new StringBuilder();
		sql.append("select t.back_no as backNo,t.create_Date as backDate,t.app_Status as appStatus,t.msg as msg, ");
		sql.append(" t.voucher_Stat as voucherStat,BT_CONCAT4('(',t.create_By,')',t.create_name) as createByAndName,");
		sql.append(" t.back_Money as backMoney,t.rmk as rmk,t.id as id from TP_REFUND_ORDER t where t.dtl_id=:dtlId order by t.create_Date");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("dtlId", dtlId);
		List<TpRefundOrderVO> listInfo=this.listBySQLAliasToBean(sql.toString(), params, TpRefundOrderVO.class);
		if(listInfo!=null){
			return listInfo;
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: 获取各支付方式对账单字段和表信息
	 * @return list    返回类型  
	 * @throws
	 */
	public List<TpPayBillViewDtlVO> getTpMerchantVOList(){
		StringBuilder sql=new StringBuilder("select ");
		sql.append("  v.pay_type as paytype,v.table_code as tablecode,v.show_name as payshowname,d.field_code as fieldcode,d.show_name as fieldname,d.data_type as datatype,d.vo_attr as voattr ");
		sql.append("  ,d.field_use as fieldUse,d.dict_type as dictType ");
		sql.append(" from TP_BILL_VIEW_DTL d ");
		sql.append(" left join TP_BILL_VIEW v on v.id=d.view_id ");
		sql.append(" where v.valid_sign=:validSign and d.valid_sign=:validSign ");
		sql.append(" order by v.pay_type,d.sort ");
		Map<String,Object> params=new HashMap<>();
		params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
		return listBySQLAliasToBean(sql.toString(), params, TpPayBillViewDtlVO.class);
	}
}
