package com.bytter.biz.erp.service;

import com.bytter.biz.erp.dao.ItemQueryDao;
import com.bytter.biz.erp.entity.ErpItemQueryView;
import com.bytter.biz.erp.vo.request.ItemQueryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ItemQueryService
 * @Author zx
 * @Description 预算科目查询接口实现
 * @Date 2026/4/28 15:27
 */
@Service("itemQueryService")
public class ItemQueryService {

    @Autowired
    private ItemQueryDao itemQueryDao;

    /**
     * 查询预算科目信息
     *
     * @param reqVo
     * @param tenantId
     * @return
     */
    public List<ErpItemQueryView> queryItemList(ItemQueryRequestVO reqVo, String tenantId) {
        return itemQueryDao.queryItemList(reqVo, tenantId);
    }

}
