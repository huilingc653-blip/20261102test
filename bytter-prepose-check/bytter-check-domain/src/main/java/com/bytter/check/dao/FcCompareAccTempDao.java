package com.bytter.check.dao;
import com.bytter.check.entity.FcCompareAccTemp;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
/**
* @Title: Dao
* @Description: ERP未达账初始化
* @author onlineGenerator
* @date 2017-06-08 09:16:58
* @version 1.0
*
*/
@Repository
public class FcCompareAccTempDao extends BaseDao<FcCompareAccTemp,String>{
    /**
     * 清空临时表数据
     */
    public void deleteFcCompareAccTemp() {
        String sql = "delete from fc_compare_acc_temp";
        createSQLQuery(sql).executeUpdate();
    }
}
