package com.bytter.credit.domain.service;
import cn.hutool.core.util.NumberUtil;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.vm.BaseBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.IConfigService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * 抽取授信模块公共校验
 */
public abstract class BaseBkRationService {
    protected IConfigService configService;
    public void check(List<BaseBkRationDtlVm> baseBkRationDtlVms){
        for (BaseBkRationDtlVm baseBkRationDtlVm : baseBkRationDtlVms) {
            AssertHelper.isTrue(StringUtils.isNotBlank(baseBkRationDtlVm.getItemCode()),RatMessageEnum.CMS_RAT_DTL_ITEMCODE_ISNULL);
        }
    }
    public void rebuildRationDtlVm(List<BaseBkRationDtlVm> baseBkRationDtlVms) {
        //排序 重新设置序列
        AtomicInteger index = new AtomicInteger(1);
        baseBkRationDtlVms.stream().sorted(Comparator.comparing(BaseBkRationDtlVm::getItemFlag)
                        .thenComparing(BaseBkRationDtlVm::getItemOrder))
                .peek(item -> {
                    item.setItemOrder(index.getAndIncrement());
                    item.setMoney(NumberUtil.null2Zero(item.getMoney()));
                });
    }
    private void setRatBkrationDtls(RatBkRationVm rationtDTO) {
        List<RatBkRationDtlVm> dtls = new ArrayList<RatBkRationDtlVm>();
        List<RatBkRationDtlVm> rationDtls = rationtDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationDtlVm>();
        }
        if (EmptyUtils.isEmpty(rationtDTO.getIncludeBailmoney())) {
            rationtDTO.setIncludeBailmoney(RationConstant.RAT_COMMON_Y);// 包含保证金
        }
        BigDecimal money = null;
        BigDecimal openMoney = null;
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO && !StringUtils.isEmpty(dtlDTO.getItemFlag())) {
                dtls.add(dtlDTO);
            }
            // 取综合授信品种的授信金额
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                if (null != dtlDTO.getMoney()) {
                    money = dtlDTO.getMoney();
                    dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_1);// 综合授信使用标识
                }
                if (null != dtlDTO.getOpenMoney()) {
                    openMoney = dtlDTO.getOpenMoney();
                }
            }
            if (null == dtlDTO.getMoney()) {
                dtlDTO.setMoney(BigDecimal.ZERO);
            }
            if (dtlDTO.getOpenMoney().compareTo(BigDecimal.ZERO) == 0
                    && dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                dtlDTO.setOpenMoney(dtlDTO.getMoney());
            }
        }
        // 按照单品种和综合品种排序
        Collections.sort(dtls, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        // 重设itemorder
        Integer index = Integer.valueOf(1);
        for (RatBkRationDtlVm dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
            if (StringUtils.isEmpty(dtlDTO.getCompSign())) {
                dtlDTO.setCompSign(RationConstant.RAT_COMP_SIGN_0);// 默认都是0
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                // 所有的综合授信品种都设置同一个授信金额
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(BigDecimal.ZERO);
                }
            }
            if (null == dtlDTO.getOpenMoney()) {
                dtlDTO.setOpenMoney(dtlDTO.getMoney());
            }
        }
        rationtDTO.setRationDtls(dtls);
    }
}
