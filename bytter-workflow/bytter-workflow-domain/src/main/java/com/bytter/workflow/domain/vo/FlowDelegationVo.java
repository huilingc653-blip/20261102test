package com.bytter.workflow.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.domain.entity.FlowDelegation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Data
/**
 * Created by lifh on 2018-11-19
 */
public class FlowDelegationVo implements Serializable {
    public FlowDelegationVo(){}
    private String id;
    /**
     * 授权人
     */
    @BtCompositeSearch(fieldName = "principal")
    private String principal;
    private String principalName;
    /**
     * 被授权人
     */
    @BtCompositeSearch(fieldName = "proxy")
    private String proxy;
    private String proxyName;
    /**
     * 参见枚举status
     */
    @BtCompositeSearch(fieldName = "status")
    private Integer status;
    private String statusStr;
    /**
     * 开始时间
     */
    @BtCompositeSearch(fieldName = "start_Time")
    private Date startTime;
    /**
     * yyyy-MM-dd hh:mm:ss,用于接收前端传入的时间参数
     */
    private String startTimeStr;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 序列号
     */
    @BtCompositeSearch(fieldName = "delegation_no")
    private String delegationNo;
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpId;
    /**
     * 流程分类id集合
     */
    private List<String> flowCategoryIdList;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    @AllArgsConstructor
    @Getter
    public enum Status{
        /**
         * 1、暂存 2、待确认 3、打回 4、已确认 5、结束
         */
        STORAGE(1,"暂存"),
        WAIT(2,"待确认"),
        REJEC(3,"打回"),
        SURE(4,"已确认"),
        FINISH(5,"结束"),
        UNK(-1,"未知"),;
        private final Integer code;
        private final String desc;
        public static Status of(Integer status) {
            return Arrays.stream(Status.values()).filter(item -> Objects.equals(status, item.getCode()))
                    .findFirst().orElse(UNK);
        }
    }
    public static FlowDelegationVo from(FlowDelegation delegation){
        FlowDelegationVo flowDelegationVo = new FlowDelegationVo();
        BeanUtils.copyProperties(delegation, flowDelegationVo);
        Status status1 = Status.of(delegation.getStatus());
        flowDelegationVo.setStatusStr(status1.getDesc());
        if (delegation.getStartTime() != null){
            flowDelegationVo.setStartTimeStr(DateUtils.formatDate());
        }
        return flowDelegationVo;
    }
}
