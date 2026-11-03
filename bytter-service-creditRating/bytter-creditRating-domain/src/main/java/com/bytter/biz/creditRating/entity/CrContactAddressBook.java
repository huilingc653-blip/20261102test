package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 联系人通讯录
 */
@Entity
@Table(name = "CONTACT_ADDRESS_BOOK", schema ="")
public class CrContactAddressBook  extends IdEntity implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 职务
     */
    private String job;
    /**
     * 电话号码
     */
    private String  telephoneNum;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 微信号
     */
    private String  weChatNum;
    /**
     * qq号
     */
    private String qqNum;
    /**
     * 外键（结构原始数据）id
     */
    private String institutionalRawDataId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间:yyyy-mm-dd hh24:mm:ss
     */
    private Date createDate;

    @Column(name = "NAME", length =64 )
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SEX", length =100 )
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "JOB", length =128 )
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Column(name = "TELEPHONE_NUM", length =128 )
    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    @Column(name = "EMAIL", length =128 )
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "WECHAT_NUM", length =128 )
    public String getWeChatNum() {
        return weChatNum;
    }

    public void setWeChatNum(String weChatNum) {
        this.weChatNum = weChatNum;
    }

    @Column(name = "QQ_NUM", length =128 )
    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    @Column(name = "INSTITUTIONAL_RAW_DATA_ID", length =32 )
    public String getInstitutionalRawDataId() {
        return institutionalRawDataId;
    }

    public void setInstitutionalRawDataId(String institutionalRawDataId) {
        this.institutionalRawDataId = institutionalRawDataId;
    }

    @Column(name = "RMK", length =300 )
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "CREATOR", length =100 )
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "CREATE_DATE", length =100 )
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CrContactAddressBook{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", job='" + job + '\'' +
                ", telephoneNum='" + telephoneNum + '\'' +
                ", email='" + email + '\'' +
                ", weChatNum='" + weChatNum + '\'' +
                ", qqNum='" + qqNum + '\'' +
                ", institutionalRawDataId='" + institutionalRawDataId + '\'' +
                ", rmk='" + rmk + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
