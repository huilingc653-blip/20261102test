package com.bytter.bill.domain.bill.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillRuleBillDtlRmkDao;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmk;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillDtlRmkDtl;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillRuleBillDtlRmkVO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhous
 * @Title: Service
 * @Description: 票据流水备注规则
 * @date 2025-02-24
 */
@Service("AbBillRuleBillDtlRmkService")
@Transactional
public class AbBillRuleBillDtlRmkService extends BaseService<AbBillRuleBillDtlRmk, String> {



    @Autowired
    private AbBillRuleBillDtlRmkDao abBillRuleBillDtlRmkDao;
    @Autowired
    private AbBillRuleBillDtlRmkDtlService abBillRuleBillDtlRmkDtlService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<AbBillRuleBillDtlRmk, String> getEntityDao() {
        return abBillRuleBillDtlRmkDao;
    }

    /**
     * 票据流水备注规则
     *
     * @param input
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getBillRuleList(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(
                AccebillConstant.AB_BILL_RESOURCE_RULEBILLDTLRMK_QUERY, input.getCurrentUserId());
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_RESOURCE_RULEBILLDTLRMK_QUERY, corpIds);
        DataTablesOutput<AbBillRuleBillDtlRmkVO> out = abBillRuleBillDtlRmkDao.getDataTablesUnion(input, corpAuthList);
        Optional.ofNullable(out.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("accebill_billNo_insbriefid","valid_sign");
            for (AbBillRuleBillDtlRmkVO vo : listData){
                injectValues(vo, listDictMap);
            }
        });
        return out;
    }


    /**
    * @Description 注入字典信息
    * @author zhous
    * @date 2025/2/25 8:45
    */
    private void injectValues(AbBillRuleBillDtlRmkVO bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 业务类型
        Optional.ofNullable(bill.getBustype()).ifPresent(busType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("accebill_billNo_insbriefid");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType)))
                    .ifPresent(dict -> bill.setBustypeName(dict.getName()));
        });
        // 有效标志
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("valid_sign");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(status))))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
    }

    /**
    * @Description 判断是否存在重复的有效信息
    * @author zhous
    * @date 2025/2/25 8:45
    */
    public boolean checkAddBillDtlRmk(AbBillRuleBillDtlRmkVO abBillRuleBillDtlRmkVO,boolean isSave) {
        List<AbBillRuleBillDtlRmkVO> list = abBillRuleBillDtlRmkDao.getByRepeat(abBillRuleBillDtlRmkVO,isSave);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * @Description 保存票据流水备注规则
    * @author zhous
    * @date 2025/2/25 8:45
    */
    public void save(AbBillRuleBillDtlRmk entity, List<AbBillRuleBillDtlRmkDtlVO> ruleDtl) {
        super.saveEntity(entity);
        batchSaveRuleDtl(entity.getId(), ruleDtl);
    }

    /**
    * @Description 保存票据流水备注规则（旧版 request 并行数组参数方式，兼容旧接口）
    * @author zhous
    * @date 2025/2/25 8:45
    */
    public void save(AbBillRuleBillDtlRmk entity, HttpServletRequest request) {
        save(entity, parseRuleDtlFromRequest(request));
    }

    /**
    * @Description 保存更新票据流水备注规则
    * @author zhous
    * @date 2025/2/25 8:45
    */
    public void saveOrUpdate(AbBillRuleBillDtlRmk entity, List<AbBillRuleBillDtlRmkDtlVO> ruleDtl) {
        super.saveOrUpdate(entity);
        //先删除规则再插入
        abBillRuleBillDtlRmkDtlService.deleteByBusId(entity.getId());
        batchSaveRuleDtl(entity.getId(), ruleDtl);
    }

    /**
    * @Description 保存更新票据流水备注规则（旧版 request 并行数组参数方式，兼容旧接口）
    * @author zhous
    * @date 2025/2/25 8:45
    */
    public void saveOrUpdate(AbBillRuleBillDtlRmk entity, HttpServletRequest request) {
        saveOrUpdate(entity, parseRuleDtlFromRequest(request));
    }

    /**
    * @Description 将旧版 request 的并行数组参数转换为明细列表
    */
    private List<AbBillRuleBillDtlRmkDtlVO> parseRuleDtlFromRequest(HttpServletRequest request) {
        String[] matchRule = request.getParameterValues("matchRule");
        String[] matchElements = request.getParameterValues("matchElements");
        String[] matchRelations = request.getParameterValues("matchRelations");
        String[] matchContent = request.getParameterValues("matchContent");
        String[] leftParenthesis = request.getParameterValues("leftParenthesis");
        String[] rightParenthesis = request.getParameterValues("rightParenthesis");
        List<AbBillRuleBillDtlRmkDtlVO> ruleDtl = new ArrayList<>();
        if (EmptyUtils.isEmpty(matchElements)) {
            return ruleDtl;
        }
        for (int i = 0; i < matchElements.length; i++) {
            AbBillRuleBillDtlRmkDtlVO vo = new AbBillRuleBillDtlRmkDtlVO();
            vo.setMatchRule(getArrayValue(matchRule, i));
            vo.setMatchElements(matchElements[i]);
            vo.setMatchRelations(getArrayValue(matchRelations, i));
            vo.setMatchContent(getArrayValue(matchContent, i));
            vo.setLeftParenthesis(getArrayValue(leftParenthesis, i));
            vo.setRightParenthesis(getArrayValue(rightParenthesis, i));
            ruleDtl.add(vo);
        }
        return ruleDtl;
    }

    /**
    * @Description 获取数组指定下标的值，越界或为空返回null
    */
    private String getArrayValue(String[] array, int index) {
        if (EmptyUtils.isEmpty(array) || index >= array.length) {
            return null;
        }
        return array[index];
    }

    /**
    * @Description 构建明细并保存，含空值判断与括号匹配校验
    */
    private void batchSaveRuleDtl(String busId, List<AbBillRuleBillDtlRmkDtlVO> ruleDtl) {
        if (ruleDtl == null || ruleDtl.isEmpty()) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_CLAIM_RULE_ISNULL);
        }
        List<AbBillRuleBillDtlRmkDtl> abBillRuleBillDtlRmkDtls = new ArrayList<>();
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;
        int sort = 0;
        for (AbBillRuleBillDtlRmkDtlVO vo : ruleDtl) {
            if (StringUtils.isEmpty(vo.getMatchElements()) || StringUtils.isEmpty(vo.getMatchElements().trim())) {
                continue;
            }
            AbBillRuleBillDtlRmkDtl abBillRuleBillDtlRmkDtl = new AbBillRuleBillDtlRmkDtl();
            abBillRuleBillDtlRmkDtl.setMatchRule(vo.getMatchRule());
            abBillRuleBillDtlRmkDtl.setMatchElements(vo.getMatchElements());
            abBillRuleBillDtlRmkDtl.setMatchRelations(vo.getMatchRelations());
            abBillRuleBillDtlRmkDtl.setMatchContent(vo.getMatchContent());
            abBillRuleBillDtlRmkDtl.setSort(sort);
            abBillRuleBillDtlRmkDtl.setBusId(busId);
            abBillRuleBillDtlRmkDtl.setLeftParenthesis(vo.getLeftParenthesis());
            abBillRuleBillDtlRmkDtl.setRightParenthesis(vo.getRightParenthesis());
            if ("(".equals(vo.getLeftParenthesis())) {
                leftCount++;
                leftSort += sort;
            }
            if (")".equals(vo.getRightParenthesis())) {
                rightCount++;
                rightSort += sort;
            }
            abBillRuleBillDtlRmkDtls.add(abBillRuleBillDtlRmkDtl);
            sort++;
        }
        if (leftCount != rightCount || leftSort > rightSort) {
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
        }
        abBillRuleBillDtlRmkDtlService.batchSave(abBillRuleBillDtlRmkDtls);
    }

    /**
    * @Description 根据票据流水备注规则主表id获取明细数据
    * @author zhous
    * @date 2025/2/25 8:46
    */
    public List<AbBillRuleBillDtlRmkDtlVO> getByByBusId(String id) {
        return abBillRuleBillDtlRmkDtlService.getByByBusId(id);
    }

    /**
    * @Description 删除规则
    * @author zhous
    * @date 2025/2/25 8:47
    */
    public void deleteRuleBillDtlRmk(String id) {
        this.delete(id);
        //删除规则明细
        abBillRuleBillDtlRmkDtlService.deleteByBusId(id);
    }

    /**
    * @Description 禁用
    * @author zhous
    * @date 2025/2/25 8:47
    */
    public void disable(String ids, ICurrentUser user) {
        abBillRuleBillDtlRmkDao.disable(ids,user);
    }

    /**
    * @Description 根据银行类型和业务类型获取对应的规则明细
    * @author zhous
    * @date 2025/2/25 10:18
    */
    public List<AbBillRuleBillDtlRmkDtlVO> getRulesByBankTypeIdAndBusType(String bankTypeId, String busType) {
        return abBillRuleBillDtlRmkDao.getRulesByBankTypeIdAndBusType(bankTypeId,busType);
    }

    /**
    * @Description 获取有效的票据流水备注规则
    * @author zhous
    * @date 2025/2/25 14:48
    */
    public List<AbBillRuleBillDtlRmkVO> listValidInfo() {
        return abBillRuleBillDtlRmkDao.listValidInfo();
    }
}
