package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.util.List;
/**
 * 预算上报父子关系
 * @author qijh
 */
public class FbsBudgetPaternityVO implements Serializable {
    /***父标题*/
    private String paternal;
    /***子标题*/
    private List<String> childList;
    /***父预算*/
    private FbsBudgetVO paternalFbsBudgetVO;
    /***子预算*/
    private List<FbsBudgetVO> childFbsBudgetVOList;
    public String getPaternal() {
        return paternal;
    }
    public void setPaternal(String paternal) {
        this.paternal = paternal;
    }
    public List<String> getChildList() {
        return childList;
    }
    public void setChildList(List<String> childList) {
        this.childList = childList;
    }
    public FbsBudgetVO getPaternalFbsBudgetVO() {
        return paternalFbsBudgetVO;
    }
    public void setPaternalFbsBudgetVO(FbsBudgetVO paternalFbsBudgetVO) {
        this.paternalFbsBudgetVO = paternalFbsBudgetVO;
    }
    public List<FbsBudgetVO> getChildFbsBudgetVOList() {
        return childFbsBudgetVOList;
    }
    public void setChildFbsBudgetVOList(List<FbsBudgetVO> childFbsBudgetVOList) {
        this.childFbsBudgetVOList = childFbsBudgetVOList;
    }
}
