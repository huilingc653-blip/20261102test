package com.bytter.letter.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeReceiveDao
 * @Description 保函领取dao
 * @date 2019年1月8日
 */
@Repository
public class GuaGuaranteeReceiveDao extends BaseDao<GuaGuaranteeInfo, String> {
    /**
     * 领取列表数据
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append("B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append("B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,B.BANK_NAME AS openCorpName,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append("B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,");
        sql.append("B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
        sql.append("b.corp_name as corpName,b.cur_name as curName,");
        sql.append("B.UPDATE_BY AS	updateBy  FROM GUA_GUARANTEE_INFO B WHERE 1=1 AND B.STATUS = 95 ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaGuaranteeInfoVO.class, "", false);
    }
    /**
     * 通过保函id查询相应的保函信息
     *
     * @param id
     * @return
     */
    public GuaGuaranteeInfoVO getGuaGuaranteeInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append("B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append("B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,B.RATMODEL AS ratmodel,");
        sql.append("B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,");
        sql.append("B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
        sql.append("B.UPDATE_BY AS	updateBy,B.IS_HOOK_BOND AS isHookBond,B.IS_RMB_RAT AS  isRmbRat,");
        sql.append("B.BOND_CURID  AS bondCurId,B.BOND_CONTRACT AS bondContract,B.IS_OPEN_RAT  AS isOpenRat,B.bond_rate as bondRate, ");
        sql.append("B.CORP_NAME as corpName,B.CUR_NAME AS curName,B.BANK_NAME AS openCorpName,bc.CUR_NAME as bondCurName ");
        sql.append(" FROM GUA_GUARANTEE_INFO B  ");
        sql.append(" left join bt_currency bc on bc.id = b.BOND_CURID ");
        sql.append(" where B.STATUS=95 AND B.ID=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaGuaranteeInfoVO.class);
    }
}
