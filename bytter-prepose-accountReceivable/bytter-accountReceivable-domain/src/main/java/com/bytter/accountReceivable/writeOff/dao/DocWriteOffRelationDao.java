package com.bytter.accountReceivable.writeOff.dao;

import com.bytter.accountReceivable.writeOff.entity.DocWriteOffRelation;
import com.bytter.accountReceivable.writeOff.vo.DocWriteOffRelationVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应收单据核销关系表 dao
 */
@Repository
public class DocWriteOffRelationDao extends BaseDao<DocWriteOffRelation, String> implements Serializable {

    /**
     * 根据应收单据id获取中间关联表的id
     *
     * @param docId
     * @param batchNo
     * @return
     */
    public List<DocWriteOffRelation> getDocWriteOffRelation(String docId, String batchNo, List<String> writeOffStatusList) {
        HashMap<String, Object> params = new HashMap<>();
        String sql = " select  * from DOC_WIRTE_OFF_RELATION where 1=1 ";
        if(StringUtils.isNotEmpty(docId)){
            sql += " and doc_id =:docId ";
            params.put("docId", docId);
        }
        if(StringUtils.isNotEmpty(batchNo)){
            sql += " and batch_no =:batchNo ";
            params.put("batchNo", batchNo);
        }
        if(EmptyUtils.isNotEmpty(writeOffStatusList)){
            sql += " and " + SqlUtil.getInSql(writeOffStatusList, "write_off_status", true);
        }

        return listBySQLAliasToBean(sql,params,DocWriteOffRelation.class);
    }

    /**
     * 获取核销历史数据
     * @param docId
     * @return
     */
    public List<DocWriteOffRelation> getWriteOffHisByDocId(String docId, String batchNo){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ");
        sql.append(" rel.BATCH_NO as batchNo, ");
        sql.append(" max(rel.WRITE_OFF_TYPE) as writeOffType, ");
        sql.append(" max(rel.PAYMENT_MEANS) as paymentMeans, ");
        sql.append(" max(rel.CREATE_DATE) as createDate, ");
        sql.append(" sum(rel.RELATION_ORIGIN_MONEY) as relationOriginMoney, ");
        sql.append(" max(rel.EXCHANGE_RATE) as exchangeRate, ");
        sql.append(" sum(rel.RELATION_CONVERT_MONEY) as relationConvertMoney ");
        sql.append(" from DOC_WIRTE_OFF_RELATION rel ");
        sql.append(" where 1 = 1 ");
        if(StringUtils.isNotEmpty(docId)){
            sql.append(" and rel.DOC_ID =:docId ");
            params.put("docId", docId);
        }
        if(StringUtils.isNotEmpty(batchNo)){
            sql.append(" and rel.batch_no =:batchNo ");
            params.put("batchNo", batchNo);
        }
        sql.append(" group by rel.BATCH_NO ");
        return listBySQLAliasToBean(sql.toString(), params, DocWriteOffRelation.class);
    }

    /**
     * 获取核销历史数据
     * @param docId
     * @return
     */
    public List<DocWriteOffRelation> getWriteOffHisByDocId1(String docId, String batchNo){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ");
        sql.append(" rel.BATCH_NO as batchNo, ");
        sql.append(" max(rel.WRITE_OFF_TYPE) as writeOffType, ");
        sql.append(" max(rel.PAYMENT_MEANS) as paymentMeans, ");
        sql.append(" max(rel.CREATE_DATE) as createDate, ");
        sql.append(" sum(rel.RELATION_ORIGIN_MONEY) as relationOriginMoney, ");
        sql.append(" max(rel.EXCHANGE_RATE) as exchangeRate, ");
        sql.append(" sum(rel.RELATION_CONVERT_MONEY) as relationConvertMoney ");
        sql.append(" from DOC_WIRTE_OFF_RELATION rel ");
        sql.append(" where 1 = 1 ");
        sql.append(" and rel.status not in (-3,-1) ");
        if(StringUtils.isNotEmpty(docId)){
            sql.append(" and rel.DOC_ID =:docId ");
            params.put("docId", docId);
        }
        if(StringUtils.isNotEmpty(batchNo)){
            sql.append(" and rel.batch_no =:batchNo ");
            params.put("batchNo", batchNo);
        }
        sql.append(" group by rel.BATCH_NO ");
        return listBySQLAliasToBean(sql.toString(), params, DocWriteOffRelation.class);
    }
    //获取 DOC_WIRTE_OFF_RELATION 数据

