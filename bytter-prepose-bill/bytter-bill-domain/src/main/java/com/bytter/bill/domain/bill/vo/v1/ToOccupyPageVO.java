package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.ParamsVO;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ToOccupyPageVO implements Serializable {
    private  ParamsVO vo;
    private List<GuaPledgeInfoVO> list;
    private ConfigDTO config;

}
