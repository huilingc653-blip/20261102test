package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.system.api.vo.FcAccLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel("账号数据修改日志信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccLogPageVO implements Serializable {

    @ApiModelProperty("账号修改日志列表")
    List<FcAccLogVO> listLogPage;
}
