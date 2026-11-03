package com.bytter.check.reconcile.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.check.reconcile.dao.BisCheckDtlDao;
import com.bytter.check.reconcile.dao.BisCheckReasonDao;
import com.bytter.check.reconcile.entity.BisCheckReason;
import com.bytter.check.reconcile.vo.BisCheckReasonVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bisCheckReasonService")
@Transactional
public class BisCheckReasonService extends BaseService<BisCheckReason, String> {
    @Autowired
    public BisCheckReasonDao bisCheckReasonDao;

    @Autowired
    public BisCheckDtlDao bisCheckDtlDao;


    @Override
    public BaseDao<BisCheckReason, String> getEntityDao() {
        return bisCheckReasonDao;
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        return bisCheckReasonDao.getDataTablesUnion(input);
//		(select bbt.type_Name from bt_bank_type bbt where bbt.id=br.bank_type_id) as bankTypeName
    }

    public AjaxJson saveOrUpdate(String operator,
                                 BisCheckReasonVO bisCheckReasonVO, ICurrentUser currentUser) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        BisCheckReason bisCheckReason = new BisCheckReason();
        BeanUtils.copyNotNullProperties(bisCheckReasonVO, bisCheckReason);
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isBlank(bisCheckReason.getId())) {
            params.put("brId", bisCheckReason.getId());
        }
        params.put("reasonCode", bisCheckReason.getReasonCode());
        params.put("reasonName", bisCheckReason.getReasonName());
        //code name唯一判断
        if (bisCheckReasonDao.repeatCheck(params)) {
            throw new BusinessException(NetbankMessageEnum.BIS_CHECK_REASON_MESSAGE_MTB_ERROR);
        }
        if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
            bisCheckReason.setCreateDate(new Date());
            bisCheckReason.setCreateBy(currentUser.getLoginName());
            this.saveEntity(bisCheckReason);
        } else {
            bisCheckReason.setUpdateDate(new Date());
            bisCheckReason.setUpdateBy(currentUser.getLoginName());
            this.updateEntity(bisCheckReason);
        }
        reJson.setSuccess(true);
        return reJson;
    }

    public AjaxJson saveOrUpdate(String ids, ICurrentUser currentUser) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(ids)) {
            if (bisCheckDtlDao.getObjectByResonId(ids)) {
                throw new BusinessException(NetbankMessageEnum.BIS_CHECK_REASON_MESSAGE_BDT_ERROR);
            }
            if (bisCheckReasonDao.updateValidSign(ids) > 0) {
                reJson.setSuccess(true);
            }
        }
        return reJson;
    }

    public BisCheckReasonVO getBisCheckReason(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validSign2", ReceiptConstant.BIS_BANK_VALID_SIGN_2);
        params.put("brId", id);
        return bisCheckReasonDao.getBisCheckReason(params);
    }

    public List<BisCheckReasonVO> getListByValidSign(Map<String, Object> params) {
        return bisCheckReasonDao.getListByValidSign(params);
    }

}
