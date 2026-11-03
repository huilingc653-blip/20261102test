package com.bytter.ras.service;

import com.bytter.ras.dao.RasQueryDao;
import com.bytter.ras.entity.RasAuditInfo;
import com.bytter.ras.vo.RasAuditCommonInfoVO;
import com.bytter.ras.vo.RasRevenueDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核查询统计Service
 * @date 2019/5/16 13:39
 */
@Service("rasQueryService")
@Transactional
public class RasQueryService extends BaseService<RasAuditInfo, String> {

    @Autowired
    public RasQueryDao rasQueryDao;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired
    private RasShopGrantService rasShopGrantService;

    @Override
    public BaseDao<RasAuditInfo, String> getEntityDao() {
        return rasQueryDao;
    }

    /**
     * 已处理差异明细查询列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDifferentDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_DIFFERENTDTL_OP,dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_QUERY, dataTablesInput.getCurrentUser());
        return rasQueryDao.getDifferentDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }

    /**
     * （营收）明细列表加载
     * @param dataTablesInput
     * @param id
     * @return
     */
    public DataTablesOutput getRevenueDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id,String queryType) {
        return rasQueryDao.getRevenueDifferentDataTablesUnion(dataTablesInput, id,queryType);
    }

    /**
     * （实收）明细列表加载
     * @param dataTablesInput
     * @param id
     * @return
     */
    public DataTablesOutput getReceivedDifferentDataTablesUnion(DataTablesInput dataTablesInput, String id,String queryType) {
        return rasQueryDao.getReceivedDifferentDataTablesUnion(dataTablesInput, id,queryType);
    }

    /**
     * 跳转（营收）（实收）明细页面，根据id查找数据
     * @param id 稽核表id
     * @return
     */
    public RasAuditCommonInfoVO getMessage(String id) {
        return rasQueryDao.getMessage(id);
    }

    /**
     * 跳转数据明细页面，根据id查找数据
     * @param id 稽核表id
     * @return
     */
    public HashMap<String,Object> getRevenueAndReceived(String id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        HashMap<String, Object> map = new HashMap<>();
        List<RasRevenueDtlVO> listRevenue = rasQueryDao.listRevenue("ras_revenue_dtl",params);
        // 判断营收表中是否有数据，没有则去历史表中查数据
        if (listRevenue != null && listRevenue.size() > 0) {
            map.put("revenue", listRevenue);
            map.put("received", rasQueryDao.listReceived("RAS_RECEIVED_DTL",params));
            map.put("audit",rasQueryDao.getAuditInfo("RAS_AUDIT_INFO",params));
        }else {
            //当在明细表中查不到数据时则去历史表查
            map.put("revenue", rasQueryDao.listRevenue("RAS_REVENUE_HIS_DTL",params));
            map.put("received", rasQueryDao.listReceived("RAS_RECEIVED_HIS_DTL",params));
            map.put("audit",rasQueryDao.getAuditInfo("RAS_AUDIT_INFO_HIS",params));
        }
        return map;
    }

    /**
     * 已稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getYesAuditDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_SHOPYESAUDIT_OP,dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_QUERY, dataTablesInput.getCurrentUser());
        return rasQueryDao.getYesAuditDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }

    /**
     * 待稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getNotAuditDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_SHOPNOTAUDIT_OP,dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_QUERY, dataTablesInput.getCurrentUser());
        return rasQueryDao.getNotAuditDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }
    /**
     * 跳转（营收）（实收）明细页面，根据id查找数据
     * @param id 门店id
     * @return 稽核信息
     */
    public RasAuditCommonInfoVO getNotMessage(String id) {
        return rasQueryDao.getNotMessage(id);
    }
}
