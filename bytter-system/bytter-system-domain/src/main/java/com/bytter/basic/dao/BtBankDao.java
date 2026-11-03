package com.bytter.basic.dao;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.basic.entity.BtBank;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.BtBankDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK
 * @date 2017-07-20 14:28:41
 */
@Repository
public class BtBankDao extends BaseDao<BtBank, String> {
    /**
     * 根据联行号查询
     *
     * @param bankCode 联行号
     * @return
     */
    public BtBank getByBankCode(String bankCode) {
        Map<String, String> params = Maps.newHashMap();
        String hql = " from BtBank where bankCode = :bankCode ";
        params.put("bankCode", bankCode);
        return this.getUnique(hql, params);
    }

    /**
     * 根据联行号名称查询
     *
     * @param bankName	联行号名称
     * @return
     */
    public BtBank getByBankName(String bankName) {
        Map<String, String> params = Maps.newHashMap();
        String hql = " from BtBank where bankName = :bankName ";
        params.put("bankName", bankName);
        return this.getUnique(hql, params);
    }

    public List<BtBank> getByBankNames(List<String> bankName) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from BtBank where bankName in (:bankName) ";
        params.put("bankName", bankName);
        return this.listByHQL(hql, params);
    }

    /**
     * 根据银行类别ID查询
     *
     * @param bankTypeId 银行类别ID
     * @return List<BtBank>
     */
    public List<BtBank> getByBankTypeId(String bankTypeId) {
        return this.listBy("bankTypeId", bankTypeId);
    }
    public List<BtBankDTO> getBankUnionList() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct rb.sys_bank_code as bankCode, rb.bank_name as bankName,bt.id as bankTypeId,bt.TYPE_NAME as bankTypeName ");
        sql.append("from bt_bank rb left join bt_bank_type bt on rb.bank_type_id = bt.id order by rb.sys_bank_code ");
        return listBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
    }
    public List<BtBankDTO> getListByCodes(List<String> codes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct rb.sys_bank_code as bankCode, rb.bank_name as bankName,bt.id as bankTypeId,bt.TYPE_NAME as bankTypeName ");
        sql.append("from bt_bank rb left join bt_bank_type bt on rb.bank_type_id = bt.id where 1=1 ");
        if (CollectionUtils.isNotEmpty(codes)){
            sql.append(" and ").append(SqlUtil.getInSql(codes,"rb.sys_bank_code",true));
        }
        sql.append(" order by rb.sys_bank_code ");
        /*String strCodes = String.join(",", codes);
        params.put("codes", strCodes);*/
        return listBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
    }
}
