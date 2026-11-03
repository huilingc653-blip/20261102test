package com.bytter.biz.payment.api.vo;

import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BtAssignPayWayAddVO implements Serializable {
    //获取有效的付款方式
    List<FcVoucherTypeDTO> voucherTypes;
    //获取分配的单位
    HashMap<String, String> corps;
    // 是否启用用户分配付款方式 0 否，1是
    String isEnableUserAssign;
}
