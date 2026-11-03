package com.bytter.report.domain.cms.service.impl;
import com.bytter.report.api.dto.BiRemainTermDTO;
import com.bytter.report.api.service.IBiRemainTermService;
import com.bytter.report.domain.cms.service.BiRemainTermService;
import com.bytter.report.domain.cms.vo.BiRemainTermDetailVO;
import com.bytter.report.domain.cms.vo.BiRemainTermVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.report.api.constant.RemainTermConstant;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @ClassName BiRemainTermServiceImpl
 * @Description 剩余期限维护接口实现类
 * @Author fusb
 * @Date 2020/1/15 18:58
 * @Version 1.0
 **/
@Service("biReportRemainTermServiceImpl")
@Transactional
@DubboService
public class BiReportRemainTermServiceImpl implements IBiRemainTermService {
    @Autowired
    BiRemainTermService biRemainTermService;
    
    @Override
    public List<BiRemainTermDTO> getBiRemainTermDTOs(String deadLine) {
        return this.getBiRemainDTOs(deadLine);
    }
    
    @Override
    public List<BiRemainTermDTO> getBiRemainTermDTOList() {
        List<BiRemainTermVO> list = biRemainTermService.getRemainTermList();
        List<BiRemainTermDTO> dtoList = new ArrayList<BiRemainTermDTO>();
        BiRemainTermDTO dto = null;
        for (BiRemainTermVO vo:list) {
            dto = new BiRemainTermDTO();
            dto.setId(vo.getId());
            dto.setTermName(vo.getTermName());
            dtoList.add(dto);
        }
        return dtoList;
    }
    /**
    *@Description 根据业务到期日期，返回带期限查询条件的剩余期限维护DTO
    *@Param deadLine
    *@return java.util.List<com.bytter.bi.spi.dto.BiRemainTermDTO>
    *@Date 2020/1/15 19:36
    *@Author fusb
    */
    public List<BiRemainTermDTO> getBiRemainDTOs(String deadLine){
        List<BiRemainTermVO> termVOList = biRemainTermService.getRemainTermList();
        List<BiRemainTermDTO> listDTO = new ArrayList<BiRemainTermDTO>();
        for (BiRemainTermVO termVO:termVOList) {
            StringBuilder searchSql = new StringBuilder();
            BiRemainTermDTO dto = null;
            Date maxDate = null;
            Date tempDate = null;
            for (BiRemainTermDetailVO dtl:termVO.getRemainTermDtl()) {
                tempDate = this.tempDate(dtl);
                switch (dtl.getConditions()) {
                    case RemainTermConstant.CONDITION_GREATER:
                        searchSql.append(deadLine).append(">").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    case RemainTermConstant.CONDITION_LESS:
                        searchSql.append(deadLine).append("<").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    case RemainTermConstant.CONDITION_EQUAL:
                        searchSql.append(deadLine).append("=").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    case RemainTermConstant.CONDITION_LESS_EQUAL:
                        searchSql.append(deadLine).append("<=").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    case RemainTermConstant.CONDITION_GREATER_EQUAL:
                        searchSql.append(deadLine).append(">=").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    case RemainTermConstant.CONDITION_NOT_EQUAL:
                        searchSql.append(deadLine).append("<>").append("bt_to_date('").append(DateUtils.formatDate(tempDate)).append("','yyyy-MM-dd') ");break;
                    default:
                }
                if (null==maxDate) {
                    maxDate = tempDate;
                } else {
                    if (maxDate.before(tempDate)) {
                        maxDate = tempDate;
                    }
                }
                if (termVO.getRemainTermDtl().size() > 1) { // 拼接逻辑关系
                    if (RemainTermConstant.RELATION_OR.equals(dtl.getLogicalRelationship())) { // 或者
                        searchSql.append(" or ");
                    }
                    if (RemainTermConstant.RELATION_AND.equals(dtl.getLogicalRelationship())) { // 并且
                        searchSql.append(" and ");
                    }
                }
            }
            dto = new BiRemainTermDTO();
            dto.setId(termVO.getId());
            dto.setTermName(termVO.getTermName());
            dto.setSearchSql(searchSql.toString());
            dto.setMaxTermDate(maxDate);
            listDTO.add(dto);
        }
        return listDTO;
    }
    /**
    *@Description 获取剩余期限的查询日期
    *@Param dtl
    *@return java.util.Date
    *@Date 2020/1/15 19:29
    *@Author fusb
    */
    public Date tempDate(BiRemainTermDetailVO dtl){
        Date tempDate = null;
        Date systemDate = DateUtils.getDate();
        if (RemainTermConstant.REMAIN_TERM_DAY.equals(dtl.getUnit())) { // 天
            tempDate = DateUtils.addDay(systemDate,dtl.getDays());
        }
        if (RemainTermConstant.REMAIN_TERM_MONTH.equals(dtl.getUnit())) { // 月
            tempDate = DateUtils.addMonth(systemDate,dtl.getDays());
        }
        if (RemainTermConstant.REMAIN_TERM_YEAR.equals(dtl.getUnit())) { // 年
            tempDate = DateUtils.addYear(systemDate,dtl.getDays());
        }
        return tempDate;
    }
}
