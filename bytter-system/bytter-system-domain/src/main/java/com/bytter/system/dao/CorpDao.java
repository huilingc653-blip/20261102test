package com.bytter.system.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.bean.FexCorpExposureVO;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.SysBlackWhiteConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.ErpBasicDataVOVO;
import com.bytter.system.constants.RationConstant;
import com.bytter.system.entity.Corp;
import com.bytter.system.vo.CorpAndBtBankAccVO;
import com.bytter.system.vo.CorpComponentVO;
import com.bytter.system.vo.CorpSimpleVO;
import com.bytter.system.vo.CorpVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.util.*;

import static com.bytter.system.api.constant.CorpConstant.CORP_TYPE_CODE_CORP;
import static com.bytter.system.api.constant.CorpConstant.CORP_TYPE_CODE_NET;
/**
 * 网点/单位Dao
 *
 * @author zhoul
 * @date 2017/4/13
 */
@Repository
public class CorpDao extends BaseDao<Corp, String> {

    private static final String corpSql = "select id,account_licence,address,city,code,collect_flag,ctl_budget,head_person,listed_company,\n" +
            "name,parent_id,parent_ids,province,sort,status,type,use_account_code,net_id,rat_group,create_time,\n" +
            "create_by,update_time,update_by,is_limit_quota,day_limit_quota,month_limit_quota,name_en,address_en,\n" +
            "cur_id,unit_attribute,unit_cm,country_id,cert_type,cert_no,soc_code,auto_signnature\n" +
            " from sys_corp \n" +
            " union all\n" +
            "  select id,account_licence,address,city,code,collect_flag,ctl_budget,head_person,listed_company,\n" +
            "name,parent_id,parent_ids,province,sort,status,type,use_account_code,net_id,rat_group,create_time,\n" +
            "create_by,update_time,update_by,is_limit_quota,day_limit_quota,month_limit_quota,name_en,address_en,\n" +
            "cur_id,unit_attribute,unit_cm,country_id,cert_type,cert_no,soc_code,auto_signnature \n" +
            "  from sys_corp_manager";

    /**
     * 根据ID查询单位
     *
     * @param id 单位ID。
     * @return 单位。
     */
    @SuppressWarnings("unchecked")
    public Corp getCropById(String id) {
        String hql = " from Corp where id=:id ";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Corp> corpList = listByHQL(hql, params, false);
        Corp corp = new Corp();
        if (!corpList.isEmpty()) {
            corp = corpList.get(0);
        }
        return corp;
    }

    /**
     * 根据ID查询单位
     *
     * @param id 单位ID。
     * @return 单位。
     */
    @SuppressWarnings("unchecked")
    public Corp getCropById(String id, String tenantId) {
        String hql = " from Corp where id=:id and tenantId=:tenantId ";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("tenantId", tenantId);
        List<Corp> corpList = listByHQL(hql, params, false);
        Corp corp = new Corp();
        if (!corpList.isEmpty()) {
            corp = corpList.get(0);
        }
        return corp;
    }

