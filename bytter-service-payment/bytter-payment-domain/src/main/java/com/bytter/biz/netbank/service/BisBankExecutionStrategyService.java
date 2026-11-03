package com.bytter.biz.netbank.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.dao.BisBankExecutionStrategyDao;
import com.bytter.biz.netbank.dao.NisBusTypeRuleDao;
import com.bytter.biz.netbank.entity.BisBankExecutionStrategy;
import com.bytter.biz.netbank.entity.NisBusTypeRule;
import com.bytter.biz.netbank.vo.BisBankExecutionStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
/**
 * 三单关联执行策略
 */
@Service("bisBankExecutionStrategyService")
@Transactional
public class BisBankExecutionStrategyService extends BaseService<BisBankExecutionStrategy, String> {
    @Autowired
    private BisBankExecutionStrategyDao bisBankExecutionStrategyDao;
    @DubboReference(check = false)
	private IDictService dictService;
//    @DubboReference(check = false)
//	private IMultiLangService iMultiLangService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Autowired(required = false)
    private NisBusTypeRuleDao nisBusTypeRuleDao;

    @Override
    public BaseDao<BisBankExecutionStrategy, String> getEntityDao() {
        return bisBankExecutionStrategyDao;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankExecutionStrategyVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        String rules = (String) dataTablesInput.getFormdata().get("likes_rulesName");
        if (!rules.isEmpty() && multiLangServiceI.getLang("table.bisBankExecution.unique").contains(rules)) {
            dataTablesInput.getFormdata().put("likes_rulesName", "");
            dataTablesInput.getFormdata().put("eqs_isExistId", "1");
        }
        //银行类型名称  (任务类型名称  启用标志名称  前端标签设值)  设值
        DataTablesOutput<BisBankExecutionStrategyVO> dataTablesOutput = bisBankExecutionStrategyDao.getDataTablesUnion(dataTablesInput);
        List<BisBankExecutionStrategyVO> bisBankExecutionStrategyVOList = dataTablesOutput.getData();
        for(BisBankExecutionStrategyVO bisBankExecutionStrategyVO:bisBankExecutionStrategyVOList){
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bisBankExecutionStrategyVO.getBankTypeId());
            if(!EmptyUtils.isEmpty(bankTypeDTO)){
                bisBankExecutionStrategyVO.setBankTypeName(bankTypeDTO.getTypeName());
            }
        }
        return dataTablesOutput;
    }
    /**
     * 新增
     *
     * @param entity
     * @return
     */
    public void saveBisBankExecutionStrategy(BisBankExecutionStrategy entity, ICurrentUser user) {
        List<String> ids = new ArrayList<>();
        if (CommonConstant.NO.equals(entity.getIsExistId()) && EmptyUtils.isEmpty(entity.getRules())) {
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS_ERROR);
        }
        for (String bankTypeId : entity.getBankTypeId().split(CommonConstant.COMMA_SPLIT) ){
            BisBankExecutionStrategy bankExecutionStrategy = new BisBankExecutionStrategy();
            String[] rules = entity.getRules().split(CommonConstant.COMMA_SPLIT);
            StringBuilder rulesName = new StringBuilder();
            for (int i = 0; i < rules.length; i++) {
                if (StringUtils.isNotEmpty(rulesName)) {
                    rulesName.append("+");
                }
                rulesName.append(dictService.get("btPurpose.receiptRules", rules[i]));
            }
            bankExecutionStrategy.setRulesName(entity.getRulesName());
            bankExecutionStrategy.setRules(entity.getRules());
            bankExecutionStrategy.setType(entity.getType());
            if ("2".equals(entity.getType())){
                bankExecutionStrategy.setIsExistId("0");
            }else{
                bankExecutionStrategy.setIsExistId(entity.getIsExistId());
            }
            bankExecutionStrategy.setIsEnable(entity.getIsEnable());
            bankExecutionStrategy.setRulesName(rulesName.toString());
            bankExecutionStrategy.setCreateBy(user.getLoginName());
            bankExecutionStrategy.setCreateDate(DateUtils.getDate());
            bankExecutionStrategy.setExecuteSort(entity.getExecuteSort());
            if(entity.getBillOnlyId()!=null){
                bankExecutionStrategy.setBillOnlyId(entity.getBillOnlyId());
            }
            if(entity.getDtlOnlyId()!=null){
                bankExecutionStrategy.setDtlOnlyId(entity.getDtlOnlyId());
            }
            if(entity.getReceiptOnlyId()!=null){
                bankExecutionStrategy.setReceiptOnlyId(entity.getReceiptOnlyId());
            }
            bankExecutionStrategy.setBankTypeId(bankTypeId);
            bankExecutionStrategy.setTransDays(entity.getTransDays());
            super.saveEntity(bankExecutionStrategy);
            ids.add(bankExecutionStrategy.getId());
        }

        // 保存配置规则
        String[] matchRule = entity.getMatchRule();
        String[] matchElements = entity.getMatchElements();
        String[] matchRelations = entity.getMatchRelations();
        String[] matchContent = entity.getMatchContent();
        String[] leftParenthesis = entity.getLeftParenthesis();
        String[] rightParenthesis = entity.getRightParenthesis();
        List<NisBusTypeRule> nisBusTypeRuleList = new ArrayList<>();
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;
        if (ObjectUtil.isNotEmpty(matchElements)){
            for(int i=0;i<matchElements.length;i++){
                if(StringUtils.isNotEmpty(matchElements[i].trim())){
                    NisBusTypeRule  nisBusTypeRule = new NisBusTypeRule();
                    nisBusTypeRule.setMatchRule(matchRule[i]);
                    nisBusTypeRule.setMatchElements(matchElements[i]);
                    if(!EmptyUtils.isEmpty(matchRelations) && !EmptyUtils.isEmpty(matchRelations[i])) {
                        nisBusTypeRule.setMatchRelations(matchRelations[i]);
                    }
                    nisBusTypeRule.setMatchContent(matchContent[i]);
                    nisBusTypeRule.setSort(i);
                    nisBusTypeRule.setBusId(entity.getId());
                    if(!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length>i) {
                        nisBusTypeRule.setLeftParenthesis(leftParenthesis[i]);
                        if("(".equals(leftParenthesis[i])){
                            leftCount++;
                            leftSort +=i;
                        }
                    }
                    if(!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length>i) {
                        nisBusTypeRule.setRightParenthesis(rightParenthesis[i]);
                        if(")".equals(rightParenthesis[i])){
                            rightCount++;
                            rightSort +=i;
                        }
                    }
                    nisBusTypeRuleList.add(nisBusTypeRule);
                }
            }
        }
        if(leftCount != rightCount || leftSort>rightSort){
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
        }
        if (ObjectUtil.isNotEmpty(nisBusTypeRuleList)){
            for (String busId : ids){
                List<NisBusTypeRule> temp = new ArrayList<>();
                for (NisBusTypeRule rule : nisBusTypeRuleList){
                    temp.add(copyRule(rule, busId));
                }
                nisBusTypeRuleDao.batchSave(temp);
            }
        }
    }
    /**
     * 每个银行复制一份规则并赋新主键，避免复用同一实体导致主键冲突
     */
    private NisBusTypeRule copyRule(NisBusTypeRule source, String busId) {
        NisBusTypeRule copy = new NisBusTypeRule();
        copy.setId(IdUtil.simpleUUID());
        copy.setBusId(busId);
        copy.setMatchRule(source.getMatchRule());
        copy.setMatchElements(source.getMatchElements());
        copy.setMatchRelations(source.getMatchRelations());
        copy.setMatchContent(source.getMatchContent());
        copy.setSort(source.getSort());
        copy.setLeftParenthesis(source.getLeftParenthesis());
        copy.setRightParenthesis(source.getRightParenthesis());
        return copy;
    }

    /**
     * 根据id查询数据集
     *
     * @param id
     * @return
     */
    public BisBankExecutionStrategy queryBisBankExecutionStrategy(String id) {
        return super.get(id,true);
    }
    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    public BisBankExecutionStrategyVO getById(String id) {
        return bisBankExecutionStrategyDao.getById(id);
    }
    /**
     * 修改
     *
     * @param entity
     * @return
     */
    public void modify(BisBankExecutionStrategy entity) {
        super.updateEntity(entity);
        if (!"updateIsEnable".equals(entity.getOpType())){
            // 先删除
            nisBusTypeRuleDao.deleteByBusId(entity.getId());
            String[] matchRule = entity.getMatchRule();
            String[] matchElements = entity.getMatchElements();
            String[] matchRelations = entity.getMatchRelations();
            String[] matchContent = entity.getMatchContent();
            String[] leftParenthesis = entity.getLeftParenthesis();
            String[] rightParenthesis = entity.getRightParenthesis();
            List<NisBusTypeRule> nisBusTypeRuleList = new ArrayList<>();
            int leftCount = 0;
            int leftSort = 0;
            int rightCount = 0;
            int rightSort = 0;
            if(ObjectUtil.isNotEmpty(matchElements)){
                for(int i=0;i<matchElements.length;i++){
                    if(StringUtils.isNotEmpty(matchElements[i].trim())){
                        NisBusTypeRule  nisBusTypeRule = new NisBusTypeRule();
                        nisBusTypeRule.setMatchRule(matchRule[i]);
                        nisBusTypeRule.setMatchElements(matchElements[i]);
                        nisBusTypeRule.setMatchRelations(matchRelations[i]);
                        nisBusTypeRule.setMatchContent(matchContent[i]);
                        nisBusTypeRule.setSort(i);
                        nisBusTypeRule.setBusId(entity.getId());
                        if(!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length>i) {
                            nisBusTypeRule.setLeftParenthesis(leftParenthesis[i]);
                            if("(".equals(leftParenthesis[i])){
                                leftCount++;
                                leftSort +=i;
                            }
                        }
                        if(!EmptyUtils.isEmpty(rightParenthesis)  && rightParenthesis.length>i) {
                            nisBusTypeRule.setRightParenthesis(rightParenthesis[i]);
                            if(")".equals(rightParenthesis[i])){
                                rightCount++;
                                rightSort +=i;
                            }
                        }
                        nisBusTypeRuleList.add(nisBusTypeRule);
                    }
                }
            }
            if (ObjectUtil.isNotEmpty(nisBusTypeRuleList)){
                if(leftCount != rightCount || leftSort>rightSort){
                    throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
                }
                nisBusTypeRuleDao.batchSave(nisBusTypeRuleList);
            }
        }
    }
    /**
     * 校验优先执行排序
     *
     * @param type
     * @return
     */
    public boolean CheckxecuteSort(String type,String executeSort,String bankTypeId,String rules,String isExistId) {
        return bisBankExecutionStrategyDao.CheckxecuteSort(type,executeSort,bankTypeId,rules,isExistId);
    }
    /**
     * 校验优先执行排序(修改)
     *
     * @param type
     * @return
     */
    public boolean checkExecuteUpdateSort(String type,String executeSort,String id,String bankTypeId,String rules,String isExistId) {
        return bisBankExecutionStrategyDao.checkExecuteUpdateSort(type,executeSort,id,bankTypeId,rules,isExistId);
    }

    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delete(id);
        }
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankExecutionStrategyVO> getDataTablesUnionRefund(DataTablesInput dataTablesInput) {
        String rules = (String) dataTablesInput.getFormdata().get("likes_rulesName");
        if (!rules.isEmpty() && multiLangServiceI.getLang("table.bisBankExecution.unique").contains(rules)) {
            dataTablesInput.getFormdata().put("likes_rulesName", "");
            dataTablesInput.getFormdata().put("eqs_isExistId", "1");
        }
        return bisBankExecutionStrategyDao.getDataTablesUnionRefund(dataTablesInput);
    }

}
