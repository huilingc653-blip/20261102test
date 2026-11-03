package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpBankAcc;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fusb
 * @version 1.0
 * @Title: FexCorpBankAccDao
 * @Description: 主体单位默认银行账户Dao
 * @date 2020-02-17 15:20:33
 */
@Repository
public class FexCorpBankAccDao extends BaseDao<FexCorpBankAcc,String> {
    /**
     * 查询sql
     * @return
     */
    public String getSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,");
        sql.append("       b.bus_type as busType,");
        sql.append("       b.corp_id as corpId,");
        sql.append("       d.name as corpName,");
        sql.append("       b.tran_type as tranType,");
        sql.append("       b.pro_id as proId,");
        sql.append("       c.pro_name as proName,");
        sql.append("       b.opp_corp_id as oppCorpId,");
        sql.append("       b.opp_corp_id as oppId,");
        sql.append("       b.cd_sign as cdSign,");
        sql.append("       b.cash_flow_type as cashFlowType,");
        sql.append("       b.cur_id as curId,");
        sql.append("       b.status as status,");
        sql.append("       b.create_by as createBy,");
        sql.append("       b.create_date as createDate,");
        sql.append("       b.update_by as updateBy,");
        sql.append("       b.update_date as updateDate");
        return sql.toString();
    }

    /**
     * 查询主体单位默认银行账户列表
     * @param dataTablesInput
     * @param corpAuth
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth,String busType) {
        return getDataTablesUnion(dataTablesInput, corpAuth, busType, null);
    }

    /**
     * 查询主体单位默认银行账户列表。
     * 交易对手信息归属 bytter-system，当前 DAO 只按外部单位 ID 过滤，不直接关联 sys_external_corp。
     * @param externalCorpIds null 表示不按交易对手过滤，空集合表示名称筛选无结果
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth,String busType,
                                               List<String> externalCorpIds) {
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_corp_bankacc b ");
        sql.append("       left join fex_product c on b.pro_id=c.id left join sys_corp d ");
        sql.append("       on d.id=b.corp_id where 1=1 and b.status in(-1,0,11,95,-3) " );
        sql.append("       and b.bus_type="+busType);
        if(corpAuth!=null && corpAuth.size()>0){
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "b.corp_id "));
        }
        appendExternalCorpFilter(sql, externalCorpIds);
        removeExternalCorpNameFilter(dataTablesInput);
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), FexCorpBankAccVO.class, "", false);
    }

    /**
     * 查询主体单位默认银行账户审批列表
     * @param dataTablesInput
     * @param ids
     * @return
     */
    public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput, String  ids,String busType,List<String> corpIds){
        return getApproveDataTables(dataTablesInput, ids, busType, corpIds, null);
    }

    /**
     * 查询主体单位默认银行账户审批列表。
     * 交易对手名称由 service 通过 system-client 补齐，这里只使用外部单位 ID 做本服务表过滤。
     * @param externalCorpIds null 表示不按交易对手过滤，空集合表示名称筛选无结果
     */
    public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput, String  ids,String busType,List<String> corpIds,
                                                 List<String> externalCorpIds){
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_corp_bankacc b ");
        sql.append("       left join fex_product c on b.pro_id=c.id left join sys_corp d ");
        sql.append("       on d.id=b.corp_id where 1=1" );
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and b.status="+ ForeignConstant.STATUE_WAIT_CHECK );
        }
        sql.append("       and b.bus_type="+busType);
        if(StringUtils.isNoneBlank(ids)){
            sql.append(" and ").append(ids);
        }
        if(corpIds!=null && corpIds.size()>0){
            sql.append(" and ").append("(" + getSQLIn(corpIds, 900, "b.corp_id ") + ")");
        }
        appendExternalCorpFilter(sql, externalCorpIds);
        removeExternalCorpNameFilter(dataTablesInput);
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), FexCorpBankAccVO.class, "", false);
    }

    /**
     * 通过id查询交易对手银行账户信息
     * @param id
     * @return
     */
    public FexCorpBankAccVO getFexCorpBankAcc(String id){
        StringBuilder sql = new StringBuilder(this.getSql());
        sql.append("       from fex_corp_bankacc b ");
        sql.append("       left join fex_product c on b.pro_id=c.id left join sys_corp d ");
        sql.append("       on d.id=b.corp_id where b.id=:id " );
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        return this.getBySQLAliasToBean(sql.toString(),params,FexCorpBankAccVO.class);
    }

    /**
     * 删除交易对手银行账户信息
     * @param id
     * @return
     */
    public void deleteFexCorpBankAcc (String id){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from fex_corp_bankacc where id=:id");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 修改主体单位默认银行账户状态
     * @param id
     * @param status
     */
    public void updateFexCorpBankAccSatus(String id,Integer status){
        StringBuilder sql = new StringBuilder();
        sql.append("update fex_corp_bankacc a set a.status=:status where a.id=:id ");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        params.put("status",status);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 获取主体单位默认银行账户下拉框数据
     * @param busType
     * @return
     */
    public List<FexCorpBankAccVO> getSelectFexCorpBankAcc(String corpId,String busType){
         Map<String,Object> params = new HashMap<String,Object>();
         StringBuilder sql = new StringBuilder(this.getSql());
         sql.append("       from fex_corp_bankacc b ");
         sql.append("       left join fex_product c on b.pro_id=c.id left join sys_corp d ");
         sql.append("       on d.id=b.corp_id where 1=1 and b.status=95 " );
         if (StringUtils.isNotEmpty(corpId)){
             sql.append("   and b.corp_id=:corpId");
             params.put("corpId",corpId);
         }
         sql.append("       and b.bus_type=:busType");
         params.put("busType",busType);
         return this.listBySQLAliasToBean(sql.toString(),params,FexCorpBankAccVO.class);
    }

    /**
     *根据单位id，别种，银行类别查询主体单位默认银行信息
     *@Param corpId
     *@Param curCode
     *@Param bankTypeId
     *@return
     */
    public List<FexCorpBankAccVO> getFexCorpBankAccList(String corpId,String curId){
        StringBuilder sql = new StringBuilder(this.getSql());
        Map<String,String> params = new HashMap<String,String>();
        sql.append("       from fex_corp_bankacc b ");
        sql.append("       left join fex_product c on b.pro_id=c.id left join sys_corp d ");
        sql.append("       on d.id=b.corp_id where b.bus_type="+ForeignConstant.FEX_BUS_TYPE_CORP );
        sql.append("       and b.status=95 ");
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append("   and b.corp_id=:corpId ");
            params.put("corpId",corpId);
        }
        if (StringUtils.isNotEmpty(curId)){
            sql.append("   and b.cur_id=:curId ");
            params.put("curId",curId);
        }
        return this.listBySQLAliasToBean(sql.toString(),params,FexCorpBankAccVO.class);
    }

    /**
     * 拼接交易对手过滤条件；空集合需要返回空结果，避免名称查询无匹配时退化为不过滤。
     */
    private void appendExternalCorpFilter(StringBuilder sql, List<String> externalCorpIds) {
        if (externalCorpIds == null) {
            return;
        }
        if (externalCorpIds.isEmpty()) {
            sql.append(" and 1=0 ");
            return;
        }
        sql.append(" and ").append(getSQLIn(externalCorpIds, 900, "b.opp_corp_id "));
    }

    /**
     * 移除交易对手名称的 SQL 动态条件和排序。
     * 名称来自 bytter-system 远程补充，payment 本地 SQL 无法直接过滤或排序该字段。
     */
    private void removeExternalCorpNameFilter(DataTablesInput dataTablesInput) {
        if (dataTablesInput != null && dataTablesInput.getFormdata() != null) {
            dataTablesInput.getFormdata().remove("likes_oppCorpName");
        }
        if (dataTablesInput == null || dataTablesInput.getColumns() == null) {
            return;
        }
        for (Column column : dataTablesInput.getColumns()) {
            if (column == null) {
                continue;
            }
            if ("oppCorpName".equals(column.getName()) || "oppCorpName".equals(column.getData())
                    || "cotpNameEn".equals(column.getName()) || "cotpNameEn".equals(column.getData())) {
                column.setOrderable(false);
            }
        }
    }
}
