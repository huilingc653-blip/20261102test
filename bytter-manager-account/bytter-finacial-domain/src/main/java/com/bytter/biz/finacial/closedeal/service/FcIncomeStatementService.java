package com.bytter.biz.finacial.closedeal.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.closedeal.dao.FcIncomeStatementDao;
import com.bytter.biz.finacial.closedeal.entity.FcIncomeStatement;
import com.bytter.biz.finacial.closedeal.vo.FcReportFormatBean;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_INCOME_STATEMENT
 * @date 2017-07-17 18:37:10
 */
@Service("fcIncomeStatementService")
@Transactional
public class FcIncomeStatementService extends FcMonthCloseService<FcIncomeStatement, String> {
    @Autowired
    private FcIncomeStatementDao fcIncomeStatementDao;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcIncomeStatement entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcIncomeStatement entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcIncomeStatement entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcIncomeStatement entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<FcIncomeStatement, String> getEntityDao() {
        return fcIncomeStatementDao;
    }
    @Override
    public List<FcReportFormatBean> getNetReportFormat(FcMonthCloseDTO closeDTO, String curCode, String reportType) {
        FcAccDTO fcAccDto = new FcAccDTO();
        fcAccDto.setCurId(curCode);
        fcAccDto.setNetId(closeDTO.getNetCode());
        fcAccDto.setSubjectSort("I");
        List<FcAccDTO> fcList = fcAccService.listByCondition(fcAccDto);
        if (CollectionUtils.isEmpty(fcList)) {
            return new ArrayList<>();
        }
        Map<String, FcAccDTO> subjectMap = fcList.stream().collect(Collectors.toMap(FcAccDTO::getSubjectId, data -> data, (pre, next) -> next));
        List<FcReportFormatBean> reportFormatList = fcIncomeStatementDao.getNetReportFormat(closeDTO, curCode, FinacialConstant.REPORT_TYPE_2, fcList);
        reportFormatList.forEach(item -> {
            if (StringUtils.isNotBlank(item.getSubject()) && subjectMap.containsKey(item.getSubject())) {
                item.setBalance(item.getBalance().multiply(new BigDecimal(subjectMap.get(item.getSubject()).getBalanceWay())));
            }
        });
        return reportFormatList;
    }
    @Override
    public void updateInitYearBalance(FcMonthCloseDTO closeDTO, Integer lastYear) {
        fcIncomeStatementDao.updateInitYearBalance(closeDTO, lastYear);
    }
    /**
     * 功能介绍 : 生成损益明细。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public void processFormatBean(List<FcReportFormatBean> transBeans, FcMonthCloseDTO closeDTO, String curCode, Boolean initFlag) {
        for (FcReportFormatBean bean : transBeans) {
            FcIncomeStatement statement = new FcIncomeStatement();
            statement.setYearMonth2(closeDTO.getYearMonth2());
            statement.setNetCode(bean.getNetCode());
            statement.setCurCode(curCode);
            //损益数据
            statement.setItem(bean.getItem());
            statement.setOrderNo(bean.getOrderNo());
            statement.setBalance(bean.getBalance());
//			if(StringUtils.isNotEmpty(bean.getBalance())&&StringUtils.isNotEmpty(bean.getYearBalance())){
//			}
            statement.setYearTotal(bean.getYearBalance());
            saveEntity(statement);
        }
    }
    /**
     * 功能介绍 : 转移资产损益数据。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseIncomeStatement(FcMonthCloseDTO closeDTO) {
        fcIncomeStatementDao.deleteIncomStatement(closeDTO);
    }
    /**
     * 功能介绍 : 查询损益类账户的本年累计。<br>
     * <b>日期：</b>2020年6月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Override
    public List<FcInitSubjectDTO> getTotalSubjectBal(FcMonthCloseDTO closeDTO, Date startDate) {
        FcAccDTO fcAccDto = new FcAccDTO();
        fcAccDto.setNetId(closeDTO.getNetCode());
        fcAccDto.setSubjectSort("I");
        List<FcAccDTO> fcAccList = fcAccService.listByCondition(fcAccDto);
        if (CollectionUtil.isEmpty(fcAccList)) {
            return new ArrayList<>();
        }
        Map<String, FcAccDTO> fcAccDTOMap = fcAccList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, item -> item, (pre, next) -> next));
        List<FcInitSubjectDTO> totalSubjectBal = fcIncomeStatementDao.getTotalSubjectBal(closeDTO, startDate, FinacialConstant.REPORT_TYPE_2, fcAccList);
        return totalSubjectBal.stream().map(item -> {
            FcInitSubjectDTO fcInitSubjectDTO = new FcInitSubjectDTO();
            BeanUtils.copyProperties(item, fcInitSubjectDTO);
            FcAccDTO fcAccDTO = fcAccDTOMap.get(item.getAccount());
            fcInitSubjectDTO.setSubjectId(fcAccDTO.getSubjectId());
            fcInitSubjectDTO.setCurId(fcAccDTO.getCurId());
            fcInitSubjectDTO.setBalanceWay(fcAccDTO.getBalanceWay());
            fcInitSubjectDTO.setYearBalance(fcInitSubjectDTO.getDebitMoney().subtract(fcInitSubjectDTO.getCreditMoney())
                    .multiply(BigDecimal.valueOf(fcAccDTO.getBalanceWay())));
            return fcInitSubjectDTO;
        }).collect(Collectors.groupingBy(item -> item.getCurId() + "_" + item.getSubjectId()+ "_" + item.getBalanceWay(),
                Collectors.reducing(FcInitSubjectDTO::merge))).values().stream().map(Optional::get).collect(Collectors.toList());

    }
}
