package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.inneraccount.dao.BisCorpPoolConfigDao;
import com.bytter.biz.inneraccount.dao.BisCorpPoolConfigRuleDao;
import com.bytter.biz.inneraccount.entity.BisCorpPoolConfig;
import com.bytter.biz.inneraccount.entity.BisCorpPoolConfigRule;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 多级单位余额核算-单位归集配置表
 * @date 2025年6月20日11:03:04
 */
@Service("bisCorpPoolConfigService")
@Transactional
public class BisCorpPoolConfigService extends BaseService<BisCorpPoolConfig, String> {
    @Autowired
    private BisCorpPoolConfigDao bisCorpPoolConfigDao;

    @Autowired
    private BisCorpPoolConfigRuleDao bisCorpPoolConfigRuleDao;


    @Override
    public BaseDao<BisCorpPoolConfig, String> getEntityDao() {
        return bisCorpPoolConfigDao;
    }

    /**
     * 跳转 单位归集余额维护 列表
     *
     * @return
     */
    @LogMessage(content = "查询单位归集配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisCorpPoolConfig";
    }

    /**
     * 查询 单位归集余额表 列表数据
     *
     * @param input
     * @param user
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        return bisCorpPoolConfigDao.getDataTablesUnion(input);
    }

    /**
     * 单位归集配置保存
     *
     * @param entity
     * @param request
     */
    public void save(BisCorpPoolConfig entity, HttpServletRequest request) {
        super.saveEntity(entity);
        String[] matchRule = request.getParameterValues("matchRule");
        String[] matchElements = request.getParameterValues("matchElements");
        String[] matchRelations = request.getParameterValues("matchRelations");
        String[] matchContent = request.getParameterValues("matchContent");
        String[] leftParenthesis = request.getParameterValues("leftParenthesis");
        String[] rightParenthesis = request.getParameterValues("rightParenthesis");
        List<BisCorpPoolConfigRule> bisCorpPoolConfigRuleList = new ArrayList<>();
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;
        for (int i = 0; i < matchElements.length; i++) {
            if (StringUtils.isNotEmpty(matchElements[i].trim())) {
                BisCorpPoolConfigRule bisCorpPoolConfigRule = new BisCorpPoolConfigRule();
                bisCorpPoolConfigRule.setMatchRule(matchRule[i]);
                bisCorpPoolConfigRule.setMatchElements(matchElements[i]);
                if (!EmptyUtils.isEmpty(matchRelations) && !EmptyUtils.isEmpty(matchRelations[i])) {
                    bisCorpPoolConfigRule.setMatchRelations(matchRelations[i]);
                }
                bisCorpPoolConfigRule.setMatchContent(matchContent[i]);
                bisCorpPoolConfigRule.setSort(i);
                bisCorpPoolConfigRule.setConfigId(entity.getId());
                if (!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length > i) {
                    bisCorpPoolConfigRule.setLeftParenthesis(leftParenthesis[i]);
                    if ("(".equals(leftParenthesis[i])) {
                        leftCount++;
                        leftSort += i;
                    }
                }
                if (!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length > i) {
                    bisCorpPoolConfigRule.setRightParenthesis(rightParenthesis[i]);
                    if (")".equals(rightParenthesis[i])) {
                        rightCount++;
                        rightSort += i;
                    }
                }
                bisCorpPoolConfigRuleList.add(bisCorpPoolConfigRule);
            }
        }
        if (leftCount != rightCount || leftSort > rightSort) {
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
        }
        bisCorpPoolConfigRuleDao.batchSave(bisCorpPoolConfigRuleList);
    }

    /**
     * 产生规则编码
     *
     * @return
     */
    public String getConfigRuleCode() {
        return SequenceSingleton.getSequence(SequenceEnum.BIS_CORP_POOL_CONFIG_NO);
    }

    /**
     * 单位归集配置更新 or 保存
     *
     * @param entity
     * @param request
     */
    public void saveOrUpdate(BisCorpPoolConfig entity, HttpServletRequest request) {
        super.saveOrUpdate(entity);
        int leftCount = 0;
        int leftSort = 0;
        int rightCount = 0;
        int rightSort = 0;

        String[] matchRule = request.getParameterValues("matchRule");
        String[] matchElements = request.getParameterValues("matchElements");
        String[] matchRelations = request.getParameterValues("matchRelations");
        String[] matchContent = request.getParameterValues("matchContent");
        String[] leftParenthesis = request.getParameterValues("leftParenthesis");
        String[] rightParenthesis = request.getParameterValues("rightParenthesis");
        List<BisCorpPoolConfigRule> bisCorpPoolConfigRuleList = new ArrayList<>();
        for (int i = 0; i < matchElements.length; i++) {
            if (StringUtils.isNotEmpty(matchElements[i].trim())) {
                BisCorpPoolConfigRule bisCorpPoolConfigRule = new BisCorpPoolConfigRule();
                bisCorpPoolConfigRule.setMatchRule(matchRule[i]);
                bisCorpPoolConfigRule.setMatchElements(matchElements[i]);
                bisCorpPoolConfigRule.setMatchRelations(matchRelations[i]);
                bisCorpPoolConfigRule.setMatchContent(matchContent[i]);
                bisCorpPoolConfigRule.setSort(i);
                bisCorpPoolConfigRule.setConfigId(entity.getId());
                if (!EmptyUtils.isEmpty(leftParenthesis) && leftParenthesis.length > i) {
                    bisCorpPoolConfigRule.setLeftParenthesis(leftParenthesis[i]);
                    if ("(".equals(leftParenthesis[i])) {
                        leftCount++;
                        leftSort += i;
                    }
                }
                if (!EmptyUtils.isEmpty(rightParenthesis) && rightParenthesis.length > i) {
                    bisCorpPoolConfigRule.setRightParenthesis(rightParenthesis[i]);
                    if (")".equals(rightParenthesis[i])) {
                        rightCount++;
                        rightSort += i;
                    }
                }
                bisCorpPoolConfigRuleList.add(bisCorpPoolConfigRule);
            }
        }
        if (leftCount != rightCount || leftSort > rightSort) {
            throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
        }
        //先删除规则再插入
        bisCorpPoolConfigRuleDao.deleteByConfigId(entity.getId());
        bisCorpPoolConfigRuleDao.batchSave(bisCorpPoolConfigRuleList);
    }

    /**
     * 根据业务id获取所有业务规则
     *
     * @param configId
     * @return
     */
    public List<BisCorpPoolConfigRuleVO> getConfigRuleByConfigId(String configId) {
        return bisCorpPoolConfigRuleDao.getConfigRuleByConfigId(configId);
    }

    /**
     * 删除单位归集配置表
     *
     * @param entity
     */
    public void deleteBisCorpPoolConfig(BisCorpPoolConfig entity) {
        this.deleteEntity(entity);
        bisCorpPoolConfigRuleDao.deleteByConfigId(entity.getId());
    }

    /**
     * 批量删除单位归集配置表
     *
     * @param ids
     */
    public void batchDeleteConfig(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisCorpPoolConfig entity = this.get(id);
            this.deleteEntity(entity);
            bisCorpPoolConfigRuleDao.deleteByConfigId(entity.getId());
        }
    }

    /**
     * 批量禁用单位归集配置表
     *
     * @param ids
     */
    public void batchDisableConfig(String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisCorpPoolConfig entity = this.get(id);
            entity.setValidSign("0");
            bisCorpPoolConfigDao.saveOrUpdate(entity);
        }
    }
}
