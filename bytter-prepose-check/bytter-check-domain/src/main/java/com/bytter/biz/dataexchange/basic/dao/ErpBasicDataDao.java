package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpBasicData;
import com.bytter.biz.dataexchange.basic.vo.ErpBasicDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: 基础数据映射
* @author onlineGenerator
* @date 2017-12-14 11:24:27
* @version 1.0
*
*/
@Repository
public class ErpBasicDataDao extends BaseDao<ErpBasicData,String> {
    public ErpBasicData getBean(String bytterCode,String dataTypeCode,String typeCode){
        return getUnique("from ErpBasicData where bytterCode =?0 and dataTypeCode =?1 and typeCode =?2",bytterCode,dataTypeCode,typeCode);
    }
    /**
     * 判断是否有相同的银行账号
     *
     * @param bytterCode
     * @return
     */
    public boolean isBankAcc(String bytterCode) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from ErpBasicData where bytterCode = :bytterCode ");
        params.put("bytterCode", bytterCode);
        List list = this.listByHQL(hql.toString(), params);
        return EmptyUtils.isEmptyList(list);
    }
    /**
     * 判断是否有相同币别的银行账号
     *
     * @param bytterCode
     * @return
     */
    public boolean isBankAcc(String bytterCode, String erpReverse) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from ErpBasicData where bytterCode = :bytterCode and erpReverse= :erpReverse ");
        params.put("bytterCode", bytterCode);
        params.put("erpReverse", erpReverse);
        List list = this.listByHQL(hql.toString(), params);
        return EmptyUtils.isEmptyList(list);
    }
     
    public DataTablesOutput getErpList(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder();
        sql.append(" select bytter_code as bytterCode,erp_reverse as erpReverse,erp_code as erpCode,erp_name as erpName ");
        sql.append(" from Erp_Basic_Data");
        sql.append(" where 1=1 ");
        sql.append(" and data_type_code = 'BT_BANK_ACC' ");
        sql.append(" and type_code = 'rsm' ");
        
        return getDynamicBySQL(dataTablesInput, sql.toString(), ErpBasicDataVO.class, "", false);
    }
}
