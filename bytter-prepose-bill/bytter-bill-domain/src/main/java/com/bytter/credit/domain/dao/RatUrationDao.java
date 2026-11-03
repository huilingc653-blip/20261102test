package com.bytter.credit.domain.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.*;
import com.bytter.credit.vo.RatUrationSysBean;
import com.bytter.system.api.dto.BtBankDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 授信占用台账表
 * @date 2017-08-19 11:36:37
 */
@Repository
public class RatUrationDao extends BaseDao<RatUration, String> {
    /**
     * 功能介绍 : 根据系统类型和业务编号获取占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUration> queryRatUrationById(String sysCode, String billNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select r from RatUration r where r.syscode = :sysCode and r.billId = :billNo and r.ratType=:ratType ");
        sql.append(" and r.status <> :status ");
        params.put("sysCode", sysCode);
        params.put("billNo", billNo);
        params.put("status", RationConstant.RAT_USE_STATUS_CLOSE);
        params.put("ratType", RationConstant.RAT_TYPE_0);// 银行授信
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 根据系统类型和业务编号获取占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUration> queryRatUrationById(String sysCode, String billNo, String ratType) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select r from RatUration r where r.syscode = :sysCode and r.billId = :billNo and r.ratType=:ratType ");
        sql.append(" and r.status <> :status ");
        params.put("sysCode", sysCode);
        params.put("billNo", billNo);
        params.put("status", RationConstant.RAT_USE_STATUS_CLOSE);
        params.put("ratType", ratType);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 根据系统类型和业务编号获取正式占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUration> queryUrationByBillId(String sysCode, String billNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select r from RatUration r where r.syscode = :sysCode and r.billId = :billNo ");
        sql.append(" and r.status = :status ");
        params.put("sysCode", sysCode);
        params.put("billNo", billNo);
        params.put("status", RationConstant.RAT_USE_STATUS_USE);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 查询授信占用可结转数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @param corpAuthSql
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput queryRatURationList(DataTablesInput input, String... corpAuthSql) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE as type,rs.name as typeName, ");
        sql.append(" ru.CORP_ID as corpId, rb.corp_name as corpName,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.CUR_CODE as curCode,rb.CUR_NAME as curName, ");
        sql.append(" ru.ITEM_FLAG as itemFlag,ru.ITEM_CODE as itemCode,rc.NAME as itemName, ");
        sql.append(" ru.MONEY as money,ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate, ");
        sql.append(" ru.BILL_ID as billId, ru.BILL_NO as billNo,ru.remark as remark, ");
        sql.append(" rb.RAT_CODE as ratCode,ru.status as status, race.race_name  as billName ");
        sql.append(" from RAT_URATION ru  ");
        sql.append(" left join RAT_URATION_APPLY ra on ra.id = ru.BILL_id ");
        sql.append(" left join RAT_BKRATION rb on rb.id = ru.RAT_CODE ");
        sql.append(" left join rat_bkration_dis rd on rd.id = ru.dis_code ");
        sql.append(" left join RAT_SYS_ITEM rs on ru.TYPE = rs.CODE ");
        sql.append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID ");
        sql.append(" left join rat_business_race race on ru.bill_name=race.id ");
        sql.append(" where ru.STATUS = 2 and rb.RAT_TYPE = '0' ");
        sql.append(" and bt_nvl_char(ru.credit_type, '1') <>0 ");// 过滤不循环授信
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatUrationBeanDTO.class, "", false);
    }
    // 获取授信录入对应状态的授信占用数据(循环授信)
    public List<RatUrationDisBean> queryByCodeStatus(String ratCode, List<Integer> statuss) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE as type,rs.name as typeName, ");
//        sql.append(" ru.CORP_ID as corpId, sc.name as corpName,rb.BANK_CODE as bankCode,bb.BANK_NAME as bankName, ");
//        sql.append(" rb.CUR_CODE as curCode,bc.CUR_NAME as curName, ");
        sql.append(" ru.CORP_ID as corpId, rb.CORP_NAME as corpName,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(" rb.CUR_CODE as curCode,rb.CUR_NAME as curName, ");
        sql.append(" ru.ITEM_FLAG as itemFlag,ru.ITEM_CODE as itemCode,rc.NAME as itemName, ");
        sql.append(" ru.MONEY as money,ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate, ");
        sql.append(" ru.BILL_ID as billId, ru.BILL_NO as billNo,ru.remark as remark, ");
        sql.append(" rb.RAT_CODE as ratCode,ru.status as status, race.race_name  as billName ");
        sql.append(" from RAT_URATION ru  ");
        sql.append(" left join RAT_URATION_APPLY ra on ra.id = ru.BILL_id ");
        sql.append(" left join RAT_BKRATION rb on rb.id = ru.RAT_CODE ");
        sql.append(" left join RAT_SYS_ITEM rs on ru.TYPE = rs.CODE ");
//        sql.append(" left join sys_corp sc on ru.CORP_ID = sc.id ");
//        sql.append(" left join BT_CURRENCY bc on rb.CUR_CODE = bc.ID ");
//        sql.append(" left join bt_bank bb on rb.BANK_CODE = bb.SYS_BANK_CODE ");
        sql.append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID ");
        sql.append(" left join rat_business_race race on ru.bill_name=race.id ");
        sql.append(" where rb.RAT_TYPE = '0' ");
        sql.append(" and bt_nvl_char(ru.credit_type, '1') <>0 ");// 过滤不循环授信
        sql.append(" and ru.RAT_CODE=:ratCode");
        sql.append(" and ").append(getSQLIn(statuss, 900, "ru.status"));
        params.put("ratCode", ratCode);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信额度及占用情况。<br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisBean> queryCorpRatUration(String corpId, List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称变更调整
        sql.append(" select m.corp_id, ");
//        sql.append(" sc.name as corpName, ");
//        sql.append(" sc.code as corpNo, ");
        sql.append(" m.CORP_NAME as corpName, ");
        sql.append(" m.CORP_CODE as corpNo, ");
        sql.append(" m.rat_code as ratCode, ");
        sql.append(" rb.rat_code as ratNo, ");
        sql.append(" rb.begindate as beginDate, ");
        sql.append(" rb.enddate as endDate, ");
//        sql.append(" bb.bank_name as bankName, ");
        sql.append(" rb.bank_name as bankName, ");
        sql.append(" m.money as money, ");
        sql.append(" n.superProcessMoney as useMoney, ");
        sql.append(" n.freezeMoney as freezeMoney, ");
        sql.append(" case when n.addMoney is null then 0 else n.addMoney end as addMoney, ");
        sql.append(" N.VIRTUALURATION as VIRTUALURATION, ");
        sql.append(" N.VIRTUALRELIEF as VIRTUALRELIEF, ");
        sql.append(" N.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY, ");
        sql.append(" N.DISTRIBUTE_WAY as disWay ");
//        sql.append(" from (select rbd.CORP_ID, rbd.rat_code, a.money ");
        sql.append(" from (select rbd.CORP_ID, rbd.CORP_NAME, rbd.CORP_CODE, rbd.rat_code, a.money ");
        sql.append(" from rat_bkration_dis rbd, ");
        sql.append(" (select rbdd.DIS_CODE, ");
        sql.append(" sum(rbdd.money + rbdd.super_by_relief_money - ");
        sql.append(" rbdd.super_relief_money) money ");
        sql.append(" from rat_bkration_dis rbd, rat_bkration_dis_detail rbdd ");
        sql.append(" where rbdd.DIS_CODE = rbd.id ");
        sql.append(" group by rbdd.DIS_CODE) a ");
        sql.append(" where rbd.id = a.DIS_CODE ");
        sql.append(" and rbd.corp_id = :corpId ");
        sql.append(" and rbd.status = 95) m, ");
        sql.append(" (select rbd.corp_id, ");
        sql.append(" rbd.rat_code, ");
        sql.append(" b.superProcessMoney, ");
        sql.append(" b.freezeMoney, ");
        sql.append(" b.addMoney, ");
        sql.append(" B.VIRTUALURATION, ");
        sql.append(" B.VIRTUALRELIEF, ");
        sql.append(" B.RELIEFANDURATIONMONEY, ");
        sql.append(" RBD.DISTRIBUTE_WAY ");
        sql.append(" from rat_bkration_dis rbd, ");
        sql.append(" (select rbdd.dis_code, ");
        sql.append(" SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION, ");
        sql.append(" SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF, ");
        sql.append(" SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY, ");
        sql.append(" sum(rbdd.super_process_money) superProcessMoney,    ");
        sql.append(" SUM(FREEZE_MONEY) as freezeMoney, ");
        sql.append(" SUM(radd.MONEY) as  addMoney ");
        sql.append(" FROM RAT_BKRATION_DIS RBD ");
        sql.append(" JOIN RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append(" ON RBDD.DIS_CODE = RBD.id ");
        sql.append(" LEFT JOIN RAT_BKRATION RBK ");
        sql.append(" ON RBD.RAT_CODE = RBK.id ");
        sql.append("  LEFT JOIN (select RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE,SUM((-1) * RBAD.MONEY) as MONEY, ");
        sql.append("    SUM((-1) * RBAD.max_MONEY) as max_MONEY ");
        sql.append("     from RAT_BKRATION_ADD RBAA ");
        sql.append("     LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD ");
        sql.append("      ON RBAD.ADD_CODE = RBAA.id ");
        sql.append("     where (RBAD.money < 0 or RBAD.max_money < 0) ");
        sql.append("     AND RBAA.FLAG = :flag ");
        sql.append("     AND 11 <= RBAA.STATUS ");
        sql.append("     AND RBAA.STATUS < 95 ");
        sql.append("     group by RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE) radd ");
        sql.append("     on radd.rat_code = RBK.id and radd.CORP_ID = RBD.CORP_ID ");
        sql.append("     AND radd.ITEM_CODE = RBDD.ITEM_CODE ");
        sql.append("     where RBD.status = 95 ");
        sql.append(" group by rbdd.dis_code) b ");
        sql.append(" where rbd.id = b.dis_code ");
        sql.append(" and rbd.corp_id = :corpId ");
        sql.append(" and rbd.status = 95) n, ");
//        sql.append(" sys_corp sc, bt_bank bb, rat_bkration rb ");
        sql.append(" rat_bkration rb ");
        sql.append(" where m.rat_code = n.rat_code ");
        sql.append(" and m.corp_id = n.corp_id ");
//        sql.append(" and m.corp_id = sc.id ");
//        sql.append(" and rb.BANK_CODE = bb.sys_bank_code ");
        sql.append(" and m.rat_code = rb.id and rb.rat_type = '0' ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "m.rat_code"));
        sql.append(" order by m.corp_id,rb.rat_code desc ");
        params.put("corpId", corpId);
        params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团追加
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信额度及占用情况。<br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisBean> queryCorpRatDisration(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(
                " select rat.corpId as corpId, rat.corpName as corpName, rat.corpCode as corpNo,rat.ratCode as ratCode,rat.disCode as disCode, ");
        sql.append(" rb.beginDate as beginDate,rb.endDate as endDate,rat.compSign as compSign, ");
        sql.append(" rb.rat_code  ratNo, rb.contract_no as contractNo, ");
        sql.append(
                " rat.itemFlag as itemFlag,rat.itemCode as itemCode,rat.itemOrder as itemOrder,rb.bank_code as bankCode,rb.bank_name as bankName, ");
        sql.append(
                " rc.name as itemName,rtype.RIT_CODE as ritCode,rat.money as money,rat.SUPERPROCESSMONEY as useMoney,rat.freezeMoney as freezeMoney, ");
        sql.append(
                " rat.addMoney as addMoney,rat.VIRTUALURATION as VIRTUALURATION,rat.VIRTUALRELIEF as VIRTUALRELIEF, ");
        sql.append(
                " rat.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY,rat.maxMONEY as maxMONEY,rat.virtualMaxMoney as virtualMaxMoney, ");
        sql.append(
                " rat.PROCESSMaxMONEY as useMaxMONEY, rat.freezeMaxMoney as freezeMaxMoney, rat.addMaxMoney as addMaxMoney,BT_NVL(rat.creditType,'1') as creditType, ");
        sql.append(
                " rat.distributeway as disWay, rb.rat_mode as ratMode, rb.cur_code as curCode,rb.cur_name as curName, rat.isHangGuarantee, ");
        sql.append(" rat.status ");
        sql.append(" from ( ");
        sql.append(" SELECT E.corp_id as corpId, E.corp_name as corpName, E.corp_code as corpCode,");
        sql.append("        E.RAT_CODE as ratCode, ");
        sql.append("        E.DIS_CODE as disCode, ");
        sql.append("        SUM(E.MONEY) as money, ");
        sql.append("        SUM(E.SUPERPROCESSMONEY) as SUPERPROCESSMONEY, ");
        sql.append("        SUM(E.freezeMoney) as freezeMoney, ");
        sql.append("        SUM(E.addMoney) as addMoney, ");
        sql.append("        SUM(E.VIRTUALURATION) as VIRTUALURATION , ");
        sql.append("        SUM(E.VIRTUALRELIEF) as VIRTUALRELIEF, ");
        sql.append("        SUM(E.RELIEFANDURATIONMONEY) as RELIEFANDURATIONMONEY, ");
        sql.append("        E.DISTRIBUTE_WAY as DISTRIBUTEWAY, ");
        sql.append("        SUM(E.maxMONEY) as maxmoney, ");
        sql.append("        SUM(E.PROCESSMaxMONEY) as PROCESSMaxMONEY, ");
        sql.append("        SUM(E.freezeMaxMoney) as freezeMaxMoney, ");
        sql.append("        SUM(E.addMaxMoney) as addMaxMoney, ");
        sql.append("        SUM(E.virtualMaxMoney) as virtualMaxMoney, ");
        sql.append("        E.ITEM_CODE as itemCode, ");
        sql.append("        e.item_flag as itemFlag, ");
        sql.append("        e.comp_Sign as compSign, ");
        sql.append("        e.item_order as itemOrder, ");
        sql.append("        E.Is_Hang_Guarantee AS isHangGuarantee, ");
        sql.append("		E.CREDIT_TYPE AS creditType, ");
        sql.append("        E.STATUS ");
        sql.append("   FROM (SELECT M.corp_id, M.corp_name, M.corp_code, ");
        sql.append("                M.RAT_CODE, ");
        sql.append("                M.DIS_CODE, ");
        sql.append("                M.MONEY, ");
        sql.append("                N.SUPERPROCESSMONEY, ");
        sql.append("                N.freezeMoney, ");
        sql.append("                N.addMoney, ");
        sql.append("                M.maxMONEY, ");
        sql.append("                N.PROCESSMaxMONEY, ");
        sql.append("                N.freezeMaxMoney, ");
        sql.append("                N.addMaxMoney, ");
        sql.append("                N.virtualMaxMoney, ");
        sql.append("                N.VIRTUALURATION, ");
        sql.append("                N.VIRTUALRELIEF, ");
        sql.append("                N.RELIEFANDURATIONMONEY, ");
        sql.append("                N.ITEM_CODE, ");
        sql.append("                N.item_flag, ");
        sql.append("                N.item_order, ");
        sql.append("                M.comp_Sign, ");
        sql.append("                N.DISTRIBUTE_WAY, ");
        sql.append("                N.Is_Hang_Guarantee, ");
        sql.append("				N.CREDIT_TYPE, ");
        sql.append("                M.STATUS ");
        //TODO 补充单位名称
        sql.append(
                "  FROM (SELECT RBD.corp_id,RBD.corp_name,RBD.corp_code,RBD.RAT_CODE,RBD.STATUS,A.dis_code,A.MONEY,A.maxMONEY,A.ITEM_CODE,A.ITEM_FLAG,A.item_order,A.comp_Sign ");
        sql.append("                   FROM RAT_BKRATION_DIS RBD, ");
        sql.append("                        (SELECT RBDD.DIS_CODE, ");
        sql.append("                                RBDD.ITEM_CODE, ");
        sql.append("                                RBDD.ITEM_FLAG, ");
        sql.append("                                RBDD.item_order, ");
        sql.append("                                RBDD.comp_Sign, ");
        sql.append("                                SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - ");
        sql.append("                                    RBDD.SUPER_RELIEF_MONEY) MONEY, ");
        sql.append("                                SUM(RBDD.max_MONEY + RBDD.BY_RELIEF_max_MONEY - ");
        sql.append("                                	RBDD.RELIEF_max_MONEY) as  maxMONEY ");
        sql.append("                           FROM RAT_BKRATION_DIS        RBD, ");
        sql.append("                                RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("                          WHERE RBDD.DIS_CODE = RBD.id ");
        sql.append(
                "                          GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE, RBDD.ITEM_FLAG,RBDD.item_order,RBDD.comp_Sign) A ");
        sql.append("                  WHERE RBD.id = A.DIS_CODE ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("       and (" + getSQLIn(Arrays.asList(paramDTO.getCorpId().split(",")), 900, "RBD.corp_id")
                    + ") ");
        }
        sql.append("                    AND RBD.STATUS in(:status) ");
        sql.append("                    ) M, ");
        sql.append("                (SELECT RBD.corp_id, ");
        sql.append("                        RBD.RAT_CODE, ");
        sql.append("                        B.DIS_CODE,");
        sql.append("                        B.SUPERPROCESSMONEY, ");
        sql.append("                        B.freezeMoney, ");
        sql.append("                        case when B.addMoney is null then 0 else B.addMoney end addMoney, ");
        sql.append("                        B.PROCESSMaxMONEY, ");
        sql.append("                        B.freezeMaxMoney, ");
        sql.append(
                "                        case when B.addMaxMoney is null then 0 else B.addMaxMoney end addMaxMoney, ");
        sql.append("                        B.virtualMaxMoney, ");
        sql.append("                        B.VIRTUALURATION, ");
        sql.append("                        B.VIRTUALRELIEF, ");
        sql.append("                        B.RELIEFANDURATIONMONEY, ");
        sql.append("                        RBD.DISTRIBUTE_WAY, ");
        sql.append("                        B.ITEM_CODE, ");
        sql.append("                        b.item_flag, ");
        sql.append("                        b.item_order, ");
        sql.append("                        b.Is_Hang_Guarantee, ");
        sql.append("						B.CREDIT_TYPE ");
        sql.append("                   FROM RAT_BKRATION_DIS RBD, ");
        sql.append("                        (SELECT RBDD.DIS_CODE, ");
        sql.append("                                RBDD.ITEM_CODE, ");
        sql.append("                                RBDD.ITEM_FLAG, ");
        sql.append("                                RBDD.item_order, ");
        sql.append("                                SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION, ");
        sql.append("                                SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF, ");
        sql.append("                                SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY, ");
        sql.append("                                SUM(RBDD.SUPER_PROCESS_MONEY) SUPERPROCESSMONEY, ");
        sql.append("                                SUM(FREEZE_MONEY) as freezeMoney, ");
        sql.append("            					SUM(RBDD.PROCESS_max_MONEY) as PROCESSMaxMONEY, ");
        sql.append("            					sum(RBDD.FREEZE_MAX_MONEY) as freezeMaxMoney, ");
        sql.append("            					sum(RBDD.virtual_Max_Money) as virtualMaxMoney, ");
        sql.append("            					SUM(radd.MONEY) as addMoney, ");
        sql.append("            					SUM(radd.max_MONEY) as addMaxMoney, ");
        sql.append("                                RBK.Is_Hang_Guarantee, ");
        sql.append("								RBDD.CREDIT_TYPE ");
        sql.append("                           FROM RAT_BKRATION_DIS RBD ");
        sql.append("                           JOIN RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("                             ON RBDD.DIS_CODE = RBD.id ");
        sql.append("                          LEFT JOIN RAT_BKRATION RBK ");
        sql.append("                             ON RBD.RAT_CODE = RBK.id ");
        sql.append("  LEFT JOIN (select RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE,SUM((-1) * RBAD.MONEY) as MONEY, ");
        sql.append("    SUM((-1) * RBAD.max_MONEY) as max_MONEY ");
        sql.append("     from RAT_BKRATION_ADD RBAA ");
        sql.append("     LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD ");
        sql.append("      ON RBAD.ADD_CODE = RBAA.id ");
        sql.append("     where (RBAD.money < 0 or RBAD.max_money < 0) ");
        sql.append("     AND RBAA.FLAG = :flag ");
        sql.append("     AND 11 <= RBAA.STATUS ");
        sql.append("     AND RBAA.STATUS < 95 ");
        sql.append("     group by RBAA.rat_code,RBAA.corp_id,RBAD.ITEM_CODE) radd ");
        sql.append("     on radd.rat_code = RBK.id and radd.CORP_ID = RBD.CORP_ID ");
        sql.append("     AND radd.ITEM_CODE = RBDD.ITEM_CODE ");
        sql.append("     where RBD.status in(:status)");// 作废和审批通过的数据均可查询
        sql.append("                          GROUP BY RBDD.DIS_CODE, ");
        sql.append("                                   RBDD.ITEM_CODE, ");
        sql.append("                                   RBDD.ITEM_Flag, ");
        sql.append("                                   RBDD.ITEM_order, ");
        sql.append("                                   RBK.Is_Hang_Guarantee, ");
        sql.append("								   RBDD.CREDIT_TYPE ");
        sql.append("                         ) B ");
        sql.append("                  WHERE RBD.id = B.DIS_CODE ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("       and (" + getSQLIn(Arrays.asList(paramDTO.getCorpId().split(",")), 900, "RBD.corp_id")
                    + ") ");
        }
        sql.append("                    AND RBD.STATUS in(:status)) N ");
        sql.append("          WHERE M.RAT_CODE = N.RAT_CODE ");
        sql.append("            AND M.corp_id = N.corp_id ");
        sql.append("            AND M.ITEM_CODE = N.ITEM_CODE and m.dis_code=n.dis_code ) E ");
        sql.append("          GROUP BY E.corp_id,E.corp_name,E.corp_code, ");
        sql.append("           E.RAT_CODE, ");
        sql.append("           E.DIS_CODE, ");
        sql.append("           E.ITEM_CODE, ");
        sql.append("           E.ITEM_FLAG, ");
        sql.append("           e.item_order, ");
        sql.append("           e.comp_sign, ");
        sql.append("		   E.CREDIT_TYPE, ");
        sql.append("           E.DISTRIBUTE_WAY,E.Is_Hang_Guarantee,E.STATUS ) rat, ");
//        sql.append("           sys_corp sc, ");
//        sql.append("           bt_input_bank_info bb, ");
//        sql.append("           bt_bank_type tt, ");
        // sql.append(" )bb, ");
        // sql.append(" bt_bank bb, ");
        sql.append("        rat_bkration rb, ");
        sql.append("        RAT_ITEM_CUSTOM rc, ");
        sql.append("        RAT_ITEM_TYPE rtype ");
//        sql.append("        BT_CURRENCY cur ");
//        sql.append("       where  rat.corpid = sc.id ");
        sql.append("       where  rat.ratCode = rb.id ");
//        sql.append("       and rb.BANK_CODE = bb.sys_bank_code ");
//        sql.append(
//                "  and bb.BANK_PREFIX = tt.BANK_PREFIX and (tt.bank_name_Prefix is null or bb.bank_name like bt_concat3('%', tt.bank_name_Prefix, '%')) ");
        if (!EmptyUtils.isEmpty(paramDTO.getBankCtlType())
                && paramDTO.getBankCtlType().equals(RationConstant.RAT_BANKTYPE_SAME)) {// 银行类别相同
            if (!EmptyUtils.isEmpty(paramDTO.getBankPrefix())) {
                sql.append(" and rb.bank_type = :bankTypeId ");
                params.put("bankTypeId", paramDTO.getBankPrefix());
            }
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getBankCode())) {
                List bankCodes = Arrays.asList(paramDTO.getBankCode().split(","));
//                sql.append(" and (" + getSQLIn(bankCodes, 900, "bb.sys_bank_code") + ")");
                sql.append(" and (" + getSQLIn(bankCodes, 900, "rb.bank_code") + ")");
            }
        }
        sql.append("       and rat.itemCode = rc.id ");
        sql.append("       and rc.RIT_CODE = rtype.id ");
        sql.append("  and rb.rat_Type='0' and rb.status = 95 ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("       and (" + getSQLIn(Arrays.asList(paramDTO.getCorpId().split(",")), 900, "rat.corpId")
                    + ") ");
        }
        if (paramDTO.getCurCode() != null && !EmptyUtils.isEmpty(paramDTO.getCurCode().trim())) {
            sql.append("       and rb.cur_CODE = :curCode ");
            params.put("curCode", paramDTO.getCurCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgBeginDate()) && !EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
            sql.append("  and rb.beginDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgBeginDate())
                    + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlBeginDate()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getgBeginDate())) {
                sql.append("  and rb.beginDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getgBeginDate())
                        + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
                sql.append("  and rb.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getlBeginDate())
                        + "','yyyy-MM-dd') ");
            }
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgEndDate()) && !EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
            sql.append("  and rb.endDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgEndDate())
                    + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlEndDate()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getgEndDate())) {
                sql.append("  and rb.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getgEndDate())
                        + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
                sql.append("  and rb.endDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getlEndDate())
                        + "','yyyy-MM-dd') ");
            }
        }
        if (EmptyUtils.isEmpty(paramDTO.getgBeginDate()) && EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
            if (EmptyUtils.isEmpty(paramDTO.getgEndDate()) && EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
                if (!EmptyUtils.isEmpty(paramDTO.getBeginDate()) && !EmptyUtils.isEmpty(paramDTO.getEndDate())) {
                    sql.append(" and (");
                    sql.append(" (rb.beginDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getBeginDate())
                            + "','yyyy-MM-dd') ");
                    sql.append("  and rb.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getEndDate())
                            + "','yyyy-MM-dd')) ");
                    sql.append(" or (rb.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getBeginDate())
                            + "','yyyy-MM-dd') ");
                    sql.append("  and rb.endDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getEndDate())
                            + "','yyyy-MM-dd')) ");
                    sql.append(" or (rb.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getBeginDate())
                            + "','yyyy-MM-dd') ");
                    sql.append("  and rb.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getEndDate())
                            + "','yyyy-MM-dd')) )");
                } else if (!EmptyUtils.isEmpty(paramDTO.getBeginDate())) {
                    sql.append(" and rb.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getBeginDate())
                            + "','yyyy-MM-dd') ");
                    sql.append("  and rb.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getBeginDate())
                            + "','yyyy-MM-dd')");
                } else if (!EmptyUtils.isEmpty(paramDTO.getEndDate())) {
                    sql.append(" and rb.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getEndDate())
                            + "','yyyy-MM-dd') ");
                    sql.append("  and rb.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getEndDate())
                            + "','yyyy-MM-dd')");
                }
            }
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatMode())) {
            sql.append("  and rb.rat_Mode = '").append(paramDTO.getRatMode()).append("' ");
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatCode())) {
            sql.append(" and rat.ratCode = :ratCode ");
            params.put("ratCode", paramDTO.getRatCode());
        }
        params.put("flag", RationConstant.RAT_ADD_FLAG_GROUP);// 集团追加
        params.put("status", paramDTO.getStatus().split(","));// 分配表的数据状态，挂接数据只查审批通过的数据，列表查询可查保函作废的数据
        sql.append("  order by rat.corpid,rat.ratCode,rat.itemOrder ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信额度及占用情况。<br>
     * <b>日期：</b>2020年11月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisBean> queryCorpRatDisList(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT RBDD.id, ");
        sql.append(" rbd.corp_Id as corpId, ");
        sql.append(" rbd.corp_name as corpName, ");
        sql.append(" rat.bank_code as bankCode, ");
        sql.append(" rat.bank_name as bankName, ");
        sql.append(" rat.bank_type as bankType, ");
        sql.append(" rat.bank_type_name as bankTypeName, ");
        sql.append(" RBD.rat_Code as ratCode, ");
        sql.append(" RBD.id as disCode, ");
        sql.append(" rat.beginDate as beginDate, ");
        sql.append(" rat.enddate as endDate, ");
        sql.append(" rc.name as itemName, ");
        sql.append(" BT_NVL(RBDD.credit_Type, '1') as creditType, ");
        sql.append(" RBD.distribute_way as disWay, ");
        sql.append(" rat.rat_mode as ratMode, ");
        sql.append(" rat.cur_code as curCode, ");
        sql.append(" rat.cur_name as curName, ");
        sql.append(" rat.is_hang_guarantee as isHangGuarantee, ");
        sql.append(" rat.status, ");
        sql.append(" rbdd.item_code as itemcode, ");
        sql.append(" rbdd.item_flag as itemflag, ");
        sql.append(" rbdd.item_order as itemorder, ");
        sql.append(" rbdd.comp_sign as compsign, ");
        sql.append(" (rbdd.money + rbdd.super_by_relief_money - rbdd.super_relief_money) as money, ");
        sql.append(" (rbdd.max_money + rbdd.by_relief_max_money -  rbdd.relief_max_money) as maxmoney ");
        sql.append(" from rat_bkration_dis rbd ");
        sql.append(" inner join rat_bkration_dis_detail rbdd on rbd.id = rbdd.dis_code ");
        sql.append(" inner join rat_bkration rat on rbd.rat_code = rat.id ");
        sql.append(" inner join rat_item_custom rc on rbdd.item_code = rc.id and rc.status = 0 ");
        sql.append(" where rat.rat_type = '0' and rbd.status in (:status) ");

        if (!EmptyUtils.isEmpty(paramDTO.getBankCtlType()) && paramDTO.getBankCtlType().equals(RationConstant.RAT_BANKTYPE_SAME)) {//银行类别相同
            sql.append(" and exists (select 1 from bt_bank_type t where rat.bank_type = t.id ");
            if (!EmptyUtils.isEmpty(paramDTO.getBankPrefix())) {
                sql.append(" and t.id in (:bankTypeId) ) ");
                params.put("bankTypeId", paramDTO.getBankPrefix().split(","));
            } else {
                sql.append(" ) ");
            }
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getBankCode())) {
                sql.append(" and rat.bank_code in (:bankCode) ");
                params.put("bankCode", paramDTO.getBankCode().split(","));
            }
        }
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(paramDTO.getCorpId().split(",")), 900, "rbd.corp_id") + ") ");
//            params.put("corpId", paramDTO.getCorpId());
        }
        if (paramDTO.getCurCode() != null && !EmptyUtils.isEmpty(paramDTO.getCurCode().trim())) {
            sql.append("  and rat.cur_code = :curCode ");
            params.put("curCode", paramDTO.getCurCode());
        }
        if (paramDTO.getBankTypeId() != null && !EmptyUtils.isEmpty(paramDTO.getBankTypeId().trim())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(paramDTO.getBankTypeId().split(",")), 900, "rat.bank_type") + ") ");
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgBeginDate()) && !EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
            sql.append("  and rat.beginDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgBeginDate()) + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlBeginDate()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getgBeginDate())) {
                sql.append("  and rat.beginDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getgBeginDate()) + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
                sql.append("  and rat.beginDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getlBeginDate()) + "','yyyy-MM-dd') ");
            }
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgEndDate()) && !EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
            sql.append("  and rat.endDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgEndDate()) + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlEndDate()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(paramDTO.getgEndDate())) {
                sql.append("  and rat.endDate >= bt_to_date('" + DateUtils.formatDate(paramDTO.getgEndDate()) + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
                sql.append("  and rat.endDate <= bt_to_date('" + DateUtils.formatDate(paramDTO.getlEndDate()) + "','yyyy-MM-dd') ");
            }
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatMode()) && !EmptyUtils.isEmpty(paramDTO.getRatMode().trim())) {
            sql.append("  and rat.rat_Mode = '").append(paramDTO.getRatMode()).append("' ");
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatCode())) {
            sql.append(" and rat.id = :ratCode ");
            params.put("ratCode", paramDTO.getRatCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getBankPrefix()) && !EmptyUtils.isEmpty(paramDTO.getBankPrefix().trim())){
            sql.append(" and rat.BANK_TYPE =:bankType");
            params.put("bankType", paramDTO.getBankPrefix());
        }
        params.put("status", paramDTO.getStatus().split(","));
        sql.append("  order by rbd.corp_id,rat.rat_Code,rbd.id,rbdd.item_order ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信业务占用明细 <br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationSysBean> queryRatUrationBySys(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rat.disCode as ratCode,rat.syscode,rat.itemcode,rat.status,sum(rat.money) as money ")
                .append(" from ")
                .append(" (select ru.corp_id as corpId,ru.rat_Code as ratCode,ru.dis_Code as disCode,ru.business_name as syscode,ru.item_code as itemCode,ru.status,ru.money as money ")
                .append(" from rat_uration ru where ru.status <> '3' ) rat, ");
        sql.append(createUrationsql(paramDTO, params));
        sql.append("  group by rat.disCode,rat.syscode,rat.itemcode,rat.status ");
        sql.append("  order by rat.disCode,rat.syscode,rat.itemcode ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationSysBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信调剂审批中业务占用明细 <br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationSysBean> queryRatReliefUration(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rat.disCode as ratCode,rat.syscode,rat.itemcode,rat.status,sum(rat.money) as money ")
                .append(" from ")
                .append(" (select ru.corp_id as corpId,ru.rat_code as ratCode,ru.relief_code as disCode,'05' as syscode,")
                .append(" ru.relief_item_code as itemCode,'1' as status,ru.relief_money as money ")
                .append(" from rat_bkration_relief ru where ru.status >= 11 and ru.status < 95 and ru.relief_item_flag = '0' ")
                .append(" union ")
                .append(" select ru2.corp_id as corpId,ru2.rat_code as ratCode,ru2.relief_code as disCode,'05' as syscode, ")
                .append(" rfd.relief_item_code as itemCode,'1' as status,rfd.relief_max_money as money ")
                .append(" from rat_bkration_relief ru2,rat_bkration_relief_detail rfd ")
                .append(" where ru2.id = rfd.ref_code and ru2.status >= 11 and ru2.status < 95 and ru2.relief_item_flag = '1' and rfd.relief_max_money > 0")
                .append(" ) rat, ");
        sql.append(createUrationsql(paramDTO, params));
        sql.append("  group by rat.disCode,rat.syscode,rat.itemcode,rat.status ");
        sql.append("  order by rat.disCode,rat.syscode,rat.itemcode ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationSysBean.class);
    }
    /**
     * 功能介绍 : 查询单位授信追减审批中业务占用明细 <br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationSysBean> queryRatAddUration(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rat.disCode as ratCode,rat.syscode,rat.itemcode,rat.status,sum(rat.money) as money ")
                .append(" from ").append(" (select ru.corp_id as corpId, ru.rat_code as ratCode,rbd1.id as disCode, ")
                .append(" '05' as syscode, ").append(" rd.item_code as itemCode, ")
                .append(" (-1) * rd.money as money, ").append(" '1' as status ").append("  from RAT_BKRATION_ADD ru ")
                .append(" left join RAT_BKRATION_ADD_detail rd on ru.id = rd.add_code ")
                .append(" inner join rat_bkration_dis rbd1 on ru.rat_code = rbd1.rat_code and ru.corp_id = rbd1.corp_id ")
                .append("  where ru.FLAG = '1' ").append("  and ru.status >=11 and ru.status < 95 ")
                .append(" and rd.money < 0 and rd.item_flag = '0' ").append(" union ")
                .append(" select ru2.corp_id as corpId, ru2.rat_code as ratCode,rbd2.id as disCode, ")
                .append(" '05' as syscode, ").append(" rd2.item_code as itemCode, ")
                .append(" (-1) * rd2.max_money as money, ").append(" '1' as status ")
                .append("  from RAT_BKRATION_ADD ru2 ")
                .append(" left join RAT_BKRATION_ADD_detail rd2 on ru2.id = rd2.add_code ")
                .append(" inner join rat_bkration_dis rbd2 on ru2.rat_code = rbd2.rat_code and ru2.corp_id = rbd2.corp_id ")
                .append("  where ru2.FLAG = '1' ").append("  and ru2.status >=11 and ru2.status < 95 ")
                .append(" and rd2.max_money < 0 and rd2.item_flag = '1' ").append(" ) rat, ");
        sql.append(createUrationsql(paramDTO, params));
        sql.append("  group by rat.disCode,rat.syscode,rat.itemcode,rat.status ");
        sql.append("  order by rat.disCode,rat.syscode,rat.itemcode ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationSysBean.class);
    }
    private String createUrationsql(UrationParamDTO paramDTO, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" rat_bkration rb, ");
        sql.append(" RAT_ITEM_CUSTOM rc ");
        sql.append(" where rat.ratCode = rb.id ");
        sql.append(" and rat.itemCode = rc.id ");
        sql.append(" and rb.rat_Type='0' ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("       and (" + getSQLIn(Arrays.asList(paramDTO.getCorpId().split(",")), 900, "rat.corpId")
                    + ") ");
        }
        if (paramDTO.getCurCode() != null && !EmptyUtils.isEmpty(paramDTO.getCurCode().trim())) {
            sql.append("       and rb.cur_CODE = :curCode ");
            params.put("curCode", paramDTO.getCurCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgBeginDate()) && !EmptyUtils.isEmpty(paramDTO.getlBeginDate())) {
            sql.append("  and rb.beginDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgBeginDate())
                    + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlBeginDate()) + "','yyyy-MM-dd') ");
        }
        if (!EmptyUtils.isEmpty(paramDTO.getgEndDate()) && !EmptyUtils.isEmpty(paramDTO.getlEndDate())) {
            sql.append("  and rb.endDate between bt_to_date('" + DateUtils.formatDate(paramDTO.getgEndDate())
                    + "','yyyy-MM-dd') ");
            sql.append("  and bt_to_date('" + DateUtils.formatDate(paramDTO.getlEndDate()) + "','yyyy-MM-dd') ");
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatCode())) {
            sql.append(" and rat.ratCode = :ratCode ");
            params.put("ratCode", paramDTO.getRatCode());
        }
        return sql.toString();
    }
    /**
     * 功能介绍 : 查询银行授信已分配数据。<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationVO> queryRationDisDtl(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
//        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,sc.name as corpName, ")
//                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,bb.BANK_NAME as bankName, ")
//                .append(" rb.cur_code as curCode,bc.cur_name as curName, ")
        sql.append("select rb.id as id,rb.rat_code as ratCode,rb.corp_id as corpId,rb.corp_name as corpName, ")
                .append(" rb.contract_no as contractNo,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ")
                .append(" rb.cur_code as curCode,rb.cur_name as curName, ")
                .append(" rb.begindate as beginDate,rb.enddate as endDate,rb.status as status,rb.money as money ")
//                .append(" from RAT_BKRATION rb, sys_corp sc,bt_bank bb,bt_currency bc ")
                .append(" from RAT_BKRATION rb ")
//                .append(" where rb.corp_id = sc.id and rb.bank_code = bb.sys_bank_code ")
//                .append(" and rb.cur_code = bc.id and rb.status <> -2 ")
                .append(" where rb.status <> -2 ")
                .append(" and exists (select 1 from rat_bkration_dis rd where rd.rat_code = rb.id ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {// 授信分配单位
            sql.append(" and rd.corp_id = :corpId ");
            params.put("corpId", paramDTO.getCorpId());
        }
        sql.append(" ) ");
        if (!EmptyUtils.isEmpty(paramDTO.getBankCode())) {// 银行类型
            sql.append(" and rb.bank_code = :bankCode ");
            params.put("bankCode", paramDTO.getBankCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getCurCode())) {// 币别
            sql.append(" and rb.cur_code = :curCode ");
            params.put("curCode", paramDTO.getCurCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getBeginDate())) {// 估计授信日期
            sql.append(" and rb.begin_date <= :beginDate ");
            sql.append(" and rb.end_date >= :beginDate ");
            params.put("beginDate", paramDTO.getBeginDate());
        }
        return listBySQLAliasToBean(sql.toString(), params, RatBkrationVO.class);
    }
    /**
     * 功能介绍 : 查询授信占用情况。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationUseBeanVO> queryCorpRationUseDtl(RatUrationQueryVO queryVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE as type,rs.name as typeName, ");
        sql.append(" ru.BUSINESS_NAME as businessName, ru.ITEM_FLAG as itemFlag,");
//        sql.append(" ru.CORP_ID as corpId, sc.name as corpName,rb.BANK_CODE as bankCode,bb.BANK_NAME as bankName, ");
        sql.append(" ru.CORP_ID as corpId, rb.CORP_NAME as corpName,rb.BANK_CODE as bankCode,rb.BANK_NAME as bankName, ");
        sql.append(
                " rb.CUR_CODE as curCode,rb.CUR_NAME as curName,ru.init_CUR_CODE as initCurCode,rb.CUR_NAME as initCurName,");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(
                    " ra.ITEM_CODE as itemCode,ri.NAME as itemName,ra.max_money as money,ru.CONVERT_PRE_MONEY as convertPreMoney, ");
        } else {
            sql.append(
                    " ru.ITEM_CODE as itemCode,rc.NAME as itemName,ru.MONEY as money,ru.CONVERT_PRE_MONEY as convertPreMoney,");
        }
        sql.append(" ru.EXCHANGE_RATE as exchangeRate, ");
        sql.append(" ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate, ");
        sql.append(" ru.BILL_ID as billId, ru.BILL_NO as billNo, ru.status as status ,ru.bill_money billMoney,");
        sql.append(" case when ru.credit_Type is null then '1' else ru.credit_Type end as creditType ");
        sql.append(" from RAT_URATION ru  ");
        sql.append(" left join RAT_BKRATION rb on rb.id = ru.RAT_CODE ");
        sql.append(" left join RAT_SYS_ITEM rs on ru.TYPE = rs.CODE ");
//        sql.append(" left join sys_corp sc on ru.CORP_ID = sc.id ");
//        sql.append(" left join BT_CURRENCY bc on ru.CUR_CODE = bc.ID ");
//        sql.append(" left join BT_CURRENCY cur on ru.INIT_CUR_CODE = cur.ID ");
//        sql.append(" left join bt_bank bb on rb.BANK_CODE = bb.SYS_BANK_CODE ");
        sql.append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID ");
        if (RationConstant.RAT_COMFLAG_1.equals(queryVO.getItemFlag()) && !EmptyUtils.isEmpty(queryVO.getItemCode())) {// 查询综合品种明细
            sql.append(" left join RAT_URATION_APPLY_DTL ra on ru.id = ra.APPLY_CODE ");
            sql.append(" left join RAT_ITEM_CUSTOM ri on ra.ITEM_CODE = ri.ID ");
        }
        sql.append(" where rb.RAT_TYPE = '0' ");
        if (EmptyUtils.isNotEmpty(queryVO.getCorpId())) {
            sql.append(" and ru.corp_id = :corpId ");
            params.put("corpId", queryVO.getCorpId());
        }
        if (EmptyUtils.isNotEmpty(queryVO.getRatCode())) {
            sql.append(" and ru.rat_code = :ratCode ");
            params.put("ratCode", queryVO.getRatCode());
        }
        if (EmptyUtils.isNotEmpty(queryVO.getCurCode())) {
            sql.append(" and ru.cur_code = :curCode ");
            params.put("curCode", queryVO.getCurCode());
        }
        if (EmptyUtils.isNotEmpty(queryVO.getBusinessName())) {
            sql.append(" and ru.BUSINESS_NAME = :businessName ");
            params.put("businessName", queryVO.getBusinessName());
        }
        if (!EmptyUtils.isEmpty(queryVO.getUseType())) {
            if (RationConstant.RAT_USE_STATUS_FREEZE.equals(queryVO.getUseType())) {
                sql.append(" and ru.STATUS = :status ");
                params.put("status", RationConstant.RAT_USE_STATUS_FREEZE);
            } else if (RationConstant.RAT_USE_STATUS_USE.equals(queryVO.getUseType())) {
                sql.append(" and ru.STATUS in (:status) ");
                params.put("status", new Integer[]{RationConstant.RAT_USE_STATUS_USE});
            }
        }
        if (EmptyUtils.isNotEmpty(queryVO.getItemFlag())) {
            sql.append(" and ru.item_flag = :itemFlag ");
            params.put("itemFlag", queryVO.getItemFlag());
            if (RationConstant.RAT_COMFLAG_0.equals(queryVO.getItemFlag())) {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and ru.item_Code = :itemCode ");
                    params.put("itemCode", queryVO.getItemCode());
                }
            } else {
                if (EmptyUtils.isNotEmpty(queryVO.getItemCode())) {
                    sql.append(" and ra.item_Code = :itemCode and ra.max_money > 0 ");
                    params.put("itemCode", queryVO.getItemCode());
                }
            }
        }
        sql.append("  order by ru.create_date desc ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationUseBeanVO.class);
    }
    /**
     * 银行授信业务品种明细查询
     *
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
    public DataTablesOutput<RatItemDisVO> queryRaturationUsedItemDetail(DataTablesInput dataTablesInput, Map<String,List<String>> businessParams, List<String> corpIds) {
        OrderString order = new OrderString("tab.item_code", "asc");
        String ratType = RationConstant.RAT_TYPE_0;
        StringBuilder sql = new StringBuilder();
        sql.append(" select ratItem.Name     sysItemName, ");
        sql.append("		ratItem.id       itemCode,");
        sql.append("        race.id          raceId, ");
        sql.append("        race.race_name        raceName, ");
        sql.append("        tab.corp_name             corpName, ");
        sql.append("        tab.bank_type_name        bankName, ");
        sql.append("        tab.begin_date        beginDate, ");
        sql.append("        tab.end_date          endDate, ");
        sql.append("        tab.init_cur_code              as initCurId,");
        sql.append("        tab.cur_name          initCurName, ");
        sql.append("        tab.money, ");
        sql.append("        tab.bill_money        billMoney, ");
        sql.append("        tab.exchange_rate     exchangeRate, ");
        sql.append("        tab.convert_pre_money convertPreMoney, ");
        sql.append("        tab.assurepercent     assurepercent, ");
        sql.append("        tab.bond_money        bondMoney, ");
        sql.append("        tab.remark            remark, ");
        sql.append("        tab.init_cur_code     initCurCode, ");
        sql.append("        tab.corp_id, ");
        sql.append("        tab.type, ");
        sql.append("        tab.Bill_Name, ");
        sql.append("        tab.Bill_id           billId, ");
        sql.append("        tab.syscode           sysCode, ");
        sql.append("        tab.rat_code          ratCode, ");
        sql.append("		bt_nvl(tab.credit_type,'1') creditType ");
        sql.append("        from (select rat.*, ");
        sql.append(" 		rb.bank_code,  ");
        sql.append(" 		rb.bank_type_name,rb.corp_name,rb.cur_name ");
        sql.append(" 		from rat_uration rat ");
        sql.append("  		left join rat_bkration rb ");
        sql.append("  		on rat.rat_code = rb.id ");
        sql.append("		where 1 = 1 ");
        sql.append("		and rat.status = 2 ");
        sql.append("		and rat.rat_type = '0' ");
        sql.append("		and rb.is_hang_guarantee = '1' ");
        List<String> enrolList = businessParams.get("enrol");
        if (!EmptyUtils.isEmpty(enrolList) && enrolList.size() > 0) {
            sql.append("		union all select rat.*, ");
            sql.append("				rb.bank_code, ");
            sql.append("				rb.bank_type_name,rb.corp_name,rb.cur_name  ");
            sql.append("				from rat_uration rat ");
            sql.append("				left join rat_bkration rb  ");
            sql.append("				on rat.rat_code = rb.id ");
//        sql.append("				left join lc_enrol lc ");
//        sql.append(" 				on rat.bill_id = lc.id ");
//        sql.append(" 			    and lc.is_hang_guarantee = '1' ");
            sql.append(" 				where 1 = 1 ");
            sql.append("				and rat.status = 2 ");
            sql.append("				and rat.rat_type = '0' ");
            sql.append("				and (rb.is_hang_guarantee = '0' or ");
            sql.append("						rb.is_hang_guarantee is null) ");
            sql.append(" and rat.syscode = 'lcs' ");
            sql.append(" and " + getSQLIn(enrolList, 900, "rat.bill_id"));
        }
        List<String> billList = businessParams.get("bill");
        if (!EmptyUtils.isEmpty(billList) && billList.size() > 0) {
            sql.append(" union all  ");
            sql.append("  select rat.*, ");
            sql.append("        rb.bank_code,rb.bank_type_name,rb.corp_name,rb.cur_name  ");
            sql.append("        from rat_uration rat ");
            sql.append(" 		left join rat_bkration rb ");
            sql.append(" 		on rat.rat_code = rb.id ");
//        sql.append(" 		left join AB_VOUCHERINFO_VICE avv ");
//        sql.append(" 		on rat.bill_id = avv.vid ");
//        sql.append(" 		and avv.bear_model = '1' ");
            sql.append("		where 1 = 1 ");
            sql.append("		and rat.status = 2 ");
            sql.append("		and rat.rat_type = '0' ");
            sql.append("		and (rb.is_hang_guarantee = '0' or ");
            sql.append("				rb.is_hang_guarantee is null) ");
            sql.append(" and rat.syscode = 'accebill' ");
            sql.append(" and " + getSQLIn(billList, 900, "rat.bill_id"));
        }
        // Include eligible cms loan occupation rows; this query does not populate loanList.
        sql.append(" union all  ");
        sql.append("select rat.*,");
        sql.append("\t\trb.bank_code,");
        sql.append("\t\trb.bank_type_name,rb.corp_name,rb.cur_name ");
        sql.append(" from rat_uration rat ");
        sql.append(" left join rat_bkration rb ");
        sql.append(" on rat.rat_code = rb.id ");
//        sql.append(" left join cms_provide_loan_info cpli ");
//        sql.append(" on rat.bill_id = cpli.id ");
//        sql.append(" and cpli.is_hang_guarantee = '1' ");
        sql.append("  where 1 = 1  ");
        sql.append(" and rat.status = 2 ");
        sql.append(" and rat.rat_type = '0' ");
        sql.append(" and (rb.is_hang_guarantee = '0' or ");
        sql.append("\t\trb.is_hang_guarantee is null) ");
        sql.append(" and rat.syscode = 'cms' ");
        sql.append(" union all ");
        sql.append(" select rat.*, rb.bank_code, rb.bank_type_name,rb.corp_name,rb.cur_name ");
        sql.append(" from rat_uration rat ");
        sql.append(" left join rat_bkration rb ");
        sql.append(" on rat.rat_code = rb.id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and rat.status = 2 ");
        sql.append("  and rat.rat_type = '0' ");
        sql.append("  and (rb.is_hang_guarantee = '0' or ");
        sql.append(" 		rb.is_hang_guarantee is null) ");
        sql.append(" and rat.syscode = 'rat')tab ");
        sql.append("   left join rat_business_race race ");
        sql.append("     on tab.bill_name = race.id ");
        sql.append("   left join rat_item_custom ratItem ");
        sql.append("     on tab.item_code = ratItem.id ");
        sql.append("   left join rat_uration_apply ru ");
        sql.append("     on tab.bill_id = ru.id ");
        sql.append(" where 1 = 1  ");
        sql.append(" and " + getSQLIn(corpIds, 900, "tab.corp_id"));
        dataTablesInput.setOrderString(order);
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), RatItemDisVO.class, "tab", false, null, null);
    }
    /**
     * 获取保证金比例
     *
     * @param ratCode
     * @param itemCode
     * @return
     */
    public BigDecimal getAssurepercentByRatCode(String ratCode, String itemCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("ratCode", ratCode);
        params.put("itemCode", itemCode);
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select bt_nvl(RBD.ASSUREPERCENT,0) as ASSUREPERCENT from RAT_BKRATION_DETAIL RBD where RBD.rat_code = :ratCode and RBD.item_code = :itemCode");
        Map<String, Object> result = getBySQL(sql.toString(), params);
        return new BigDecimal(result.get("ASSUREPERCENT").toString());
    }
    /**
     * 功能介绍 : 根据系统类型和业务编号，状态获取占用信息。
     */
    public List<RatUration> queryRatUrationByParams(RatUrationDTO urationDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select r from RatUration r where r.syscode = :sysCode and r.billId = :billId ");
        sql.append(" and r.status =:status ");
        params.put("sysCode", urationDTO.getSysCode());
        params.put("billId", urationDTO.getBillId());
        params.put("status", urationDTO.getStatus());
        sql.append(" order by r.createDate ");
        return listByHQL(sql.toString(), params);
    }
    /**
     * 查询手工正式占用可释放数据
     *
     * @param endDate 系统当前时间
     * @return 授信占用DTO集合
     */
    public List<RatUrationBeanDTO> queryRatUApplyList(Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE as type,");
        sql.append(" ru.CORP_ID as corpId, ");
        sql.append(" ru.ITEM_FLAG as itemFlag,ru.ITEM_CODE as itemCode,");
        sql.append(" ru.MONEY as money,ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate, ");
        sql.append(" ru.BILL_ID as billId, ru.BILL_NO as billNo,ru.remark as remark, ");
        sql.append(" ru.status as status ");
        sql.append(" from RAT_URATION_APPLY ra  ");
        sql.append(" left join RAT_URATION ru on ra.id = ru.BILL_id ");
        sql.append(" where ru.STATUS = 2 and ru.RAT_TYPE = '0' and ru.syscode='rat' ");
        if (endDate != null) {
            sql.append(" and ru.END_DATE <= :endDate");
            params.put("endDate", endDate);
        }
        return listBySQLAliasToBean(sql.toString(), params, RatUrationBeanDTO.class);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String ratType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from RAT_URATION where status in(1,2) and corp_id=:corpId and rat_type=:ratType");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("ratType", ratType);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    public List<String> queryRationBank() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct rb.bank_code as bankCode, rb.bank_name from rat_bkration rb where rb.status = :status and rb.bank_code is not null order by rb.bank_code ");
        params.put("status", RationConstant.STATUS_CHECK);
        List<BtBankDTO> dtoList = listBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
        if (EmptyUtils.isNotEmpty(dtoList)) {
            return dtoList.stream().map(BtBankDTO::getBankCode).collect(Collectors.toList());
        }
        return null;
    }
    public List<String> queryRationBank(String corpId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select DISTINCT t.bankCode, t.bank_name from ( ");
        sql.append(" select rb.bank_code as bankCode, rb.bank_name ");
        sql.append(" from rat_bkration rb ");
        sql.append(" where rb.STATUS = :status and rb.RAT_TYPE = '0' and rb.BANK_CODE is not null ");
        if(StringUtils.isNotBlank(corpId)){
            sql.append(" and rb.CORP_ID = :corpId ");
            params.put("corpId", corpId);
        }
        // 看不到集团授信分配给子公司的授信借款银行
        sql.append(" union all ");
        sql.append(" select rb.bank_code as bankCode, rb.bank_name ");
        sql.append(" from rat_bkration rb inner join rat_bkration_dis rbd on rb.ID = rbd.RAT_CODE ");
        // 集团授信
        sql.append(" where rb.STATUS = :status and rbd.STATUS = :status and rb.RAT_TYPE = '0' and rb.BANK_CODE is not null and rb.RAT_MODE = '0' ");
        if(StringUtils.isNotBlank(corpId)){
            sql.append(" and rbd.CORP_ID = :corpId ");
            params.put("corpId", corpId);
        }
        sql.append(" ) t ");
        sql.append(" order by t.bankCode ");

        params.put("status", RationConstant.STATUS_CHECK);
        List<BtBankDTO> dtoList = listBySQLAliasToBean(sql.toString(), params, BtBankDTO.class);
        if (EmptyUtils.isNotEmpty(dtoList)) {
            return dtoList.stream().map(BtBankDTO::getBankCode).collect(Collectors.toList());
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public List<String> queryRationBill(){
        String sql = "select billId from RatUration  where syscode=:syscode and bt_nvl_char( creditType, '1') = '0'";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("syscode", AccebillConstant.AB_CONFIG_TYPE);
        return this.createQuery(sql).setProperties(params).list();
    }
    public List<GuaContractOccupDTO> getRatGuaWayNameList(List<String> businessIds, String sysCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ru.bill_id as businessId, rb.id as id ");
        sql.append(" from RAT_URATION ru ");
        sql.append(" inner join RAT_BKRATION rb on ru.RAT_CODE = rb.ID ");
        sql.append("where 1=1 ");
        sql.append(" and ru.SYSCODE = :sysCode ");
        sql.append(" and ru.bill_id in (:businessIds) ");
        sql.append(" order by ru.bill_id ");
        Map<String, Object> params = new HashMap<>();
        params.put("businessIds", businessIds);
        params.put("sysCode", sysCode);
        return this.listBySQLAliasToBean(sql.toString(), params, GuaContractOccupDTO.class);
    }

    /**
     * 根据授信编号和授信品种类型获取授信信息
     * @param corpId 授信单位ID
     * @param contractNo 授信合同编号
     * @return 授信信息
     */
    public RatUrationDisDTO getRatUrationDisDTOByParams(String corpId, String contractNo){
        if(StrUtil.isEmpty(corpId)||StrUtil.isEmpty(contractNo)){
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select rbd.id,rbd.RAT_CODE as ratCode,rbd.CORP_ID corpId,rbd.RAT_CORP_ID,rb.BEGINDATE as beginDate,rb.ENDDATE as endDate ");
        sql.append("from RAT_BKRATION rb ");
        sql.append("inner join RAT_BKRATION_DIS rbd on rb.ID=rbd.RAT_CODE ");
        sql.append("where rbd.STATUS='95' and rbd.CORP_ID=:corpId and rb.CONTRACT_NO=:contractNo ");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("contractNo", contractNo);
        List<RatUrationDisDTO> list = this.listBySQLAliasToBean(sql.toString(), params, RatUrationDisDTO.class);
        if(CollectionUtil.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    /**
     * 根据授信编号和授信品种类型获取授信信息
     * @param disId 授信分配表ID
     * @param itemName 授信品名称
     * @return 授信信息
     */
    public List<RatUrationDisDtlDTO> getRatUrationDisDtlDTOByParams(String disId, String itemName){
        if(StrUtil.isEmpty(disId)||StrUtil.isEmpty(itemName)){
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select rbdd.ITEM_FLAG as itemFlag,rbdd.ITEM_CODE itemCode,ric.NAME as itemName,rbdd.ITEM_ORDER itemOrder,rbdd.money ");
        sql.append("from RAT_BKRATION_DIS_DETAIL rbdd ");
        sql.append("inner join RAT_ITEM_CUSTOM ric on ric.id=rbdd.ITEM_CODE ");
        sql.append("inner join RAT_ITEM_TYPE rit on rit.id=ric.RIT_CODE ");
        sql.append("where rbdd.DIS_CODE=:disId and ric.NAME=:itemName order by rbdd.ITEM_ORDER desc ");
        Map<String, Object> params = new HashMap<>();
        params.put("disId", disId);
        params.put("itemName", itemName);
        List<RatUrationDisDtlDTO> list = this.listBySQLAliasToBean(sql.toString(), params, RatUrationDisDtlDTO.class);
        return list;
    }

    /**
    * @Description 查询授信占用中业务单据为空的数据
    * @author zhous
    * @date 2024/8/20 16:03
    */
    public List<RatUration> getRationBillNoIsNullList(String syscode,String businessName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ru.bill_id as billId,ru.id as Id ");
        sql.append("from RAT_URATION ru  ");
        sql.append("where syscode=:syscode and BUSINESS_NAME =:businessName and (ru.bill_no ='' or ru.bill_no is null)  ");
        Map<String, Object> params = new HashMap<>();
        params.put("syscode", syscode);
        params.put("businessName", businessName);
        List<RatUration> list = this.listBySQLAliasToBean(sql.toString(), params, RatUration.class);
        return list;
    }

    /**
    * @Description 根据id修改业务单号
    * @author zhous
    * @date 2024/8/22 9:33
    */
    public void updateBillNoSupply(String id, String origNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update rat_uration set bill_no = :origNo ");
        sql.append(" where id = :id ");
        params.put("origNo", origNo);
        params.put("id", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
