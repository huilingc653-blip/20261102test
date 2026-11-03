package com.bytter.biz.vo;

import com.bytter.biz.budget.vo.FbsItemVO;
import lombok.Data;

@Data
public class FbsItemViewVO {

    /**
     *预算科目
     */
    private FbsItemVO fbsItemPage;
    /**
     *
     */
    private String isSubItem;

    /**
     * 原跳转链接  如果parentId==null 则跳转 budget/addFbsItem  如果parentId！=null 则跳转 budget/updateFbsItem
     */
    private String forwardUrl;
}
