package com.bytter.biz.report.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.report.vo.EnterpriseBankGoOnlineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhoup
 * @version 1.0
 * @Title: Dao
 * @Description: 银企上线占比表Dao
 * @date 2024-5-10 16:31:56
 */
@Repository
public class EnterpriseBankGoOnlineDao extends BaseDao<EnterpriseBankGoOnlineVo, String> {

    public DataTablesOutput getDataTablesUnion(List<String> corpIdList1,DataTablesInput dataTablesInput,String unitCmId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select sc.id as corpId,sc.name as corpName,cp.id as regionId,cp.name as regionName,sc.operationArea_ID as operateAreaId ");
        sql.append(" ,bac1.cooperationBankNum as cooperationBankNum,bac2.accSumNum as accSumNum,bac3.isOnlineEnterpriseBankNum as isOnlineEnterpriseBankNum ");
        sql.append(" ,bac3.isNotOnlineEnterpriseBankNum as isNotOnlineEnterpriseBankNum ");
        sql.append(" from sys_corp sc ");
        sql.append(" left join sys_corp cp on sc.parent_id = cp.id ");
        sql.append(" left join ( select count(*) as cooperationBankNum, bac.corpId from ( ");
        sql.append(" select bt.type_name , ba.corp_id as corpId from bt_bank_acc ba left join bt_bank_type bt on ba.bank_type_id = bt.id where bt.BANK_TYPE <> '8500' and ba.acc_type<>'04' group by bt.type_name,ba.corp_id ");
        sql.append("  ) bac group by corpId ) bac1 on bac1.corpId = sc.id ");
        sql.append(" left join ( select count(*) as accSumNum, bba.corp_id as corpId from bt_bank_acc bba left join bt_bank_type bt on bba.bank_type_id = bt.id where bt.BANK_TYPE <> '8500' and bba.acc_type<>'04' group by bba.corp_id) bac2 on bac2.corpId = sc.id ");
        sql.append(" left join ( select SUM(case when  bt.WHETHER_CONNECT_BANK  = 1 then 1 else 0 end) as isNotOnlineEnterpriseBankNum, SUM(case when bba.is_online = 1 and bt.WHETHER_CONNECT_BANK  = 1 then 1 else 0 end) as isOnlineEnterpriseBankNum, bba.corp_id as corpId from bt_bank_acc bba LEFT JOIN bt_bank_type bt on bba.bank_type_id = bt.id where bt.BANK_TYPE <> '8500' and bba.acc_type<>'04'  group by bba.corp_id) bac3 on bac3.corpId = sc.id");
        sql.append(" where 1=1");
        //如果大区不存在，则返回空
        if (EmptyUtils.isNotEmpty(unitCmId)){
            sql.append("  and cp.unit_cm= '"+unitCmId+"' ");
        }else{
            return new DataTablesOutput();
        }
        sql.append(" and sc.`operationArea_ID` is not null ");
        if (dataTablesInput.getFormdata()!=null && dataTablesInput.getFormdata().get("ins_corpId")!=null&&!("".equals(dataTablesInput.getFormdata().get("ins_corpId")))){
            String corpId = dataTablesInput.getFormdata().get("ins_corpId").toString();
            List<String> corpIdList = Arrays.asList(corpId.split(","));
            sql.append(" and"+getSQLIn(corpIdList,900,"sc.id"));
        }else{
            if (!CollectionUtils.isEmpty(corpIdList1)){
                sql.append(" and"+getSQLIn(corpIdList1,900,"sc.id"));
            }
        }
        if(CollectionUtils.isEmpty(dataTablesInput.getOrder())){
            sql.append(" order by cp.sort ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), EnterpriseBankGoOnlineVo.class, "", false);
    }

    public int getCooperationBankTotal(DataTablesInput dataTablesInput,String unitCmId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select count(*) as cooperationBankNum from ( ");
        sql.append(" select  distinct bt.id");
        sql.append(" from sys_corp sc ");
        sql.append(" left join sys_corp cp on sc.parent_id = cp.id ");
        sql.append(" left join bt_bank_acc ba on ba.corp_id = sc.id ");
        sql.append(" left join bt_bank_type bt on ba.bank_type_id = bt.id ");
        sql.append(" where 1=1 and ba.acc_type<>'04' ");
        //如果大区不存在，则返回空
        if (EmptyUtils.isNotEmpty(unitCmId)){
            sql.append("  and cp.unit_cm= '"+unitCmId+"' ");
        }else{
            return 0;
        }
        sql.append(" and sc.`operationArea_ID` is not null ");
        if (dataTablesInput.getFormdata()!=null && dataTablesInput.getFormdata().get("ins_corpId")!=null&&!("".equals(dataTablesInput.getFormdata().get("ins_corpId")))){
            String corpId = dataTablesInput.getFormdata().get("ins_corpId").toString();
            List<String> corpIdList = Arrays.asList(corpId.split(","));
            sql.append(" and"+getSQLIn(corpIdList,900,"sc.id"));
        }
        sql.append(" ) a");
        return ((Number)this.createSQLQuery(sql.toString(),map).uniqueResult()).intValue();
    }
    public int getCooperationBankGroupByRegion(DataTablesInput dataTablesInput,String unitCmId,String regionId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select count(*) as cooperationBankNum from ( ");
        sql.append(" select  distinct bt.id");
        sql.append(" from sys_corp sc ");
        sql.append(" left join sys_corp cp on sc.parent_id = cp.id ");
        sql.append(" left join bt_bank_acc ba on ba.corp_id = sc.id ");
        sql.append(" left join bt_bank_type bt on ba.bank_type_id = bt.id ");
        sql.append(" where 1=1 and ba.acc_type<>'04' ");
        if (EmptyUtils.isNotEmpty(regionId)){
            sql.append("  and cp.id= '"+regionId+"' ");
        }
        //如果大区不存在，则返回空
        if (EmptyUtils.isNotEmpty(unitCmId)){
            sql.append("  and cp.unit_cm= '"+unitCmId+"' ");
        }else{
            return 0;
        }
        sql.append(" and sc.`operationArea_ID` is not null ");
        if (dataTablesInput.getFormdata()!=null && dataTablesInput.getFormdata().get("ins_corpId")!=null&&!("".equals(dataTablesInput.getFormdata().get("ins_corpId")))){
            String corpId = dataTablesInput.getFormdata().get("ins_corpId").toString();
            List<String> corpIdList = Arrays.asList(corpId.split(","));
            sql.append(" and"+getSQLIn(corpIdList,900,"sc.id"));
        }
        sql.append(" ) a");
        return ((Number)this.createSQLQuery(sql.toString(),map).uniqueResult()).intValue();
    }
}
