package com.bytter.bill.domain.bill.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.BillAccKeepAmtDao;
import com.bytter.bill.domain.bill.entity.BillAccKeepAmt;
import com.bytter.bill.domain.bill.vo.BillAccKeepAmtVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Description: 银承账号留存金额
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/3/26
 */
@Service("billAccKeepAmtService")
public class BillAccKeepAmtService extends BaseService<BillAccKeepAmt,String> implements IUserExcelVerifyHandler<BillAccKeepAmtVO> {
    @Autowired
    private BillAccKeepAmtDao billAccKeepAmtDao;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public BaseDao<BillAccKeepAmt, String> getEntityDao() {
        return billAccKeepAmtDao;
    }

    /***
     * 新增
     * @param accKeepAmtVO
     * @param currentUser
     * @return TODO
     */
    @Transactional
    public void save(BillAccKeepAmtVO accKeepAmtVO, ICurrentUser currentUser) {
        // 校验账户是否存在
        BillAccKeepAmt accKeepAmt = billAccKeepAmtDao.checkByBankAcc(accKeepAmtVO.getBankAcc(),"");
        if (ObjectUtil.isNotEmpty(accKeepAmt)){
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_ACC_KEEP_AMT_EXISTS,accKeepAmt.getBankAcc());
        }
        // 新增数据
        BillAccKeepAmt billAccKeepAmt = new BillAccKeepAmt();
        BeanUtils.copyProperties(accKeepAmtVO, billAccKeepAmt);
        // 处理账户信息
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(accKeepAmtVO.getBankAcc());
        if (ObjectUtil.isNotEmpty(bankAccInfo)){
            billAccKeepAmt.setAccName(bankAccInfo.getAccName());
            billAccKeepAmt.setBankType(bankAccInfo.getBankTypeId());
            billAccKeepAmt.setBankTypeName(bankAccInfo.getBankTypeName());
            billAccKeepAmt.setBankCode(bankAccInfo.getBankCode());
            billAccKeepAmt.setBankName(bankAccInfo.getBankName());
        }
        // 处理单位名字
        billAccKeepAmt.setCorpName(corpService.getCorpName(accKeepAmtVO.getCorpId()));
        billAccKeepAmt.setCreateBy(currentUser.getLoginName());
        billAccKeepAmt.setCreateDate(new Date());
        billAccKeepAmtDao.save(billAccKeepAmt);
    }

    /***
     * 修改
     * @param accKeepAmtVO
     * @param currentUser
     * @return TODO
     */
    public void modify(BillAccKeepAmtVO accKeepAmtVO, ICurrentUser currentUser) {
        // 校验账户是否存在
        BillAccKeepAmt accKeepAmt = billAccKeepAmtDao.checkByBankAcc(accKeepAmtVO.getBankAcc(),accKeepAmtVO.getId());
        if (ObjectUtil.isNotEmpty(accKeepAmt)){
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_ACC_KEEP_AMT_EXISTS,accKeepAmt.getBankAcc());
        }
        // 修改
        BillAccKeepAmt billAccKeepAmt = billAccKeepAmtDao.get(accKeepAmtVO.getId());
        BeanUtils.copyProperties(accKeepAmtVO, billAccKeepAmt);
        // 处理账户信息
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(accKeepAmtVO.getBankAcc());
        if (ObjectUtil.isNotEmpty(bankAccInfo)){
            billAccKeepAmt.setAccName(bankAccInfo.getAccName());
            billAccKeepAmt.setBankType(bankAccInfo.getBankTypeId());
            billAccKeepAmt.setBankTypeName(bankAccInfo.getBankTypeName());
            billAccKeepAmt.setBankCode(bankAccInfo.getBankCode());
            billAccKeepAmt.setBankName(bankAccInfo.getBankName());
        }
        // 处理单位名字
        billAccKeepAmt.setCorpName(corpService.getCorpName(accKeepAmtVO.getCorpId()));
        billAccKeepAmt.setUpdateBy(currentUser.getLoginName());
        billAccKeepAmt.setUpdateDate(new Date());
        billAccKeepAmtDao.update(billAccKeepAmt);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BillAccKeepAmtVO obj, Object... user) {
        return null;
    }

    @Override
    public void verifyHandler(List<BillAccKeepAmtVO> list, Object... user) {
        if (!ListUtils.isNullOrEmpty(list)) {
            Map<String,String> bankAccMap = new HashMap<>();
            for (BillAccKeepAmtVO scheduleVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(scheduleVO.getBankAcc())){
                    errorMsgList.add("银行账号：不能为空！");
                }else{
                    BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(scheduleVO.getBankAcc());
                    if (ObjectUtil.isEmpty(bankAccInfo)) {
                        errorMsgList.add("银行账号："+scheduleVO.getBankAcc()+"，输入值不存在！");
                    }else{
                        // 判断是否是票据活期户
                        if (!BtBankAccConstant.ACCTYPE_10.equals(bankAccInfo.getAccType())){
                            errorMsgList.add("银行账号："+scheduleVO.getBankAcc()+"，输入值不是票据活期户！");
                        }

                        if (ObjectUtil.isNotEmpty(billAccKeepAmtDao.checkByBankAcc(scheduleVO.getBankAcc(),""))){
                            errorMsgList.add("银行账号："+scheduleVO.getBankAcc()+"，已存在！不允许重复添加。");
                        }

                        if (bankAccMap.containsKey(scheduleVO.getBankAcc())){
                            errorMsgList.add("银行账号："+scheduleVO.getBankAcc()+"，已存在！不允许重复添加。");
                        }

                        bankAccMap.put(scheduleVO.getBankAcc(),scheduleVO.getBankAcc());
                        scheduleVO.setAccName(bankAccInfo.getAccName());
                        scheduleVO.setBankType(bankAccInfo.getBankTypeId());
                        scheduleVO.setBankTypeName(bankAccInfo.getBankTypeName());
                        scheduleVO.setBankCode(bankAccInfo.getBankCode());
                        scheduleVO.setBankName(bankAccInfo.getBankName());
                        scheduleVO.setCorpId(bankAccInfo.getCorpId());
                        scheduleVO.setCorpName(corpService.getCorpName(bankAccInfo.getCorpId()));
                    }
                }

                if (ObjectUtil.isEmpty(scheduleVO.getKeepAmt())){
                    errorMsgList.add("留底金额：不能为空！");
                }else{
                    if (scheduleVO.getKeepAmt().compareTo(BigDecimal.ZERO) < 0){
                        errorMsgList.add("留底金额：不能小于0！");
                    }
                }
                scheduleVO.addErrorMsg(errorMsgList);
            }
        }

    }

    public void saveImport(List<BillAccKeepAmtVO> list, ICurrentUser currentUser) {
        List<BillAccKeepAmt> result = new ArrayList<>();
        for (BillAccKeepAmtVO accKeepAmtVO : list){
            BillAccKeepAmt accKeepAmt = new BillAccKeepAmt();
            BeanUtils.copyNotNullProperties(accKeepAmtVO,accKeepAmt);
            accKeepAmt.setStatus(CommonConstant.YES);
            accKeepAmt.setCreateBy(currentUser.getLoginName());
            accKeepAmt.setCreateDate(new Date());

            result.add(accKeepAmt);
        }
        if (ObjectUtil.isNotEmpty(result)){
            billAccKeepAmtDao.batchSave(result);
        }

    }

    /***
     * 批量禁用
     * @param vo
     * @param currentUser
     * @return TODO
     */
    @Transactional
    public void batchForbidden(BillAccKeepAmtVO vo, ICurrentUser currentUser) {
        if (ObjectUtil.isNotEmpty(vo.getIds())){
            String[] idArr = vo.getIds().split(",");
            for (String id : idArr){
                BillAccKeepAmt accKeepAmt = billAccKeepAmtDao.get(id,true);
                accKeepAmt.setStatus(CommonConstant.NO);
                accKeepAmt.setUpdateBy(currentUser.getLoginName());
                accKeepAmt.setUpdateDate(new Date());
                billAccKeepAmtDao.update(accKeepAmt);
            }
        }
    }
}
