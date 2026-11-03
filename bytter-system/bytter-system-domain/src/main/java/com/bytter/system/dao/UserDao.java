package com.bytter.system.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.SysResourceConstant;
import com.bytter.system.api.constant.UserConstant;
import com.bytter.system.entity.User;
import com.bytter.system.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户DAO层
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class UserDao extends BaseDao<User, String> {
    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    public long countUserByTenantId(String tenantId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantId", tenantId));
        criteria.add(Restrictions.ne("status", UserConstant.STATUS_DELETE));
        criteria.add(Restrictions.ne("status", UserConstant.STATUS_SYS_INIT));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }

    /**
     * 批量修改用户锁定/解锁状态
     *
     * @param userIds 用户ID集合
     * @param status  状态
     */
    public void lockOrUnLock(List<String> userIds, Object status) {
        StringBuilder hql = new StringBuilder("update User u set ");
        hql.append("u.status=:status");
        hql.append(" where u.id in (:userIds)");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("userIds", userIds);
        batchExecute(hql.toString(), map);
    }
//    public DataTablesOutput listUser(DataTablesInput input) {
//        Map<String, Object> map = new HashMap<>();
//        StringBuilder hql = new StringBuilder();
//
//        String loginName = (String) input.getFormdata().get("loginName");
//        String username = (String) input.getFormdata().get("username");
//        String corpId = (String) input.getFormdata().get("corpId");
//
//        hql.append("select u.id as id, ");
//        hql.append(" u.loginName as loginName, ");
//        hql.append(" u.username as username, ");
//        hql.append(" u.status as status, ");
//        hql.append(" u.beginUseTime as beginUseTime, ");
//        hql.append(" u.code as code, ");
//        hql.append(" u.corp.id as corpId, ");
//        hql.append(" u.isAdmin as isAdmin ");
//        hql.append("from User u where u.status=1 ");
//        if (StringUtils.isNotBlank(loginName)) {
//            hql.append("and u.loginName like :loginName ");
//            map.put("loginName", "%" + loginName + "%");
//        }
//
//        if (StringUtils.isNotBlank(username)) {
//            hql.append("and u.username like :username ");
//            map.put("username", "%" + username + "%");
//        }
//
//        if (StringUtils.isNotBlank(corpId)) {
//            hql.append("and u.corp.id=:corpId ");
//            map.put("corpId", corpId);
//        }
//
//
//        OrderString order = input.getOrderString();
//        if (order != null && StringUtils.isNotBlank(order.getColumn())) {
//            hql.append(" order by u.").append(order.getColumn()).append(" ").append(order.getSort());
//        }
//
//        DataTablesOutput output = getDataTablesOutputByHQLAliasToBean(input, hql.toString(), map, UserVO.class);
//        return output;
//    }
    public IPageResult<UserVO> listUserNew(IParameterInput input, String dictGroupId,List<String> corpIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        IParameter parameter = input.getParameter();
        String loginName = parameter.get("loginName");
        String username = parameter.get("username");
        String userIds = parameter.get("userIds");
        String corpId = parameter.get("corpId");
        String statusString = parameter.get("status");
        List<String> corpIdList = parameter.get("corpIdList");
        hql.append("select u.id as id, ");
        hql.append(" u.loginName as loginName, ");
        hql.append(" u.username as username, ");
        hql.append(" u.status as status, ");
        hql.append(" u.beginUseTime as beginUseTime, ");
        hql.append(" u.code as code, ");
        hql.append(" u.corp.id as corpId, ");
        hql.append(" u.isAdmin as isAdmin, ");
        hql.append(" u.createTime as createTime, ");
        hql.append(" u.updateTime as updateTime, ");
        hql.append(" u.userCadn as userCadn, ");
        hql.append(" c.code as corpCode,");
        hql.append(" c.name as corpName,");
        hql.append(" d.name as statusLang");
        hql.append(" from User u ");
        hql.append(" left join Corp c on u.corp.id=c.id");
        hql.append(" left join Dict d on d.dictGroup.id=:dictGroupId and d.code = u.status");
        hql.append(" where 1=1");
        map.put("dictGroupId", dictGroupId);
        if (StringUtils.isBlank(statusString)) {
            /*
                2019-08-12
                系统用户状态字典值为0~3，系统增加了一个schedule用户，status=4，该用户不出现在用户管理列表之中。
             */
            hql.append(" and u.status < 4 ");
        } else if (StringUtils.isNotEmpty(statusString)) {
            int status = Integer.parseInt(statusString);
            hql.append(" and u.status=:status ");
            map.put("status", status);
        }
        if (StringUtils.isNotBlank(loginName)) {
            hql.append(" and u.loginName like :loginName ");
            map.put("loginName", "%" + loginName + "%");
        }
        if (StringUtils.isNotBlank(username)) {
            hql.append(" and u.username like :username ");
            map.put("username", "%" + username + "%");
        }

        if (StringUtils.isNotBlank(corpId)) {
            hql.append(" and u.corp.id=:corpId ");
            map.put("corpId", corpId);
        }
        //查询当前登录的用户拥有“用户查询”这个特殊权限的单位
        if (!CollectionUtils.isEmpty(corpIdList)) {
//            hql.append(" and u.corp.id in :corpIdList ");
//            map.put("corpIdList", corpIdList);
            hql.append(" and ").append(SqlUtil.getInSqlByParams(corpIdList, "u.corp.id", "corpIdList", map));
        }
        if(StringUtils.isBlank(corpId) && CollectionUtils.isEmpty(corpIdList) && !CollectionUtils.isEmpty(corpIds)) {
        	//;SqlUtil.getInSqlByParams(corpIds, "u.corp.id", "corpIdList", map)
        	hql.append(" and ").append(SqlUtil.getInSql(corpIds, "u.corp.id", true));
        }
        // 用户过滤
        if (StringUtils.isNotBlank(userIds)) {
            hql.append(" and ").append(SqlUtil.getInSql(userIds.split(CommonConstant.COMMA_SPLIT), "u.id", true));
        }
        if (input instanceof TableGridInput) {
            TableGridInput gridInput = (TableGridInput) input;
            String order = gridInput.getOrder();
            String sort = gridInput.getSort();
            if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
                hql.append("order by u.").append(order).append(" ").append(sort);
            }
        }
        if (input instanceof DataTablesInput) {
            DataTablesInput dataInput = (DataTablesInput) input;
            String orderPara = dataInput.getOrderPara();
            String orderType = dataInput.getOrderByPara();
            if (StringUtils.isNotEmpty(orderPara) && StringUtils.isNotEmpty(orderType)) {
                if ("corpName".equals(orderPara)) {
                    hql.append("order by u.").append("corp.id").append(" ").append(orderType);
                } else {
                    hql.append("order by u.").append(orderPara).append(" ").append(orderType);
                }
            }
        }
        return getPageResultByHQL(input, hql.toString(), map, UserVO.class);
    }

    public IPageResult<UserVO> listUser(IParameterInput input, String dictGroupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        IParameter parameter = input.getParameter();
        String loginName = parameter.get("loginName");
        String username = parameter.get("username");
        String userIds = parameter.get("userIds");
        String corpId = parameter.get("corpId");
        String statusString = parameter.get("status");
        List<String> corpIdList = parameter.get("corpIdList");
        hql.append("select u.id as id, ");
        hql.append(" u.login_name as loginName, ");
        hql.append(" u.username as username, ");
        hql.append(" u.status as status, ");
        hql.append(" u.begin_use_time as beginUseTime, ");
        hql.append(" u.code as code, ");
        hql.append(" u.corp_id as corpId, ");
        hql.append(" u.is_admin as isAdmin, ");
        hql.append(" u.create_time as createTime, ");
        hql.append(" u.update_time as updateTime, ");
        hql.append(" u.user_cadn as userCadn, ");
        hql.append(" c.code as corpCode,");
        hql.append(" c.name as corpName,");
        hql.append(" d.type_name as statusLang");
        hql.append(" from SYS_USER u ");
        hql.append(" left join SYS_CORP c on u.corp_id=c.id");
        hql.append(" left join SYS_DICT d on d.type_group_id=:dictGroupId and d.type_code = BT_CONCAT(u.status,'')");
        hql.append(" where 1=1");
        map.put("dictGroupId", dictGroupId);
        if (StringUtils.isBlank(statusString)) {
            /*
                2019-08-12
                系统用户状态字典值为0~3，系统增加了一个schedule用户，status=4，该用户不出现在用户管理列表之中。
             */
            hql.append(" and u.status < 4 ");
        } else if (StringUtils.isNotEmpty(statusString)) {
            int status = Integer.parseInt(statusString);
            hql.append(" and u.status=:status ");
            map.put("status", status);
        }
        if (StringUtils.isNotBlank(loginName)) {
            hql.append(" and u.LOGIN_NAME like :loginName ");
            map.put("loginName", "%" + loginName + "%");
        }
        if (StringUtils.isNotBlank(username)) {
            hql.append(" and u.username like :username ");
            map.put("username", "%" + username + "%");
        }

        if (StringUtils.isNotBlank(corpId)) {
            hql.append(" and u.corp_id=:corpId ");
            map.put("corpId", corpId);
        }
        //查询当前登录的用户拥有“用户查询”这个特殊权限的单位
        if (!CollectionUtils.isEmpty(corpIdList)) {
//            hql.append(" and u.corp.id in :corpIdList ");
//            map.put("corpIdList", corpIdList);
            hql.append(" and ").append(SqlUtil.getInSqlByParams(corpIdList, "u.corp_id", "corpIdList", map));
        }
        // 用户过滤
        if (StringUtils.isNotBlank(userIds)) {
            hql.append(" and ").append(SqlUtil.getInSql(userIds.split(CommonConstant.COMMA_SPLIT), "u.id", true));
        }
        if (input instanceof TableGridInput) {
            TableGridInput gridInput = (TableGridInput) input;
            String order = gridInput.getOrder();
            String sort = gridInput.getSort();
            if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
                hql.append("order by u.").append(order).append(" ").append(sort);
            }
        }
        if (input instanceof DataTablesInput) {
            DataTablesInput dataInput = (DataTablesInput) input;
            String orderPara = dataInput.getOrderPara();
            String orderType = dataInput.getOrderByPara();
            if (StringUtils.isNotEmpty(orderPara) && StringUtils.isNotEmpty(orderType)) {
                if ("corpName".equals(orderPara)) {
                    hql.append("order by u.").append("corp_id").append(" ").append(orderType);
                } else {
                    hql.append("order by u.").append(orderPara).append(" ").append(orderType);
                }
            }
        }
        return getPageResultBySQL(input, hql.toString(), map, UserVO.class);
    }
    public List<String> listUserIdByMenuId(String menuId) {
        String sql = "SELECT DISTINCT u.id FROM sys_menu menu LEFT JOIN sys_role_menu rm ON menu.id = rm.menu_id LEFT JOIN sys_role r ON rm.role_id = r.id " +
                "LEFT JOIN sys_user_role ur ON ur.role_id = r.id left join sys_user u on u.id=ur.user_id where menu.id = :menuId";
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("menuId", menuId);
        return namedParameterJdbcOperations.queryForList(sql, objectObjectHashMap, String.class);
    }
    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param corpId
     * @param resourceCode
     */
    public List<UserVO> loadUserByCorpIdOrResource(String corpId, String resourceCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct c.id,c.login_name as loginName,c.username ")
                .append("from SYS_CORP_USER_RESOURCE a ")
                .append("left join sys_resources b on a.resource_id=b.id ")
                .append("left join sys_user c on c.id=a.user_id ")
                .append("where b.permission in (:resourceCode) and c.id is not null  ");
        Map<String, Object> params = new HashMap<String, Object>();
        String[] resourceCodes = {};
        if (StringUtils.isNotEmpty(resourceCode)) {
            resourceCodes = resourceCode.split(",");
        } else {
            resourceCodes = new String[]{" "};
        }
        params.put("resourceCode", resourceCodes);
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and (" + getSQLIn(Arrays.asList(corpId.split(CommonConstant.COMMA_SPLIT)), 900, "a.corp_id") + ")");
        }
        sql.append(" order by c.login_name,c.username ");
        return this.listBySQLAliasToBean(sql.toString(), params, UserVO.class);
    }
    /**
     * 根据单位ID列出有效的用户
     *
     * @param corpId 单位ID
     * @return 有效用户集合
     */
    public List<User> listByCorpId(String corpId) {
        String hql = "from User u where u.status=1 and u.corp.id=?0";
        return createQuery(hql, corpId).list();
    }
    /**
     * 查找有效的管理员
     *
     * @return
     */
    public List<User> listByUserAdmin() {
        String hql = "from User u where u.isAdmin=1 and u.status=?0";
        return createQuery(hql, UserConstant.STATUS_NORMAL).list();
    }
    /**
     * 根据用户ID集合查找用户的实体
     *
     * @param ids 用户ID集合
     * @return 用户集合实体
     */
    public List<User> listByUserIds(List<String> ids) {
        if (ListUtils.isNullOrEmpty(ids)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String hql = "from User where " + SqlUtil.getInSql(ids, "id", true);
        Query query = createQuery(hql, map);
        return query.list();
    }
    /**
     * 根据用户ID获取登录名
     *
     * @param userIds 用户ID
     * @return 登录名
     */
    public List<String> listLoginNameByUserIds(List<String> userIds) {
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct SU.LOGIN_NAME from SYS_USER SU ");
        sql.append(" where 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(userIds, "SU.ID", true));
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.addScalar("LOGIN_NAME", StandardBasicTypes.STRING);
        sqlQuery.setFetchSize(5000);
        List<String> loginNameList = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return distinctList(loginNameList);
    }
    /**
     * 通过角色ID集合与单位ID查询拥有权限的用户id，包含特殊权限
     *
     * @param rolesId
     * @param corpId
     * @return
     */
    public List<String> listUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId) {
        if (EmptyUtils.isEmpty(rolesId)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct cur.USER_ID from SYS_CORP_USER_RESOURCE cur ");
        sql.append("left join SYS_USER_ROLE userRole on cur.USER_ID=userRole.USER_ID ");
        sql.append("left join SYS_USER u on u.id=userRole.USER_ID and u.STATUS=1 ");
        sql.append("left join SYS_ROLE role on userRole.ROLE_ID=role.Id ");
        sql.append("where 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(rolesId, "role.ID", true));
        sql.append(" and cur.CORP_ID = :corpId ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameter("corpId", corpId);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        sqlQuery.setFetchSize(5000);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return distinctList(userIds);
    }
    /**
     * List非对象去重复
     *
     * @param list
     * @return
     * @author zuoy 2019-07-01
     */
    public List<String> distinctList(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        List<String> newList = new ArrayList<>();
        set.addAll(list);
        newList.addAll(set);
        return newList;
    }
    /**
     * 通过角色ID集合与单位ID查询拥有"工作流-待办事项-流程审批"权限的用户id
     *
     * @param rolesId
     * @param corpId
     * @param resourceContent
     * @return
     */
    public List<String> listApproveUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId, List<String> resourceContent) {
        if (EmptyUtils.isEmpty(rolesId)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct cur.USER_ID from SYS_RESOURCES res ");
        sql.append("left join SYS_CORP_USER_RESOURCE cur on res.ID=cur.RESOURCE_ID and res.PERMISSION in :resList ");
        sql.append("left join SYS_USER_ROLE userRole on cur.USER_ID=userRole.USER_ID ");
        sql.append("left join SYS_USER u on u.id=userRole.USER_ID and u.STATUS=1 ");
        sql.append("left join SYS_ROLE role on userRole.ROLE_ID=role.Id ");
        sql.append("where role.ID in :rolesId");
        sql.append(" and cur.CORP_ID=:corpId");
        sql.append(" and u.status=:status");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameterList("rolesId", rolesId);
        sqlQuery.setParameter("corpId", corpId);
        sqlQuery.setParameter("status", CommonConstant.YES);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        List<String> resList = Lists.newArrayList(SysResourceConstant.SYS_FLOW_APPROVE);
        if (ListUtils.isNotNullOrEmpty(resourceContent)) {
            resList.addAll(resourceContent);
        }
        sqlQuery.setParameterList("resList", resList);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }

    /**
     * 通过角色ID集合与单位ID查询拥有"工作流-待办事项-流程审批"权限的用户id
     *
     * @param rolesId
     * @param corpId
     * @param resourceContent
     * @return
     */
    public List<String> listApproveUserIdsByRoleIdAndCorpIdNew(List<String> rolesId, String corpId, List<String> resourceContent) {
        if (EmptyUtils.isEmpty(rolesId)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct cur.USER_ID from SYS_RESOURCES res ");
        sql.append("left join SYS_CORP_USER_RESOURCE cur on res.ID=cur.RESOURCE_ID and res.PERMISSION in :resList ");
        sql.append("left join SYS_USER_ROLE userRole on cur.USER_ID=userRole.USER_ID ");
        sql.append("left join SYS_USER u on u.id=userRole.USER_ID and u.STATUS=1 ");
        sql.append("left join SYS_ROLE role on userRole.ROLE_ID=role.Id ");
        sql.append("where role.ID in :rolesId");
        sql.append(" and cur.CORP_ID=:corpId");
        sql.append(" and u.status=:status");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameterList("rolesId", rolesId);
        sqlQuery.setParameter("corpId", corpId);
        sqlQuery.setParameter("status", CommonConstant.YES);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        List<String> resList = Lists.newArrayList(SysResourceConstant.SYS_FLOW_APPROVE);
        if (ListUtils.isNotNullOrEmpty(resourceContent)) {
            resList.addAll(resourceContent);
        }
        sqlQuery.setParameterList("resList", resList);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }

        List<String> userIdMapCorps = listApproveUserIdsByRoleIdAndCorpId2(rolesId,corpId);
        List<String> userIdMapCorpsDelete = new ArrayList<String>();
        if(EmptyUtils.isEmpty(userIdMapCorps)) {
        	return userIds;
        }else {
        	System.out.println("============================单位角色对应关系检查前"+userIds);
        	for(String userid : userIds) {
        		if(!userIdMapCorps.contains(userid)) {
        			userIdMapCorpsDelete.add(userid);
        		}
        	}
        	userIds.removeAll(userIdMapCorpsDelete);
        	System.out.println("============================单位角色对应关系检查后"+userIds);
        	return userIds;
        }

    }

    public List<String> listApproveUserIdsByRoleIdAndCorpId2(List<String> rolesId, String corpId) {
        if (EmptyUtils.isEmpty(rolesId)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct res.USER_ID from SYS_USER_CORP_ROLE res ");
        sql.append("where res.role_id in :rolesId");
        sql.append(" and res.corp_id=:corpId");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameterList("rolesId", rolesId);
        sqlQuery.setParameter("corpId", corpId);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }
    /**
     * 获得具有指定单位，指定权限的userId，并且userId属于传入的userIdRange里面
     *
     * @param corpId
     * @param permissions
     * @param userIdRange
     * @return
     */
    public List<String> listUserIdsByUserIdAndCorpIdAndPermission(String corpId, List<String> permissions, List<String> userIdRange) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select distinct u.id from SYS_USER u left join SYS_CORP_USER_RESOURCE cur on u.id = cur.user_id ");
        sql.append(" left join SYS_RESOURCES res on res.ID = cur.RESOURCE_ID and res.PERMISSION in :permissions ");
        if (EmptyUtils.isNotEmpty(corpId)) {
            sql.append("  where cur.CORP_ID = :corpId  and u.STATUS = 1 ");
        } else {
            sql.append("  where u.STATUS = 1 ");
        }
        if (EmptyUtils.isNotEmpty(userIdRange)) {
            sql.append(" and cur.USER_ID in :userId");
        }
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        if (EmptyUtils.isNotEmpty(userIdRange)) {
            sqlQuery.setParameterList("userId", userIdRange);
        }
        if (EmptyUtils.isNotEmpty(corpId)) {
            sqlQuery.setParameter("corpId", corpId);
        }
        //审批权限兼容旧权限（系统审批+流程分类对应审批）
        sqlQuery.setParameterList("permissions", permissions);
        sqlQuery.addScalar("id", StandardBasicTypes.STRING);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }
    /**
     * 获得具有单位，指定权限的userId，
     *
     * @param corpId
     * @param permission
     * @return
     */
    public List<String> listUserIdsByCorpIdAndPermission(List<String> corpId, String permission) {
        if (EmptyUtils.isEmptyList(corpId)) {
            return new ArrayList<>();
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct cur.USER_ID from SYS_RESOURCES res ");
        sql.append("left join SYS_CORP_USER_RESOURCE cur on res.ID=cur.RESOURCE_ID and res.PERMISSION= :permission ");
        sql.append("left join SYS_USER u on u.id=cur.USER_ID and u.STATUS=1 ");
        sql.append(" where cur.CORP_ID in :corpId");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameterList("corpId", corpId);
        sqlQuery.setParameter("permission", permission);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }
    /**
     * 获得具有指定单位，指定权限的userId，并且userId属于传入的userIdRange里面
     *
     * @param permission
     * @return
     */
    public List<String> listUserIdsByPermission(String permission) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct cur.USER_ID from SYS_RESOURCES res ");
        sql.append("left join SYS_CORP_USER_RESOURCE cur on res.ID=cur.RESOURCE_ID and res.PERMISSION= :permission ");
        sql.append("left join SYS_USER u on u.id=cur.USER_ID and u.STATUS=1 ");
        sql.append(" where u.status=1");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameter("permission", permission);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }
    public List<String> listUserIdsByRoleId(List<String> rolesId) {
        if (EmptyUtils.isEmpty(rolesId)) {
            return new ArrayList<>();
        }
        SQLQuery sqlQuery = createSQLQuery("select distinct USER_ID from SYS_USER_ROLE ur RIGHT JOIN SYS_USER u ON u.ID=ur.USER_ID where ROLE_ID in(:rolesId) and u.status=1");
        sqlQuery.setParameterList("rolesId", rolesId);
        sqlQuery.addScalar("USER_ID", StandardBasicTypes.STRING);
        List<String> userIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return userIds;
    }
    public long countUsefullUser() {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("status", UserConstant.STATUS_NORMAL));
        criteria.setProjection(Projections.rowCount());
        return (long) criteria.uniqueResult();
    }
    /**
     * 修改用户判断loginName唯一性
     *
     * @param loginName
     * @param userId
     * @return
     */
    public User findUserByLoginNameNeUserId(String loginName, String userId) {
        StringBuilder hql = new StringBuilder("from User u where u.loginName=:loginName and u.id!=:userId");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginName", loginName);
        map.put("userId", userId);
        return getUnique(hql.toString(), map);
    }
    public String getSaltByLoginName(String loginName) {
        if (EmptyUtils.isEmpty(loginName)) {
            return "";
        }
        SQLQuery sqlQuery = createSQLQuery("select salt from SYS_USER u where u.login_name =:loginName and u.status=1");
        sqlQuery.setParameter("loginName", loginName);
        sqlQuery.addScalar("salt", StandardBasicTypes.STRING);
        String salt = (String) sqlQuery.uniqueResult();
        return salt;
    }

    public String getUserCodeByCadn(String cadn) {
        if (EmptyUtils.isEmpty(cadn)) {
            return "";
        }
        SQLQuery sqlQuery = createSQLQuery("select login_name from SYS_USER u where u.user_cadn =:userCadn");
        sqlQuery.setParameter("userCadn", cadn);
        String loginName = (String) sqlQuery.uniqueResult();
        return loginName;
    }
    /**
     * 获取除删除以外所有用户
     *
     * @return
     */
    public List<UserVO> listAllUserByUnDelete() {
        StringBuilder hql = new StringBuilder();
        hql.append("select u.id as id, u.loginName as loginName, u.username as username, u.status as status, u.beginUseTime as beginUseTime, ");
        hql.append(" u.code as code, u.corp.id as corpId, u.isAdmin as isAdmin, u.createTime as createTime, u.updateTime as updateTime, ");
        hql.append(" u.userCadn as userCadn from User u ");
        hql.append(" where u.status in (1, 2, 3) ");
        hql.append(" order by status, is_admin desc ");
        return this.listByHQLAliasToBean(hql.toString(), UserVO.class);
    }

    /**
     * 用户查询
     *
     * @param userVO
     * @return
     */
    public List<UserVO> listUserByParam(UserVO userVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select u.id as id, u.login_name as loginName, u.username as username, u.status as status, u.begin_use_time as beginUseTime,  ");
        sql.append(" u.code as code, u.corp_id as corpId, s.name as corpName, u.is_admin as isAdmin, u.create_time as createTime, u.update_time as updateTime, ");
        sql.append(" u.user_cadn as userCadn ");
        sql.append(" from sys_user u ");
        sql.append(" left join sys_corp s on u.corp_id = s.id ");
        sql.append(" where 1=1 ");

        Map<String, Object> params = Maps.newHashMap();
        if (!StringUtils.isBlank(userVO.getCorpId())) {
            sql.append(" and u.corp_id = :corpId ");
            params.put("corpId", userVO.getCorpId());
        }

        if (!EmptyUtils.isEmpty(userVO.getStatus())) {
            sql.append(" and u.status = :status ");
            params.put("status", userVO.getStatus());
        } else {
            sql.append(" and u.status in(1,2,3) ");
        }

        if (!EmptyUtils.isEmpty(userVO.getLoginName())) {
            sql.append(" and u.login_name like :loginName ");
            params.put("loginName", "%" + userVO.getLoginName() + "%");
        }

        if (!EmptyUtils.isEmpty(userVO.getUsername())) {
            sql.append(" and u.username like :username ");
            params.put("username", "%" + userVO.getUsername() + "%");
        }
        return listBySQLAliasToBean(sql.toString(), params, UserVO.class);
    }

    /**
     * 获取用户 userName和corpName
     */
    public List<UserVO> getUserNameAndCorpNameByUserIds(List<String> userIds){
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, a.username as username, b.id as corpId, b.name as corpName from sys_user a join sys_corp b on a.corp_id = b.id ");
        if (CollectionUtil.isNotEmpty(userIds)){
            sql.append(" where ").append(getSQLIn(userIds, 900, "a.id"));
        }
        return listBySQLAliasToBean(sql.toString(), null, UserVO.class);
    }

    /**
     * 获取有效的用户
     *
     * @return
     */
    public List<UserVO> listAllUser() {
        StringBuilder hql = new StringBuilder();
        hql.append("select u.id as id, u.loginName as loginName, u.username as username, u.status as status, u.beginUseTime as beginUseTime, ");
        hql.append(" u.code as code, u.corp.id as corpId, u.isAdmin as isAdmin, u.createTime as createTime, u.updateTime as updateTime, ");
        hql.append(" u.userCadn as userCadn from User u ");
        hql.append(" where u.status = 1 ");
        hql.append(" order by status, is_admin desc ");
        return this.listByHQLAliasToBean(hql.toString(), UserVO.class);
    }
    /**
     * 根据单位id获取此单位下有效的用户数量
     *
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidUserNumByCorpId(String corpId) {
        String sql = "select count(*) from sys_user where corp_id=:corpId and status=:status";
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("status", UserConstant.STATUS_NORMAL);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据单位id获取此单位被非本单位用户特殊管理权限授权的数量
     *
     * @param corpId
     * @return 有效用户笔数
     */
    public int getCorpAuthNumByCorpId(String corpId) {
        String sql = "select count(*) From SYS_CORP_USER_RESOURCE r left join sys_user u on u.id=r.user_id where r.corp_id=:corpId and u.corp_id!=r.corp_id";
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 修改系统肤色
     *
     * @param userId  用户ID
     * @param sysSkin 状态
     */
    public void modifySysSkin(String userId, String sysSkin) {
        StringBuilder hql = new StringBuilder("update User u set ");
        hql.append("u.sysSkin=:sysSkin");
        hql.append(" where u.id = :userId)");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("sysSkin", sysSkin);
        batchExecute(hql.toString(), map);
    }
    /**
     * 查找有效的管理员
     *
     * @return
     */
    public List<String> listUserAdminByCorpIds(List<String> corpIds) {
        String sql = "select u.id as id from sys_user u where u.is_admin=1 and u.status=1";
        sql += " and " + SqlUtil.getInSql(corpIds, "u.corp_id", true);
        return createSQLQuery(sql).list();
    }
    /**
     * 根据用户手机号获取用户
     *
     * @param mobile
     * @return
     */
    public User findUserByMobile(String mobile) {
        StringBuilder hql = new StringBuilder("from User u where u.phone = :phone ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", mobile);
        return getUnique(hql.toString(), map);
    }
    /**
     * 根据传入的 loginNames，返回 k=loginName, v=username
     * @param loginNames
     * @return
     */
    public List<User> getUserNameByLoginNames(List<String> loginNames) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.login_name as loginName, a.username as username from sys_user a");
        sql.append(" where ").append(getSQLIn(loginNames, 900, "a.login_name"));
        return listBySQLAliasToBean(sql.toString(), null, User.class);
    }
    public List<User> getLoginNameByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id, a.login_name as loginName ,a.username as username from sys_user a");
        sql.append(" where ").append(getSQLIn(ids, 900, "a.id"));
        return listBySQLAliasToBean(sql.toString(), null, User.class);
    }
    /**
     * 根据用户ID查询用户角色ID
     * @param userId
     * @return 角色ID
     */
    public List<String> listRidsByUid(String userId) {
        String sql = "select role_id from SYS_USER_ROLE where user_id = :userId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        return createSQLQuery(sql, param).list();
    }
    /**
     * 根据用户ID查询用户角色ID
     * @param roleId 角色编号。
     * @return 用户ID集合
     */
    public List<String> listUserIdsByRid(String roleId) {
        String sql = "select user_id from sys_user_role where role_id = :roleId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("roleId", roleId);
        return createSQLQuery(sql, param).list();
    }

    /**
     * 根据员工编号获取用户编号。
     *
     * @param employeeIdList 员工编号列表。
     * @return 用户编号信息。
     */
    public List<Map<String, Object>> listUserIdByEmployeeId(List<String> employeeIdList) {
        String sql = " select a.id as id, a.employee_id as employeeId,a.corp_id as corpId,a.status from sys_user a where "
                + getSQLIn(employeeIdList, 900, "a.employee_id");
        return (List<Map<String, Object>>) listBySQL(sql, null);
    }

    public List<UserVO> listNormalUser() {
        StringBuilder hql = new StringBuilder();
        hql.append("select u.id as id, u.loginName as loginName, u.username as username, u.status as status, u.beginUseTime as beginUseTime, ");
        hql.append(" u.code as code, u.corp.id as corpId, u.isAdmin as isAdmin, u.createTime as createTime, u.updateTime as updateTime, ");
        hql.append(" u.userCadn as userCadn from User u ");
        hql.append(" where u.status = 1 ");
        hql.append(" order by status, is_admin desc ");
        return this.listByHQLAliasToBean(hql.toString(), UserVO.class);
    }

    public IPageResult<UserVO> getCashierUserTables(DataTablesInput dataTablesInput) {
        String loginName = (String) dataTablesInput.getFormdata().get("loginName");
        String username = (String) dataTablesInput.getFormdata().get("username");
        String userIds = (String) dataTablesInput.getFormdata().get("userIds");

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        if (ObjectUtil.isNotEmpty(userIds)){
            sql.append("SELECT u.id,u.login_name loginName,u.username,'1' isSelect ");
            sql.append("FROM sys_user u ");
            sql.append("WHERE  u.ID IN (:userIds) ");
            sql.append("UNION ");
            params.put("userIds", userIds.split(","));
        }
        sql.append("SELECT u.id,u.login_name loginName,u.username,'0' isSelect   ");
        sql.append("FROM sys_user u  ");
        sql.append("WHERE u.`STATUS` = 1 ");
        if (ObjectUtil.isNotEmpty(userIds)){
            sql.append(" AND u.ID NOT IN (:userIds) ");
        }
        if (ObjectUtil.isNotEmpty(loginName)){
            sql.append(" AND u.login_name like '%" + loginName + "%' ");
        }
        if (ObjectUtil.isNotEmpty(username)){
            sql.append(" AND u.username like '%" + username + "%' ");
        }

        sql.append(" ) AS t ");
        sql.append("ORDER BY t.isSelect desc ");
        return getPageResultBySQL(dataTablesInput, sql.toString(), params, UserVO.class);
    }

    public List<User> listUser(User query) {
        StringBuilder sql = new StringBuilder("SELECT * FROM sys_user WHERE status = 1 ");

        if (null != query) {

            if (CharSequenceUtil.isNotEmpty(query.getLoginName())) {
                sql.append(" AND LOGIN_NAME LIKE '%").append(query.getLoginName()).append("%' ");
            }

            if (CharSequenceUtil.isNotEmpty(query.getUsername())) {
                sql.append(" AND USERNAME LIKE '%").append(query.getUsername()).append("%' ");
            }

        }

        return listBySQLAliasToBean(sql.toString(), new HashMap<>(), User.class);
    }
}
