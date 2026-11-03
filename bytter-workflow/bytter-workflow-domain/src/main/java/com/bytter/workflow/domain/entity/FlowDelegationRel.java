package com.bytter.workflow.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@TableName("act_extend_delegation_rel")
@EqualsAndHashCode(callSuper = false)
@Data
@Accessors(chain = true)
public class FlowDelegationRel extends BaseEntity {
    /**
     * 授权配置id ： delegation表的id
     */
    private String delegationId;
    /**
     * 流程分类id : flow_category表id
     */
    private String cateId;
    /**
     * 流程分类key
     */
    private String cateKey;
    public FlowDelegationRel() {
    }
    public FlowDelegationRel(String delegationId, String cateId) {
        this.delegationId = delegationId;
        this.cateId = cateId;
    }
}
