package com.bytter.ras.service;

import com.bytter.ras.dao.RasBackPayDao;
import com.bytter.ras.entity.RasBackPay;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核退款管理Service
 * @date 2019/5/9 13:39
 */
@Service("rasBackPayService")
@Transactional
public class RasBackPayService extends BaseService<RasBackPay, String> {

    @Autowired
    public RasBackPayDao rasBackPayDao;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired
    private RasShopGrantService rasShopGrantService;

    @Override
    public BaseDao<RasBackPay, String> getEntityDao() {
        return rasBackPayDao;
    }

    /**
     * 设置更新人信息
     */
    public void setUpdateMsg(RasBackPay rasBackPay,ICurrentUser currentUser){
        rasBackPay.setUpdateBy(currentUser.getLoginName());
        rasBackPay.setUpdateDate(DateUtils.getDate());
        rasBackPay.setUpdateName(currentUser.getUsername());
    }
    /**
     * 退款申请处理列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_BACKPAYOP_OP,dataTablesInput.getCurrentUserId());
        //获取该用户有（稽核）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasBackPayDao.getDataTablesUnion(dataTablesInput,corpAuthList,shopAuthList);
    }

    /**
     * 提交退款单（处理页面）
     * @param ids
     */
    public void submitOrder(String ids,ICurrentUser currentUser) {
        for (String id : ids.split(",")) {
            RasBackPay rasBackPay = this.get(id);
            if(RasConstant.RAS_BACK_PAY_STATUS_00.equals(rasBackPay.getStatus())){
                rasBackPay.setStatus(RasConstant.RAS_BACK_PAY_STATUS_11);//退款单状态---》处理中
                rasBackPay.setSendSign(RasConstant.RAS_BACK_PAY_SEND_SIGN_11);//指令发送状态---》待发送
                this.setUpdateMsg(rasBackPay,currentUser);//设置更新人信息
                this.updateEntity(rasBackPay);
            }else{
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
    }
    /**
     * 打回退款单（处理页面）
     * @param ids
     * @param msg 打回原因
     */
    public void backOrder(String ids, String msg,ICurrentUser currentUser) {
        for (String id : ids.split(",")) {
            RasBackPay rasBackPay = this.get(id);
            if(RasConstant.RAS_BACK_PAY_STATUS_00.equals(rasBackPay.getStatus())||
                    RasConstant.RAS_BACK_PAY_STATUS_10.equals(rasBackPay.getStatus())){
                rasBackPay.setStatus(RasConstant.RAS_BACK_PAY_STATUS_RETURN_1);//退款单状态---》已打回
                rasBackPay.setMsg(msg);//设置打回原因
                this.setUpdateMsg(rasBackPay,currentUser);//设置更新人信息
                this.updateEntity(rasBackPay);
            }else{
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
    }

    /**
     * 退款申请查询列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnionQuery(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_BACKPAYQUERY_OP,dataTablesInput.getCurrentUserId());
        //获取该用户有（稽核）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasBackPayDao.getDataTablesUnionQuery(dataTablesInput,corpAuthList,shopAuthList);
    }

    /**
     * 重发退款单
     * @param ids
     */
    public void reSendOrder(String ids,ICurrentUser currentUser) {
        RasBackPay rasBackPay = this.get(ids);
        //判断状态是否正确
        if(RasConstant.RAS_BACK_PAY_SEND_SIGN_00.equals(rasBackPay.getSendSign())){
            rasBackPay.setSendSign(RasConstant.RAS_BACK_PAY_SEND_SIGN_11);//指令发送状态---》待发送
            this.setUpdateMsg(rasBackPay,currentUser);//设置更新人信息
            this.updateEntity(rasBackPay);
        }else{
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
    }

    /**
     * 打回退款单（查询页面）
     * @param ids
     */
    public void backOrderQuery(String ids,ICurrentUser currentUser) {
        RasBackPay rasBackPay = this.get(ids);
        //判断状态是否正确
        if(RasConstant.RAS_BACK_PAY_SEND_SIGN_00.equals(rasBackPay.getSendSign())){
            rasBackPay.setStatus(RasConstant.RAS_BACK_PAY_STATUS_10);//退款单状态---》待打回
            this.setUpdateMsg(rasBackPay,currentUser);//设置更新人信息
            this.updateEntity(rasBackPay);
        }else{
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
    }
}
