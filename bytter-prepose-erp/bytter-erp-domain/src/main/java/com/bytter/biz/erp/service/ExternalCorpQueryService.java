package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ExternalCorpQueryDao;
import com.bytter.biz.erp.entity.ErpExternalCorpQueryView;
import com.bytter.biz.erp.vo.request.ExternalCorpQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客商信息查询服务。
 *
 * @author zx
 * @date 2026/4/29
 */
@Service("externalCorpQueryService")
public class ExternalCorpQueryService {

    @Autowired
    private ExternalCorpQueryDao externalCorpQueryDao;

    /**
     * 按请求条件查询客商列表。
     *
     * @param reqVo    查询条件（客商编码、名称等）
     * @param tenantId 租户 ID，与视图数据隔离
     * @return 客商视图列表；无匹配时为空列表（非 {@code null}）
     */
    public List<ErpExternalCorpQueryView> queryExternalCorpList(ExternalCorpQueryRequestVO reqVo, String tenantId) {
        return externalCorpQueryDao.queryExternalCorpList(reqVo, tenantId);
    }
}
