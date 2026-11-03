package com.bytter.report.domain.netbank.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.service.IHomeBankAccBalDistributeService;
import com.bytter.report.api.vo.HomeBankAccBalDistributeSearchVO;
import com.bytter.report.domain.netbank.dao.HomeBankAccBalDistributeDao;
import com.bytter.report.domain.netbank.service.HomeBankAccBalDistributeService;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalChartVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccBalVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行账户余额分布Service接口实现类。
 *
 * @author zhaohc
 * @date 2024/4/18 16:38
 */
@Service("homeBankAccBalDistributeServiceI")
@Transactional
@DubboService
public class HomeBankAccBalDistributeServiceImpl implements IHomeBankAccBalDistributeService {

    @Autowired(required = false)
    private HomeBankAccBalDistributeDao bankAccBalDistributeDao;
    @Autowired(required = false)
    private HomeBankAccBalDistributeService homeBankAccBalDistributeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;


    @Override
    public Map<String, Object> createBankAccBalBarToMobile(HomeBankAccBalDistributeSearchVO searchVO, ICurrentUser user) {
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        //获取账户余额
        String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "acc.corp_Id", true);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
            corpAuthSql += "and " + SqlUtil.getInSql(accList, "acc.id", true);
        }
        //银行账户月类别统计占比
        List<HomeBankAccBalVO> listBal = null;
        listBal = this.bankAccBalDistributeDao.getBankAccBalGroupByBankType(corpAuthSql);
        homeBankAccBalDistributeService.convertMoneyType(listBal, searchVO.getMoneyType());
        Map<String, Object> map = new HashMap<>();
        Map<String, HomeBankAccBalChartVO> balMap = new HashMap<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (HomeBankAccBalVO vo:listBal) {
            String key = vo.getBankTypeName();
            HomeBankAccBalChartVO chart = null;
            if (balMap.containsKey(key)) {
                chart = balMap.get(key);
                chart.setValue(chart.getValue().add(vo.getBal()));
                sum = sum.add(vo.getBal());
            } else {
                chart = new HomeBankAccBalChartVO();
                if (StringUtils.isBlank(vo.getBankTypeName())) {
                    continue;
                }
                chart.setName(vo.getBankTypeName());
                chart.setValue(vo.getBal());
                sum = sum.add(vo.getBal());
            }
            balMap.put(chart.getName(), chart);
        }
        List<String> names = new ArrayList<>();
        List<HomeBankAccBalChartVO> bals = new ArrayList<>();
        BigDecimal elseBankType = BigDecimal.ZERO;
        for (String key:balMap.keySet()) {
            HomeBankAccBalChartVO homeBankAccBalChartVO = balMap.get(key);
            BigDecimal value = homeBankAccBalChartVO.getValue();
            if(BigDecimal.ZERO.compareTo(value) < 0 && BigDecimal.ZERO.compareTo(sum) < 0){
                BigDecimal percent = value.divide(sum, 12, BigDecimal.ROUND_HALF_UP);
                if(new BigDecimal("0.01").compareTo(percent) >= 0){
                    elseBankType = elseBankType.add(value);
                }else{
                    names.add(key);
                    bals.add(balMap.get(key));
                }
            }
        }

        //银行类型占比<=1%的银行统一归类为“其他银行”
        if(BigDecimal.ZERO.compareTo(elseBankType) < 0){
            HomeBankAccBalChartVO homeBankAccBalChartVO = new HomeBankAccBalChartVO();
            homeBankAccBalChartVO.setName("其他银行");
            homeBankAccBalChartVO.setValue(elseBankType);
            names.add("其他银行");
            bals.add(homeBankAccBalChartVO);
        }
        map.put("bals", bals);
        map.put("names", names);
        return map;
    }
}
