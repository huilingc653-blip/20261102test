package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.velocity.tools.config.DefaultKey;
/**
 * 币别标签
 *
 * @author zuoy 2017/11/21
 */
@DefaultKey("currencyTag")
public class CurrencyTag {
    private final IBtCurrencyService currencyService;
    public CurrencyTag() {
        this.currencyService = ApplicationContextUtil.getContext().getBean("btCurrencyServiceI", IBtCurrencyService.class);
    }
    /**
     * 根据ID获取币别符号
     *
     * @param id
     * @return
     */
    public String getCurSign(String id) {
        if (EmptyUtils.isBlank(id)) {
            return "";
        }
        String curSign = currencyService.getCurSign(id);
        if (EmptyUtils.isBlank(curSign)) {
            return "";
        }
        return curSign;
    }
    /**
     * 获取币别对象
     *
     * @param id
     * @return
     */
    public Object getBtCurrency(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtCurrencyDTO currencyDTO = currencyService.getObjectById(id);
        return EmptyUtils.isEmpty(currencyDTO) ? null : currencyDTO;
    }
    /**
     * 获取本位币信息
     *
     * @return
     */
    public Object getNatualCurrency() {
        BtCurrencyDTO currencyDTO = currencyService.getNatualCurrency();
        return EmptyUtils.isEmpty(currencyDTO) ? null : currencyDTO;
    }
    /**
     * 获取人民币信息
     *
     * @return
     */
    public Object getRMBCurrency() {
        BtCurrencyDTO currencyDTO = currencyService.getRMBCurrency();
        return EmptyUtils.isEmpty(currencyDTO) ? null : currencyDTO;
    }
    /**
     * 获取币别名称
     *
     * @param id
     * @return
     */
    public String getCurName(String id) {
        if (EmptyUtils.isBlank(id)) {
            return "";
        }
        BtCurrencyDTO currencyDTO = currencyService.getObjectById(id);
        return EmptyUtils.isEmpty(currencyDTO) ? "" : currencyDTO.getCurName();
    }
}
