package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("银行账户开户办理查询数据")
@Data
public class BtBankAccHandlePageVO implements Serializable {

    @ApiModelProperty("单位名称")
    private String corpName;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("币别名称")
    private String curName;

    @ApiModelProperty("业务类型")
    private String busType;

    @ApiModelProperty("银行账户相关信息")
    private BtBankAccDTO btBankAccPage;

    @ApiModelProperty("标记电票标记")
    private String electricBill;

    @ApiModelProperty("境内境外标识")
    private String isDomesticBankType;

    @ApiModelProperty("是否为第三方平台")
    private String isThirdParty;

    @ApiModelProperty("查询数据来源app为账号申请表，acc为银行账号表")
    private String isFrom;

    @ApiModelProperty("今日日期 yyyy-MM-dd 格式")
    private String todate;

    @ApiModelProperty("是否允许变更为保证金户")
    private String isAllowedChangeDeposit;

    @ApiModelProperty("虚拟户列表")
    private List<BtBankAccVirtual> listVirtual;

    @ApiModelProperty("虚拟户列表个数")
    private Integer dataListSize;

    @ApiModelProperty("银行类型是否8500")
    private String bankTypeIs8500;

    @ApiModelProperty("绑定银行账户号")
    private String bindingBankAcc;

    @ApiModelProperty("是否展示网银U-Key")
    private Boolean isShowUKeyOther;

    @ApiModelProperty("审核ID 审批相关使用")
    private String taskId;
}
