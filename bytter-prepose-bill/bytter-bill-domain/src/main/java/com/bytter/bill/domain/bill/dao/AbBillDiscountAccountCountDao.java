package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountCountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description:贴现到账统计Dao
 * @date 2017-7-21 13:51:15
 */
@Repository
public class AbBillDiscountAccountCountDao extends BaseDao<AbBillDiscountAccountCountVO, String> {
    /**
     * 列表显示贴现到账信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<AbBillDiscountAccountCountVO> listAbBillDiscountAccountCount(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> formData = input.getFormdata();
        //判断是按月还是按天统计
        SimpleDateFormat sdf = new SimpleDateFormat(AccebillConstant.AB_STATISTICAL_TYPE1.equals(formData.get("eqi_statistical")) ? "yyyy-MM-dd" : "yyyy-MM");
        String formatType = AccebillConstant.AB_STATISTICAL_TYPE1.equals(formData.get("eqi_statistical")) ? "yyyy-MM-dd" : "yyyy-MM";
        String dimensions = String.valueOf(formData.get("dimensions"));
        String gbusinessDate = "";
        String lbusinessDate = "";
        StringBuilder sql = new StringBuilder(" select c.rec_nm as recNm,c.real_money as realMoney,c.business_date as businessDateFmt from (");
        sql.append(" select ps.rec_nm as rec_nm,ps.real_money as real_money,ps.business_date as business_date");
        sql.append(" from (select ");
        if (StringUtils.isNotBlank(dimensions)) {
            //按持票单位统计
            if (AccebillConstant.AB_STATISTICAL_DIMENSION_1.equals(dimensions)) {
//                sql.append("sc.name ");
                sql.append("P.CORP_NAME ");
            }else{
                sql.append("s.rec_nm ");
            }
        }
        sql.append("as rec_nm,sum(t.real_money) as real_money,BT_TO_CHAR(s.business_date,'" + formatType + "') as business_date");
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append("  JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
//        sql.append(" LEFT JOIN SYS_CORP SC ON SC.ID=P.CORP_ID");
        sql.append(" where 1=1 ");
//        sql.append(" and p.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'");
        sql.append(" and s.status='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "'");
        sql.append(" and s.opt_status='" + AccebillConstant.AB_BILL_OPT_STATUS19 + "' ");
        //是否贴现到账确认：是则加载贴现到账完成的数据否则加载贴现办理完成的数据
        if(formData.containsKey("isDiscountConfirm")
                &&"true".equals(String.valueOf(formData.get("isDiscountConfirm")))) {
            sql.append(" and (s.brief_id in (:briefId) or (s.brief_id in (:briefId2) and s.sttlmmk=:sttlmmk))");
            params.put("briefId", new String[]{AccebillConstant.BRIEF_STATUS_6012});
            params.put("briefId2", new String[]{AccebillConstant.BRIEF_STATUS_011});
            params.put("sttlmmk", AccebillConstant.AB_STTLMMK_Y);
        }else{
            sql.append(" and brief_id in (:briefId) ");
            params.put("briefId", new String[]{AccebillConstant.BRIEF_STATUS_011});
        }
        if (StringUtils.isNotBlank((String) formData.get("eqi_billFlag"))) {
            sql.append(" and v.bill_Flag=:billFlag");
            params.put("billFlag", formData.get("eqi_billFlag"));
        }
        if (StringUtils.isNotBlank((String) formData.get("eqi_billType"))) {
            sql.append(" and v.bill_Type=:billType");
            params.put("billType", formData.get("eqi_billType"));
        }
        if (StringUtils.isNotBlank((String) formData.get("likes_recNm"))) {
            sql.append(" and s.rec_Nm like:recNm");
            String recNmStr = "%" + formData.get("likes_recNm") + "%";
            params.put("recNm", recNmStr);
        }
        if (StringUtils.isNotBlank((String) formData.get("ins_corpId"))) {
            String corpStr = (String) formData.get("ins_corpId");
            String[] corpArr = corpStr.split(",");
            sql.append(" and (" + getSQLIn(Arrays.asList(corpArr), 900, "p.corp_id") + ")");
        }
        if (StringUtils.isNotBlank((String) formData.get("ged_businessDate"))) {
            sql.append(" and s.business_date>=:ged_businessDate");
            Date gedDate;
            gbusinessDate = (String) formData.get("ged_businessDate");
            if (AccebillConstant.AB_STATISTICAL_TYPE1.equals(formData.get("eqi_statistical"))) {
                gedDate = DateUtils.str2Date(gbusinessDate, sdf);
            } else {
                gedDate = DateUtils.getMonthFirstDate(DateUtils.str2Date(gbusinessDate, sdf));
            }
            params.put("ged_businessDate", gedDate);
        }
        if (StringUtils.isNotBlank((String) formData.get("led_businessDate"))) {
            sql.append(" and s.business_date<:led_businessDate");
            Date ledDate;
            lbusinessDate = (String) formData.get("led_businessDate");
            if (AccebillConstant.AB_STATISTICAL_TYPE1.equals(formData.get("eqi_statistical"))) {
                ledDate = DateUtils.str2Date(lbusinessDate, sdf);
                Calendar cal = Calendar.getInstance();
                cal.setTime(ledDate);
                cal.add(Calendar.DATE, 1);
                ledDate = cal.getTime();
            } else {
                ledDate = DateUtils.getNextMonthFirstDate(DateUtils.str2Date(lbusinessDate, sdf));
            }
            params.put("led_businessDate", ledDate);
        }
        formData.clear();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "P.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        sql.append(" group by ");
        if (StringUtils.isNotBlank(dimensions)) {
            //按持票单位统计
            if (AccebillConstant.AB_STATISTICAL_DIMENSION_1.equals(dimensions)) {
//                sql.append("sc.name, ");
                sql.append("P.CORP_NAME, ");
            }else{
                sql.append("s.rec_nm, ");
            }
        }
        sql.append(" BT_TO_CHAR(s.business_date,'" + formatType + "'))ps ");
//        String orderInit = "asc";
//        if (input.getOrder() != null && !input.getOrder().isEmpty() && input.getOrder().get(0) != null) {
//            if (input.getOrder().get(0).getColumn() == 0) {
//                orderInit = input.getOrder().get(0).getDir();
//            }
//        }
//        sql.append(" order by ps.rec_Nm " + orderInit + ") c");
        sql.append(" ) c");
        sql.append(" where 1=1" );
		String orderInit = "asc";
		if (input.getOrder() != null && !input.getOrder().isEmpty() && input.getOrder().get(0) != null) {
			if (input.getOrder().get(0).getColumn() == 0) {
				orderInit = input.getOrder().get(0).getDir();
			}
		}
		sql.append(" order by c.rec_Nm " + orderInit + " ");
		List<AbBillDiscountAccountCountVO> list = this.listBySQLAliasToBean(sql.toString(), params, AbBillDiscountAccountCountVO.class);
        return list;
    }
}
