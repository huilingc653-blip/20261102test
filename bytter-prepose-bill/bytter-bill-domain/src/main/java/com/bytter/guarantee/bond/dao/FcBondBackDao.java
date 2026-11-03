package com.bytter.guarantee.bond.dao;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBondBack;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBackVO;
import com.bytter.guarantee.vo.FcBondBalVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author fusb
 * @version 1.0
 * @Title FcBondBackDao
 * @Description 保证金转回dao
 * @date 2018年9月17日
 */
@Repository
public class FcBondBackDao extends BaseDao<FcBondBack, String> {
    /**
     * 查询保证金转回列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<FcBondBackVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        // // 保证金到期提醒日期
        // String endDate= Convert.toStr(dataTablesInput.getFormdata().get("endDate"));
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS bondId,B.bank_acc_id AS bankAccId,B.CORP_ID AS corpId, B.CORP_NAME AS corpName,B.CUR_ID AS curId,B.CUR_NAME AS curName,");
        sql.append("B.BANK_NAME AS bankName,B.BANK_ACC AS bankAcc,B.ACC_NAME AS accName,A.backMoney as backMoney,A.backInterest as backInterest,");
        sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY) AS balMoney,(C.APPLY_MONEY+C.FREEZE_MONEY) AS applyMoney,");
        sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY-C.APPLY_MONEY-C.FREEZE_MONEY) AS availMoney, t.bond_type_name as bondTypeName ");
        sql.append(" FROM (SELECT ID,BANK_ACC_ID,CORP_ID,CORP_NAME, cur_id,CUR_NAME,bond_type_id,BANK_NAME,BANK_ACC,ACC_NAME FROM FC_BOND fb WHERE fb.STATUS IN (95) ");
        sql.append(" and exists (SELECT f.ID FROM FC_BOND f WHERE fb.id = f.id and f.STATUS = 95 ");
//        sql.append(" and f.IS_REGULAR = '1' and f.end_date <= to_date('").append(DateUtils.formatDate(DateUtils.getDate())).append("', 'yyyy-MM-dd') ");
//        sql.append(" union ");
//        sql.append(" SELECT f.ID FROM FC_BOND f WHERE fb.id = f.id and f.STATUS = 95 and f.IS_REGULAR = '0' ");
        sql.append(" )) B left join fc_bond_type t on b.bond_type_id = t.id ");
        sql.append("  LEFT JOIN (select BOND_ID AS bondId,SUM(BACK_MONEY) as backMoney,SUM(BACK_INTEREST) AS backInterest");
        sql.append("  FROM  FC_BOND_BACK GROUP BY  FC_BOND_BACK.BOND_ID) A ON B.ID=A.bondId ");
        sql.append(" LEFT JOIN FC_BOND_BAL C on B.ID = C.BOND_ID");
        sql.append(" where 1=1 ");
//         sql.append("  and not exists (select 1  from nis_allocation_req n");
//          sql.append("  where B.id = n.erp_id and n.status <> -2 ");
//           sql.append("  and ((n.status in (95,100) ) or (n.status not in (95,100))) ");
//           sql.append("  and n.bill_from = '3' )");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String erpIds = Convert.toStr(dataTablesInput.getFormdata().get("erpIds"));
        if (ObjectUtil.isNotEmpty(erpIds)) {
            String[] ids = erpIds.split(",");
            List<String> idList = Arrays.asList(ids);
            sql.append(" and ").append(getSQLNotIn(idList, 900, "b.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondBackVO.class, "", false);
    }
    /**
     * 根据保证金id,得到保证金账户信息
     *
     * @param id
     * @return
     */
    public FcBondBackVO getByIdFcBondBackVO(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d.bondId,d.corpId,d.curId,d.bankAccId,(CASE WHEN d.backMoney IS NULL THEN 0 ELSE d.backMoney END) backMoney, ");
        sql.append("(CASE WHEN d.backInterest IS NULL THEN 0 ELSE d.backInterest END) backInterest,d.balMoney,d.applyMoney,d.availMoney,d.status, ");
        sql.append(" d.bondTypeName,d.startDate,d.isRegular, d.interestRate,d.endDate,d.dayLimit,d.bondMoney ");
        sql.append("FROM (SELECT B.ID AS bondId,B.STATUS AS status,B.CORP_ID AS corpId,B.CUR_ID AS curId,B.bank_acc_id AS bankAccId,");
        sql.append("A.backMoney as backMoney,A.backInterest as backInterest,");
        sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY) AS balMoney,(C.APPLY_MONEY+C.FREEZE_MONEY) AS applyMoney,");
        sql.append("(C.BOND_MONEY+C.ADD_MONEY-C.BACK_MONEY-C.APPLY_MONEY-C.FREEZE_MONEY) AS availMoney, ");
        sql.append(" t.bond_type_name as bondTypeName,b.START_DATE as startDate,b.is_regular as isRegular, ");
        sql.append(" b.interest_rate as interestRate,b.end_date as endDate,b.day_limit as dayLimit,b.bond_money as bondMoney ");
        sql.append(" FROM FC_BOND B LEFT JOIN (select BOND_ID AS bondId,SUM(BACK_MONEY) as backMoney,SUM(BACK_INTEREST) AS backInterest ");
        sql.append(" FROM  FC_BOND_BACK GROUP BY  FC_BOND_BACK.BOND_ID) A ON B.ID=A.bondId ");
        sql.append(" LEFT JOIN FC_BOND_BAL C on B.ID = C.BOND_ID ");
        sql.append(" left join fc_bond_type t on t.id = b.bond_type_id ");
        sql.append(" ) d ");
        sql.append("  where bondId=:id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcBondBackVO.class);
    }
    /**
     * 通过保证金ID得到相应的保证金余额表中的信息
     *
     * @param bondId
     * @return
     */
    public FcBondBal getFcBondBalById(String bondId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ID AS id,BOND_ID AS bondId,BOND_MONEY AS bondMoney,ADD_MONEY AS addMoney,");
        sql.append("APPLY_MONEY AS  applyMoney,FREEZE_MONEY AS freezeMoney,BACK_MONEY AS backMoney ");
        sql.append("FROM FC_BOND_BAL ");
        sql.append("where BOND_ID=:bondId");
        Map<String, String> params = new HashMap<String, String>();
        params.put("bondId", bondId);
        return getBySQLAliasToBean(sql.toString(), params, FcBondBal.class);
    }
    /**
     * 根据保证金id,得到账户项目信息
     *
     * @param id
     * @return
     */
    public List<FcBondApplyVO> getByIdFcBondApplys(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  B.ID AS id,B.BILL_NO AS billNo,B.APPLY_TYPE AS applyType,B.ITEM_TYPE AS itemType,");
        sql.append("B.ITEM_NO AS itemNo,B.CORP_ID AS corpId,B.BANK_ID AS bankId,B.ITEM_MONEY AS itemMoney,");
        sql.append("B.BOND_PERCENT AS bondPercent,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append("B.ITEM_UNIT AS itemUnit,B.BOND_CONTRACT AS bondContract,B.BOND_ID AS bondId,");
        sql.append("B.APPLY_MONEY as applyMoney,B.REMARK AS remark,B.STATUS AS status,B.CREATE_BY AS createBy,");
        sql.append("B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updatDate  ");
        sql.append(" FROM FC_BOND_APPLY B  where B.STATUS='95'");
        sql.append(" and bond_id=:id order by B.START_DATE desc ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, FcBondApplyVO.class);
    }

    public DataTablesOutput<FcBondApplyVO> fcBondApplyDatatable(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  c.ID AS id,c.BILL_NO AS billNo,c.APPLY_TYPE AS applyType,c.ITEM_TYPE AS itemType,");
        sql.append("c.ITEM_NO AS itemNo,c.CORP_ID AS corpId,c.BANK_ID AS bankId,c.ITEM_MONEY AS itemMoney,");
        sql.append("c.BOND_PERCENT AS bondPercent,c.START_DATE AS startDate,c.END_DATE AS endDate,");
        sql.append("c.ITEM_UNIT AS itemUnit,c.BOND_CONTRACT AS bondContract,c.BOND_ID AS bondId,");
        sql.append("c.APPLY_MONEY as applyMoney,c.REMARK AS remark,c.STATUS AS status,c.CREATE_BY AS createBy,");
        sql.append("c.CREATE_DATE AS createDate,c.UPDATE_BY AS updateBy,c.UPDATE_DATE AS updatDate  ");
        sql.append(" FROM FC_BOND_APPLY c  where c.STATUS='95' ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcBondApplyVO.class, "", false);
    }
    /**
     * 根据保证金id,得到转回历史记录
     *
     * @param id
     * @return
     */
    public List<FcBondBackVO> getByIdFcBondBacks(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.BILL_NO AS billNo,B.BOND_ID AS bondId,B.BACK_DATE AS backDate,");
        sql.append("B.BACK_MONEY AS backMoney,B.BACK_INTEREST AS backInterest,B.STATUS AS status,");
        sql.append("B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,B.UPDATE_BY AS updateBy,");
        sql.append("B.UPDATE_DATE AS updateDate,b.out_bank_acc as outBankAcc,b.purpose as purpose ");
        sql.append(" from FC_BOND_BACK B ");
        sql.append(" where bond_id=:id  order by B.BACK_DATE asc ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, FcBondBackVO.class);
    }

    public int updateFcBondBack(String bondId, BigDecimal backMoney) {
        StringBuilder sql = new StringBuilder();
        sql.append("update FC_BOND_BAL set back_money=:backMoney where bond_id=:bondId");
        Map<String, Object> params = new HashMap<>();
        params.put("backMoney", backMoney);
        params.put("bondId", bondId);
        return this.createSQLQuery(sql.toString(), params).executeUpdate();
    }

    public List<FcBondBalVO> getFcBondBalListForAutoFcBondRotationJob() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT v.ID,v.BOND_ID bondId,v.`BACK_MONEY` backMoney ,v.`BOND_MONEY`+v.`ADD_MONEY`-v.`APPLY_MONEY`-v.`FREEZE_MONEY`-v.`BACK_MONEY` AS availMoney ");
        sql.append("FROM fc_bond_bal v ");
        sql.append("JOIN fc_bond t ON v.`BOND_ID`=t.`ID` ");
        sql.append("JOIN bt_bank_acc a ON t.BANK_ACC_ID = a.ID ");
        sql.append("JOIN bt_bank_type bbt ON a.BANK_TYPE_ID =  bbt.ID AND bbt.BANK_TYPE <> '9004' ");
        sql.append("WHERE t.`STATUS` = 95 AND (v.`BOND_MONEY`+v.`ADD_MONEY`-v.`APPLY_MONEY`-v.`FREEZE_MONEY`-v.`BACK_MONEY`) > 0 ");
        return listBySQLAliasToBean(sql.toString(), null, FcBondBalVO.class);
    }
}
