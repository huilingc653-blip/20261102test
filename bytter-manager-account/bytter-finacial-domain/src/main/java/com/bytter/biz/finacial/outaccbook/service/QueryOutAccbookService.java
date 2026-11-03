package com.bytter.biz.finacial.outaccbook.service;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterBalanceWay;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterMeasureUnit;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SubjectSort;
import com.bytter.biz.finacial.outaccbook.dao.QueryOutAccbookDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.vo.FcOutAccbookVo;
import com.bytter.biz.finacial.paramconfig.entity.FcSubject;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 表外流水账主档表
 * @date 2017-07-05 15:40:08
 */
@Service("queryOutAccbookService")
@Transactional
public class QueryOutAccbookService extends BaseService<FcOutAccbook, String> {
    @Autowired
    private QueryOutAccbookDao fcOutAccbookDao;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    /**
     * 功能介绍 :流水账查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listAll(DataTablesInput input, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccNameAndMeasureUnitByCorpId(corpAuth);
        List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
        DataTablesOutput dataTablesOutput = fcOutAccbookDao.listAll(input, accounts);
        List data = dataTablesOutput.getData();
        ArrayList<String> operatorCodes = new ArrayList<>();
        ArrayList<FcOutAccbookVo> voLists = new ArrayList<>();
        for (Object datum : data) {
            FcOutAccbookVo vo = (FcOutAccbookVo) datum;
            FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(vo.getAccount());
            vo.setAccname(fcAccSubjectDTO.getAccName());
            vo.setMeasureUnit(fcAccSubjectDTO.getMeasureUnit());
            operatorCodes.add(vo.getOperatorCode());
            voLists.add(vo);
        }
        Map<String, String> usernameMap = iUserService.getUserNameByIds(operatorCodes);
        voLists.forEach(a -> {
            a.setUsername(usernameMap.get(a.getOperatorCode()));
        });
        dataTablesOutput.setData(voLists);
        return dataTablesOutput;
    }
    /**
     * 功能介绍 :查看详情<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public FcOutAccbookVo view(String id) {
        FcOutAccbookVo view = fcOutAccbookDao.view(id);
        FcAccSubjectDTO fcAccSubjectDTO = iFcAccService.getAccSubjectCurDateByAccount(view.getAccount());
        view.setMeasureUnit(fcAccSubjectDTO.getMeasureUnit());
        view.setCurId(fcAccSubjectDTO.getCurId());
        view.setAccname(fcAccSubjectDTO.getAccName());
        Map<String, String> usernameMap = iUserService.getUserNameByIds(Lists.newArrayList(view.getUsername(), view.getCheckerName()));
        view.setUsername(usernameMap.get(view.getUsername()));
        view.setCheckerName(usernameMap.get(view.getCheckerName()));
        return view;
    }
    /**
     * 功能介绍 : 分户账查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public Map<String, Object> divided(Integer pageNum, FcOutAccbookVo vo, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcAccDTO> fcAccList = getFcList(vo);
        Map<String, Object> result = fcOutAccbookDao.divided(pageNum, vo, corpAuth, fcAccList);
        List<FcOutAccbookVo> list = (List<FcOutAccbookVo>) result.get("res");// table集合
        FcOutAccbookVo accObj = (FcOutAccbookVo) result.get("accObj");// 根据页码显示的账号
        if (!EmptyUtils.isEmptyList(list)) {
            // 期初余额
            BigDecimal balance = fcOutAccbookDao.fundInitBalanByAccount(accObj.getAccount(), accObj.getStartDate());
            result.put("qcbalance", balance);
            cacleBalance(list, balance, "");// 计算余额 TODO 单位未确定
        }
        return result;
    }
    /**
     * 根据查询条件获取内部账号集合
     *
     * @param vo 查询条件
     * @return 账号集合数据
     */
    private List<FcAccDTO> getFcList(FcOutAccbookVo vo) {
        FcAccDTO fcAccDto = new FcAccDTO();
        fcAccDto.setCorpId(vo.getCorpId());
        fcAccDto.setNetId(vo.getNetId());
        fcAccDto.setSubjectSort(vo.getSubjectSort());
        return iFcAccService.listByCondition(fcAccDto);
    }
    /**
     * 功能介绍 : 计算余额<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    private void cacleBalance(List<FcOutAccbookVo> list, BigDecimal balance, String measureUnit) {
        // 收方合计
        double a = 0.00;
        BigDecimal sumCredit = BigDecimal.valueOf(a);
        // 借方合计
        BigDecimal sumDebit = BigDecimal.valueOf(a);
        //余额
        for (FcOutAccbookVo vo : list) {
            BigDecimal creditMoney = vo.getCreditMoney();
            BigDecimal debitMoney = vo.getDebitMoney();
            Integer balanceWay = vo.getBalanceWay();
            if (!OuterBalanceWay.BAL_DEBIT.get().equals(balanceWay)) {
                balanceWay = OuterBalanceWay.BAL_CREDIT.get();
            }
            // 乘以余额方向
            BigDecimal dc = debitMoney.subtract(creditMoney).multiply(BigDecimal.valueOf(balanceWay));
            balance = balance.add(dc);
            vo.setBalance(balance);
            sumCredit = sumCredit.add(creditMoney);
            sumDebit = sumDebit.add(debitMoney);
            vo.setMeasureUnit(measureUnit);
        }
        /**
         * 合计收入金额，付出金额，余额
         */
        FcOutAccbookVo sumMap = new FcOutAccbookVo();
        /* sumMap.put("measureUnit", measureUnit);*/
        sumMap.setContractNo("合计:");
        if (!OuterMeasureUnit.UNIT_MONEY.get().equals(measureUnit)) {
            sumMap.setCreditMoney(sumCredit);
            sumMap.setDebitMoney(sumDebit);
        } else {
            sumMap.setCreditMoney(sumCredit);
            sumMap.setDebitMoney(sumDebit);
        }
        list.add(sumMap);
    }
    /**
     * 功能介绍 : 余额查询<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryBalance(DataTablesInput dataTablesInput, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccSubjectDateByCorpIds(corpAuth);
        List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
        DataTablesOutput dataTablesOutput = fcOutAccbookDao.queryBalance(dataTablesInput, accounts);
        List data = dataTablesOutput.getData();
        for (Object datum : data) {
            FcOutAccbookVo vo = (FcOutAccbookVo) datum;
            FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(vo.getAccount());
            vo.setAccname(fcAccSubjectDTO.getAccName());
            vo.setBalanceWay(fcAccSubjectDTO.getBalanceWay());
            vo.setSubjectName(fcAccSubjectDTO.getSubjectName());
        }
        return dataTablesOutput;
    }
    /**
     * 功能介绍 : 总账查询<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public Map<String, Object> querySumAccount(Integer pageNum, FcOutAccbookVo vo, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcOutAccbookVo> searchList = fcOutAccbookDao.querySumAccount(pageNum, vo, corpAuth);
        Map<String, Object> result = new HashMap<String, Object>();// 保存结果的map
        if (searchList.isEmpty()) {
            result.put("search", new ArrayList<FcOutAccbookVo>());// list为null，返回空集合
        } else {
            FcOutAccbookVo res = searchList.get(pageNum - 1);// 根据页数获取账号
            result.put("search", res);// 保存科目，网点等对象
            result.put("size", searchList.size());// 总页数
            Map<String, Object> initData = fcOutAccbookDao.getInitDataBySubject(res);// 期初余额，笔数
            result.put("qcCount", initData.get("count"));// 保存期初笔数
            result.put("qcBalance", initData.get("money"));// 保存期初余额
            List<String> list = iFcSubjectService.getSubsetBySubjectSort(res.getSubjectId(), SubjectSort.SUBJECT_SORT_G.get());
            result.put("table", fcOutAccbookDao.querySumAccountList(res, list));// 列表集合
        }
        return result;
    }
    /**
     * 功能介绍 : 科目余额汇总表<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public Map<String, Object> querySubjectBalance(Integer pageNum, FcOutAccbookVo entity, String resource, String userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcOutAccbookVo> searchList = fcOutAccbookDao.querySubjectBalance(pageNum, entity, corpAuth);
        if (searchList.isEmpty()) {
            result.put("lst", new ArrayList<FcOutAccbookVo>());
        } else {
            FcOutAccbookVo head = searchList.get(pageNum - 1);// 获取网点，日期，币别
            result.put("head", head);// 表头信息
            result.put("size", searchList.size());//总页数
            List<FcOutAccbookVo> lst = fcOutAccbookDao.querySubjectBalanceList(head);// table集合
            // 计算合计
            Map<String, Object> total = new HashMap<String, Object>();
            BigDecimal yBalanceMoney = BigDecimal.ZERO;
            BigDecimal debitMoney = BigDecimal.ZERO;
            BigDecimal creditMoney = BigDecimal.ZERO;
            BigDecimal tBalanceMoney = BigDecimal.ZERO;
            total.put("subjectName", "<div align='center'><b>合计:</b></div>");
            for (FcOutAccbookVo fcOutAccbookVo : lst) {
                BigDecimal yBalance = fcOutAccbookVo.getyBalance();
                BigDecimal dMoney = fcOutAccbookVo.getDebitMoney();
                BigDecimal cMoney = fcOutAccbookVo.getCreditMoney();
                BigDecimal tBalance = fcOutAccbookVo.gettBalance();
                // 算合计
                yBalanceMoney = yBalanceMoney.add(yBalance);// 期初
                debitMoney = debitMoney.add(dMoney);
                creditMoney = creditMoney.add(cMoney);
                tBalanceMoney = tBalanceMoney.add(tBalance);// 期末
            }
            total.put("yBalance", "<b>" + NumberUtil.formatNumeric(yBalanceMoney.doubleValue()) + "</b>");
            total.put("debitMoney", "<b>" + NumberUtil.formatNumeric(debitMoney.doubleValue()) + "</b>");
            total.put("creditMoney", "<b>" + NumberUtil.formatNumeric(creditMoney.doubleValue()) + "</b>");
            total.put("tBalance", "<b>" + NumberUtil.formatNumeric(tBalanceMoney.doubleValue()) + "</b>");
            result.put("total", total);
            result.put("lst", lst);
        }
        return result;
    }
    /**
     * 功能介绍 : 日计表查询<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public Map<String, Object> queryOuterCharge(FcOutAccbookVo vo, boolean isDayCharge, Integer page, String resource, String userId) {// 核对方法参数和dao有没有问题
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcOutAccbookVo> res = fcOutAccbookDao.queryOuterDayCharge(vo, page, corpAuth);//表头
        if (res.isEmpty()) {
            result.put("list", new ArrayList());
            result.put("size", 0);
        } else {
            FcOutAccbookVo head = res.get(page - 1);
            result.put("startDate", head.getStartDate());
            result.put("head", head);// 表头
            result.put("size", res.size());
            if (isDayCharge) {// 是否是日计账表
                result.put("list", fcOutAccbookDao.queryOuterChargeTable(head));// 日计列表
            } else {
                result.put("list", fcOutAccbookDao.queryMonthChargeTable(head));// 月计列表
            }
        }
        if (vo.getStartDate() != null) {
            result.put("optDate", DateUtils.formatDate(vo.getStartDate(), "yyyy-MM-dd"));
        }
        List<FcOutAccbookVo> list = (List<FcOutAccbookVo>) result.get("list");
        if (EmptyUtils.isNotEmpty(vo.getIsShowChild()) && !" ".equals(vo.getIsShowChild()) && "1".equals(vo.getIsShowChild())) {// 是否选明细科目
            processDetailedSubjects(list);
        }
        // lst = addRowSum(lst,"day");//添加合计
        result.put("list", list);
        return result;
    }
    /**
     * 功能介绍 : 汇总明细科目<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    private void processDetailedSubjects(List<FcOutAccbookVo> lst) {
        if (!EmptyUtils.isEmptyList(lst)) {
            // 汇总结果
            Map<String, FcOutAccbookVo> results = new HashMap<String, FcOutAccbookVo>();
            // 查询所有表外科目
            List<FcSubject> sbj = fcOutAccbookDao.getAllOuterFcSubjects();
            // 科目-名称
            Map<String, String> subjectNameMaps = new HashMap<String, String>();
            // 科目-父科目
            Map<String, String> subjectCodeMaps = new HashMap<String, String>();
            for (FcSubject fcSubject : sbj) {
                subjectNameMaps.put(fcSubject.getSubjectCode(), fcSubject.getSubjectName());
                subjectCodeMaps.put(fcSubject.getSubjectCode(), fcSubject.getFatherId());
            }
            for (FcOutAccbookVo vo : lst) {
                String subjectCode = vo.getSubjectCode();
                if (!subjectCodeMaps.containsKey(subjectCode)) {
                    continue;
                }
                // 期初
                int yCount = vo.getyCount();
                BigDecimal yBalance = vo.getyBalance();
                Integer yNumber = vo.getyNumber();
                Integer yTicket = vo.getyTicket();
                // 收入
                BigDecimal dCount = vo.getDebitCount();
                BigDecimal dMoney = vo.getDebitMoney();
                Integer dNumber = vo.getDebitNumber();
                Integer dTicket = vo.getDebitTicket();
                // 支出
                BigDecimal cCount = vo.getCreditCount();
                BigDecimal cMoney = vo.getCreditMoney();
                Integer cNumber = vo.getCreditNumber();
                Integer cTicket = vo.getCreditTicket();
                // 期末
                BigDecimal tCount = vo.gettCount();
                BigDecimal tBalance = vo.gettBalance();
                Integer tNumber = vo.gettNumber();
                Integer tTicket = vo.gettTicket();
                // 获取科目顶级父节点
                String fatherCode = this.processRecursiveSubject(subjectCode, subjectCodeMaps);
                if (results.containsKey(fatherCode)) {
                    FcOutAccbookVo datas = results.get(fatherCode);//
                    datas.setyCount(yCount + (datas.getyCount() == null ? 0 : datas.getyCount()));
                    datas.setyBalance(yBalance.add(datas.getyBalance() == null ? BigDecimal.ZERO : datas.getyBalance()));
                    datas.setyNumber(yNumber + (datas.getyNumber() == null ? 0 : datas.getyNumber()));
                    datas.setyTicket(yTicket + (datas.getyTicket() == null ? 0 : datas.getyTicket()));
                    datas.setDebitCount(dCount.add(datas.getDebitCount() == null ? BigDecimal.ZERO : datas.getDebitCount()));
                    datas.setDebitMoney(dMoney.add(datas.getDebitMoney() == null ? BigDecimal.ZERO : datas.getDebitMoney()));
                    datas.setDebitNumber(dNumber + (datas.getDebitNumber() == null ? 0 : datas.getDebitNumber()));
                    datas.setDebitTicket(dTicket + (datas.getDebitTicket() == null ? 0 : datas.getDebitTicket()));
                    datas.setCreditCount(cCount.add(datas.getCreditCount() == null ? BigDecimal.ZERO : datas.getCreditCount()));
                    datas.setCreditMoney(cMoney.add(datas.getCreditMoney() == null ? BigDecimal.ZERO : datas.getCreditMoney()));
                    datas.setCreditNumber(cNumber + (datas.getCreditNumber() == null ? 0 : datas.getCreditNumber()));
                    datas.setCreditTicket(cTicket + (datas.getCreditTicket() == null ? 0 : datas.getCreditTicket()));
                    datas.settCount(tCount.add(datas.gettCount() == null ? BigDecimal.ZERO : datas.gettCount()));
                    datas.settBalance(tBalance.add(datas.gettBalance() == null ? BigDecimal.ZERO : datas.gettBalance()));
                    datas.settNumber(tNumber + (datas.gettNumber() == null ? 0 : datas.gettNumber()));
                    datas.settTicket(tTicket + (datas.gettTicket() == null ? 0 : datas.gettTicket()));
                } else {
                    FcOutAccbookVo datas = vo;
                    datas.setSubjectCode(fatherCode);
                    datas.setSubjectName(subjectNameMaps.get(fatherCode));
                    results.put(fatherCode, datas);
                }
            }
            lst.clear();
            for (FcOutAccbookVo data : results.values()) {
                lst.add(data);
            }
        }
    }
    /**
     * 功能介绍 : 获取科目顶级父节点<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    private String processRecursiveSubject(String subjectCode, Map<String, String> subjectCodeMaps) {
        String fatherCode = subjectCodeMaps.get(subjectCode);
        if (fatherCode == null || SubjectSort.SUBJECT_SORT_G.get().equals(fatherCode)) {
            return subjectCode;
        } else {
            return this.processRecursiveSubject(fatherCode, subjectCodeMaps);
        }
    }
    public BaseDao<FcOutAccbook, String> getEntityDao() {
        return fcOutAccbookDao;
    }
}
