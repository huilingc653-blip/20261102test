package com.bytter.biz.inneraccount.dao;

import cn.hutool.core.convert.Convert;
import com.bytter.biz.inneraccount.vo.BisCorpPoolDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 多级单位余额核算-单位归集流水维护表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Repository
public class BisCorpPoolDtlDao extends BaseDao<BisCorpPoolDtlVO, String> {

    /**
     * 查询 单位归集流水维护表 列表数据
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.id as dtlId,t.acc_no as accNo,acc.corp_id as corpId,c.name as corpName,t.bank_acc,t.acc_name,t.bank_name,t.bif_code as bifCode, bis.name bifName,");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,t.rb_sign as rbSign,");
        sql.append(" t.amt,t.bal,t.cur_id as curId,cur.cur_Name as curName,t.uses,t.postscript,t.remark,t.trans_time as transTime,t.voucher_no as voucherNo,");
        sql.append(" t.bank_serial_no as bankSerialNo,t.return_time as returnTime, t.pool_Type as poolType,t.pool_Blend_Sign as poolBlendSign ");
        sql.append(" from bis_acc_dtl t");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code ");
        sql.append(" left join bt_Currency cur on cur.id=t.cur_id");
        // 本方账号信息
        sql.append(" inner join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join sys_corp c on c.id=acc.corp_id");
        sql.append(" left join sys_corp pc on pc.id=c.parent_id");
        sql.append(" where 1=1");
        // 过滤本级单位为本方账户名、本级单位对应的上级单位为对方户名的所有流水
        sql.append(" and t.acc_name = c.name and t.opp_Acc_Name = pc.name");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '')");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisCorpPoolDtlVO.class, "t", false);
    }

    /**
     * 更新类型
     * @param vo
     * @return
     */
    public int updatePoolType(BisCorpPoolDtlVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("update bis_acc_dtl set pool_Type=:poolType, pool_Blend_Sign=:poolBlendSign,update_Date=:updateDate");
        if (StringUtil.isNotEmpty(vo.getUpdateBy())) {
            sql.append(",update_By=:updateBy");
        }
        sql.append(" where ").append(SqlUtil.getInSql(vo.getId().split(CommonConstant.COMMA_SPLIT), "id", true));
        Map<String,Object> params = new HashMap<>();
        params.put("poolType", vo.getPoolType());
        params.put("poolBlendSign", vo.getPoolBlendSign());
        params.put("updateBy", vo.getUpdateBy());
        params.put("updateDate", DateUtils.getDate());
        return batchSQLExecute(sql.toString(), params);
    }

    /**
     * 获取指定单位某天的归集流水集合
     * @param corpId
     * @param curId
     * @param transTime
     * @return
     */
    public List<BisCorpPoolDtlVO> listByCorpIdAndTransTime(String corpId, String curId, Date transTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.id as dtlId,acc.corp_id as corpId,c.name as corpName,");
        sql.append(" t.amt,t.pool_Type as poolType,t.pool_Blend_Sign as poolBlendSign ");
        sql.append(" from bis_acc_dtl t");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code ");
        sql.append(" left join bt_Currency cur on cur.id=t.cur_id");
        // 本方账号信息
        sql.append(" inner join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join sys_corp c on c.id=acc.corp_id");
        sql.append(" left join sys_corp pc on pc.id=c.parent_id");
        sql.append(" where 1=1");
        // 过滤本级单位为本方账户名、本级单位对应的上级单位为对方户名的所有流水
        sql.append(" and t.acc_name = c.name and t.opp_Acc_Name = pc.name");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '')");
        // 类型为1-归集和2-下拨
        sql.append(" and t.pool_Type in ('1','2') and t.pool_Blend_Sign = '1'");
        sql.append(" and acc.corp_Id=:corpId and t.cur_id=:curId and bt_to_char(t.trans_time,'yyyy-MM-dd') = :transTime ");
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("curId", curId);
        params.put("transTime", DateUtils.formatDate(transTime));
        return this.listBySQLAliasToBean(sql.toString(), params, BisCorpPoolDtlVO.class);
    }

    /**
     * 自动认领单位归集流水。
     *
     * @param dtlIdList 银行流水编号列表。
     * @param poolType  业务类型
     * @return 响应条数。
     */
    public int updateBusTypeByIdList(List<String> dtlIdList, String poolType) {
        String sql = " update bis_acc_dtl set pool_Type = :poolType,pool_Blend_Sign =:poolBlendSign where id in (:dtlIdList)";
        Map<String, Object> params = new HashMap<>();
        params.put("poolType", poolType);
        params.put("dtlIdList", dtlIdList);
        params.put("poolBlendSign", CommonConstant.YES);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 根据匹配规则的获取待认领流水总行数
     *
     * @param busRuleSql
     * @return
     */
    public int countDtlByConfigRule(String busRuleSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) ");
        sql.append(" from bis_acc_dtl t");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code ");
        sql.append(" left join bt_Currency cur on cur.id=t.cur_id");
        // 本方账号信息
        sql.append(" inner join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join sys_corp c on c.id=acc.corp_id");
        sql.append(" left join sys_corp pc on pc.id=c.parent_id");
        sql.append(" where 1=1");
        // 过滤本级单位为本方账户名、本级单位对应的上级单位为对方户名的所有流水
        sql.append(" and t.acc_name = c.name and t.opp_Acc_Name = pc.name");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '')");
        sql.append(" and t.pool_blend_sign <> 1 ");
        sql.append(busRuleSql);
        Map<String, Object> params = new HashMap<>();
        return Convert.toInt(createSQLQuery(sql.toString(), params).uniqueResult(), 0);
    }

    /**
     * 分批查询匹配的单位归集流水。
     *
     * @param busRuleSql
     * @param startIndex
     * @param maxIndex
     * @return
     */
    public List<BisCorpPoolDtlVO> listDtlByConfigRule(String busRuleSql, Integer startIndex, Integer maxIndex) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.id as dtlId,t.acc_no as accNo,acc.corp_id as corpId,c.name as corpName,t.bank_acc,t.acc_name,t.bank_name,t.bif_code as bifCode, bis.name bifName,");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,t.rb_sign as rbSign,");
        sql.append(" t.amt,t.bal,t.cur_id as curId,cur.cur_Name as curName,t.uses,t.postscript,t.remark,t.trans_time as transTime,t.voucher_no as voucherNo,");
        sql.append(" t.bank_serial_no as bankSerialNo,t.return_time as returnTime, t.pool_Type as poolType,t.pool_Blend_Sign as poolBlendSign ");
        sql.append(" from bis_acc_dtl t");
        sql.append(" left join bis_bif_init bis on bis.id = t.bif_code ");
        sql.append(" left join bt_Currency cur on cur.id=t.cur_id");
        // 本方账号信息
        sql.append(" inner join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join sys_corp c on c.id=acc.corp_id");
        sql.append(" left join sys_corp pc on pc.id=c.parent_id");
        sql.append(" where 1=1");
        // 过滤本级单位为本方账户名、本级单位对应的上级单位为对方户名的所有流水
        sql.append(" and t.acc_name = c.name and t.opp_Acc_Name = pc.name");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '')");
        sql.append(" and t.pool_blend_sign <> 1 ");
        sql.append(busRuleSql);
        return listBySQLAliasToBean(sql.toString(), startIndex, maxIndex, null, BisCorpPoolDtlVO.class);
    }
}
