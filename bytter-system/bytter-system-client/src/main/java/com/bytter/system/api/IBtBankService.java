package com.bytter.system.api;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankDTO;
import java.util.List;
import java.util.Map;
/**
 * Created by zuoy on 2017/7/20.
 */
public interface IBtBankService {
    /**
     * 通过银行代码查找联行号
     *
     * @param bankCode
     */
    BtBankDTO getByBankCode(String bankCode);
    /**
     * 通过银行名称获取银行信息
     *
     * @param bankName
     */
    BtBankDTO getByBankName(String bankName);
    /**
     * 根据联行号代码初始化BtBank
     *
     * @param bankCode   联行号代码
     * @param bankTypeId 银行类别ID
     */
    void saveBtBank(String bankCode, String bankTypeId);

    List<BtBankDTO> getByBankNames(List<String> bankName);
    /**
     * 根据银行类别ID查询
     *
     * @param bankTypeId 银行类别ID
     * @return
     */
    List<BtBankDTO> getByBankTypeId(String bankTypeId);
    public List<BtBankDTO> getBankUnionList();
    Map<String,BtBankDTO> getBankUnionMap();
    void saveBtBank(List<BtBankAccDTO> entityList);
    /**
     * 根据联行号code获取银行信息
     *
     * @param codes 联行号集合
     * @return 银行信息
     */
    List<BtBankDTO> getListByCodes(List<String> codes);

    BtBankDTO getBankName(String bankCode);
}
