package com.bytter.biz.inneraccount.service;

import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.vo.BisAccDtlHisVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlImportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service("bisAccDtlHisService")
@Transactional
public class BisAccDtlHisService extends BaseService<BtBankAccDtl, String> implements ICustomVerifyHandler<BisAccDtlHisVO> {
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<BtBankAccDtl, String> getEntityDao() {
        return null;
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisAccDtlHisVO obj, Object... objs) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisAccDtlHisVO> list, Object... objs) {
        if (EmptyUtils.isNotEmpty(list)) {
            BtBankAccDTO bankAccDTO = null;
            for(BisAccDtlHisVO vo :list){
                List<String> msgList = new ArrayList<>();
                //本方账户
                if(StringUtils.isBlank(vo.getBankAcc())) {
                    msgList.add("nis.netbank.message.dtl.bankacc.null");
                }else {
                    bankAccDTO = new BtBankAccDTO();
                    bankAccDTO.setBankAcc(vo.getBankAcc());
                    List<BtBankAccDTO> listBankAcc = btBankAccService.getBankAccList(bankAccDTO);
                    if(EmptyUtils.isEmpty(listBankAcc)) {
                        msgList.add("nis.netbank.message.dtl.bankaccinfo.null");
                    }else {
                        BeanUtils.copyNotNullProperties(listBankAcc.get(0), bankAccDTO);
                        if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAccDTO.getIsOnline())) {
                            // 只能导入离线账号
                            msgList.add("nis.netbank.message.dtl.online.no");
                        }
                        vo.setBifCode(bankAccDTO.getBifCode());
                        vo.setBankName(bankAccDTO.getBankName());
                        vo.setAccName(bankAccDTO.getAccName());
                        //账号只有一个币种时，默认
                        if(bankAccDTO.getCurList().size()==1){
                            vo.setCurId(bankAccDTO.getCurList().get(0));
                        }
                    }
                }
                if(StringUtils.isBlank(vo.getCurId())) {
                    // 币别不为空
                    if (EmptyUtils.isBlank(vo.getCurName())) {
                        msgList.add("nis.netbank.message.curname.null");
                    } else {
                        // 判断币别是否在系统中存在
                        BtCurrencyDTO currency = this.btCurrencyService.getByCurNameOrEnglishCode(vo.getCurName());
                        if (EmptyUtils.isEmpty(currency) || StringUtils.isBlank(currency.getId())) {
                            msgList.add("nis.netbank.message.cur.null");
                        } else {
                            vo.setCurId(currency.getId());
                        }
                    }
                }
                //币种与账号是否一致
                if (!EmptyUtils.isBlank(vo.getAccName()) && !EmptyUtils.isBlank(vo.getCurId())) {
                    if (EmptyUtils.isEmpty(this.btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId()))) {
                        msgList.add("nis.netbank.message.bankaccinfo.cur.null");
                    }
                }
                try {
                    //交易金额 不能同时为空
                    if (StringUtils.isBlank(vo.getPayMoneyStr()) || "0".equals(vo.getPayMoneyStr())) {
                        vo.setPayMoney(BigDecimal.ZERO);
                    } else {
                        //考虑带千分位的金额格式
                        vo.setPayMoneyStr(vo.getPayMoneyStr().replaceAll(CommonConstant.COMMA_SPLIT, ""));
                        vo.setPayMoney(new BigDecimal(vo.getPayMoneyStr()));
                        vo.setPayMoneyStr(NumberUtil.formatNumeric(vo.getPayMoney()));
                    }
                    if (StringUtils.isBlank(vo.getIncomeMoneyStr()) || "0".equals(vo.getIncomeMoneyStr())) {
                        vo.setIncomeMoney(BigDecimal.ZERO);
                    } else {
                        //考虑带千分位的金额格式
                        vo.setIncomeMoneyStr(vo.getIncomeMoneyStr().replaceAll(CommonConstant.COMMA_SPLIT, ""));
                        vo.setIncomeMoney(new BigDecimal(vo.getIncomeMoneyStr()));
                        vo.setIncomeMoneyStr(NumberUtil.formatNumeric(vo.getIncomeMoney()));
                    }
                    if (vo.getPayMoney().compareTo(BigDecimal.ZERO) != 0 && vo.getIncomeMoney().compareTo(BigDecimal.ZERO) != 0) {
                        msgList.add("bisaccdtl.message.importconfig.money.mismatch");
                    } else {
                        if (vo.getPayMoney().compareTo(BigDecimal.ZERO) != 0) {
                            vo.setAmt(vo.getPayMoney());
                            vo.setCdSign(BisAccBalConstant.CDSIGN_OUT);
                        } else {
                            vo.setAmt(vo.getIncomeMoney());
                            vo.setCdSign(BisAccBalConstant.CDSIGN_IN);
                        }
//                        // 金额大于0
//                        if (vo.getAmt().compareTo(BigDecimal.ZERO) <= 0) {
//                            msgList.add("nis.netbank.message.amt.mismatch");
//                        }
                    }
                    //余额
                    if(StringUtils.isNotBlank(vo.getBalStr())){
                        //考虑带千分位的金额格式
                        vo.setBalStr(vo.getBalStr().replaceAll(CommonConstant.COMMA_SPLIT, ""));
                        vo.setBal(new BigDecimal(vo.getBalStr()));
                        vo.setBalStr(NumberUtil.formatNumeric(vo.getBal()));
                    }
                } catch (NumberFormatException e) {
                    //金额格式化不正确
                    msgList.add("bisaccdtl.message.amt.mismatch");
                }
                //交易时间
                String transTimeStr = "";
                Date transTime = null;
                if(!EmptyUtils.isEmpty(vo.getTransDateStr())) {
                    String transDateStr = vo.getTransDateStr();
                    //无分隔符YYYYMMDD
                    if (transDateStr.length() == 8) {
                        transDateStr = transDateStr.substring(0, 4) + "-" + transDateStr.substring(4, 6) + "-" + transDateStr.substring(6, 8);
                    }
                    vo.setTransDate(DateUtils.parse(transDateStr, DateUtils.PATTERN_CLASSICAL_SIMPLE));
                    if (vo.getTransDate() == null) {
                        try {
                            DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                            vo.setTransDate(DateUtils.parse(DateUtils.formatDate(df.parse(vo.getTransDateStr()), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
                        } catch (Exception e) {
                            vo.setTransDate(null);
                        }
                    }
                    if (vo.getTransDate() != null) {
                        transDateStr = DateUtils.formatDate(vo.getTransDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                    }
                    vo.setTransDateStr(transDateStr);
                    if (StringUtils.isNotBlank(vo.getTransTimeStr())) {
                        if (vo.getTransTimeStr().length() == 8 || vo.getTransTimeStr().length() == 7) {
                            transTimeStr = vo.getTransTimeStr();
                        } else {
                            msgList.add("ras.import.revenue.transdate.format.error");
                        }
                        vo.setTransTimeStr(transTimeStr);
                        transTime = DateUtils.parse(transDateStr + " " + transTimeStr, DateUtils.PATTERN_CLASSICAL);
                        vo.setTransTime(transTime);
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }

    @Override
    public ImportCustomRules getImportCustomRules(Object... obj) {
        return null;
    }
}
