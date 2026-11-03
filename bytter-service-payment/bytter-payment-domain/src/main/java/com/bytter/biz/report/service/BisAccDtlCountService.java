package com.bytter.biz.report.service;

import com.bytter.biz.report.dao.BisAccDtlCountDao;
import com.bytter.biz.report.vo.BisAccDtlCountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BisAccDtlCountService extends BaseService {
    @Autowired
    private BisAccDtlCountDao bisAccDtlCountDao;
    @Autowired
    private ICorpService corpService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    public DataTablesOutput datatableData(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds("report:bisaccdtl:count:query", user.getId());
        String authSql = " and " + SqlUtil.getInSql(corpIds,"sc.id",true);
        String beginDate = dataTablesInput.getFormdata().get("beginDate").toString();
        String endDate = dataTablesInput.getFormdata().get("endDate").toString();
        DataTablesOutput output = bisAccDtlCountDao.listData(dataTablesInput, authSql);
        if(!EmptyUtils.isEmpty(output.getData())){
            List<BisAccDtlCountVO> data = output.getData();
            for(BisAccDtlCountVO vo : data){
                vo.setBeginDate(DateUtils.parse(beginDate,"yyyy-MM-dd"));
                vo.setEndDate(DateUtils.parse(endDate,"yyyy-MM-dd"));
                if(CommonConstant.YES.equals(vo.getIsOnline())){
                    vo.setIsOnlineName("是");
                }else{
                    vo.setIsOnlineName("否");
                }
            }
        }
        return output;
    }
}
