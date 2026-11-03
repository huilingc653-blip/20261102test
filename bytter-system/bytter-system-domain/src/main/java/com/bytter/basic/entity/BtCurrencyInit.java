package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 系统货币资料
 * @date 2022-3-1 11:14:22
 */
@Entity
@Table(name = "BT_CURRENCY_INIT")
public class BtCurrencyInit extends IdEntity implements Serializable {
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 英文简码
     */
    private String englishCode;
    /**
     * 方法: 取得String
     *
     * @return: String  币别名称
     */
    @Column(name = "CUR_NAME", nullable = false, length = 30)
    public String getCurName() {
        return this.curName;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  币别名称
     */
    public void setCurName(String curName) {
        this.curName = curName;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  英文简码
     */
    @Column(name = "ENGLISH_CODE", nullable = true, length = 30)
    public String getEnglishCode() {
        return this.englishCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  英文简码
     */
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
}
