package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 资金计划科目接口类
 *
 * @author zhenghj
 */
@Service("abFbsItemService")
@Transactional
public class AbFbsItemService extends BaseService<String, AbAccrecords> {
    @DubboReference(check = false)
	private IFbsItemService fbsItemService;
    @Override
    public BaseDao<String, AbAccrecords> getEntityDao() {
        return null;
    }
    /**
     * 根据科目代码查找科目信息
     *
     * @param itemCode 计划科目
     * @return 计划科目信息
     */
    public FbsItemDTO getFbsItemByCode(String itemCode) {
        if (StringUtils.isNotEmpty(itemCode)) {
            return fbsItemService.getFbsItemByCode(itemCode);
        }
        return null;
    }
}
