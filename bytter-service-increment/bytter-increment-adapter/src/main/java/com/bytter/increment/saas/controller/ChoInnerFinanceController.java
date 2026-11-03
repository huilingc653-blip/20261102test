package com.bytter.increment.saas.controller;

import com.bytter.biz.finacial.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import com.bytter.increment.domain.service.ChoInnerFinanceService;
import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import com.bytter.increment.domain.vo.v1.ChoInnerFinanceAddVO;
import com.bytter.increment.domain.vo.v1.ChoInnerFinanceViewPageVO;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.system.pricing.service.IPricingDepositRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
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
@Api(value = "/v1-increment/chowmatistic/choinnerfinance",tags = "内部理财信息表")
@RestController("v1ChoInnerFinanceController")
@RequestMapping("/v1-increment/chowmatistic/choinnerfinance")
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
     * 内部理财表详情页
     *
     * @return
     */
    @ApiOperation(value = "内部理财表详情页")
    @LogMessage(content = "查询内部理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{vouhcerNo}")
    public AjaxJson<ChoInnerFinanceViewPageVO> viewByVouhcerNo(@PathVariable("vouhcerNo") String vouhcerNo) {
        ChoInnerFinanceViewPageVO pageVO = new ChoInnerFinanceViewPageVO();
        ChoInnerFinanceVO choInnerFinance = choInnerFinanceService.getByBillNo(vouhcerNo);
        if(choInnerFinance==null){
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
        }
        if(StringUtils.isNotBlank(choInnerFinance.getRelatedBillId())) {
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            pageVO.setChoInnerFinanceRelatedPage(choInnerFinanceRelated);
        }
        pageVO.setChoInnerFinancePage(choInnerFinance);
        pageVO.setBusType(ChoMessageConstant.BUS_TYPE_INNER);
        return success(pageVO);
    }

    /**
     * 内部理财表 table列表
     *
     * @return
     */
    @ApiOperation(value = "内部理财表列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerFinanceService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 获取账号
     * @param corpId
     * @return
     */
    @GetMapping("/getBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId, String curId){
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
        return success(listAcc);
    }

    /**
     * 内部理财表新增页面跳转
     *
     * @return
     */
    @ApiOperation(value = "内部理财表新增页")
    @GetMapping("/add/{busType}")
    public AjaxJson<ChoInnerFinanceAddVO> add(@PathVariable("busType") Integer busType, String id) {
        ChoInnerFinanceAddVO vo = new ChoInnerFinanceAddVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoInnerFinanceVO choInnerFinance = choInnerFinanceService.getByIdUnion(id);
            vo.setChoInnerFinancePage(choInnerFinance);
        }
        vo.setBusType(busType);
        vo.setSysDate(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(vo, busType);
        return success(vo);
    }

    /**
     * 内部理财表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(value = "内部理财表编辑页")
    @GetMapping("/update")
    public AjaxJson<ChoInnerFinanceAddVO> update(ChoInnerFinanceVO choInnerFinance) {
        ChoInnerFinanceAddVO model = new ChoInnerFinanceAddVO();
        if (StringUtils.isNotEmpty(choInnerFinance.getId())) {
            ChoInnerFinanceVO vo = choInnerFinanceService.getByIdUnion(choInnerFinance.getId());
            model.setChoInnerFinancePage(vo);
            if(!InnerFinanceConstant.BUS_TYPE_C2T.equals(vo.getBusType())
                    && !InnerFinanceConstant.BUS_TYPE_C2N.equals(vo.getBusType()) ){
                //取款获取存款信息
                ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(vo.getRelatedBillId());
                //编辑时剩余取款金额需包含单据本身
                choInnerFinanceRelated.setTimeBalance(choInnerFinanceRelated.getTimeBalance().add(vo.getPayMoney()));
                //编辑时可预约金额需包含单据本身
                choInnerFinanceRelated.setAllowBookMoney(choInnerFinanceRelated.getAllowBookMoney().add(vo.getPayMoney()));
                model.setChoInnerFinanceRelatedPage(choInnerFinanceRelated);
            }
        }
        model.setSysDate(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(model, choInnerFinance.getBusType());
        return success(model);
    }

    /**
     * 内部理财表详情页
     *
     * @return
     */
    @ApiOperation(value = "内部理财表详情页")
    @LogMessage(content = "查询内部理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<ChoInnerFinanceViewPageVO> view(@PathVariable("id") String id) {
        ChoInnerFinanceViewPageVO pageVO = new ChoInnerFinanceViewPageVO();
        ChoInnerFinanceVO choInnerFinance = choInnerFinanceService.getByIdUnion(id);
        if(StringUtils.isNotBlank(choInnerFinance.getRelatedBillId())) {
            ChoInnerFinanceVO choInnerFinanceRelated = choInnerFinanceService.getByIdUnion(choInnerFinance.getRelatedBillId());
            pageVO.setChoInnerFinanceRelatedPage(choInnerFinanceRelated);
        }
        pageVO.setChoInnerFinancePage(choInnerFinance);
        pageVO.setBusType(ChoMessageConstant.BUS_TYPE_INNER);
        return success(pageVO);
    }

    /**
     * 添加内部理财表
     * @param choInnerFinance
     * @return
     */
    @ApiOperation(value = "添加内部理财表")
    @LogMessage(content = "添加内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ChoInnerFinanceVO choInnerFinance){
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
    @ApiOperation(value = "修改内部理财表")
    @LogMessage(content = "修改内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody ChoInnerFinanceVO choInnerFinance){
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
        return success();
    }

    /**
     * 删除内部理财表
     * @param id
     * @return
     */
    @ApiOperation(value = "删除内部理财表")
    @LogMessage(content = "删除内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id){
        choInnerFinanceService.delChoInnerFinance(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除内部理财表
     * @param idsDTO
     * @return
     */
    @ApiOperation(value = "批量删除内部理财表")
    @LogMessage(content = "批量删除内部理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody BisIDsDTO idsDTO){
        for(String id : idsDTO.getIds().split(CommonConstant.COMMA_SPLIT)){
            choInnerFinanceService.delChoInnerFinance(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 跳转至审核列表
     * @param busType	业务类型
     * @return
     */
    @ApiOperation(value = "内部理财表审核列表页")
    @GetMapping("/listApprove/{busType}")
    public AjaxJson<ChoInnerFinanceAddVO> listApprove(@PathVariable Integer busType, String flowStatus, String cycleTime){
        ChoInnerFinanceAddVO model = new ChoInnerFinanceAddVO();
        model.setBusType(busType);
        model.setFlowStatus(flowStatus);
        model.setCycleTime(cycleTime);
        return success(model);
    }

    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @ApiOperation(value = "加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.choInnerFinanceService.dataTableApprove(input));
    }


    /**
     * 跳转至手机审核详情界面
     * @param businessId
     * @param taskId
     * @return
     */

    /**
     * 存款类业务查询 table列表
     *
     * @return
     */
    @ApiOperation(value = "存款类业务查询列表")
    @PostMapping("/dataTableQuery")
    public AjaxJson<DataTablesOutput> dataTableQuery(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerFinanceService.getDataTablesQuery(input, this.getCurrentUser()));
    }

    /**
     * 内部存款应收未收利息统计表
     *
     * @return
     */
    @ApiOperation(value = "内部存款应收未收利息统计表")
    @PostMapping("/dataTableReport")
    public AjaxJson<DataTablesOutput> dataTableReport(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choInnerFinanceService.getDataTablesReport(input, this.getCurrentUser()));
    }

    /**
     * 选择存款信息
     * @return
     */
    @ApiOperation(value = "选择存款信息页")
    @GetMapping("/select")
    public AjaxJson<ChoInnerFinanceAddVO> select(String corpId, Integer busType){
        ChoInnerFinanceAddVO model = new ChoInnerFinanceAddVO();
        model.setBusType(busType);
        return success(model);
    }

    /**
     * 获取利率
     * @param depositRateType
     * @return
     */
    @ApiOperation(value = "获取利率")
    @GetMapping("/getInterestRate")
    public AjaxJson getInterestRate(String depositRateType, String interestStartDate, String curId){
        DepositRateDTO dto = new DepositRateDTO();
        dto.setTimeOfTakeEffect(DateUtils.parseDate(interestStartDate));
        dto.setMoneyType(curId);
        dto.setDepositRateType(depositRateType);
        dto = pricingDepositRateService.getDepositRateByParams(dto);
        return success(dto);
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
     * 初始化系统参数
     * @param vo
     * @param busType
     */
    private void initConfig(ChoInnerFinanceAddVO vo, Integer busType){
        if(BankFinanceConstant.BUS_TYPE_C2T.equals(busType)){
            //活期转定期
            vo.setRegularOnset(configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_REGULARONSET).getValue());
            vo.setRegularKeep(configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_REGULARKEEP).getValue());
        }else if(BankFinanceConstant.BUS_TYPE_C2N.equals(busType)){
            //活期转通知
            vo.setNoticeOnset(configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_NOTICEONSET).getValue());
            vo.setNoticeKeep(configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_NOTICEKEEP).getValue());
        }
    }
}
