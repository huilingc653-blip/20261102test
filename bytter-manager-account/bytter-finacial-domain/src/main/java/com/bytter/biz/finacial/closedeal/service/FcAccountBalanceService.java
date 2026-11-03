package com.bytter.biz.finacial.closedeal.service;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.closedeal.dao.FcAccountBalanceDao;
import com.bytter.biz.finacial.closedeal.vo.FcAccountBalanceVo;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 账户批量余额表
 * @date 2017-06-23 14:22:37
 */
@Service("fcAccountBalanceService")
@Transactional
public class FcAccountBalanceService extends BaseService<FcAccountBalanceVo, String> {
    @Autowired
    private FcAccountBalanceDao fcAccountBalanceDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IDictService dictService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 查询每日余额
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    public DataTablesOutput<FcAccountBalanceVo> getFcAccBalance(DataTablesInput dataTablesInput, String resource, String userId) {
        StringBuilder authSql = new StringBuilder();
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accAuth = accountGrantService.listByMultiValAccIds(userId, resource, null, AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            authSql.append(" and ").append(SqlUtil.getInSql(accAuth, "b.id", true));
        } else {
            List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
            authSql.append(" and ").append(SqlUtil.getInSql(corpAuth, "b.corp_Id", true));
        }
        //todo: 暂时不知道页面有什么查询条件和传参的查询条件数据格式，先不传，后面补上。
        String subjectId = StrUtil.toStringOrNull(dataTablesInput.getFormdata().get("eqs_subjectId"));
        //不属于后一张表的条件 防止自动拼接
        dataTablesInput.getFormdata().remove("eqs_subjectId");
        List<FcAccDTO> fcAccList = getFcAccList(null, null, null, subjectId, authSql.toString());
        if (CollectionUtils.isEmpty(fcAccList)) {
            return new DataTablesOutput<>();
        }
        //获取账号集合当作查询条件
        List<String> accList = fcAccList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        //组装拼接sql
        authSql = new StringBuilder(SqlUtil.getInSql(accList, "b.account", true));
        //查询数据
        DataTablesOutput<FcAccountBalanceVo> fcAccBalance = fcAccountBalanceDao.getFcAccBalance(dataTablesInput, authSql);
        //组装返回数据
        fcAccBalance.setData(assembleEntity(fcAccList, fcAccBalance.getData()));
        return fcAccBalance;
    }
    @Transactional(readOnly = true)
    public BaseDao<FcAccountBalanceVo, String> getEntityDao() {
        return fcAccountBalanceDao;
    }
    /**
     * 导出账户每日余额
     */
    @Transactional(readOnly = true)
    public List<FcAccountBalanceVo> export(String eqs_corpId, String eqs_netId, Date ged_balanceDate, Date led_balanceDate, String eqs_curId, String subjectId) {
        List<FcAccDTO> fcAccList = getFcAccList(eqs_corpId, eqs_netId, eqs_curId, subjectId, null);
        if (CollectionUtils.isEmpty(fcAccList)) {
            return new ArrayList<>();
        }
        List<String> accList = fcAccList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<FcAccountBalanceVo> fcAccBalance = fcAccountBalanceDao.getFcAccBalance(ged_balanceDate, led_balanceDate, accList);
        return assembleEntity(fcAccList, fcAccBalance);
    }
    /**
     * 组装参数
     *
     * @param fcAccList    内部账户集合
     * @param fcAccBalance 账户批量余额
     * @return 账户批量余额信息
     */
    private List<FcAccountBalanceVo> assembleEntity(List<FcAccDTO> fcAccList, List<FcAccountBalanceVo> fcAccBalance) {
        Map<String, FcAccDTO> fcAccMap = fcAccList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity(), (first,second) -> first));
        //设置字典值
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap( "Admin.BalanceWay");
        List<String> curIds = fcAccBalance.stream().map(FcAccountBalanceVo::getCurId).collect(Collectors.toList());
        Map<String, BtCurrencyDTO> currency = btCurrencyService.getCurrencyByIds(curIds);
        fcAccBalance.forEach(item -> {
            if (fcAccMap.containsKey(item.getAccount())) {
                FcAccDTO entity = fcAccMap.get(item.getAccount());
                BeanUtils.copyNotNullProperties(entity, item);
            }
            if (StringUtils.isNotEmpty(item.getCurId())) {
                item.setCurName(currency.get(item.getCurId()).getCurName());
            }
            if (Objects.nonNull(item.getBalanceWay())) {
                item.setBalanceName(dictMap.get("Admin.BalanceWay").get(String.valueOf(item.getBalanceWay())).getName());
            }
        });
        return fcAccBalance;
    }
    /**
     * 根据查询条件获取内部账号信息和科目代码信息
     *
     * @param eqsCorpId 单位id
     * @param eqsNetId  网点id
     * @param eqsCurId  币别id
     * @param subjectId 科目id
     * @return 内部账号集合
     */
    private List<FcAccDTO> getFcAccList(String eqsCorpId, String eqsNetId, String eqsCurId, String subjectId, String authSql) {
        FcAccDTO fcAccDto = new FcAccDTO();
        fcAccDto.setCorpId(eqsCorpId);
        fcAccDto.setNetId(eqsNetId);
        fcAccDto.setCurId(eqsCurId);
        fcAccDto.setSubjectId(subjectId);
        fcAccDto.setAuthSql(authSql);
        return fcAccService.listByCondition(fcAccDto);
    }
    @Transactional(readOnly = true)
    public List<FcAccDTO> getAccount(String userId) {
        List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(userId, null, ResourceConstant.NETBANK_INNERACCOUNT_FCACC_ALLQUERY, null, AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
        return listFcAcc;
    }
}
