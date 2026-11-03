package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 授信占用申请表新增页面数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RatUrationApplyAddVO extends RatUrationApplyBaseVO implements Serializable {
    private RatBkrationCpAddListDTO ratBkrationPage;
}
