package com.bytter.report.domain.cms.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.RationConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticVO;
import com.bytter.report.domain.cms.vo.RatUrationUseBeanVO;
import com.bytter.report.domain.cms.vo.RatUrationVO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RatBkStatisticsDao extends BaseDao<RatBkStatisticVO, String> {


    /**
     * 根据 单位,银行,授信结束日期 查询统计额度
     *
     * @param ratVO
     * @return
     */
    public List<RatBkStatisticVO> getRatBkAmountStatisticsList(RatBkStatisticSearchVO ratVO, List<String> corpIds) {
        if (StringUtils.isNotEmpty(ratVO.getCorpId()) && EmptyUtils.isNotEmpty(ratVO.getCorpId().trim())) {
            corpIds = Arrays.asList(ratVO.getCorpId().split(","));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select rat.corpId as corpId,sc.name as corpName,sc.code as corpNo,rat.ratCode as ratCode,bb.bank_name as bankName,bb.sys_bank_code as bankCode,");
        sql.append("rat.money as money,rat.superProcessMoney as useMoney,rat.freezeMoney as freezeMoney,rat.addMoney as addMoney,");
        sql.append("rat.virtualUration as virtualUration,rat.virtualRelief as virtualRelief,rat.reliefAndUrationMoney as reliefAndUrationMoney,");
        sql.append("rat.distributeway as disWay,rb.cur_code as curCode,cur.cur_name as curName,rb.beginDate as beginDate,rb.endDate as endDate,rb.rat_mode as ratMode ");

        sql.append(" from (SELECT E.corp_id as corpId,E.RAT_CODE as ratCode,SUM(E.MONEY) as money,SUM(E.SUPERPROCESSMONEY) as superProcessMoney,");
        sql.append("SUM(E.freezeMoney) as freezeMoney,SUM(E.addMoney) as addMoney,SUM(E.VIRTUALURATION) as virtualUration,");
        sql.append("SUM(E.VIRTUALRELIEF) as virtualRelief,SUM(E.RELIEFANDURATIONMONEY) as reliefAndUrationMoney,E.DISTRIBUTE_WAY as distributeway ");
        sql.append(" FROM (SELECT M.corp_id,M.RAT_CODE,M.MONEY,N.SUPERPROCESSMONEY,N.freezeMoney,N.addMoney,N.VIRTUALURATION,N.VIRTUALRELIEF,");
        sql.append("N.RELIEFANDURATIONMONEY,N.DISTRIBUTE_WAY ");
        sql.append(" FROM (SELECT RBD.corp_id,RBD.RAT_CODE,A.MONEY FROM RAT_BKRATION_DIS RBD,");
        sql.append("(SELECT RBDD.DIS_CODE,SUM(RBDD.MONEY +RBDD.SUPER_BY_RELIEF_MONEY -RBDD.SUPER_RELIEF_MONEY) MONEY ");
        sql.append(" FROM RAT_BKRATION_DIS RBD, RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("WHERE RBDD.DIS_CODE = RBD.id GROUP BY RBDD.DIS_CODE) A ");
        sql.append("WHERE RBD.id = A.DIS_CODE ");
        if (null != corpIds && corpIds.size() > 0) {
            sql.append("and (" + getSQLIn(corpIds, 900, "rbd.corp_id") + ") ");
        }
        sql.append(" AND RBD.STATUS = 95) M,");
        sql.append("(SELECT RBD.corp_id,RBD.RAT_CODE,B.SUPERPROCESSMONEY,B.freezeMoney,case when B.addMoney is null then 0 else B.addMoney end addMoney,");
        sql.append("B.VIRTUALURATION,B.VIRTUALRELIEF,B.RELIEFANDURATIONMONEY, RBD.DISTRIBUTE_WAY ");
        sql.append(" FROM RAT_BKRATION_DIS RBD,(SELECT RBDD.DIS_CODE,SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION,");
        sql.append("SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF,SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY,");
        sql.append("SUM(RBDD.SUPER_PROCESS_MONEY) SUPERPROCESSMONEY,SUM(FREEZE_MONEY) as freezeMoney,SUM(radd.MONEY) as addMoney ");
        sql.append(" FROM RAT_BKRATION_DIS RBD inner JOIN RAT_BKRATION_DIS_DETAIL RBDD ON RBDD.DIS_CODE = RBD.id ");
        sql.append(" LEFT JOIN RAT_BKRATION RBK ON RBD.RAT_CODE = RBK.id ");
        sql.append("LEFT JOIN (select RBAA.rat_code,RBAA.corp_id,SUM((-1) * RBAD.MONEY) as MONEY ");
        sql.append(" from RAT_BKRATION_ADD RBAA LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD ON RBAD.ADD_CODE = RBAA.id ");
        sql.append(" where RBAD.money < 0 AND RBAA.FLAG = :flag ");
        sql.append("AND 11 <= RBAA.STATUS AND RBAA.STATUS < 95 group by RBAA.rat_code,RBAA.corp_id) radd ");
        sql.append("on radd.rat_code = RBK.id and radd.CORP_ID = RBD.CORP_ID ");
        sql.append("where RBD.status = 95 GROUP BY RBDD.DIS_CODE) B ");
        sql.append("WHERE RBD.id = B.DIS_CODE ");
        if (null != corpIds && corpIds.size() > 0) {
            sql.append("and (" + getSQLIn(corpIds, 900, "rbd.corp_id") + ") ");
        }
        sql.append(" AND RBD.STATUS = 95) N ");
        sql.append(" WHERE M.RAT_CODE = N.RAT_CODE AND M.corp_id = N.corp_id) E ");
        sql.append("GROUP BY E.corp_id,E.RAT_CODE,E.DISTRIBUTE_WAY) rat,sys_corp sc,bt_bank bb,rat_bkration rb,BT_CURRENCY cur  ");
        sql.append(" where rat.corpid = sc.id and rat.ratCode = rb.id and rb.BANK_CODE = bb.sys_bank_code and rb.cur_code = cur.id ");
//		sql.append(" and rat.corpId in (:corpId) ");
        if (null != corpIds && corpIds.size() > 0) {
            sql.append("and (" + getSQLIn(corpIds, 900, "rat.corpId") + ") ");
        }
        if (!EmptyUtils.isEmpty(ratVO.getCurCode()) && !EmptyUtils.isEmpty(ratVO.getCurCode().trim())) {
            sql.append(" and rb.cur_CODE = :curCode ");
            params.put("curCode", ratVO.getCurCode());
        }
        if (!EmptyUtils.isEmpty(ratVO.getBankCode())) {
            sql.append(" and rb.BANK_CODE = :bankCode ");
            params.put("bankCode", ratVO.getBankCode());
        }
        if (!EmptyUtils.isEmpty(ratVO.getRatType())) {
            sql.append(" and rb.RAT_TYPE = :ratType ");
            params.put("ratType", ratVO.getRatType());
        }
        if (!EmptyUtils.isEmpty(ratVO.getBankTypeId()) && !EmptyUtils.isEmpty(ratVO.getBankTypeId().trim())) {
            sql.append(" and bb.bank_type_id in (:bankTypeId) ");
            params.put("bankTypeId", ratVO.getBankTypeId().split(","));
        }
        if (!EmptyUtils.isEmpty(ratVO.getBeginDate())) {
            sql.append(" and rb.begindate >=:beginDate ");
            params.put("ratDate", ratVO.getRatDate());
            params.put("beginDate", ratVO.getBeginDate());
        }

        if (!EmptyUtils.isEmpty(ratVO.getEndDate())) {
            sql.append(" and rb.begindate <=:endDate ");
            params.put("endDate", ratVO.getEndDate());
        }

        if (!EmptyUtils.isEmpty(ratVO.getBeginDate2())) {
            sql.append(" and rb.enddate >=:beginDate2 ");
            params.put("beginDate2", ratVO.getBeginDate2());
        }

        if (!EmptyUtils.isEmpty(ratVO.getEndDate2())) {
            sql.append(" and rb.enddate <=:endDate2 ");
            params.put("endDate2", ratVO.getEndDate2());
        }

        if (!EmptyUtils.isEmpty(ratVO.getRatMode()) && !EmptyUtils.isEmpty(ratVO.getRatMode().trim())) {
            sql.append(" and rb.rat_mode = :ratMode ");
            params.put("ratMode", ratVO.getRatMode());
        }
        params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);//集团追加
        sql.append(" order by rat.corpid, rat.ratCode ");
        return listBySQLAliasToBean(sql.toString(), params, RatBkStatisticVO.class);
    }

    /**
     * 功能介绍 : 查询授信占用情况。<br>
     * <b>日期：</b>2018年03月16日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public List<RatUrationUseBeanVO> queryCorpRationUseDtl(RatBkStatisticSearchVO searchVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE as type,rs.name as typeName, ");
        sql.append(" ru.BUSINESS_NAME as businessName, ru.ITEM_FLAG as itemFlag,");
        sql.append(" ru.CORP_ID as corpId, sc.name as corpName,rb.BANK_CODE as bankCode,bb.BANK_NAME as bankName, ");
        sql.append(" rb.CUR_CODE as curCode,bc.CUR_NAME as curName,ru.init_CUR_CODE as initCurCode,cur.CUR_NAME as initCurName,");
        sql.append(" ru.ITEM_CODE as itemCode,rc.NAME as itemName,ru.MONEY as money,ru.CONVERT_PRE_MONEY as convertPreMoney,");
        sql.append(" ru.EXCHANGE_RATE as exchangeRate, ");
        sql.append(" ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate, ");
        sql.append(" bt_nvl(ru.credit_type,'1') as creditType, ");
        sql.append(" ru.BILL_ID as billId, ru.BILL_NO as billNo, ru.status as status ");
        sql.append(" from RAT_URATION ru  ");
        sql.append(" left join RAT_BKRATION rb on rb.id = ru.RAT_CODE ");
        sql.append(" left join RAT_SYS_ITEM rs on ru.TYPE = rs.CODE ");
        sql.append(" left join sys_corp sc on ru.CORP_ID = sc.id ");
        sql.append(" left join BT_CURRENCY bc on ru.CUR_CODE = bc.ID ");
        sql.append(" left join BT_CURRENCY cur on ru.INIT_CUR_CODE = cur.ID ");
        sql.append(" left join bt_bank bb on rb.BANK_CODE = bb.SYS_BANK_CODE ");
        sql.append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID ");
        sql.append(" where rb.RAT_TYPE = '0' ");
        if (EmptyUtils.isNotEmpty(searchVO.getCorpId())) {
            sql.append(" and ru.corp_id = :corpId ");
            params.put("corpId", searchVO.getCorpId());
        }
        if (EmptyUtils.isNotEmpty(searchVO.getRatCode())) {
            sql.append(" and ru.rat_code = :ratCode ");
            params.put("ratCode", searchVO.getRatCode());
        }
        if (EmptyUtils.isNotEmpty(searchVO.getCurCode())) {
            sql.append(" and ru.cur_code = :curCode ");
            params.put("curCode", searchVO.getCurCode());
        }
        sql.append(" and ru.STATUS in (:status) ");
        params.put("status", new Integer[]{RationConstant.RAT_USE_STATUS_USE, RationConstant.RAT_USE_STATUS_CLOSE});
        if (!EmptyUtils.isEmpty(searchVO.getBeginDate())) {
            sql.append(" and ru.begin_date >=:beginDate ");
            params.put("beginDate", searchVO.getBeginDate());
        }
        if (!EmptyUtils.isEmpty(searchVO.getEndDate())) {
            sql.append(" and ru.end_date <=:endDate ");
            params.put("endDate", searchVO.getEndDate());
        }
        sql.append(" order by ru.create_date desc ");

        return listBySQLAliasToBean(sql.toString(), params, RatUrationUseBeanVO.class);
    }

    public List<RatUrationVO> getListByRatCodes(List<String> ratCodeList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" SELECT SUM(MONEY) as money, RAT_CODE as ratCode, CORP_ID as corpId FROM RAT_URATION where status = 4 and rat_code in (:ratCodes) GROUP BY RAT_CODE, CORP_ID ");
        params.put("ratCodes", ratCodeList);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationVO.class);
    }
}
