package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.CorpQueryDao;
import com.bytter.biz.erp.entity.ErpCorpQueryView;
import com.bytter.biz.erp.vo.request.CorpQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单位信息查询接口服务。
 *
 * @author zx
 * @date 2026/4/29
 */
@Service("corpQueryService")
public class CorpQueryService {

    @Autowired
    private CorpQueryDao corpQueryDao;

    /**
     * 根据查询条件获取单位信息列表。
     *
     * @param reqVo 查询请求参数
     * @param tenantId 租户ID
     * @return 单位信息结果列表
     */
    public List<ErpCorpQueryView> queryCorpList(CorpQueryRequestVO reqVo, String tenantId) {
        return corpQueryDao.queryCorpList(reqVo, tenantId);
    }

}
