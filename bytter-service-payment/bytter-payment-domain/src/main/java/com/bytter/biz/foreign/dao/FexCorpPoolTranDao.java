package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpPoolTran;
import com.bytter.biz.foreign.vo.FexCorpPoolTranVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 资金池交易信息
 * @date 2020-02-21 14:31:56
 */
@Repository
public class FexCorpPoolTranDao extends BaseDao<FexCorpPoolTran, String> {

    /**
     * 资金池交易表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id, p.id as poolId,c.pool_code as poolCode, c.pool_name as poolName, p.pool_nature as poolNature, p.rmk as rmk, ");
        sql.append(" f.cur_id as curId, cur.english_code as englishCode, cur.cur_name as curName, t.bankacc_id as bankaccId, acc.bank_acc as bankAcc,");
        sql.append(" acc.acc_name as accName, acc.bank_name as bankName, t.tran_sign as tranSign, t.tran_type as tran_Type,t.pro_id as proId,");
        sql.append(" fp.pro_name as proName, p.rmk as poolRmk");
        sql.append(" from fex_corp_pool_tran t left join fex_corp_pool p on t.pool_id = p.id left join fc_corp_pool c on p.pool_id = c.id");
        sql.append(" left join fex_corp_pool_bankacc f on t.bankacc_id = f.id left join bt_bank_acc acc on f.acc_id = acc.id");
        sql.append(" left join bt_currency cur on f.cur_id = cur.id left join fex_product fp on t.pro_id = fp.id");
        sql.append(" where f.parent_sign = '1'");
        return sql.toString();
    }

    /**
     * 资金池交易列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCorpPoolTranVO.class, "t", false);
    }

    /**
     * 根据资金池交易表id得到资金池交易相关信息
     *
     * @param id
     * @return
     */
    public FexCorpPoolTranVO getFexCorpPoolTranVOById(String id) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        Map<String,Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(id)){
            sql.append(" and t.id = :id");
            params.put("id",id);
        }
       return getBySQLAliasToBean(sql.toString(),params,FexCorpPoolTranVO.class);
    }

    /**
     * 根据外汇资金池id得到资金池交易相关信息
     *
     * @param poolId
     * @return
     */
    public List<FexCorpPoolTranVO> getFexCorpPoolTranVOByPoolId(String poolId, String bankAccId) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        Map<String,Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(poolId)){
            sql.append(" and p.id = :poolId");
            params.put("poolId",poolId);
        }
        if(StringUtils.isNotEmpty(bankAccId)){
            sql.append(" and t.bankacc_Id = :bankAccId");
            params.put("bankAccId",bankAccId);
        }
        return listBySQLAliasToBean(sql.toString(),params,FexCorpPoolTranVO.class);
    }

    /**
     * 根据资金池id和账号id和币别id得到资金池交易相关信息
     * @return
     */
    public List<FexCorpPoolTranVO> getTranListByPoolIdAndBankAccId(String poolId,String bankAccId,String curId){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        Map<String,Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(poolId)){
            sql.append(" and t.pool_id = :poolId");
            params.put("poolId",poolId);
        }
        if(StringUtils.isNotEmpty(bankAccId)){
            sql.append(" and t.bankacc_id = :bankAccId");
            params.put("bankAccId",bankAccId);
        }
        if(StringUtils.isNotEmpty(curId)){
            sql.append(" and f.cur_id = :curId");
            params.put("curId",curId);
        }
        return listBySQLAliasToBean(sql.toString(),params,FexCorpPoolTranVO.class);
    }

}
