package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.RegionQueryDao;
import com.bytter.biz.erp.entity.ErpRegionQueryView;
import com.bytter.biz.erp.vo.request.RegionRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName RegionQueryService
 * @Author zx
 * @Description 省市查询接口实现
 * @Date 2026/4/28 15:27
 */
@Service("regionQueryService")
public class RegionQueryService {

    @Autowired
    private RegionQueryDao regionQueryDao;

    /**
     * 查询省市信息
     *
     * @param reqVo
     * @param tenantId
     * @return
     */
    public List<ErpRegionQueryView> queryRegionList(RegionRequestVO reqVo, String tenantId) {
        return regionQueryDao.queryRegionList(reqVo, tenantId);
    }

}
