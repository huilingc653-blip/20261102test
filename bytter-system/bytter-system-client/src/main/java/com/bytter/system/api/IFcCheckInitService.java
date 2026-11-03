package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.FcCheckInitDTO;
import java.util.List;
/**
 * 核算对账初始化接口
 */
public interface IFcCheckInitService {
    /**
     * 中心对账余额初始化
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    DataTablesOutput getFcCheckInit(DataTablesInput dataTablesInput, List<String> corpAuth,List<String> bankAcc);
    FcCheckInitDTO FcCheckInitList(String bankAcc,String account);
    /**
     * ERP余额初始化列表查询
     * @param dataTablesInput
     * @param corpAuth
     * @param bankAcc
     * @return
     */
    DataTablesOutput getFcCheckInitForErp(DataTablesInput dataTablesInput, List<String> corpAuth,List<String> bankAcc);
}
