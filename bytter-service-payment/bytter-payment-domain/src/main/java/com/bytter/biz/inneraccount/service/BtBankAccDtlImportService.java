package com.bytter.biz.inneraccount.service;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.BankAccDtlMessageEnum;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccDtl;
import com.bytter.biz.inneraccount.vo.BisAccDtlImportConfigVO;
import com.bytter.biz.inneraccount.vo.BtBankAccDtlImportVO;
@Service("btBankAccDtlImportService")
@Transactional
public class BtBankAccDtlImportService extends BaseService<BtBankAccDtl, String> implements ICustomVerifyHandler<BtBankAccDtlImportVO> {
    @Autowired
    private BisAccDtlImportConfigService bisAccDtlImportConfigService;
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
    public ExcelVerifyHanlderResult verifyHandler(BtBankAccDtlImportVO btBankAccDtlImportVO, Object... objects) {
        return null;
    }

    public static void main(String[] args) throws ParseException {

        String dateStr = "Mon Mar 23 00:00:00 CST 2026";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date pareseDate = sdf.parse(dateStr);
        String format = DateUtils.yyyyMMdd().format(pareseDate);
        System.out.println(format);
    }
    @Override
    public void verifyHandler(List<BtBankAccDtlImportVO> list, Object... objs) {
        ICurrentUser userVo =(ICurrentUser) objs[0];
        ImportCustomRules importCustomRules = (ImportCustomRules) objs[1];
        BisAccDtlImportConfigVO queryVo = new BisAccDtlImportConfigVO();
        String bankAcc = "";
        String curId = "";
        if(importCustomRules.getDataSources().indexOf(CommonConstant.COMMA_SPLIT)>-1){
            String[] dateSourcess = importCustomRules.getDataSources().split(CommonConstant.COMMA_SPLIT);
            queryVo.setBankTypeId(dateSourcess[0]);
            bankAcc = dateSourcess[1];
            curId = dateSourcess[2];
        }else {
            queryVo.setBankTypeId(importCustomRules.getDataSources());
        }
        queryVo.setFileType(importCustomRules.getSuffix());
        if (EmptyUtils.isNotEmpty(list)) {
            BtBankAccDTO bankAccDTO = null;
            for(BtBankAccDtlImportVO vo :list){
                List<String> msgList = new ArrayList<String>();
                //本方账户
                if(StringUtils.isBlank(bankAcc) && StringUtils.isBlank(vo.getBankAcc())) {
                    msgList.add("nis.netbank.message.dtl.bankacc.null");
                }else {
                    if(StringUtils.isNotBlank(bankAcc)) {
                        vo.setBankAcc(bankAcc);
                        vo.setCurId(curId);
                    }
                    bankAccDTO = new BtBankAccDTO();
                    bankAccDTO.setBankAcc(vo.getBankAcc());
                    if(StringUtils.isNotBlank(curId)){
                        bankAccDTO.setCurId(curId);
                    }
                    List<BtBankAccDTO> listBankAcc = btBankAccService.getBankAccList(bankAccDTO);
                    if(EmptyUtils.isEmpty(listBankAcc)) {
                        msgList.add("nis.netbank.message.dtl.bankaccinfo.null");
                    }else {
                        BeanUtils.copyNotNullProperties(listBankAcc.get(0), bankAccDTO);
                        if (BtBankAccConstant.ISONLINE_1.equalsIgnoreCase(bankAccDTO.getIsOnline())) {
                            // 只能导入离线账号
                            msgList.add("nis.netbank.message.dtl.online.no");
                        }
                        Boolean flag = accountGrantService.checkPermissionByUserAndAccount(userVo.getId(), vo.getBankAcc(), AccountGrantConstant.OFFLINE_BANK_TYPE,
                                AccountGrantConstant.QUERY_DETAIL, ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
                        //为空说明无权限
                        if(flag){
                            msgList.add("nis.netbank.message.bankacc.detail");
                        }
                        vo.setCorpId(bankAccDTO.getCorpId());
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
                    if(StringUtils.isNotBlank(vo.getTransMoneyStr())){
                        if (StringUtils.isNotBlank(vo.getCdSignStr())) {
                            if ("收入".equals(vo.getCdSignStr())
                                    || "CR".equalsIgnoreCase(vo.getCdSignStr())) {
                                vo.setCdSign(BisAccBalConstant.CDSIGN_IN);
                            } else if ("支出".equals(vo.getCdSignStr())
                                    || "DR".equalsIgnoreCase(vo.getCdSignStr())) {
                                vo.setCdSign(BisAccBalConstant.CDSIGN_OUT);
                            } else {
                                msgList.add("bisaccdtl.message.importconfig.cdsign.mismatch");
                            }
                        } else {
                            if(vo.getTransMoneyStr().indexOf("-") != -1){
                                vo.setCdSign(BisAccBalConstant.CDSIGN_OUT);
                            } else {
                                vo.setCdSign(BisAccBalConstant.CDSIGN_IN);
                            }
                        }
                        //考虑带千分位的金额格式
                        vo.setTransMoneyStr(vo.getTransMoneyStr().replaceAll(CommonConstant.COMMA_SPLIT, ""));
                        vo.setTransMoney(new BigDecimal(vo.getTransMoneyStr().replaceAll("-","")));
                        vo.setAmt(vo.getTransMoney());
                    } else {
                        //交易金额 不能同时为空
                        if (StringUtils.isBlank(vo.getPayMoneyStr()) || "-".equals(vo.getPayMoneyStr())) {
                            vo.setPayMoney(BigDecimal.ZERO);
                        } else {
                            //考虑带千分位的金额格式
                            vo.setPayMoneyStr(vo.getPayMoneyStr().replaceAll(CommonConstant.COMMA_SPLIT, ""));
                            vo.setPayMoney(new BigDecimal(vo.getPayMoneyStr()));
                            vo.setPayMoneyStr(NumberUtil.formatNumeric(vo.getPayMoney()));
                        }
                        if (StringUtils.isBlank(vo.getIncomeMoneyStr()) || "-".equals(vo.getIncomeMoneyStr())) {
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
                            if (vo.getPayMoney().compareTo(BigDecimal.ZERO) > 0) {
                                vo.setAmt(vo.getPayMoney());
                                vo.setCdSign(BisAccBalConstant.CDSIGN_OUT);
                            } else {
                                vo.setAmt(vo.getIncomeMoney());
                                vo.setCdSign(BisAccBalConstant.CDSIGN_IN);
                            }
                            // 金额大于0
                            if (vo.getAmt().compareTo(BigDecimal.ZERO) <= 0) {
                                msgList.add("nis.netbank.message.amt.mismatch");
                            }
                        }
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
                if (EmptyUtils.isEmpty(vo.getTransTimeStr())) {
                    msgList.add("nis.netbank.message.transtime.null");
                } else {
                    String transTimeStr = "";
                    Date transTime = null;
                    if(!EmptyUtils.isEmpty(vo.getTransDateStr())){
                        String transDateStr = vo.getTransDateStr();
                        //无分隔符YYYYMMDD
                        if(transDateStr.length() == 8){
                            transDateStr = transDateStr.substring(0,4)+"-"+transDateStr.substring(4,6)+"-"+transDateStr.substring(6,8);
                        }
                        vo.setTransDate(DateUtils.parse(transDateStr, DateUtils.PATTERN_CLASSICAL_SIMPLE));
                        if(vo.getTransDate() == null){
                            try {
                                DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                vo.setTransDate(DateUtils.parse(DateUtils.formatDate(df.parse(vo.getTransDateStr()), DateUtils.PATTERN_CLASSICAL_SIMPLE), DateUtils.PATTERN_CLASSICAL_SIMPLE));
                            }catch (Exception e){
                                vo.setTransDate(null);
                            }
                        }
                        if(vo.getTransDate() != null){
                            transDateStr = DateUtils.formatDate(vo.getTransDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                        }
                        vo.setTransDateStr(transDateStr);
                        if (StringUtils.isNotBlank(vo.getTransTimeStr())) {
                            //mm:ss
                            if (vo.getTransTimeStr().length() == 5 && vo.getTransTimeStr().indexOf(":") != -1) {
                                vo.setTransTimeStr(vo.getTransTimeStr()+":00");
                            }
                            if (vo.getTransTimeStr().length() <= 6) {
                                String pre = "";
                                for (int i= vo.getTransTimeStr().length(); i<6;i++) {
                                    pre += "0";
                                }
                                vo.setTransTimeStr(pre + vo.getTransTimeStr());
                                transTimeStr = vo.getTransTimeStr().substring(0, 2) + ":" + vo.getTransTimeStr().substring(2, 4) + ":" + vo.getTransTimeStr().substring(4, 6);
                                transTimeStr = DateUtils.formatDate(DateUtils.parse(transTimeStr, DateUtils.PATTERN_SHORTTIMESS), DateUtils.PATTERN_SHORTTIMESS);
                            } else if(vo.getTransTimeStr().length() == 8 || vo.getTransTimeStr().length() == 7) {
                                transTimeStr = vo.getTransTimeStr();
                            } else if (vo.getTransTimeStr().length() > 8) {
                                try {
                                    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                    String transTimeTemp = DateUtils.formatDate(df.parse(vo.getTransTimeStr()),DateUtils.PATTERN_SHORTTIMESS);
                                    if (StringUtils.isNotBlank(transTimeTemp)) {
                                        transTimeStr = transTimeTemp;
                                    }
                                }catch (Exception e){
                                    vo.setTransTimeStr(null);
                                }
                            } else {
                                msgList.add("ras.import.revenue.transdate.format.error");
                            }
                            vo.setTransTimeStr(transTimeStr);
                            transTime = DateUtils.parse(transDateStr + " " + transTimeStr, DateUtils.PATTERN_CLASSICAL);
                            vo.setTransTime(transTime);
                        }
                    }else{
                        vo.setTransTimeStr(vo.getTransTimeStr().replaceAll(BtBankAccConstant.SLASH, BtBankAccConstant.HORIZONTAL));
                        transTime = DateUtils.parse(vo.getTransTimeStr(), DateUtils.PATTERN_CLASSICAL);
                        if (transTime == null) {
                            transTime = DateUtils.parse(vo.getTransTimeStr(), DateUtils.PATTERN_CLASSICAL_NORMAL);
                        }
                        if (transTime == null) {
                            transTime = DateUtils.parse(vo.getTransTimeStr(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                        }
                        if(transTime == null){
                            try {
                                DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                                transTime = df.parse(vo.getTransTimeStr());
                                transTime = DateUtils.parse(DateUtils.formatDate(transTime, DateUtils.PATTERN_CLASSICAL), DateUtils.PATTERN_CLASSICAL);
                            }catch (Exception e){
                                transTime = null;
                            }
                        }
                        //无分隔符YYYYMMDD 00:00:00
                        if(transTime == null){
                            //厦门银行 YYYYMMDD
                            if(vo.getTransTimeStr().length() == 8){
                                String transDateStr = vo.getTransTimeStr().substring(0, 4) + "-" + vo.getTransTimeStr().substring(4, 6) + "-" + vo.getTransTimeStr().substring(6, 8) + " 00:00:00";
                                transTime = DateUtils.parse(transDateStr, DateUtils.PATTERN_CLASSICAL);
                            } else {
                                String transDateStr = vo.getTransTimeStr().substring(0, 4) + "-" + vo.getTransTimeStr().substring(4, 6) + "-" + vo.getTransTimeStr().substring(6, 8) + vo.getTransTimeStr().substring(8);
                                transTime = DateUtils.parse(transDateStr, DateUtils.PATTERN_CLASSICAL);
                            }
                        }
                    }
                    //31 Mar 2022 01:37 GMT+8
                    if(transTime == null){
                        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.PATTERN_CLASSICAL);
                        String transDateStr = sdf.format(new Date(vo.getTransTimeStr()));
                        transTime = DateUtils.parse(transDateStr, DateUtils.PATTERN_CLASSICAL);
                    }
                    if (transTime == null) {
                        msgList.add("nis.netbank.message.transtime.null");
                    }
                    vo.setTransTime(transTime);
                    vo.setTransTimeStr(DateUtils.formatDate(transTime, DateUtils.PATTERN_CLASSICAL));
                    if(vo.getTransTime() != null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(vo.getTransTime());
                        vo.setMonths(c.get(Calendar.MONTH) + 1);
                    }
                    //校验交易日期不能大于系统当前日期
                    if(vo.getTransTime() != null && vo.getTransTime().after(DateUtils.getDate())){
                        msgList.add("bt.bankaccdtl.message.transtime.lt.sysdate");
                    }
                    //明细截止时间校验
                    if (bankAccDTO != null && StringUtils.isNotBlank(bankAccDTO.getId()) && vo.getTransTime() != null) {
                        Date endDtlDate = this.btBankAccService.getLastEndDtlDate(bankAccDTO.getId(), vo.getCurId());
                        if (!EmptyUtils.isEmpty(endDtlDate)) {
                            if (!endDtlDate.before(vo.getTransTime())) {
                                msgList.add("bt.bankaccdtl.import.message.baldate.lt.completedate");
                            }
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }
    @Override
    public ImportCustomRules getImportCustomRules(Object... obj) {
        ImportCustomRules customRules = new ImportCustomRules();
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) obj[0];
        String suffix = String.valueOf(params.get("suffix"));//文件后缀名
        String[] bankType = (String[]) params.get("bankType");//银行类型
        String[] bankAcc = (String[]) params.get("bankAcc");//银行账户
        String[] curId = (String[]) params.get("curId");//银行账户对应的币种
        BisAccDtlImportConfigVO queryVo = new BisAccDtlImportConfigVO();
        queryVo.setFileType(suffix);
        queryVo.setBankTypeId(String.valueOf(bankType[0]));
        //银行类型为空
        if (StringUtils.isBlank(queryVo.getBankTypeId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKTYPENAME_NULL);
        }
        //获取模板信息
        BisAccDtlImportConfigVO bisAccDtlImportConfigVO = bisAccDtlImportConfigService.getByBankTypeIdAndFileType(bankType[0], suffix);
        if (null == bisAccDtlImportConfigVO) {
            throw new ParameterException(BankAccDtlMessageEnum.BISACCDTL_IMPORT_FILETYPE_NOT_CONFIGURED_EXIST);
        }
        //取配置行列数据
        //取配置行列数据
        List<Map<String, String>> ImportConfigList
                = "txt".equalsIgnoreCase(suffix) ? bisAccDtlImportConfigService.getTxtModelAnalysis(bisAccDtlImportConfigVO) : bisAccDtlImportConfigService.getTableInfo(queryVo);
        //存在列头
        if ("csv".equalsIgnoreCase(suffix)
                || "xls".equalsIgnoreCase(suffix)
                || "xlsx".equalsIgnoreCase(suffix)) {
            Map<String, String> coluMap = new HashMap<String, String>();
            for (Map<String, String> lineTopMap : ImportConfigList) {
                if (lineTopMap.get("value") != null) {
                    coluMap.put(lineTopMap.get("required").toString().equals("true")
                            ? "*" + lineTopMap.get("nameCNY").toString() : lineTopMap.get("nameCNY").toString(), lineTopMap.get("value"));
                }
            }
            int startRow = bisAccDtlImportConfigVO.getDtlStartLine();
            //设置起始行
            customRules.setStartRow(startRow);
            //设置无列标题
            customRules.setHead(false);
            //设置列标题
            customRules.setColumnPositio(coluMap);
        } else if ("txt".equalsIgnoreCase(suffix)) {
            int startRow = bisAccDtlImportConfigVO.getDtlStartLine();//起始行
            //设置起始行
            customRules.setStartRow(startRow);
            Map<String, Integer> separatorPositionMap = new HashMap<String, Integer>();
            for (Map<String, String> lineTopMap : ImportConfigList) {
                if (lineTopMap.get("value") != null) {
                    //不存在分隔符要拼接value
                    separatorPositionMap.put(lineTopMap.get("required").toString().equals("true")
                            ? "*" + lineTopMap.get("nameCNY").toString() : lineTopMap.get("nameCNY").toString(), Integer.valueOf(lineTopMap.get("value")));
                }
            }
            //设置无列标题
            customRules.setHead(false);
            //设置是否存在分隔符
            customRules.setSeparator(true);
            //设置分隔符
            customRules.setSeparator(bisAccDtlImportConfigVO.getDtlSeparator());
            //设置分隔符位置
            customRules.setSeparatorPosition(separatorPositionMap);
        } else {
            throw new ParameterException(BankAccDtlMessageEnum.BISACCDTL_IMPORT_FILETYPE_NOT_CONFIGURED_EXIST);
        }
        customRules.setSuffix(suffix);
        if(bankAcc !=null && EmptyUtils.isNotEmpty(bankAcc[0])) {
            customRules.setDataSources(String.valueOf(bankType[0])+CommonConstant.COMMA_SPLIT+bankAcc[0]+CommonConstant.COMMA_SPLIT+curId[0]);
        }else{
            customRules.setDataSources(String.valueOf(bankType[0]));
        }
        return customRules;
    }
}
