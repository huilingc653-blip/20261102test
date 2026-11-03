package com.bytter.accountReceivable.writeOff.vo;

import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 应收单核销确认页面
 */
@Data
public class DocSureWriteOffVo implements java.io.Serializable {

    private boolean checkFlag;
    private String checkMsg;

    private String docCurId;
    private BigDecimal docMoney;
    private String relationCurId;
    private BigDecimal relationMoney;

    private List<DocWriteOffRelationVo> relationDataList;
    private List<NisReceivableDTO> nisReceivableDTOList;

    public DocSureWriteOffVo(boolean checkFlag) {
        this.checkFlag = checkFlag;
        this.relationDataList = new ArrayList<>();
    }
}
