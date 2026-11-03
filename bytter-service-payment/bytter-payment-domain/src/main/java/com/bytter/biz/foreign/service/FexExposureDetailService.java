package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexExposureDetailDao;
import com.bytter.biz.foreign.entity.FexExposureDetail;
import com.bytter.biz.foreign.vo.FexExposureDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Title: Service
 * @Description: 单位敞口上报明细表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Service("fexExposureDetailService")
@Transactional
public class FexExposureDetailService extends BaseService<FexExposureDetail, String> {
    @Autowired
    private FexExposureDetailDao fexExposureDetailDao;

    @Override
    public BaseDao<FexExposureDetail, String> getEntityDao() {
        return fexExposureDetailDao;
    }

    /**
     * 根据敞口上报信息获取上报明细
     * @param exposureId
     * @return
     */
    public List<FexExposureDetailVO> listVOByExposureId(String exposureId) {
        return fexExposureDetailDao.listVOByExposureId(exposureId, "");
    }

    /**
     * 根据敞口上报信息获取上报明细
     * @param exposureId
     * @return
     */
    public List<FexExposureDetailVO> listVOByExposureId(String exposureId, String itemCode) {
        String itemCodeNew = "";
        if (StringUtils.isNotBlank(itemCode)) {
            for (String code : itemCode.split(CommonConstant.COMMA_SPLIT)) {
                String codeTemp = code;
                while (codeTemp.length() > 3) {
                    itemCodeNew += codeTemp + CommonConstant.COMMA_SPLIT;
                    codeTemp = code.substring(0, code.length() - 3);
                }
                itemCodeNew += codeTemp + CommonConstant.COMMA_SPLIT;
            }
        }
        return fexExposureDetailDao.listVOByExposureId(exposureId, itemCodeNew);
    }

    /**
     * 删除上报明细
     * @param id
     */
    public void deleteByMainId(String id) {
        fexExposureDetailDao.deleteByMainId(id);
    }

    /**
     * 根据科目获取上报明细
     * @param itemCode
     * @return
     */
    public List<FexExposureDetail> listByItemCode(String itemCode) {
        return fexExposureDetailDao.listBy("itemCode", itemCode);
    }

    /**
     * 根据子单获取上报明细集合
     * @param childMainIds
     * @return
     */
    public List<FexExposureDetailVO> getDtlByChildCorp(List<String> childMainIds, String curId, Date beginDate, Date endDate) {
        return fexExposureDetailDao.getDtlByChildCorp(childMainIds, curId, beginDate, endDate);
    }
}
