package com.bytter.biz.finacial.inneraccount.service;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.finacial.inneraccount.dao.FcAccDetailBudgetMatchDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户流水
 * @date 2017-06-23 19:36:12
 */
@Service("fcAccDetailBudgetMatchService")
@Transactional
public class FcAccDetailBudgetMatchService extends BaseService<FcAccDetailBudgetMatchVO, String> {
    @Autowired
    private FcAccDetailBudgetMatchDao fcAccDetailBudgetMatchDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService iFcExcrateAdjustService;

    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    @Override
    public BaseDao<FcAccDetailBudgetMatchVO, String> getEntityDao() {
        return fcAccDetailBudgetMatchDao;
    }
    /**
     * 查询内部账号流水列表
     *
     * @param dataTablesInput
     * @return 没有被调用，注释
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcAccDetailBudgetMatchDao.getDataTablesUnion(dataTablesInput, null);
    }
    /**
     * 根据id查找明细信息
     *
     * @param id
     * @return 上游没有被调用，注释
     */
    public FcAccDetailBudgetMatchVO getInfoById(String id) {
        return fcAccDetailBudgetMatchDao.getInfoById(id);
    }

    public FcAccDetailBudgetMatchVO getItemInfoById(String id) {
        FcAccDetailBudgetMatchVO info = fcAccDetailBudgetMatchDao.getInfoById(id);
        this.setItem(info);
        return info;
    }


    public void setItem(FcAccDetailBudgetMatchVO vo) {
        if(vo!=null){
            String billNo = vo.getVoucherNo();
            if (EmptyUtils.isBlank(billNo)) {
                billNo = vo.getId();
            }
            billNo = billNo + "_" + vo.getCdSign();
            List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
            if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
                vo.setProcessDate(DateUtils.date2Str(fbsBudgetProcessDTOList.get(0).getProcessDate(), new SimpleDateFormat("yyyy-MM-dd")));
            }
            this.setItemInfo(vo, fbsBudgetProcessDTOList);
            BigDecimal curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), null);
            vo.setCurExrate(curExrate);
        }
    }

    public void setItemInfo(FcAccDetailBudgetMatchVO vo, List<FbsBudgetProcessDTO> fbsBudgetProcessDTOList) {
        StringBuilder item = new StringBuilder();
        StringBuilder itemInfoText = new StringBuilder();
        if (!EmptyUtils.isEmptyList(fbsBudgetProcessDTOList)) {
            for (int j = 0; j < fbsBudgetProcessDTOList.size(); j++) {
                if (j == 0) {
                    item.append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    item.append(fbsBudgetProcessDTOList.get(j).getItemName()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    item.append(fbsBudgetProcessDTOList.get(j).getProcessMoney().toString());
                    itemInfoText.append("(").append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(")")
                            .append(fbsBudgetProcessDTOList.get(j).getItemName());
                } else {
                    item.append(CommonConstant.COMMA_SPLIT).append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    item.append(fbsBudgetProcessDTOList.get(j).getItemName()).append(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    item.append(fbsBudgetProcessDTOList.get(j).getProcessMoney().toString());
                    itemInfoText.append(CommonConstant.COMMA_SPLIT).append("(").append(fbsBudgetProcessDTOList.get(j).getItemCode()).append(")")
                            .append(fbsBudgetProcessDTOList.get(j).getItemName());
                }
            }
        }
        vo.setItemInfo(item.toString());
        vo.setItemInfoText(itemInfoText.toString());
    }
}
