package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel("内部传票凭证批量打印数据")
public class FcAccSummonsPrintVo implements Serializable {

    @ApiModelProperty("错误标识")
    private boolean errorSign;

    @ApiModelProperty("凭证列表信息")
    private List<Map<String, List<VoucherInfoVO>>> list;

    public boolean getErrorSign() {
        return errorSign;
    }

    public void setErrorSign(boolean errorSign) {
        this.errorSign = errorSign;
    }

    public List<Map<String, List<VoucherInfoVO>>> getList() {
        return list;
    }

    public void setList(List<Map<String, List<VoucherInfoVO>>> list) {
        this.list = list;
    }
}
