package com.bytter.system.api.dto;
import lombok.Data;
import java.io.Serializable;
@Data
public class BankTypeByAccDTO implements Serializable {
    //账号
    private String  bankAcc;
    //账户名称
    private String  accName;
    //开户行code
    private String  bankCode;
    //开户行名称
    private String  bankName;
    //银行类型id
    private String  bankTypeId;
    //银行类型名称
    private String  bankTypeName;
}
