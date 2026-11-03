package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.vo.BisAccGatherPolicyTreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhengzf
 * @Description: 银行账号关系策略维护表列表查询查询条件
 * @date 2026-01-04 15:02:30
 */
@ApiModel("银行账号关系策略维护表列表查询查询条件")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BisGatherPolicyListVO implements Serializable {

    @ApiModelProperty("获取树形表格-增加父账号筛选")
    private List<BisAccGatherPolicyTreeVO> treeDate;
    @ApiModelProperty("筛选条件-调拨策略")
    private BisGatherPolicy bisGatherPolicyPage;
    @ApiModelProperty("筛选条件-父账户名")
    private String parentAccName;
}
