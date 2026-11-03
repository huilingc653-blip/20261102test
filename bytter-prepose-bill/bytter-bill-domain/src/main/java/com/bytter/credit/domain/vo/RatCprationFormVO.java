package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 内部授信录入 新增/编辑页面数据(共用)
 */
@Data
public class RatCprationFormVO implements Serializable {
    private RatBkRationVm ratCpration;
    private ConfigDTO relateCon;
    private ConfigDTO conType;
    private String conId;
}
