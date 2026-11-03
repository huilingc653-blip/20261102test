package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import org.apache.velocity.tools.config.DefaultKey;
import java.math.BigDecimal;
/**
 * 百分比标签。
 *
 * @author zhaohc
 * @date 2023/4/10 16:05
 */
@DefaultKey("excRateTag")
public class ExcRateTag {
    private final  IFcExcrateAdjustService fcExcrateAdjustService;
    public ExcRateTag() {
        this.fcExcrateAdjustService = ApplicationContextUtil.getContext().getBean("fcExcrateAdjustServiceI", IFcExcrateAdjustService.class);;
    }
    public String getConvert(BigDecimal numeric){
        String result = "";
        String typeId = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        Integer places = fcExcrateAdjustService.getRateDecimalPlaces(typeId);
        if(EmptyUtils.isEmpty(places)){
            places = 4;
        }
        String pattern = "#,##0.0000";
        if(places == 6){
            pattern = "#,##0.000000";
        }
        result = NumberUtil.formatNumeric(numeric,pattern);
        return result;
    }
    public String getBuget(BigDecimal numeric){
        String result = "";
        String typeId = FcExcrateAdjustConstant.RATE_TYPE_BUDGET;
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        Integer places = fcExcrateAdjustService.getRateDecimalPlaces(typeId);
        if(EmptyUtils.isEmpty(places)){
            places = 4;
        }
        String pattern = "#,##0.0000";
        if(places == 6){
            pattern = "#,##0.000000";
        }
        result = NumberUtil.formatNumeric(numeric,pattern);
        return result;
    }
}
