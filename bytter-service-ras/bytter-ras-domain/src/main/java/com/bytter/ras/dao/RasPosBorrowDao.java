package com.bytter.ras.dao;

import com.bytter.ras.entity.RasPosBorrow;
import com.bytter.ras.vo.RasPosBorrowVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.ras.api.constant.RasConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: dao
 * @Description: 营收稽核POS机借入数据库操作类
 * @date 2019-05-07
 */
@Repository
public class RasPosBorrowDao extends BaseDao<RasPosBorrow, String> {

    /**
     * 营收稽核POS机借入列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("p.id as id,p.bd_id as bdId,p.corp_id as corpId,p.shop_id as shopId,r.name as shopName,r.code as shopCode,");
        sql.append("p.status as status,p.bd_date as bdDate,p.ubd_date as ubdDate,p.msg as msg,b.pos_no as posNo,");
        sql.append("p.rmk as rmk,p.create_date as createDate,p.update_date as updateDate,s.name as corpName,s.code as corpCode ");

        sql.append(" from RAS_POS_BORROW p  ");
        sql.append(" left join ras_pos_binding b on p.bd_id = b.id ");
        sql.append(" left join sys_corp s on p.corp_id = s.id ");
        sql.append(" left join ras_shop r on p.shop_id = r.id");
        sql.append(" WHERE p.status<>" + RasConstant.RAS_POS_BR_STATUS_RETURN);
        Map<String, Object> params = new HashMap<>();
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "r.id", "shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "s.id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), RasPosBorrowVO.class, "", false, params);
    }

    /**
     * 根据ids批量修改POS机借入表数据状态
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update RasPosBorrow "
                + " set "
                + "updateDate = :updateDate ");
        if (params.containsKey("updateBy")) {
            hql.append(",updateBy = :updateBy ");
        }
        if (params.containsKey("updateName")) {
            hql.append(",updateName = :updateName ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(",status = :statusNew ");
        }
        if (params.containsKey("msg")) {
            hql.append(",msg = :msg ");
        }
        if (params.containsKey("bdDate")) {
            hql.append(",bdDate = :bdDate ");
        }
        if (params.containsKey("ubdDate")) {
            hql.append(",ubdDate = :ubdDate ");
        }
        hql.append(" where id in (:ids) ");
        if (params.containsKey("statusOld")) {
            hql.append(" and status=:statusOld ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(" and status!=:statusNew ");
        }
        return this.batchExecute(hql.toString(), params);
    }


    /**
     * 通过pos机绑定id查询pos机借出数据
     *
     * @param bdId   pos机绑定id
     * @param status 状态
     * @return
     */
    public List<RasPosBorrowVO> getRasPosBorrowByBdIdAndStatus(String bdId, String[] status) {
        StringBuilder sql = new StringBuilder("select b.id as id,b.bd_id as bdId,b.shop_id as shopId,b.status as status,b.rmk as rmk,");
        sql.append("b.bd_date as bdDate,b.ubd_date as ubdDate,b.msg as msg,s.id as corpId,s.name as corpName,");
        sql.append("r.name as shopName,r.code as shopCode ");
        sql.append(" from RAS_POS_BORROW b ");
        sql.append(" left join sys_corp s on b.corp_id = s.id ");
        sql.append(" left join ras_shop r on b.shop_id = r.id ");
        sql.append(" where b.bd_id = :bdId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bdId", bdId);
        if (status != null) {
            sql.append(" and b.status in (:status) ");
            params.put("status", status);
        }
        sql.append(" order by b.bd_date desc ");
        return this.listBySQLAliasToBean(sql.toString(), params, RasPosBorrowVO.class);
    }

    /**
     * 通过pos机借入id查询pos机借出数据
     *
     * @param id pos机借入id
     * @return
     */
    public List<RasPosBorrowVO> getRasPosBorrowById(String id) {
        StringBuilder sql = new StringBuilder("select b.id as id,b.bd_id as bdId,b.shop_id as shopId,b.status as status,b.rmk as rmk,");
        sql.append("b.bd_date as bdDate,b.ubd_date as ubdDate,b.msg as msg,s.id as corpId,s.name as corpName,");
        sql.append("r.name as shopName,r.code as shopCode,p.pos_no as posNo,s.code as corpCode ");
        sql.append(" from RAS_POS_BORROW b ");
        sql.append(" left join ras_pos_binding p on b.bd_id = p.id ");
        sql.append(" left join sys_corp s on b.corp_id = s.id ");
        sql.append(" left join ras_shop r on b.shop_id = r.id ");
        sql.append(" where b.id = :id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        sql.append(" order by b.bd_date desc ");
        return this.listBySQLAliasToBean(sql.toString(), params, RasPosBorrowVO.class);
    }
}




















