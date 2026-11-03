package com.bytter.dual.vo;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * Created by zhoul on 2017/9/1.
 */
public class DualManagerVO implements IBusinessKey {
    private String id;
    private String managerType;
    private String managerTypeName;
    private Integer status;
    private String approvePage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getManagerType() {
        return managerType;
    }
    public void setManagerType(String managerType) {
        this.managerType = managerType;
    }
    public String getManagerTypeName() {
        return managerTypeName;
    }
    public void setManagerTypeName(String managerTypeName) {
        this.managerTypeName = managerTypeName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getApprovePage() {
        return approvePage;
    }
    public void setApprovePage(String approvePage) {
        this.approvePage = approvePage;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
