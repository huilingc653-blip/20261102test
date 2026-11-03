package com.bytter.biz.inneraccount.service;


import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.vo.BisAccBalVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BIS_ACC_BAL
 * @date 2017-06-23 15:02:30
 */
@Service("bisAccBalImportService")
@Transactional
public class BisAccBalImportService implements IExcelVerifyHandler<BisAccBalVO> {
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @Autowired
    private IDictService dictServiceI;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;


    @Override
    public ExcelVerifyHanlderResult verifyHandler(BisAccBalVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<BisAccBalVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //用来存储导入模版里面账号，日期，币别来判断是否有重复
            List<String> balcurdate = new ArrayList<>();
            for (BisAccBalVO vo : list) {
                List<String> msgList = new ArrayList<>();
                BtBankAccDTO bankAcc = null;
                String curId = "";
                // 银行账号不为空
                if (EmptyUtils.isBlank(vo.getBankAcc())) {
                    msgList.add("nis.netbank.message.bankacc.null");
                } else {
                    // 判断银行账号是否在系统中存在
//                    BtBankAccDTO bankAcc = this.btBankAccService.getBankAccInfo(vo.getBankAcc());
                    bankAcc = this.btBankAccService.getBankAccInfoByBankAcc(vo.getBankAcc());
                    if (EmptyUtils.isEmpty(bankAcc)) {
                        msgList.add("nis.netbank.message.bankaccinfo.null");
                    } else {
                        vo.setCorpId(bankAcc.getCorpId());
                        vo.setBifCode(bankAcc.getBifCode());
                        vo.setBankName(bankAcc.getBankName());
                        vo.setAccName(bankAcc.getAccName());
                        if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAcc.getIsOnline())) {
                            // 只能导入离线账号
                            msgList.add("nis.netbank.message.online.no");
                        }
                    }
                }
                // 币别不为空
                if (EmptyUtils.isBlank(vo.getCurName())) {
                    msgList.add("nis.netbank.message.curname.null");
                } else {
                    // 判断币别是否在系统中存在
                    curId = this.btCurrencyService.getCurrencyByName(vo.getCurName());
                    if (EmptyUtils.isBlank(curId)) {
                        msgList.add("nis.netbank.message.cur.null");
                    } else {
                        //查询 银行账号币别是否存在
                        BtBankAccDTO accAndCurId = this.btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), curId);
                        if( accAndCurId == null){
                            //该银行账户未存在该币种
                            msgList.add("nis.netbank.message.bankacc.cur.null");
                        }else {
                            vo.setCurId(curId);
                        }
                    }
                }
                // 余额不能为空
                if (EmptyUtils.isEmpty(vo.getBal())) {
                    msgList.add("nis.netbank.message.bal.null");
                }
                // 余额日期不能为空
                if (EmptyUtils.isEmpty(vo.getBalDate())) {
                    msgList.add("nis.netbank.message.baldate.null");
                } else {
                    if (bankAcc != null && !EmptyUtils.isEmpty(curId)) {
//                        BigDecimal bal = bisAccBalService.getBalByBalDate(vo.getBankAcc(),curId,vo.getBalDate());
//                        // 当前日期已存在余额
//                        if (!EmptyUtils.isEmpty(bal)) {
//                            msgList.add("nis.netbank.message.balbybaldate.notnull");
//                        }
//                        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(), vo.getCurId());
                        // 账户注册日期为空
//                        if (EmptyUtils.isEmpty(btBankAccDTO) || EmptyUtils.isEmpty(btBankAccDTO.getRegDate())) {
                        if (EmptyUtils.isEmpty(bankAcc) || EmptyUtils.isEmpty(bankAcc.getRegDate())) {
                            msgList.add("nis.netbank.message.regdate.null");
                        }
                        // 当前余额日期在注册日期之前
//                        if (!EmptyUtils.isEmpty(btBankAccDTO) && vo.getBalDate().before(btBankAccDTO.getRegDate())) {
                        if (!EmptyUtils.isEmpty(bankAcc) && vo.getBalDate().before(bankAcc.getRegDate())) {
                            msgList.add("bt.bankaccdtl.message.transtime.lt.accreddate");
                        }
                        //校验交易日期不能大于系统当前日期
                        if (vo.getBalDate().after(DateUtils.getDateYMD())) {
                            msgList.add("bt.bankaccdtl.message.baldate.lt.sysdate");
                        }
                    }
                }
//                //根据账号日期币别id判断余额是否有重复
//                if (balcurdate.contains(vo.getBankAcc() + DateUtils.format(vo.getBalDate(), "yyyyMMdd") + vo.getCurId())) {
//                    msgList.add("nis.netbank.message.balcurdate.repetition");
//                }
//                balcurdate.add(vo.getBankAcc() + DateUtils.format(vo.getBalDate(), "yyyyMMdd") + vo.getCurId());
                vo.addErrorMsg(msgList);
            }
        }
    }
}
