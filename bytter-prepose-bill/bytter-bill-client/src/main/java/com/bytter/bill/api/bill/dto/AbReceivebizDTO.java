package com.bytter.bill.api.bill.dto;
import java.io.Serializable;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 电票签收-接口参数对象
 * @date 2021-3-2
 */
public class AbReceivebizDTO implements Serializable {

    /**
     *  票据签收表ID
     */
    private String billRecId;
    /**
     * 票据号码
     */
    private String origNo;
    /**
     * 签收人账号
     */
    private String bankAcc;
    /**
     * 签收类型（002承兑签收、003收票签收、010背书签收、012回购式贴现赎回签收、
     * 018质押签收、019质押解除签收、020提示付款签收、021逾期提示付款签收）
     */
    private String signmsgType;
    /**
     * 签收状态（SU00-同意  SU01-拒绝）
     */
    private String signUpFlag;
    /**
     * 拒付理由代码（DC00与自己有直接债权债务关系的持票人未履行约定义务；
     * DC01持票人以欺诈、偷盗或者胁迫等手段取得票据；
     * DC02持票人明知有欺诈、偷盗或者胁迫等情形，出于恶意取得票据；
     * DC03持票人明知债务人与出票人或者持票人的前手之间存在抗辩事由而取得票据；
     * DC04持票人因重大过失取得不符合《票据法》规定的票据；
     * DC05超过提示付款期；
     * DC06被法院冻结或收到法院止付通知书；
     * DC07票据未到期；
     * DC08商业承兑汇票承兑人账户余额不足。
     * DC09其他（必须注明））
     * <p>
     * 必填的业务类型：签收类型为020提示付款签收,签收状态为SU01-拒绝签收时必填
     */
    private String dshnrcd;
    /**
     * 拒付理由备注
     */
    private String dshnrrsn;
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getSignmsgType() {
        return signmsgType;
    }
    public void setSignmsgType(String signmsgType) {
        this.signmsgType = signmsgType;
    }
    public String getSignUpFlag() {
        return signUpFlag;
    }
    public void setSignUpFlag(String signUpFlag) {
        this.signUpFlag = signUpFlag;
    }
    public String getDshnrcd() {
        return dshnrcd;
    }
    public void setDshnrcd(String dshnrcd) {
        this.dshnrcd = dshnrcd;
    }
    public String getDshnrrsn() {
        return dshnrrsn;
    }
    public void setDshnrrsn(String dshnrrsn) {
        this.dshnrrsn = dshnrrsn;
    }

    public String getBillRecId() {
        return billRecId;
    }

    public void setBillRecId(String billRecId) {
        this.billRecId = billRecId;
    }

    @Override
    public String toString() {
        return "AbReceivebizDTO{" +
                "billRecId='" + billRecId + '\'' +
                ", origNo='" + origNo + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", signmsgType='" + signmsgType + '\'' +
                ", signUpFlag='" + signUpFlag + '\'' +
                ", dshnrcd='" + dshnrcd + '\'' +
                ", dshnrrsn='" + dshnrrsn + '\'' +
                '}';
    }
}
