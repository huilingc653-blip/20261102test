package com.bytter.biz.finacial.accbook.service.impl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.service.FcAccbookBlnedService;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookBlnedService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
@Service("fcAccbookBlnedServiceImpl")
@DubboService
@Transactional
public class FcAccbookBlnedServiceImpl implements IFcAccbookBlnedService {
    @Autowired
    private FcAccbookBlnedService fcAccbookBlnedService;
    /**
     * 对账查询日结后账务数据
     * @param dataTablesInput
     * @param accList
     * @return
     */
    @Override
    public DataTablesOutput listGenAcc(DataTablesInput dataTablesInput, List<String> accList) {
        return fcAccbookBlnedService.listGenAcc(dataTablesInput,accList);
    }
    /**
     * 内部账号凭证保存为对账单数据
     * @param params
     * @param accList
     * @param orderNos
     * @return
     */
    @Override
    public List<FcCompareAccDTO> queryCompareAcc(Map<String, Object> params, List<String> accList, List<String> orderNos) {
        return fcAccbookBlnedService.queryCompareAcc(params,accList,orderNos);
    }
    @Override
    public FcAccbookDTO queryCompareAccBy(FcAccbookDTO dto) {
        return fcAccbookBlnedService.queryCompareAcc(dto);
    }
    @Override
    public List<FcCompareAccDTO> getVoucherNosByOrigVoucherNo(String origVoucherNo) {
        return fcAccbookBlnedService.getAccbookDtlsByOrigVoucherNo(origVoucherNo);
    }
}
