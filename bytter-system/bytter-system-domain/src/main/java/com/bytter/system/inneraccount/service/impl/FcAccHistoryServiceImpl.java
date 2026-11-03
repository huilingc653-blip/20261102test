package com.bytter.system.inneraccount.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcAccHistoryService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.FcAccHistoryPoolVO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.inneraccount.entity.FcAccHistory;
import com.bytter.system.inneraccount.service.FcAccHistoryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 内部账号
 *
 * @author wangf
 */
@Service("fcAccHistoryServiceI")
@DubboService
@Transactional
public class FcAccHistoryServiceImpl implements IFcAccHistoryService {
    @Autowired
    private FcAccHistoryService fcAccHistoryService;
    @Autowired
    private IDictService dictServiceI;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        fcAccHistoryService.delete(id);
    }
    /**
     * 保存
     *
     * @param vo
     */
    public void save(FcAccHistoryVO vo) {
        FcAccHistory entity = BeanUtils.copyNewInstanceProperties(vo, FcAccHistory.class);
        fcAccHistoryService.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param vo
     */
    public void update(FcAccHistoryVO vo) {
        FcAccHistory entity = BeanUtils.copyNewInstanceProperties(vo, FcAccHistory.class);
        fcAccHistoryService.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param vo
     */
    @Override
    public void saveOrUpdate(FcAccHistoryVO vo) {
        FcAccHistory entity = BeanUtils.copyNewInstanceProperties(vo, FcAccHistory.class);
        fcAccHistoryService.saveOrUpdate(entity);
    }
    /**
     * 待提交的可改户申请的数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        DataTablesOutput output = fcAccHistoryService.getDataTablesUnion(dataTablesInput, userVO);
        SetVoDictDate(output);
        return output;
    }
    /**
     * 待提交的可改户申请的数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser userVO) {
        DataTablesOutput output = fcAccHistoryService.getDataTablesUnionForPool(dataTablesInput, userVO);
        setPoolDictDate( output);
        return output;
    }
    private void setPoolDictDate(DataTablesOutput output) {
        List<FcAccHistoryPoolVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO>  corpMap = corpServiceI.getCorpNetMap();
            Map<String, BtCurrencyDTO>  curMap = btCurrencyServiceI.getCurrency();
            for (FcAccHistoryPoolVO vo : list) {
                vo.setAccTypeName(dictServiceI.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictServiceI.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictServiceI.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
    }
    /**
     * 复核数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output = fcAccHistoryService.getCheckDataTable(dataTablesInput, user);
        SetVoDictDate(output);
        return output;
    }
    private void SetVoDictDate(DataTablesOutput output) {
        List<FcAccHistoryVO> list = output.getData();
        if(list.size()>0) {
            Map<String, CorpDTO>  corpMap = corpServiceI.getCorpNetMap();
            Map<String, BtCurrencyDTO>  curMap = btCurrencyServiceI.getCurrency();
            for (FcAccHistoryVO vo : list) {
                vo.setAccTypeName(dictServiceI.get("InnerAccount.AccType", vo.getAccType()));
                vo.setAccStateName(dictServiceI.get("inneraccount.fcStatus", vo.getAccState()));
                vo.setBalanceWayName(dictServiceI.get("Admin.BalanceWay", vo.getBalanceWay()));

                CorpDTO corpDTO= corpMap.get(vo.getCorpId());
                vo.setCorpName(corpDTO.getName());
                vo.setNetName(corpDTO.getNetName());
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
    }
    /**
     * 复核数据列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getCheckDataTableForPool(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output =  fcAccHistoryService.getCheckDataTableForPool(dataTablesInput, user);
        setPoolDictDate( output);
        return output;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOById(String id) {
        FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOById(id);
        return fcAcc;
    }
    /**
     * 根据id查对象
     *
     * @param id
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByIdForPool(String id) {
        FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOByIdForPool(id);
        return fcAcc;
    }
    /**
     * 根据账号查询历史账户对象
     *
     * @param blnctrAcc
     * @return
     */
    public FcAccHistoryVO findFcAccHistoryVOByBlnctrAcc(String blnctrAcc) {
        FcAccHistoryVO fcAcc = fcAccHistoryService.findFcAccHistoryVOByBlnctrAcc(blnctrAcc);
        return fcAcc;
    }
    /**
     * 根据账号信息查找计息账号列表
     *
     * @param vo
     * @return
     */
    public List<FcAccHistoryVO> getAccListByExpPay(FcAccHistoryVO vo) {
        List<FcAccHistoryVO> accList = fcAccHistoryService.getAccListByExpPay(vo);
        return accList;
    }
    /**
     * 根据账号信息查找计息账号
     *
     * @param vo
     * @return
     */
    public FcAccHistoryVO getFcAccHistoryVO(FcAccHistoryVO vo) {
        FcAccHistoryVO accList = fcAccHistoryService.getFcAccHistoryVO(vo);
        return accList;
    }
    @Override
    public FcAccHistoryVO getFcAccHistoryVOById(String id) {
        FcAccHistory history = fcAccHistoryService.get(id);
        FcAccHistoryVO vo = new FcAccHistoryVO();
        BeanUtils.copyProperties(history, vo);
        return vo;
    }
    @Override
    public FcAccHistoryVO getFcAccHistoryById(String id) {
        FcAccHistoryVO entity = new FcAccHistoryVO();
        FcAccHistory history = fcAccHistoryService.get(id);
        BeanUtils.copyProperties(history, entity);
        return entity;
    }
    @Override
    public FcAccHistoryVO mergeObj(FcAccHistoryVO object) {
        FcAccHistoryVO vo = fcAccHistoryService.getFcAccHistoryVO(object);
        return vo;
    }
}
