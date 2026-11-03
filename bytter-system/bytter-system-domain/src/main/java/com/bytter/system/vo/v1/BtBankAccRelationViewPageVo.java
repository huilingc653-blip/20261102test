package com.bytter.system.vo.v1;


import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.entity.BtBankAcc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("账户父子关系 查询数据")
@Data
public class BtBankAccRelationViewPageVo implements Serializable {

    @ApiModelProperty("银行账户信息")
    private BtBankAccVO btBankAccPage;

    @ApiModelProperty("prentBtBankAccPage")
    private BtBankAccVO prentBtBankAccPage;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @ApiModelProperty("资金池标识")
    private String poolSign;

}
