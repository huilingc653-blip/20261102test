package com.bytter.biz.bdlink.dao;

import com.bytter.biz.bdlink.entity.BisBifMonitor;
import com.bytter.biz.bdlink.vo.BisBifMonitorVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 银行接口监控预警表
 * @date 2022-07-07 17:07:12
 */
@Repository
public class BisBifMonitorDao extends BaseDao<BisBifMonitor, String> {
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bis_Bif_Id as bisBifId,b.task_Type as taskType,b.role_Ids as roleIds,b.monitor_Hz as monitorHz,");
        sql.append(" b.status,b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate");
        sql.append(" from bis_bif_monitor b where 1=1");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisBifMonitorVO.class, "b", false);
    }
    /**
     * 根据ID信息
     *
     * @param id
     * @return
     */
    public BisBifMonitorVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bis_Bif_Id as bisBifId,b.task_Type as taskType,b.role_Ids as roleIds,b.monitor_Hz as monitorHz,");
        sql.append(" b.status,b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate");
        sql.append(" from bis_bif_monitor b where b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisBifMonitorVO.class);
    }
    /**
     * 获取有效的银行接口监控信息
     *
     * @return
     */
    public List<BisBifMonitorVO> listByValid() {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.bis_Bif_Id as bisBifId,bbi.name as bifName,b.task_Type as taskType,b.role_Ids as roleIds,b.monitor_Hz as monitorHz,");
        sql.append(" b.status,b.create_By as createBy,b.create_Date as createDate,b.update_By as updateBy,b.update_Date as updateDate");
        sql.append(" from bis_bif_monitor b left join bis_bif_init bbi on b.bis_bif_id=bbi.id where b.status=:status");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("status", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, BisBifMonitorVO.class);
    }
    /**
     * 根据银行类型获取最新的余额获取日期
     *
     * @param bifId
     * @return
     */
    public Date getMaxBalDate(String bifId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(bal.get_date) as getDate from bis_acc_bal bal left join bt_bank_acc acc on bal.bank_acc = acc.bank_acc");
        sql.append(" where acc.is_online = '1' and bal.bif_code=:bifId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bifId", bifId);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        if (result != null) {
            return (Date) result;
        }
        return null;
    }
}
