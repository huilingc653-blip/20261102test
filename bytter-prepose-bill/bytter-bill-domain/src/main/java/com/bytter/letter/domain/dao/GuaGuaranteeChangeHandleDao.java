package com.bytter.letter.domain.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.letter.domain.entity.GuaGuaranteeChangeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeHandleDao
 * @Description:保函更改办理数据层
 * @date 2019年1月14日 上午11:51:14
 */
@Repository
public class GuaGuaranteeChangeHandleDao extends BaseDao<GuaGuaranteeChangeInfo, String> {
    /**
     * 获取保函申请更改办理列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.guara_id as guaraId,a.guarantee_no as guaranteeNo,b.guara_change_no as guaraChangeNo, ")
                .append(" b.change_count as changeCount,a.guarantee_type as guaranteeType,a.corp_id as corpId,a.open_corp_type as openCorpType,")
                .append(" a.open_way as openWay,a.open_type as openType,a.open_corp as openCorp,a.BANK_NAME AS openCorpName,")
                .append(" a.income_corp as incomeCorp,a.guarantee_attr as guaranteeAttr,a.cur_id as curId,b.gua_change_type as guaChangeType,")
                .append(" b.gua_change_money as guaChangeMoney,b.guarantee_money as guaranteeMoney,b.expect_end_date as expectEndDate,")
                .append(" b.start_date as startDate,b.end_date as endDate,b.status as status,b.handle_charge as handleCharge,a.cur_name as curName, ")
                .append(" b.bond_change_type as bondChangeType,b.bond_change_money as bondChangeMoney,b.bond_money as bondMoney,a.corp_name as corpName,")
                .append(" b.change_date as changeDate,b.change_term as changeTerm,b.change_reason as changeReason,b.operator as operator")
                .append(" from GUA_GUARANTEE_CHANGE_INFO b,GUA_GUARANTEE_INFO a where b.status in(95) ")
                .append(" and  b.guara_id=a.id ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "a.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaGuaranteeChangeInfoVO.class, "", false);
    }
}
