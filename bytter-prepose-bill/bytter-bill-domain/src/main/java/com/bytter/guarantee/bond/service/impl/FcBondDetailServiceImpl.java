package com.bytter.guarantee.bond.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.bond.dao.FcBondDao;
import com.bytter.guarantee.bond.entity.FcBond;
import com.bytter.guarantee.bond.service.FcBondDetailService;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.vo.FcBondVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("fcBondDetailServiceImpl")
@DubboService
public class FcBondDetailServiceImpl implements IFcBondDetailService {
    @Autowired
    private FcBondDetailService fcBondDetailService;
    @Autowired
    private FcBondDao fcBondDao;

    public DataTablesOutput<FcBondDetailVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {

        return fcBondDetailService.getDataTablesUnion(dataTablesInput, user);
    }

    /**
     * 新增保证金
     *
     * @param fcBond
     * @param user
     */
    public void saveFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        fcBondDetailService.saveFcBondDetail(fcBond, user);
    }

    /**
     * fusb 2018-09-13
     * 修改存出保证金
     *
     * @param fcBond
     * @param user
     */
    public void updateFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        fcBondDetailService.updateFcBondDetail(fcBond, user);
    }

    /**
     * 获取审批列表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput<WorkFlowPersistence> dataTableApprove(DataTablesInput dataTablesInput) {
        return fcBondDetailService.dataTableApprove(dataTablesInput);
    }

    /**
     * fusb,2018-9-12添加
     * 根据保证金id,得到保证金信息
     *
     * @param id
     * @return
     */
    public FcBondDetailVO getFcBondById(String id) {
        return fcBondDetailService.getFcBondById(id);
    }

    /**
     * 查询保证金账户是否已存出
     */
    public boolean queryBondAcc(String bankAccId) {
        return fcBondDetailService.queryBondAcc(bankAccId);
    }

    /**
     * 改变保证金的状态为删除
     */
    public void deleteBond(String id, ICurrentUser user) {
        fcBondDetailService.deleteBond(id, user);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcBondVO entity) {
        FcBond en = new FcBond();
        BeanUtils.copyProperties(entity, en);
        fcBondDetailService.saveOrUpdate(en);
    }
    /**
     * 保证金额度进行汇率折算
     *
     * @param balList
     * @param preDate
     * @param corpId  当前用户所属单位ID
     * @return
     */
    public BigDecimal dealBondBalance(List<FcBondDetailVO> balList, Date preDate, String corpId) {
        return fcBondDetailService.dealBondBalance(balList, preDate, corpId);
    }

    /**
     * <br>
     * <b>功能：</b> 删除头寸调拨单打回保证金 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/8/18 0018 <br>
     */
    public void tempBond(String id, ICurrentUser user) {
        fcBondDetailService.tempBond(id, user);
    }

    @Override
    public FcBondDetailVO addFcBondDetail(FcBondDetailVO fcBond, ICurrentUser user) {
        return fcBondDetailService.addFcBondDetail(fcBond, user);
    }

    @Override
    public void updateExistFcBondDetail(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user) {
        fcBondDetailService.updateExistFcBondDetail(fcBond,bailMoney,user);
    }

    @Override
    public FcBondDetailVO getFcBondByBankAcc(String bankAcc) {
        return fcBondDetailService.getFcBondByBondBankAcc(bankAcc);
    }

    @Override
    public void addFcBondAddData(FcBondDetailVO fcBond,BigDecimal bailMoney, ICurrentUser user) {
        fcBondDetailService.addFcBondAddData(fcBond,bailMoney,user);
    }

    /**
     * 保证金账户查询批量编辑利率
     *
     * @param bondIds      多条id记录
     * @param interestRate 利率
     * @param user 用户对象
     */
    @Override
    public void batchEditInterestRate(String bondIds, BigDecimal interestRate, ICurrentUser user) {
        if (StringUtils.isNotEmpty(bondIds)) {
            List<String> bondIdList = Arrays.asList(bondIds.split(CommonConstant.COMMA_SPLIT));
            List<FcBond> entityList = Lists.newArrayList();
            bondIdList.forEach(bondId -> {
                // 根据保证金主键id获取对象实体信息
                FcBond entity = fcBondDao.get(bondId, true);
                // 修改后的利率
                entity.setInterestRate(interestRate);
                entityList.add(entity);
            });
            if (!CollectionUtils.isEmpty(entityList)) {
                // 批量更新利率
                fcBondDao.batchSaveOrUpdate(entityList);
            }
        }
    }
}
