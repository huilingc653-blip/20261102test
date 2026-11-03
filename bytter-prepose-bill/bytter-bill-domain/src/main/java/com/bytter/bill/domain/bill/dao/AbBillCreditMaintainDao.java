package com.bytter.bill.domain.bill.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.bill.domain.bill.entity.AbBillCreditInfoMaintain;
import com.bytter.bill.domain.bill.vo.AbBillCreditInfoMaintainVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.bill.api.bill.dto.AbBillCreditInfoMaintainDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 银行开票授信资料维护业务层DAO层
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Repository
public class AbBillCreditMaintainDao extends BaseDao<AbBillCreditInfoMaintain, String> {

    /**
     * 查询银行开票授信资料维护列表
     *
     * @param input 入参
     * @return 出参
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> authCorpList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.ID as id, m.corp_id as corpId, sc.name as corpName, m.bank_type_id as bankTypeId, bbt.type_name as bankTypeName, ");
        sql.append(" m.credit_type as creditType, m.bond_ratio as bondRatio, m.open_bill_rate as openBillRate, m.bond_rate as bondRate, m.is_used as isUsed, ");
        sql.append(" m.create_by as createBy, m.create_time as createTime, m.update_by as updateBy, m.update_time as updateTime ");
        sql.append(" from AB_BILL_CREDIT_INFO_MAINTAIN m ");
        sql.append(" left join SYS_CORP sc on sc.ID = m.CORP_ID ");
        sql.append(" left join BT_BANK_TYPE bbt on bbt.ID = m.BANK_TYPE_ID ");
        sql.append(" where 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(authCorpList, "m.corp_id", true));
        // 修改时间倒序
        input.setOrderString(new OrderString("m.update_time", "desc"));
        return this.getDynamicBySQL(input, sql.toString(), AbBillCreditInfoMaintainVO.class, "m", false);
    }

    /**
     * 银行开票授信资料，相同单位 + 相同银行 + 相同授信类型只能有一条记录
     *
     * @param corpId     单位id
     * @param bankTypeId 银行类型
     * @param creditType 授信类型
     * @param id         主键id
     */
    public boolean checkUnique(String corpId, String bankTypeId, String creditType, String id) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AbBillCreditInfoMaintain where corpId = :corpId and bankTypeId = :bankTypeId and creditType = :creditType ";
        if (StringUtils.isNotEmpty(id)) {
            hql += " and id != :id ";
            params.put("id", id);
        }
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("creditType", creditType);
        List<AbBillCreditInfoMaintain> resultList = this.listByHQLAliasToBean(hql, params, AbBillCreditInfoMaintain.class);
        return CollectionUtils.isEmpty(resultList);
    }

    /**
     * 根据单位，银行类型，授信类型，获取唯一的一条银行开票授信资料
     *
     * @param corpId     单位id
     * @param bankTypeId 银行类型
     * @param creditType 授信类型
     */
    public AbBillCreditInfoMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AbBillCreditInfoMaintain where corpId = :corpId and bankTypeId = :bankTypeId and creditType = :creditType ";
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("creditType", creditType);
        List<AbBillCreditInfoMaintainDTO> resultList = this.listByHQLAliasToBean(hql, params, AbBillCreditInfoMaintainDTO.class);
        return CollectionUtils.isEmpty(resultList) ? null : resultList.get(0);
    }
}
