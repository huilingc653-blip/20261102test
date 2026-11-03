package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolBailDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDtlVO;
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
 * @Title: AbPoolBailDtlDao
 * @Description: 保证金管理DAO
 * @author wangzy
 * @date 2020/1/10 18:10
 *
 */
@Repository
public class AbPoolBailDtlDao  extends BaseDao<AbPoolBailDtl, String> {
    /**
     * 通用查询保证金数据sql
     *
     * @return
     */
    public StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select dtl.id as id,pb.BANK_ACC as bankAcc,BT_CONCAT4('(',pc.corp_code,')',pc.corp_name) as corpCodeAndName,dtl.MONEY as money,");
        sql.append(" BT_CONCAT4('(',p.BP_CODE,')',p.BP_NAME) as pbCodeAndName,dtl.APP_DATE as appDate,dtl.OPP_ACC as oppAcc, ");
        sql.append(" dtl.STATUS as status,dtl.OPT_STATUS as optStatus,dtl.opp_acc_name as oppAccName,dtl.bail_money as bailMoney, ");
        sql.append(" dtl.opp_bank_no as oppBankNo,dtl.opp_bank_name as oppBankName,pb.bail_date as bailDate,dtl.RETURN_MSG as returnMsg ");
        sql.append(" from AB_POOL_BAIL_DTL dtl left join AB_POOL_BAIL pb on dtl.BAIL_ID=pb.id ");
        sql.append(" left join ab_pool_corp pc on pc.corp_id=pb.corp_id and pc.valid_Sign=:validSign ");
        // sql.append(" left join sys_corp s on s.id=pc.corp_id  ");
        sql.append(" left join AB_POOL p on p.id=pb.BP_ID  ");
        sql.append(" where pb.VALID_SIGN=:validSign and dtl.status<>-2 ");
        return sql;
    }
    /**
     * 保证金存入列表查询
     * @param type 类型
     * @param input
     * @return 列表信息
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbPoolBailDtlVO> getDataTablesUnion(String type, DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String etime = (String)input.getFormdata().get("led_appDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_appDate", etime);
        }
        StringBuilder sql = this.getCommonSql();
        if(StringUtils.isNotEmpty(type)){
            sql.append(" and dtl.type=:type ");
            params.put("type", type);
        }
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "pb.corp_Id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return getDynamicBySQL(input, sql.toString(), AbPoolBailDtlVO.class, "", false, params);
    }
    /**
     * 保证金存入列表查询
     * @param corpId 单位ID
     * @param bpId 票据池ID
     * @return 保证金信息
     */
    @SuppressWarnings("rawtypes")
    public AbPoolBailDtlVO getBailInfo(String corpId, String bpId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select id as id,BANK_ACC as bankAcc,BAIL_MONEY as bailMoney from AB_POOL_BAIL ");
        sql.append(" where corp_Id=:corpId and BP_ID=:bpId and VALID_SIGN=:validSign ");
        params.put("corpId", corpId);
        params.put("bpId", bpId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolBailDtlVO.class);
    }
    /**
     * 保证金存取信息查询
     * @param id 流水ID
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolBailDtlVO getAbPoolBailDtlInfo(String id) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = this.getCommonSql();
        sql.append(" and dtl.id=:id ");
        params.put("id", id);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return getBySQLAliasToBean(sql.toString(), params, AbPoolBailDtlVO.class);
    }
    /**
     * 加载存入审批列表数据
     *
     * @param dataTablesInput 查询条件
     * @param businessList 审批中流水
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput, List<String> businessList, String type) {
        // 设置结束时间
        String etime = (String)dataTablesInput.getFormdata().get("led_appDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_appDate", etime);
        }
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = this.getCommonSql();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "dtl.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        if(StringUtils.isNotEmpty(type)){
            sql.append(" and dtl.type=:type ");
            params.put("type", type);
        }
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolBailDtlVO.class, "", false,params);
        return dataOutInfo;
    }
    
	 /**
    * 根据单位id获取此单位中是否存在保证金
    * @param corpId
    * @return 有效用户笔数
    */
   public int getValidBailNumByCorpId(String corpId){
       String sql="select count(*) from AB_POOL_BAIL where 1=1  and corp_id=:corpId";
       Map<String,Object> params=new HashMap<String,Object>();
       params.put("corpId",corpId);
       return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
   }
    /**
     * 根据单位id获取此单位下有没有保证金的数据
     * @param corpId
     * @return 存在保证金数据
     */
    public int getPoolBailDtlByCorpId(String corpId){
        String sql="select count(*) from ab_pool_bail where corp_id=:corpId and VALID_SIGN=:validSign";
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
}
