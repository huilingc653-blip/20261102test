package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: SYS_CA_INFO
 * @date 2017-10-26 16:56:53
 */
@Entity
@Table(name = "SYS_CA_INFO")
public class SysCaInfo extends IdEntity implements java.io.Serializable {
    /**
     * 产生编码日期
     */
    private java.util.Date encryDate;
    /**
     * 加密随机数
     */
    private String random;
    /**
     * 编码源
     */
    private String encrySource;
    /**
     * CA编码
     */
    private byte[] caCoding;
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  产生编码日期
     */
    @Column(name = "ENCRY_DATE", nullable = false)
    public java.util.Date getEncryDate() {
        return this.encryDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  产生编码日期
     */
    public void setEncryDate(java.util.Date encryDate) {
        this.encryDate = encryDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  加密随机数
     */
    @Column(name = "RANDOM", nullable = false, length = 20)
    public String getRandom() {
        return this.random;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  加密随机数
     */
    public void setRandom(String random) {
        this.random = random;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  编码源
     */
    @Column(name = "ENCRY_SOURCE", nullable = false, length = 200)
    public String getEncrySource() {
        return this.encrySource;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  编码源
     */
    public void setEncrySource(String encrySource) {
        this.encrySource = encrySource;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  CA编码
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ca_coding", columnDefinition = "BLOB", nullable = true)
    public byte[] getCaCoding() {
        return this.caCoding;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  CA编码
     */
    public void setCaCoding(byte[] caCoding) {
        this.caCoding = caCoding;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  业务类型 nisPay-付款；nisAllocation-资金调拨
     */
}
