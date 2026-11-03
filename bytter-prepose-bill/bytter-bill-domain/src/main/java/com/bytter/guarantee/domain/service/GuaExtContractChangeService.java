package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.dao.GuaExtContractInfoDao;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.entity.GuaContractPledge;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaExtContractChangeService
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同变更服务层
 * @date 2020/5/11 11:11
 */
@Service("guaExtContractChangeService")
@Transactional
public class GuaExtContractChangeService extends BaseService {
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private GuaContractPledgeService guaContractPledgeService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractWayService guaContractWayService;
    @Autowired
    private GuaLogService guaLogService;
    @Autowired
    private GuaExtContractInfoDao guaExtContractInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private GuaExtContractInfoService guaExtContractInfoService;
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
    public DataTablesOutput queryChangeList(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(GuaranteeConstant.GUA_EXTCONTRACT_CHANGE_QUERY,
                user.getId());
        DataTablesOutput data = guaExtContractInfoDao.queryChangeList(input, corpAuth);
        data.setData(guaExtContractInfoService.setGuaWayName(data.getData()));
        return data;
    }
    /**
     * 保存担保变更信息
     * 
     * @param contractVO
     * @param user
     */
    public void tempOrSubmit(GuaContractInfoVO contractVO, ICurrentUser user) {
        guaContractChangeService.checkDate(contractVO);// 校验担保日期
        BigDecimal guaMoney = BigDecimal.ZERO;
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            guaMoney = guaMoney.add(guaContractWayVO.getMoney());
        }
        GuaContractInfo contractInfo = guaContractChangeService.get(contractVO.getId());
        BeanUtils.copyProperties(contractVO, contractInfo, "id", "createDate", "createBy", "conId", "conTypeId",
                "businessType","createName","innerGua","corpName","curName");
        contractInfo.setGuaMoney(guaMoney);
        contractInfo.setStatus(GuaranteeConstant.STATUS_CHECK);
        contractInfo.setUpdateBy(user.getId());
        contractInfo.setUpdateDate(DateUtils.getDate());
        contractInfo.setIsChange(GuaranteeConstant.GUA_YES_CHANGE);
        contractInfo.setDrawMoneyDate(contractVO.getDrawMoneyDate());
        // 生成担保合同占用、释放轨迹
        guaContractChangeService.createContractChangeTrack(contractVO, user, GuaranteeConstant.CON_TYPE_1);
        // 更新抵质押物信息
        if (!StringUtils.isEmpty(contractInfo.getId())) {// 修改担保合同，更新抵质押物信息
            // 先根据担保合同id查询原抵押物信息
            List<GuaContractPledge> pledgeList = guaContractPledgeService.getGuaContractPledge(contractInfo.getId());
            // 还原抵质押物占用金额
            if (pledgeList != null && pledgeList.size() > 0) {
                guaContractChangeService.convertPledgeMoney(pledgeList);
            }
        }
        for (GuaContractWayVO guaContractWayVO : guaDtls) {
            if (StringUtils.isNotEmpty(guaContractWayVO.getPledgeIds())) {
                guaContractChangeService.savePledgeInfo(guaContractWayVO.getPledgeIds());
            }
        }
        guaExtContractInfoService.checkExCorpAmount(contractInfo);
        guaContractInfoService.saveOrUpdate(contractInfo);
        // 保存原担保合同关联担保方式表信息
        List<GuaContractWayVO> listGuaContractWay = guaContractWayService.getGuaContractWay(contractInfo.getId());
        // 更新担保合同关联担保方式表
        guaContractInfoService.saveGuaContractWay(contractVO);
        // 更新担保合同关联抵质押物表
        guaContractInfoService.saveGuaWayPledge(contractVO, listGuaContractWay);
        // 日志记录
        guaLogService.saveGuaLog(contractInfo.getId(), contractInfo.getGuaMoney(),
                GuaranteeConstant.MESSAGE_TYPE_CHANGE, GuaranteeConstant.MSGA_CHANGE, user);
    }
}
