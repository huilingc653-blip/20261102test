package com.bytter.finance.domain.provide.service;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.provide.vo.CmsBudgetVO;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("cmsBudgetService")
@Transactional
public class CmsBudgetService {
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    /**
     * 判断单位与 业务名次是否挂接 资金预算
     * 
     * @param corpId
     *            单位ID
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBudget(String corpId) {
        if (StringUtils.isNotEmpty(corpId) && fbsBudgetProcessService.corpCtlBudget(corpId)) {
            return true;
        }
        return false;
    }
    /**
     * 是否挂机预算系统
     * 
     * @param sysName
     *            系统名称
     * @param businessName
     *            业务名称
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isBudget(String sysName, String businessName) {
        if (configService.getConfig(sysName, businessName).toBoolean()) {
            return true;
        }
        return false;
    }
    /**
     * 占用预算
     * 
     * @param cmsVO
     */
    public void batchEngross(CmsBudgetVO cmsVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(cmsVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < cmsVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(cmsVO.getStatus());// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(cmsVO.getBillNo());// 业务单据号
                fbsBudgetProcessDTO.setItemCode(cmsVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(cmsVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(cmsVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(cmsVO.getProcessDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(cmsVO.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(cmsVO.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(cmsVO.getBusinessName());// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(cmsVO.getProcessSysName());// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(cmsVO.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(cmsVO.getCreateDate());
                fbsBudgetProcessDTO.setExchangeRate(cmsVO.getExchangeRate()[i]);
                fbsBudgetProcessDTO.setProcessCurMoney(cmsVO.getProcessCurMoney()[i]);
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(cmsVO.getBusinessName(), cmsVO.getProcessSysName(),
                    cmsVO.getBillNo());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
}
