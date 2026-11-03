package com.bytter.increment.web.controller;

import com.bytter.biz.finacial.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.domain.entity.ChoInnerFinance;
import com.bytter.increment.domain.service.ChoInnerFinanceService;
import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/chowmatistic/choinnerfinance")
public class ChoInnerFinanceController extends BaseController {
    @Resource
    private ChoInnerFinanceService choInnerFinanceService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IPricingDepositRateService pricingDepositRateService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService iFcOnlineLedgerService;

    /**
     * 内部理财表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询内部理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{busType}")
    public String list(@PathVariable("busType") Integer busType, Model model) {
        model.addAttribute("busType", busType);
        return "chowmatistic/listChoInnerFinance" + this.getForwardByBusType(busType);
    }
    /**
     * 根据业务类型返回不同的跳转页面
     * @param busType
     * @return
     */
    public String getForwardByBusType(Integer busType){
        /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
        String forward = "";
        if(InnerFinanceConstant.BUS_TYPE_C2T.equals(busType)){
            forward = "Current2Time";
        }else if(InnerFinanceConstant.BUS_TYPE_T2C.equals(busType)){
            forward = "Time2Current";
        }else if(InnerFinanceConstant.BUS_TYPE_C2N.equals(busType)){
            forward = "Current2Notice";
        }else if(InnerFinanceConstant.BUS_TYPE_N2C.equals(busType)){
            forward = "Notice2Current";
        }else if(InnerFinanceConstant.BUS_TYPE_N2B.equals(busType)){
            forward = "Notice2Book";
        }else if(InnerFinanceConstant.BUS_TYPE_B2N.equals(busType)){
            forward = "Book2Notice";
        }
        return forward;
    }
    /**
     * 内部理财表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return choInnerFinanceService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 获取账号
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId, String curId){
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        List<FcAccDTO> listFcAcc = accountGrantService.listInnerAccount(this.getUserId(),corpId,AccountGrantConstant.TRANSFER_ACCOUNT);
        if (!EmptyUtils.isEmptyList(listFcAcc)) {
            for (FcAccDTO fcAcc : listFcAcc) {
                //过滤企业活期账号
                if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                    if(StringUtils.isBlank(curId) || (StringUtils.isNotBlank(curId) && curId.equals(fcAcc.getCurId()))){
                        BtBankAccDTO bankAcc = new BtBankAccDTO();
                        bankAcc.setId(fcAcc.getId());
                        bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                        bankAcc.setAccName(fcAcc.getAccName());
                        bankAcc.setCurId(fcAcc.getCurId());
                        bankAcc.setCurName(fcAcc.getCurName());
                        bankAcc.setBankName(fcAcc.getNetName());
                        //查询账号余额
                        FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
                        // fl.check_balance - fl.freeze_money as fcBalance,
                        bankAcc.setBalance(onlineLedgerDTO.getCheckBalance().subtract(onlineLedgerDTO.getFreezeMoney()));
                        bankAcc.setInterestRate(fcAcc.getInterestRate());
                        listAcc.add(bankAcc);
                    }
                }
            }
        }
        return listAcc;
    }
    /**
     * 内部理财表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add/{busType}")
    public String add(@PathVariable("busType") Integer busType, ChoInnerFinance choInnerFinance, Model model) {
        if (StringUtils.isNotEmpty(choInnerFinance.getId())) {
            choInnerFinance = choInnerFinanceService.get(choInnerFinance.getId());
            model.addAttribute("choInnerFinancePage", choInnerFinance);
        }
        model.addAttribute("busType", busType);
        model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(model, busType);
        return "chowmatistic/addChoInnerFinance" + this.getForwardByBusType(busType);
    }
    /**
     * 初始化系统参数
     * @param model
     * @param busType
     */
    private void initConfig(Model model, Integer busType){
        if(BankFinanceConstant.BUS_TYPE_C2T.equals(busType)){
            //活期转定期
            model.addAttribute("regularOnset", configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_REGULARONSET).getValue());
            model.addAttribute("regularKeep", configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_REGULARKEEP).getValue());
        }else if(BankFinanceConstant.BUS_TYPE_C2N.equals(busType)){
            //活期转通知
            model.addAttribute("noticeOnset", configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_NOTICEONSET).getValue());
            model.addAttribute("noticeKeep", configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_NOTICEKEEP).getValue());
        }
    }
    /**
     * 内部理财表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(ChoInnerFinanceVO choInnerFinance, Model model) {
        if (StringUtils.isNotEmpty(choInnerFinance.getId())) {
            choInnerFinance = choInnerFinanceService.getByIdUnion(choInnerFinance.getId());
            model.addAttribute("choInnerFinancePage", choInnerFinance);
            if(!InnerFinanceConstant.BUS_TYPE_C2T.equals(choInnerFinance.getBusType())
                    && !InnerFinanceConstant.BUS_TYPE_C2N.equals(choInnerFinance.getBusType()) ){
                //取款获取存款信息
                ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
                //编辑时剩余取款金额需包含单据本身
                choInnerFinanceRelated.setTimeBalance(choInnerFinanceRelated.getTimeBalance().add(choInnerFinance.getPayMoney()));
                //编辑时可预约金额需包含单据本身
                choInnerFinanceRelated.setAllowBookMoney(choInnerFinanceRelated.getAllowBookMoney().add(choInnerFinance.getPayMoney()));
                model.addAttribute("choInnerFinanceRelatedPage", choInnerFinanceRelated);
            }
        }
        model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(model, choInnerFinance.getBusType());
        return "chowmatistic/updateChoInnerFinance" + this.getForwardByBusType(choInnerFinance.getBusType());
    }
    /**
     * 内部理财表详情页
     *
     * @return
     */
    @LogMessage(content = "查询内部理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        ChoInnerFinanceVO choInnerFinance = choInnerFinanceService.getByIdUnion(id);
        if(StringUtils.isNotBlank(choInnerFinance.getRelatedBillId())) {
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            model.addAttribute("choInnerFinanceRelatedPage", choInnerFinanceRelated);
        }
        model.addAttribute("choInnerFinancePage", choInnerFinance);
        model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_INNER);
        return "chowmatistic/viewChoInnerFinance";
    }
    /**
     * 内部理财表详情页
     *
     * @return
     */
    @LogMessage(content = "查询内部理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{vouhcerNo}")
    public String viewByVouhcerNo(@PathVariable("vouhcerNo") String vouhcerNo, Model model) {
        ChoInnerFinanceVO choInnerFinance = choInnerFinanceService.getByBillNo(vouhcerNo);
        if(choInnerFinance==null){
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
        }
        if(StringUtils.isNotBlank(choInnerFinance.getRelatedBillId())) {
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            model.addAttribute("choInnerFinanceRelatedPage", choInnerFinanceRelated);
        }
        model.addAttribute("choInnerFinancePage", choInnerFinance);
        model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_INNER);
        return "chowmatistic/viewChoInnerFinance";
    }
    
    /**
     * 添加内部理财表
     * @param choInnerFinance
     * @return
     */
    @LogMessage(content = "添加内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoInnerFinanceVO choInnerFinance){
        if(InnerFinanceConstant.BUS_TYPE_N2B.equals(choInnerFinance.getBusType())){
            //通知预约校验预约金额不允许超过可预约金额
            //取款获取存款信息
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            CorpDTO corpDTO = corpService.get(choInnerFinance.getCorpId());//单位信息
            //剩余预约金额=剩余取款金额-已预约金额
            choInnerFinanceRelated.setAllowBookMoney(choInnerFinanceRelated.getTimeBalance().subtract(choInnerFinanceRelated.getBookMoney()));
            if(choInnerFinanceRelated.getAllowBookMoney().compareTo(choInnerFinance.getPayMoney())<0){
                throw new ParameterException(ChoInnerFinanceMessageEnum.CHO_INNERFINANCE_MESSAGE_BOOKMONEY_EXCEED);
            }
            choInnerFinance.setNetId(corpDTO.getNetId());
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.equals(choInnerFinance.getBusType()) ||
                InnerFinanceConstant.BUS_TYPE_N2C.equals(choInnerFinance.getBusType())){
            Date payDate = choInnerFinance.getPayDate();//支取日期
            CorpDTO corpDTO = corpService.get(choInnerFinance.getCorpId());//单位信息
            Date closeDate = dayCloseService.getDayCloseFinalDate(corpDTO.getNetId());//网点日结日期
            choInnerFinance.setNetId(corpDTO.getNetId());
            if (DateUtils.dayDiffs(payDate, closeDate) <= 0) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.CHO_INNERFINANCE_MESSAGE_BOOKDATE_OVER);//支取日期大于日结日期
            }
        } else {
            CorpDTO corpDTO = corpService.get(choInnerFinance.getCorpId());//单位信息
            choInnerFinance.setNetId(corpDTO.getNetId());
        }
        choInnerFinanceService.saveInnerFinance(choInnerFinance, this.getCurrentUser());
        return success();
    }
    /**
     * 修改内部理财表
     * @param choInnerFinance
     * @return
     */
    @LogMessage(content = "修改内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(ChoInnerFinanceVO choInnerFinance){
        if(InnerFinanceConstant.BUS_TYPE_N2B.equals(choInnerFinance.getBusType())){
            //通知预约校验预约金额不允许超过可预约金额
            //取款获取存款信息
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            ChoInnerFinanceVO choInnerFinanceCur = choInnerFinanceService.getByIdUnion(choInnerFinance.getId());
            //剩余预约金额=剩余取款金额-已预约金额（编辑时需扣减本单据的预约金额）
            choInnerFinanceRelated.setAllowBookMoney(choInnerFinanceRelated.getTimeBalance().subtract(choInnerFinanceRelated.getBookMoney()).add(choInnerFinanceCur.getPayMoney()));
            if(choInnerFinanceRelated.getAllowBookMoney().compareTo(choInnerFinance.getPayMoney())<0){
                throw new ParameterException(ChoInnerFinanceMessageEnum.CHO_INNERFINANCE_MESSAGE_BOOKMONEY_EXCEED);
            }
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.equals(choInnerFinance.getBusType()) ||
                InnerFinanceConstant.BUS_TYPE_N2C.equals(choInnerFinance.getBusType())){
            Date payDate = choInnerFinance.getPayDate();//支取日期
            CorpDTO corpDTO = corpService.get(choInnerFinance.getCorpId());//单位信息
            Date closeDate = dayCloseService.getDayCloseFinalDate(corpDTO.getNetId());//网点日结日期
            if (DateUtils.dayDiffs(payDate, closeDate) <= 0) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.CHO_INNERFINANCE_MESSAGE_BOOKDATE_OVER);//支取日期大于日结日期
            }
        }
        choInnerFinanceService.updateInnerFinance(choInnerFinance, this.getCurrentUser());
        return  success();
    }
    /**
     * 删除内部理财表
     * @param id
     * @return
     */
    @LogMessage(content = "删除内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        choInnerFinanceService.delChoInnerFinance(id, this.getCurrentUser());
        return success();
    }
    /**
     * 批量删除内部理财表
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids")String ids){
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
            choInnerFinanceService.delChoInnerFinance(id, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 跳转至审核列表
     * @param busType	业务类型
     * @return
     */
    @GetMapping("/listApprove/{busType}")
    public String listApprove(@PathVariable Integer busType, String flowStatus, String cycleTime, Model model){
        model.addAttribute("busType", busType);
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "chowmatistic/listChoInnerFinance" + this.getForwardByBusType(busType) + "Approve";
    }
    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody  DataTablesInput dataTablesInput){
        return this.choInnerFinanceService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        ChoInnerFinanceVO vo = new ChoInnerFinanceVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.choInnerFinanceService.getByIdUnion(id);
            model.addAttribute("choInnerFinancePage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_INNER);
            if(StringUtils.isNotBlank(vo.getRelatedBillId())) {
                ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(vo.getRelatedBillId());
                model.addAttribute("choInnerFinanceRelatedPage", choInnerFinanceRelated);
            }
        }
        return "chowmatistic/approveChoInnerfinance";
    }
    /**
     * 跳转至手机审核详情界面
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            ChoInnerFinanceVO vo = this.choInnerFinanceService.getByIdUnion(businessId);
            model.addAttribute("choInnerFinancePage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_INNER);
        }
        return "chowmatistic/viewApproveChoInnerFinance";
    }
    /**
     * 存款类业务查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询内部理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listQuery")
    public String listQuery(Model model) {
        return "chowmatistic/listChoInnerFinanceQuery";
    }
    /**
     * 存款类业务查询 table列表
     *
     * @return
     */
    @PostMapping("/dataTableQuery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody  DataTablesInput dataTablesInput){
        return choInnerFinanceService.getDataTablesQuery(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 内部存款应收未收利息统计表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "内部存款应收未收利息统计表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listReport")
    public String listReport(Model model) {
        return "chowmatistic/listChoInnerFinanceReport";
    }
    /**
     * 内部存款应收未收利息统计表
     *
     * @return
     */
    @PostMapping("/dataTableReport")
    @ResponseBody
    public DataTablesOutput dataTableReport(@RequestBody  DataTablesInput dataTablesInput){
        return choInnerFinanceService.getDataTablesReport(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 选择存款信息
     * @return
     */
    @GetMapping("/select")
    public String select(String corpId, Integer busType, Model model){
        if(StringUtils.isEmpty(corpId)){
            corpId = "--";
        }
        model.addAttribute("corpId", corpId);
        model.addAttribute("busType", busType);
        return "chowmatistic/selectChoInnerFinance";
    }
    /**
     * 获取利率
     * @param depositRateType
     * @return
     */
    @RequestMapping(params = "getInterestRate")
    @ResponseBody
    public AjaxJson getInterestRate(String depositRateType,String interestStartDate, String curId){
        DepositRateDTO dto = new DepositRateDTO();
        dto.setTimeOfTakeEffect(DateUtils.parseDate(interestStartDate));
        dto.setMoneyType(curId);
        dto.setDepositRateType(depositRateType);
        dto = pricingDepositRateService.getDepositRateByParams(dto);
        AjaxJson j=new AjaxJson();
        j.setSuccess(true);
        j.setObj(dto);
        return j;
    }
}
