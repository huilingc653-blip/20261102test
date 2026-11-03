package com.bytter.letter.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.letter.domain.entity.GuaPerformReg;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.letter.domain.vo.GuaPerformRegVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author fusb
 * @version 1.0
 * @Title GuaPerformRegDao
 * @Description 履行登记dao
 * @date 2019年1月8日
 */
@Repository
public class GuaPerformRegDao extends BaseDao<GuaPerformReg, String> {
    /**
     * 领取列表数据
     *
     * @param dataTablesInput 查询参数
     * @param corpAuth        有权限的id集合
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.GUARANTEE_NO AS guaranteeNo,B.GUARANTEE_TYPE AS guaranteeType,");
        sql.append("B.CORP_ID AS corpId,B.OPEN_CORP_TYPE AS openCorpType,B.OPEN_WAY AS openWay,B.OPEN_TYPE AS openType,");
        sql.append("B.OPEN_CORP AS openCorp,B.INCOME_CORP AS incomeCorp,B.CUR_ID AS curId,");
        sql.append("B.GUARANTEE_MONEY AS guaranteeMoney,B.GUARANTEE_ATTR AS guaranteeAttr,B.GUA_ITEM AS guaItem,");
        sql.append("B.EXPECT_END_DATE AS expectEndDate,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append("B.TURN_BANK AS turnBank,B.STATUS AS status,B.ATTACHMENT_ID AS attachmentId,B.HANDLE_RATE AS handleRate,");
        sql.append("B.HANDLE_CHARGE AS handleCharge,B.BOND_PERCENT AS bondPercent,B.BOND_MONEY AS bondMoney,");
        sql.append("B.BOND_ACC AS bondAcc,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,");
        sql.append("B.UPDATE_BY AS	updateBy,B.IS_HOOK_BOND AS isHookBond,B.BOND_CURID  AS bondCurId,B.BOND_CONTRACT AS bondContract, ");
        sql.append("B.CORP_NAME as corpName,B.CUR_NAME AS curName,B.BANK_NAME AS openCorpName ");
        sql.append(" FROM GUA_GUARANTEE_INFO B WHERE 1=1 AND B.STATUS=96 ");
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
        sql.append("B.BOND_ACC AS bondAcc,B.Receiver as receiver,B.receive_date as receiveDate,");
        sql.append("B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate, ");
        sql.append("B.UPDATE_BY AS	updateBy,B.IS_HOOK_BOND AS isHookBond, ");
        sql.append("B.BOND_CURID  AS bondCurId,B.BOND_CONTRACT AS bondContract,B.bond_rate as bondRate, ");
        sql.append("B.CORP_NAME as corpName,B.CUR_NAME AS curName,B.BANK_NAME AS openCorpName ");
        sql.append("FROM GUA_GUARANTEE_INFO B WHERE B.ID=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaGuaranteeInfoVO.class);
    }
    /**
     * 通过保函id查询相应的履行登记信息
     *
     * @param id
     * @return
     */
    public GuaPerformRegVO getGuaPerformRegByGuaId(String guaraId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id,guara_id as guaraId,contract_no as contractNo,contract_date as contractDate,");
        sql.append("payment_method as paymentMethod,payment_date as paymentDate,gua_number as guaNumber,rmk ");
        sql.append(" from GUA_PERFORM_REG where guara_id=:guaraId ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("guaraId", guaraId);
        return getBySQLAliasToBean(sql.toString(), params, GuaPerformRegVO.class);
    }
}
