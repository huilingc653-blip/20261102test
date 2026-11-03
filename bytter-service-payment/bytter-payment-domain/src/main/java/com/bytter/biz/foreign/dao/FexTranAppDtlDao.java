package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranAppDtl;
import com.bytter.biz.foreign.vo.FexTranAppDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FexTranAppDtlDao extends BaseDao<FexTranAppDtl, String> {


    /**
     * 查询正式表数据
     * @param input
     * @return
     */
    public DataTablesOutput getDtlDataTable(DataTablesInput input) {
        return getDtlDataTable(input, null);
    }

    /**
     * 查询正式表数据
     * @param input
     * @param externalCorpIds null 表示不按交易对手过滤，空集合表示名称筛选无结果
     * @return
     */
    public DataTablesOutput getDtlDataTable(DataTablesInput input, List<String> externalCorpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, ");
        sql.append("        t.tran_type tranType, ");
        sql.append("        fp.pro_name proName, ");
        sql.append("        t.cotp_id cotpId, ");
        sql.append("        t.tran_date tranDate, ");
        sql.append("        t.settle_date settleDate");
        sql.append("   from FEX_TRAN_APP_DTL t ");
        sql.append("   left join fex_product fp ");
        sql.append("     on t.pro_id = fp.id ");
        sql.append("    where 1 = 1");
        appendExternalCorpFilter(sql, externalCorpIds);
        return getDynamicBySQL(input, sql.toString(), FexTranAppDtlVO.class, "t", false, null);
    }


    /**
     * 通过id查询
     * @return
     */
    public FexTranAppDtlVO getById(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.*, ");
        sql.append("        fp.pro_name        proName, ");
        sql.append("        fti1.tran_name     tranCode1Name, ");
        sql.append("        fti2.tran_name     tranCode2Name, ");
        sql.append("        bankAcc1.acc_name  boodaccName, ");
        sql.append("        bankType.Type_Name corpBankName, ");
        sql.append("        bankAcc2.Acc_Name corpBankAccName, ");
        sql.append("        tq.id  as tranReqId,");
        sql.append("        tq.bill_no as tranReqBillNo,");
        sql.append("        t.wish_rate as wishRate,");
        sql.append("        t.end_wish_rate as endWishRate,");
        sql.append("        t.fex_tran_type_remote_end as tranTypeRemoteEnd");
        sql.append("   from FEX_TRAN_APP_DTL t ");
        sql.append("   left join fex_product fp ");
        sql.append("     on t.pro_id = fp.id ");
        sql.append("   left join fex_tran_info fti1 ");
        sql.append("     on t.tran_code1 = fti1.tran_code ");
        sql.append("   left join fex_tran_info fti2 ");
        sql.append("     on t.tran_code2 = fti2.tran_code ");
        sql.append("   left join bt_bank_acc bankAcc1 ");
        sql.append("     on t.bood_acc_id = bankAcc1.id ");
        sql.append("   left join bt_bank_type bankType ");
        sql.append("     on t.corp_bank = bankType.Id ");
        sql.append("   left join bt_bank_acc bankAcc2 ");
        sql.append("     on t.corp_acc_id = bankAcc2.Id ");
        sql.append("   left join fex_tran_req tq ");
        sql.append("     on tq.tran_App_Dtl_Id = t.id ");
        sql.append("  where t.id = :id ");
      //  sql.append("   group by t.id  ");
        return getBySQLAliasToBean(sql.toString(), params, FexTranAppDtlVO.class);
    }

    /**
     * 查询交易需求内交易明细
     * @param params
     * @return
     */
    public List<FexTranAppDtlVO> listFexTranAppDtlByBusId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from FEX_TRAN_APP_DTL where app_id = :id");
        return listBySQLAliasToBean(sql.toString(), params, FexTranAppDtlVO.class);
    }

    /**
     * 查询是否存在
     * @param id
     * @return
     */
    public String countDtl(String id){
        String sql = "select count(*) from FEX_TRAN_APP_DTL  where id = ?";
        return createSQLQuery(sql, id).uniqueResult().toString();
    }


    /**
     * 通过appId查询
     * @return
     */
    public List<FexTranAppDtlVO> listByAppId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.*, ");
        sql.append("        fp.pro_name        proName, ");
        sql.append("        fti1.tran_name     tranCode1Name, ");
        sql.append("        fti2.tran_name     tranCode2Name, ");
        sql.append("        bankAcc1.acc_name  boodaccName, ");
        sql.append("        bankType.Type_Name corpBankName, ");
        sql.append("        bankAcc2.Acc_Name corpBankAccName, ");
        sql.append("        t.wish_rate as wishRate,");
        sql.append("        t.end_wish_rate as endWishRate,");
        sql.append("        t.fex_tran_type_remote_end as tranTypeRemoteEnd");
        sql.append("   from FEX_TRAN_APP_DTL t ");
        sql.append("   left join fex_product fp ");
        sql.append("     on t.pro_id = fp.id ");
        sql.append("   left join fex_tran_info fti1 ");
        sql.append("     on t.tran_code1 = fti1.tran_code ");
        sql.append("   left join fex_tran_info fti2 ");
        sql.append("     on t.tran_code2 = fti2.tran_code ");
        sql.append("   left join bt_bank_acc bankAcc1 ");
        sql.append("     on t.bood_acc_id = bankAcc1.id ");
        sql.append("   left join bt_bank_type bankType ");
        sql.append("     on t.corp_bank = bankType.Id ");
        sql.append("   left join bt_bank_acc bankAcc2 ");
        sql.append("     on t.corp_acc_id = bankAcc2.Id ");
        sql.append("  where t.app_id = :id and (t.SWAP_TYPE = '"+ ForeignConstant.SWAP_TYPE_1 +"' or t.SWAP_TYPE is null)");
        return listBySQLAliasToBean(sql.toString(), params, FexTranAppDtlVO.class);
    }

    /**
     *
     * 根据币种查货币对
     *
     * @param curId1
     * @param curId2
     * @return
     */
    public Object getCurrencyPairIdBy(String curId1, String curId2) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.base_cur_id||'_'||(case when c.cur_id_1=:curId2 then c.cur_id_1 else cur_id_2 end) from bt_currency_pair c where c.valid_sign='1' and c.base_cur_id=:curId1 and (c.cur_id_1 = :curId2 or c.cur_id_2 = :curId2)");
        Map<String,String> params = new HashMap<>();
        params.put("curId1",curId1);
        params.put("curId2",curId2);
        List<String> list = this.createSQLQuery(sql.toString(), params).list();
        if(!EmptyUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 追加客商ID过滤条件。
     * null 表示不按客商过滤；空集合表示名称筛选无结果。
     */
    private void appendExternalCorpFilter(StringBuilder sql, List<String> externalCorpIds) {
        if (externalCorpIds == null) {
            return;
        }
        if (externalCorpIds.isEmpty()) {
            sql.append(" and 1=0");
            return;
        }
        sql.append(" and (").append(getSQLIn(externalCorpIds, 900, "t.cotp_id")).append(")");
    }
}
