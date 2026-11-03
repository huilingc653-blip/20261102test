package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Created by zhoul on 2018/07/30.
 */
@Entity
@Table(name = "SYS_USER_FACE_DETECTION")
public class UserFaceDetection {
    private String account;
    /**
     * 人脸数据
     */
    private String face;
    /**
     * 人脸复核状态：1通过；0未通过
     */
    private Integer verifyStatus;
    /**
     * 待复核的人脸图像
     */
    private String verifyFace;
    @Id
    @Column(name = "ACCOUNT", unique = true, length = 30)
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Column(name = "FACE")
    public String getFace() {
        return face;
    }
    public void setFace(String face) {
        this.face = face;
    }
    @Column(name = "VERIFY_STATUS")
    public Integer getVerifyStatus() {
        return verifyStatus;
    }
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
    @Column(name = "VERIFY_FACE")
    public String getVerifyFace() {
        return verifyFace;
    }
    public void setVerifyFace(String verifyFace) {
        this.verifyFace = verifyFace;
    }
}
