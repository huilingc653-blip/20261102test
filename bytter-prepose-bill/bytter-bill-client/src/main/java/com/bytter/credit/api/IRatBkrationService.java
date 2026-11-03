package com.bytter.credit.api;

import java.util.List;
import com.bytter.credit.dto.BtBankDTO;

/**
 * @author luxy
 * @Title: IRatBkrationService
 * @ProjectName T3(3.2)
 * @Description: 授信对外接口
 * @date 2020/5/7 15:53
 */
public interface IRatBkrationService {
    /**
     * 设置授信挂接担保合同
     *
     * @param id 授信录入id
     */
    void occupyGuaContract(String id);
    /**
     * 设置授信取消挂接担保合同
     *
     * @param id 授信录入id
     */
    void cancelGuaContract(String id);
    
    List<BtBankDTO> queryRationBank();
}
