package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.utils.AbPoolUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 业务检查Dao
 * @Description: 业务检查处理类（检查池接口、票据池、池单位、质权人等是否有在途业务数据）
 * @author: zhenghj
 * @date: 2020-2-24
 */
@Repository
public class AbPoolCheckDao extends BaseDao<AbPool, String> {
    /**
     *根据id查询各业务是否存在在途数据
     * @param tableName 查询表名
     * @param type 查询类型 1池接口 2票据池 3池单位  4质权人
     * @param queryField 查询字段
     * @param id 查询id
     * @param checkType 查询在途类型 ：0=审批，1指令
     * @param businessType 业务类型
     * @param briefId 业务id
     * @return
     */
    public boolean checkQuery(String tableName,Integer type,String queryField,String id,Integer checkType,Integer businessType,String briefId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select t.id");
        sql.append(" FROM ").append(tableName).append(" t ");
        StringBuilder sqlWhere = new StringBuilder(" ");
        //查池接口
        if(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_1 == type){
            sql.append(" left join AB_POOL ap on ap.id=t.BP_ID ");
            sqlWhere.append(" and ap.PIF_ID=:pifId ");
            params.put("pifId",id);
        }else
        //查质权人
        if(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_4 == type){
            sql.append(" left join AB_POOL_PLEDGEE pp on pp.BP_ID=t.BP_ID ");
            sqlWhere.append(" and pp.ID=:pId ");
            params.put("pId",id);
        }else
        //池单位且票据池明细表
        if(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_3 == type && "AB_POOL_DTL".equals(tableName)){
            sql.append(" left join AB_VOUCHERINFO_VICE vv on t.vv_id=vv.id  ");
            sql.append(" left join ab_pool_corp apc on vv.corp_id = apc.corp_id and APC.valid_sign=1 ");
            sqlWhere.append(" and apc.CORP_ID=:corpId ");
            params.put("corpId",id);
        }else{
            //查询字段所对应的id
            sqlWhere.append(" and "+queryField+"=:"+queryField);
            params.put(queryField,id);
        }
        sql.append(" where  1=1 ");
        //审批
        if(AbPoolConstant.AB_POOL_CHECK_TYPE_0==checkType){
            sqlWhere.append(" and t.status in (:status) ");
            params.put("status", AbPoolUtils.getStatus(businessType));
        //指令
        }else if(AbPoolConstant.AB_POOL_CHECK_TYPE_1==checkType){
            sqlWhere.append(" and t.opt_status in (:optStatus) ");
            params.put("optStatus",AbPoolUtils.getOptStatus(businessType));
        }
        //业务id不为空时，查询票据池明细表业务
        if(StringUtils.isNotEmpty(briefId)){
            sqlWhere.append(" and t.BRIEF_ID=:briefId ");
            params.put("briefId",briefId);
        }
        //当查票据池明细表时
        if("AB_POOL_DTL".equals(tableName)){
            sqlWhere.append(" and t.VALID_SIGN<>:validSign ");
            params.put("validSign",AbPoolConstant.AB_POLL_VALID_SIGN_2);
            //当业务id为空时为查电票签收业务
            if(StringUtils.isEmpty(briefId)){
                sqlWhere.append(" and t.BRIEF_ID is null ");
            }
        }
        sql.append(sqlWhere);
        List list = createSQLQuery(sql.toString(), params).list();
        if(list!=null&&!list.isEmpty()){
            return true;
        }
        return false;
    }
    /**
     * 根据池接口id查询保证金是否存在在途数据
     * @param type 查询类型 1池接口 2票据池 3池单位  4质权人
     * @param queryField 查询字段
     * @param id 池接口id
     * @param checkType 查询在途类型 ：0=审批，1指令
     * @param bailType 存取类型:1：存入， 2：取出
     * @return
     */
    public boolean checkBailQuery(Integer type,String queryField,String id,Integer checkType,String bailType){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select bd.id  ");
        sql.append(" FROM AB_POOL_BAIL_DTL bd ");
        sql.append(" left join AB_POOL_BAIL b on b.id=bd.BAIL_ID ");
        sql.append(" left join AB_POOL ap on ap.id=b.BP_ID ");
        sql.append(" where  1=1 ");
        if(AbPoolConstant.AB_POOL_CHECK_TYPE_0==checkType){
            sql.append(" and bd.status in (:status) ");
            params.put("status",new Integer[]{AbPoolConstant.AB_POOL_STATUS11});
        }else if(AbPoolConstant.AB_POOL_CHECK_TYPE_1==checkType){
            sql.append(" and bd.opt_status in (:optStatus) ");
            params.put("optStatus",new String[]{AbPoolConstant.AB_POOL_OPT_STATUS16,AbPoolConstant.AB_POOL_OPT_STATUS18});
        }
        sql.append(" and bd.type=:type");
        params.put("type",bailType);
        //查询池接口
        if(AbPoolConstant.AB_POOL_CHECK_QUERYTYPE_1 == type){
            sql.append(" and ap.PIF_ID=:pifId");
            params.put("pifId",id);
        }else{
            //查询字段所对应的id
            sql.append(" and "+queryField+"=:"+queryField);
            params.put(queryField,id);
        }
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        if(list!=null&&!list.isEmpty()){
            return true;
        }
        return false;
    }
}
