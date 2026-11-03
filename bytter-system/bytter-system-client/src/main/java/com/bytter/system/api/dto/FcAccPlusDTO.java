package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * 内部账号简化后DTO
 *
 * @author liyh
 */
public class FcAccPlusDTO implements Serializable {
    /**
     * 账号ID
     */
    private String id;
    /**
     * 账号
     */
    private String blnctrAcc;
    /**
     * 户名
     */
    private String accName;
    /**
     * 币别
     */
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 科目
     */
    private String subjectName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
