package com.bytter.finance.domain.lease.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.finance.domain.lease.dao.LeaseObjectInfoDao;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service("leaseObjectInfoImportService")
public class LeaseObjectInfoImportService extends BaseService<LeaseObjectInfo, String>
        implements IUserExcelVerifyHandler<ContractLeaseObjectInfoVO> {
    @Autowired
    private LeaseObjectInfoDao leaseObjectInfoDao;
    @Autowired
    private LeaseBasedataService leaseBasedataService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Override
    public BaseDao<LeaseObjectInfo, String> getEntityDao() {
        return leaseObjectInfoDao;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(ContractLeaseObjectInfoVO contractLeaseObjectInfoVO,
            Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<ContractLeaseObjectInfoVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (ContractLeaseObjectInfoVO leaseObjectInfoVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(leaseObjectInfoVO.getLeaseNo())) {
                    errorMsgList.add("租赁单号不能为空");
                } else {// 判断合同编号是否存在
                    CmsFinancingLeaseInfoVO vo = leaseObjectInfoDao.checkItem(leaseObjectInfoVO.getLeaseNo());
                    if (EmptyUtils.isEmpty(vo)) {
                        errorMsgList.add("没有对应的租赁单号");
                    } else {
                        leaseObjectInfoVO.setLeaseId(vo.getId());
                    }
                }
                if (EmptyUtils.isEmpty(leaseObjectInfoVO.getLeaseItemCode())) {
                    errorMsgList.add("租赁物编号不能为空");
                }
                if (EmptyUtils.isEmpty(leaseObjectInfoVO.getLeaseItemName())) {
                    errorMsgList.add("租赁物名称不能为空");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getName())) {
                    LeaseItemType leaseItemType = leaseBasedataService.getLeaseTypebyName(leaseObjectInfoVO.getName());
                    if (EmptyUtils.isEmpty(leaseItemType)) {
                        errorMsgList.add("租赁物类别不存在");
                    } else {
                        leaseObjectInfoVO.setLeaseId(leaseItemType.getId());
                    }
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getOldMoney())
                        && leaseObjectInfoVO.getOldMoney().compareTo(BigDecimal.ZERO) < 0) {
                    errorMsgList.add("原值金额不能小于0");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getNumb()) && leaseObjectInfoVO.getNumb() < 0) {
                    leaseObjectInfoVO.setNumb(0);
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getNowMoney())) {
                    if (leaseObjectInfoVO.getNowMoney().compareTo(BigDecimal.ZERO) < 0) {
                        errorMsgList.add("现值金额不能小于0");
                    }
                } else {
                    errorMsgList.add("现值金额不能为空");
                }
                if (!EmptyUtils.isEmpty(leaseObjectInfoVO.getCurName())) {
                    String curId = this.btCurrencyService.getCurrencyByName(leaseObjectInfoVO.getCurName());
                    if (!EmptyUtils.isEmpty(curId)) {
                        leaseObjectInfoVO.setCurId(curId);
                    } else {
                        errorMsgList.add("币种不存在");
                    }
                } else {
                    errorMsgList.add("币种不能为空");
                }
                if (!ListUtils.isNullOrEmpty(errorMsgList)) {
                    leaseObjectInfoVO.addErrorMsg(errorMsgList);
                }
            }
        }
    }
}
