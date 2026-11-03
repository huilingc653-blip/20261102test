package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.finacial.api.util.FinacialUtils;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.CheckMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.vo.FcAccbookImportVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("voucherImportService")
@Transactional
public class VoucherImportService implements IUserExcelVerifyHandler<FcAccbookImportVO> {
    @DubboReference(check = false)
    private ITallyService tallyService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    /**
     * 凭证导入列表
     **/
    public DataTablesOutput voucherImpDatatable(@RequestBody DataTablesInput dataTablesInput, String userId) {
        return tallyService.voucherImpDatatable(dataTablesInput, userId);
    }
    public Map<String, Map<String, FcBusRaceDTO>> groupBusRace(List<FcBusRaceDTO> list) {
        Map<String, Map<String, FcBusRaceDTO>> raceMap = new HashMap<>();
        for (FcBusRaceDTO raceDTO : list) {
            String netId = raceDTO.getNetId();
            String raceName = raceDTO.getRaceName();
            Map<String, FcBusRaceDTO> raceDTOMap = raceMap.get(netId);
            if (EmptyUtils.isEmpty(raceDTOMap)) {
                raceDTOMap = new HashMap<>();
                raceMap.put(netId, raceDTOMap);
            }
            raceDTOMap.put(raceName, raceDTO);
        }
        return raceMap;
    }
    /**
     * 功能介绍 : 导入确定<br>
     */
    public void impDataSave(List<FcAccbookImportVO> list, ICurrentUser user) {
        List<FcBusRaceDTO> fcBusRaceDTO = new ArrayList<>();
        fcBusRaceDTO = iFcBusRaceService.findAllRaceCode();
        Map<String, Map<String, FcBusRaceDTO>> raceMap = groupBusRace(fcBusRaceDTO);
        List<FcBusRaceDTO> defaultRaceList = iFcBusRaceService.findBySysRaceCode("9999");
        FcBusRaceDTO defaultRace = null;
        if (!EmptyUtils.isEmptyList(defaultRaceList)) {
            defaultRace = defaultRaceList.get(0);
        }
        List<FcAccbookImportDTO> importlist = new ArrayList<FcAccbookImportDTO>();
        int ss = 1;
        List<String> accountList = list.stream().map(FcAccbookImportVO::getAccount).collect(Collectors.toList());
        List<FcAccDTO> fcList = fcAccService.getFcAccByBlnctrAccs(accountList);//判断内部账号是否存在
        Map<String, FcAccDTO> accMap = fcList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        //验证
        for (FcAccbookImportVO vo : list) {
            if (!accMap.containsKey(vo.getAccount())) {
                //内部账号不存在
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_ACCOUNT_IS_NULL, vo.getAccount());
            }
            FcAccDTO fcAccDTO = accMap.get(vo.getAccount());
            vo.setRedblueSign("B");
            Calendar date = DateUtils.getCalendar(DateUtils.getDate().getTime());
            date.add(Calendar.SECOND, ss++);
            vo.setCreateDate(date.getTime());
            if (vo.getCreditMoney() == null) {
                vo.setCreditMoney(BigDecimal.ZERO);
            }
            if (vo.getDebitMoney() == null) {
                vo.setDebitMoney(BigDecimal.ZERO);
            }
            FcVoucherTypeDTO dto = null;
            if (EmptyUtils.isNotEmpty(vo.getVoucherType())) {
                dto = iFcVoucherTypeService.getDtoByVoucherName(vo.getVoucherType());
            }
            if (dto == null || EmptyUtils.isEmpty(dto.getId())) {
                dto = iFcVoucherTypeService.getDtoByVoucherName("其他");
            }
            vo.setVoucherType(dto.getId());//凭证种类
            Map<String, FcBusRaceDTO> raceDTOMap = raceMap.get(fcAccDTO.getNetId());
            if (EmptyUtils.isEmpty(raceDTOMap)) {
                vo.setBusRaceId(defaultRace.getId());
            } else {
                String raceName = vo.getBusRaceId();
                FcBusRaceDTO busRaceDTO = raceDTOMap.get(raceName);
                if (EmptyUtils.isEmpty(busRaceDTO)) {
                    vo.setBusRaceId(defaultRace.getId());
                } else {
                    vo.setBusRaceId(busRaceDTO.getId());
                }
            }
            if (StringUtils.isBlank(vo.getBusGlobalId())) {//生成全局ID
                String busGlobalId = FinacialUtils.createGlobalId();
                vo.setBusGlobalId(busGlobalId);
            }
            vo.setOperatorCode(user.getId());//操作人
            FcAccbookImportDTO accbookImport = new FcAccbookImportDTO();
            BeanUtils.copyProperties(vo, accbookImport);
            importlist.add(accbookImport);
        }
        tallyService.batchSaveFcAccBookImport(importlist);
    }
    /**
     * 功能介绍 : 提交凭证<br>
     */
    @Transactional(readOnly = false)
    public void submit(List<String> ids) {
        List<FcAccbookImportDTO> accbookImports = tallyService.getImprotVoucher(ids);//先从中间表中查出数据
        Map<Integer, List<FcAccbookImportDTO>> importList = groupByImprotData(accbookImports);//k-v
        int listSize = importList.size();//map长度
        for (int i = 0; i < listSize; i++) {
            List<FcAccbookImportDTO> importVOS = importList.get(i + 1);//k-v
            if (importVOS != null && !importVOS.isEmpty()) {
                //凭证数据
                FcAccbookDTO accbook = new FcAccbookDTO();
                FcAccbookDtlDTO accbookDtl = null;
                FcAccbookExtDTO accbookExt = null;
                ArrayList<FcAccbookDtlDTO> listDtl = new ArrayList<FcAccbookDtlDTO>();
                Date tallyDate = null;
                Date interestDate = null;
                String busRaceId = null;
                boolean flag = true;
                List<String> accountList = importVOS.stream().map(FcAccbookImportDTO::getAccount).collect(Collectors.toList());
                List<FcAccDTO> fcList = fcAccService.getFcAccByBlnctrAccs(accountList);//判断内部账号是否存在
                Map<String, FcAccDTO> accMap = fcList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
                for (int j = 0; j < importVOS.size(); j++) {
                    accbookDtl = new FcAccbookDtlDTO();//dtl
                    FcAccbookImportDTO importData = importVOS.get(j);
                    FcAccDTO fcAccDTO = accMap.get(importData.getAccount());//判断内部账号是否存在
                    int lineNo = importData.getLineNo().intValue();
                    if (j != 0 && lineNo == 1 && flag) {
                        FcAccbookVoucherDTO ffa = new FcAccbookVoucherDTO();
                        accbook = new FcAccbookDTO();
                        accbook.setTallyDate(tallyDate);
                        accbook.setInterestStartDate(interestDate);
                        accbook.setVoucherType(importData.getVoucherType());
                        accbook.setBusRaceId(importData.getBusRaceId());
                        accbook.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_GENERAL.get());//是否加急
                        accbook.setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
                        accbook.setRmk(importData.getRmk());
                        accbook.setAffixAmount(importData.getAffixAmount());
                        accbook.setOperatorCode(importData.getOperatorCode());
                        ffa.setBookDtls(listDtl);
                        accbookExt = new FcAccbookExtDTO();
                        accbookExt.setExternalCorp(importData.getExternalCorp());
                        accbookExt.setExternalAcc(importData.getExternalAcc());
                        accbookExt.setExternalBank(importData.getExternalBank());
                        accbookExt.setExternalType("1");
                        accbookExt.setTypeSign("1");
                        ffa.setBookExt(accbookExt);
                        //凭证包装类
                        VoucherWrapperDTO voucherWrapper = new VoucherWrapperDTO();
                        //自动复核
                        voucherWrapper.setAutoCheck(true);
                        //柜台记账
                        voucherWrapper.setOriType(FinaBookConstant.BusOriType.TAILLY.get());//手工记账
                        voucherWrapper.setProcessType(ProcessType.PROCESS_COMMON);//正常记账
                        voucherWrapper.setUserCode(accbookImports.get(j).getOperatorCode());
                        List<FcAccbookVoucherDTO> target = voucherWrapper.getTarget();
                        ffa.setAccbook(accbook);
                        target.add(ffa);
                        //提交凭证
                        iFcAccbookService.saveVoucher(voucherWrapper);
                        flag = false;
                    }
                    if (lineNo != 1) {
                        flag = true;
                    }
                    tallyDate = importData.getTallyDate();
                    interestDate = importData.getInterestStartDate();
                    busRaceId = importData.getBusRaceId();
                    accbookDtl.setSummaryContent(importData.getSummaryContent());
                    accbookDtl.setAccount(importData.getAccount());
                    accbookDtl.setAccName(fcAccDTO.getAccName());
                    accbookDtl.setDebitMoney(importData.getDebitMoney());
                    accbookDtl.setCreditMoney(importData.getCreditMoney());
                    accbookDtl.setVoucherType(importData.getVoucherType());
                    accbookDtl.setLineNo(importData.getLineNo().intValue());
                    listDtl.add(accbookDtl);
                    if (j == importVOS.size() - 1) {
                        FcAccbookVoucherDTO ffa = new FcAccbookVoucherDTO();
                        accbook = new FcAccbookDTO();
                        accbook.setTallyDate(tallyDate);
                        accbook.setInterestStartDate(interestDate);
                        accbook.setVoucherType(importData.getVoucherType());
                        accbook.setBusRaceId(importData.getBusRaceId());
                        accbook.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_GENERAL.get());//是否加急
                        accbook.setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
                        accbook.setRmk(importData.getRmk());
                        accbook.setAffixAmount(importData.getAffixAmount());
                        accbook.setOperatorCode(importData.getOperatorCode());
                        ffa.setBookDtls(listDtl);
                        accbookExt = new FcAccbookExtDTO();
                        accbookExt.setExternalCorp(importData.getExternalCorp());
                        accbookExt.setExternalAcc(importData.getExternalAcc());
                        accbookExt.setExternalBank(importData.getExternalBank());
                        accbookExt.setExternalType("1");
                        accbookExt.setTypeSign("1");
                        ffa.setBookExt(accbookExt);
                        //凭证包装类
                        VoucherWrapperDTO voucherWrapper = new VoucherWrapperDTO();
                        //自动复核
                        voucherWrapper.setAutoCheck(false);
                        voucherWrapper.setAutoAccounter(false);
                        //柜台记账
                        voucherWrapper.setOriType(FinaBookConstant.BusOriType.TAILLY.get());
                        voucherWrapper.setProcessType(ProcessType.PROCESS_COMMON);
                        voucherWrapper.setUserCode(accbookImports.get(j).getOperatorCode());
                        List<FcAccbookVoucherDTO> target = voucherWrapper.getTarget();
                        ffa.setAccbook(accbook);
                        target.add(ffa);
                        //提交凭证
                        iFcAccbookService.saveVoucher(voucherWrapper);
                    }
                }
            }
        }
        //删除已提交的数据
        tallyService.deleteImportVoucher(ids);
    }
    /**
     * 功能介绍 :删除凭证 <br>
     */
    public void batchDelete(List<String> ids) {
        tallyService.deleteImportVoucher(ids);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(FcAccbookImportVO obj, Object... user) {
        return null;
    }
    @Override
    public void verifyHandler(List<FcAccbookImportVO> list, Object... user) {
        List<FcBusRaceDTO> fcBusRaceDTO = iFcBusRaceService.findAllRaceCode();
        Map<String, Map<String, FcBusRaceDTO>> raceMap = groupBusRace(fcBusRaceDTO);
        BigDecimal debitMoney = BigDecimal.ZERO;
        BigDecimal creditMoney = BigDecimal.ZERO;
        String curId = null;
        List<String> accountList = list.stream().map(FcAccbookImportVO::getAccount).collect(Collectors.toList());
        List<FcAccDTO> fcList = fcAccService.getFcAccByBlnctrAccs(accountList);//判断内部账号是否存在
        Map<String, FcAccDTO> accMap = fcList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        for (int i = 0; i < list.size(); i++) {
            FcAccbookImportVO importVO = list.get(i);
            List<String> msgList = new ArrayList<>();
            if (null == importVO.getDebitMoney()) {
                importVO.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == importVO.getCreditMoney()) {
                importVO.setCreditMoney(BigDecimal.ZERO);
            }
            if (importVO.getTallyDate() == null) {
                msgList.add("fin.check.centraliydatamanager.tallydate.empty");
            }
            if (importVO.getInterestStartDate() == null) {
                msgList.add("fin.book.tally.intereststartdate.no");//起息日期不能为空
            }
            if (EmptyUtils.isEmpty(importVO.getSummaryContent())) {
                msgList.add("fin.book.tally.summarycontent.no");//摘要不能为空
            }
            if (EmptyUtils.isEmpty(importVO.getLineNo())) {
                msgList.add("bt.bankaccdtl.message.orderno.null");//序号不能为空
            }
            if (EmptyUtils.isEmpty(importVO.getAccount())) {
                msgList.add("fin.collect.manual.payaccount.isnull");//内部账号不能为空
            }
            if (EmptyUtils.isEmpty(importVO.getRmk())) {
                msgList.add("fin.book.tally.rmk.no");//备注不能为空
            }
            if (!bigDecimalIsZero(importVO.getCreditMoney()) && !bigDecimalIsZero(list.get(i).getDebitMoney())) {
                msgList.add("fin.check.centraliydatamanager.de.cr.notzero");
            }
            if (bigDecimalIsZero(importVO.getCreditMoney()) && bigDecimalIsZero(list.get(i).getDebitMoney())) {
                msgList.add("fin.check.centraliydatamanager.de.cr.zero");
            }
            ICurrentUser userInfo = (ICurrentUser) user[0];
            Map<String, FcBusRaceDTO> raceDTOMap = raceMap.get(userInfo.getCorpId());
            if (EmptyUtils.isEmpty(raceDTOMap)) {
                List<FcBusRaceDTO> raceDTOS = iFcBusRaceService.findByNetIdSysRaceCode("9999", userInfo.getCorpId());
                if (raceDTOS.size() < 1) {
                    msgList.add("fin.book.tally.user.is.not.busrace");//当前用户没有业务种类:其它使用权限，请先去授权
                }
            }
            String raceName = importVO.getBusRaceId();
            if (EmptyUtils.isNotEmpty(raceName)) {
                FcBusRaceDTO busRaceDTO = raceDTOMap.get(raceName);
                if (EmptyUtils.isEmpty(busRaceDTO)) {
                    msgList.add("fin.book.tally.user.has.no.busrace");//当前用户没有业务种类:其它使用权限，请先去授权
                }
            } else {
                List<FcBusRaceDTO> raceDTOS = iFcBusRaceService.findByNetIdSysRaceCode("9999", userInfo.getCorpId());
                if (raceDTOS.size() < 1) {
                    msgList.add("fin.book.tally.user.is.not.busrace");//当前用户没有业务种类:其它使用权限，请先去授权
                }
            }
            FcAccDTO fcAccDTO = null;
            if (!accMap.containsKey(importVO.getAccount())) {
                msgList.add("nis.netbank.message.fcacc.null");//内部账号不存在，请重新输入
            } else {
                fcAccDTO = accMap.get(importVO.getAccount());
                Boolean isOk = iAccountGrantService.checkPermissionByUserAndAccount(userInfo.getId(), fcAccDTO.getBlnctrAcc(), "1", "t",
                        FinacialResourceConstant.FINACIAL_VOUCHERIMPORT_APPLY);
                if (isOk) {
                    msgList.add("fin.book.tally.account.is.not.permission");//当前用户没有内部账号使用权限，请先去授权
                }
                if (InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAccDTO.getAccState())) {
                    msgList.add("fin.book.tally.account.has.destory");//该内部账号已注销
                }
                //起息日期大于账号启用日期
                if (importVO.getInterestStartDate().compareTo(fcAccDTO.getAccStartDate()) == -1) {
                    msgList.add("InterestStartDate.gt.account.startDate");//起息日期必须大于账号启用日期,请重新输入
                }
                //记账日期必须大于账号启用日期
                if (importVO.getTallyDate().compareTo(fcAccDTO.getAccStartDate()) == -1) {
                    msgList.add("fin.outbook.tally.tallydate.le.startdate");//记账日期必须大于账号启用日期,请重新输入
                }
                //记账日期必须比已日结日期晚
                Date closeDay = iFcDayCloseService.getDayCloseFinalDate(fcAccDTO.getNetId());
                if (importVO.getTallyDate().compareTo(closeDay) == -1) {
                    msgList.add("tallydate.gt.closedate");//记账日期必须比已日结日期晚,请重新输入
                }
                curId = fcAccDTO.getCurId();
            }
            //判断借贷是否平衡
//            if (importVO.getLineNo().compareTo(BigDecimal.ONE) == 0) {
//                debitMoney = debitMoney.add(importVO.getDebitMoney());
//            } else {
                if (fcAccDTO != null) {
                    if (!curId.equals(fcAccDTO.getCurId())) {
                        msgList.add("account.eq.curid");//内部账号币别必须相同
                    }
                }
                if (importVO.getDebitMoney().compareTo(BigDecimal.ZERO) == 0) {
                    creditMoney = creditMoney.add(importVO.getCreditMoney());
                } else {
                    debitMoney = debitMoney.add(importVO.getDebitMoney());
                }
                if (i == list.size() - 1) {
                    if (debitMoney.compareTo(creditMoney) != 0) {
                        msgList.add("fin.book.tally.money.balance");//借贷不平衡,请重新输入
                    }
                    debitMoney = BigDecimal.ZERO;
                    creditMoney = BigDecimal.ZERO;
                } else if (list.get(i + 1).getLineNo().compareTo(BigDecimal.ONE) == 0) {
                    if (debitMoney.compareTo(creditMoney) != 0) {
                        msgList.add("fin.book.tally.money.balance");//借贷不平衡,请重新输入
                    }
                    debitMoney = BigDecimal.ZERO;
                    creditMoney = BigDecimal.ZERO;
                }
//            }
            list.get(i).addErrorMsg(msgList);
        }
    }
    /**
     * 功能介绍 : 判断BigDecimal是否为null或者为0<br>
     *
     * @return 为null或者0 ：true
     */
    @Transactional(readOnly = true)
    public boolean bigDecimalIsZero(BigDecimal data) {
        return data == null || data.compareTo(BigDecimal.ZERO) == 0;
    }
    private Map<Integer, List<FcAccbookImportDTO>> groupByImprotData(List<FcAccbookImportDTO> list) {
        List<FcAccbookImportDTO> res = null;
        BigDecimal num = BigDecimal.ZERO;
        Map<Integer, List<FcAccbookImportDTO>> map = new HashMap<Integer, List<FcAccbookImportDTO>>();
        if (list.size() == 1) {
            throw new ParameterException(CollectMessageEnum.NETBANK_MESSAGE_CONFIRM_SELECTDATA);
        }
        for (int i = 0; i < list.size(); i++) {
            FcAccbookImportDTO vo = list.get(i);
            if (list.get(i).getLineNo().compareTo(BigDecimal.ONE) == 1) {//是大于序列1
                if (EmptyUtils.isNotEmpty(res)) {
                    res.add(vo);
                    if (i == list.size() - 1) {
                        map.put(num.intValue(), res);
                    } else if (list.get(i + 1).getLineNo().compareTo(BigDecimal.ONE) == 0) {
                        map.put(num.intValue(), res);
                    }
                }
            } else {
                res = new ArrayList<FcAccbookImportDTO>();
                num = num.add(BigDecimal.ONE);
                res.add(vo);
            }
        }
        return map;
    }
}
