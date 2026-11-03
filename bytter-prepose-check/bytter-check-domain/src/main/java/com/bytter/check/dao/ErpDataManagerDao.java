package com.bytter.check.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpBasicData;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.BtBankAccVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 中心数据管理DAO
* @author onlineGenerator
* @date 2017-05-31 15:28:08
* @version 1.0
*
*/
@Repository
public class ErpDataManagerDao extends BaseDao<FcCompareAcc,String>{
    
    /**Erp数据导入*/
    public DataTablesOutput erpImpDatatable(@RequestBody DataTablesInput dataTablesInput,List<String> bankList){
        StringBuilder sql = new StringBuilder();
        sql.append(" select comp_acc as compAcc,tally_date as tallyDate,voucher_no as voucherNo,debit_money as debitMoney,");
        sql.append("credit_money as creditMoney, balance as balance,redblue_sign as redblueSign,");
        sql.append("summary_content as summaryContent,data_source as dataSource,cur_id as curId ");
        sql.append(" from FC_COMPARE_ACC A  ");
        sql.append(" where import_type = '2'  and check_type = '2' and compare_sign = '0' ");
        sql.append(" and ").append(getSQLIn(bankList, 900, "comp_acc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcCompareAccVO.class, "", false);
    }
    
    /**
     * erp维护列表数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getErpDataTables(@RequestBody DataTablesInput dataTablesInput,List<String> bankList){
    	StringBuilder sql = new StringBuilder();
        sql.append(" select A.id,comp_acc as compAcc,tally_date as tallyDate,voucher_no as voucherNo,debit_money as debitMoney,");
        sql.append("credit_money as creditMoney, balance as balance,redblue_sign as redblueSign,compare_sign as compareSign,");
        sql.append("summary_content as summaryContent,data_source as dataSource,cur_id as curId, ");
        sql.append("check_type as checkType,order_no as orderNo,line_no as lineNo,bank_id as bankId,bill_no as billNo");
        sql.append(" from FC_COMPARE_ACC A  ");
        sql.append(" where check_type = '2' ");
        sql.append(" and ").append(getSQLIn(bankList, 900, "comp_acc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcCompareAccVO.class, "", false);
    }
    
    /**
     * 查询ERP在离线账号
     * @param
     * @return
     */
    //TODO SYS服务提供
    public List<BtBankAccDTO> getBankAccsErpData(List<String> bankList){
    	StringBuilder sql = new StringBuilder();
    	Map<String, Object> params=new HashMap<String, Object>();
    	sql.append(" select distinct erp.bytter_code bankAcc,erp.erp_reverse as curId ");
        sql.append(" from  erp_basic_data erp  ");
        sql.append(" where erp.data_type_code = 'BT_BANK_ACC' and erp.type_code = 'rsm'  ");
        sql.append(" and ").append(getSQLIn(bankList, 900, "erp.bytter_code"));
        return listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
    }
   /* *//**
     * 查询ERP在离线账号
     * @param dataTablesInput
     * @return
     *//*
  //TODO SYS服务提供
    public DataTablesOutput getBankAccsErpData(DataTablesInput dataTablesInput,String userId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params=new HashMap<String, Object>();
        sql.append(" select distinct acc.bank_name bankName, acc.bank_acc bankAcc,accCur.cur_id as curId,acc.corp_id as corpId,acc.acc_name as accName,  ");
        sql.append(" cur.cur_name as curName,acc.IS_ONLINE as isOnline,banktype.type_name as bankTypeName  ");
        sql.append(" from bt_bank_acc acc left join erp_basic_data erp on acc.bank_acc = erp.bytter_code ");
        sql.append(" left join bt_bank_acc_cur accCur on acc.id = accCur.bank_acc_id and accCur.Cur_Id = erp.erp_reverse ");
        sql.append(" left join bt_currency cur on accCur.cur_id=cur.id ");
        sql.append(" left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" where erp.data_type_code = 'BT_BANK_ACC' and erp.type_code = 'rsm'  ");
        sql.append(" and acc.valid_sign =:validSign and acc.status=:status ");
        sql.append(" and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' and ACCOUNT_GRANT.ACCOUNT_TYPE in('2','3') ");
        sql.append(" and ACCOUNT_GRANT.USER_ID = :userId");
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("userId", userId);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccVO.class, "", false,params);
    }*/

    /**
     * 获取所有ERP对应该的账号
     * @return
     */
    public List<ErpBasicData> getAllErpBasicData(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select bytter_code,erp_reverse from erp_basic_data ");
        sql.append(" where data_type_code = 'BT_BANK_ACC' and type_code = 'rsm' ");
        return listBySQLAliasToBean(sql.toString(),null,ErpBasicData.class);
    }
    
    /**更新导入标志**/
  //TODO SYS整体改造
    public void updateCompAccExt(List<String> ids,String beginDate, String endDate,String corpId,String bankAcc,String userId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update fc_compare_acc_ext f set f.import_sign = '1' ");
        sql.append(" where 1 = 1");
        sql.append(" and (f.import_sign <> '1' or f.import_sign is null) ");
        if (ListUtils.isNotNullOrEmpty(ids)) {
            sql.append(" and (" + getSQLIn(ids, 900, "f.id") + ")");
        } else {
            sql.append(" and tally_date >= bt_to_date(:beginDate,'yyyy-mm-dd')");
            sql.append(" and tally_date <= bt_to_date(:endDate,'yyyy-mm-dd')");
            if (StringUtils.isNotEmpty(bankAcc)) {
                sql.append(" and f.comp_acc = :bankAcc");
                params.put("bankAcc", "bankAcc");
            } else {
                sql.append(" and f.comp_acc in (");
                sql.append(" select acc.bank_acc ");
                sql.append(" from bt_bank_acc acc ");
                sql.append("          left join erp_basic_data erp on acc.bank_acc = erp.bytter_code ");
                sql.append("          left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id ");
                sql.append(" where erp.data_type_code = 'BT_BANK_ACC' ");
                sql.append("   and erp.type_code = 'rsm' ");
                sql.append("   and acc.valid_sign = '1' ");
                sql.append("   and acc.status = 95 ");
                sql.append("   and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' ");
                sql.append("   and ACCOUNT_GRANT.ACCOUNT_TYPE in ('2', '3') ");
                sql.append("   and ACCOUNT_GRANT.CORP_ID = :corpId ");
                sql.append("   and ACCOUNT_GRANT.USER_ID = '" + userId + "' ");
                sql.append(" )");
            }
            params.put("beginDate", beginDate);
            params.put("endDate", endDate);
            params.put("corpId", corpId);
        }
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public void updateCompAccExt1(List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" update fc_compare_acc_ext f set f.import_sign = '0' ");
        sql.append(" where ").append(getSQLIn(ids, 900, "f.id"));
        createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b>查询需要删除ERP接口保存数据<br>
     */
    public List queryDeleteCompAcc(String bankAcc,String curId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.bank_id as id from fc_compare_acc a  ");
        sql.append(" where a.import_type ="+"'"+ FinacialConstant.IMPORT_TYPE_INTERFACE+"'");//接口导入
        sql.append(" and a.comp_acc ="+"'"+bankAcc+"'");
        sql.append(" and a.cur_id =" + "'" +curId+"'");
        sql.append(" and a.compare_sign <>"+"'"+FinacialConstant.COMPARE_SIGN_OVER+"'");//已结转
        return createSQLQuery(sql.toString()).list();
    }
    /**
     * <br>
     * <b>功能：</b>删除ERP接口保存数据<br>
     */
    public void deleteCompAcc(String bankAcc,String curId){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from fc_compare_acc   ");
        sql.append(" where import_type ="+"'"+FinacialConstant.IMPORT_TYPE_INTERFACE+"'");//接口导入
        sql.append(" and comp_acc ="+"'"+bankAcc+"'");
        sql.append(" and cur_id =" + "'" +curId+"'");
        sql.append(" and compare_sign <>"+"'"+FinacialConstant.COMPARE_SIGN_OVER+"'");//已结转
        createSQLQuery(sql.toString()).executeUpdate();
    }
     
    
    //TODO 整体改造
    public DataTablesOutput listInter(DataTablesInput dataTablesInput,List<String> bankaccList){
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.* ");
        sql.append(" from fc_compare_acc_ext f ");
        sql.append(" where 1=1 ");
        sql.append(" and not exists (select 1 from fc_compare_acc a ");
        sql.append(" where f.id = a.id and f.comp_acc = a.comp_acc ) ");
        sql.append(" and (f.import_sign <> '1' or f.import_sign is null)");
        sql.append(" and ").append(getSQLIn(bankaccList, 900, "f.comp_acc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcCompareAccVO.class, "", false);
    }
    //TODO 整体改造
    public List<FcCompareAcc> getcompAccExt(List<String> ids) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select f.* from  fc_compare_acc_ext f ");
        sql.append(" where 1=1 ");
        sql.append(" and (f.import_sign is null or f.import_sign!='1')");
        if (ListUtils.isNotNullOrEmpty(ids)) {
            sql.append(" and ").append(getSQLIn(ids, 900, "f.id"));
        }
        return listBySQLAliasToBean(sql.toString(), params, FcCompareAcc.class);
    }
    /**
     * 根据账户和币别设置 银行勾兑交易流水已勾对变成未勾兑
     * @param bankAcc
     */
    public void updateFcBankBillYesToNo(String bankAcc,String curId){
        if(StringUtils.isNotEmpty(bankAcc)&&StringUtils.isNotEmpty(curId)) {
            StringBuilder sql = new StringBuilder();
            sql.append(" update fc_bank_bill set compare_sign = :compareSignNo ");
            sql.append(" where bank_acc=:bankAcc and compare_sign in (:compareSignYes) ");
            sql.append(" and cur_id = :curId ");
            Map<String, Object> params = new HashMap<>();
            params.put("bankAcc", bankAcc);
            params.put("curId", curId);
            params.put("compareSignNo", FinacialConstant.COMPARE_SIGN_NO);
            params.put("compareSignYes", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_HAND});
            createSQLQuery(sql.toString(), params).executeUpdate();
        }
    }
    /**
     * 根据账户和币别设置 erp企业勾兑交易流水已勾对变成未勾兑
     * @param bankAcc
     */
    public void updateFcCompareBillYesToNo(String bankAcc,String curId){
        if(StringUtils.isNotEmpty(bankAcc)&&StringUtils.isNotEmpty(curId)) {
            StringBuilder sql = new StringBuilder();
            sql.append(" update fc_compare_acc set compare_sign = :compareSignNo ");
            sql.append(" where comp_acc=:bankAcc and compare_sign in (:compareSignYes) ");
            sql.append(" and cur_id = :curId ");
            Map<String, Object> params = new HashMap<>();
            params.put("bankAcc", bankAcc);
            params.put("curId", curId);
            params.put("compareSignNo", FinacialConstant.COMPARE_SIGN_NO);
            params.put("compareSignYes", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_HAND});
            createSQLQuery(sql.toString(), params).executeUpdate();
        }
    }
    /**
     * <br>
     * <b>功能：</b> 删除ERP对账单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/3 0003 <br>
     */
    public int deleteCompareAcc(FcCompareAcc entity) {
        String sql = " delete from fc_compare_acc where id = :id and comp_acc = :compAcc and compare_sign = :compareSign "
                + " and compare_Sign in (:oldSign) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("compAcc", entity.getCompAcc());
        params.put("compareSign", entity.getCompareSign());
        params.put("oldSign", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_NO});
        return createSQLQuery(sql, params).executeUpdate();
    }
}
