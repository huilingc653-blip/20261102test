package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ExternalCorpAccQueryDao;
import com.bytter.biz.erp.entity.ErpExternalCorpAccQueryView;
import com.bytter.biz.erp.vo.request.ExternalCorpAccQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客商账号信息查询服务。
 *
 * @author zx
 * @date 2026/4/29
 */
@Service("externalCorpAccQueryService")
public class ExternalCorpAccQueryService {

    @Autowired
    private ExternalCorpAccQueryDao externalCorpAccQueryDao;

    /**
     * 按请求条件查询客商账号列表。
     *
     * @param reqVo    查询条件（客商账号编码、名称等）
     * @param tenantId 租户 ID，与视图数据隔离
     * @return 客商账号视图列表；无匹配时为空列表（非 {@code null}）
     */
    public List<ErpExternalCorpAccQueryView> queryExternalCorpAccList(ExternalCorpAccQueryRequestVO reqVo, String tenantId) {
        return externalCorpAccQueryDao.queryExternalCorpAccList(reqVo, tenantId);
    }
}
