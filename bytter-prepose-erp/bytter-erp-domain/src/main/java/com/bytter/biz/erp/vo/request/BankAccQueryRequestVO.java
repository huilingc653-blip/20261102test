package com.bytter.biz.erp.vo.request;

import lombok.Data;
import java.io.Serializable;

/**
 * 银行账户查询请求参数。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
public class BankAccQueryRequestVO extends BaseRequest implements Serializable {

    /** 银行账号。 */
    private String bankAcc;

    /** 账户名称。 */
    private String accName;

    /** 单位代码。 */
    private String corpCode;

    /** 单位名称。 */
    private String corpName;

    /** 更新日期，格式：yyyy-MM-dd。 */
    private String updateDate;
}
