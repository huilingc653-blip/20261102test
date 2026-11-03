package com.bytter.biz.fundwarn.dao;
import com.bytter.biz.fundwarn.entity.BtWarnStrategy;
import com.bytter.biz.fundwarn.vo.BtWarnStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 预警策略数据操作类
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Repository
public class BtWarnStrategyDao extends BaseDao<BtWarnStrategy,String> {
    /**
     * 加载数预警数据
     *
     * @param dataTablesInput 查询条件
     * @param corpAuthList 查询权限特殊管理单位
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BtWarnStrategyVO> listWarnStrategy(DataTablesInput dataTablesInput, List<String> corpAuthList, String tenantId) {
        StringBuilder sql = new StringBuilder("select s.id,s.net_id as netId,s.code,s.name,s.cur_id as curId,s.type_code as typeCode,s.dimension_code as dimensionCode," +
                "s.status,s.begin_num as beginNum,s.end_num as endNum,s.begin_operator as beginOperator,s.end_operator as endOperator,s.num_operator " +
                "as numOperator,s.task_id as taskId,s.task_cron as taskCron,s.forward_time as forwardTime,s.run_date as runDate," +
                "s.create_name as createName,s.create_by as createBy,s.create_date as createDate,s.update_name as updateName," +
                "s.update_by as NupdateBy,s.update_date as updateDate,s.warn_level as warnLevel,  ");
        sql.append(" sc.name as netName,bc.cur_name as curName ");
        sql.append(" from bt_warn_strategy s ");
        sql.append(" left join sys_corp sc on sc.id = s.net_id ");
        sql.append(" left join bt_currency bc  on bc.id = s.cur_id  ");
        sql.append(" where 1=1");
        //1代表从“逾期账款预警设置“页面查询而来
        if ("1".equals(dataTablesInput.getFormdata().get("overdueWarn"))) {
            sql.append(" and s.type_code in ('exReceivaOver','timeWarnOver') "); //逾期账款超标,逾期时间预警
            sql.append(" and s.dimension_code in ('corp','bsCusCreditRating') "); //单位,客商信用等级
        }
        Map<String, Object> params = new HashMap<String, Object>();
//        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "s.net_id", params);
//        sql.append(" and ").append(corpAuthSql).append(" ");
        sql.append(" and s.tenant_id=:tenantId");
        params.put("tenantId",tenantId);
        DataTablesOutput<BtWarnStrategyVO> dataOutInfo = getDynamicBySQL(dataTablesInput, sql.toString(), BtWarnStrategyVO.class,"",false,params);
        return dataOutInfo;
    }

    /**
     * 根据单位ID集合查询数据
     *
     * @param dataTablesInput
     * @return
     * @author zhenghj 2018-10-12
     */
    public DataTablesOutput<CorpDTO> getDataTablesCorpByCorpIds(DataTablesInput dataTablesInput, List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id,c.net_Id as netId,s.name as netName,c.code, c.name from sys_corp c left join sys_corp s on c.net_id=s.id where 1 = 1  ");
        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "c.id", true));
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("netIdStr"))){
            String[] netIds = dataTablesInput.getFormdata().get("netIdStr").toString().split(",");
            String netIdStrInSql = SqlUtil.getInSql(netIds, "c.net_id", true);
            String[] ids = dataTablesInput.getFormdata().get("netIdStr").toString().split(",");
            String idStrInSql = SqlUtil.getInSql(ids, "c.id", true);
            sql.append(" and ( ").append(netIdStrInSql).append(" or ").append(idStrInSql).append(" ) ");
        }
        sql.append(" and c.status = :status order by c.sort, c.code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        dataTablesInput.setFormdata(null);
        DataTablesOutput<CorpDTO> dataOutInfo = getDynamicBySQL(dataTablesInput, sql.toString(), CorpDTO.class,"",false,params);
        return dataOutInfo;
    }

    /**
     * 根据条件查询没有删除且是有效的银行账户
     * @param dataTablesInput
     * @param bankacc
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getNotDeleteAndValidAndBankAcc(DataTablesInput dataTablesInput, List<String> bankacc) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,sy.name as corpName, acccur.cur_id,");
        sql.append(" acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,cur.cur_name as curName ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
        sql.append(" left join sys_corp sy on sy.id=acc.corp_id");
        sql.append(" left join bt_currency cur on cur.id = acccur.cur_id ");
        sql.append(" where 1=1 and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
        sql.append(" and acc.valid_sign="+ BtBankAccConstant.VALIDSIGN_1+" and sy.status = :corpStatus  ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
        if(!EmptyUtils.isEmptyList(bankacc)){
            sql.append(" and ").append(SqlUtil.getInSql(bankacc, "acc.bank_acc", true));
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("netIdStr"))){
            sql.append(" and (sy.net_id='").append(dataTablesInput.getFormdata().get("netIdStr")).append("' or sy.id='")
                    .append(dataTablesInput.getFormdata().get("netIdStr")).append("') ");
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId"))){
            sql.append(" and acccur.cur_id='").append(dataTablesInput.getFormdata().get("curId")).append("' ");
        }
        dataTablesInput.setFormdata(null);
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccDTO.class, "", false, params);
    }

    /**
     * 根据内部账号和币别查询状态正常的内部账号数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcAccDTO> getblnctrAccByAccAndcurId(DataTablesInput dataTablesInput, List<String> acc) {

        StringBuilder sql = new StringBuilder("select t.id as id,");
        sql.append("t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,sy.name as corpName,t.cur_Id as curId,cur.cur_name as curName,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t ");
        sql.append(" left join sys_corp sy on sy.id=t.corp_id ");
        sql.append(" left join BT_CURRENCY cur on cur.id = t.cur_Id ");
        sql.append(" where sy.status = :corpStatus ");
        sql.append(" and ( t.acc_State = 'N' or t.acc_State = 'P' or t.acc_State = 'S' ) ");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpStatus", CorpConstant.CORP_STATUS_ENABLE);
        if(!EmptyUtils.isEmptyList(acc)){
            sql.append(" and ").append(SqlUtil.getInSql(acc, "t.blnctr_Acc", true));
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("netIdStr"))){
            sql.append(" and (t.net_id='").append(dataTablesInput.getFormdata().get("netIdStr")).append("' or t.corp_id='")
                    .append(dataTablesInput.getFormdata().get("netIdStr")).append("') ");
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId"))){
            sql.append(" and t.cur_Id='").append(dataTablesInput.getFormdata().get("curId")).append("' ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccDTO.class, "t", false,null, params);
    }


    /**
     * 策略代码 重复验证
     * @param warnCode
     * @return 存在数据返回true否则false
     */
    public boolean checkWarnCode(String warnCode,String tenantId){
        StringBuilder sql=new StringBuilder("select s.code as code from BT_WARN_STRATEGY s where s.code=:code and s.tenant_id =:tenantId ");
        Map<String,String> params=new HashMap<String,String>();
        params.put("code", warnCode);
        params.put("tenantId", tenantId);

        List<BtWarnStrategy> list=this.listBySQLAliasToBean(sql.toString(), params, BtWarnStrategy.class);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }


    /**
     * 根据单位id获取此单位有资金预警数据，不允许删除！
     * @param corpId
     * @return 有效笔数
     */
    public int getValidFundWranNumByCorpId(String corpId){
        String sql="select count(*) from bt_warn_data P where P.CORP_ID=:corpId";
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }

    public String getTenantCodeById(String id){
        String sql="select tenant_code as tenantCode from sys_tenant where id=:id";
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("id",id);
        Map<String,String> tenantCodeMap = getBySQL(sql, params);
        return tenantCodeMap.get("tenantCode");
    }


    /**
     * 获取有效租户的资金预警数据(定时任务类)
     * @return 有效资金预警数据
     */
    public List<BtWarnStrategy> getValidBtWarnStrategy() {
        StringBuilder sql = new StringBuilder("select a.* from bt_warn_strategy a ,sys_tenant b ");
        sql.append(" where a.tenant_id = b.id and b.status=1 and a.status=1 ");
        sql.append(" and a.type_code not in ('eqCorpPay','bankAccPay','cashPay','daySumPay') ");
        List<BtWarnStrategy> list=this.listBySQLAliasToBean(sql.toString(), null, BtWarnStrategy.class);
        return list;
    }

    /**
     * 获取第一个结算中心
     * @param tenantId
     * @return
     */
    public String getFirstNetId(String tenantId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        //登录名C00001由柜面签约而来，且永远不会变
        sql.append(" select CORP_ID  from sys_user where LOGIN_NAME = 'C00001' and TENANT_ID = :tenantId ");
        map.put("tenantId", tenantId);
        Query query = createSQLQuery(sql.toString(), map);
        if (!EmptyUtils.isEmpty(query.list())) {
            List<String> list = query.list();
            return list.get(0); //实际只会取第一个结算中心
        } else {
            return null;
        }
    }

    /**
     * 获取全部单位
     * @param tenantId
     * @return
     */
    public String getAllCorpId(String tenantId){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append("select id from sys_corp");
        sql.append(" where 1 = 1 ");
        sql.append(" and status = 1 ");
        sql.append(" and tenant_id=:tenantId ");
        map.put("tenantId", tenantId);
        Query query = createSQLQuery(sql.toString(), map);
        if (!EmptyUtils.isEmpty(query.list())) {
            String allCorpIds = "";
            List<String> list = query.list();
            for (String s : list) {
                allCorpIds += s + ",";
            }
            if (allCorpIds.endsWith(",")) {
                allCorpIds = allCorpIds.substring(0,allCorpIds.length()-1);
            }
            return allCorpIds;
        } else {
            return null;
        }
    }

    /**
     * 根据code查预警策略
     * @param code
     * @return
     */
    public BtWarnStrategy getWSByCode(String code, String tenantId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" SELECT ID, NET_ID, CODE, NAME, CUR_ID, TYPE_CODE, DIMENSION_CODE, STATUS, BEGIN_NUM, END_NUM, BEGIN_OPERATOR, END_OPERATOR, NUM_OPERATOR, TASK_ID, TASK_CRON, FORWARD_TIME, RUN_DATE, ");
        sql.append(" CREATE_NAME, CREATE_BY, CREATE_DATE, UPDATE_NAME, UPDATE_BY, UPDATE_DATE, WARN_LEVEL, TENANT_ID ");
        sql.append(" from BT_WARN_STRATEGY ");
        sql.append(" where 1=1 ");
        sql.append(" and code=:code");
        sql.append(" and tenant_id=:tenantId");
        params.put("code",code);
        params.put("tenantId",tenantId);

        List<BtWarnStrategy> list=this.listBySQLAliasToBean(sql.toString(), params,BtWarnStrategy.class);
        if(!ListUtils.isNullOrEmpty(list)){
            return list.get(0); //理论上应该只存在一条
        }
        return null;
    }
}
