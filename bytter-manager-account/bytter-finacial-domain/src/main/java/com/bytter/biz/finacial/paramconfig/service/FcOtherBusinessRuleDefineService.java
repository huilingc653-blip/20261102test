package com.bytter.biz.finacial.paramconfig.service;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.dao.FcBusinessRuleDefineDao;
import com.bytter.biz.finacial.paramconfig.dao.FcNisBusTypeRuleDao;
import com.bytter.biz.finacial.paramconfig.dao.FcOtherBusinessRuleDefineDao;
import com.bytter.biz.finacial.paramconfig.entity.FcNisBusTypeRule;
import com.bytter.biz.finacial.paramconfig.entity.FcOtherBusinessRuleDefine;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineListVo;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lijinquan
 * @description:
 * @date 2024-10-28 14:25
 */
@Service("fcOtherPaymentAutoTallyService")
@Transactional
public class FcOtherBusinessRuleDefineService extends BaseService<FcOtherBusinessRuleDefine,String> {

    @Autowired
    private FcOtherBusinessRuleDefineDao fcOtherBusinessRuleDefineDao;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @Autowired(required = false)
    private FcNisBusTypeRuleDao fcNisBusTypeRuleDao;
    @Autowired
    private FcVoucherEntryDefineService voucherEntryService;
    @Autowired
    private FcBusinessRuleDefineDao fcBusinessRuleDefineDao;

    @Override
    public BaseDao<FcOtherBusinessRuleDefine, String> getEntityDao() {
        return fcOtherBusinessRuleDefineDao;
    }

    /**
     * 功能介绍 : 业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
        DataTablesOutput dataTablesOutput = fcOtherBusinessRuleDefineDao.getBusninessRuleVoList(input);
        List data = dataTablesOutput.getData();
        ArrayList<String> netIds = new ArrayList<String>();
        ArrayList<String> curIds = new ArrayList<String>();
        ArrayList<FcOtherBusinessRuleDefineListVo> vos = new ArrayList<>();
        for (Object datum : data) {
            FcOtherBusinessRuleDefineListVo vo = (FcOtherBusinessRuleDefineListVo)datum;
            if (StringUtils.isNotEmpty(vo.getNetId())) {
                netIds.add(vo.getNetId());
            }
            curIds.add(vo.getCurId());
            vos.add(vo);
        }
        List<CorpDTO> corpDTOS = iCorpService.listCorpDTOByCorpIds(netIds);
        Map<String, CorpDTO> corpIdMap = corpDTOS.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
        Map<String, BtCurrencyDTO> curIdMap = iBtCurrencyService.getCurrencyByIds(curIds);
        vos.forEach( a -> {
            CorpDTO corpDTO = corpIdMap.get(a.getNetId());
            BtCurrencyDTO btCurrencyDTO = curIdMap.get(a.getCurId());

            if (ObjectUtils.isNotEmpty(corpDTO)){
                a.setNetName(corpDTO.getName());
            }
            if (ObjectUtils.isNotEmpty(btCurrencyDTO)){
                a.setCurName(btCurrencyDTO.getCurName());
                a.setCurCode(btCurrencyDTO.getCurCode());
            }
        });
        return dataTablesOutput;
    }

    /**
     * 功能介绍 : 根据记账规则ID获取记账规则信息和分录明细。<br>
     * <b>日期：</b>2017年5月24日<br>
     * @version 1.0
     * @author liyh
     */
    public FcOtherBusinessRuleDefineVo getBusinessRuleDefineVoById(String id) {
        FcOtherBusinessRuleDefineVo ruleDefineVo = new FcOtherBusinessRuleDefineVo();
        FcOtherBusinessRuleDefine fcOtherBusinessRuleDefine = this.get(id);
        if (null == fcOtherBusinessRuleDefine) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        BeanUtils.copyProperties(fcOtherBusinessRuleDefine, ruleDefineVo);
        List<FcVoucherEntryDefineVo> entryDefineVos=voucherEntryService.getVoucherEntryDefinesByRuleId(id);
        for (FcVoucherEntryDefineVo fcVoucherEntryDefineVo : entryDefineVos) {
            if(fcVoucherEntryDefineVo.getSubjectName() == null){
                fcVoucherEntryDefineVo.setSubjectCode(null);
            }
        }
        ruleDefineVo.setVouchers(entryDefineVos);
        return ruleDefineVo;
    }


