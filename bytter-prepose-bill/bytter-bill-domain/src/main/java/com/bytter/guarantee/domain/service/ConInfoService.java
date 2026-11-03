package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.dao.ConInfoDao;
import com.bytter.guarantee.domain.entity.ConInfo;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
/**
 * @Title: 合同主信息业务Service
 * @Description: 合同主信息业务处理类
 * @author zhenghj
 * @date 2019-11-04
 * @version 1.0
 *
 */
@Service("conInfoService")
@Transactional
public class ConInfoService extends BaseService<ConInfo, String> implements ICorpDeleteVerifyService {
    @Autowired
    private ConInfoDao conInfoDao;
    @Resource
    private IMultiLangService multiLangServiceI;
    public BaseDao<ConInfo, String> getEntityDao() {
        return conInfoDao;
    }
    /**
     * 根据id修改合同主表数据状态
     *
     * @param id
     *            合同主表ID
     * @param status
     *            状态
     * @return 修改成功的笔数
     */
    public int updateConInfoStatusById(String id, Integer status) {
        String[] ids = { id };
        if (StringUtils.isEmpty(ids) || status == null) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("status", status);
        int num = conInfoDao.batchUpdate(params);
        return num;
    }
    /**
     * 根据id修改合同占用状态
     *
     * @param id
     *            合同主表ID
     * @param isHook
     *            true持有，false取消持有
     * @return 修改成功的笔数
     */
    public int hookConOp(String id, boolean isHook) {
        String[] ids = { id };
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (isHook) {
            params.put("hookSign", ConConstant.CON_BUSHOOK_VALID_SIGN_1);
        } else {
            params.put("hookSign", ConConstant.CON_BUSHOOK_VALID_SIGN_0);
        }
        int num = conInfoDao.batchUpdate(params);
        return num;
    }
    /**
     * 根据id修改合同主表数据状态
     *
     * @param id
     *            合同主表ID
     * @param status
     *            状态
     * @return 修改成功的笔数
     */
    public int updateConInfoByConInfoVO(ConInfoVO conVO) {
        if (StringUtils.isEmpty(conVO.getId())) {
            return 0;
        }
        String[] ids = { conVO.getId() };
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (StringUtils.isNotEmpty(conVO.getIsChangeNum())) {
            params.put("isChangeNum", conVO.getIsChangeNum());
        }
        if (StringUtils.isNotEmpty(conVO.getIsPerformanceNum())) {
            params.put("isPerformanceNum", conVO.getIsPerformanceNum());
        }
        if (StringUtils.isNotEmpty(conVO.getStatus())) {
            params.put("status", conVO.getStatus());
        }
        int num = conInfoDao.batchUpdate(params);
        return num;
    }
    /**
     * 通过参数获取合同主信息
     * 
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConInfo> getConInfo(ConInfoVO conVO) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(conVO.getId())) {
            params.put("id", conVO.getId());
        }
        if (StringUtils.isNotEmpty(conVO.getBusId())) {
            params.put("busId", conVO.getBusId());
        }
        if (StringUtils.isNotEmpty(conVO.getRecordId())) {
            params.put("recordId", conVO.getRecordId());
        }
        return conInfoDao.getConInfoByParams(params);
    }
    /**
     * 根据id修改合同主信息
     *
     * @param id
     *            合同主表ID
     * @param status
     *            状态
     * @return 修改成功的笔数
     */
    public int updateConInfoById(String id, Integer status) {
        String[] ids = { id };
        if (StringUtils.isEmpty(ids) || status == null) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("status", status);
        params.put("signDate", new Date());
        int num = conInfoDao.batchUpdate(params);
        return num;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        // 禁用划分细则
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            // 单位存在合同信息，不允许删除！
            int num = conInfoDao.getValidContractNumByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CON,
                        SysConfigConstant.BusType.CON_TRACT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CON_VALID_CONTRACT_ERROR)));
            }
            if (ListUtils.isNotNullOrEmpty(verifyErrorDTOS)) {
                return verifyErrorDTOS;
            }
        }
        return null;
    }
}
