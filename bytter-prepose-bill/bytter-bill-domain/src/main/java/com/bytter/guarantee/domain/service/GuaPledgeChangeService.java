package com.bytter.guarantee.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaPledgeChangeDao;
import com.bytter.guarantee.domain.dao.GuaPledgeInfoDao;
import com.bytter.guarantee.domain.entity.GuaContractPledge;
import com.bytter.guarantee.domain.entity.GuaPledgeInfoHis;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoHisVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaPledgeChangeService
 * @Description:抵质押物变更业务逻辑层
 * @date 2018年12月12日 下午1:46:12
 */
@Service("GuaPledgeChangeService")
@Transactional
public class GuaPledgeChangeService extends BaseService<GuaPledgeInfoHis, String> implements IWorkFlowListener {
    @Autowired
    private GuaPledgeChangeDao pledgeChangeDao;
    @Autowired
    private GuaPledgeInfoDao guaPledgeInfoDao;
    @Autowired
    private GuaPledgeInfoService pledgeInfoService;
    @Autowired
    private GuaLogService guaLogService;
    @DubboReference
    private ICorpService corpService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 抵质押物变更列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryDataList(@RequestBody DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_PLEDGE_CHANGE_QUERY, user.getId());
        return guaPledgeInfoDao.querypledgeInfoPass(input, corpAuth);
    }
    /**
     * 根据id查询抵质押物信息
     */
    public GuaPledgeInfoVO queryPledgeInfoById(String id) {
        // 获取是否允许多次抵押
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.PLEDGE, GuaranteeConstant.MANY_TIMES);
        return guaPledgeInfoDao.getGuaPledgeInfoVOById(id, configDTO);
    }
    /**
     * 根据id查询历史变更抵质押物信息
     */
    public GuaPledgeInfoVO queryHisPledgeInfoById(String id) {
        // 获取是否允许多次抵押
        return guaPledgeInfoDao.getHisGuaPledgeInfoVOById(id);
    }
    /**
     * 查询抵质押物变更记录
     */
    public DataTablesOutput queryChangeHisList(DataTablesInput input, String pledgeId) {
        DataTablesOutput dataTablesOutput=pledgeChangeDao.queryChangeHisData(input, pledgeId);
        List data=dataTablesOutput.getData();
        ArrayList<GuaPledgeInfoHisVO> guaPledgeInfoHisVOs=new ArrayList<>();
        for(Object datum : data){
            GuaPledgeInfoHisVO vo=(GuaPledgeInfoHisVO)datum;
            String corpName=corpService.getCorpName(vo.getCorpId());
            vo.setCorpName(corpName);
            // savePledgeChangeHis 保存的时候就是存的Username
            /*if(StringUtils.isNotEmpty(vo.getCreateBy())){
                UserDTO create = userService.getByUserId(vo.getCreateBy());
                vo.setCreateBy(create.getUsername());
            }
            if(StringUtils.isNotEmpty(vo.getUpdateBy())){
                UserDTO update = userService.getByUserId(vo.getUpdateBy());
                vo.setUpdateBy(update.getUsername());
            }*/
            guaPledgeInfoHisVOs.add(vo);
        }
        dataTablesOutput.setData(guaPledgeInfoHisVOs);
        return dataTablesOutput;
    }
    /**
     * 保存抵质押物变更信息
     */
    public void savePledgeChangeHis(GuaPledgeInfoHisVO pledgeInfoHisVO, ICurrentUser user) {
        GuaPledgeInfoHis guaPledgeHis = new GuaPledgeInfoHis();
        GuaPledgeInfoVO pledgeInfoVO = pledgeInfoService.getGuaPledgeInfoVOById(pledgeInfoHisVO.getPledgeId());
        BeanUtils.copyNotNullProperties(pledgeInfoHisVO, guaPledgeHis);
        guaPledgeHis.setPledgeId(pledgeInfoHisVO.getPledgeId());
        //UserDTO create = userService.getByLoginName(pledgeInfoVO.getCreateBy());
        if (ObjectUtil.isNotEmpty(pledgeInfoVO.getCreateBy())){
            guaPledgeHis.setCreateBy(pledgeInfoVO.getCreateBy());
        }else{
            guaPledgeHis.setCreateBy(user.getUsername());
        }
        //if (ObjectUtil.isNotEmpty(pledgeInfoVO.getCreateDate())){
        //    guaPledgeHis.setCreateDate(pledgeInfoVO.getCreateDate());
        //}else {
        //    guaPledgeHis.setCreateDate(DateUtils.getDate());
        //}
        guaPledgeHis.setCreateDate(DateUtils.getDate());
        guaPledgeHis.setUpdateBy(user.getUsername());
        guaPledgeHis.setUpdateDate(DateUtils.getDate());
        // 页面提交的主表id会被copyNotNullProperties复制到历史记录主键上，同一笔抵质押物第二次变更时
        // 会复用第一次的历史记录主键导致 Duplicate entry。置空由Hibernate重新生成，原业务数据靠 pledgeId 关联。
        guaPledgeHis.setId(null);
        super.saveEntity(guaPledgeHis);
        // 更新到登记表
        BeanUtils.copyNotNullProperties(pledgeInfoHisVO, pledgeInfoVO);
        if(StringUtils.isNotEmpty(pledgeInfoHisVO.getRegCorp())){
            CorpDTO corpDTO = corpService.get(pledgeInfoHisVO.getRegCorp());
            pledgeInfoVO.setRegCorpName(corpDTO.getName());
        }
        pledgeInfoVO.setId(pledgeInfoHisVO.getPledgeId());
        pledgeInfoVO.setRegCorp(pledgeInfoHisVO.getRegCorp());
        pledgeInfoService.updateGuaPledgeInfo(pledgeInfoVO, user);
        // 日志记录
        guaLogService.saveGuaLog(pledgeInfoVO.getId(), pledgeInfoVO.getPledgeMoney(),
                GuaranteeConstant.MESSAGE_TYPE_CHANGE, GuaranteeConstant.MSGA_CHANGE, user);
    }
    /**
     * 保存抵质押物变更信息
     */
    public void savePledgeChangeHist(GuaPledgeInfoHisVO pledgeInfoHisVO, ICurrentUser user) {
        GuaPledgeInfoHis guaPledgeHis = new GuaPledgeInfoHis();
        BeanUtils.copyNotNullProperties(pledgeInfoHisVO, guaPledgeHis);
        guaPledgeHis.setCreateBy(user.getId());
        guaPledgeHis.setCreateDate(DateUtils.getDate());
        GuaPledgeInfoVO pledgeInfoVO = new GuaPledgeInfoVO();
        super.saveEntity(guaPledgeHis);
        // 日志记录
        guaLogService.saveGuaLog(pledgeInfoVO.getId(), pledgeInfoVO.getPledgeMoney(),
                GuaranteeConstant.MESSAGE_TYPE_CHANGE, GuaranteeConstant.MSGA_CHANGE, user);
    }
    /**
     * 通过抵质押物ID判断抵质押物是否已被担保合同占用
     *
     * @param id
     * @return
     */
    public boolean checkData(String id) {
        List<GuaContractPledge> list = guaContractPledgeService.getGuaContractPledgeByPledgeId(id);
        GuaPledgeInfoVO pledgeInfoVO = pledgeInfoService.getGuaPledgeInfoVOById(id);
        if (list != null && list.size() > 0 && GuaranteeConstant.GUA_TYPE_DY.equals(pledgeInfoVO.getPledgeAttr())) {
            return true; // 抵押已被占用
        } else {
            return false; // 抵押未被占用
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public BaseDao<GuaPledgeInfoHis, String> getEntityDao() {
        // TODO Auto-generated method stub
        return pledgeChangeDao;
    }
}