    /**
     * 查询单位管理列表数据
     *
     * @param search
     * @return
     */
    public List<Corp> listTreeTableBySearch(CorpVO search) {
        StringBuilder hql = new StringBuilder("from Corp m where 1 = 1");
        Map<String, Object> params = Maps.newHashMap();
        if (search != null) {
            if (!EmptyUtils.isBlank(search.getId())) {
                String[] ids = search.getId().split(CommonConstant.COMMA_SPLIT);
                if (ids.length == 1) {
                    hql.append(" and m.id = :id ");
                    params.put("id", search.getId());
                } else {
                    hql.append(" and ").append(SqlUtil.getInSql(ids, "id", true));
                }
            }
            if (!EmptyUtils.isEmpty(search.getStatus())) {
                hql.append(" and m.status = :status");
                params.put("status", search.getStatus());
            }
        }
        hql.append(" order by m.sort, m.code");
        return createQuery(hql.toString(), params).list();
    }
    /**
     * 查询单位管理列表数据
     *
     * @param search
     * @return
     */
    public List<CorpVO> listTreeTableBySearch(CorpVO search, String dictGroupId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.id,m.code,m.listed_company as listedCompany,m.name,m.parent_id as parentId,m.sort,m.status,m.type,m.net_id as netId,m.rat_group as ratGroup,m.cur_id as curId,m.unit_attribute as unitAttribute, ");
        sql.append(" p.name as netName,c.cur_name as curName,d.type_name as unitAttributeName,m.unit_Cm as unitCm,cm.cell_name as unitCmName from sys_corp m ");
        sql.append(" left join sys_corp p on m.net_id = p.id ");
        sql.append(" left join bt_currency c on m.cur_id = c.id ");
        sql.append(" left join SYS_CELL_MANAGE cm on cm.id=m.unit_Cm");
        sql.append(" left join  sys_Dict d on d.type_group_id=:dictGroupId and d.type_code = m.UNIT_ATTRIBUTE  where 1 = 1 ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("dictGroupId", dictGroupId);
        if (search != null) {
            if (!EmptyUtils.isBlank(search.getId())) {
                String[] ids = search.getId().split(CommonConstant.COMMA_SPLIT);
                if (ids.length == 1) {
                    sql.append(" and m.id = :id ");
                    params.put("id", search.getId());
                } else {
                    sql.append(" and ").append(SqlUtil.getInSql(ids, "m.id", true));
                }
            }
            if (!EmptyUtils.isEmpty(search.getStatus())) {
                sql.append(" and m.status = :status");
                params.put("status", search.getStatus());
            }
        }
        sql.append(" order by m.sort, m.code");
        List<Map> mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return Lists.newArrayListWithCapacity(0);
        }
        return BeanUtils.copyMapListProperties(mapList, CorpVO.class);
    }
    /**
     * 列出有效的网点和单位
     *
     * @return
     */
    public List<Corp> listEffectiveCorp(List corpAuth) {
        StringBuilder hql = new StringBuilder("from Corp m where m.status = 1");
        Map<String, Object> params = new HashMap<>();
        if (corpAuth != null) {
            if (corpAuth.size() == 0) {
                // 修复Druid异常：sql injection violation, dbType mysql, druid-version 1.2.15, part alway false condition not allow
                // hql.append(" and 1!=1 ");
                hql.append(" and m.id is null ");
            } else {
                hql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuth, "m.id", "corpId", params));
            }
        }
        hql.append(" order by m.sort, m.code");
        return createQuery(hql.toString(), params).list();
    }
    /**
     * 列出有效的网点和单位
     *
     * @return
     */
    public List<Corp> listEffectiveCorpBaiscInfo() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("listEffectiveCorpBaiscInfo select spends :");
        Map<String, Object> params = new HashMap<>();
        List<Map> mapResult = this.listBySQL("select m.id as id,m.name as name,m.code as code,m.parent_id as parentId,m.status as status,m.sort as sort from sys_corp m where m.status = 1" + " order by m.sort, m.code", params);
        stopWatch.stop();
        stopWatch.start("listEffectiveCorpBaiscInfo convert record to Corp object spends :");
        List<Corp> result = null;
        if (EmptyUtils.isNotEmpty(mapResult)) {
            result = BeanUtils.copyMapListProperties(mapResult, Corp.class);
        }
        stopWatch.stop();
        logger.info("listEffectiveCorpBaiscInfo select and convert record to Corp object spends:{}", stopWatch.prettyPrint());
        return result;
    }
    /**
     * 查找有效的网点
     *
     * @return
     */
    public List<Corp> listEffectiveCorpByNet(List corpAuth) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" select c.id as id, c.name as name, c.parentId as parentId,c.code as code, c.parentIds as parentIds, c.sort as sort, c.curId as curId from Corp c ");
        hql.append(" where c.type = :type and status = :status ");
        params.put("type", CORP_TYPE_CODE_NET);
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (corpAuth != null) {
            if (corpAuth.size() == 0) {
                hql.append(" and 1!=1 ");
            } else {
                hql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuth, "c.id", "corpId", params));
            }
        }
        hql.append(" order by c.sort, c.code ");
        return listByHQLAliasToBean(hql.toString(), params, Corp.class);
    }
    /**
     * 查找有效的单位
     *
     * @return
     */
    public List<Corp> listEffectiveCorpByCorp(List corpAuth) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" select c.id as id, c.name as name, c.parentId as parentId,c.code as code, c.parentIds as parentIds, c.sort as sort, c.curId as curId from Corp c ");
        hql.append(" where c.type = :type and status = :status ");
        params.put("type", CORP_TYPE_CODE_CORP);
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (corpAuth != null) {
            if (corpAuth.size() == 0) {
                hql.append(" and 1!=1 ");
            } else {
                hql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuth, "c.id", "corpId", params));
            }
        }
        hql.append(" order by c.sort, c.code ");
        return listByHQLAliasToBean(hql.toString(), params, Corp.class);
    }
    /**
     * 列出网点类型和是否有效
     *
     * @param type
     * @param status
     * @return
     */
    public List<Corp> listCorpByTypeAndStatus(String netId, Integer type, Integer status) {
        return listCorpByTypeAndStatus(null, netId, type, status, null);
    }
    /**
     * 列出网点类型和是否有效
     *
     * @param type
     * @param status
     * @return
     */
    public List<Corp> listCorpBasicInfoByTypeAndStatus(String netId, Integer type, Integer status) {
        return listCorpBasicInfoByTypeAndStatus(null, netId, type, status, null);
    }
    /**
     * 列出网点类型和是否有效
     *
     * @param type
     * @param status
     * @return
     */
    public List<Corp> listCorpBasicInfoByTypeAndStatus(String corpAuthSql, String netId, Integer type, Integer status, String parentId) {
        StringBuilder hql = new StringBuilder("select c.id as id, c.name as name, c.parent_id as parentId,c.code as code ,c.net_id as netId, c.type as type, c.sort as sort, c.cur_id as curId ");
        hql.append(" from sys_corp c where 1=1 ");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(netId)) {
            hql.append(" and (c.net_id=:netId or (c.parent_id = :netId and c.type = 0)) ");
            map.put("netId", netId);
        }
        if (type != null) {
            hql.append(" and c.type=:type ");
            map.put("type", type);
        }
        if (status != null) {
            hql.append(" and c.status=:status ");
            map.put("status", status);
        }
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            hql.append(" and ").append(corpAuthSql).append(" ");
        }
        if (StringUtils.isNotEmpty(parentId)) {
            hql.append(" and c.parent_ids like :parentId");
            map.put("parentId", "%" + parentId + "%");
        }
        hql.append(" order by c.sort, c.code ");
        List<Map> mapResult = this.listBySQL(hql.toString(), map);
        List<Corp> result = null;
        if (EmptyUtils.isNotEmpty(mapResult)) {
            result = BeanUtils.copyMapListProperties(mapResult, Corp.class);
        }
        return result;
    }
    /**
     * 列出网点类型和是否有效
     *
     * @param type
     * @param status
     * @return
     */
    public List<Corp> listCorpByTypeAndStatus(String corpAuthSql, String netId, Integer type, Integer status, String parentId) {
        StringBuilder hql = new StringBuilder("select c.id as id, c.name as name, c.parentId as parentId,c.code as code ,c.netId as netId, c.type as type, c.parentIds as parentIds, c.sort as sort, c.curId as curId ");
        hql.append(" from Corp c where 1=1 ");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(netId)) {
            hql.append(" and (c.netId=:netId or (c.parentId = :netId and c.type = 0)) ");
            map.put("netId", netId);
        }
        if (type != null) {
            hql.append(" and c.type=:type ");
            map.put("type", type);
        }
        if (status != null) {
            hql.append(" and c.status=:status ");
            map.put("status", status);
        }
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            hql.append(" and ").append(corpAuthSql).append(" ");
        }
        if (StringUtils.isNotEmpty(parentId)) {
            hql.append(" and c.parentIds like :parentId");
            map.put("parentId", "%" + parentId + "%");
        }
        hql.append(" order by c.sort, c.code ");
        return listByHQLAliasToBean(hql.toString(), map, Corp.class);
    }
    /**
     * 根据网点单位的父节点ID查找子节点
     *
     * @param id
     * @return
     */
    public List<CorpSimpleVO> listByCorpId(String id) {
        StringBuilder hql = new StringBuilder("select c.id as id, c.name as name, c.parentId as pid,c.code as code, c.sort as sort,, c.curId as curId, ");
        hql.append("(select count(*) from Corp corp where corp.parentId = c.id ) as childrenSize ");
        hql.append("from Corp c where ");
        hql.append("c.status = 1");
        if (StringUtils.isBlank(id) || id.length() < 32) {
            hql.append("and c.parentId = null");
            return listByHQLAliasToBean(hql.toString(), CorpSimpleVO.class, true);
        } else {
            hql.append("and c.parentId = :parentId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentId", id);
            return listByHQLAliasToBean(hql.toString(), map, CorpSimpleVO.class, true);
        }
    }
    public Corp getByIdCache(String id) {
        String hql = "select new Corp(name,code) from Corp where id = ?0";
        Query query = createQuery(hql, id);
        query.setCacheable(true);
        return (Corp) query.uniqueResult();
    }
    /**
     * 根据ID集合查询
     *
     * @param ids id集合
     * @return
     */
    public List<Corp> listByIds(List<String> ids) {
        StringBuilder hql = new StringBuilder(" from Corp where 1 = 1 ");
        hql.append(" and ").append(SqlUtil.getInSql(ids, "id", true));
        return listByHQL(hql.toString());
    }
    /**
     * 根据网点ID获取该网点下所属下级
     *
     * @param netId
     * @return
     */
    public List<Corp> listValidCorpByNetId(String netId) {
        String hql = "from Corp where (netId = :netId or (parentId = :netId and type = 0)) and status=:status order by sort, code ";
        Map<String, Object> params = new HashMap<>();
        params.put("netId", netId);
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        return listByHQL(hql, params);
    }
    public List<Corp> listUnderCorpByParentCorpId(String corpId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder(" from Corp where parentId = :parentId and status = :status ");
        params.put("parentId", corpId);
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        hql.append(" order by sort, code ");
        return this.listByHQL(hql.toString(), params);
    }
    public List<Corp> listUnderCorpByParentCorpIdSkipLevel(String corpId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder(" from Corp where parentIds like :parentId and status = :status ");
        params.put("parentId", "%" + corpId + "%");
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        hql.append(" order by sort, code ");
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据单位名称查找
     *
     * @param name
     * @return
     */
    public Corp getCorpByName(String name) {
        String hql = "from Corp where status = 1 and name = ?0";
        return getUniqueCache(hql, name);
    }
    /**
     * 根据单位编码查找
     *
     * @param code
     * @return
     */
    public Corp getCorpByCode(String code) {
        String hql = "from Corp where status = 1 and code = ?0";
        Query query = createQuery(hql, code);
        Object corp = query.uniqueResult();
        if (corp == null) {
            return null;
        }
        return (Corp) corp;
    }
    /**
     * 查找上级单位
     *
     * @param id 单位id
     * @return 上级单位
     */
    public Corp getParentById(String id) {
        Corp corp = get(id);
        Corp parentCorp = null;
        if (StringUtils.isNotEmpty(corp.getParentId())) {
            parentCorp = get(corp.getParentId());
        }
        return parentCorp;
    }
    /**
     * 功能介绍 : 获取集团统一授信主体单位。<br>
     * <b>日期：</b>2017年7月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<Corp> listRatGroupCorpNet(List<String> corpIds) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append(" select c.id as id, c.name as name, c.parentId as parentId,c.code as code, c.parentIds as parentIds, c.sort as sort, c.curId as curId ");
        hql.append(" from Corp c where c.ratGroup = :ratGroup and status = :status ");
        if (!CollectionUtils.isEmpty(corpIds)) {
            hql.append(" and ").append(SqlUtil.getInSql(corpIds, "c.id", true));
        }
        hql.append(" order by c.sort, c.code ");
        params.put("ratGroup", Integer.valueOf(RationConstant.RAT_GROUP_1));
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        return listByHQLAliasToBean(hql.toString(), params, Corp.class);
    }
    /**
     * Update by chenlj 2018/4/11
     * 修改内容：修改code使其支持模糊查询
     *
     * @param input
     * @return
     */
    public IPageResult listByCorpDetail(IParameterInput input) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select s.id as id,s.code as code,s.name as name,s.net_id as netId," +
                "s.parent_id as parentId,s.type as type, c.sort as sort, s.cur_id as curId from sys_corp s where s.status = 1 and s.type = 1 ");
        IParameter parameter = input.getParameter();
        String code = parameter.get("code");
        String name = parameter.get("name");
        String corpId = parameter.get("corpId");
		/*if(StringUtils.isNotEmpty(code)){
			sql.append(" and s.code = :code ");
        	map.put("code", code);
        }*/
        if (StringUtils.isNotEmpty(code)) {
            sql.append(" and s.code like :code ");
            map.put("code", "%" + code + "%");
        }
        if (StringUtils.isNotEmpty(name)) {
            sql.append(" and s.name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and (s.parent_id = :corpId or s.id = :corpId) ");
            map.put("corpId", corpId);
        }
        sql.append(" order by s.sort,s.code ");
        return getPageResultBySQL(input, sql.toString(), map, CorpVO.class);
    }
    /**
     * 做逻辑删除
     *
     * @param id 实体主键
     */
    @Override
    public void delete(String id) {
//        Corp corp = get(id);
//        corp.setStatus(CorpConstant.CORP_STATUS_DISABLE);
        //删除修改为物理删除
        super.delete(id);
    }
    /**
     * 根据type做统计
     *
     * @param type
     * @return
     */
    public long countCorpByType(Integer type) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", CorpConstant.CORP_STATUS_ENABLE));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }

    public long countCorpByTenantId(String tenantId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantId", tenantId));
        //criteria.add(Restrictions.eq("status", CorpConstant.CORP_STATUS_ENABLE));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }
    /**
     * 获取最大排序号
     *
     * @return
     */
    public Integer getMaxSort() {
        Criteria criteria = createCriteria();
        criteria.setProjection(Projections.projectionList().add(Projections.max("sort")));
        return (Integer) criteria.uniqueResult();
    }
    /**
     * Created by chenlj 2018/4/24
     *
     * @param input
     * @return
     */
    public IPageResult listByCorpDetailAndBtBankAccVO(IParameterInput input) {
        Map<String, Object> map = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        IParameter parameter = input.getParameter();
        String corpBank = parameter.get("corpBank");
        if (StringUtils.isNotEmpty(corpBank)) {
            sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,s.name as corpName,s.name_en as corpNameEn, ");
            sql.append(" acc.CREATE_BY as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, acc.bank_code as bankCode, ");
            sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
            sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
            sql.append(" acc.prov as prov,p.PROV_INPUT_NAME as provName,acc.city as city,c.NAME as cityName,acc.electric_bill as electricBill,acc.bank_type_id as bankTypeId, ");
            sql.append(" s.code as code,s.name as name,s.net_id as netId, nc.name AS netName,s.parent_id as parentId,s.type as type, cur.cur_id as curId, ");
            sql.append(" bc.cur_name AS curName,bc.ENGLISH_CODE AS englishCode, s.soc_code as socCode ");
            sql.append(" from sys_corp s ");
            sql.append(" LEFT JOIN sys_corp nc ON s.net_id=nc.id ");
            sql.append(" left join bt_bank_acc acc on acc.corp_id = s.id and acc.status <>  " + BtBankAccConstant.STATUE_DEL + " ");
            sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id  ");
            sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
            sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
            sql.append(" left join bt_bank_acc_cur cur on cur.bank_acc_id = acc.id ");
            sql.append(" LEFT JOIN bt_currency bc ON cur.cur_id = bc.id ");
            sql.append(" LEFT JOIN sys_region p ON acc.PROV = p.ID ");
            sql.append(" LEFT JOIN sys_region c ON acc.CITY = c.ID ");
            sql.append(" where s.status = 1 ");
        } else {
            sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId, s.name as corpName,s.name_en as corpNameEn,");
            sql.append(" acc.CREATE_BY as createBy,acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, acc.bank_code as bankCode, ");
            sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
            sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
            sql.append(" acc.prov as prov,p.PROV_INPUT_NAME as provName,acc.city as city,c.NAME as cityName,acc.electric_bill as electricBill, acc.bank_type_id as bankTypeId, ");
            sql.append(" s.code as code,s.name as name,s.net_id as netId,nc.name AS netName, s.parent_id as parentId,s.type as type,");
            sql.append("  cur.cur_id as curId,bc.cur_name AS curName,bc.ENGLISH_CODE AS englishCode, s.soc_code as socCode ");
            sql.append(" from bt_bank_acc acc ");
            sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
            sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
            sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
            sql.append(" left join bt_bank_acc_cur cur on cur.bank_acc_id = acc.id ");
            sql.append(" LEFT JOIN bt_currency bc ON cur.cur_id = bc.id ");
            sql.append(" left join sys_corp s on acc.corp_id = s.id ");
            sql.append(" LEFT JOIN sys_corp nc ON s.net_id=nc.id ");
            sql.append(" LEFT JOIN sys_region p ON acc.PROV = p.ID ");
            sql.append(" LEFT JOIN sys_region c ON acc.CITY = c.ID ");
            sql.append(" where s.status = 1 and acc.status <> " + BtBankAccConstant.STATUE_DEL + " ");
        }
        String corpId = parameter.get("corpId");
        String code = parameter.get("code");
        String name = parameter.get("name");
        List<String> corpIds = parameter.get("corpIds");
        String bankAcc = parameter.get("bankAcc");
        String curId = parameter.get("eqs_curId");
        /*if(StringUtils.isNotEmpty(code)){
        	sql.append(" and s.code = :code ");
        	map.put("code", code);
        }*/
        if (StringUtils.isNotEmpty(code)) {
            sql.append(" and s.code like :code ");
            map.put("code", "%" + code + "%");
        }
        if (StringUtils.isNotEmpty(bankAcc)) {
            sql.append(" and acc.bank_acc like :bankAcc ");
            map.put("bankAcc", "%" + bankAcc + "%");
        }
        if (StringUtils.isNotEmpty(name)) {
            sql.append(" and s.name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and (" + getSQLIn(corpIds, 900, "s.id") + ")");
        }
//        if (StringUtils.isNotEmpty(corpId)) {
//            sql.append(" and s.id !=:corpId ");
//            map.put("corpId", corpId);
//        }
//        if (StringUtils.isNotEmpty(corpIds)) {
//            sql.append(" and s.id in :corpIds");
//            map.put("corpIds", corpIds);
//        }
        if (StringUtils.isNotEmpty(curId) && StringUtils.isNotBlank(curId)) {
            sql.append(" and cur.cur_id = :curId  ");
            map.put("curId", curId);
        }
        sql.append(" order by s.sort,s.code,acc.create_date ");
        return getPageResultBySQL(input, sql.toString(), map, CorpAndBtBankAccVO.class);
    }
    /**
     * 获取网点或单位下的ID集合
     *
     * @param netId 网点id
     * @return by zhoul 2018-05-11
     */
    public List<String> getIdsByUnderTheNet(String netId) {
        StringBuffer sql = new StringBuffer("select id from sys_corp where parent_ids like :netId");
        Map<String, Object> map = new HashMap<>();
        map.put("netId", "%" + netId + "%");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        return sqlQuery.list();
    }
    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-05-26
     */
    public List<Corp> listCorpByCorpIds(List<String> corpIds) {
        StringBuilder hql = new StringBuilder();
        hql.append("from Corp where 1 = 1 ");
        if (!EmptyUtils.isEmptyList(corpIds)) {
            hql.append(" and ").append(SqlUtil.getInSql(corpIds, "id", true));
        }
        hql.append(" and status = :status order by sort, code ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        return this.listByHQL(hql.toString(), params);
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id, c.code, c.name, c.type, c.net_id as netId, net.NAME as netName, c.soc_code as socCode from sys_corp c left join sys_corp net on c.NET_ID = net.ID where 1 = 1 ");
        //针对前端选择已选数据筛选过滤 zhengg 20200716
        String isSelct = input.getParameter().get("isSelect");
        String ids = input.getParameter().get("ids");
        String[] idsArray = null;
        if (StringUtils.isNotEmpty(ids)) {
            idsArray = ids.split(",");
        }
        Map<String, Object> params = new HashMap<>();
        if ("1".equals(isSelct)) {
            input.getFormdata().remove("eqi_status");//查看已选的时候需要把已禁用的已选也要显示
            sql.append(" and ").append(SqlUtil.getInSqlByParams(idsArray, "c.id", params));
        } else if ("0".equals(isSelct)) {
            sql.append(" and ").append(SqlUtil.getNotInSqlByParams(idsArray, "c.id", params));
        }
        return getDynamicBySQL(input, sql.toString(), CorpVO.class, "c", false, params);
    }
    /**
     * 获取二次授信单位
     *
     * @author luxy
     */
    public List<Corp> listRatCpCorpNet(String netId, String corpAuthSql) {
        Corp corp = get(netId);
        String parentId = null;
        String hql = null;
        if (StringUtils.isNotEmpty(corp.getParentIds())) {
            String parId = corp.getParentIds();
            String[] parIds = parId.split(",");//父节点集合
            parentId = parIds[0];//根节点的id
        }
        hql = "select c.id as id, c.name as name, c.parentId as parentId,c.code as code, c.sort as sort, c.curId as curId "
                + " from Corp c where c.id not in('" + netId + "') and c.status = 1  ";
        if (!EmptyUtils.isBlank(corpAuthSql)) {
            hql += " and " + corpAuthSql;
        }
        if (EmptyUtils.isNotEmpty(parentId)) {
            hql += " and c.parentIds like '%" + parentId + "%' ";
        }
        hql += " order by c.sort ";
        Map<String, Object> params = new HashMap<String, Object>();
        return listByHQLAliasToBean(hql, params, Corp.class, false);
    }
    /**
     * 获取传入用户对应资源有效的网点或单位id
     * <br>
     *
     * @param resource
     * @param userId
     * @param type     CorpConstant.CORP_TYPE_CODE_NET or CorpConstant.CORP_TYPE_CODE_CORP
     * @return 单位id
     * @author zuoy 2019-07-01
     */
    public List<String> getUserNetIds(String resource, String userId, Integer type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select temp.corp_id from (select distinct cur.corp_id,corp.CREATE_TIME from ");
        sql.append(" sys_resources r , sys_corp_user_resource cur , sys_corp corp ");
        sql.append(" where cur.resource_id = r.id ");
        sql.append(" and corp.id = cur.corp_id ");
        sql.append(" and r.permission = :permission and cur.user_id  = :userId ");
        if (!EmptyUtils.isEmpty(type)) {
            sql.append(" and corp.type = :type ");
            params.put("type", type);
        }
        sql.append(" and corp.status = :status");
        sql.append(" order by corp.CREATE_TIME asc ) temp");
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        params.put("permission", resource);
        params.put("userId", userId);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        sqlQuery.addScalar("corp_id", StandardBasicTypes.STRING);
        sqlQuery.setCacheable(true);
        List<String> list = sqlQuery.list();
        return list;
    }
    /**
     * 获取传入用户对应资源的单位或网点id
     * <br>
     *
     * @param resource
     * @param userId
     * @return 单位id
     * @author zuoy 2019-07-01
     */
    public List<String> getUserCorpIds(String resource, String userId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append("SELECT  distinct cur.CORP_ID AS corpId FROM SYS_CORP_USER_RESOURCE cur ");
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotBlank(userId)) {
            sql.append(" and cur.USER_ID =:userId ");
            map.put("userId", userId);
        }
        if (StringUtils.isNotEmpty(resource) && resource.contains(CommonConstant.COMMA_SPLIT)) {
            map.put("permission", resource.split(CommonConstant.COMMA_SPLIT));
            sql.append(" AND cur.PERMISSION in (:permission) ");
        } else {
        	if(StringUtils.isNotEmpty(resource)) {
        		 map.put("permission", resource);
                 sql.append(" AND cur.PERMISSION =:permission ");
        	}
        }
        Query query = createSQLQuery(sql.toString(), map);
        query.setFetchSize(5000);
        List<String> list = query.list();
        return list;
    }
    /**
     * 获取传入用户对应资源的单位或网点id
     * <br>
     *
     * @param resource
     * @param userId
     * @return 单位id
     * @author zuoy 2019-07-01
     */
    public List<String> getCorpIdsByUserResource(String resource, String userId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append("SELECT distinct cur.CORP_ID AS corpId FROM SYS_CORP_USER_RESOURCE cur ");
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotBlank(userId)) {
            sql.append(" and cur.USER_ID =:userId ");
            map.put("userId", userId);
        }
        if (StringUtils.isNotEmpty(resource) && resource.contains(CommonConstant.COMMA_SPLIT)) {
            map.put("permission", resource.split(CommonConstant.COMMA_SPLIT));
            sql.append(" AND cur.PERMISSION in (:permission) ");
        } else {
            map.put("permission", resource);
            sql.append(" AND cur.PERMISSION =:permission ");
        }
        Query query = createSQLQuery(sql.toString(), map);
        query.setFetchSize(5000);
        List<String> list = query.list();
        return list;
    }

    public List<String> getCorpIdsByUser(String resource, String userId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append("SELECT distinct cur.CORP_ID AS corpId FROM SYS_CORP_USER_RESOURCE cur ");
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotBlank(userId)) {
            sql.append(" and cur.USER_ID =:userId ");
            map.put("userId", userId);
        }
        Query query = createSQLQuery(sql.toString(), map);
        query.setFetchSize(5000);
        List<String> list = query.list();
        return list;
    }
    /**
     * 根据单位id获取单位基本信息
     * <br>
     *
     * @param corpIds
     * @return 单位id
     * @author zuoy 2019-07-01
     */
    public List<Corp> getCorpBasicInfoByCorpIds(Set<String> corpIds) {
        StringBuilder sql = new StringBuilder("select c.id as id, c.name as name, c.parent_id as parentId,c.code as code ,c.sort as sort,c.status as status ");
        sql.append(" from sys_corp c where c.status=1 ");
        Map<String, Object> map = new HashMap<>();
        if (EmptyUtils.isNotEmpty(corpIds)) {
            //sql.append(" and id in (:corpIds) ");
            //map.put("corpIds", corpIds);
            List<String> corpIdList = new ArrayList<>(corpIds);
            sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "c.id", true));
        }
        sql.append(" order by c.sort asc ");
        List<Map> mapResult = this.listBySQL(sql.toString(), map);
        List<Corp> result = null;
        if (EmptyUtils.isNotEmpty(mapResult)) {
            result = BeanUtils.copyMapListProperties(mapResult, Corp.class);
        }
        return result;
    }
    /**
     * 获取传入单位对应资源的用户ID
     * <br>
     *
     * @param resource
     * @param userCorpId
     * @return 用户ID
     * @author zuoy 2019-07-01
     */
    public List<String> getUserIdsByCorpId(String resource, String userCorpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select c.user_id from ");
        sql.append(" SYS_RESOURCES r left join SYS_CORP_USER_RESOURCE c on r.id=c.resource_id where r.permission = :permission ");
        map.put("permission", resource);
        if (StringUtils.isNotBlank(userCorpId)) {
            sql.append(" and c.corp_id  = :corpId ");
            map.put("corpId", userCorpId);
        }
        Query query = createSQLQuery(sql.toString(), map);
//        query.setCacheable(true);
        List<String> list = query.list();
        return list;
    }
    /**
     * 检查是否存在parentId和parendIds为空的数据
     *
     * @return
     */
    public Boolean checkParentIdAndParentIdsIsNull() {
        StringBuilder hql = new StringBuilder("from Corp m where m.parentId is null and m.parentIds is null ");
        List list = createQuery(hql.toString()).list();
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 查询用户拥有权限的成员单位列表
     *
     * @param userId 用户ID
     * @return 成员单位列表
     */
    public List<Map> getCorpListByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT SCUR.CORP_ID  AS id, CORP.NAME AS name");
        sql.append(" FROM SYS_CORP_USER_RESOURCE SCUR ");
        sql.append(" JOIN SYS_CORP CORP ON CORP.ID = SCUR.CORP_ID ");
        sql.append(" WHERE SCUR.USER_ID=:userId AND CORP.STATUS = 1 ");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return listMapBySQL(sql.toString(), params);
    }
    /**
     * 获取传入用户对应资源的单位或网点id
     * <br>
     *
     * @param resource
     * @param userId
     * @return 单位id
     */
    public List<Map> getUserCorpList(String resource, String userId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT SCUR.CORP_ID AS id, SC.NAME AS name FROM SYS_RESOURCES SR ");
        sql.append("LEFT JOIN SYS_CORP_USER_RESOURCE SCUR ON SCUR.RESOURCE_ID = SR.ID AND SR.PERMISSION = :permission ");
        sql.append("LEFT JOIN SYS_CORP SC ON SCUR.CORP_ID = SC.ID AND SC.STATUS = 1 ");
        params.put("permission", resource);
        if (StringUtils.isNotBlank(userId)) {
            sql.append("WHERE SCUR.USER_ID =:userId ");
            params.put("userId", userId);
        }
        return listMapBySQL(sql.toString(), params);
    }
    /**
     * 单位组件-
     * 获取过滤权限资源的结构
     *
     * @param corpAuthSql
     * @param corpComponentVO
     * @return
     */
    public List<Corp> getFilterCorp(String corpAuthSql, CorpComponentVO corpComponentVO) {
        StringBuilder hql = new StringBuilder("select c.id as id, c.name as name, c.parentId as parentId,c.code as code ,c.netId as netId, c.type as type, c.parentIds as parentIds, c.sort as sort, c.curId as curId ");
        hql.append(" from Corp c where 1=1 ");
        Map<String, Object> map = new HashMap<>();
        if(!ObjectUtils.isEmpty(corpComponentVO)) {
	        if (StringUtils.isNotBlank(corpComponentVO.getNetId())) {
	            hql.append(" and (c.netId=:netId or (c.parentId = :netId and c.type = 0) ");
	            map.put("netId", corpComponentVO.getNetId());
	            // 查找网点及网点下的单位
	            if (StringUtils.isNotEmpty(corpComponentVO.getUnderNetCorp()) && corpComponentVO.getUnderNetCorp()) {
	                hql.append(" or c.parentIds like :parentId");
	                map.put("parentId", "%" + corpComponentVO.getNetId() + "%");
	            }
	            hql.append(")");
	        }
	        if (!EmptyUtils.isBlank(corpComponentVO.getType())) {
	            hql.append(" and c.type=:type ");
	            map.put("type", CorpConstant.CORP_TYPE_NET.equalsIgnoreCase(corpComponentVO.getType()) ? CORP_TYPE_CODE_NET : CORP_TYPE_CODE_CORP);
	        }
        }
        hql.append(" and c.status=:status ");
        map.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            hql.append(" and ").append(corpAuthSql).append(" ");
        }
        if (!ObjectUtils.isEmpty(corpComponentVO) && StringUtils.isNotEmpty(corpComponentVO.getParentId())) {
            hql.append(" and c.parentIds like :parentId");
            map.put("parentId", "%" + corpComponentVO.getParentId() + "%");
        }
        hql.append(" order by c.sort, c.code ");
        Query query = this.createQuery(hql.toString(), map);
        query.setCacheable(false);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> mapResult = query.list();
        if (EmptyUtils.isEmpty(mapResult)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyMapListProperties(mapResult, Corp.class);
        //return listByHQLAliasToBean(hql.toString(), map, Corp.class);
    }
    /**
     * 根据单位ID集合查询数据
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
    public DataTablesOutput getDataTablesCorpByCorpIds(DataTablesInput dataTablesInput, List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id,c.net_Id as netId,s.name as netName,c.code, c.name from sys_corp c left join sys_corp s on c.net_id=s.id where 1 = 1  ");
        if(!EmptyUtils.isEmptyList(corpIds)){
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "c.id", true));
        }
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("netIdStr"))){
            sql.append(" and (c.net_id='").append(dataTablesInput.getFormdata().get("netIdStr")).append("' or c.id='")
                    .append(dataTablesInput.getFormdata().get("netIdStr")).append("') ");
        }
        sql.append(" and c.status = :status order by c.sort, c.code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        dataTablesInput.setFormdata(null);
        DataTablesOutput dataOutInfo = getDynamicBySQL(dataTablesInput, sql.toString(), CorpDTO.class,"",false,params);
        return dataOutInfo;
    }
    /**
     * 根据条件查询没有删除且是有效的银行账户
     * @param dataTablesInput
     * @param bankacc
     * @return
     */
    public DataTablesOutput getNotDeleteAndValidAndBankAcc(DataTablesInput dataTablesInput, List<String> bankacc) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,sy.name as corpName, acccur.cur_id,");
        sql.append(" acc.REG_DATE as regDate,acc.VALID_SIGN as validSign, ");
        sql.append(" acc.acc_type as accType,att.attribute_name as attributeName, acc.IS_ONLINE as isOnline,acc.RATES as rates, ");
        sql.append(" nat.nature_name as natureName,banktype.type_name as bankTypeName,acc.bank_name as bankName, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        sql.append(" left join bt_bank_acc_cur acccur on acccur.BANK_ACC_ID=acc.id");
        sql.append(" left join sys_corp sy on sy.id=acc.corp_id");
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
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.acc_Type as accType,");
        sql.append("t.acc_State as accState,t.ACC_OPEN_TIME as accOpenTime");
        sql.append(" from Fc_Acc t ");
        sql.append(" left join sys_corp sy on sy.id=t.corp_id ");
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
    public void resetRatGroup() {
        String sql = " update sys_corp a set a.rat_group = :ratGroup ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratGroup", RationConstant.RAT_GROUP_0);
        createSQLQuery(sql, params).executeUpdate();
    }
    public void updateRatGroup(List<String> corpIds) {
        String sql = " update sys_corp a set a.rat_group = :ratGroup where " + getSQLIn(corpIds, 900, "a.id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ratGroup", RationConstant.RAT_GROUP_1);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 获取有效单位Id集合
     * @return
     */
    public List<String> getCorpIds() {
        StringBuffer sql = new StringBuffer("select c.id from sys_corp c where c.status =:status order by c.sort, c.code");
        Map<String, Object> map = new HashMap<>();
        map.put("status", CorpConstant.CORP_STATUS_ENABLE);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), map);
        return sqlQuery.list();
    }
    /**
     * 根据查询条件获取有效的单位Id集合
     * @param seach 单位名称 or 单位编码
     * @return
     */
    public List<String> listCorpIdsBySeach(String seach) {
        StringBuffer sql = new StringBuffer("select c.id from sys_corp c where c.status =:status");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (!EmptyUtils.isBlank(seach)) {
            sql.append(" and (c.code like :seach or c.name like :seach) ");
            params.put("seach", "%" + seach + "%");
        }
        sql.append(" order by c.sort, c.code");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.list();
    }
    /**
     * 根据查询条件获取有效的单位Id集合
     * @param seach 单位名称 or 单位编码
     * @return
     */
    public List<CorpDTO> listCorpBySeach(String seach) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id,c.name,c.code, c.parent_id as parentId, c.type, c.net_id as netId, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = :status ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (!EmptyUtils.isBlank(seach)) {
            sql.append(" and (c.code like :seach or c.name like :seach) ");
            params.put("seach", "%" + seach + "%");
        }
        sql.append(" order by c.sort, c.code");
        return this.listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 根据网点Id获取单位信息集合
     * @param netId
     * @return
     */
    public List<CorpDTO> getCorpByNetId(String netId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id,c.name,c.code, c.parent_id as parentId, c.type, c.net_id as netId, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = :status ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (!EmptyUtils.isBlank(netId)) {
            sql.append(" and c.net_id = :netId or c.id= :netId");
            params.put("netId", netId);
        }
        sql.append(" order by c.sort, c.code");
        return this.listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 根据Id集合获取单位信息（包含下级单位数量）
     * @param corpIds 单位Id
     * @return
     */
    public List<CorpDTO> getCorpByIdsHadChildCount(List<String> corpIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id,c.name,c.code, c.parent_id as parentId, c.type, c.net_id as netId, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = :status ");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (!EmptyUtils.isEmptyList(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "c.id", true));
        }
        sql.append(" order by c.sort, c.code");
        return this.listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentId 上级单位Id
     * @return
     */
    public List<CorpDTO> getCorpByParentId(String parentId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id,c.name,c.code, c.parent_id as parentId, c.type, c.net_id as netId, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = :status and c.parent_id = :parentId");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        params.put("parentId", parentId);
        sql.append(" order by c.sort, c.code");
        return this.listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 根据上级单位集合获取子单位信息
     * @param parentIds 上级单位Ids
     * @return
     */
    public List<CorpDTO> getCorpByParentIds(List<String> parentIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.id,c.name,c.code, c.parent_id as parentId, c.type, c.net_id as netId, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = :status");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        if (!EmptyUtils.isEmptyList(parentIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(parentIds, "c.id", true));
        }
        sql.append(" order by c.sort, c.code");
        return this.listBySQLAliasToBean(sql.toString(), params, CorpDTO.class);
    }
    /**
     * 根据上级单位Id获取子单位信息
     * @param parentId 上级单位Id
     * @return
     */
    public List<String> getCorpIdByParentId(String parentId) {
        StringBuffer sql = new StringBuffer("select c.id from sys_corp c");
        sql.append(" where c.status = :status and c.parent_id = :parentId");
        Map<String, Object> params = new HashMap<>();
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        params.put("parentId", parentId);
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.list();
    }
    /**
     * 获取单位ID集合(从FexCorpExposureDao迁移过来)
     * @param orgType
     * @return
     */
    public List<String> listCorpIdsBySeach(String orgType, String search) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String corpTableName = "sys_corp";
        if (CommonConstant.YES.equals(orgType)) {
            //资金计划的组织架构 暂未实现
        }
        sql.append(" select c.id corpId  from " + corpTableName + " c ");
        sql.append(" where c.status = 1 ");
        if (!EmptyUtils.isBlank(search)) {
            sql.append(" and (c.code like :search or c.name like :search) ");
            params.put("search", "%" + search + "%");
        }
        sql.append(" order by c.sort, c.code ");
        return this.createSQLQuery(sql.toString()).setProperties(params).list();
    }
    /**
     * 单位敞口设置集合(从FexCorpExposureDao迁移过来)
     * @param orgType
     * @return
     */
    public List<FexCorpExposureVO> listFbsCorpExposure(String orgType, String search) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String corpTableName = "sys_corp";
        if (CommonConstant.YES.equals(orgType)) {
            //资金计划的组织架构 暂未实现
        }
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId, ");
        sql.append(" (select count(*) from " + corpTableName + " c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from  " + corpTableName + "  c ");
        sql.append(" where c.status = 1 ");
        if (!EmptyUtils.isBlank(search)) {
            sql.append(" and (c.code like :search or c.name like :search) ");
            params.put("search", "%" + search + "%");
        }
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }
    /**
     * 根据单位ID获取上级单位敞口信息(从FexCorpExposureDao迁移过来)
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getParentCorpExposureByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = 1 and c.id=(select parent_Id from sys_corp where id=:corpId)");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.getBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }
    /**
     * 根据上级单位ID获取下级单位的集合(从FexCorpExposureDao迁移过来)
     * @param parentId
     * @return
     */
    public List<FexCorpExposureVO> listCorpExposureByParentId(String parentId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = 1 and c.parent_Id=:parentId");
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        return this.listBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }
    /**
     * 根据单位ID获取上级单位敞口信息(从FexCorpExposureDao迁移过来)
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getCorpExposureByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId ");
        sql.append(" from sys_corp c ");
        sql.append(" where c.status = 1 and c.id=:corpId");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.getBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }
    /**
     * 根据策略值获取有效的策略关联数据。
     *
     * @param dataValList 策略值集合。
     * @return 策略关联数据。
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getStrategyDataByDataVal(List<String> dataValList){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append("  select sc.id as dataVal, sc.name,sc.code from sys_corp sc where sc.id in (:dataValList)");
        sql.append("  union all");
        sql.append("  SELECT bt.id as dataVal,bt.type_name as name,bt.id as code from bt_bank_type bt where bt.id in (:dataValList)");
        sql.append("  union all");
        sql.append("  select ec.id as dataVal, ec.name,ec.code from sys_external_corp ec where ec.id in (:dataValList)");
        sql.append("    and ec.bw_type not in (:bwType)");
        sql.append(") tmp");
        Map<String, Object> params = new HashMap<>();
        params.put("dataValList",dataValList);
        params.put("bwType", new String[]{SysBlackWhiteConstant.EXTERNALCORP_BWTYPE_01});
        return listBySQL(sql.toString(), params);
    }
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCorpAndNetInfoByCorpId(List<String> corpIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sc.id as corpId,sc.code as corpCode,sc.name as corpName,cn.code as netCode,cn.name as netName");
        sql.append("  from sys_corp sc LEFT JOIN sys_corp cn on cn.id=sc.net_id");
        sql.append("  where sc.id in (:corpIdList) ");
        Map<String, Object> params = new HashMap<>();
        params.put("corpIdList",corpIdList);
        return listBySQL(sql.toString(), params);
    }
    public List<CorpDTO> getNameByTypeAndIds(List<String> corpIds, Integer type) {
        String sql = " select a.id , a.name from sys_corp a " +
                " where a.type = :type " +
                " and " + getSQLIn(corpIds, 900, "a.id");
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("type",type);
        return listBySQLAliasToBean(sql, params, CorpDTO.class);
    }

    public List<CorpDTO> getNameByIds(List<String> corpIds) {
        String sql = " select a.id , a.name from sys_corp a " +
                " where 1=1 " +
                " and " + getSQLIn(corpIds, 900, "a.id");
        HashMap<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql, params, CorpDTO.class);
    }

    public List<CorpDTO> getNameByNetIds(List<String> netIds) {
        String sql = " select a.net_id , a.name from sys_corp a " +
                " where 1=1 " +
                " and " + getSQLIn(netIds, 900, "a.id");
        HashMap<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql, params, CorpDTO.class);
    }

    public List<CorpDTO> listCorpDTOByCorpIds(List<String> corpIds) {
        String sql = " select a.* from sys_corp a " +
                " where 1=1 " +
                " and " + getSQLIn(corpIds, 900, "a.id");
        return listBySQLAliasToBean(sql, null, CorpDTO.class);
    }
    public List<ErpBasicDataVOVO> listErpBasicDataVOVO(String sql) {
        return listBySQLAliasToBean(sql, null, ErpBasicDataVOVO.class);
    }

    /**
     * 根据单位ID集合获取单位和网点的code和name。
     *
     * @param corpIds 单位ID集合。
     * @return 单位和网点信息。
     */
    public List<Map<String, Object>> listCorpAndNetByCorpIds(List<String> corpIds){
        String sql = " select sc.id as CORPID,sc.code as CORPCODE,sc.NAME as CORPNAME,net.CODE as NETCODE,net.NAME as NETNAME " +
                " from sys_corp sc left join sys_corp net on sc.NET_ID = net.ID " +
                " where 1=1 " +
                " and " + getSQLIn(corpIds, 900, "sc.id");
        return listBySQL(sql, null);
    }

    /**
     * 获取单位和网点的code和name。
     *
     * @param corpIds 单位ID集合。
     * @return 单位和网点信息。
     */
    public List<CorpDTO> listCorpAndNetByCorpId(List<String> corpIds) {
        String sql = " select sc.id as id,sc.code as code,sc.NAME as name,net.CODE as netCode,net.NAME as netName " +
                " from sys_corp sc left join sys_corp net on sc.NET_ID = net.ID " +
                " where 1=1 " +
                " and " + getSQLIn(corpIds, 900, "sc.id");
        return listBySQLAliasToBean(sql, null, CorpDTO.class);
    }

    public List<CorpDTO> listCorpDTOByNetIds(List<String> netIds) {
        String sql = " select a.* from sys_corp a " +
                " where 1=1 " +
                " and " + getSQLIn(netIds, 900, "a.net_id");
        return listBySQLAliasToBean(sql, null, CorpDTO.class);
    }
    public List<Map<String, Object>> queryVAccListVo(String bankAcc){
        String  sql ="SELECT C.ID AS CORPID, C.ACCOUNT_LICENCE AS ACCOUNTLICENCE, B.BANK_CODE AS BANKCODE,B.ACC_NAME ACCNAME" +
                " ,c.SOC_CODE as socCode, b.ELECTRIC_BILL as electricBill From sys_corp c,bt_bank_acc b where c.id=b.corp_id and b.bank_acc='"+bankAcc+"'";
        List<Map<String,Object>> objStr =this.listBySQL(sql,null);
        return objStr;
    }
    public List<String> getCorpIdsByType(String type){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.id corpId  from sys_corp c ");
        if (!EmptyUtils.isBlank(type)) {
            sql.append(" where c.type = :type ");
            params.put("type", type);
        }
        return this.createSQLQuery(sql.toString()).setProperties(params).list();
    }


    public List<CorpDTO> getCorpNetList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id,c.net_Id AS netId,s.name AS netName,c.code, c.name ");
        sql.append(" from sys_corp c");
        sql.append(" left join sys_corp s on c.net_id=s.id");

        return listBySQLAliasToBean(sql.toString(), null, CorpDTO.class);
    }

    public List<CorpDTO> getSimpleCorpList() {
        String sql = "select id,code,name from sys_corp where status = 1";
        return listBySQLAliasToBean(sql, null, CorpDTO.class);
    }

    public Corp queryByAreaIdOrEquityIdOrStoreId(String id, String type) {
        StringBuilder hql = new StringBuilder(" from Corp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        if ("1".equals(type)){
            hql.append(" and operationArea_ID like '%" +id +"%'");

        }
        if ("2".equals(type)){
            hql.append(" and equityMaintenance_ID =:id");
        }
        if ("3".equals(type)){
            hql.append(" and unitStoreMaintenance_ID like '%"+ id + "%'");
        }
        if ("4".equals(type)){
            hql.append(" and province_id like '%"+ id + "%'");
        }
        List<Corp> corpList= listByHQL(hql.toString(),params,false);
        Corp corp=new Corp();
        if(corpList.size()>0){
            corp=corpList.get(0);
        }
        return corp;
    }

    public List<Corp> listByAreaIdOrEquityIdOrStoreId(String id, String type) {
        StringBuilder hql = new StringBuilder(" from Corp where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        if ("1".equals(type)){
            hql.append(" and operationArea_ID like '%" +id +"%'");

        }
        if ("2".equals(type)){
            hql.append(" and equityMaintenance_ID =:id");
        }
        if ("3".equals(type)){
            hql.append(" and unitStoreMaintenance_ID like '%"+ id + "%'");
        }
        if ("4".equals(type)){
            hql.append(" and province_id like '%"+ id + "%'");
        }
        List<Corp> corpList= listByHQL(hql.toString(),params,false);
        return corpList;
    }

    public List<CorpDTO> queryCorpDTOByCodes(List<String> codes) {
        String sql = " select a.* from sys_corp a " +
                " where 1=1 " +
                " and " + getSQLIn(codes, 900, "a.code");
        return listBySQLAliasToBean(sql, null, CorpDTO.class);
    }

    public List<CorpDTO> getRegion(String id) {
        Map<String, Object> map = new HashMap<>();
        String sql = " select a.* from sys_corp a " +
                " where 1=1 " +
                " and a.unit_cm=:id order by a.sort ";
        map.put("id",id);
        return listBySQLAliasToBean(sql, map, CorpDTO.class);
    }

    public CorpDTO getRegionByCorpId(String corpId,String regionId) {
        Map<String, Object> map = new HashMap<>();
        String sql = " select a.* from sys_corp a " +
                " where 1=1 " +
                " and a.unit_cm=:regionId and a.id=:corpId ";
        map.put("regionId",regionId);
        map.put("corpId",corpId);
        return getBySQLAliasToBean(sql, map, CorpDTO.class);
    }

    public List<CorpDTO> getCorpListAll() {
    	StringBuilder hql = new StringBuilder("from Corp m where m.status = 1");
        List<Corp> list = listByHQLAliasToBean(hql.toString(), null, Corp.class);
        List<CorpDTO> listDTO = BeanUtils.copyListProperties(list, CorpDTO.class);
        return listDTO;
    }

    public List<Corp> listCorpByParentId(String corpId, List<String> equityMaintenanceList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder(" from Corp where parentId = :parentId and status = :status  ");
        if (ObjectUtil.isNotEmpty(equityMaintenanceList)){
            hql.append(" and equityMaintenanceId in (:equityMaintenanceList) ");
            params.put("equityMaintenanceList", equityMaintenanceList);
        }
        params.put("parentId", corpId);
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        hql.append(" order by sort, code ");
        return this.listByHQL(hql.toString(), params);
    }

    public List<Corp> listByCodes(List<String> codes) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.* from sys_corp c where c.status = :status ");
        sql.append(" and " + getSQLIn(codes, 900, "c.code"));
        params.put("status", CorpConstant.CORP_STATUS_ENABLE);
        return this.listBySQLAliasToBean(sql.toString(), params, Corp.class);
    }

    /**
     * 根据传入的id或者ids获取所有子级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    public List<Corp> listSubCorp(List<String> ids,boolean includeThisCorp) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH RECURSIVE SubUnits AS ( ");
        sql.append("               SELECT sc.* ");
        sql.append("               FROM sys_corp sc ");
        sql.append("               WHERE sc.`STATUS` = 1 ");
        sql.append(" and " + getSQLIn(ids, 900, "sc.id"));
        sql.append("               UNION ALL ");
        sql.append("               SELECT c.* ");
        sql.append("               FROM sys_corp c ");
        sql.append("               INNER JOIN SubUnits s ON c.PARENT_ID = s.id ");
        sql.append("               WHERE c.`STATUS` = 1 ");
        sql.append(" ) ");
        sql.append(" SELECT * FROM SubUnits where 1=1 ");
        if (!includeThisCorp){
            sql.append(" and " + getSQLNotIn(ids, 900, "id"));
        }
        return this.listBySQLAliasToBean(sql.toString(), null, Corp.class);
    }

    /**
     * 根据传入的id或者ids获取所有父级单位信息
     * @param ids
     * @param includeThisCorp 是否包含当前单位，默认为true
     * @return TODO
     */
    public List<Corp> listParentCorp(List<String> ids, boolean includeThisCorp) {
        StringBuilder sql = new StringBuilder();
        sql.append("WITH RECURSIVE ParentUnits AS ( ");
        sql.append("               SELECT sc.* ");
        sql.append("               FROM sys_corp sc  ");
        sql.append("               WHERE sc.`STATUS` = 1  ");
        sql.append(" and " + getSQLIn(ids, 900, "sc.id"));
        sql.append("               UNION ALL ");
        sql.append("               SELECT c.* ");
        sql.append("               FROM sys_corp c ");
        sql.append("               INNER JOIN ParentUnits p ON c.id = p.PARENT_ID ");
        sql.append("               WHERE c.`STATUS` = 1 ");
        sql.append(" ) ");
        sql.append(" SELECT  * FROM ParentUnits where 1=1 ");
        if (!includeThisCorp){
            sql.append(" and " + getSQLNotIn(ids, 900, "id"));
        }
        return this.listBySQLAliasToBean(sql.toString(), null, Corp.class);
    }

    public List<Corp> listByCorpType(int corpType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from sys_corp where type = :corpType order by sort asc ");
        return this.listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(){{put("corpType", corpType);}}, Corp.class);
    }


    /**
     * 客商组件 单位网点数据信息
     * @param input
     * @return
     */
    public DataTablesOutput getAllCorpData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id as id,c.code as code,c.name as name,c.name_en as nameEn,c.type, c.net_id as netId,");
        sql.append(" c.unit_attribute as unitAttribute,c.country_id as countryId,scc.name_zh as countryName ");
        sql.append(" from sys_corp c");
        sql.append("  left join sys_country_cust scc ");
        sql.append("    on c.country_id = scc.id ");
        sql.append(" where c.status = 1");
        String countryName=(String)input.getFormdata().get("countryName");
        if(!EmptyUtils.isEmpty(countryName)){
            sql.append(" and scc.name_zh='"+countryName+"'");
        }
        return getDynamicBySQL(input, sql.toString(), CorpVO.class, "c", false, null);
    }

    /**
     * 根据单位ID集合查询数据
     *
     * @param corpIds
     * @return
     * @author zuoy 2018-05-26
     */
    public List<Corp> listCorpByCorpIdsOrgan(List<String> corpIds,String releaseId) {
        String sql = "select c.id as id, c.name as name, o.parent_id as parentId,c.code as code ,c.net_id as netId, c.type as type, c.sort as sort, c.cur_id as curId " +
                " from ("+corpSql+") c " +
                " left join sys_organtype_corp_release o on c.id = o.corp_id and release_id= :releaseId " +
                " where c.status = 1  " ;
        Map<String, Object> params = new HashMap<>();
        params.put("releaseId", releaseId);
        if(EmptyUtils.isNotEmpty(corpIds)){
            sql += "and " + SqlUtil.getInSql(corpIds,"c.id",true);
        }
        List<Corp> list = this.listBySQLAliasToBean(sql,params,Corp.class);
        return list;
    }
}
