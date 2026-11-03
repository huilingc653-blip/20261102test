package com.bytter.basic.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.basic.service.FcExcrateAdjustService;
import com.bytter.basic.vo.FcExcrateAdjustVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.FcExcrateAdjustDTO;
import com.bytter.system.api.vo.FbsBudgetProcessVo;
/**
 * 资金汇率维护
 * Created by zy on 2017/10/17
 */
@Transactional
@DubboService
public class FcExcrateAdjustServiceImpl implements IFcExcrateAdjustService {
    @Autowired
    private FcExcrateAdjustService fcExcrateAdjustService;
    @Override
    public FcExcrateAdjustDTO get(String id) {
        FcExcrateAdjustVO vo = this.fcExcrateAdjustService.getByUnion(id);
        if (EmptyUtils.isEmpty(vo)) {
            return null;
        }
        FcExcrateAdjustDTO dto = new FcExcrateAdjustDTO();
        BeanUtils.copyNotNullProperties(vo, dto);
        return dto;
    }
    @Override
    public BigDecimal getExchangeRateBy(String curId, Date preTime) {
        return this.fcExcrateAdjustService.getExchangeRateBy(curId, preTime);
    }
    /**
     * 查询货币汇率
     *
     * @param curId   币别ID
     * @param preTime 执行日期
     * @param typeId  汇率类型id
     * @return id
     */
    @Override
    public BigDecimal getExchangeRateBy(String curId, Date preTime, String typeId) {
        return this.fcExcrateAdjustService.getExchangeRateBy(curId, preTime, typeId);
    }
    /**
     * 查询货币汇率
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return BigDecimal
     */
    @Override
    public BigDecimal getExchangeRateBy(String originalCurId, String convertCurId, Date preTime, String typeId) {
        return this.fcExcrateAdjustService.getExchangeRateBy(originalCurId, convertCurId, preTime, typeId);
    }
    
    /**
     * 查询货币汇率
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return BigDecimal
     */
    @Override
    public Map<String,BigDecimal> getExchangeRateByMap(List<FbsBudgetProcessVo> list, String convertCurId, String typeId) {
    	Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
    	for(FbsBudgetProcessVo vo:list) {
    		if(!map.containsKey(vo.getId())) {
    			map.put(vo.getId(), this.fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), convertCurId, DateUtils.setSpecifiedDateOfTime(vo.getProcessDate()), typeId));
    		}
    	}
    	return map;
    }
    
    @Override
    public BigDecimal getExchangeRateByNoCheck(String originalCurId, String convertCurId, Date preTime, String typeId) {
        if (preTime == null) {
            return null;
        }
        String checkTime = DateUtils.format(preTime, "yyyy-MM-dd HH:mm:ss");
        if (checkTime == null || checkTime.length() < 11) {
            return this.fcExcrateAdjustService.getExchangeRateByNoCheck(originalCurId, convertCurId, preTime, typeId);
        }
        String substring = checkTime.substring(10);
        if (" 00:00:00".equals(substring)) {
            String format = DateUtils.format(preTime, "yyyy-MM-dd");
            String concat = format.concat(" 23:59:59");
            Date parse = DateUtils.parse(concat, "yyyy-MM-dd HH:mm:ss");
            return this.fcExcrateAdjustService.getExchangeRateByNoCheck(originalCurId, convertCurId, parse, typeId);
        }
        return this.fcExcrateAdjustService.getExchangeRateByNoCheck(originalCurId, convertCurId, preTime, typeId);
    }
    /**
     * 查询货币汇率 小数位数
     *
     * @param typeId 汇率类型id
     * @return
     */
    @Override
    public Integer getRateDecimalPlaces(String typeId) {
        return this.fcExcrateAdjustService.getRateDecimalPlaces(typeId);
    }
}
