package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.vo.RatBkrationVO;
import com.bytter.report.domain.cms.vo.RatUrationDisBean;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class BankRatStatisticsDao extends BaseDao<RatBkrationVO,String> { 

    public DataTablesOutput getDataTablesUnion(DataTablesInput input,List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        List<String> corpIds = null;
        if(input.getFormdata().get("ins_corpId") != null && !"".equals(input.getFormdata().get("ins_corpId"))){
            corpIds = StringUtils.splitToList(",", (String)input.getFormdata().get("ins_corpId"));
        }
        String curId = (String) input.getFormdata().get("eqs_curId");
        List<String> bankTypes = null;
        if(StringUtils.isNotEmpty(String.valueOf(input.getFormdata().get("ins_bankTypeId")))) {
            bankTypes = StringUtils.splitToList(",", String.valueOf(input.getFormdata().get("ins_bankTypeId")));
        }

        String ratMode = (String) input.getFormdata().get("eqs_ratMode");
        String startDate = (String) input.getFormdata().get("ged_regDate");
        
        sql.append(" select rr.corpName,rr.ratCode,sum(rr.useMoney) as useMoney,rr.money ,rr.bankType,rr.ratMode,rr.bankName,rr.curCode,rr.curName from ( ");
        
        sql.append(" select rat.corpId as corpId, sc.name as corpName,  sc.code as corpNo,");
        sql.append("  rat.ratCode as ratCode,  rat.disCode as disCode,rat.compSign as compSign,");
        sql.append("  rb.beginDate as beginDate,  rb.endDate as endDate,rat.itemFlag as itemFlag,");
        sql.append("  rat.itemCode as itemCode, rat.itemOrder  as itemOrder,bb.bank_name as bankName,");
        sql.append("  rc.name as itemName, rat.SUPERPROCESSMONEY as useMoney,");
        sql.append("  rat.freezeMoney as freezeMoney,rat.addMoney as addMoney,rat.VIRTUALURATION  as VIRTUALURATION,");
        sql.append("  rat.VIRTUALRELIEF as VIRTUALRELIEF,rat.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY,");
        sql.append("  rat.maxMONEY as maxMONEY,rat.virtualMaxMoney as virtualMaxMoney, rat.PROCESSMaxMONEY as useMaxMONEY,");
        sql.append("  rat.freezeMaxMoney as freezeMaxMoney,rat.addMaxMoney as addMaxMoney,cur.cur_name as curName, rat.sumMoney as money,");
        sql.append("  rat.distributeway as disWay, rb.rat_mode as ratMode,rb.cur_code as curCode, bt.id as bankType,bb.bank_prefix as bankCode ");
       
        sql.append("  from (SELECT E.corp_id as corpId, E.RAT_CODE as ratCode, E.DIS_CODE as disCode,");
        sql.append("  SUM(E.SUPERPROCESSMONEY) as SUPERPROCESSMONEY,SUM(E.freezeMoney) as freezeMoney,SUM(E.addMoney) as addMoney,");
        sql.append("  SUM(E.VIRTUALURATION) as VIRTUALURATION,SUM(E.VIRTUALRELIEF) as VIRTUALRELIEF,");
        sql.append("  SUM(E.RELIEFANDURATIONMONEY) as RELIEFANDURATIONMONEY, E.DISTRIBUTE_WAY as DISTRIBUTEWAY,");
        sql.append("  SUM(E.maxMONEY) as maxmoney,SUM(E.PROCESSMaxMONEY) as PROCESSMaxMONEY,SUM(E.freezeMaxMoney) as freezeMaxMoney,");
        sql.append("  SUM(E.addMaxMoney) as addMaxMoney, SUM(E.virtualMaxMoney) as virtualMaxMoney, E.ITEM_CODE as itemCode,");
        sql.append("  e.item_flag as itemFlag, e.comp_Sign as compSign,e.item_order as itemOrder,E.sumMoney ");
        
        sql.append("  FROM (SELECT M.corp_id, M.RAT_CODE, M.DIS_CODE, M.MONEY, N.SUPERPROCESSMONEY, N.freezeMoney, N.addMoney,");
        sql.append("  M.maxMONEY,N.PROCESSMaxMONEY, N.freezeMaxMoney,N.addMaxMoney, N.virtualMaxMoney, N.VIRTUALURATION, N.sumMoney,");
        sql.append("  N.VIRTUALRELIEF, N.RELIEFANDURATIONMONEY,N.ITEM_CODE,N.item_flag, N.item_order, M.comp_Sign, N.DISTRIBUTE_WAY ");
       
        sql.append("  FROM (SELECT RBD.corp_id, RBD.RAT_CODE,A.dis_code, A.MONEY,A.maxMONEY, A.ITEM_CODE, A.ITEM_FLAG, ");
        sql.append("  A.item_order,A.comp_Sign ");
        
        sql.append("  FROM RAT_BKRATION_DIS RBD,(SELECT RBDD.DIS_CODE,RBDD.ITEM_CODE,RBDD.ITEM_FLAG,RBDD.item_order,RBDD.comp_Sign,");
        sql.append("  SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - RBDD.SUPER_RELIEF_MONEY) MONEY,");
        sql.append("  SUM(RBDD.max_MONEY + RBDD.BY_RELIEF_max_MONEY - RBDD.RELIEF_max_MONEY) as maxMONEY ");
        
        sql.append("  FROM RAT_BKRATION_DIS RBD, RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("  WHERE RBDD.DIS_CODE = RBD.id");
        sql.append(" GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE,RBDD.ITEM_FLAG,RBDD.item_order, RBDD.comp_Sign) A ");
        sql.append("  WHERE RBD.id = A.DIS_CODE  AND RBD.STATUS = 95) M, ");
        sql.append(" (SELECT RBD.corp_id, RBD.RAT_CODE,B.SUPERPROCESSMONEY, B.freezeMoney,");
        sql.append("  case when B.addMoney is null then 0 else B.addMoney end addMoney,  B.PROCESSMaxMONEY, B.freezeMaxMoney,");
        sql.append("  case when B.addMaxMoney is null then 0 else  B.addMaxMoney  end addMaxMoney, B.virtualMaxMoney,");
        sql.append("  B.VIRTUALURATION, B.VIRTUALRELIEF,B.RELIEFANDURATIONMONEY,RBD.DISTRIBUTE_WAY,B.ITEM_CODE,");
        sql.append("  b.item_flag, b.item_order, B.sumMoney");
       
        sql.append(" FROM RAT_BKRATION_DIS RBD,(SELECT RBDD.DIS_CODE,RBDD.ITEM_CODE, RBDD.ITEM_FLAG,RBDD.item_order,");
        sql.append("  SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION,SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF,");
        sql.append(" SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY,SUM(RBDD.SUPER_PROCESS_MONEY) SUPERPROCESSMONEY,");
        sql.append(" SUM(FREEZE_MONEY) as freezeMoney,SUM(RBDD.PROCESS_max_MONEY) as PROCESSMaxMONEY,sum(RBDD.FREEZE_MAX_MONEY) as freezeMaxMoney,");
        sql.append(" sum(RBDD.virtual_Max_Money) as virtualMaxMoney, SUM(radd.MONEY) as addMoney,SUM(radd.max_MONEY) as addMaxMoney, ");
        sql.append(" rbd.money as sumMoney ");
        
        sql.append(" FROM RAT_BKRATION_DIS RBD  JOIN RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("  ON RBDD.DIS_CODE = RBD.id LEFT JOIN RAT_BKRATION RBK  ON RBD.RAT_CODE = RBK.id ");
        sql.append(" LEFT JOIN (select RBAA.rat_code, RBAA.corp_id, RBAD.ITEM_CODE,SUM((-1) * RBAD.MONEY) as MONEY,");
        sql.append(" SUM((-1) * RBAD.max_MONEY) as max_MONEY ");
        
        sql.append(" from RAT_BKRATION_ADD RBAA  LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD  ON RBAD.ADD_CODE = RBAA.id ");
        sql.append(" where (RBAD.money < 0 or  RBAD.max_money < 0) ");
        sql.append(" AND RBAA.FLAG = '1' AND 11 <= RBAA.STATUS AND RBAA.STATUS < 95  ");
        sql.append(" group by RBAA.rat_code, RBAA.corp_id,RBAD.ITEM_CODE) radd");
        sql.append(" on radd.rat_code = RBK.id  and radd.CORP_ID = RBD.CORP_ID ");
        sql.append(" AND radd.ITEM_CODE = RBDD.ITEM_CODE where RBD.status = 95 ");
        sql.append(" GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE, RBDD.ITEM_Flag, RBDD.ITEM_order, rbd.money  ) B ");
        sql.append(" WHERE RBD.id = B.DIS_CODE AND RBD.STATUS = 95) N ");
        sql.append(" WHERE M.RAT_CODE = N.RAT_CODE  AND M.corp_id = N.corp_id AND M.ITEM_CODE = N.ITEM_CODE) E ");
        sql.append(" GROUP BY E.corp_id, E.RAT_CODE,E.DIS_CODE,E.ITEM_CODE,E.ITEM_FLAG,e.item_order,e.comp_sign,");
        sql.append(" E.DISTRIBUTE_WAY ,E.sumMoney ) rat, ");
        sql.append(" sys_corp sc,bt_bank bb,bt_bank_type bt,rat_bkration rb, RAT_ITEM_CUSTOM rc, BT_CURRENCY cur ");
        sql.append(" where rat.corpid = sc.id ");
        sql.append(" and rat.ratCode = rb.id");
        sql.append(" and rb.BANK_CODE = bb.sys_bank_code");
        sql.append(" and bb.bank_type_id = bt.id ");
        sql.append(" and rat.itemCode = rc.id ");
        sql.append(" and rb.cur_code = cur.id");
        
        if(StringUtils.isNotEmpty(curId)) {
            sql.append(" and rb.cur_CODE = :curId ");
            params.put("curId",curId.trim());
        }
        if(!EmptyUtils.isBlank(startDate)) {
            sql.append(" and ((rb.beginDate <= to_date(:startDate,'yyyy-MM-dd') and rb.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rb.beginDate <=  to_date(:startDate,'yyyy-MM-dd') and rb.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rb.beginDate >=  to_date(:startDate,'yyyy-MM-dd') and rb.endDate <=  to_date(:startDate,'yyyy-MM-dd'))) ");
            params.put("startDate",startDate.trim());
        }
        if(EmptyUtils.isNotEmpty(bankTypes) && !"null".equals(bankTypes.get(0))) {
            sql.append(" and ").append(getSQLIn(bankTypes, 900, "bt.id"));
        }
        if(StringUtils.isNotEmpty(ratMode)){
            sql.append(" and rb.rat_mode = :ratMode ");
            params.put("ratMode",ratMode.trim());
        }
        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(getSQLIn(corpIds, 900, "rat.corpId"));
        }else{
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "rat.corpId"));
        }
        //sql.append(" and  rat.SUPERPROCESSMONEY <> 0 ");
        sql.append(" order by rat.corpid, rat.ratCode, rat.itemOrder");
        sql.append(" )rr  group by rr.corpName,rr.ratCode,rr.money ,rr.bankType,rr.ratMode,rr.bankName,rr.curCode,rr.curName ");
        
        List<RatUrationDisBean> list = listBySQLAliasToBean(sql.toString(),params,RatUrationDisBean.class);
        DataTablesOutput dto = new DataTablesOutput();
        dto.setData(list);
        return dto;
        
    }

    public List<RatUrationDisBean> getBankBal(RatBkrationVO vo,List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" select sum(rt.money) as money,sum(rt.useMoney) as useMoney,rt.bankCode,rt.bankType,rt.disCode from( ");
        sql.append(" select max(r.sumMoney) as money,sum(r.useMoney) as useMoney,r.bankCode,r.bankType,r.ratCode,r.disWay,r.disCode from( ");
        sql.append(" select rat.corpId as corpId, sc.name as corpName,  sc.code as corpNo,");
        sql.append("  rat.ratCode as ratCode,  rat.disCode as disCode,rat.compSign as compSign,");
        sql.append("  rb.beginDate as beginDate,  rb.endDate as endDate,rat.itemFlag as itemFlag,");
        sql.append("  rat.itemCode as itemCode, rat.itemOrder  as itemOrder,bb.bank_name as bankName,");
        sql.append("  rc.name as itemName,rat.sumMoney, rat.SUPERPROCESSMONEY as useMoney,");
        sql.append("  rat.freezeMoney as freezeMoney,rat.addMoney as addMoney,rat.VIRTUALURATION  as VIRTUALURATION,");
        sql.append("  rat.VIRTUALRELIEF as VIRTUALRELIEF,rat.RELIEFANDURATIONMONEY as RELIEFANDURATIONMONEY,");
        sql.append("  rat.maxMONEY as maxMONEY,rat.virtualMaxMoney as virtualMaxMoney, rat.PROCESSMaxMONEY as useMaxMONEY,");
        sql.append("  rat.freezeMaxMoney as freezeMaxMoney,rat.addMaxMoney as addMaxMoney,cur.cur_name as curName,");
        sql.append("  rat.distributeway as disWay, rb.rat_mode as ratMode,rb.cur_code as curCode, bt.type_name as bankType, bb.bank_prefix as bankCode ");

        sql.append("  from (SELECT E.corp_id as corpId, E.RAT_CODE as ratCode, E.DIS_CODE as disCode, ");
        sql.append("  SUM(E.SUPERPROCESSMONEY) as SUPERPROCESSMONEY,SUM(E.freezeMoney) as freezeMoney,SUM(E.addMoney) as addMoney,");
        sql.append("  SUM(E.VIRTUALURATION) as VIRTUALURATION,SUM(E.VIRTUALRELIEF) as VIRTUALRELIEF,");
        sql.append("  SUM(E.RELIEFANDURATIONMONEY) as RELIEFANDURATIONMONEY, E.DISTRIBUTE_WAY as DISTRIBUTEWAY,");
        sql.append("  SUM(E.maxMONEY) as maxmoney,SUM(E.PROCESSMaxMONEY) as PROCESSMaxMONEY,SUM(E.freezeMaxMoney) as freezeMaxMoney,");
        sql.append("  SUM(E.addMaxMoney) as addMaxMoney, SUM(E.virtualMaxMoney) as virtualMaxMoney, E.ITEM_CODE as itemCode,");
        sql.append("  e.item_flag as itemFlag, e.comp_Sign as compSign,e.item_order as itemOrder, E.sumMoney ");

        sql.append("  FROM (SELECT M.corp_id, M.RAT_CODE, M.DIS_CODE, M.MONEY, N.SUPERPROCESSMONEY, N.freezeMoney, N.addMoney,");
        sql.append("  M.maxMONEY,N.PROCESSMaxMONEY, N.freezeMaxMoney,N.addMaxMoney, N.virtualMaxMoney, N.VIRTUALURATION,");
        sql.append("  N.VIRTUALRELIEF, N.RELIEFANDURATIONMONEY,N.ITEM_CODE,N.item_flag, N.item_order, M.comp_Sign, N.DISTRIBUTE_WAY, ");
        sql.append("  N.sumMoney ");

        sql.append("  FROM (SELECT RBD.corp_id, RBD.RAT_CODE,A.dis_code, A.MONEY,A.maxMONEY, A.ITEM_CODE, A.ITEM_FLAG, ");
        sql.append("  A.item_order,A.comp_Sign ");

        sql.append("  FROM RAT_BKRATION_DIS RBD,(SELECT RBDD.DIS_CODE,RBDD.ITEM_CODE,RBDD.ITEM_FLAG,RBDD.item_order,RBDD.comp_Sign,");
        sql.append("  SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - RBDD.SUPER_RELIEF_MONEY) MONEY,");
        sql.append("  SUM(RBDD.max_MONEY + RBDD.BY_RELIEF_max_MONEY - RBDD.RELIEF_max_MONEY) as maxMONEY ");

        sql.append("  FROM RAT_BKRATION_DIS RBD, RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("  WHERE RBDD.DIS_CODE = RBD.id");
        sql.append(" GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE,RBDD.ITEM_FLAG,RBDD.item_order, RBDD.comp_Sign) A ");
        sql.append("  WHERE RBD.id = A.DIS_CODE  AND RBD.STATUS = 95) M, ");
        sql.append(" (SELECT RBD.corp_id, RBD.RAT_CODE,B.SUPERPROCESSMONEY, B.freezeMoney,");
        sql.append("  case when B.addMoney is null then 0 else B.addMoney end addMoney,  B.PROCESSMaxMONEY, B.freezeMaxMoney,");
        sql.append("  case when B.addMaxMoney is null then 0 else  B.addMaxMoney  end addMaxMoney, B.virtualMaxMoney,");
        sql.append("  B.VIRTUALURATION, B.VIRTUALRELIEF,B.RELIEFANDURATIONMONEY,RBD.DISTRIBUTE_WAY,B.ITEM_CODE,");
        sql.append("  b.item_flag, b.item_order, B.sumMoney ");

        sql.append(" FROM RAT_BKRATION_DIS RBD,(SELECT RBDD.DIS_CODE,RBDD.ITEM_CODE, RBDD.ITEM_FLAG,RBDD.item_order,");
        sql.append("  SUM(RBDD.VIRTUAL_URATION) AS VIRTUALURATION,SUM(RBDD.VIRTUAL_RELIEF_MONEY) AS VIRTUALRELIEF,");
        sql.append(" SUM(RBDD.RELIEF_AND_URATION_MONEY) AS RELIEFANDURATIONMONEY,SUM(RBDD.SUPER_PROCESS_MONEY) SUPERPROCESSMONEY,");
        sql.append(" SUM(FREEZE_MONEY) as freezeMoney,SUM(RBDD.PROCESS_max_MONEY) as PROCESSMaxMONEY,sum(RBDD.FREEZE_MAX_MONEY) as freezeMaxMoney,");
        sql.append(" sum(RBDD.virtual_Max_Money) as virtualMaxMoney, SUM(radd.MONEY) as addMoney,SUM(radd.max_MONEY) as addMaxMoney , rbd.money as sumMoney ");

        sql.append(" FROM RAT_BKRATION_DIS RBD  JOIN RAT_BKRATION_DIS_DETAIL RBDD ");
        sql.append("  ON RBDD.DIS_CODE = RBD.id LEFT JOIN RAT_BKRATION RBK  ON RBD.RAT_CODE = RBK.id ");
        sql.append(" LEFT JOIN (select RBAA.rat_code, RBAA.corp_id, RBAD.ITEM_CODE,SUM((-1) * RBAD.MONEY) as MONEY,");
        sql.append(" SUM((-1) * RBAD.max_MONEY) as max_MONEY ");

        sql.append(" from RAT_BKRATION_ADD RBAA  LEFT JOIN RAT_BKRATION_ADD_DETAIL RBAD  ON RBAD.ADD_CODE = RBAA.id ");
        sql.append(" where (RBAD.money < 0 or  RBAD.max_money < 0) ");
        sql.append(" AND RBAA.FLAG = '1' AND 11 <= RBAA.STATUS AND RBAA.STATUS < 95  ");
        sql.append(" group by RBAA.rat_code, RBAA.corp_id,RBAD.ITEM_CODE) radd");
        sql.append(" on radd.rat_code = RBK.id  and radd.CORP_ID = RBD.CORP_ID ");
        sql.append(" AND radd.ITEM_CODE = RBDD.ITEM_CODE where RBD.status = 95 ");
        sql.append(" GROUP BY RBDD.DIS_CODE, RBDD.ITEM_CODE, RBDD.ITEM_Flag, RBDD.ITEM_order, rbd.money) B ");
        sql.append(" WHERE RBD.id = B.DIS_CODE AND RBD.STATUS = 95) N ");
        sql.append(" WHERE M.RAT_CODE = N.RAT_CODE  AND M.corp_id = N.corp_id AND M.ITEM_CODE = N.ITEM_CODE) E ");
        sql.append(" GROUP BY E.corp_id, E.RAT_CODE,E.DIS_CODE,E.ITEM_CODE,E.ITEM_FLAG,e.item_order,e.comp_sign,");
        sql.append(" E.DISTRIBUTE_WAY, E.sumMoney ) rat,  ");
        sql.append(" sys_corp sc,bt_bank bb,bt_bank_type bt,rat_bkration rb, RAT_ITEM_CUSTOM rc, BT_CURRENCY cur ");
        sql.append(" where rat.corpid = sc.id ");
        sql.append(" and rat.ratCode = rb.id");
        sql.append(" and rb.BANK_CODE = bb.sys_bank_code");
        sql.append(" and bb.bank_prefix = bt.bank_prefix");
        sql.append(" and rat.itemCode = rc.id ");
        sql.append(" and rb.cur_code = cur.id");

        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(vo.getCorpId())) {
            corpIds = StringUtils.splitToList(",", vo.getCorpId());
        }
        List<String> bankprefix = null;
        if(StringUtils.isNotEmpty(String.valueOf(vo.getBankCode()))) {
            bankprefix = StringUtils.splitToList(",", String.valueOf(vo.getBankCode()));
        }

        if(StringUtils.isNotEmpty(vo.getCurCode())) {
            sql.append(" and rb.cur_CODE = :curId ");
            params.put("curId",vo.getCurCode().trim());
        }
        if(vo.getBeginDate() != null) {
            sql.append(" and ((rb.beginDate <= to_date(:startDate,'yyyy-MM-dd') and rb.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rb.beginDate <=  to_date(:startDate,'yyyy-MM-dd') and rb.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rb.beginDate >=  to_date(:startDate,'yyyy-MM-dd') and rb.endDate <=  to_date(:startDate,'yyyy-MM-dd'))) ");
            params.put("startDate", DateUtils.formatDate(vo.getBeginDate(),"yyyy-MM-dd"));
        }
        if(EmptyUtils.isNotEmpty(vo.getBankCode()) && !"".equals(vo.getBankCode())) {
            sql.append(" and ").append(getSQLIn(bankprefix, 900, "bb.bank_prefix"));
        }
        if(StringUtils.isNotEmpty(vo.getRatMode()) && !" ".equals(vo.getRatMode()) ){
            sql.append(" and rb.rat_mode = :ratMode ");
            params.put("ratMode",vo.getRatMode().trim());
        }

        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(getSQLIn(corpIds, 900, "rat.corpId"));
        }else{
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "rat.corpId"));
        }
        //sql.append(" and  rat.SUPERPROCESSMONEY <> 0 ");
        sql.append(" order by rat.corpid, rat.ratCode, rat.itemOrder");
        sql.append("  )r  group by r.bankCode,r.bankType,r.ratCode,r.disWay,r.disCode ");
        sql.append(" ) rt group by rt.bankCode,rt.bankType,rt.disCode ");

        return this.listBySQLAliasToBean(sql.toString(),params,RatUrationDisBean.class);
    }

    public DataTablesOutput getDataTables(DataTablesInput input,List<String> corpAuth) {
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        List<String> corpIds = null;
        if(input.getFormdata().get("ins_corpId") != null && !"".equals(input.getFormdata().get("ins_corpId"))){
            corpIds = StringUtils.splitToList(",", (String)input.getFormdata().get("ins_corpId"));
        }
        List<String> bankTypes = null;
        if(StringUtils.isNotEmpty(String.valueOf(input.getFormdata().get("ins_bankTypeId")))) {
            bankTypes = StringUtils.splitToList(",", String.valueOf(input.getFormdata().get("ins_bankTypeId")));
        }
        String curId = (String) input.getFormdata().get("eqs_curId");
        String ratMode = (String) input.getFormdata().get("eqs_ratMode");
        String startDate = (String) input.getFormdata().get("ged_regDate");
        this.commonSql(sql, true);
        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(getSQLIn(corpIds, 900, "rat.corp_Id"));
        }else{
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "rat.corp_Id"));
        }
        if(EmptyUtils.isNotEmpty(bankTypes) && !"null".equals(bankTypes.get(0))) {
            sql.append(" and ").append(getSQLIn(bankTypes, 900, "bt.id"));
        }
        if(StringUtils.isNotEmpty(ratMode)){
            sql.append(" and rbk.rat_mode = :ratMode ");
            params.put("ratMode",ratMode.trim());
        }

        if(StringUtils.isNotEmpty(curId)) {
            sql.append(" and rbk.cur_CODE = :curId ");
            params.put("curId",curId.trim());
        }
        if(!EmptyUtils.isBlank(startDate)) {
            sql.append(" and ((rbk.beginDate <= to_date(:startDate,'yyyy-MM-dd') and rbk.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rbk.beginDate >=  to_date(:startDate,'yyyy-MM-dd') and rbk.endDate <=  to_date(:startDate,'yyyy-MM-dd'))) ");
            params.put("startDate",startDate.trim());
        }
        sql.append(" group by rat.corp_id,rbk.bank_code,bb.bank_name,rbk.rat_mode, rbk.cur_code,bt.id, bt.type_name, rbk.corp_name, rbk.cur_name ");
