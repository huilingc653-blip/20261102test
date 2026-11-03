package com.bytter.check.reconcile.service;

import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.dao.BisCheckDtlDao;
import com.bytter.check.reconcile.dao.BisCheckExcDao;
import com.bytter.check.reconcile.dao.BisTrackingDao;
import com.bytter.check.reconcile.entity.BisCheckExc;
import com.bytter.check.reconcile.vo.BisTrackingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhenghj
 * @Title: Service
 * @Description:银企对账跟踪Service
 * @date 2020-10-16
 */
@Service("bisTrackingService")
@Transactional
public class BisTrackingService extends BaseService<BisTrackingVO, String> {
    @Autowired
    private BisTrackingDao bisTrackingDao;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @Autowired
    private BisCheckExcDao bisCheckExcDao;
    @Autowired
    private BisCheckDtlDao bisCheckDtlDao;
    @Autowired
    private BisCheckExcService bisCheckExcService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    public IBankTypeService iBankTypeService;

    @Override
    public BaseDao<BisTrackingVO, String> getEntityDao() {
        return null;
    }
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        List<String> corpAuthList = iCorpService.getUserCorpIds(ReceiptConstant.BIS_BANK_STATETRACKING_CHECK_QUERY, input.getCurrentUserId());
        Map<String,Object> params = new HashMap<>();
        String insBankTypeId=  input.getFormdata().get("ins_bankTypeId")+"";
        String joinSql = " and acc.bank_type_id=:insBankTypeId";
        params.put("insBankTypeId", insBankTypeId);
        input.getFormdata().remove("ins_bankTypeId");
        List<String> bankAccList=iBtBankAccService.getBankAccsByCorpIdList(corpAuthList,joinSql,params);
        DataTablesOutput<BisTrackingVO> data = bisTrackingDao.getDataTablesUnion(input, corpAuthList,bankAccList,insBankTypeId);
        List<BisTrackingVO> list=data.getData();
        List<String> accList = list.stream().map(BisTrackingVO::getBankAcc).collect(Collectors.toList());
        Map<String, BtBankAccDTO> bankAccMap = iBtBankAccService.getBankAccByInfoAccs(accList);
        Map<String, CorpDTO> corpMap =iCorpService.listCorpDtoByCorpIds(corpAuthList);
        Map<String, BankTypeDTO> bankTypeMap = iBankTypeService.getBankTypeMap();
        for(BisTrackingVO vo:list){
            BtBankAccDTO acc=bankAccMap.get(vo.getBankAcc());

            CorpDTO corp=corpMap.get(vo.getCorpId());
            vo.setCorpCodeAndName("（"+corp.getCode()+")"+corp.getName());
            vo.setCheckResultName(dictService.get("bisCheckDtl.mark",vo.getCheckResult()));
            vo.setVoucherStatName(dictService.get("bisExc.voucherStat",vo.getVoucherStat()));
            vo.setCheckStatusName(dictService.get("bisCheckDtl.status",vo.getCheckStatus()));
            vo.setCheckStatName(dictService.get("bisCheckDtl.status",vo.getCheckStat()));
            if(bankTypeMap.containsKey(vo.getBankTypeId())){
                vo.setBankTypeName(bankTypeMap.get(vo.getBankTypeId()).getTypeName());
            }

            if(acc == null){
                continue;
            }
            vo.setCurName(acc.getCurName());
            vo.setBankTypeId(acc.getBankTypeId());
            vo.setBankTypeName(acc.getBankTypeName());
        }
        // T24090015094 银企对账功能优化：银企对账针对招商银行增加字段【对账账户】以及【是否保证金】用于对账
        list.forEach(item -> {
            String expandFields = item.getExpandFields();
            if (StringUtils.isNotEmpty(expandFields)) {
                String[] split = expandFields.split("\\|");// 大参林该字段格式以“|”隔开
                // 【对账账户】字段取expandFields第三个值
                if (split.length >= 3 && StringUtils.isNotEmpty(split[2])) {
                    item.setCheckAccount(split[2]);
                } else {
                    // 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
                    item.setCheckAccount(item.getBankAcc());
                }
                // 【是否保证金】字段取expandFields第五个值，判断如值为Y，则显示“是”，该值为N或者没有值，则显示“否”。
                if (split.length >= 5 && StringUtils.isNotEmpty(split[4])) {
                    if ("Y".equals(split[4].toUpperCase())) {
                        item.setIsBail(CommonConstant.YES);
                    } else {
                        item.setIsBail(CommonConstant.NO);
                    }
                } else {
                    item.setIsBail(CommonConstant.NO);
                }
            } else {
                // 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
                item.setCheckAccount(item.getBankAcc());
                // 如果该字段没有值，则显示“否”
                item.setIsBail(CommonConstant.NO);
            }
        });
        return data;
    }
    public BisTrackingVO getCheckVo(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", id);
        BisTrackingVO checkVo = bisTrackingDao.getCheckVo(params);
        if(checkVo != null){
            if(StringUtils.isNotEmpty(checkVo.getCheckResult())){
                checkVo.setCheckResultName(dictService.get("bisCheckDtl.mark", checkVo.getCheckResult()));
            }
            if(StringUtils.isNotEmpty(checkVo.getVoucherStat())){
                checkVo.setVoucherStatName(dictService.get("bisExc.voucherStat", checkVo.getVoucherStat()));
            }
            if(StringUtils.isNotEmpty(checkVo.getAppScope())){
                checkVo.setAppScopeName(dictService.get("bisCheckDtl.appScope", checkVo.getAppScope()));
            }
        }
        return checkVo;
    }
    /**
     * 生成指令
     *
     * @param bean
     * @return
     */
    public void manualExc(BisTrackingVO bean, ICurrentUser currentUser) {
        if (StringUtils.isNotEmpty(bean)) {
            BisCheckExc bisCheckExc = new BisCheckExc();
            bisCheckExc.setCorpId(bean.getCorpId());
            bisCheckExc.setDtlId(bean.getDtlId());
            bisCheckExc.setBankTypeId(bean.getBankTypeId());
            bisCheckExc.setBeginDate(bean.getDateStart());
            bisCheckExc.setEndDate(bean.getDateEnd());
            bisCheckExc.setBusType(bean.getBusType());
            bisCheckExc.setCurId(bean.getCurId());
            bisCheckExc.setBankAcc(bean.getBankAcc());
            bisCheckExc.setCheckStatus(bean.getCheckStatus());
            bisCheckExc.setCbsBillNo(bean.getBillNo());
            bisCheckExc.setCheckDate(new Date());
            bisCheckExc.setDtlId(bean.getId());
            bisCheckExc.setVoucherStat(bean.getVoucherStat());
            bisCheckExc.setBal(bean.getBal());
            bisCheckExc.setCreateDate(new Date());
            bisCheckExc.setCreateBy(currentUser.getLoginName());
            bisCheckExcDao.save(bisCheckExc);
        }
    }
    public AjaxJson reviewSure(BisTrackingVO bean, ICurrentUser currentUser) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(bean) && !StringUtils.isBlank(bean.getIds())) {
            Map<String, Object> beanParams = new HashMap<String, Object>();
            beanParams.put("ids", Arrays.asList(bean.getIds().split(",")));
            List<BisTrackingVO> liseBean = bisTrackingDao.queryList(beanParams);
            if (ListUtils.isNotNullOrEmpty(liseBean)) {
                for (BisTrackingVO vo : liseBean) {
                    //生成新指令
                    vo.setVoucherStat(BdlinkConstant.PRE_SEND);
                    vo.setBusType(ReceiptConstant.BIS_BANK_BUS_TYPE_4);
                    vo.setDateStart(vo.getToBillDate());
                    vo.setDateEnd(vo.getToBillDate());
                    vo.setCheckStatus(bean.getOpt());
                    this.manualExc(vo, currentUser);
                    //复核通过 | 不通过
                    //修改当前流水状态状态为3准备发送
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("updateDate", new Date());
                    params.put("updateBy", currentUser.getLoginName());
                    params.put("voucherStat", BdlinkConstant.PRE_SEND);
                    params.put("ids", bean.getIds().split(","));
                    params.put("checkDate", new Date());
                    params.put("checkStatus", bean.getOpt());
                    bisCheckDtlDao.updateVoucherStatByIds(params);
                }
            }
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 重新发送指令
     *
     * @param bisTrackingVO
     * @return
     */
    public boolean reSend(BisTrackingVO bisTrackingVO, ICurrentUser userVo) {
        String bisExcId = bisTrackingVO.getBisExcId();
        if (StringUtils.isEmpty(bisExcId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] idArrays = bisExcId.split(",");
        // 批量修改指令状态为待发送
        int num = bisCheckExcService.batchUpdateByIds(idArrays, userVo);
        if (num != idArrays.length ) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        return true;
    }
}
