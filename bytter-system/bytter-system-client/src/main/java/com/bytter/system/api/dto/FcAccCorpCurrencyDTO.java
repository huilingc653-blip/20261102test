package com.bytter.system.api.dto;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author YonJar
 * @date 2023/6/1.
 * @description
 */
@Data
public class FcAccCorpCurrencyDTO implements Serializable {
    private static final long serialVersionUID = -7350204991340542420L;
    private String blnctrAcc;
    private String curId;
    private String curCode;
    private String overdraftSign;
    private BigDecimal overdraftMoney;
    private Integer balanceWay;
    private String netId;
    private String netCode;
    private String accType;
    private String chargeAcc;
    private String subjectId;
}
