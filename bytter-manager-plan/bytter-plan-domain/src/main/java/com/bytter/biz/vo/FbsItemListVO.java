package com.bytter.biz.vo;

import com.bytter.biz.budget.vo.FbsItemVO;
import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class FbsItemListVO {
    List<FbsItemVO>  treeData;
    FbsItemVO  fbsItemPage;
    String maxLevelShow;
}
