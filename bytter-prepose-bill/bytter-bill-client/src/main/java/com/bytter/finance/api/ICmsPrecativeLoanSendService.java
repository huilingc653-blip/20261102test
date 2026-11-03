package com.bytter.finance.api;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import java.util.List;
public interface ICmsPrecativeLoanSendService {
    CmsPrecativeLoanSendDTO getPrecativeLoanSendVoByVo(CmsPrecativeLoanSendDTO vo);

    CmsPrecativeLoanSendDTO getById(String id);
    
    public List<CmsPrecativeLoanRepayDTO> queryRepayListByBillCodes(List<String> billCodes);
}
