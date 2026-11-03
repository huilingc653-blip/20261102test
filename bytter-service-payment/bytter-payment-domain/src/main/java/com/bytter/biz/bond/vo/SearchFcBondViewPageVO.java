package com.bytter.biz.bond.vo;

import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBackVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("保证金账户查询VO类")
public class SearchFcBondViewPageVO {

    @ApiModelProperty("保证金账户主题信息")
    private FcBondBackVO fcBondBackVO;

    @ApiModelProperty("保证金账户项目信息")
    private List<FcBondApplyVO> listFcBondApply;

    @ApiModelProperty("保证金转回历史记录")
    private List<FcBondBackVO> listFcBondBack;

}
