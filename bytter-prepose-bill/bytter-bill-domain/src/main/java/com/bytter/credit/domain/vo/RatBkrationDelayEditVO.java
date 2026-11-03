package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatBkrationDelay;
import lombok.Data;

import java.io.Serializable;

/**
 * 授信延期表编辑页面数据
 */
@Data
public class RatBkrationDelayEditVO implements Serializable {
    private RatBkrationDelay delay;
    private RatBkrationPageVO ratBkrationPage;
    private String isGuarantee;
}
