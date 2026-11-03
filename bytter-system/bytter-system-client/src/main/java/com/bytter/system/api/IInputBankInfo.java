package com.bytter.system.api;
import com.bytter.system.api.dto.InputBankInfoDTO;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/6/30.
 */
public interface IInputBankInfo {
    /**
     * 通过银行代码查找联行号
     *
     * @param bankCode
     */
    InputBankInfoDTO getByBankCode(String bankCode);
    /**
     * 通过银行名称查找联行号
     *
     * @param bankName
     */
    InputBankInfoDTO getByBankName(String bankName);
    /**
     * <b>功能：</b>根据联行号获取银行名称<br>
     * <p>
     * 联行号
     *
     * 参考逻辑
     *    String sql = "select t.BANK_NAME,t.IBPS_CODE from bt_input_bank_info t where t.SYS_BANK_CODE='"
     *                 + paysysbankcode + "'";
     *         List<Map<String, Object>> objStr = this.listBySQL(sql, null);
     *         return objStr;
     *
     * @return 票据信息
     */
     List<Map<String, Object>> queryBankNameByPaysyBbankCode(String paysysbankcode);
}
