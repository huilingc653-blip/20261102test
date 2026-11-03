package com.bytter.letter.domain.dto;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import lombok.Data;

@Data
public class GuaGuaranteeChangeSubmitDTO {
    private GuaGuaranteeChangeInfoVO lgChangeInfoVo;
    private RatUrationDTO urationDTO;
}
