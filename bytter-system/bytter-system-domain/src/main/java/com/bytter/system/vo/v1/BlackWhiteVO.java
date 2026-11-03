package com.bytter.system.vo.v1;

import com.bytter.system.vo.BlackWhiteListVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 黑/白名单VO
 */
@Data
public class BlackWhiteVO implements Serializable {
    private String intercept;
    /**
     * 白名单信息
     */
    private BlackWhiteListVO blackWhite;
    private String operator;
}
