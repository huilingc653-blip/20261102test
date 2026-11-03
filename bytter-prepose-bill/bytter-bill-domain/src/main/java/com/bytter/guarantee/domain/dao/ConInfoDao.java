package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @Title: 合同主表Dao
 * @Package com.bytter.biz.contract.dao
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-4 下午3:09:23
 * @version V1.0
 */
@Repository
public class ConInfoDao extends BaseDao<ConInfo, String> {
    /**
     * 根据ids批量修改合同主表数据状态
     * 
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update ConInfo set ");
        StringBuilder hqlWhere = new StringBuilder("");
        // 合同状态
        if (params.containsKey("status")) {
            hqlWhere.append("status = :status");
        }
        if (params.containsKey("hookSign")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hql.append("hookSign=:hookSign ");
        }
        if (params.containsKey("performanceNum")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hqlWhere.append("performanceNum = :performanceNum ");
        }
        if (params.containsKey("changeNum")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hqlWhere.append("changeNum = :changeNum ");
        }
        if (params.containsKey("isChangeNum")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hqlWhere.append("changeNum = bt_nvl_num(changeNum,0) + 1 ");
        }
        if (params.containsKey("isPerformanceNum")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hqlWhere.append("performanceNum = bt_nvl_num(performanceNum,0) +1 ");
        }
        // 增加修改签订时间修改
        if (params.containsKey("signDate")) {
            if (hqlWhere.length() > 0) {
                hqlWhere.append(",");
            }
            hqlWhere.append("signDate = :signDate ");
        }
        hql.append(hqlWhere).append(" where id in (:ids) ");
        if (params.containsKey("status")) {
            hql.append(" and status<>:status ");
        }
        if (params.containsKey("hookSign")) {
            if (ConConstant.CON_BUSHOOK_VALID_SIGN_1.equals(String.valueOf(params.get("hookSign")))) {
                hql.append(" and (hookSign !=:hookSign or hookSign is null )");
            } else if (ConConstant.CON_BUSHOOK_VALID_SIGN_0.equals(String.valueOf(params.get("hookSign")))) {
                hql.append(" and hookSign!=:hookSign ");
            }
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 通过参数获取合同主信息
     * 
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ConInfo> getConInfoByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuffer hql = new StringBuffer("from ConInfo ci  ");
        hql.append(" where 1=1 ");
        hql.append(" and ci.status!=:status ");
        params.put("status", ConConstant.CON_DRAFT_STATUS_99);
        if (params.containsKey("id")) {
            hql.append(" and ci.id=:id ");
        }
        if (params.containsKey("conId")) {
            hql.append(" and ci.id=:conId ");
        }
        if (params.containsKey("busId")) {
            hql.append(
                    " and exists(select 1 from ConBusHook cbh where ci.id=cbh.conId and cbh.busId=:busId and cbh.validSign=:hookValidSign ) ");
            params.put("hookValidSign", ConConstant.CON_BUSHOOK_VALID_SIGN_1);
        }
        if (params.containsKey("recordId")) {
            hql.append(" and exists(select 1 from ConInfoRecord cir where ci.id=cir.mid and cir.id=:recordId ) ");
        }
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据单位id获取此单位中是否存在合同
     * 
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidContractNumByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder().append(" select count(*) ").append(" from CON_INFO_RECORD cir")
                .append(" left join CON_INFO ci on cir.mid = ci.id")
//                .append(" left join sys_corp s on ci.corp_id = s.id").append(" WHERE 1=1 and s.id=:corpId");
                .append(" WHERE 1=1 and ci.corp_id=:corpId");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        return ((Number) this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue();
    }
}
