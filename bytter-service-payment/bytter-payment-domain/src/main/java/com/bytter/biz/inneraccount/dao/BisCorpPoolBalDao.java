package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.BisCorpPoolBal;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 多级单位余额核算-单位归集余额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Repository
public class BisCorpPoolBalDao extends BaseDao<BisCorpPoolBal, String> {

    /**
     * 查询 单位归集余额表 列表数据
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.corp_Id as corpId,c.name as corpName,b.cur_id as curId,cur.cur_Name as curName,b.bal as bal,b.bal_Date as balDate,");
        sql.append(" b.get_Date as getDate,b.init_Sign as initSign,o.overdraft_Amt as overdraftAmt");
        sql.append(" from Bis_Corp_Pool_Bal b");
        sql.append(" left join Bis_Corp_Pool_Overdraft o on o.corp_Id = b.corp_Id and o.cur_Id = b.cur_id");
        sql.append(" left join sys_corp c on c.id=b.corp_id");
        sql.append(" left join Bt_Currency cur on cur.id=b.cur_id");
        sql.append(" where 1=1");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisCorpPoolBalVO.class, "b", false);
    }

    /**
     * 根据ID获取单位归集余额表
     * @param id
     * @return
     */
    public BisCorpPoolBalVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.corp_Id as corpId,c.name as corpName,b.cur_id as curId,cur.cur_Name as curName,b.bal as bal,b.bal_Date as balDate,");
        sql.append(" b.get_Date as getDate,b.init_Sign as initSign,o.overdraft_Amt as overdraftAmt");
        sql.append(" from Bis_Corp_Pool_Bal b");
        sql.append(" left join Bis_Corp_Pool_Overdraft o on o.corp_Id = b.corp_Id and o.cur_Id = b.cur_id");
        sql.append(" left join sys_corp c on c.id=b.corp_id");
        sql.append(" left join Bt_Currency cur on cur.id=b.cur_id");
        sql.append(" where b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisCorpPoolBalVO.class);
    }

    /**
     * 删除 单位归集余额数据
     * @param ids
     */
    public void delBisCorpPoolBal(String ids) {
        StringBuilder sql = new StringBuilder("delete from Bis_Corp_Pool_Bal");
        sql.append(" where ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "id", true));
        HashMap<String, String> params = new HashMap<String, String>();
        this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 存储校验
     * @param vo
     * @return
     */
    public List<BisCorpPoolBalVO> listValidByCorpIdAndBalDate(BisCorpPoolBalVO vo) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BisCorpPoolBal b");
        hql.append(" where corpId=:corpId and curId=:curId and balDate=:balDate");
        if (StringUtil.isNotEmpty(vo.getId())) {
            hql.append(" and id<>:id ");
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", vo.getCorpId());
        params.put("curId", vo.getCurId());
        params.put("balDate", vo.getBalDate());
        params.put("id", vo.getId());
        return listByHQLAliasToBean(hql.toString(), params, BisCorpPoolBalVO.class);
    }

    /**
     * 根据单位和余额日期，获取余额
     * @param vo
     * @return
     */
    public List<BisCorpPoolBalVO> listByCorpIdAndBalDate(BisCorpPoolBalVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.corp_id,b.cur_id,b.bal_date,b.bal from bis_corp_pool_bal b inner join (");
        sql.append("select corp_id,cur_id,max(bal_date) as bal_date");
        sql.append(" from bis_corp_pool_bal");
        sql.append(" where bal_date < :balDate");
        if (StringUtil.isNotEmpty(vo.getCorpId())) {
            sql.append(" and ").append(SqlUtil.getInSql(vo.getCorpId().split(CommonConstant.COMMA_SPLIT), "corp_id", true));
        }
        if (StringUtil.isNotEmpty(vo.getCurId())) {
            sql.append(" and ").append(SqlUtil.getInSql(vo.getCurId().split(CommonConstant.COMMA_SPLIT), "cur_id", true));
        }
        sql.append(" group by corp_id,cur_id");
        sql.append(") c on b.corp_id=c.corp_id and b.cur_id=c.cur_id and b.bal_date=c.bal_date");
        sql.append(" where b.bal_date < :balDate");
        if (StringUtil.isNotEmpty(vo.getCorpId())) {
            sql.append(" and ").append(SqlUtil.getInSql(vo.getCorpId().split(CommonConstant.COMMA_SPLIT), "b.corp_id", true));
        }
        if (StringUtil.isNotEmpty(vo.getCurId())) {
            sql.append(" and ").append(SqlUtil.getInSql(vo.getCurId().split(CommonConstant.COMMA_SPLIT), "b.cur_id", true));
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("balDate", vo.getBalDate());
        return listBySQLAliasToBean(sql.toString(), params, BisCorpPoolBalVO.class);
    }

    /**
     * 根据单位和币种，获取初始化余额
     * @param corpId
     * @param curId
     * @return
     */
    public BisCorpPoolBalVO getInitBalByCorpId(String corpId, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.corp_Id as corpId,c.name as corpName,b.cur_id as curId,cur.cur_Name as curName,b.bal as bal,b.bal_Date as balDate,");
        sql.append(" b.get_Date as getDate,b.init_Sign as initSign,o.overdraft_Amt as overdraftAmt");
        sql.append(" from Bis_Corp_Pool_Bal b");
        sql.append(" left join Bis_Corp_Pool_Overdraft o on o.corp_Id = b.corp_Id and o.cur_Id = b.cur_id");
        sql.append(" left join sys_corp c on c.id=b.corp_id");
        sql.append(" left join Bt_Currency cur on cur.id=b.cur_id");
        sql.append(" where b.init_Sign=:initSign and b.corp_Id=:corpId and b.cur_id=:curId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("initSign", CommonConstant.YES);
        params.put("corpId", corpId);
        params.put("curId", curId);
        return getBySQLAliasToBean(sql.toString(), params, BisCorpPoolBalVO.class);
    }

    public List<BisCorpPoolBalVO> getLevelCorpBalances(int corpLevel) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT corp_id corpId,corpName,net_id netId,cur_id curId,curName, bal, bal_date balDate ");
        sql.append("FROM ( ");
        sql.append("        SELECT pb.corp_id,c.`NAME` corpName,c.net_id,pb.cur_id,bc.CUR_NAME curName,pb.bal,pb.bal_date,");
        sql.append("        ROW_NUMBER() OVER (PARTITION BY pb.corp_id, pb.CUR_ID ORDER BY pb.bal_date DESC) AS rn ");
        sql.append("        FROM bis_corp_pool_bal pb ");
        sql.append("        LEFT JOIN sys_corp c ON pb.CORP_ID = c.ID ");
        sql.append("        LEFT JOIN bt_currency bc ON pb.cur_id = bc.id ");
        sql.append("        WHERE LENGTH(c.PARENT_IDS) - LENGTH(REPLACE(c.PARENT_IDS, ',', '')) >= :corpLevel ");
        sql.append(") tmp ");
        sql.append("WHERE rn = 1");
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpLevel", corpLevel);
        return listBySQLAliasToBean(sql.toString(), params, BisCorpPoolBalVO.class);
    }
}
