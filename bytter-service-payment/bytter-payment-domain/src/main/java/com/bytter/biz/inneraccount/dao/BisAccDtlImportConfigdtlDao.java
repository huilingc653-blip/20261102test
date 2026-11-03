package com.bytter.biz.inneraccount.dao;
import com.bytter.biz.inneraccount.entity.BisAccDtlImportConfigdtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Dao
 * @Description: BIS_ACC_DTL_IMPORT_CONFIGDTL
 * @date 2020-07-31 15:02:30
 */
@Repository
public class BisAccDtlImportConfigdtlDao extends BaseDao<BisAccDtlImportConfigdtl, String> {
    /**
     * 批量删除明细
     * @param icId
     * @return
     */
    public int delByIcId(String icId){
        StringBuilder hql=new StringBuilder("");
        hql.append(" delete BisAccDtlImportConfigdtl where icId =:icId ");
        HashMap<String, String> params = new HashMap<>();
        params.put("icId", icId);
        return this.batchExecute(hql.toString(), params);
    }
}
