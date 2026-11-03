package com.bytter.biz.bdlink.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.dao.BisAccDtlCheckDao;
import com.bytter.biz.bdlink.entity.BisAccDtlCheck;
import com.bytter.biz.bdlink.vo.BisAccDtlCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTableOutPutOrderUtils;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
@Service("bisAccDtlCheckService")
@Transactional
public class BisAccDtlCheckService extends BaseService<BisAccDtlCheck, String> implements ITaskService {

    @Autowired
    private BisAccDtlCheckDao bisAccDtlCheckDao;

    @Autowired(required = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    @Autowired
    private BisAccDtlService bisAccDtlService;

    @Autowired(required = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @Override
    public BaseDao<BisAccDtlCheck, String> getEntityDao() {
        return bisAccDtlCheckDao;
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCDTLCHECK_QUERY, user.getId()), "t.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<BisAccDtlCheckVO> dataTablesOutput = bisAccDtlCheckDao.getDataTablesUnion(dataTablesInput, sql.toString());
        if (dataTablesOutput != null) {
            List<BisAccDtlCheckVO> list = dataTablesOutput.getData();
            List<String> curIdList = list.stream().map(BisAccDtlCheckVO::getCurId).collect(Collectors.toList());
            List<String> corpIdList = list.stream().map(BisAccDtlCheckVO::getCorpId).collect(Collectors.toList());
            Map<String, DictDTO> dictMap = dictService.listMap("bisAccDtlCheck.status");
            Map<String, String> corpNameMap = corpService.getNameByIds(corpIdList);
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyByIds(curIdList);
            for (BisAccDtlCheckVO vo : list) {
                // 差额=（余额-核查余额）绝对值
                vo.setDiffBal((vo.getBal().subtract(vo.getCheckBal())).abs());
                // 填充数据
                Optional.ofNullable(vo.getCorpId()).flatMap(corpId ->
                        Optional.ofNullable(corpNameMap.get(corpId))).ifPresent(vo::setCorpName);
                Optional.ofNullable(vo.getCurId()).flatMap(curId ->
                                Optional.ofNullable(currencyMap.get(curId))).
                        ifPresent(currency -> vo.setCurName(currency.getCurName()));
                Optional.ofNullable(vo.getStatus()).flatMap(status ->
                                Optional.ofNullable(dictMap.get(status))).
                        ifPresent(dict -> vo.setStatusName(dict.getName()));
            }
        }
        //前后端分离的情况做排序
        if(dataTablesInput.getColumns()!=null && dataTablesInput.getColumns().size() == 1){
            DataTableOutPutOrderUtils.orderOutputList(dataTablesInput,dataTablesOutput.getData());
        }
        return dataTablesOutput;
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryBankAccDatatable(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(
                corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCDTLCHECK_APPLY, user.getId()), "acc.corp_id",
                true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<BtBankAccVO> output = bisAccDtlCheckDao.queryBankAccDatatable(dataTablesInput, sql.toString());
        if (output != null) {
            List<BtBankAccVO> bankAccData = output.getData();
            List<String> curIdList = bankAccData.stream().map(BtBankAccVO::getCurId).collect(Collectors.toList());
            List<String> corpIdList = bankAccData.stream().map(BtBankAccVO::getCorpId).collect(Collectors.toList());
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("sf_yn", "bankAcc.validSign");
            Map<String, String> corpNameMap = corpService.getNameByIds(corpIdList);
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyByIds(curIdList);
            for (BtBankAccVO vo : bankAccData) {
                btBankAccService.setBtCurrency(vo);
                // 填充数据
                Optional.ofNullable(vo.getCorpId()).flatMap(corpId ->
                        Optional.ofNullable(corpNameMap.get(corpId))).ifPresent(vo::setCorpName);
                Optional.ofNullable(vo.getCurId()).flatMap(curId ->
                                Optional.ofNullable(currencyMap.get(curId))).
                        ifPresent(currency -> vo.setCurName(currency.getCurName()));
                Optional.ofNullable(vo.getIsCheck()).ifPresent(isCheck ->
                        Optional.ofNullable(listDictMap.get("sf_yn")).
                                ifPresent(dictMap -> vo.setIsCheckStr(dictMap.get(isCheck).getName())));
                Optional.ofNullable(vo.getValidSign()).ifPresent(validSign ->
                        Optional.ofNullable(listDictMap.get("bankAcc.validSign")).
                                ifPresent(dictMap -> vo.setValidSignStr(dictMap.get(validSign).getName())));
                Optional.ofNullable(vo.getIsOnline()).ifPresent(isOnline ->
                        Optional.ofNullable(listDictMap.get("sf_yn")).
                                ifPresent(dictMap -> vo.setIsOnlineStr(dictMap.get(isOnline).getName())));
            }
            output.setData(bankAccData);
        }
        return output;
    }

    /**
     * 批量设置状态
     *
     * @param ids
     * @param status
     */
    public void batchUpdateStatus(String ids, String status, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlCheck entity = this.get(id);
            entity.setStatus(status);
            this.saveOrUpdate(entity);
            if (CommonConstant.YES.equals(status)) {
                notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL_CHECK, entity.getId(), user.getId());
            }
        }
    }

