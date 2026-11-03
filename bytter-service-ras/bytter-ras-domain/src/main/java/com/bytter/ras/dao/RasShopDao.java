package com.bytter.ras.dao;

import com.bytter.ras.entity.RasShop;
import com.bytter.ras.entity.RasShopGrant;
import com.bytter.ras.vo.RasShopVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核门店维护dao类
 * @date 2019/5/6 8:52
 */
@Repository
public class RasShopDao extends BaseDao<RasShop,String> {
    /**
     * 加载门店列表
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuffer sql = new StringBuffer();
        sql.append("select rs.id as id,rs.code,rs.name,rs.corp_id as corpId,sc.name as corpName,sc.code as corpCode,rs.shop_type as shopType,");
        sql.append("rs.audit_start_date as asDate,rs.end_check_money as ecMoney,rs.IS_CORP as isCorp,rs.shop_no as shopNo,rs.shop_name as ");
        sql.append("shopName,rs.rmk,rs.valid_sign as vSign from ras_shop rs left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        Map<String,Object> params=new HashMap<>();
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rs.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), RasShopVO.class, "rs", false,params);
    }

    /**
     * 门店授权加载门店列表
     */
    public DataTablesOutput getDataTablesUnionShopGrant(DataTablesInput dataTablesInput) {
        StringBuffer sb = new StringBuffer("select rs.id as id,rs.code,rs.name,rs.corp_id as corpId,sc.code as corpCode,sc.name as corpName,rs.shop_type as shopType," +
                "rs.audit_start_date as asDate,rs.end_check_money as ecMoney,rs.shop_no as shopNo,rs.shop_name as " +
                "shopName,rs.rmk,rs.valid_sign as vSign from ras_shop rs left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        Map<String,Object> params=new HashMap<>();
        //门店名称支持门店编码和门店名称like查询
        if(StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("likes_name"))){
            sb.append(" and (rs.name like :name or rs.code like :name) ");
            params.put("name","%" + dataTablesInput.getFormdata().get("likes_name") + "%");
            dataTablesInput.getFormdata ().remove ("likes_name");
        }
        return getDynamicBySQL(dataTablesInput, sb.toString(), RasShopVO.class, "rs", false,params);
    }

