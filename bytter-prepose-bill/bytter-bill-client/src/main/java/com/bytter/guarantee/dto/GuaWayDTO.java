package com.bytter.guarantee.dto;
import java.io.Serializable;
    /**
 * Title: GuaWayDTO
 *
 * @author luxy
 * @since 2019/7/30 14:22
 */
public class GuaWayDTO implements Serializable {
    private String id;
    /**
     * 类型编码
     */
    private String code;
    /**
     * 担保方式名称
     */
    private String name;
    /**
     * 是否抵质押
     **/
    private String isPledge;
    /**
     * 抵质押性质
     **/
    private String pledgeProperty;
    /**
     * 备注
     **/
    private String rmk;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIsPledge() {
        return isPledge;
    }
    public void setIsPledge(String isPledge) {
        this.isPledge = isPledge;
    }
    public String getPledgeProperty() {
        return pledgeProperty;
    }
    public void setPledgeProperty(String pledgeProperty) {
        this.pledgeProperty = pledgeProperty;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
