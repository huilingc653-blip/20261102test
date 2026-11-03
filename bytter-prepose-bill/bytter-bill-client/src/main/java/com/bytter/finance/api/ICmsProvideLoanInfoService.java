package com.bytter.finance.api;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import java.util.List;
/**
 * 银行借款接口
 * 
 * @author lvwl
 */
public interface ICmsProvideLoanInfoService {
    /**
     * 设置业务挂接担保合同
     * 
     * @param id
     *            业务id
     */
    CmsProvideLoanInfoDTO getInfoById(String id);
    /**
     * 获取挂接授信的借款ID集合
     * @return
     */
    List<String> getIdListByRat();

    /**
     * 根据发放单号获取借款信息
     * @param sendBillCode 订单号
     * @return
     */
    CmsProvideLoanInfoDTO getInfoByBillCode(String sendBillCode);
}