    /**
     * 根据门户编码是否存在
     * @param code
     * @return
     */
    public String checkShopByCorpAndCode(String code) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", code);
        List<RasShop> list = listByHQL("from RasShop where code=:code", params);
        if(list.size()>0){
            return list.get(0).getId();
        }
        return "";
    }

    /**
     * 根据id查找门店信息
     */
    public RasShopVO getByIdUnion(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append("select rs.id as id,rs.code,rs.name,rs.corp_id as corpId,sc.name as corpName,sc.code as corpCode,rs.shop_type as shopType,");
        sql.append("rs.audit_start_date as asDate,rs.end_check_money as ecMoney,rs.shop_no as shopNo,rs.shop_name as ");
        sql.append("shopName,rs.rmk,rs.valid_sign as vSign,extemal_no as extemalNo,is_corp as isCorp ");
        sql.append("from ras_shop rs left join sys_corp sc on rs.corp_id=sc.id where 1=1 and rs.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, RasShopVO.class);
    }

    /**
     * 根据门店名称和单位id获取门店信息
     * @param name
     * @param corpId
     * @return
     */
    public RasShop getRasShopByName(String name,String corpId) {
        String hql = "from RasShop where vSign = 1 and name = ?0 and corpId=?1";
        Query query = createQuery(hql, name,corpId);
        Object rasShop = query.uniqueResult();
        if (rasShop == null) {
            return null;
        }
        return (RasShop) rasShop;
    }

    /**
     * 根据门店编码和单位id获取门店信息
     * @param code
     * @param corpId
     * @return
     */
    public RasShop getRasShopByCode(String code,String corpId) {
        String hql = "from RasShop where  code = ?0 and corpId=?1";
        Query query = createQuery(hql, code,corpId);
        Object rasShop = query.uniqueResult();
        if (rasShop == null) {
            return null;
        }
        return (RasShop) rasShop;
    }
    
    /**
     * 根据门店名称获取门店信息
     * 控制重复数据
     * @return
     */
    public RasShop getRasInfoByShopName(String shopName) {
    	StringBuilder hql = new StringBuilder("from RasShop where 1=1 and (name=:shopName or code=:shopCode)");
    	Map<String,Object> params = new HashMap<>();
    	params.put("shopName", shopName);
    	params.put("shopCode", shopName);
    	hql.append(" order by updateDate desc");
    	@SuppressWarnings("unchecked")
		List<RasShop> listRasShop = this.listByHQL(hql.toString(), params);
    	if(null!=listRasShop &&listRasShop.size()>0){
    		return listRasShop.get(0);
    	}
        return null;
    }

    /**
     * 根据参数获取门店数据
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<RasShop> getRasShopByParams(Map<String,Object> params){
    	StringBuilder hql=new StringBuilder("from RasShop where 1=1 ");
    	if(params.containsKey("id")){
    		hql.append(" and id=:id ");
    	}
    	if(params.containsKey("ids")){
    		hql.append(" and id in (:ids) ");
    	}
    	if(params.containsKey("wValidSign")){
    		hql.append(" and vSign=:wValidSign ");
    	}
    	hql.append(" order by code asc ");
    	return this.listByHQL(hql.toString(), params);
    }

    /**
     * 校验该门店是否存在待稽核的数据
     */
    public boolean checkShopNotAudit(String ids) {
        StringBuilder sql = new StringBuilder();
        //营收
        sql.append("select shop_id from RAS_REVENUE_DTL nue where nue.shop_id in(:shopId) and nue.status=:status union all ");
        sql.append("select shop_id from RAS_RECEIVED_DTL ved where ved.shop_id in(:shopId) and ved.status=:status");
        HashMap<String, Object> params = new HashMap<>();
        params.put("shopId", ids.split(","));//门店ids
        params.put("status", RasConstant.RAS_AUDIT_STATUS_1);//待稽核
        List<Map<String,Object>> list = listBySQL(sql.toString(), params);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 校验是否单位级别门店（一个单位只能存在一个单位级别门店）
     * @param shopVO
     * @return 门店id
     */
    public String checkShopByIsCorp(RasShopVO shopVO) {
        Map<String,Object> params = new HashMap<>();
        params.put("isCorp",shopVO.getIsCorp());
        params.put("corpId",shopVO.getCorpId());
        List<RasShop> list = listByHQL("from RasShop where isCorp=:isCorp and corpId=:corpId", params);
        if(list.size()>0){
            return list.get(0).getId();
        }
        return "";
    }

    public List<String> getCacheDatas(String table,String clounm,String where) {
        StringBuilder sql = new StringBuilder("select "+clounm+" from "+table+" where 1=1");
        if(StringUtils.isNotEmpty(where)){
            sql.append(" and "+where+"");
        }
        SQLQuery query = createSQLQuery(sql.toString());
        List<String> corpList = query.list();
        if (corpList == null) {
            return new ArrayList<>();
        }
        return corpList;
    }
    
    public List<RasShopVO> hisRasShop(Map<String, String> params){
        StringBuilder hql = new StringBuilder();
        hql.append(" select rs.id as id, ");
        hql.append(" rs.code as code,");
        hql.append(" rs.name as name,");
        hql.append(" rs.valid_sign as vSign,");
        hql.append(" rsg.can_audit as isAudit from RAS_SHOP_GRANT rsg");
        hql.append(" left join ras_shop rs on rs.id=rsg.shop_id");
        hql.append(" where rsg.user_id =:userId");
    	return listBySQLAliasToBean(hql.toString(), params, RasShopVO.class);
    }
}
