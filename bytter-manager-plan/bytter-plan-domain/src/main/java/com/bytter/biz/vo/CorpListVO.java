package com.bytter.biz.vo;

import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.vo.FbsCorpItemVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CorpListVO {
    List<FbsCorpItem>  itemCorp;
    private Boolean isSubCorp;
    private Map<String, String> superCorpItemCode;
    //所有的有效计划科目
    private List<FbsItemDTO> listItem;
    //根据单位id查找已分配的计划科目
    private List<FbsCorpItemVO> fbs;
    private String corpId;

}
