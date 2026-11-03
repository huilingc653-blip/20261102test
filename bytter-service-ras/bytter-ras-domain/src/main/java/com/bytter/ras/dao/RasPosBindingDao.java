package com.bytter.ras.dao;

import com.bytter.ras.entity.RasPosBinding;
import com.bytter.ras.vo.RasPosBindingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: dao
 * @Description: 营收稽核POS机绑定数据库操作类
 * @date 2019-05-06
 */
@Repository
public class RasPosBindingDao extends BaseDao<RasPosBinding, String> {

    /**
     * 营收稽核POS机绑定列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, List<String> shopAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("p.id as id,p.corp_id as corpId,p.shop_id as shopId,r.name as shopName,r.code as shopCode,");
        sql.append("p.status as status,p.valid_sign as validSign,p.pos_no as posNo,");
        sql.append("p.rmk as rmk,p.create_date as createDate,p.update_date as updateDate,s.name as corpName,s.code as corpCode ");

        sql.append(" from ras_pos_binding p  ");
        sql.append(" left join sys_corp s on p.corp_id = s.id ");
        sql.append(" left join ras_shop r on p.shop_id = r.id");
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String shopAuthSql = SqlUtil.getInSqlByParams(shopAuthList, "r.id", "shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "s.id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), RasPosBindingVO.class, "", false, params);
    }

    /**
     * 根据ID查询营收稽核POS机绑定数据
     *
     * @param pid POS机绑定id
     * @return
     */
    public List<RasPosBindingVO> getRasPosBindingVOByPid(String pid) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("p.id as id,p.corp_id as corpId,p.shop_id as shopId,r.name as shopName,r.code as shopCode,");
        sql.append("p.status as status,p.valid_sign as validSign,p.pos_no as posNo,");
        sql.append("p.rmk as rmk,p.create_date as createDate,s.name as corpName,s.code as corpCode ");
        sql.append(" from ras_pos_binding p  ");
        sql.append(" left join sys_corp s on p.corp_id = s.id ");
        sql.append(" left join ras_shop r on p.shop_id = r.id");
        sql.append(" WHERE p.id=:pid ");
        Map<String, Object> params = new HashMap<>();
        params.put("pid", pid);
        List<RasPosBindingVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, RasPosBindingVO.class);
        return listInfo;
    }


    /**
     * pos机绑定判断重复校验(规则:门店+终端号做唯一校验来判断重复)
     *
     * @param params 门店Id 终端号
     * @param pid    pos机绑定id
     * @return 存在数据返回true否则false
     */
    public boolean checkPosBinding(Map<String, Object> params, String pid) {
        StringBuilder sql = new StringBuilder("select p.id from ras_pos_binding p where  p.pos_no=:posNo ");
        if (StringUtils.isNotEmpty(pid)) {
            sql.append(" and p.id <> :notId ");
            params.put("notId", pid);
        }
        List<RasPosBinding> list = this.listBySQLAliasToBean(sql.toString(), params, RasPosBinding.class);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }


    /**
     * 根据ids批量修改营收稽核POS机绑定数据状态
     *
     * @param params
     * @return
     */
    public int batchUpdate(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update RasPosBinding "
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
        hql.append(" where id in (:ids)");
        if (params.containsKey("statusOld")) {
            hql.append(" and status=:statusOld ");
        }
        if (params.containsKey("statusNew")) {
            hql.append(" and status!=:statusNew ");
        }
        return this.batchExecute(hql.toString(), params);
    }


    /**
     * 根据参数获取POS机绑定数据
     *
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<RasPosBinding> getRasPosBindingByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("from RasPosBinding where 1=1 ");
        if (params.containsKey("id")) {
            hql.append(" and id=:id ");
        }
        if (params.containsKey("posNo")) {
            hql.append(" and posNo=:posNo ");
        }
        if (params.containsKey("validSign")) {
            hql.append(" and validSign=:validSign ");
        }
        return this.listByHQL(hql.toString(), params);
    }
}




















