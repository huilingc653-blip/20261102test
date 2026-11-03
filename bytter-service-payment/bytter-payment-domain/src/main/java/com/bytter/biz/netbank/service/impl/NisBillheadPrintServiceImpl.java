package com.bytter.biz.netbank.service.impl;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.service.INisBillheadPrintService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
/**
 * @author huangx
 * @Description 付款支票打印接口
 * @date 2018/11/23
 */
@Service("nisBillheadPrintServiceImpl")
@DubboService
@Transactional
public class NisBillheadPrintServiceImpl implements INisBillheadPrintService {
    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisMessageService nisMessageService;
    @Override
    public void printComplete(String dataId, String vouNo,ICurrentUser userVo) {
        NisBillhead entity = nisBillheadService.get(dataId, true);
        //修改凭证号
        entity.setVoucherNo(vouNo);
        entity.setUpdateBy(userVo.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        nisBillheadService.saveOrUpdate(entity);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_BILL_PRINT, null, NisMessageConstant.BUS_TYPE_PAY, userVo);
    }
    @Override
    public DataTablesOutput<Map<String, Object>> listBvPrintVouOp(DataTablesInput input, String listFields, String where, Map<String, Object> params, boolean isOrder) {
        return null;
    }
}
