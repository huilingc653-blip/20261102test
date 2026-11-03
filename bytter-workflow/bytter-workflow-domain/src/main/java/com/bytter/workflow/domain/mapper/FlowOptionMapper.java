package com.bytter.workflow.domain.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytter.workflow.domain.entity.FlowOption;
import org.apache.ibatis.annotations.Param;
public interface FlowOptionMapper extends BaseMapper<FlowOption> {
    FlowOption findOneByCateKeyAndType(@Param("cateKey") String cateKey, @Param("type") String type);
}
