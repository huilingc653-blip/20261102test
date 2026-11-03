package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.system.entity.SysAttachment;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: SYS_ATTACHMENT
 * @date 2017-05-26 12:20:21
 */
@Repository
public class SysAttachmentDao extends BaseDao<SysAttachment, String> {

    public List<SysAttachment> findAttachmentByIds(List<String> fileIds) {
        Map<String,Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysAttachment where id in(:fileIds)");
        sb.append(" order by createDate asc");
        params.put("fileIds",fileIds);
        return listByHQL(sb.toString(),params);
    }
    public DataTablesOutput findtest(DataTablesInput input) {
        String sql = "select * from SYS_ATTACHMENT a where a.re_name=:re_name ";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("re_name", "402880215c9afdf7015c9afdf7520000");
        String[] need = new String[]{"name"};
        return getDynamicParamsBySQL(input, sql, SysAttachment.class, "a", false, DynamicallyObject.getInstance(map), need);
    }
    public List<SysAttachment> getAttachmentByGroupId(String groupId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer("from SysAttachment where groupId = :groupId");
        sb.append(" order by createDate asc");
        params.put("groupId", groupId);
        return listByHQL(sb.toString(), params);
    }
}
