package com.bytter.letter.domain.vo;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GuaGuaranteeChangeDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函更改信息 */
    private GuaGuaranteeChangeInfoVO lgChangeInfoVo;
    /** 是否挂接保证金 */
    private String isHangBail;
    /** 授信明细 */
    private RatUrationDTO urationDTO;
    /** 授信银行 */
    private String ratBankCode;
    /** 本位币标识 */
    private String naturalSign;
    /** 审批任务ID */
    private String taskId;
    /** 授信分项列表（手机端审批用） */
    private List<RatUrationDisDTO> rations;
}
