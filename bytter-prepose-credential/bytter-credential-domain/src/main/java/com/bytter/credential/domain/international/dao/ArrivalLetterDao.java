package com.bytter.credential.domain.international.dao;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class ArrivalLetterDao extends BaseDao<LcBillArrival,String> {
    /**
     * 收到信用证到单列表
     * @param input
     * @param corpAuth 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput queryAll(DataTablesInput input, List<String> corpAuth){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lc.id as id, lc.lc_no as lcNo,lc.lc_inter as lcInter,lc.credit_no AS creditNo,");
        sql.append(" lc.contract_no AS contractNo,lc.issuing_date AS issuingDate,lc.expiry_date AS expiryDate,");
        sql.append(" lc.credit_money AS creditMoney,lc.issuing_cur_id AS issuingCurId, lc.beneficiary AS beneficiary,");
        sql.append(" lc.status AS status,lc.corp_id as corpId ,lc.cur_name as curName,arr.payMoney billMoney,lc.corp_name as corpName,  ");
        sql.append(" lc.OPENING_BANK_NAME openingBankName ");
        sql.append(" from lc_enrol lc ");
        sql.append(" left join (select sum(pay_money) payMoney, lc_Id ");
        sql.append("  from lc_bill_arrival where status >= 0 group by lc_Id) arr ");
        sql.append("  on lc.id = arr.lc_Id and nvl(arr.payMoney,0) <= lc.credit_money ");
        sql.append(" where lc.lc_type = 1  and lc.status = 99   ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "lc.corp_id"));
        OrderString oder = new OrderString("issuing_date","desc");
        input.setOrderString(oder);
        return getDynamicBySQL(input, sql.toString(), LcEnrolVO.class, "lc", false);
    }
    /**
     * 收到信用证到单修改列表
     * @param input
     * @param corpAuth 单位集合
     * @param flag R交单修改查询
     * @return DataTablesOutput
     */
    public DataTablesOutput queryUpdate(DataTablesInput input, List<String> corpAuth,String flag){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lb.*,lc.credit_No as creditNo,lc.lc_inter as lcInter,  ");
        sql.append(" lm.status status2, ");
        sql.append(" lc.OPENING_BANK_NAME openingBankName, ");
        sql.append(" sc.name corpName ");
        sql.append(" from Lc_Bill_Arrival lb ");
        sql.append(" left join lc_enrol lc on lb.lc_id = lc.id ");
        sql.append(" left join LC_MORTGAGE lm on lb.id = lm.arrival_id and lm.status not in (-2 ,-3) ");
        sql.append(" left join sys_corp sc on lb.corp_id = sc.id ");
        if("R".equals(flag)){
//            sql.append(" where lb.arrival_type = 1 and lb.status >= 1 ");
            //            改为取审批通过后数据
            sql.append(" where lb.arrival_type = 1 and (lb.status = 4 or lb.status = 5 or lb.status = 99  or (lb.status = 11 and lm.status is not null)) ");
        }else{
            sql.append(" where lb.arrival_type = 1 and lb.status in( 0,-1) ");
        }
        
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "lb.corp_id"));
        // corpName为关联sys_corp的展示列，VO中无注解，普通排序拼接不生效，通过OrderString按sc.name排序
        if (input.getOrder() != null && !input.getOrder().isEmpty() && input.getOrder().get(0) != null) {
            Integer orderColumnIndex = input.getOrder().get(0).getColumn();
            List<Column> columns = input.getColumns();
            if (orderColumnIndex != null && columns != null && orderColumnIndex >= 0
                    && orderColumnIndex < columns.size()
                    && "corpName".equals(columns.get(orderColumnIndex).getName())) {
                input.setOrderString(new OrderString("sc.name", input.getOrder().get(0).getDir()));
            }
        }
        return getDynamicBySQL(input,sql.toString(),LcBillArrivalVO.class,"lb",false);
    }
    /**
     * 通过lcno获取信用证信息
     * @param lcNo 信用证单号
     * @return LcEnrol 信用证实体
     */
    public LcEnrol getInfoByLcNo(String lcNo){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from lc_enrol where lc_no = :lcNo");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("lcNo",lcNo);
        return getBySQLAliasToBean(sql.toString(),map,LcEnrol.class);
    }
    /**
     * 获取最大单号
     * @param lcCode 到单号
     * @return 单号
     */
    public String getNextArrivalNo(String lcCode){
        if(StringUtils.isBlank(lcCode)) {
            return null;
        }
        String arrivalNo = "";
        
        String sql = "select max(arrival_no) as arrivalNo from lc_bill_arrival a left join lc_enrol e on a.lc_id = e.id  where e.lc_inter = 0 and "
                +" a.arrival_no like :lcCode ";
        Map<String,Object> param = new HashMap<String, Object>();
        param.put("lcCode","%"+lcCode+"%");
        List<LcBillArrival> rs = listBySQLAliasToBean(sql,param,LcBillArrival.class);
        if(EmptyUtils.isNotEmpty(rs.get(0).getArrivalNo())){
            arrivalNo = rs.get(0).getArrivalNo();
        }
        if(!StringUtils.isBlank(arrivalNo)){
            String lastNum = arrivalNo.substring(arrivalNo.length()-3);
            int lastInt = Integer.valueOf(lastNum);
            lastInt++;
            String newNum = String.valueOf(lastInt);
            if(newNum.length()==1) {
                newNum = "00"+newNum;
            }
            if(newNum.length()==2) {
                newNum = "0"+newNum;
            }
            arrivalNo = lcCode + newNum;
        }else{
            arrivalNo = lcCode + "001";
        }
        return arrivalNo;
    }
    /**
     * 到期收款列表
     * @param input
     * @param corpAuth 单位集合
     * @param flag M:申请抵押列表，C:到期付款列表
     * @return DataTablesOutput
     */
    public DataTablesOutput queryIncoming(DataTablesInput input, List<String> corpAuth,String flag){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lb.arrival_code as arrivalCode, lb.arrival_no as arrivalNo,lc.credit_No as creditNo,");
        sql.append(" lb.pay_money as payMoney,lb.cur_id as curId , lb.cur_name as curName,lb.create_date as createDate,lb.corp_id as corpId,");
        sql.append(" lb.payment_date as paymentDate,lb.id, '' as lmId,lb.status,0 as lmStatus,lc.lc_inter as lcInter,lb.ARRIVAL_TYPE as arrivalType, ");
        sql.append(" lc.OPENING_BANK_NAME openingBankName ");
        sql.append(" from Lc_Bill_Arrival lb ");
        sql.append(" left join lc_enrol lc on lb.lc_id = lc.id ");
        sql.append(" where lb.arrival_type = 1  and lb.status = 99 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "lb.corp_id"));
        DataTablesOutput output1 = getDynamicBySQL(input,sql.toString(),LcBillArrivalVO.class,"lc",false);
        DataTablesOutput output2 = null;
        if("M".equals(flag)){
        	StringBuilder sql2 = new StringBuilder();
            sql2.append(" select lm.mortgage_no as arrivalCode, lb.arrival_no as arrivalNo,lc.credit_no as creditNo,");
            sql2.append(" lm.mortgage_money as payMoney,lb.cur_id as curId ,lb.cur_name as curName,lm.mortgage_date as createDate,lm.corp_id as corpId,");
            sql2.append(" lm.expiry_date as paymentDate,'' as id,lm.id as lmId,'' as status,lm.status as lmStatus,lc.lc_inter as lcInter, ");
            sql2.append(" lb.ARRIVAL_TYPE as arrivalType, ");
            sql2.append(" lc.OPENING_BANK_NAME openingBankName ");
            sql2.append(" from lc_mortgage lm ");
            sql2.append(" left join lc_bill_arrival lb on lm.arrival_id = lb.id ");
            sql2.append(" left join lc_enrol lc on lm.lc_id = lc.id ");
            sql2.append(" where lm.status in(11,99,-1) ");
            sql2.append(" and ").append(getSQLIn(corpAuth, 900, "lm.corp_id"));
            output2 = getDynamicBySQL(input,sql2.toString(),LcBillArrivalVO.class,"lc",false);
        }
        if(output2==null){
            return output1;
        }else {
            if(CollectionUtils.isEmpty(output1.getData()) && CollectionUtils.isEmpty(output2.getData())) {
                return new DataTablesOutput();
            }
            if(!CollectionUtils.isEmpty(output1.getData()) && !CollectionUtils.isEmpty(output2.getData())) {
                List<LcBillArrivalVO> list = output1.getData();
                List<LcBillArrivalVO> list2 = output2.getData();
                list.addAll(list2);
                output1.setData(list);
                output1.setRecordsTotal(output1.getRecordsTotal()+output2.getRecordsTotal());
                return output1;
            }
            if(CollectionUtils.isEmpty(output1.getData())) {
                return output2;
            }
            if(CollectionUtils.isEmpty(output2.getData())) {
                return output1;
            }
        }
        return new DataTablesOutput();
    }
    /**
    *到期收款列表
    *@return
    */
    public DataTablesOutput selectDatatable(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        sql.append(" select lb.arrival_code as arrivalCode, lb.arrival_no as arrivalNo,lc.credit_No as creditNo,");
        sql.append(" lb.pay_money as payMoney,lb.cur_id as curId , lb.cur_name as curName,lb.create_date as createDate,sc.name as corpName,");
        sql.append(" lb.payment_date as paymentDate,lb.id, ''as lmId,lb.status,lb.ARRIVAL_TYPE as arrivalType,lc.lc_type as lcInter, ");
        sql.append(" lc.OPENING_BANK_NAME openingBankName ");
        sql.append(" from Lc_Bill_Arrival lb ");
        sql.append(" left join lc_enrol lc on lb.lc_id = lc.id ");
        sql.append(" left join sys_corp sc on lb.corp_id = sc.id ");
        sql.append(" where lb.arrival_type = 1 and lb.status = 99 and (lb.BANK_FLOW_SIGN = '0' or lb.BANK_FLOW_SIGN is null) ");
        return getDynamicBySQL(input,sql.toString(),LcBillArrivalVO.class,"lb",false);
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>	
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput,List<String> ids) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select lb.*,lc.credit_No as creditNo,bc.cur_name as curName,lc.OPENING_BANK_NAME openingBankName  from Lc_Bill_Arrival lb");
        sql.append(" left join lc_enrol lc on lb.lc_id = lc.id ");
        sql.append(" left join bt_currency bc on bc.id = lb.cur_id ");
        sql.append(" where lb.arrival_type = 1 and ");
        sql.append(getSQLIn(ids, 900, "lb.id"));
        return getDynamicBySQL(dataTablesInput,sql.toString(),LcBillArrivalVO.class,"lb",false);
    }
    /**
    *查询合计交单金额
    *@Param lcId
    *@return
    */
    public double getTotalMoney(String lcId){
        StringBuilder sql = new StringBuilder();
        sql.append("select bt_nvl_num(sum(PAY_MONEY),0) from Lc_Bill_Arrival where 1 = 1 and status in (11,99) and lc_id = ?");
        return Double.valueOf(createSQLQuery(sql.toString(), lcId).uniqueResult().toString());
    }
    
}
