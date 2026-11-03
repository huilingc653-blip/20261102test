package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.EvaluationLogDao;
import com.bytter.biz.creditRating.entity.EvaluationLog;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.vo.EvaluationLogVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 信用评估操作日志
 * @author chenqin
 * @date 2024-08-28
 */
@Service("evaluationLogService")
@Transactional
public class EvaluationLogService extends BaseService<EvaluationLog, String> {

    @Autowired
    private EvaluationLogDao evaluationLogDao;

    @Resource
    private IMultiLangService multiLangServiceI;

    @Override
    public BaseDao<EvaluationLog, String> getEntityDao() {
        return evaluationLogDao;
    }

    /**
     * 获取信用评估操作日志列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput outPut = evaluationLogDao.getDataTablesUnion(dataTablesInput);
        List<EvaluationLogVO> data = outPut.getData();
        if (EmptyUtils.isEmptyList(data)) {
            return outPut;
        }
        List<String> langCodeList = data.stream().filter(dto -> StringUtils.isNotBlank(dto.getMsg())).map(EvaluationLogVO::getMsg).distinct().collect(Collectors.toList());
        Map<String, String> langMap = multiLangServiceI.getLangList(LocaleContextHolder.getLocale(), langCodeList);
        for (EvaluationLogVO vo : data) {
            if (StringUtils.isBlank(vo.getMsg())) {
                continue;
            }
            if (langMap.containsKey(vo.getMsg())) {
                vo.setMsg(langMap.get(vo.getMsg()));
            }
        }
        return outPut;
    }

    /**
     * 新增操作日志记录
     */
    public void saveEvaluationLog(String quoteId, String logType, String msg, ICurrentUser user){
        if (StringUtils.isBlank(quoteId)){
            throw new BusinessException(CreditRatingEnum.EVALUATION_LOG_QUOTEID_NOT_NULL);
        }
        EvaluationLog log = new EvaluationLog();
        log.setQuoteId(quoteId);
        log.setLogType(logType);
        log.setMsg(msg);
        log.setCreateDate(new Date());
        log.setCreateBy(user.getLoginName());
        log.setCreateName(user.getUsername());
        this.saveOrUpdate(log);
    }

}
