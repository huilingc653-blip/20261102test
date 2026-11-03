package com.bytter.bill.domain.billbank.dao;

import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 票据主信息从表
 * @date 2017-06-21 15:50:03
 */
@SuppressWarnings("serial")
@Repository
public class BillAbVoucherinfoViceDao extends BaseDao<BillAbVoucherinfoVice, String> {
    /**
     * 审批打回或指令打回时设置从表打回业务类型
     *
     * @param id      从表ID
     * @param briefId 被修改的打回业务类型值
     * @param acid    被修改的打回业务流水ID
     * @return
     */
    public int updateBackBriefId(String id, String briefId, String acid) {
        StringBuilder hql = new StringBuilder(" update BillAbVoucherinfoVice set backBriefId=:briefId ");
        if (StrUtil.isNotEmpty(acid)) {
            hql.append(" ,backAcid=:acid ");
        }
        hql.append(" where id=:id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("briefId", EDraftConstant.getT2BriefId(briefId));
        params.put("acid", acid);
        return this.batchExecute(hql.toString(), params);
    }


    /**
     * 根据ids批量修改票据从表所属库存信息
     *
     * @param params
     * @return
     */
    public int batchUpdateEnterStock(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update BillAbVoucherinfoVice "
                + " set enterStock = :enterStockNew"
                + ",updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        hql.append(" where id in (:ids)");
        if (params.containsKey("enterStockOld")) {
            hql.append(" and enterStock=:enterStockOld ");
        }
        if (params.containsKey("enterStockNew")) {
            hql.append(" and (enterStock!=:enterStockNew or enterStock is null) ");
        }
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 根据ids批量修改票据从表所属库存信息
     *
     * @param params
     * @return
     */
    public int batchUpdateValidSign(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update BillAbVoucherinfoVice "
                + " set validSign = :validSignNew");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        hql.append(",updateDate = :updateDate ");
        hql.append(" where id in (:ids)");
        if (params.containsKey("validSignOld")) {
            hql.append(" and validSign=:validSignOld ");
        }
        return this.batchExecute(hql.toString(), params);
    }


    /**
     * 根据票据主信息ID和单位ID获取从表数据
     *
     * @param vid    票据主表id
     * @param corpId 单位id
     * @param type   从表类型 0应收1应付
     * @return
     */
    public BillAbVoucherinfoVice getVoucherinfoViceByVidAndCorpId(String vid, String corpId, String type, String bankAcc) {
        String hql = "from BillAbVoucherinfoVice where vid=:vid and corpId=:corpId and type=:type and validSign='1' ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vid", vid);
        params.put("corpId", corpId);
        params.put("type", type);
        if (StrUtil.isNotEmpty(bankAcc)) {
            params.put("bankAcc", bankAcc);
            hql += " and bankAcc=:bankAcc ";
        }
        hql += " order by createDate desc ";
        @SuppressWarnings("unchecked")
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 上划下拨或归集时根据id修改票据从表持票人信息
     *
     * @param params
     * @return
     */
    public int updateAbVoucherinfoVice(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update BillAbVoucherinfoVice "
                + " set bankAcc = :bankAcc,bankCode = :bankCode,bankName = :bankName ,bifCode = :bifCode ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        hql.append(",updateDate = :updateDate ");
        hql.append(" where id = :id and corpId = :corpIdOld and (corpId <> :corpIdOld or corpId is null) ");
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 根据开票登记单号获取从表信息
     *
     * @param appNo 开票登记单号
     * @return BillAbVoucherinfoVice
     */
    @SuppressWarnings("unchecked")
    public BillAbVoucherinfoVice getAbVoucherinfoViceByAppNo(String appNo) {
        String hql = "from BillAbVoucherinfoVice where appNo=:appNo ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appNo", appNo);
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据业务id修改票据从表票据池状态和业务占用状态
     *
     * @param busId      业务ID
     * @param poolStatus 池状态 空或0池外1池内2入池在途3出池在途
     * @param boxStatus  业务占用状态
     * @return
     */
    public int updateVoucherinfoViceByBusId(String busId, String poolStatus, Integer boxStatus) {
        if (StringUtil.isEmpty(busId)
                || (StringUtil.isEmpty(poolStatus)
                && StringUtil.isEmpty(boxStatus))) {
            return 0;
        }
        StringBuffer sql = new StringBuffer("update AB_VOUCHERINFO_VICE set  ");
        Map<String, Object> params = new HashMap<String, Object>();
        boolean isSet = false;
        if (StringUtil.isNotEmpty(poolStatus)) {
            sql.append("POOL_STATUS=:poolStatus");
            params.put("poolStatus", poolStatus);
            isSet = true;
        }
        if (boxStatus != null) {
            if (isSet) {
                sql.append(",");
            } else {
                isSet = true;
            }
            sql.append("BOX_STATUS=:boxStatus");
            params.put("boxStatus", boxStatus);
        }
        sql.append(" where id=:busId ");
        params.put("busId", busId);
        return this.batchSQLExecute(sql.toString(), params);
    }


    /**
     * 根据时间段查询数据来源的票据从表信息
     * @param source
     * @param statusDate
     * @param endDate
     * @return
     */
    public List<BillAbVoucherinfoVice> queryBySource(String source, String statusDate, String endDate) {

        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT * ");
        sql.append("  FROM ab_voucherinfo_vice ");
        sql.append(" WHERE SOURCE=:source  ");
        sql.append("  AND VOUCHER_ID<>'' ");
        sql.append("  AND VALID_SIGN<>'2' ");
        sql.append("  AND VOUCHER_ID NOT IN ( ");
        sql.append("           SELECT id ");
        sql.append("   FROM nis_billhead ");
        sql.append(" ) ");
        sql.append(" and CREATE_DATE >= bt_to_date(:statusDate,'yyyy-MM-dd HH24:mi:ss') ");
        sql.append(" and CREATE_DATE <= bt_to_date(:endDate,'yyyy-MM-dd HH24:mi:ss') ");


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", source);
        params.put("statusDate", statusDate);
        params.put("endDate", endDate);
        return this.listBySQLAliasToBean(sql.toString(), params, BillAbVoucherinfoVice.class);
    }


}
