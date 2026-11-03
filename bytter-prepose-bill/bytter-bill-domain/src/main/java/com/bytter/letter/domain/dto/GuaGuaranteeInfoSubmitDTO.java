package com.bytter.letter.domain.dto;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import lombok.Data;

@Data
public class GuaGuaranteeInfoSubmitDTO {

    private GuaGuaranteeInfoVO lgInfoVo;
    private RatUrationDTO urationDTO;
}
