package com.bytter.olap.dao;

import com.bytter.olap.pojo.DashboardBoard;
import com.bytter.olap.pojo.DashboardBoardParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/23.
 */
@Repository
public interface BoardDao {
    
    int save(DashboardBoard board);

    List<DashboardBoard> getAllBoardList();

    List<DashboardBoard> getBoardListByPermission(@Param("currentUserId") String currentUserId);

    long countExistBoardName(Map<String, Object> map);

    int update(DashboardBoard board);

    int delete(String id);

    DashboardBoard getBoard(String id);

    DashboardBoardParam getBoardParam(String boardId);

    int saveBoardParam(DashboardBoardParam boardParam);

    /**
     * 删除看板参数
     *
     * @param boardId 看板ID
     * @return 删除数量
     */
    int deleteBoardParam(String boardId);
}
