package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpPoolBankacc;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 外汇资金池账户表
 * @date 2020-02-12 14:31:56
 */
@Repository
public class FexCorpPoolBankaccDao extends BaseDao<FexCorpPoolBankacc, String> {


    /**
     * 外汇资金池账户通用查@param sqlWhere
     * @return
     */
    private String getCommonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id, p.pool_id as poolId, c.pool_name as poolName,p.is_limit_control as isLimitControl, f.parent_id as parentId, f.corp_id as corpId, sc.name as corpName, f.acc_id as accId, acc.bank_acc as bankAcc, ");
        sql.append("acc.acc_name as accName, acc.bank_name as bankName,f.cur_id as curId, cur.cur_name as curName, cur.english_code as englishCode,f.acc_nature as accNature, ");
        sql.append("f.rmk as rmk, f.parent_sign as parentSign, p.valid_sign as validSign, p.pool_nature as poolnature,");
        sql.append("f.create_by as creatrBy, f.create_date as createDate, f.update_by as updateBy, f.update_date as updateDate ");
        sql.append(" from fex_corp_pool_bankacc f left join fex_corp_pool p on f.parent_id = p.id ");
        sql.append(" left join fc_corp_pool c on c.id = p.pool_id ");
        sql.append(" left join sys_corp sc on sc.id = f.corp_id ");
        sql.append("left join bt_bank_acc acc on f.acc_id = acc.id left join bt_currency cur on f.cur_id = cur.id");
        sql.append(" where 1=1 ");
        return sql.toString();
    }

    /**
     * 根据外汇资金池表id(或主账号id)和币别id和是否主账号标识查找账号
     *
     * @param parentId
     * @param curId
     * @param parentSign
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listBankAccByParentId(String parentId, String curId, String parentSign, String accId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(parentId)) {
            sql.append(" and f.parent_id = :parentId");
            params.put("parentId", parentId);
        }
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and f.cur_id = :curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotBlank(parentSign)) {
            sql.append(" and f.parent_sign = :parentSign");
            params.put("parentSign", parentSign);
        }
        if (StringUtils.isNotBlank(accId)) {
            sql.append(" and f.acc_id = :accId");
            params.put("accId", accId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     *根据id查找账号信息
     * @param id
     * @return
     */
    public FexCorpPoolBankAccVO getFexCorpPoolBankAccVOById(String id){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and f.id = :id");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     * 根据外汇资金池表id和币别id和是否主账号标识查找账号
     *
     * @param poolId 资金池id
     * @param curId 币别id
     * @param parentSign 是否主账户
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listBankAccByPoolId(String poolId, String curId, String parentSign, String accId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(poolId)) {
            sql.append(" and p.id = :poolId");
            params.put("poolId", poolId);
        }
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and f.cur_id = :curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotBlank(parentSign)) {
            sql.append(" and f.parent_sign = :parentSign");
            params.put("parentSign", parentSign);
        }
        if (StringUtils.isNotBlank(accId)) {
            sql.append(" and f.acc_id = :accId");
            params.put("accId", accId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }

    /**
     * 根据资金池id和币别id和是否主账号标识查找账号
     *
     * @param poolId 资金池id
     * @param curId 币别id
     * @param parentSign 是否主账户
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolBankAccVO> listMainAccByPoolId(String poolId, String curId, String parentSign) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(poolId)) {
            sql.append(" and p.pool_id = :poolId");
            params.put("poolId", poolId);
        }
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and f.cur_id = :curId");
            params.put("curId", curId);
        }
        if (StringUtils.isNotBlank(parentSign)) {
            sql.append(" and f.parent_sign = :parentSign");
            params.put("parentSign", parentSign);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexCorpPoolBankAccVO.class);
    }
}
