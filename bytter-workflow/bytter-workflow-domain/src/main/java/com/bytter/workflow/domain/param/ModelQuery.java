package com.bytter.workflow.domain.param;
import com.bytter.core.common.page.PageParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ModelQuery extends PageParam {
    private String categoryId;
    private String corpId;
    private Integer status;
    //null 为空，true = 创建时间，false = 更新时间
    private Boolean isCreateTime;
    //true = 升序，false = 降序
    private boolean isAsc;
}