//        input.setFormdata(null);
        return getDynamicBySQL(input, sql.toString(), RatUrationDisBean.class, "", false,params);

    }

    public List<RatUrationDisBean> getCartogramData(RatBkrationVO vo,List<String> corpAuth) {
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(vo.getCorpId())) {
            corpIds = StringUtils.splitToList(",", vo.getCorpId());
        }
        List<String> bankprefix = null;
        if(StringUtils.isNotEmpty(String.valueOf(vo.getBankCode()))) {
            bankprefix = StringUtils.splitToList(",", String.valueOf(vo.getBankCode()));
        }
        sql.append("  select sum(rt.money) as money,sum(rt.useMoney) as useMoney,rt.bankType,rt.bankTypeName from ( ");
        this.commonSql(sql, false);

        if(StringUtils.isNotEmpty(vo.getCurCode())) {
            sql.append(" and rbk.cur_CODE = :curId ");
            params.put("curId",vo.getCurCode().trim());
        }
        if(vo.getBeginDate() != null) {
            sql.append(" and ((rbk.beginDate <= to_date(:startDate,'yyyy-MM-dd') and rbk.endDate >= to_date(:startDate,'yyyy-MM-dd')) ");
            sql.append(" or (rbk.beginDate >=  to_date(:startDate,'yyyy-MM-dd') and rbk.endDate <=  to_date(:startDate,'yyyy-MM-dd'))) ");
            params.put("startDate", DateUtils.formatDate(vo.getBeginDate(),"yyyy-MM-dd"));
        }
        if(EmptyUtils.isNotEmpty(vo.getBankCode()) && !"".equals(vo.getBankCode())) {
            sql.append(" and ").append(getSQLIn(bankprefix, 900, "bb.bank_prefix"));
        }
        if(StringUtils.isNotEmpty(vo.getRatMode()) && !" ".equals(vo.getRatMode()) ){
            sql.append(" and rbk.rat_mode = :ratMode ");
            params.put("ratMode",vo.getRatMode().trim());
        }

        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(getSQLIn(corpIds, 900, "rat.corp_Id"));
        }else{
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "rat.corp_Id"));
        }
        sql.append(" group by rat.corp_id,rbk.bank_code,bb.bank_name,rbk.rat_mode, rbk.cur_code,bt.id, bt.type_name ");
        sql.append(" ) rt group by rt.bankType, rt.bankTypeName ");
        return listBySQLAliasToBean(sql.toString(),params,RatUrationDisBean.class);
    }

    public StringBuilder commonSql(StringBuilder  sql, boolean groupBy){
        sql.append(" select ");
        if (groupBy) {
            sql.append(" rat.corp_id   as corpId,rbk.corp_name as corpName,rbk.bank_code as bankCode, bb.bank_name as bankName,rbk.rat_mode as ratMode,")
                    .append(" rbk.cur_code  as curCode,rbk.cur_name as curName,");
        }
        sql.append(" bt.id as bankType,bt.type_name as bankTypeName, ")
                .append(" sum(rat.sumMoney)  as Money, ")
                .append(" sum(rat.useMoney) useMoney ")
                .append(" from (select RBD.Corp_Id, RBD.CORP_NAME, RBD.rat_code, M.sumMoney,  BT_NVL_NUM(n.useMoney,0) useMoney ")
                .append(" from RAT_BKRATION_DIS RBD ")
                .append(" join (select RBDD.Dis_Code, ")
                .append(" SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - RBDD.SUPER_RELIEF_MONEY) sumMoney ")
                .append(" FROM RAT_BKRATION_DIS_DETAIL RBDD ")
                .append(" group by RBDD.Dis_Code) M ")
                .append(" on RBD.id = M.dis_code ")
                .append(" left join (select urat.dis_code, sum(urat.money) useMoney ")
                .append(" from rat_uration urat ")
                .append(" WHERE urat.status = 2 ")
                .append(" group by urat.dis_code) N ")
                .append(" on RBD.id = N.dis_code ")
                .append(" WHERE RBD.Status = 95) rat, ")
                .append(" RAT_BKRATION RBK, ")
                .append(" bt_bank bb, ")
                .append(" bt_bank_type bt ")
                .append(" where RBK.Status = 95 ")
                .append(" and rat.rat_code = rbk.id ")
                .append(" and rbk.BANK_CODE = bb.sys_bank_code ")
                .append(" and (bb.bank_type_id = bt.id or bb.bank_prefix = bt.bank_prefix) ");
        sql.append(" and (bt.bank_name_Prefix is null or bb.bank_name like bt_concat3('%', bt.bank_name_Prefix, '%')) ");
        return sql;
    }
}
