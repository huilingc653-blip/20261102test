package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.vo.FcBondLogVO;

import java.math.BigDecimal;
import java.util.List;

public interface IFcBondLogService {

    /**
     * 新增保证金日志
     *
     * @param bondId      保证金id
     * @param money       金额
     * @param messageType 日志类型
     * @param message     日志内容
     * @param user        用户
     * @return boolean
     */
    boolean saveFcBondMessge(String bondId, BigDecimal money, Integer messageType, String message, ICurrentUser user);

    /**
     * 加载日志列表信息
     *
     * @param bondId
     * @param input
     * @return
     */
    //TODO SYS服务提供
    @SuppressWarnings("rawtypes")
    DataTablesOutput listFcBondMsg(String bondId, DataTablesInput input);

    /**
     * 根据保证金id,从日志表FC_BOND_LOG中得到相应的操作信息
     *
     * @param bondId
     * @return
     */
    List<FcBondLogVO> listOpMessage(String bondId);

    /**
     * 删除对象
     *
     * @param entity
     */
    void deleteEntity(FcBondLogVO entity);

    /**
     * 根据id删除对象
     *
     * @param id
     */
    void delete(String id);

    /**
     * 更新新对象
     *
     * @param entity
     */
    void updateEntity(FcBondLogVO entity);

    /**
     * 新增对象
     *
     * @param entity
     */
    void save(FcBondLogVO entity);

    /**
     * 保存or新增
     *
     * @param entity
     */
    void saveOrUpdate(FcBondLogVO entity);

    /**
     * 新增保证金日志
     *
     * @param entity
     * @return boolean
     */
    boolean saveFcBondLog(FcBondLogVO entity);
}
