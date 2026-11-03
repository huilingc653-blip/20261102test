package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccQueryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("查询菜单 -> 银行账户查询数据")
@Data
public class BtBankAccQueryViewPageVO {


    @ApiModelProperty("银行账户信息")
    private BtBankAccDTO accVO;

    @ApiModelProperty("是否是第三方")
    private String isThirdParty;

    @ApiModelProperty("绑定银行账号")
    private String bindingBankAcc;

    @ApiModelProperty("银行账户实体对象")
    private BtBankAccDTO btBankAcc;

    @ApiModelProperty("银行账户VO信息")
    private BtBankAccDTO btBankAccPage;

    @ApiModelProperty("根据账号查询账号对应的流水信息（开户、变更、注销、冻结、解冻）")
    private List<BtBankAccQueryVO> listReq;

    @ApiModelProperty("虚拟账户列表")
    private List<BtBankAccVirtual> listVirtual;
}
