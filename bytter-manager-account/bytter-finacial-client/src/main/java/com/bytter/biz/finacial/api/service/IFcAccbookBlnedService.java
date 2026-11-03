package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import java.util.List;
import java.util.Map;
public interface IFcAccbookBlnedService {
    /**
     * 列表查询日结完成后的账务数据
     * @param dataTablesInput
     * @param accList
     * @return
     */
    DataTablesOutput listGenAcc(DataTablesInput dataTablesInput, List<String> accList);
    /**
     * 内部账号凭证保存为对账单数据
     * @param params
     * @param accList
     * @param orderNos
     * @return
     */
    List<FcCompareAccDTO> queryCompareAcc(Map<String,Object> params, List<String> accList, List<String> orderNos);
    FcAccbookDTO queryCompareAccBy(FcAccbookDTO dto);

    List<FcCompareAccDTO> getVoucherNosByOrigVoucherNo(String origVoucherNo);

}
