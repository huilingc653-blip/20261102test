package com.bytter.check.api;

import com.bytter.check.dto.FcCompareAccDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luoyj
 * @date 2023/8/21.
 */
public interface IFcCompareAccService {

    void deleteFcCompareAccByVoucherNoAndCloseDate(List<String> voucherNos, Date closeDate);

    List<FcCompareAccDTO> getBalanceByCondition(List<FcCompareAccDTO> fcCompareAccDTOList);

    void updateById(List<FcCompareAccDTO> dtos);

    List<FcCompareAccDTO> getFcCompareAccDTOBySql(String sql, Map<String, Object> params);

    void updateCompareAccBalance(Map<String, Object> params);
}
