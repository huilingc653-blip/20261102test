package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountReportVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Title: Dao
 * @Description: AbBillCollectionAccountReportDao
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Repository
public class AbBillCollectionAccountReportDao extends BaseDao<AbBillCollectionAccountReportVo, String> {
	/**
	 * 托收到账列表查询
	 * @param input
	 * @param corpAuthList
	 * @param type 0:按月统计，1:按日统计
	 * @return
	 */
	public List<AbBillCollectionAccountReportVo> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String type) {
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> formdata = input.getFormdata();
		String countType = "";
		if (AccebillConstant.AB_STATISTICAL_TYPE1.equals(type)) {
			countType = "yyyy-mm-dd";
		} else {
			countType = "yyyy-MM";
		}
		StringBuilder sql = new StringBuilder(
				"select sub.billMoney as money,sub.acceptor,sub.businessDate as handleDate from ");
		sql.append("(select sum(z.bill_money) as billMoney, z.acceptor as acceptor, BT_TO_CHAR(l.business_date,'"
				+ countType + "') as businessDate from ab_voucherinfo_vice c ");
		sql.append("left join ab_accrecords l on c.id=l.vvid ");
		sql.append("left join ab_voucherinfo z on c.vid=z.id  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
//		sql.append("left join sys_corp s on c.corp_id = s.id ");
		sql.append("where 1=1 ");
		sql.append("and l.status=:status ");
//		sql.append("and c.valid_sign=:validSign1 ");
//		sql.append("and l.valid_sign=:validSign2 ");
//		sql.append("and c.box_status=:boxStatus ");
		sql.append("and l.opt_status=:optStatus ");
		sql.append("and l.brief_id in (:briefId) ");
		params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("validSign2", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("boxStatus", AccebillConstant.AB_BOX_STATUS_Y);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		//是否托收到账确认：是则加载托收到账完成的数据否则加载托收申请完成的数据
		if(formdata.containsKey("isCollectionApplyConfirm")
			&&"true".equals(String.valueOf(formdata.get("isCollectionApplyConfirm")))) {
			params.put("briefId", new String[]{AccebillConstant.BRIEF_STATUS_6020});
		}else{
			params.put("briefId", new String[]{AccebillConstant.BRIEF_STATUS_020,AccebillConstant.BRIEF_STATUS_021});
		}
		if (formdata.containsKey("corpId") && StringUtils.isNotBlank(String.valueOf(formdata.get("corpId")))) {
			String corpId = (String) formdata.get("corpId");
			String[] corpIdArray = corpId.split(",");
			sql.append(" and (" + getSQLIn(Arrays.asList(corpIdArray), 900, "c.corp_id") + ") ");
		}
		if (formdata.containsKey("billFlag") && StringUtils.isNotBlank(String.valueOf(formdata.get("billFlag")))) {
			sql.append(" and z.bill_Flag=:billFlag");
			params.put("billFlag", formdata.get("billFlag"));
		}
		if (formdata.containsKey("billType") && StringUtils.isNotBlank(String.valueOf(formdata.get("billType")))) {
			sql.append(" and z.bill_Type=:billType");
			params.put("billType", formdata.get("billType"));
		}
		if (formdata.containsKey("acceptor") && StringUtils.isNotBlank(String.valueOf(formdata.get("acceptor")))) {
			sql.append(" and z.acceptor like:acceptor");
			String acceptor = "%" + formdata.get("acceptor") + "%";
			params.put("acceptor", acceptor);
		}
		if (formdata.containsKey("startDate") && StringUtils.isNotBlank(String.valueOf(formdata.get("startDate")))
				&& formdata.containsKey("endDate") && StringUtils.isNotBlank(String.valueOf(formdata.get("endDate")))) {
			String startDateStr = String.valueOf(formdata.get("startDate"));// 开始日期字符串形式
			String endDateStr = String.valueOf(formdata.get("endDate"));// 结束日期字符串形式
			Date startDate;//开始日期date形式
			Date endDate;//结束日期date形式
			if (AccebillConstant.AB_STATISTICAL_TYPE0.equals(type)) {// 按月统计
				startDate = DateUtils.str2Date(startDateStr, new SimpleDateFormat("yyyy-MM"));
				endDate = DateUtils.str2Date(endDateStr, new SimpleDateFormat("yyyy-MM"));
				startDate = DateUtils.getMonthFirstDate(startDate);
				endDate = DateUtils.getNextMonthFirstDate(endDate);
				sql.append(" and l.business_date >= :startDate ");
				sql.append(" and l.business_date < :endDate ");
			} else {// 按日统计
				startDate = DateUtils.str2Date(startDateStr, DateUtils.date_sdf());
				endDate = DateUtils.str2Date(endDateStr, DateUtils.date_sdf());
				endDate=DateUtils.addDay(endDate, 1);//结束日期加一天
				sql.append(" and l.business_date >= :startDate ");
				sql.append(" and l.business_date < :endDate ");
			}
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		sql.append(" group by z.acceptor,BT_TO_CHAR(l.business_date,'" + countType + "') )sub ");
		sql.append(" order by sub.acceptor ");
		Map<String, String> map=input.getOrderByMap();
		if(map.containsKey("acceptor")&&StringUtils.isNotBlank(map.get("acceptor"))){
			sql.append(map.get("acceptor")+" ");
		}
		return listBySQLAliasToBean(sql.toString(), params, AbBillCollectionAccountReportVo.class);
	}
}
