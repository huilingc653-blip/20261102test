package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.bond.entity.FcBondLog;
import com.bytter.guarantee.bond.service.FcBondLogService;
import com.bytter.guarantee.vo.FcBondLogVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service("fcBondLogServiceImpl")
@DubboService
public class FcBondLogServiceImpl implements IFcBondLogService {

    @Autowired
    private FcBondLogService fcBondLogService;
    /**
     * 新增保证金日志
     * @param bondId 保证金id
     * @param money  金额
     * @param messageType 日志类型
     * @param message  日志内容
     * @param user 用户
     * @return boolean
     */
    public boolean saveFcBondMessge(String bondId, BigDecimal money, Integer messageType, String message, ICurrentUser user){
        return fcBondLogService.saveFcBondMessge( bondId,  money,  messageType,  message,  user);
    }
    /**
     * 加载日志列表信息
     * @param bondId
     * @param input
     * @return
     */
    //TODO SYS服务提供
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listFcBondMsg(String bondId, DataTablesInput input){

        return fcBondLogService.listFcBondMsg( bondId,  input);
    }

    /**
     * 根据保证金id,从日志表FC_BOND_LOG中得到相应的操作信息
     * @param bondId
     * @return
     */
    public List<FcBondLogVO> listOpMessage(String bondId){
        return fcBondLogService.listOpMessage(bondId);
    }

    /**
     * 删除对象
     * @param entity
     */

    public void deleteEntity(FcBondLogVO entity){
        FcBondLog en = new FcBondLog();
        BeanUtils.copyProperties(entity, en);
        fcBondLogService.deleteEntity(en);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    public void delete(String id){
        fcBondLogService.delete(id);
    }
    /**
     * 更新新对象
     * @param entity
     */
    public void updateEntity(FcBondLogVO entity){
        FcBondLog en = new FcBondLog();
        BeanUtils.copyProperties(entity, en);
        fcBondLogService.updateEntity(en);
    }
    /**
     * 新增对象
     * @param entity
     */
    public void save(FcBondLogVO entity){
        FcBondLog en = new FcBondLog();
        BeanUtils.copyProperties(entity, en);
        fcBondLogService.save(en);
    }
    /**
     * 保存or新增
     * @param entity
     */
    public void saveOrUpdate(FcBondLogVO entity){
        FcBondLog en = new FcBondLog();
        BeanUtils.copyProperties(entity, en);
        fcBondLogService.saveOrUpdate(en);
    }
    /**
     * 新增保证金日志
     * @param entity
     * @return boolean
     */
    public boolean saveFcBondLog(FcBondLogVO entity){
        FcBondLog en = new FcBondLog();
        BeanUtils.copyProperties(entity, en);
        return fcBondLogService.saveFcBondLog(en);
    }
}
