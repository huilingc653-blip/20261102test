package com.bytter.supply.domain.payable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.bytter.bill.api.scf.dto.ScfReceivableTransferSaveDTO;
import com.bytter.bill.api.scf.service.IScfVoucherReceivableService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferReceiveDTO;
import com.bytter.supply.domain.receivable.service.ScfReceivableTransferInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应链金融-应收债权凭证门面实现
 * <p>
 * 由 bytter-service-payment 等模块通过 dubbo 调用，跨模块完成：
 * - 应收债权凭证（付款方式 52）转让暂存：透传调用债权凭证转让接口发起暂存，不发起审批流
 * <p>
 * 用户上下文由 DubboUserProviderFilter 从调用方模块透传，此处可直接使用
 * {@link UserContextHelper#helper()#getCurrentUser()}。
 *
 * @author ztd
 * @since 2026/8/21
 */
@Slf4j
@Service("ScfVoucherReceivableServiceImpl")
@DubboService(interfaceClass = IScfVoucherReceivableService.class)
public class ScfVoucherReceivableServiceImpl implements IScfVoucherReceivableService {

    @Autowired
    private ScfReceivableTransferInfoService scfReceivableTransferInfoService;

    /**
     * 应收债权凭证转让提交：按凭证id集合查询暂存转让信息并组装，写入转让信息并发起转让审批流（与页面提交走同一接口）
     */
    @Override
    public void submitTransfer(String voucherIds) {
        if (StringUtils.isBlank(voucherIds)) {
            throw new BusinessException("应收债权凭证转让提交，凭证id集合不能为空");
        }
        // 按凭证id集合查询暂存转让信息（转让标识、接收方等以暂存数据为准）
        List<ScfReceivableTransferInfoVO> tempList = scfReceivableTransferInfoService.listTempByVoucherIds(voucherIds);
        if (CollectionUtil.isEmpty(tempList)) {
            throw new BusinessException("未查询到应收债权凭证转让暂存信息，无法提交");
        }
        ScfReceivableTransferInfoDTO transferInfoDTO = assembleSubmitDTO(voucherIds, tempList);
        // 提交：写入转让信息并发起审批流
        transferInfoDTO.setSendStatus(ScfBusinessConstant.SEND_STATUS_SUBMIT);
        log.info("submitTransfer==req:{}", JSON.toJSONString(transferInfoDTO));
        scfReceivableTransferInfoService.sendTransferInfo(transferInfoDTO, resolveCurrentUser());
    }

    /**
     * 组装转让提交数据：按暂存记录的转让标识组装
     * <p>一对多：多条暂存记录即多个接收方，按凭证编号+接收方列表提交；
     * 多对一：多条暂存记录即多张凭证，按凭证id集合+接收方信息提交。
     */
    private ScfReceivableTransferInfoDTO assembleSubmitDTO(String voucherIds, List<ScfReceivableTransferInfoVO> tempList) {
        ScfReceivableTransferInfoVO first = tempList.get(0);
        ScfReceivableTransferInfoDTO dto = new ScfReceivableTransferInfoDTO();
        dto.setTransferFlag(first.getTransferFlag());
        dto.setPlatformId(first.getPlatformId());
        dto.setPlatformName(first.getPlatformName());
        dto.setProductId(first.getProductId());
        dto.setProductName(first.getProductName());
        dto.setIssueTransferCompany(first.getIssueTransferCompany());
        dto.setIssueTransferCompanyId(first.getIssueTransferCompanyId());
        dto.setCurId(first.getCurId());
        dto.setCurName(first.getCurName());
        if (ScfBusinessConstant.TRANSFER_FLAG_ONE_TO_MANY.equals(first.getTransferFlag())) {
            // 一对多：凭证拆分转让多家，暂存记录即接收方列表
            dto.setVoucherCode(first.getVoucherCode());
            List<ScfReceivableTransferReceiveDTO> receiveList = new ArrayList<>();
            for (ScfReceivableTransferInfoVO item : tempList) {
                ScfReceivableTransferReceiveDTO receive = new ScfReceivableTransferReceiveDTO();
                receive.setId(item.getId());
                receive.setReceiveCompany(item.getReceiveCompany());
                receive.setReceiveCompanyId(item.getReceiveCompanyId());
                receive.setReceiverCreditCode(item.getReceiverCreditCode());
                receive.setTransferDate(item.getTransferDate());
                receive.setTransferAmt(item.getTransferAmt());
                receive.setPurpose(item.getPurpose());
                receive.setRemark(item.getRemark());
                receiveList.add(receive);
            }
            dto.setTransferReceive(receiveList);
        } else {
            // 多对一：多张凭证转让一家，接收方信息取暂存记录
            dto.setVoucherIds(voucherIds);
            dto.setReceiveCompany(first.getReceiveCompany());
            dto.setReceiveCompanyId(first.getReceiveCompanyId());
            dto.setReceiverCreditCode(first.getReceiverCreditCode());
            dto.setTransferDate(first.getTransferDate());
            dto.setTransferAmt(first.getTransferAmt());
            dto.setPurpose(first.getPurpose());
            dto.setRemark(first.getRemark());
        }
        return dto;
    }

    /**
     * 解析当前用户：优先取透传的登录用户，取不到时忽略（暂存流程不依赖用户）
     */
    private ICurrentUser resolveCurrentUser() {
        try {
            return UserContextHelper.helper().getCurrentUser();
        } catch (Exception e) {
            log.warn("获取当前用户信息失败，忽略", e);
            return null;
        }
    }
}
