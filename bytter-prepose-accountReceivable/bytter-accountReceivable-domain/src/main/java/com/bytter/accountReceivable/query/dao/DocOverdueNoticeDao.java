package com.bytter.accountReceivable.query.dao;

import com.bytter.accountReceivable.query.entity.DocOverdueNotice;
import com.bytter.accountReceivable.query.vo.DocOverdueNoticeDtlVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 逾期催收登记表 dao
 */
@Repository
public class DocOverdueNoticeDao extends BaseDao<DocOverdueNotice, String> {

    /**
     * 根据应收单id获取 逾期催收登记数据
     * @param docId
     * @return
     */
    public List<DocOverdueNotice> getOverDueNoticeByDocId(String docId, ICurrentUser user){
        String hql = " from DocOverdueNotice don where don.docId =:docId";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("docId", docId);
        //需求要求也能看到别人录入的
        /*if(user!=null){
            hql+=" and createBy=:createBy";
            params.put("createBy", user.getId());
        }*/
        hql+=" order by don.allocationDate";
        return listByHQL(hql.toString(), params);
    }

    /**
     * 根据应收单id 删除已登记逾期催收记录
     * @param docId
     * @return
     */
    public int delOverDueNoticeByDocId(String docId){
        String hql = " delete from DocOverdueNotice don where don.docId =:docId ";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("docId", docId);
        return createQuery(hql.toString(), params).executeUpdate();
    }

    public List<DocOverdueNoticeDtlVo> queryOverDueNoticeByDocId(String docId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.DOC_ID as docId, t.OVERDUE_REASON as overdueReason,t.COLLECTION_BY as allocationBy,");
        sql.append(" t.COLLECTION_DATE as allocationDate, t.COLLECTIO_RESULT as allocationResult,");
        sql.append(" t.TENANT_ID as tenantId, t.CREATE_BY as createBy, t.CREATE_DATE as createDate");
        sql.append(" from DOC_OVERDUE_NOTICE t");
        sql.append(" where t.DOC_ID =:docId");
        sql.append(" order by t.COLLECTION_DATE");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("docId", docId);
        return listBySQLAliasToBean(sql.toString(), params, DocOverdueNoticeDtlVo.class);
    }

}
