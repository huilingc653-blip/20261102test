package com.bytter.workflow.domain.mapper;
import com.bytter.workflow.domain.vo.TodoNumberVo;
import com.bytter.workflow.dto.BusinessTaskDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
public interface FlowTaskMapper {
    Long countTotal(@Param("assignee") String assignee, @Param("category") String category);
    List<TodoNumberVo> countTodoNumberList(@Param("assignee") String assignee, @Param("category") String category);
    List<BusinessTaskDTO> listBusinessTask(@Param("assignee") String assignee, @Param("category") String category);

    List<BusinessTaskDTO> listTrackTask(@Param("userId") String userId, @Param("category") String category);
    Long countTrackTotal(@Param("userId") String userId,@Param("category") String category);
    List<TodoNumberVo> countTrackNumberGpCateKey(@Param("userId") String userId,@Param("category") String category);
    List<TodoNumberVo> countFinishGpCateKey(@Param("assignee") String assignee, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    List<String> listFinishedBusinessKeys(@Param("assignee") String assignee, @Param("category") String category, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
