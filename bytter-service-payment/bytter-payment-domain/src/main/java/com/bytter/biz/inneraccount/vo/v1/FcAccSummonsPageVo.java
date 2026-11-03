package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("账号复核-内部传票凭证查看页面所需数据")
public class FcAccSummonsPageVo implements Serializable {

    @ApiModelProperty("凭证列表信息")
    private List<VoucherInfoVO>  listInfo;

    @ApiModelProperty("是否开户办理")
    private boolean isTra;

    @ApiModelProperty("是否为拨付")
    private boolean isAllocation;

    @ApiModelProperty("查看标志")
    private String viewSign;

    @ApiModelProperty("结算方式")
    private String voucherType;

    @ApiModelProperty("错误标识")
    private boolean errorSign;
    public List<VoucherInfoVO> getListInfo() {
        return listInfo;
    }

    public void setListInfo(List<VoucherInfoVO> listInfo) {
        this.listInfo = listInfo;
    }

    public boolean getIsTra() {
        return isTra;
    }

    public void setIsTra(boolean tra) {
        isTra = tra;
    }

    public boolean getIsAllocation() {
        return isAllocation;
    }

    public void setIsAllocation(boolean allocation) {
        isAllocation = allocation;
    }

    public String getViewSign() {
        return viewSign;
    }

    public void setViewSign(String viewSign) {
        this.viewSign = viewSign;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public boolean getErrorSign() {
        return errorSign;
    }

    public void setErrorSign(boolean errorSign) {
        this.errorSign = errorSign;
    }
}
