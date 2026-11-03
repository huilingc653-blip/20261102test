package com.bytter.system.dao;
import com.bytter.system.entity.CorpCmMap;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Dao
 * @Description: 管理单元网点单位映射表数据库操作
 * @date 2020-05-15 17:18:59
 */
@Repository
public class CorpCmMapDao extends BaseDao<CorpCmMap, String> {
    /**
     * 根据单位id删除管理单元网点单位映射表
     *
     * @param corpId
     * @return
     */
    public int deleteByCorpId(String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            return 0;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        String sql = "delete from sys_corp_cm_map where corp_id =:corpId";
        return this.batchSQLExecute(sql, params);
    }
}
