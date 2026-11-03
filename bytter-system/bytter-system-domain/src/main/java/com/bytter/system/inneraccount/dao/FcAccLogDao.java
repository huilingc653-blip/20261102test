package com.bytter.system.inneraccount.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.FcAccLogVO;
import com.bytter.system.inneraccount.entity.FcAccLog;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcAccLogDao extends BaseDao<FcAccLog,String>{
    /**
     * 根据业务表单据Id查询日志信息
     * @param bizId
     * @return
     */
    public List<FcAccLog> listByBizId(String bizId) {
        String hql = " from FcAccLog where bizId =:bizId order by opTime desc";
        Map params = new HashMap();
        params.put("bizId",bizId);
        return this.listByHQL(hql,params);
    }
    /**
     * 查询日志信息
     * 排除开户 已经删除的过的记录；再开户的时候账号会重用，但是应该算是新账号。
     * @param bizId
     * @param account
     * @param bizType
     * @return
     */
    public List<FcAccLogVO> listByParams(String bizId, String account, String bizType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id as id,account as account,biz_Id as bizId,biz_Type as bizType,");
        sql.append(" biz_Msg as bizMsg,op_Code as opCode,op_Time as opTime,op_Type as opType ");
        sql.append(" from Fc_Acc_Log where 1=1 and biz_id not in ");
        sql.append(" (select biz_id from fc_acc_log f where 1=1 and f.account=:account and f.op_type='D' and biz_type='10' )");
        Map params = new HashMap();
        if(StringUtils.isNotEmpty(bizId)) {
            sql.append( " and biz_Id =:bizId");
            params.put("bizId", bizId);
        }
        if(StringUtils.isNotEmpty(account)) {
            sql.append( " and account =:account");
        }
        if(StringUtils.isNotEmpty(bizType)) {
            sql.append( " and biz_Type =:bizType");
            params.put("bizType", bizType);
        }
        sql.append(" order by op_Time desc");
        params.put("account", account);
        return this.listBySQLAliasToBean(sql.toString(),params,FcAccLogVO.class);
    }
}
