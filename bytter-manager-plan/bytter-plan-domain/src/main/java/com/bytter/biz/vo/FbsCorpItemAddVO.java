package com.bytter.biz.vo;

import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import lombok.Data;

import java.util.List;

/**
 * 新增科目VO
 */
@Data
public class FbsCorpItemAddVO {
    List<FbsCorpItem> itemCorp;
    //默认所有的有效计划科目
    List<FbsItemDTO> listItem;
}
