package com.bytter.credit.domain.dao;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationBeanDTO;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCpUrationDao
 * @Description:内部授信结转Dao
 * @date 2018年11月28日 下午3:56:34
 */
@Repository
public class RatCpUrationDao extends BaseDao<RatUration, String> {
    /**
     * 查询授信占用可结转数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryRatURationList(DataTablesInput input, String... corpAuthSql) {
        StringBuilder sql = new StringBuilder();
        // 查询占用表
        sql.append(" select ru.id as id,ru.URATION_CODE as urationCode,ru.TYPE  as type,rs.name as typeName,")
                .append(" ru.CORP_ID as corpId,rb.corp_name  as corpName,rb.CUR_CODE  as curCode,rb.CUR_NAME as curName,")
                .append(" ru.ITEM_FLAG as itemFlag,ru.ITEM_CODE as itemCode,rc.NAME  as itemName,")
                .append(" ru.MONEY as money,ru.BEGIN_DATE as beginDate,ru.END_DATE as endDate,")
                .append(" ru.remark as remark,rb.RAT_CODE as ratCode,ru.status as status,ru.BILL_NO as billNo ")
                .append(" from RAT_URATION ru ").append(" left join RAT_BKRATION rb on rb.id = ru.RAT_CODE ")
                .append(" left join RAT_SYS_ITEM rs on ru.TYPE = rs.CODE ")
                .append(" left join RAT_ITEM_CUSTOM rc on ru.ITEM_CODE = rc.ID ")
                .append(" where ru.STATUS =2 and rb.rat_type =1");
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            if (corpAuthSql.length >= 1) {
                sql.append(" and ").append(corpAuthSql[0]);
            }
        }
        return getDynamicBySQL(input, sql.toString(), RatUrationBeanDTO.class, "", false);
    }
    /**
     * 查询单位授信额度及占用情况
     */
    public List<RatUrationDisBean> queryCorpRatUration(String corpId, List<String> ratCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(" select m.corp_id, ");
        sql.append(" m.corp_name as corpName, ");
        sql.append(" m.corp_code as corpNo, ");
        sql.append(" m.rat_code as ratCode, ");
        sql.append(" rb.rat_code as ratNo, ");
        sql.append(" rb.begindate as beginDate, ");
        sql.append(" rb.enddate as endDate, ");
        sql.append(" m.money as money, ");
        sql.append(" n.superProcessMoney as useMoney, ");
        sql.append(" n.freezeMoney as freezeMoney, ");
        sql.append(" case when n.addMoney is null then 0 else n.addMoney end as addMoney, ");
        sql.append(" N.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY ");
        sql.append(" from (select rbd.corp_id,rbd.corp_name,rbd.corp_code, rbd.rat_code, a.money ");
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
        sql.append(" (select rbd.corp_id,rbd.corp_name,rbd.corp_code,");
        sql.append(" rbd.rat_code, ");
        sql.append(" b.superProcessMoney, ");
        sql.append(" b.freezeMoney, ");
        sql.append(" b.addMoney, ");
        sql.append(" B.RELIEFANDURATIONMONEY  ");
        sql.append(" from rat_bkration_dis rbd, ");
        sql.append(" (select rbdd.dis_code, ");
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
//        sql.append(" sys_corp sc, rat_bkration rb ");
        sql.append(" rat_bkration rb ");
        sql.append(" where m.rat_code = n.rat_code ");
        sql.append(" and m.corp_id = n.corp_id ");
//        sql.append(" and m.corp_id = sc.id ");
        sql.append(" and m.rat_code = rb.id ");
        sql.append(" and ").append(getSQLIn(ratCodes, 900, "m.rat_code"));
        sql.append(" order by m.corp_id,rb.rat_code desc ");
        params.put("corpId", corpId);
        params.put("flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部额度追加
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 查询内部授信占用到期数据
     */
    @SuppressWarnings("unchecked")
    public List<RatUration> queryRatUList(Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select ru from RatUration ru where ru.status=:status ")
                .append(" and ru.endDate<=:endDate and ru.ratType=:ratType ").append(" order by ru.endDate");
        params.put("status", RationConstant.RAT_USE_STATUS_USE);
        params.put("endDate", endDate);
        params.put("ratType", RationConstant.RAT_TYPE_1);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据系统类型和业务编号获取占用信息
     */
    @SuppressWarnings("unchecked")
    public List<RatUration> queryRatUrationById(String sysCode, String billNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select r from RatUration r where r.syscode = :sysCode and r.billId = :billNo and r.ratType=:ratType ");
        sql.append(" and r.status <> :status ");
        sql.append(" order by r.createDate ");
        params.put("sysCode", sysCode);
        params.put("billNo", billNo);
        params.put("status", RationConstant.RAT_USE_STATUS_CLOSE);
        params.put("ratType", RationConstant.RAT_TYPE_1);// 企业授信
        return listByHQL(sql.toString(), params);
    }
    /**
     * 查询单位授信额度及占用情况
     */
    public List<RatUrationDisBean> queryCorpRatDisration(UrationParamDTO paramDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 名称调整
        sql.append(
                " select rat.corpId as corpId, rat.corpName as corpName, rat.corpCode as corpNo, rat.ratCode as ratCode,rat.disCode as disCode, ");
        sql.append(" rb.beginDate as beginDate,rb.endDate as endDate,rat.compSign as compSign, ");
        sql.append(" rat.itemFlag as itemFlag,rat.itemCode as itemCode,rat.itemOrder as itemOrder, ");
        sql.append(
                " rc.name as itemName,rat.money as money,rat.SUPERPROCESSMONEY as useMoney,rat.freezeMoney as freezeMoney, ");
        sql.append(
                " rat.addMoney as addMoney,rat.VIRTUALURATION as VIRTUALURATION,rat.VIRTUALRELIEF as VIRTUALRELIEF, ");
        sql.append(
                " rat.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY,rat.maxMONEY as maxMONEY,rat.virtualMaxMoney as virtualMaxMoney, ");
        sql.append(
                " rat.PROCESSMaxMONEY as useMaxMONEY, rat.freezeMaxMoney as freezeMaxMoney, rat.addMaxMoney as addMaxMoney, ");
        sql.append(
                " rat.distributeway as disWay, rb.rat_mode as ratMode, rb.cur_code as curCode, rb.cur_name as curName, ");
        sql.append(" rat.ratType  as ratType ");
        sql.append(" from ( ");
        sql.append(" SELECT E.corp_id as corpId, E.corp_name as corpName, E.corp_code as corpCode, ");
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
        sql.append("        e.rat_type   as ratType, ");
        sql.append("        e.item_order as itemOrder ");
        sql.append("   FROM (SELECT M.corp_id, M.corp_name, M.corp_code,");
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
        sql.append("                M.rat_type, ");
        sql.append("                N.DISTRIBUTE_WAY ");
        sql.append(
                "  FROM (SELECT RBD.corp_id,RBD.corp_name,RBD.corp_code,RBD.RAT_CODE,A.dis_code,A.MONEY,A.maxMONEY,A.ITEM_CODE,A.ITEM_FLAG,A.item_order,RBD.RAT_TYPE,A.comp_Sign ");
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
            sql.append("                    AND RBD.corp_id = :corpId ");
            params.put("corpId", paramDTO.getCorpId());
        }
        sql.append("                    AND RBD.STATUS = 95 ");
        sql.append("                    ) M, ");
        sql.append("                (SELECT RBD.corp_id, RBD.corp_name,RBD.corp_code,");
        sql.append("                        RBD.RAT_CODE, ");
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
        sql.append("                        b.item_order ");
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
        sql.append("            					SUM(radd.max_MONEY) as addMaxMoney ");
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
        sql.append("     where RBD.status = 95 ");
        sql.append("                          GROUP BY RBDD.DIS_CODE, ");
        sql.append("                                   RBDD.ITEM_CODE, ");
        sql.append("                                   RBDD.ITEM_Flag, ");
        sql.append("                                   RBDD.ITEM_order ");
        sql.append("                         ) B ");
        sql.append("                  WHERE RBD.id = B.DIS_CODE ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("                    AND RBD.corp_id = :corpId ");
            params.put("corpId", paramDTO.getCorpId());
        }
        sql.append("                    AND RBD.STATUS = 95) N ");
        sql.append("          WHERE M.RAT_CODE = N.RAT_CODE ");
        sql.append("            AND M.corp_id = N.corp_id ");
        sql.append("            AND M.ITEM_CODE = N.ITEM_CODE) E ");
        sql.append("          GROUP BY E.corp_id, E.corp_name, E.corp_code, ");
        sql.append("           E.RAT_CODE, ");
        sql.append("           E.DIS_CODE, ");
        sql.append("           E.ITEM_CODE, ");
        sql.append("           E.ITEM_FLAG, ");
        sql.append("           e.item_order, ");
        sql.append("           e.comp_sign, ");
        sql.append("            E.rat_type, ");
        sql.append("           E.DISTRIBUTE_WAY ) rat, ");
//        sql.append("           sys_corp sc, ");
        sql.append("        rat_bkration rb, ");
        sql.append("        RAT_ITEM_CUSTOM rc ");
//        sql.append("        BT_CURRENCY cur ");
//        sql.append("       where  rat.corpid = sc.id ");
        sql.append("       where rat.ratCode = rb.id ");
        sql.append("       and rat.itemCode = rc.id ");
//        sql.append("       and rb.cur_code = cur.id ");
        sql.append("  and ratType='1' ");
        if (!EmptyUtils.isEmpty(paramDTO.getCorpId())) {
            sql.append("       and rat.corpId = :corpId ");
            params.put("corpId", paramDTO.getCorpId());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getCurCode().trim())) {
            sql.append("       and rb.cur_CODE = :curCode ");
            params.put("curCode", paramDTO.getCurCode());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getBeginDate()) && !EmptyUtils.isEmpty(paramDTO.getEndDate())) {
            sql.append("  and ( (rb.beginDate <= :beginDate and rb.endDate >= :beginDate) ");
            sql.append("  or (rb.beginDate <= :endDate and rb.endDate >= :endDate )  ");
            sql.append("  or (rb.beginDate >= :beginDate and rb.endDate <= :endDate ) )  ");
            params.put("beginDate", paramDTO.getBeginDate());
            params.put("endDate", paramDTO.getEndDate());
        } else if (!EmptyUtils.isEmpty(paramDTO.getBeginDate())) {
            sql.append("  and rb.beginDate <= :beginDate and rb.endDate >= :beginDate ");
            params.put("beginDate", paramDTO.getBeginDate());
        } else if (!EmptyUtils.isEmpty(paramDTO.getEndDate())) {
            sql.append("  and (rb.beginDate <= :endDate and rb.endDate >= :endDate) ");
            params.put("endDate", paramDTO.getEndDate());
        }
        if (!EmptyUtils.isEmpty(paramDTO.getRatCode())) {
            sql.append(" and rat.ratCode = :ratCode ");
            params.put("ratCode", paramDTO.getRatCode());
        }
        params.put("flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信额度追加
        sql.append("  order by rat.corpId,rat.ratCode,rat.itemOrder ");
        return listBySQLAliasToBean(sql.toString(), params, RatUrationDisBean.class);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from RAT_URATION where status in(1,2) and corp_id=:corpId and rat_type=1");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
