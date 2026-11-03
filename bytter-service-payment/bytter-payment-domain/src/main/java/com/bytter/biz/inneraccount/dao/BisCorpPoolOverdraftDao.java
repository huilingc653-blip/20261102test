package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.BisCorpPoolOverdraft;
import com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 多级单位余额核算-单位归集透支金额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Repository
public class BisCorpPoolOverdraftDao extends BaseDao<BisCorpPoolOverdraft, String> {

    /**
     * 查询 单位归集余额表 列表数据
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.corp_Id as corpId,c.name as corpName,b.cur_id as curId,cur.cur_Name as curName,b.overdraft_Amt as overdraftAmt");
        sql.append(" from Bis_Corp_Pool_Overdraft b");
        sql.append(" left join sys_corp c on c.id=b.corp_id");
        sql.append(" left join Bt_Currency cur on cur.id=b.cur_id");
        sql.append(" where 1=1");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisCorpPoolOverdraftVO.class, "b", false);
    }

    /**
     * 根据ID获取单位归集余额表
     * @param id
     * @return
     */
    public BisCorpPoolOverdraftVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.corp_Id as corpId,c.name as corpName,b.cur_id as curId,cur.cur_Name as curName,b.overdraft_Amt as overdraftAmt");
        sql.append(" from Bis_Corp_Pool_Overdraft b");
        sql.append(" left join sys_corp c on c.id=b.corp_id");
        sql.append(" left join Bt_Currency cur on cur.id=b.cur_id");
        sql.append(" where b.id=:id");
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisCorpPoolOverdraftVO.class);
    }

    /**
     * 删除 单位归集透支金额数据
     * @param ids
     */
    public void delBisCorpPoolOverdraft(String ids) {
        StringBuilder sql = new StringBuilder("delete from Bis_Corp_Pool_Overdraft");
        sql.append(" where ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "id", true));
        HashMap<String, String> params = new HashMap<String, String>();
        this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 存储校验
     * @param vo
     * @return
     */
    public List<BisCorpPoolOverdraftVO> listValidByCorpId(BisCorpPoolOverdraftVO vo) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BisCorpPoolOverdraft b");
        hql.append(" where corpId=:corpId and curId=:curId");
        if (StringUtil.isNotEmpty(vo.getId())) {
            hql.append(" and id<>:id ");
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("corpId", vo.getCorpId());
        params.put("curId", vo.getCurId());
        params.put("id", vo.getId());
        return listByHQLAliasToBean(hql.toString(), params, BisCorpPoolOverdraftVO.class);
    }
}
