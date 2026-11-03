package com.bytter.system.api;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bytter.system.api.dto.FcExcrateAdjustDTO;
import com.bytter.system.api.vo.FbsBudgetProcessVo;
/**
 * 货币汇率维护接口
 * create by zuoy 2017-10-17
 */
public interface IFcExcrateAdjustService {
    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    FcExcrateAdjustDTO get(String id);
    /**
     * 查询货币汇率
     *
     * @param curId   币别ID
     * @param preTime 执行日期
     * @return BigDecimal
     * @deprecated 废弃
     */
    BigDecimal getExchangeRateBy(String curId, Date preTime);
    /**
     * 查询货币汇率
     *
     * @param curId   币别ID
     * @param preTime 执行日期
     * @param typeId  汇率类型id
     * @return BigDecimal
     * @deprecated 废弃
     */
    BigDecimal getExchangeRateBy(String curId, Date preTime, String typeId);
    /**
     * 查询货币汇率
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return BigDecimal
     */
    BigDecimal getExchangeRateBy(String originalCurId, String convertCurId, Date preTime, String typeId);
    
    Map<String,BigDecimal> getExchangeRateByMap(List<FbsBudgetProcessVo> list, String convertCurId, String typeId);
    /**
     * 查询货币汇率
     * 不强制进行错误校验，查询为空返回null
     * 时间处理(分秒时)：将指定时间如[2020-01-01]转换成[2020-01-01 23:59:59]
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return BigDecimal
     */
    BigDecimal getExchangeRateByNoCheck(String originalCurId, String convertCurId, Date preTime, String typeId);
    /**
     * 查询货币汇率 小数位数
     *
     * @param typeId 汇率类型id
     * @return
     */
    Integer getRateDecimalPlaces(String typeId);
}
