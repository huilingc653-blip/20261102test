package com.bytter.workflow.domain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytter.workflow.domain.entity.FlowDelegation;
import org.apache.ibatis.annotations.Param;
public interface FlowDelegationMapper extends BaseMapper<FlowDelegation> {
    FlowDelegation getOneByPrincipalAndCategory(@Param("principal") String principal, @Param("categoryId") String categoryId);
}
