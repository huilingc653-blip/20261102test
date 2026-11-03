package com.bytter.check.api;
import com.bytter.check.dto.FcCompareAccBeanDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/21.
 * @description
 */
public interface IFcCompareCenterService {
    List<BigDecimal> queryAccBalanceByDate(String account, Date balanceDate);

    List<FcCompareAccBeanDTO> getCompAccByOrderNo(List<FcCompareAccBeanDTO> dtos);
}
