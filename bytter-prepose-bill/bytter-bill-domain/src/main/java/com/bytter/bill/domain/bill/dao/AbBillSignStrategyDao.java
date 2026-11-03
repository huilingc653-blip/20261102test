package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillSignStrategy;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbBillSignStrategyDao
 * @Description: 电票自动签收配置Dao
 * @author wangzy
 * @date 2020/7/13 16:23
 *
 */
@Repository
public class AbBillSignStrategyDao extends BaseDao<AbBillSignStrategy,String> {
    private StringBuilder initSql(){
        return new StringBuilder().append("select ")
                .append(" ss.id as id,ss.CORP_ID as corpId,ss.CODE as code,ss.name as name,ss.BILL_TYPE as billType,ss.SIGN_TYPE as signType,")
                .append(" ss.valid_Sign as validSign,BT_CONCAT4('(',ss.corp_code,')',ss.corp_name) as corpCodeAndName,")
                .append(" ss.BANKENDRSMTMK as bankendrsmtmk,ss.BEGIN_NUM as beginNum,ss.END_NUM as endNum,")
                .append(" ss.BEGIN_OPERATOR as beginOperator,END_OPERATOR as endOperator,")
                .append(" ss.NUM_OPERATOR as numOperator")
                .append(" from AB_BILL_SIGN_STRATEGY ss ")
                .append(" WHERE 1=1 and ss.valid_sign in (:validSign)");
    }
    /**
     * 电票自动签收配置列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(this.initSql());
        if(StringUtils.isNotBlank((String)input.getFormdata().get("likes_signType"))){
            sql.append(" and (ss.SIGN_TYPE like BT_CONCAT3('%',:signType,'%') ");
            sql.append(" or BT_INSTR(:signType, ss.SIGN_TYPE)>0) ");
            params.put("signType", input.getFormdata().get("likes_signType"));
            input.getFormdata().remove("likes_signType");
        }
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "ss.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQLForBill(input, sql.toString(), AbBillSignStrategyVO.class, "", false,params);
    }
    
    public AbBillSignStrategyVO getSignStrategyVO(String id){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(this.initSql());
        sql.append(" and ss.id =:ssId");
        params.put("ssId",id);
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1, AccebillConstant.AB_BILL_VALID_SIGN_0});
        List<AbBillSignStrategyVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillSignStrategyVO.class);
        return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
    }
    
    public int updateValidSign(Object ids){
    	Map<String, Object> params = new HashMap<String, Object>();
    	String hql = " update AbBillSignStrategy set validSign =:validSign where id in (:id)";
    	params.put("id",ids);
    	params.put("validSign",AccebillConstant.AB_BILL_OPT_STATUS_2);
    	return this.batchExecute(hql, params);
    }
    /**
     * 获取全部有效的策略数据
     * zhengg 20200721
     * @return 策略数据信息
     */
    public List<AbBillSignStrategyVO> getAllValidBillSignStrategy(){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(this.initSql());
        params.put("validSign", new String[]{AccebillConstant.AB_BILL_VALID_SIGN_1});
        return this.listBySQLAliasToBean(sql.toString(),params,AbBillSignStrategyVO.class);
    }
}
