package com.bytter.biz.report.dao;

import com.bytter.biz.report.vo.BisAccDtlCountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class BisAccDtlCountDao extends BaseDao {

    public DataTablesOutput listData(DataTablesInput input, String authSql){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> formdata = input.getFormdata();
        sql.append(" SELECT sc2.id parentId,sc2.name parentName,sc.id corpId,sc.name corpName,sc.province_id provinceId,p.province_name provinceName,");
        sql.append(" acc.bank_acc bankAcc,acc.acc_name accName,acc.bank_name bankName,acc.is_online isOnline,BT_NVL_NUM(d.number,0) number");
        sql.append(" FROM bt_bank_acc acc ");
        sql.append(" LEFT JOIN sys_corp sc ON sc.id =acc.corp_id ");
        // Oracle 不允许在 left join 的 on 条件里把列外连接到一个子查询（ORA-01799）。
        // 这里子查询与外层表无关联且 cell_code 唯一，用 in 代替 = 即可绕过。
        sql.append(" LEFT JOIN sys_corp sc2 ON sc2.id = sc.parent_id AND sc2.UNIT_CM in (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') ");
        sql.append(" LEFT JOIN (SELECT dtl.bank_acc,COUNT(*) number FROM bis_acc_dtl dtl WHERE (dtl.status <> -2 or dtl.status is null) ");
        String beginDate = formdata.get("beginDate").toString();
        String endDate = formdata.get("endDate").toString();
        sql.append(" and dtl.trans_time>='"+beginDate+" 00:00:00' AND dtl.trans_time<='"+endDate+" 23:59:59'");
        sql.append("     GROUP BY dtl.bank_acc) d ON d.bank_acc = acc.bank_acc ");
        sql.append(" LEFT JOIN sys_province p ON sc.province_id = p.id ");
        sql.append(" LEFT JOIN bt_bank_type t ON t.id = acc.bank_type_id");
        sql.append(" WHERE acc.acc_type <> '04' AND t.bank_type <>'8500' AND acc.valid_sign='1' AND acc.status <> -2 AND sc.status='1'");
        sql.append(authSql);
        if(formdata.get("number")!=null&&!StringUtils.isBlank(formdata.get("number").toString())){
            sql.append(" and BT_NVL_NUM(d.number,0) <= ").append(formdata.get("number").toString());
        }
        return getDynamicBySQL(input,sql.toString(), BisAccDtlCountVO.class,"",false);
    }
}
