package com.bytter.biz.bdlink.vo;


import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ViewReceiptsVO implements Serializable {
    List<BisBankReceiptDtlDTO> list;
    List<BisBankReceiptDtlHisVO> hislist;
}
