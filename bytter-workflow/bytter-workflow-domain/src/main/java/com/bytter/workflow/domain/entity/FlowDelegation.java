package com.bytter.workflow.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
/**
 * Created by lifh on 2018-11-19
 * 流程转授配置表
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("act_extend_delegation")
public class FlowDelegation extends BaseEntity {
    /**
     * 授权人
     */
    private String principal;
    private String principalName;
    /**
     * 授权人单位
     */
    private String principalCorpId;
    /**
     * 被授权人
     */
    private String proxy;
    private String proxyName;
    /**
     * 被授权人单位
     */
    private String proxyCorpId;
    /**
     * 1、暂存 2、待确认 3、打回 4、已确认 5、结束
     */
    private Integer status;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    private String delegationNo;
}
