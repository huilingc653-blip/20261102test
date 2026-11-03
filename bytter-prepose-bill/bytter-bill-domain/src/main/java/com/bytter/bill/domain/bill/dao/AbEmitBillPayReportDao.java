package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 到期兑付确认统计
 * @author onlineGenerator
 * @date 2018-01-15 16:35:25
 * @version 1.0
 *
 */
@Repository
public class AbEmitBillPayReportDao extends BaseDao<AbAccrecordsVO, String> {
	/**
	 * 到期兑付确认统计查询
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbEmitBillPayReportVO> listAbEmitBillPayReport(DataTablesInput dataTablesInput, List<String> corpAuthList, String statistical, Map<String,Object> params) {
		String formatSdf = AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)?"yyyy-MM":"yyyy-MM-dd";
		params.put("formatSdf",formatSdf);
		StringBuilder sql = new StringBuilder();
		sql.append(" select acceptor,businessDate,totalBillMoney,totalBailMoney,(totalBillMoney-totalBailMoney) as surplusMoney from ( ");
		sql.append(" select a.acceptor as acceptor,BT_TO_CHAR(c.business_date,'"+formatSdf+"') as businessDate, ");
        sql.append(" sum(BT_NVL_NUM(a.bill_money,0)) as totalBillMoney,sum(BT_NVL_NUM(b.bail_money,0)) as totalBailMoney ");
        sql.append(" from ab_voucherinfo_vice b ");
		sql.append(" left join ab_voucherinfo a on a.id = b.vid ");
		sql.append(" left join ab_accrecords c on c.vvid = b.id ");
		sql.append(" where c.brief_id = :status6120 ");
		sql.append(" and (c.status=:status100 or (c.emit_sttlmmk != :emitSttlmmk and c.status=:status95)) ");
		//sql.append(" and c.status = (case when c.emit_sttlmmk = :emitSttlmmk then :status100 else :status95 end) ");
		//params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		//params.put("null",' ');
		//--and s.id = ''
		sql.append(" and c.business_date >= :startDate ");
		sql.append(" and c.business_date < :endDate ");
		if(StringUtils.isNotBlank(params.get ("billType")==null?null:params.get ("billType").toString ())){
			sql.append(" and a.bill_type = :billType ");
		}
		if(StringUtils.isNotBlank(params.get ("billFlag")==null?null:params.get ("billFlag").toString ())){
			sql.append(" and a.bill_flag = :billFlag ");
		}
		if(StringUtils.isNotBlank(params.get ("corpId")==null?null:params.get ("corpId").toString ())){
			String[] corpArray = (String[]) params.get("corpId");
			sql.append(" and (" + getSQLIn(Arrays.asList(corpArray), 900, "b.corp_id") + ")");
		}
		if(StringUtils.isNotBlank(params.get ("acceptor")==null?null:params.get ("acceptor").toString ())){
			sql.append(" and a.acceptor like:acceptor ");
		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		sql.append(" group by a.acceptor,c.business_date ) info ");
		if(StringUtils.isNotEmpty (dataTablesInput.getOrder ().get (0).getDir ())){
			sql.append(" order by info.acceptor " + dataTablesInput.getOrder ().get (0).getDir ());
		}
		params.put("status6120",AccebillConstant.BRIEF_STATUS_6120);
		params.put("emitSttlmmk",AccebillConstant.AB_BILL_PAYEMITSTTLMMK1);
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("status100",AccebillConstant.AB_BILL_BUS_STATUS100);
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillPayReportVO.class, "", false,params);
	}
}
