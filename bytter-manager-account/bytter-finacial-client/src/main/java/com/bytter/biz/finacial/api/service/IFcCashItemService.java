package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcCashItemDTO;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
public interface IFcCashItemService {
    FcCashItemDTO get(String cashFlux);
}
