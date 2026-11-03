//package com.bytter.workflow.domain.dao;
//
//import com.bytter.workflow.domain.entity.FlowCategory;
//import com.bytter.core.base.BaseDao;
//import com.bytter.core.utils.StringUtils;
//import com.bytter.workflow.constant.FlowCategoryConstant;
//import org.hibernate.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * @author onlineGenerator
// * @version 1.0
// * @Title: Dao
// * @Description: ACT_DE_MODEL
// * @date 2017-07-05 09:17:39
// */
//@Repository
//public class FlowCategoryDao extends BaseDao<FlowCategory, String> {
//	
//	 public List<FlowCategory> listApproveCategory() {
//	        StringBuilder sb = new StringBuilder("from act_extend_category r where r.type=?0 order by r.sort");
//	        return createQuery(sb.toString(), FlowCategoryConstant.FLOW_CATEGORY_TYPE_APPROVE).list();
//	    }
//
////    public List<FlowCategory> findByParentId(String parentId) {
////        Query query;
////        StringBuilder sb = new StringBuilder("from FlowCategory r where r.parentId=");
////
////        if (StringUtils.isBlank(parentId)) {
////            sb.append("null");
////            query = createQuery(sb.toString());
////        } else {
////            sb.append("?0");
////            query = createQuery(sb.toString(), parentId);
////        }
////        query.setCacheable(true);
////        return query.list();
////    }
////
////   
////
////    public FlowCategory findByKey(String key) {
////        Query query = createQuery("from FlowCategory r where r.key=?0", key);
////        Object object = query.uniqueResult();
////        if (object == null) {
////            return null;
////        }
////        return (FlowCategory) object;
////    }
////
////    public List<FlowCategory> findByKeyLike(String key) {
////        Query query = createQuery("from FlowCategory r where r.key like ?0", key + "%");
////        return query.list();
////    }
////
////    public List<FlowCategory> findByKeyNotLike(String key) {
////        Query query = createQuery("from FlowCategory r where r.key not like ?0", key + "%");
////        return query.list();
////    }
////
////    /**
////     * 获取不是移动审批的流程分类
////     *
////     * @return 不是移动审批的流程分类
////     */
////    public List<FlowCategory> getIsNotMobileApprovalList() {
////        Query query = createQuery("from FlowCategory r where r.mobileApprovalFlag = ?0", FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL);
////        return query.list();
////    }
////
////    /**
////     * 获取是移动审批的流程分类
////     *
////     * @return 是移动审批的流程分类
////     */
////    public List<FlowCategory> getIsMobileApprovalList() {
//////        StringBuilder sql = new StringBuilder();
//////        sql.append("select bfc.* from bpm_flow_category bfc ");
//////        sql.append("where bfc.mobile_approval_flag=:mobileApprovalFlag or bfc.mobile_approval_flag is null");
//////        Map<String, Object> params = new HashMap<>();
//////        params.put("mobileApprovalFlag", FlowCategoryConstant.IS_MOBILE_APPROVAL);
//////        return listByHQLAliasToBean(sql.toString(), params, FlowCategory.class);
////
////        Query query = createQuery("from FlowCategory r where r.mobileApprovalFlag = ?0 or r.mobileApprovalFlag is null", FlowCategoryConstant.IS_MOBILE_APPROVAL);
////        return query.list();
////    }
////
////    /**
////     * 根据通知类型ID获取流程分类
////     *
////     * @param notificationId 通知类型ID
////     * @return 流程分类
////     */
////    public FlowCategory findByNotificationId(String notificationId) {
////        Query query = createQuery("from FlowCategory r where r.notificationId = ?0 ", notificationId);
////        List<FlowCategory> list = query.list();
////        return (list != null && list.size() > 0) ? list.get(0) : null;
////    }
//
//
//}