    /**
     * 获取核销批次数据数据
     * @param input
     * @return
     */
    public DataTablesOutput getWriteOffBatchData(DataTablesInput input, String businessSql){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select rel.*, dm.code, dm.corp_id as corpId, dm.source, dm.voucher_type  as voucherType,dm.receivable_money  as receivableMoney, dm.cur_id  as curId,  dm.external_name  as externalName,\n" +
                " dm.contract_name  as contractName,  dm.write_off_status as writeOffStatus  , dm.BATCH_BUS_TYPE as batchBusType,  case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end as writeOffBal ,dm.write_off_type as writeOffType,  nbt.BUS_TYPE_NAME as batchBusTypeName, ");
        sql.append(" sc.NAME as corpName,bc.cur_name as curName ");
        sql.append(" from (select  t.BATCH_NO as BATCH_NO, exchange_rate as exchangeRate,");
        sql.append(" group_concat(distinct t.doc_id) as docId, ");
        sql.append(" group_concat(distinct t.relation_id) as relationId, ");
        sql.append(" max(t.WRITE_OFF_TYPE) as WRITE_OFF_TYPE, ");
        sql.append(" max(t.PAYMENT_MEANS) as PAYMENT_MEANS, ");
        sql.append(" max(t.CREATE_DATE) as CREATE_DATE, ");
        sql.append(" sum(t.RELATION_ORIGIN_MONEY) as RELATION_ORIGIN_MONEY, ");
        sql.append(" sum(t.EXECUTE_MONEY) as EXECUTE_MONEY, ");
        sql.append(" max(t.EXCHANGE_RATE) as EXCHANGE_RATE, ");
        sql.append(" sum(t.RELATION_CONVERT_MONEY) as RELATION_CONVERT_MONEY ");
        sql.append(" from DOC_WIRTE_OFF_RELATION t ");
        sql.append(" where 1 = 1 ");
//        sql.append(addTenantConditionForSQL("t"));
        sql.append(" group by t.BATCH_NO,exchange_rate ) rel ");
        sql.append(" left join (select BATCH_NO, group_concat(distinct m.corp_id)  as corp_id,group_concat(distinct m.code) as code,group_concat(distinct m.source) source, \n" +
                " group_concat(distinct m.voucher_type) voucher_type,  group_concat(distinct  m.cur_id ) as cur_id ,  group_concat(distinct m.external_name)  external_name, group_concat(m.external_type) as external_type ,\n" +
                " group_concat(distinct m.contract_name) as contract_name,  group_concat(m.write_off_status) as write_off_status ,group_concat(distinct m.BATCH_BUS_TYPE ) as BATCH_BUS_TYPE ,\n" +
                " sum(BT_NVL_NUM(m.WRITE_OFF_BAL,m.RECEIVABLE_MONEY)) as WRITE_OFF_bal,sum(m.RECEIVABLE_MONEY) as RECEIVABLE_MONEY,   group_concat(distinct m.write_off_type) as write_off_type \n" +
                " from doc_manage m inner join (select batch_no,DOC_ID from DOC_WIRTE_OFF_RELATION group by batch_no,DOC_ID) w on  w.doc_id = m.ID group by BATCH_NO ) dm on rel.BATCH_NO = dm.BATCH_NO ");
        sql.append(" left join nis_bus_type nbt on  dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" left join sys_corp sc on sc.id = dm.corp_id");
        sql.append(" left join bt_currency bc on bc.id = dm.cur_id");
        sql.append(" where 1=1 ");
        if(StringUtils.isNotEmpty(businessSql)){
            sql.append(" and ").append(businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), DocWriteOffRelationVo.class, "rel", false, null);
    }

    /**
     * 根据批次号 获取核销数据
     * @param batchNo
     * @return
     */
    public List<DocWriteOffRelation> getWriteOffListByBatchNo(String batchNo) {
        String hql = " from DocWriteOffRelation where batchNo =:batchNo ";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        return this.listByHQL(hql, params);
    }

    public List<DocWriteOffRelation> getWriteOffListByBatchNo1(String batchNo) {
        String hql = " from DocWriteOffRelation where batchNo =:batchNo limit 1";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        return this.listByHQL(hql, params);
    }

    /**
     * 根据批次号 删除核销关系
     * @param batchNo
     * @return
     */
    public Integer delRelationByBatchNo(String batchNo){
        //String sql = " delete from DocWriteOffRelation where batchNo =:batchNo ";
        //将状态该为作废状态
        String sql= "update DocWriteOffRelation set status=-3  where batchNo =:batchNo";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        return this.createQuery(sql, params).executeUpdate();
    }

    /**
     * 根据批次号 删除核销关系
     * @param batchNo
     * @return
     */
    public Integer delRelationByBatchNo1(String batchNo){
        //String sql = " delete from DocWriteOffRelation where batchNo =:batchNo ";
        //将状态该为打回状态
        String sql= "update DocWriteOffRelation set status=-1  where batchNo =:batchNo";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        return this.createQuery(sql, params).executeUpdate();
    }

    /**
     * 根据批次号 更新审批状态
     * @param batchNo
     * @return
     */
    public Integer updateRelationByBatchNo(String batchNo, Integer approveStatus){
        String sql = " update DocWriteOffRelation set status=:status where batchNo =:batchNo ";
        Map<String, Object> params = new HashMap<>();
        params.put("batchNo", batchNo);
        params.put("status", approveStatus);
        return this.createQuery(sql, params).executeUpdate();
    }

}
