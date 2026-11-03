package com.bytter.credit.domain.vo;

import com.bytter.system.api.dto.CorpDTO;
import lombok.Data;

import java.util.List;

@Data
public class RatBkrationQueryCorpVO {
    private String code;
    private List<CorpDTO> corpDTO;
}
