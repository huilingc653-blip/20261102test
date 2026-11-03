package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatBusinessRace;
import lombok.Data;

import java.io.Serializable;

/**
 * 业务种类 编辑页面数据
 */
@Data
public class RatBusinessRaceFormVO implements Serializable {
    private RatBusinessRace ratRace;
    private RatItemCustomVO customVO;
}
