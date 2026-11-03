package com.bytter.biz.dataexchange.basic.service;

import com.bytter.biz.dataexchange.basic.entity.DSLInterfaceLog;
import com.bytter.biz.dataexchange.basic.vo.DSLInterfaceLogVo;
import com.bytter.biz.dataexchange.basic.dao.DSLInterfaceLogDao2;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IDictService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.system.api.ITaskService;

import java.util.*;

/**
 * @author zoumy
 * @version 1.0
 * @Title: Service
 * @Description: erp接口日志维护
 * @date 2020-02-28 11:14:22
 */
@Service("dslInterfaceLogService2")
@Transactional
public class DSLInterfaceLogService2 extends BaseService<DSLInterfaceLog, String> implements ITaskService  , DictInjectProvider {
    @Autowired
    private DSLInterfaceLogDao2 logDao;
    @Override
    public BaseDao<DSLInterfaceLog, String> getEntityDao() {
        return logDao;
    }
    @Autowired
    private IDictService dictService;

    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        String eqs_type = (String)input.getFormdata().get("eqs_type");
        if("2".equals(eqs_type)){
            input.getFormdata().remove("eqs_type");
        }
        return logDao.getDataTablesUnion(input);
    }

    public DSLInterfaceLogVo getErpInterfaceLogVo(String id) {
        List<DSLInterfaceLogVo> vos = new ArrayList<DSLInterfaceLogVo>();
        DSLInterfaceLogVo vo = new DSLInterfaceLogVo();
        DSLInterfaceLog t = logDao.get(id);
        BeanUtils.copyProperties(t,vo);
        return vo;
    }

    @Override
    @XxlJob("InterfaceLogClearService")
    public void run(String arguments) {
        logger.info("开始清理ERP接口日志数据");
        try {
            Date threeMonthsAgo = DateUtils.addMonth(DateUtils.getDateTime(), -3);
            String threeMonthsAgoStr = DateUtils.format(threeMonthsAgo,DateUtils.PATTERN_CLASSICAL);
            logger.info("将删除"+threeMonthsAgoStr+"之前的日志数据");
            int i = logDao.deletehreeMonthsAgoLogs(threeMonthsAgo);
            logger.info("本次一个删除"+i+"条日志数据");
            logger.info("清理ERP接口日志数据完成");
        }catch (Exception e){
            logger.error("清理ERP接口日志数据异常",e);
        }
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
