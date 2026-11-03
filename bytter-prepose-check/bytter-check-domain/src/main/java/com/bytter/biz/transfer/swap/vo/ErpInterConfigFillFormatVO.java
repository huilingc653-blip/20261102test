package com.bytter.biz.transfer.swap.vo;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import java.util.List;
/**
 * Created by zhangxb on 2018/11/29.
 */
public class ErpInterConfigFillFormatVO implements java.io.Serializable {
    private String type;
    private List<ErpInterConfigFill> erpInterConfigFillList;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<ErpInterConfigFill> getErpInterConfigFillList() {
        return erpInterConfigFillList;
    }
    public void setErpInterConfigFillList(List<ErpInterConfigFill> erpInterConfigFillList) {
        this.erpInterConfigFillList = erpInterConfigFillList;
    }
}
