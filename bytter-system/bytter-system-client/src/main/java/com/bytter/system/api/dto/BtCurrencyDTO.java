package com.bytter.system.api.dto;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Base64;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 货币资料维护DTO
 * @date 2017-06-26
 */
public class BtCurrencyDTO implements Serializable {
    @Dict(tableName = "BT_CURRENCY", valueFieldName = "id", showFiledName = "CUR_NAME", inject = "curName")
    @Currency(inject = "curName")
    private String id;
    /**
     * 币别编码
     */
    private String curCode;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 汇率
     */
    private BigDecimal curExrate;
    /**
     * 是否本位币
     */
    private String naturalSign;
    /**
     * 币别符号
     */
    private String curSign;
    private String ceshiclj;
    private Double ceshiclj2;
    private Integer ceshiclj3;
    private Long ceshiclj4;
    /**
     * 英文简码
     */
    private String englishCode;


    /**
     * 系统币种ID
     */
    private String sysCurId;

    /**
     * 系统币种ID
     */
    private String sysCurName;


    /**
     * 人行编码
     */
    private String pbScaleCode;

    /**
     * 系统币种英文简码
     */
    private String sysEnglishCode;


    private BtCurrencyDTO btCurrencyDTO;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getCurExrate() {
        return curExrate;
    }
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
    public String getNaturalSign() {
        return naturalSign;
    }
    public void setNaturalSign(String naturalSign) {
        this.naturalSign = naturalSign;
    }
    public String getCurSign() {
        if (!EmptyUtils.isBlank(this.curSign)) {
            byte[] decode = Base64.getDecoder().decode(this.curSign.getBytes());
            return new String(decode);
        } else {
            return this.curSign;
        }
    }
    //todo 统一base64jia
    public void setCurSign(String curSign) {
        if (!EmptyUtils.isBlank(curSign)) {
            byte[] decode = Base64.getEncoder().encode(curSign.getBytes());
            this.curSign = new String(decode);
        } else {
            this.curSign = curSign;
        }
    }
    public String getCeshiclj() {
        return ceshiclj;
    }
    public void setCeshiclj(String ceshiclj) {
        this.ceshiclj = ceshiclj;
    }
    public Double getCeshiclj2() {
        return ceshiclj2;
    }
    public void setCeshiclj2(Double ceshiclj2) {
        this.ceshiclj2 = ceshiclj2;
    }
    public Integer getCeshiclj3() {
        return ceshiclj3;
    }
    public void setCeshiclj3(Integer ceshiclj3) {
        this.ceshiclj3 = ceshiclj3;
    }
    public Long getCeshiclj4() {
        return ceshiclj4;
    }
    public void setCeshiclj4(Long ceshiclj4) {
        this.ceshiclj4 = ceshiclj4;
    }
    public String getEnglishCode() {
        return englishCode;
    }
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    public BtCurrencyDTO getBtCurrencyDTO() {
        return btCurrencyDTO;
    }
    public void setBtCurrencyDTO(BtCurrencyDTO btCurrencyDTO) {
        this.btCurrencyDTO = btCurrencyDTO;
    }

    public String getSysCurId() {
        return sysCurId;
    }

    public void setSysCurId(String sysCurId) {
        this.sysCurId = sysCurId;
    }

    public String getSysCurName() {
        return sysCurName;
    }

    public void setSysCurName(String sysCurName) {
        this.sysCurName = sysCurName;
    }

    public String getPbScaleCode() {
        return pbScaleCode;
    }

    public void setPbScaleCode(String pbScaleCode) {
        this.pbScaleCode = pbScaleCode;
    }

    public String getSysEnglishCode() {
        return sysEnglishCode;
    }

    public void setSysEnglishCode(String sysEnglishCode) {
        this.sysEnglishCode = sysEnglishCode;
    }

    @Override
    public String toString() {
        return "BtCurrencyDTO{" +
                "id='" + id + '\'' +
                ", curCode='" + curCode + '\'' +
                ", curName='" + curName + '\'' +
                ", englishCode='" + englishCode + '\'' +
                ", curExrate=" + curExrate +
                ", naturalSign='" + naturalSign + '\'' +
                ", curSign='" + curSign + '\'' +
                '}';
    }
}