    /**
     * 批量设置稽查账户
     *
     * @param ids
     * @param checkSign
     */
    public void setCheckBankAccSave(String ids, String checkSign) {
        btBankAccService.setCheckBankAccSave(ids, checkSign);
    }

    @Override
    @XxlJob("BisAccDtlCheckService")
    public void run(String arguments) {
        // 获取有效的稽查账户列表
        List<BtBankAccVO> listBankAcc = bisAccDtlCheckDao.getCheckBankAcc();
        Date checkDate = DateUtils.addDay(DateUtils.getDate(), -1);
        for (BtBankAccVO bankAcc : listBankAcc) {
            // 获取账户的昨日期初余额
            BisAccBalDTO openBal = bisAccBalService.getBisAccBalBy(bankAcc.getBankAcc(), bankAcc.getCurId(), DateUtils.addDay(checkDate, -1));
            if (openBal == null) {
                continue;
            }
            if (openBal.getBal() == null) {
                openBal.setBal(BigDecimal.ZERO);
            }
            // 获取账户昨日期末余额
            BisAccBalDTO closeBal = bisAccBalService.getBisAccBalBy(bankAcc.getBankAcc(), bankAcc.getCurId(), checkDate);
            if (closeBal == null) {
                continue;
            }
            if (closeBal.getBal() == null) {
                closeBal.setBal(BigDecimal.ZERO);
            }
            // 获取账户昨日明细计算差额
            BigDecimal lastBal = bisAccDtlService.getLastBal(bankAcc.getBankAcc(), bankAcc.getCurId(), checkDate);
            // 计算差额
            BigDecimal checkBal = openBal.getBal().add(lastBal);
            BigDecimal diffBal = (checkBal.subtract(closeBal.getBal())).abs();
            if (BigDecimal.ZERO.compareTo(diffBal) != 0) {
                // 差额不为0，增加提醒功能，并记录核查记录
                BisAccDtlCheck bisAccDtlCheck = bisAccDtlCheckDao.getBisAccDtlCheck(bankAcc.getBankAcc(), bankAcc.getCurId(), checkDate);
                if (bisAccDtlCheck == null) {
                    bisAccDtlCheck = new BisAccDtlCheck();
                    bisAccDtlCheck.setCorpId(bankAcc.getCorpId());
                    bisAccDtlCheck.setBankAcc(bankAcc.getBankAcc());
                    bisAccDtlCheck.setCurId(bankAcc.getCurId());
                    bisAccDtlCheck.setCheckDate(checkDate);
                    bisAccDtlCheck.setCreateDate(DateUtils.getDate());
                } else {
                    bisAccDtlCheck.setUpdateDate(DateUtils.getDate());
                }
                bisAccDtlCheck.setStatus(CommonConstant.NO);
                bisAccDtlCheck.setBal(closeBal.getBal());
                bisAccDtlCheck.setCheckBal(checkBal);
                this.saveOrUpdate(bisAccDtlCheck);
                BisAccDtlCheckVO bisAccDtlCheckVO = new BisAccDtlCheckVO();
                BeanUtils.copyNotNullProperties(bisAccDtlCheck, bisAccDtlCheckVO);
                bisAccDtlCheckVO.setCorpName(bankAcc.getCorpName());
                // 发送消息
                notificationService.complete(NotificationConstant.NETBANK.BANKACC_DTL_CHECK, bisAccDtlCheck.getId(), null);
                notificationService.send(bisAccDtlCheck.getId(), bisAccDtlCheck.getCorpId(),
                        NotificationConstant.NETBANK.BANKACC_DTL_CHECK, bisAccDtlCheckVO);
            }
        }
    }
}
