package com.bytter.workflow.domain.param;
import com.bytter.core.common.page.PageParam;
import com.bytter.workflow.domain.vo.FlowDelegationVo;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
@Data
@Accessors(chain = true)
public class FlowDelegationQuery extends PageParam {
    private Date startTime;
    private Date endTime;
    /**
     * 转授人
     */
    private String principal;
    private String principalCorpId;
    /**
     * 被转授人
     */
    private String proxy;
    private String proxyCorpId;
    /**
     * 转授状态
     */
    public FlowDelegationVo.Status status;
}
