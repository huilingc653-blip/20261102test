package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcMortgage;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.credential.domain.international.vo.LcMortgageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class MortgageLetterDao extends BaseDao<LcMortgage,String> {
    /**
     * 判断是否存在抵押融资业务
     * @param lcId
     * @return boolean
     */
    public boolean getInfoByLcId(String lcId){
        StringBuilder sql = new StringBuilder();
        sql.append("select 1 from lc_mortgage l ");
        sql.append(" where l.arrival_id in (select id from lc_bill_arrival where lc_Id = :lcId) ");
        sql.append("  and status <> -2 and status < 99 ");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("lcId",lcId);
        List list = listBySQL(sql.toString(),param);
        if(!EmptyUtils.isEmptyList(list)){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     * @param dataTablesInput
     * @param ids
     * @return
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select lm.*,la.arrival_no as arrivalNo,lc.credit_no as creditNo,lc.issuing_cur_id as curId,lc.cur_name as curName,");
        sql.append(" lc.create_Date as lcCreateDate,sc.name as corpName ");
        sql.append(" from lc_mortgage lm  ");
        sql.append(" left join lc_bill_arrival la on lm.arrival_id = la.id ");
        sql.append(" left join lc_enrol lc on lm.lc_id = lc.id ");
        sql.append(" left join sys_corp sc on lm.corp_id = sc.id ");
        sql.append(" where 1 = 1 and ");
        sql.append(getSQLIn(ids, 900, "lm.id"));
        return getDynamicBySQL(dataTablesInput,sql.toString(), LcMortgageVO.class,"lm",false);
    }
    /**
     * 抵押打回列表
     * @param input
     * @param corpAuth 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput queryBackIncoming(DataTablesInput input, List<String> corpAuth){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lm.mortgage_no as arrivalCode, lb.arrival_no as arrivalNo,lc.credit_no as creditNo,");
        sql.append(" lm.mortgage_money as payMoney,lb.cur_name as curName,lm.mortgage_date as createDate,lm.corp_name as corpName,");
        sql.append(" lm.expiry_date as paymentDate,'' as id,lm.id as lmId,null as status,lm.status as lmStatus, ");
        sql.append(" lb.ARRIVAL_TYPE as arrivalType ");
        sql.append(" from lc_mortgage lm ");
        sql.append(" left join lc_bill_arrival lb on lm.arrival_id = lb.id ");
        sql.append(" left join lc_enrol lc on lm.lc_id = lc.id ");
        sql.append(" where lm.status in(-1) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "lm.corp_id"));
        return getDynamicBySQL(input,sql.toString(), LcBillArrivalVO.class,"lb",false);
    }
    
}
