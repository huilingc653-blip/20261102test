package com.bytter.biz.report.service;

import com.bytter.biz.budget.dao.FbsCollectCorpDao;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsCollectCorpVO;
import com.bytter.biz.report.dao.FbsBudgetSuperAddReportDao;
import com.bytter.biz.report.vo.FbsBudgetSuperAddReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
/**
 * 资金计划追加查询
 * add by qijh
 */
@Service("fbsBudgetSuperAddReportService")
@Transactional
public class FbsBudgetSuperAddReportService extends BaseService<FbsBudgetSuperadd, String> {
    @Autowired
    private FbsBudgetSuperAddReportDao fbsBudgetSuperAddReportDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @Autowired
    private FbsCollectCorpDao fbsCollectCorpDao;
    @DubboReference(check = false)
    private IDictService dictService;



    /**
     * 计划明细列表数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput, ICurrentUser user) {

        //获取计划执行周期类型
        StringBuilder sql = new StringBuilder();
        String timeType = dataTablesInput.getFormdata().get("eqs_timeType").toString();
        Object beginDateObj = dataTablesInput.getFormdata().get("ged_beginDate") ;
        Object endDateObj = dataTablesInput.getFormdata().get("led_endDate") ;
        if(!EmptyUtils.isEmpty(beginDateObj)&&EmptyUtils.isNotEmpty(beginDateObj.toString())){
            String beginDate = beginDateObj.toString();
            FbsBudgetPeriodVO fbsBudgetPeriodVOBegin = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.parse(beginDate,"yyyy-MM-dd"),timeType);
            dataTablesInput.getFormdata().put("ged_beginDate",fbsBudgetPeriodVOBegin.getBeginDateStr());
        }
        if(!EmptyUtils.isEmpty(endDateObj)&&EmptyUtils.isNotEmpty(endDateObj.toString())){
            String endDate =  endDateObj.toString();
            FbsBudgetPeriodVO fbsBudgetPeriodVOEnd = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.parse(endDate,"yyyy-MM-dd"),timeType);
            dataTablesInput.getFormdata().put("led_endDate",fbsBudgetPeriodVOEnd.getEndDateStr());
        }
        String ids = "";
        if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_corpId"))){
            String corpIds = dataTablesInput.getFormdata().get("ins_corpId").toString();
            if(EmptyUtils.isNotEmpty(corpIds)) {
                ids = this.fbsCollectCorpDao.getIdByCorpId(corpIds.split(","));
                corpIds = corpIds+","+ids;
                dataTablesInput.getFormdata().put("ins_corpId",corpIds);
            }
        }
        String resource = ResourceConstant.FBS_REPORT_SUPERADDFBSBUDGET_QUERY;
        List<String> corpIdList = corpService.getUserCorpIds(resource, user.getId());
        if(EmptyUtils.isNotEmpty(ids)){
            String[] idStr = ids.split(",");
            for(int i = 0;i<idStr.length;i++){
                corpIdList.add(idStr[i]);
            }
        }
        String corpAuthSql = SqlUtil.getInSql(corpIdList, "f.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput output = this.fbsBudgetSuperAddReportDao.dataTable(dataTablesInput, sql.toString());
        if(EmptyUtils.isNotEmpty(output.getData())){
           List<FbsBudgetSuperAddReportVO> list =  output.getData();
            //计划周期类型
            Map<String, DictDTO> itemTypeMap = dictService.listMap(BudgetConstant.BUDGET_TIMETYPE_DICT);
            //状态
            Map<String, DictDTO> statusMap = dictService.listMap(BudgetConstant.BUDGET_STATUS_DICT);
            for(FbsBudgetSuperAddReportVO fbsBudgetSuperAddReportVO : list){
               String corpId = fbsBudgetSuperAddReportVO.getCorpId();
               if(EmptyUtils.isEmpty(corpId)){
                   fbsBudgetSuperAddReportVO.setCorpName(fbsBudgetSuperAddReportVO.getCorpName()+"(汇总)");
               }
               //计划周期类型
               fbsBudgetSuperAddReportVO.setStatusStr(statusMap.get(fbsBudgetSuperAddReportVO.getStatus().toString()).getName());
               //计划周期类型
                fbsBudgetSuperAddReportVO.setTimeTypeName(itemTypeMap.get(fbsBudgetSuperAddReportVO.getTimeType()).getName());
           }
        }
        return output;
    }
    @Override
    public BaseDao<FbsBudgetSuperadd, String> getEntityDao() {
        return null;
    }
    /**
     * 获取汇总单位虚拟本单位
     * @param id
     * @return
     */
    public FbsCollectCorpVO listCorpById(String id) {
        return this.fbsCollectCorpDao.listCorpById(id);
    }
}
