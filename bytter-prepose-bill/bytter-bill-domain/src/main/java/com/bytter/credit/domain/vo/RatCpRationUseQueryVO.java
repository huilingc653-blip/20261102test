package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 内部授信额度明细查询报表数据
 */
@Data
public class RatCpRationUseQueryVO implements Serializable {
    private List<RatUrationUseVO> rations;
    private String corpId;
    private UrationParamDTO paramDTO;
    private String naturalCur;
    private String userName;
    private Integer moneyType;
    private String reportDate;
    private ConfigDTO relateCon;
}
