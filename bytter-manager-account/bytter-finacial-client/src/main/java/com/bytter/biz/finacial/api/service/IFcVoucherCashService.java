package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcVoucherCashDTO;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description 
 */
public interface IFcVoucherCashService {
    FcVoucherCashDTO getItemBySubject(String debitItem, String creditItem);
}