    /**
     * 功能介绍：保存记账规则定义和分录数据<br>
     * <b>日期：</b>2017年5月24日<br>
     * @version 1.0
     * @author liyh
     */
    public void saveBusinessRule(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefineVo, HttpServletRequest request, ICurrentUser user) {
        List<FcVoucherEntryDefineVo> vouchers = fcOtherBusinessRuleDefineVo.getVouchers();
        //分录不能为空
        if (null == vouchers || vouchers.isEmpty()) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        FcOtherBusinessRuleDefine fcOtherBusinessRuleDefine = new FcOtherBusinessRuleDefine();
        fcOtherBusinessRuleDefineVo.setCode(getDefineCode(fcOtherBusinessRuleDefineVo));
        //将网点，科目，币别等的空格过滤掉
        fcOtherBusinessRuleDefineVo.setCurCode(StringUtils.formatNull(fcOtherBusinessRuleDefineVo.getCurCode()).trim());
        fcOtherBusinessRuleDefineVo.setNetId(StringUtils.formatNull(fcOtherBusinessRuleDefineVo.getNetId()).trim());
//        fcOtherBusinessRuleDefineVo.setSubjCode(StringUtils.formatNull(fcOtherBusinessRuleDefineVo.getSubjCode()).trim());
        fcOtherBusinessRuleDefineVo.setSysRaceCode(StringUtils.formatNull(fcOtherBusinessRuleDefineVo.getSysRaceCode()).trim());
        //拷贝数据对象
        BeanUtils.copyProperties(fcOtherBusinessRuleDefineVo, fcOtherBusinessRuleDefine);
        //是否已存在相同的记账规则
        checkSameRuleDefine(fcOtherBusinessRuleDefineVo);
        fcOtherBusinessRuleDefine.setCreateBy(user.getId());
        fcOtherBusinessRuleDefine.setCreateTime(DateUtils.getDate());
        super.saveEntity(fcOtherBusinessRuleDefine);
        Set<String> summaryContent = new HashSet<String>();
        //校验分录是否包含借贷标识
        boolean debitFlag = true, creditFlag = true;
        for (FcVoucherEntryDefineVo entryDefine : vouchers) {
            entryDefine.setBusinessRuleDefineId(fcOtherBusinessRuleDefine.getId());
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
                debitFlag = false;
            }
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
                creditFlag = false;
            }
            entryDefine.setBelongType(20);
            if(!EmptyUtils.isEmpty(entryDefine.getSummary())){
                summaryContent.add(entryDefine.getSummary());
            }
        }
        //提示分录不包含借贷，必须借贷方都要定义
        if (creditFlag || debitFlag) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
        }
        //批量保存分录
        List<FcVoucherEntryDefine> entryDefines = BeanUtils.copyListProperties(vouchers, FcVoucherEntryDefine.class);
        voucherEntryService.batchSave(entryDefines);

        //保存匹配内容
        saveOrUpdateMatchContent(fcOtherBusinessRuleDefine.getId(),request);
    }

    /**
     * 生成编码
     * @return
     */
    private String getDefineCode(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefineVo) {
        String code = null;
        String prefix = null;
        String dateStr = DateUtils.date2Str(new Date(), DateUtils.yyyyMMdd());
        if("0".equals(fcOtherBusinessRuleDefineVo.getCdSign())){ //其他收款
            prefix = "A"+dateStr;
        }else { //其他付款
            prefix = "B"+dateStr;
        }
        if(StringUtils.isBlank(prefix)) return null;
        String maxCode = fcOtherBusinessRuleDefineDao.getMaxCodeBy(prefix);
        if(StringUtils.isBlank(maxCode)){
            code = prefix + "001";
        }else {
            int i = Integer.parseInt(maxCode.substring(9, 12));
            code = prefix +  String.format("%03d", i+1);
        }
        return code;
    }

    private void saveOrUpdateMatchContent(String defineId,HttpServletRequest request) {
        String[] matchRule = request.getParameterValues("matchRule");
        String[] matchElements = request.getParameterValues("matchElements");
        String[] matchRelations = request.getParameterValues("matchRelations");
        String[] matchContent = request.getParameterValues("matchContent");
        String[] leftParenthesis = request.getParameterValues("leftParenthesis");
        String[] rightParenthesis = request.getParameterValues("rightParenthesis");
        List<FcNisBusTypeRule> fcNisBusTypeRuleList = new ArrayList<>();
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;
        for(int i=0;i<matchElements.length;i++){
            if(StringUtils.isNotEmpty(matchElements[i].trim())){
                FcNisBusTypeRule fcNisBusTypeRule = new FcNisBusTypeRule();
                fcNisBusTypeRule.setMatchRule(matchRule[i]);
                fcNisBusTypeRule.setMatchElements(matchElements[i]);
                if(!EmptyUtils.isEmpty(matchRelations) && !EmptyUtils.isEmpty(matchRelations[i])) {
                    fcNisBusTypeRule.setMatchRelations(matchRelations[i]);
                }
                fcNisBusTypeRule.setMatchContent(matchContent[i]);
                fcNisBusTypeRule.setSort(i);
                fcNisBusTypeRule.setBusId(defineId);
                if(!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length>i) {
                    fcNisBusTypeRule.setLeftParenthesis(leftParenthesis[i]);
                    if("(".equals(leftParenthesis[i])){
                        leftCount++;
                        leftSort +=i;
                    }
                }
                if(!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length>i) {
                    fcNisBusTypeRule.setRightParenthesis(rightParenthesis[i]);
                    if(")".equals(rightParenthesis[i])){
                        rightCount++;
                        rightSort +=i;
                    }
                }
                fcNisBusTypeRuleList.add(fcNisBusTypeRule);
            }
        }
        if(leftCount != rightCount || leftSort>rightSort){
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
        }
        //先删除规则再插入
        fcNisBusTypeRuleDao.deleteByBusId(defineId);
        fcNisBusTypeRuleDao.batchSave(fcNisBusTypeRuleList);
    }

    /**
     * 功能介绍 : 是否存在相同的记账规则。<br>
     * <b>日期：</b>2017年11月23日<br>
     * @version 1.0
     * @author liyh
     */
    public void checkSameRuleDefine(FcOtherBusinessRuleDefineVo ruleDefineVo) {
        List<FcOtherBusinessRuleDefineVo> ruleDefineVos = fcOtherBusinessRuleDefineDao.existBusinessRule(ruleDefineVo);
        if (!EmptyUtils.isEmptyList(ruleDefineVos)) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SAME);
        }
    }


    /**
     * 功能介绍 : 根据记账规则ID查看记账规则信息和分录明细。<br>
     * <b>日期：</b>2017年9月23日<br>
     * @version 1.0
     * @author yangl
     */
    public FcOtherBusinessRuleDefineVo getBusRuleDefineVoById(String id) {
        FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefineVo = this.fcOtherBusinessRuleDefineDao.getBusninessRuleVo(id);
        if (null == fcOtherBusinessRuleDefineVo) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        List<FcVoucherEntryDefineVo> entryDefineVos=voucherEntryService.getVoucherEntryDefinesByRuleId(id);
        fcOtherBusinessRuleDefineVo.setVouchers(entryDefineVos);
        String corpId = fcOtherBusinessRuleDefineVo.getNetId();
        if (StringUtils.isNotBlank(corpId)){
            CorpDTO corpDTO = iCorpService.get(corpId);
            fcOtherBusinessRuleDefineVo.setNetId(corpDTO.getName());
        }
        String curId = fcOtherBusinessRuleDefineVo.getCurId();
        if (StringUtils.isNotBlank(curId)) {
            BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
            fcOtherBusinessRuleDefineVo.setCurCode(btCurrencyDTO.getCurName());
        }

        FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(fcOtherBusinessRuleDefineVo.getVoucherId());
        if (ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getVoucherName()))fcOtherBusinessRuleDefineVo.setVoucherType(dto.getVoucherName()); //历史遗留
        return fcOtherBusinessRuleDefineVo;
    }

    /**
     * 功能介绍：更新记账规则定义和分录数据<br>
     */
    public void updateBusinessRule(FcOtherBusinessRuleDefineVo ruleDefineVo, HttpServletRequest request,ICurrentUser user) {
        List<FcVoucherEntryDefine> vouchers = BeanUtils.copyListProperties(ruleDefineVo.getVouchers(), FcVoucherEntryDefine.class);
        //分录不能为空
        if (null == vouchers || vouchers.isEmpty()) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        FcOtherBusinessRuleDefine fcBusinessRuleDefine = this.get(ruleDefineVo.getId());
        //将网点，科目，币别等的空格过滤掉
        verify(ruleDefineVo);
        ruleDefineVo.setCurCode(ruleDefineVo.getCurCode().trim());
        ruleDefineVo.setNetId(ruleDefineVo.getNetId().trim());
        ruleDefineVo.setSysRaceCode(ruleDefineVo.getSysRaceCode().trim());

        if (!EmptyUtils.isEmpty(fcBusinessRuleDefine)) {
            ruleDefineVo.setId(fcBusinessRuleDefine.getId());
        }
        checkSameRuleDefine(ruleDefineVo);
        //拷贝数据对象
        BeanUtils.copyNotNullProperties(ruleDefineVo, fcBusinessRuleDefine);
        fcBusinessRuleDefine.setUpdateBy(user.getId());
        fcBusinessRuleDefine.setUpdateTime(DateUtils.getDate());
        super.updateEntity(fcBusinessRuleDefine);
        Set<String> summaryContent = new HashSet<String>();
        //校验分录是否包含借贷标识
        boolean debitFlag = true, creditFlag = true;
        for (FcVoucherEntryDefine entryDefine : vouchers) {
            entryDefine.setBusinessRuleDefineId(ruleDefineVo.getId());
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
                debitFlag = false;
            }
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
                creditFlag = false;
            }
            entryDefine.setBelongType(20);
            if(!EmptyUtils.isEmpty(entryDefine.getSummary())){
                summaryContent.add(entryDefine.getSummary());
            }
        }
        //提示分录不包含借贷，必须借贷方都要定义
        if (creditFlag || debitFlag) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
        }
        //先删除分录明细，再批量保存分录
        voucherEntryService.deleteEntryDefinesByRuleId(ruleDefineVo.getId());
        voucherEntryService.batchSave(vouchers);

        saveOrUpdateMatchContent(ruleDefineVo.getId(),request);
    }


    /***
     * 验证
     */
    public void verify(FcOtherBusinessRuleDefineVo ruleDefineVo) {
        /** 网点 **/
        if (EmptyUtils.isEmpty(ruleDefineVo.getNetId())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_NETID_NOTNULL);
        }

        /** 币别 **/
        if (EmptyUtils.isEmpty(ruleDefineVo.getCurCode())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_CURID_NOTNULL);
        }
    }

    /**
     * 功能介绍 : 根据记账规则ID删除分录和规则数据<br>
     * <b>日期：</b>2017年5月24日<br>
     * @version 1.0
     * @author liyh
     */
    public void deleteBusinessRule(String ruleId) {
        //删除记账分类规则
        voucherEntryService.deleteEntryDefinesByRuleId(ruleId);
        //删除匹配内容
        fcNisBusTypeRuleDao.deleteByBusId(ruleId);
        this.delete(ruleId);
    }
}
