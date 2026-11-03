package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCorpExposureDao;
import com.bytter.biz.foreign.entity.FexCorpExposure;
import com.bytter.biz.foreign.vo.FexCorpExposureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Service
 * @Description: 单位敞口设置表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexCorpExposureService")
@Transactional
public class FexCorpExposureService extends BaseService<FexCorpExposure, String> {
    @Autowired
    private FexCorpExposureDao fexCorpExposureDao;
    @Autowired(required=false)
    private ICorpService corpService;

    @Override
    public BaseDao<FexCorpExposure, String> getEntityDao() {
        return fexCorpExposureDao;
    }

    /**
     * 获取组织架构类型
     * @return
     */
    public String getOrgType(){
        List<FexCorpExposure> list = this.fexCorpExposureDao.list();
        if(EmptyUtils.isNotEmpty(list)){
            return list.get(0).getOrgType();
        }
        return CommonConstant.YES;
    }

    /**
     * 获取单位列表信息
     * @param orgType
     * @return
     */
    public List<CorpDTO> listCorpByOrgType(String orgType, String search) {
        List<String> corpIds = this.fexCorpExposureDao.listCorpIdsBySeach(orgType, search);
        List<CorpDTO> listCorp = this.corpService.listCorpDTOByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(listCorp)) {
            return null;
        }
        return corpService.listCorpTree(listCorp);
    }

    public Map<String, FexCorpExposureVO> listFbsCorpExposure(String orgType, String search) {
        List<FexCorpExposureVO> list = this.fexCorpExposureDao.listFbsCorpExposure(orgType, search);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        Map<String, FexCorpExposureVO> map = new HashMap<>();
        for (FexCorpExposureVO vo : list) {
            map.put(vo.getCorpId(), vo);
        }
        return map;
    }

    /**
     * 保存单位敞口
     *
     * @param collectCorpIds
     * @param naturalHedgeCorpIds
     */
    public void saveCorpExposure(String orgType, List<String> collectCorpIds, List<String> naturalHedgeCorpIds, ICurrentUser user) {
        //先删除数据
        fexCorpExposureDao.deleteCorpExposure();
        //重新初始化
        List<String> corpIds = this.fexCorpExposureDao.listCorpIdsBySeach(orgType, null);
        List<FexCorpExposure> list = new ArrayList<>();
        for(String corpId:corpIds){
            FexCorpExposure corpExposure = new FexCorpExposure();
            corpExposure.setCorpId(corpId);
            corpExposure.setOrgType(orgType);
            corpExposure.setIsCollect(CommonConstant.NO);
            if(collectCorpIds.contains(corpId)){
                corpExposure.setIsCollect(CommonConstant.YES);
            }
            corpExposure.setIsNaturalHedge(CommonConstant.NO);
            if(naturalHedgeCorpIds.contains(corpId)){
                corpExposure.setIsNaturalHedge(CommonConstant.YES);
            }
            corpExposure.setCreateBy(user.getLoginName());
            corpExposure.setCreateDate(DateUtils.getDate());
            list.add(corpExposure);
        }
        batchSave(list);
    }

    /**
     * 根据单位ID获取上级单位敞口信息
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getParentCorpExposureByCorpId(String corpId) {
        return fexCorpExposureDao.getParentCorpExposureByCorpId(corpId);
    }

    /**
     * 根据单位ID获取上级单位敞口信息
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getCorpExposureByCorpId(String corpId) {
        return fexCorpExposureDao.getCorpExposureByCorpId(corpId);
    }

    /**
     * 根据上级单位ID获取下级单位的集合
     * @param parentId
     * @return
     */
    public List<FexCorpExposureVO> listCorpExposureByParentId(String parentId) {
        return fexCorpExposureDao.listCorpExposureByParentId(parentId);
    }

    /**
     * 单位是否汇总
     * @param corpId 单位ID
     * @return true/false
     */
    public boolean collectFlagByCorpId(String corpId) {
        FexCorpExposureVO vo = this.getCorpExposureByCorpId(corpId);
        boolean result = false;
        if (!EmptyUtils.isEmpty(vo) && CommonConstant.YES.equals(vo.getIsCollect())
                && StringUtils.isNotEmpty(vo.getCorpParentId())) {
            result = true;
        }
        return result;
    }

    /**
     * 判断单位是否是汇总单位
     * @param corpId 单位ID
     * @return true/false
     */
    public boolean isCollectCorp(String corpId) {
        boolean result = false;
        //获取管理的单位
        List<FexCorpExposureVO> list = this.listCorpExposureByParentId(corpId);
        if (!EmptyUtils.isEmptyList(list)) {
            for (FexCorpExposureVO vo : list) {
                if (CommonConstant.YES.equals(vo.getIsCollect())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
