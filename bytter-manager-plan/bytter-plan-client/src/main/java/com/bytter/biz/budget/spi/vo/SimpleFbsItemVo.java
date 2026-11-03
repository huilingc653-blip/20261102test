package com.bytter.biz.budget.spi.vo;

import java.io.Serializable;

public class SimpleFbsItemVo implements Serializable {

    private String id;

    private String corpId;

    /**科目代码*/
    private String itemCode;
    /**科目名称*/
    private String itemName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
