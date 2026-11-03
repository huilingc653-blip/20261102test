package com.bytter.biz.bond.vo;

import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "保证金追加相关页面数据")
public class BondAdditionPageVO {

    @ApiModelProperty(value = "保证金信息")
    private FcBondDetailVO fcBond;

    @ApiModelProperty(value = "保证金追加信息")
    private FcBondAddDTO fcbondAddDTO;

    @ApiModelProperty(value = "保证金追加金额")
    private BigDecimal addMoney;

    @ApiModelProperty(value = "保证金追加历史信息")
    List<FcBondAddDTO> addHisList;
}
