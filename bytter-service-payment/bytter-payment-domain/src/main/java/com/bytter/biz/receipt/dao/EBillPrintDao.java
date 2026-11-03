package com.bytter.biz.receipt.dao;
import com.bytter.biz.receipt.entity.EBillPrint;
import com.bytter.biz.receipt.vo.EleBillQueryVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 电子回单打印
* @author onlineGenerator
* @date 2017-08-23 14:39:28
* @version 1.0
*
*/
@Repository
public class EBillPrintDao extends BaseDao<EBillPrint,String> {
    /**
     * 根据状态/单位获取不同电子回单类型 数量
     * @param status
     * @param listTableName
     * @param corpIds
     * @return
     */
    public List<EleBillQueryVO> listEleBillNum(String status, List<String> listTableName, String corpIds, List<String> accList){
        //定义一个总的sql
        StringBuilder allSql =new StringBuilder();
        StringBuilder sql =new StringBuilder();
        if (EmptyUtils.isEmpty(corpIds)|| EmptyUtils.isEmpty(listTableName)) {
            return Lists.newArrayList();
        }
        String corpSql = SqlUtil.getInSql(corpIds.split(","), "a.corp_Id", true);
        for (String tableName : listTableName){
            if(allSql.length()>0){
                allSql.append(" union ");
            }
            sql.append("select a.bill_type as billType,count(*) as count ");
            if(ReceiptConstant.E_ACCRUAL_BILL.equals(tableName)){
                sql.append(" from e_accrual_bill A,E_BILL_PRINT B WHERE A.BILL_NO=B.BILL_NO and A.accrual <> 0 ");
                sql.append(" and ").append(corpSql).append(addStatusSql(status));
                sql.append(" and ").append(SqlUtil.getInSql(accList, "A.ACC_NO", true));
            }
//            else if(ReceiptConstant.E_PRECATIVE_LOAN_BILL.equals(tableName)){
//                sql.append(" FROM (select '' precation_corp, '' precation_acc, a.* from E_PRECATIVE_LOAN_BILL a /*, fc_acc b ");
//                sql.append(" where a.precative_loan_acc = b.blnctr_acc*/ ) A,E_bill_PRINT B /*,");
//                sql.append(" cms_precative_loan_send send,cms_precative_loan_info info */");
//                sql.append(" WHERE A.BILL_NO = B.BILL_NO /*AND A.precative_loan_acc = send.loan_acc and send.father_code = info.bill_code*/ ");
//                sql.append(" and ").append(corpSql).append(addStatusSql(status));
//                sql.append(" and ").append(SqlUtil.getInSql(accList, "A.PRECATIVE_ACC", true));
//            }
            else if(ReceiptConstant.E_COMPARE_BILL.equals(tableName)){
                sql.append(" from (select DISTINCT a.acc_no,\n" +
                        "                      a.acc_name,\n" +
                        "                      a.corp_id,\n" +
                        "                      a.bill_type,\n" +
                        "                      a.bill_no,\n" +
                        "                      a.cur_id,\n" +
                        "                      a.status,\n" +
                        "                      b.prints,\n" +
                        "                      bt_to_date(bt_to_char(a.ENCRY_DATE, 'yyyy-MM-dd'),\n" +
                        "                                 'yyyy-MM-dd')\n" +
                        "      from (select acc_no ,\n" +
                        "\t\tacc_name ,\n" +
                        "\t\tcorp_id ,\n" +
                        "\t\tbill_type,\n" +
                        "\t\tbill_no ,\n" +
                        "\t\tcur_id ,\n" +
                        "\t\tstatus ,\n" +
                        "\t\tENCRY_DATE from e_compare_bill group by acc_no,acc_name,corp_id,bill_type,bill_no,cur_id,status,encry_date) a\n" +
                        "          left join (\n" +
                        "\t\tselect\n" +
                        "\t\t\tbill_no,\n" +
                        "\t\t\tcorp_id,\n" +
                        "\t\t\tmax(prints) as prints\n" +
                        "\t\tfrom\n" +
                        "\t\t\te_bill_print\n" +
                        "\t\tgroup by\n" +
                        "\t\t\tbill_no,\n" +
                        "\t\t\tcorp_id ) b on\n" +
                        "\t\ta.BILL_NO = b.BILL_NO\n" +
                        "\t\tand a.corp_id = b.corp_id\n" +
                        "      where b.prints = 1) A where 1=1 ");
                sql.append(" and ").append(corpSql).append(addStatusSql(status));
                sql.append(" and ").append(SqlUtil.getInSql(accList, "A.ACC_NO", true));
            }else{
                sql.append(" from ").append(tableName).append(" a , E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id=b.corp_id ");
                sql.append(" and ").append(corpSql);
                sql.append(addStatusSql(status));
                if(ReceiptConstant.E_LOAN_BILL.equals(tableName)) {
                    sql.append(" and ").append(SqlUtil.getInSql(accList, "A.DEPOSIT_ACC", true));
                }else if(ReceiptConstant.E_NOTICE_BILL.equals(tableName) || ReceiptConstant.E_ACCEPT_BILL.equals(tableName)
                        || ReceiptConstant.E_POUNDAGE_BILL.equals(tableName)){
                    sql.append(" and (").append(SqlUtil.getInSql(accList, "A.PAYER_ACC", true));
                    sql.append(" or ").append(SqlUtil.getInSql(accList, "A.PAYEE_ACC", true)).append(")");
                }else if(ReceiptConstant.E_TRANS_BILL.equals(tableName)){
                    sql.append(" and ").append(SqlUtil.getInSql(accList, "A.current_acc", true));
                }
            }
            sql.append(" group by a.bill_type");
            allSql.append(sql);
            //清空sql
            sql.setLength(0);
        }
        return listBySQLAliasToBean(allSql.toString(),null, EleBillQueryVO.class);
    }
    private String addStatusSql(String status){
        if(StringUtils.isNotEmpty(status)){
            return " and A.status='"+status+"'" ;
        }
        return "";
    }
    /**
     * 根据单位与单据号查询对象
     * @param corpId
     * @param billNo
     * @return
     */
    public EBillPrint getEntityByParams(String corpId, String billNo){
        StringBuilder sql=new StringBuilder();
        sql.append(" FROM EBillPrint where billNo=:billNo and corpId=:corpId  ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("billNo", billNo);
        params.put("corpId", corpId);
        List<EBillPrint> infoList=this.listByHQL(sql.toString(), params);
        if(infoList != null && !infoList.isEmpty()){
            return infoList.get(0);
        }
        return null;
    }
}
