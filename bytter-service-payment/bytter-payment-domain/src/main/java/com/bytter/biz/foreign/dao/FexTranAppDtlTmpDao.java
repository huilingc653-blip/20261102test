package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranAppDtlTmp;
import com.bytter.biz.foreign.vo.FexTranAppDtlTmpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FexTranAppDtlTmpDao extends BaseDao<FexTranAppDtlTmp, String> {

    /**
     * 查询暂存列表数据
     * @param input
     * @return
     */
    public DataTablesOutput getDtlTmpDataTable(DataTablesInput input, List<String> ids) {
        return getDtlTmpDataTable(input, ids, null);
    }

    /**
     * 查询暂存列表数据
     * @param input
     * @param ids
     * @param externalCorpIds null 表示不按交易对手过滤，空集合表示名称筛选无结果
     * @return
     */
    public DataTablesOutput getDtlTmpDataTable(DataTablesInput input, List<String> ids, List<String> externalCorpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, ");
        sql.append("        t.tran_type tranType, ");
        sql.append("        fp.pro_name proName, ");
        sql.append("        t.cotp_id cotpId, ");
        sql.append("        t.tran_date tranDate, ");
        sql.append("        t.settle_date settleDate");
        sql.append("   from FEX_TRAN_APP_DTL_TMP t ");
        sql.append("   left join fex_product fp ");
        sql.append("     on t.pro_id = fp.id ");
        sql.append("   where 1 = 1");
        sql.append(" and ( " + getSQLIn(ids, 900, "t.id")+" )");
        appendExternalCorpFilter(sql, externalCorpIds);
        return getDynamicBySQL(input, sql.toString(), FexTranAppDtlTmpVO.class, "t", false, null);
    }

    /**
     * 返回临时表交易需求明细数据
     * @return
     */
    public List<FexTranAppDtlTmpVO> listDtlTmpByIds(List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.* from  FEX_TRAN_APP_DTL_TMP t where 1 = 1");
        sql.append(" and (" + getSQLIn(ids, 900, "t.id") + ")");
        return listBySQLAliasToBean(sql.toString(), null, FexTranAppDtlTmpVO.class);
    }

    /**
     * 查询是否存在
     * @param id
     * @return
     */
    public String countDtlTmp(String id){
        String sql = "select count(*) from FEX_TRAN_APP_DTL_TMP  where id = ?";
        return createSQLQuery(sql, id).uniqueResult().toString();
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
