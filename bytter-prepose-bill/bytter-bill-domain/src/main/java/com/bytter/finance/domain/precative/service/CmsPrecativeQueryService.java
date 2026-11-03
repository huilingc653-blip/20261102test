package com.bytter.finance.domain.precative.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanDeferredDao;
import com.bytter.finance.domain.precative.dao.CmsPrecativeLoanRepayDao;
import com.bytter.finance.domain.precative.dao.CmsPrecativeQueryDao;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanDeferredVo;
import com.bytter.finance.domain.precative.vo.PrecativeQueryVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Service
 * @Description: 委托贷款查询
 * @author onlineGenerator
 * @date 2017-07-29 10:45:29
 * @version 1.0
 *
 */
@Service("cmsPrecativeQueryService")
@Transactional
public class CmsPrecativeQueryService extends BaseService<PrecativeQueryVo, String> {
    @Autowired
    private CmsPrecativeQueryDao cmsPrecativeQueryDao;
    @Autowired
    private CmsPrecativeLoanRepayDao cmsPrecativeLoanRepayDao;
    @Autowired
    private CmsPrecativeLoanDeferredDao cmsPrecativeLoanDeferredDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<PrecativeQueryVo, String> getEntityDao() {
        return cmsPrecativeQueryDao;
    }
    public DataTablesOutput dataTableQuery(DataTablesInput dataTablesInput, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_REPORT_QUERY, userId);
        return cmsPrecativeQueryDao.dataTableQuery(dataTablesInput, corpAuth);
    }
    /** 查询在途金额列表 **/
    public List<CmsPrecativeLoanRepayVo> queryPassageList(String billCode) {
        return cmsPrecativeLoanRepayDao.queryRepayList(billCode, "passage");
    }
    /** 查询已还金额列表 **/
    public List<CmsPrecativeLoanRepayVo> queryRepayList(String billCode) {
        return cmsPrecativeLoanRepayDao.queryRepayList(billCode, "");
    }
    /** 查询展期列表 **/
    public List<PrecativeLoanDeferredVo> queryDeferList(String billCode) {
        return cmsPrecativeLoanDeferredDao.getPrecativeLoanSendListByBillCode(billCode);
    }
    /**
     * 关闭归还的消息提醒
     */
    /*
     * public void checkRepayMessage(List<CmsPrecativeLoanRepayVo> list) { for (CmsPrecativeLoanRepayVo repay : list) {
     * notificationService.complete(NotificationConstant.CMS.PRECATIVE_REPAY_SUCCESS, repay.getId()); } }
     */
}
