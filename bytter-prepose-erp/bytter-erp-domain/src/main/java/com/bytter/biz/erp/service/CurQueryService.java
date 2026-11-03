package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.CurQueryDao;
import com.bytter.biz.erp.entity.ErpCurQueryView;
import com.bytter.biz.erp.vo.request.CurQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 币别信息查询服务。
 *
 * @author zx
 * @date 2026/4/29
 */
@Service("curQueryService")
public class CurQueryService {

    @Autowired
    private CurQueryDao curQueryDao;

    /**
     * 按请求条件查询币别列表。
     *
     * @param reqVo    查询条件（币别代码、名称、简码、更新日期下界等）
     * @param tenantId 租户 ID，与视图数据隔离
     * @return 币别视图列表，无匹配时为空列表（非 {@code null}）
     */
    public List<ErpCurQueryView> queryCurList(CurQueryRequestVO reqVo, String tenantId) {
        return curQueryDao.queryCurList(reqVo, tenantId);
    }
}
