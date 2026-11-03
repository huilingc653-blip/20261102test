package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.service.FexCotpBankAccService;
import com.bytter.biz.foreign.service.FexCotpService;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexRateTypeService;
import com.bytter.biz.foreign.service.FexTranAppService;
import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexTranAppDtlTmpVO;
import com.bytter.biz.foreign.vo.FexTranAppDtlVO;
import com.bytter.biz.foreign.vo.FexTranAppVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyPairService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/foreign/fexTranApp")
public class FexTranAppController extends BaseController {

    @Autowired
    private FexProductService fexProductService;

    @Autowired
    private FexTranAppService fexTranAppService;

    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FexCotpService fexCotpService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBtCurrencyPairService btCurrencyPairService;
    @Autowired
    private FexRateTypeService fexRateTypeService;
    @Autowired
    private FexCotpBankAccService fexCotpBankAccService;

    @DubboReference(check = false)
    private IConfigService configService;

    @Autowired(required=false)
    private  FexTranConfigTemplateService fexTranConfigTemplateService;

    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    /**
     * 交易需求列表
     * @return
     */
    @GetMapping("/listTranApp")
    public String listTranApp(){
        return "fexTranApp/listFexTranApp";
    }

    /**
     * 交易需求查询列表
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXTRANAPP_QUERY)
    @GetMapping("/listTranAppQuery")
    public String listTranAppQuery(){
        return "fexTranApp/listFexTranAppQuery";
    }

    /**
     * 交易需求审批列表
     * @return
     */
    @GetMapping("/listTranAppApprove")
    public String listTranAppApprove(String flowStatus, String cycleTime,Model model){
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "fexTranApp/listFexTranAppApprove";
    }

    /**
     * 新增交易需求
     * @return
     */
    @GetMapping("/add")
    public String add(){
        return "fexTranApp/addFexTranApp";
    }

    /**
     * 编辑交易需求
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id,Model model){
        model.addAttribute("fexTranAppVo", fexTranAppService.getFexTranAppVOById(id));
        return "fexTranApp/editFexTranApp";
    }

    /**
     * 查看交易需求
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "fexTranApp/viewFexTranAppApprove";
    }

    /**
     * 交易需求查看
     * @param id
     * @param flag
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String view(String id, @RequestParam(required = false,defaultValue = "1") String flag, Model model) {
        model.addAttribute("fexTranAppVo", fexTranAppService.getFexTranAppVOById(id));
        model.addAttribute("showClosBtn", flag);
        return "fexTranApp/viewFexTranApp";
    }

    /**
     * 交易需求明细查看
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewDtl")
    public String view(String id, Model model) {
        FexTranAppDtlVO fexTranAppDtlVO = fexTranAppService.getById(id);
        if (StringUtils.isNotEmpty(fexTranAppDtlVO.getCotpAccId())) {
            model.addAttribute("cotpBankAcc", fexCotpBankAccService.getFexFexCotpBankAcc(fexTranAppDtlVO.getCotpAccId()));
        }
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getCorpId())){
            CorpDTO corpInfo=   corpService.get(fexTranAppDtlVO.getCorpId());
            if(corpInfo!=null){
                fexTranAppDtlVO.setCorpName(corpInfo.getName());
            }
        }
        String tranType = fexTranAppDtlVO.getTranType();
        String isSwaptTran = fexTranAppDtlVO.getIsSwaptTran();
        String page = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
//            外汇买卖
            if (CommonConstant.YES.equals(isSwaptTran)) {
                //                掉期交易
                page = "viewTranReqDtl2";
            } else {
//                非掉期交易
                page = "viewTranReqDtl1";
            }
        } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
//            资金收付
            page = "viewTranReqDtl10";

            if(StringUtils.isNotEmpty(fexTranAppDtlVO.getTemplateId())){
                FexTranTemplateConfigVO tranTemplateConfigVO=   fexTranConfigTemplateService.getTemplateConfigById(fexTranAppDtlVO.getTemplateId());
                if(tranTemplateConfigVO!=null){
                    model.addAttribute("template", tranTemplateConfigVO);
                }
            }
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            page = "viewTranReqDtl4";
        } else if (ForeignConstant.TRAN_TYPE_7.equals(tranType)) {
            //外汇即期交易
            page = "viewTranReqDtl7";
        } else if (ForeignConstant.TRAN_TYPE_8.equals(tranType)) {
            //外汇远期交易
            page = "viewTranReqDtl8";
        } else if (ForeignConstant.TRAN_TYPE_9.equals(tranType)) {
            //外汇掉期交易
            page = "viewTranReqDtl9";
        }
//        产品id
        if(StringUtils.isNotEmpty(fexTranAppDtlVO.getProId())){
            model.addAttribute("pro", fexProductService.getByIdUnion(fexTranAppDtlVO.getProId()));
        }
        model.addAttribute("userName", getCurrentUser().getUsername());

        model.addAttribute("tranDtl", fexTranAppDtlVO);
        return "fexTranApp/" + page;
    }

    /**
     * 掉期交易查看
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewSwap")
    public String viewSwap(String id, Model model) {
        FexTranAppDtlVO fexTranAppDtlVO = fexTranAppService.getById(id);
//        产品id
        model.addAttribute("pro", fexProductService.getByIdUnion(fexTranAppDtlVO.getProId()));
        model.addAttribute("userName", getCurrentUser().getUsername());
        model.addAttribute("tranDtl", fexTranAppDtlVO);
        return "fexTranApp/viewTranReqDtl1";
    }

    /**
     * 交易需求列表
     * @param input
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input){
        return fexTranAppService.getFexTranAppDataTable(input, getCurrentUser());
    }

    /**
     * 交易需求查询
     * @param input
     * @return
     */
    @PostMapping("/dataTableQuery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput input){
        return fexTranAppService.getFexTranAppDataTableQuery(input, getCurrentUser());
    }

    /**
     * 待审批数据
     * @param input
     * @return
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input){
        return fexTranAppService.getFexTranAppDataTableApprove(input, getCurrentUser());
    }

    /**
     * 跳转至详情页面
     *
     * @return
     */
    @GetMapping("/addDtl")
    public String addDtl(String corpId,
                         @RequestParam(required = false, defaultValue = "") String templateId,
                         @RequestParam(required = false, defaultValue = "") String id,
                         Model model) {
        model.addAttribute("templateId", templateId);
        model.addAttribute("corpId", corpId);
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("tranDtlTmp", fexTranAppService.getTranDtlTmpById(id));
        }
        return "fexTranApp/addTranAppDtl";
    }


    /**
     * 新增交易需求
     *
     * @return
     */
    @GetMapping("/addSwap")
    public String addSwap(String swapType, String tranType,String isSwaptTran,String corpId,
                          String proId, @RequestParam(required = false, defaultValue = "") String id,
                          Model model) {
        model.addAttribute("swapType", swapType);
//        交易类型
        model.addAttribute("tranType", tranType);
//        是否掉期交易
        model.addAttribute("isSwaptTran", isSwaptTran);
//        单位id
        model.addAttribute("corpId", corpId);
//        产品id
        model.addAttribute("pro", fexProductService.getByIdUnion(proId));
        model.addAttribute("userName", getCurrentUser().getUsername());
        /**
         * 明细
         */
        if (StringUtils.isNotEmpty(id)) {
            FexTranAppDtlTmpVO fexTranAppDtlTmpVO = fexTranAppService.getTranDtlTmpById(id);
            if (tranType.equals(fexTranAppDtlTmpVO.getTranType()) && proId.equals(fexTranAppDtlTmpVO.getProId()) && isSwaptTran.equals(fexTranAppDtlTmpVO.getIsSwaptTran())) {
                model.addAttribute("tranDtlTmp", fexTranAppDtlTmpVO);
            }else{
                FexTranAppDtlTmpVO fexTranAppDtlTmpVO1 = new FexTranAppDtlTmpVO();
                fexTranAppDtlTmpVO1.setId(fexTranAppDtlTmpVO.getId());
                model.addAttribute("tranDtlTmp", fexTranAppDtlTmpVO1);
            }
        }
        return "fexTranApp/addTranReqDtl1";
    }

    @GetMapping("/editSwap")
    public String editSwap(String swapType, String tranType,String isSwaptTran,String corpId,
                          String proId, @RequestParam(required = false, defaultValue = "") String id,
                          String appId,Model model) {
        model.addAttribute("swapType", swapType);
//        交易类型
        model.addAttribute("tranType", tranType);
//        是否掉期交易
        model.addAttribute("isSwaptTran", isSwaptTran);
//        单位id
        model.addAttribute("corpId", corpId);
//        产品id
        model.addAttribute("pro", fexProductService.getByIdUnion(proId));
        model.addAttribute("userName", getCurrentUser().getUsername());
        /**
         * 明细
         */
        if (StringUtils.isNotEmpty(id)) {
            FexTranAppDtlVO fexTranAppDtlVO = fexTranAppService.getTranDtlById(id);
            if (tranType.equals(fexTranAppDtlVO.getTranType()) && proId.equals(fexTranAppDtlVO.getProId()) && isSwaptTran.equals(fexTranAppDtlVO.getIsSwaptTran())) {
                model.addAttribute("tranDtl", fexTranAppDtlVO);
            } else {
                FexTranAppDtlVO fexTranAppDtlVO1 = new FexTranAppDtlVO();
                fexTranAppDtlVO1.setId(fexTranAppDtlVO.getId());
                model.addAttribute("tranDtl", fexTranAppDtlVO1);
            }
        }
        model.addAttribute("appId", appId);
        return "fexTranApp/editTranReqDtl1";
    }

    /**
     * 跳转至详情页面
     *
     * @return
     */
    @GetMapping("/editDtl")
    public String editDtl(String corpId,
                          @RequestParam(required = false, defaultValue = "") String id,
                          String appId,Model model) {
        model.addAttribute("corpId", corpId);
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("tranDtl", fexTranAppService.getTranDtlById(id));
        }
        model.addAttribute("appId", appId);
        return "fexTranApp/editTranAppDtl";
    }

    /**
     * 返回具体交易需求明细页面
     *
     * @return
     */
    @GetMapping("/addTranDtl")
    public String addTranDtl(String tranType, String proId, String corpId,
                             @RequestParam(required = false, defaultValue = "") String templateId,
                             @RequestParam(required = false, defaultValue = "") String id,
                             @RequestParam(required = false, defaultValue = "") String isSwaptTran,
                             Model model) {
        String page = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
//            外汇买卖
            if (CommonConstant.YES.equals(isSwaptTran)) {
                //                掉期交易
                page = "addTranReqDtl2";
            } else {
//                非掉期交易
                page = "addTranReqDtl1";
            }
        } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
//            资金收付
            page = "addTranReqDtl10";
            if(EmptyUtils.isNotEmpty(templateId)){
                FexTranTemplateConfigVO fexTranTemplateConfig=   fexTranConfigTemplateService.getTemplateConfigById(templateId);
                if(fexTranTemplateConfig!=null){
                    model.addAttribute("template", fexTranTemplateConfig);
                }
            }
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            //外汇期权交易
            page = "addTranReqDtl4";
        } else if (ForeignConstant.TRAN_TYPE_7.equals(tranType)) {
            //外汇即期交易
            page = "addTranReqDtl7";
        } else if (ForeignConstant.TRAN_TYPE_8.equals(tranType)) {
            //外汇远期交易
            page = "addTranReqDtl8";
        } else if (ForeignConstant.TRAN_TYPE_9.equals(tranType)) {
            //外汇掉期交易
            page = "addTranReqDtl9";
        }

        //获取外汇交易参数-交易的默认外汇牌价类型
        ConfigDTO config = configService.getConfig(ConfigConstant.FEX, "tranRateTypeDefault");
        if(null != config && StringUtils.isNotEmpty(config.getValue())){
            //获取外汇牌价类型
            FexRateType fexRateType = fexRateTypeService.get(config.getValue());
            if(null !=  fexRateType){
                model.addAttribute("tranRateType", fexRateType);
            }
        }
        //model.addAttribute("tranRateType", config);

//        交易类型
        model.addAttribute("tranType", tranType);
//        是否掉期交易
        model.addAttribute("isSwaptTran", isSwaptTran);
//        单位id
        model.addAttribute("corpId", corpId);
        model.addAttribute("corp", corpService.get(corpId));
//        产品id
        FexProductVO fexProductVO = fexProductService.getByIdUnion(proId);
        if(null != fexProductVO){
            model.addAttribute("pro", fexProductVO);
            model.addAttribute("tranCurPair", btCurrencyPairService.getBtCurrencyPairByCurId(fexProductVO.getBuyCurId(), fexProductVO.getSellCurId()));
        }
        model.addAttribute("userName", getCurrentUser().getUsername());
        /**
         * 明细
         */
        if (StringUtils.isNotEmpty(id)) {
            FexTranAppDtlTmpVO fexTranAppDtlTmpVO = fexTranAppService.getTranDtlTmpById(id);
            if (tranType.equals(fexTranAppDtlTmpVO.getTranType()) && (StringUtils.isNotEmpty(proId)&&proId.equals(fexTranAppDtlTmpVO.getProId()) ||ForeignConstant.TRAN_TYPE_4.equals(tranType))) {
                //                原产品
                if (isSwaptTran.equals(fexTranAppDtlTmpVO.getIsSwaptTran()) || StringUtils.isEmpty(isSwaptTran)) {
                    model.addAttribute("tranDtlTmp", fexTranAppDtlTmpVO);
                }
            }else{
                //                修改后的产品
                FexTranAppDtlTmpVO fexTranAppDtlTmpVO1 = new FexTranAppDtlTmpVO();
                fexTranAppDtlTmpVO1.setId(fexTranAppDtlTmpVO.getId());
                model.addAttribute("tranDtlTmp", fexTranAppDtlTmpVO1);
            }
            if(ForeignConstant.FEX_PRODUCT_FPS.equals(proId) ||
                    ForeignConstant.FEX_PRODUCT_PAYPAL.equals(proId)){
                if(EmptyUtils.isNotEmpty(fexTranAppDtlTmpVO.getCotpId())){
                    FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranAppDtlTmpVO.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
                }
                //设置主体单位银行币别中文与英文简码
                if(EmptyUtils.isNotEmpty(fexTranAppDtlTmpVO.getCorpAccCurId())){
                    BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(fexTranAppDtlTmpVO.getCorpAccCurId());
                    fexTranAppDtlTmpVO.setCorpAccEnglishCode(btCurrencyDTO.getEnglishCode());
                    fexTranAppDtlTmpVO.setCorpAccCurName(btCurrencyDTO.getCurName());
                }
                //设置对手银行币别中文与英文简码
                if(EmptyUtils.isNotEmpty(fexTranAppDtlTmpVO.getCotpAccCurId())){
                    BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(fexTranAppDtlTmpVO.getCotpAccCurId());
                    fexTranAppDtlTmpVO.setCotpAccEnglishCode(btCurrencyDTO.getEnglishCode());
                    fexTranAppDtlTmpVO.setCotpAccCurName(btCurrencyDTO.getCurName());
                }
                //获取付款账户
                BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexTranAppDtlTmpVO.getCorpBankAcc());
                if(!EmptyUtils.isEmpty(payerAcc)){
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                    model.addAttribute("payerAcc", payerAcc);
                    if (sysCountryCustDTO != null) {
                        model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                        model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                        model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                    }
                    //境外银行地址
                    if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())&& StringUtils.isNotBlank(payerAcc.getBankCode())) {
                        BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(payerAcc.getBankCode());
                        if (btSwiftInfoDTO != null) {
                            payerAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                            model.addAttribute("bankAdd", payerAcc.getBankAdd());
                        }
                    }
                }
                if(EmptyUtils.isNotEmpty(fexTranAppDtlTmpVO.getCotpId())){
                    FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranAppDtlTmpVO.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
                }
            }
        }
        return "fexTranApp/" + page;
    }

    /**
     * 返回具体交易需求明细页面
     *
     * @return
     */
    @GetMapping("/editTranDtl")
    public String editTranDtl(String tranType, String proId,
                             @RequestParam(required = false,defaultValue = "") String id,
                             @RequestParam(required = false, defaultValue = "") String isSwaptTran,
                             @RequestParam(required = false, defaultValue = "") String templateId,
                             Model model) {
        String page = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
//            外汇买卖
            if (CommonConstant.YES.equals(isSwaptTran)) {
                //                掉期交易
                page = "editTranReqDtl2";
            } else {
//                非掉期交易
                page = "editTranReqDtl1";
            }
        } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)){
//            资金收付
         //   page = "editTranReqDtl3";
            //产品是香港FPS
   /*         if(ForeignConstant.FEX_PRODUCT_FPS.equals(proId)){
                page = "editTranReqDtlHKProduct";
            }
            //产品是PAYPAL
            if(ForeignConstant.FEX_PRODUCT_PAYPAL.equals(proId)){
                page = "editTranReqDtlPaypalProduct";
            }*/
            page = "editTranReqDtl10";
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            page = "editTranReqDtl4";
        } else if (ForeignConstant.TRAN_TYPE_7.equals(tranType)) {
            //外汇即期交易
            page = "editTranReqDtl7";
        } else if (ForeignConstant.TRAN_TYPE_8.equals(tranType)) {
            //外汇远期交易
            page = "editTranReqDtl8";
        } else if (ForeignConstant.TRAN_TYPE_9.equals(tranType)) {
            //外汇掉期交易
            page = "editTranReqDtl9";
        }

        //获取外汇交易参数-交易的默认外汇牌价类型
        ConfigDTO config = configService.getConfig(ConfigConstant.FEX, "tranRateTypeDefault");
        if(null != config && StringUtils.isNotEmpty(config.getValue())){
            //获取外汇牌价类型
            FexRateType fexRateType = fexRateTypeService.get(config.getValue());
            if(null !=  fexRateType){
                model.addAttribute("tranRateType", fexRateType);
            }
        }
//        交易类型
        model.addAttribute("tranType", tranType);
//        是否掉期交易
        model.addAttribute("isSwaptTran", isSwaptTran);
//        产品id
        model.addAttribute("pro", fexProductService.getByIdUnion(proId));
        model.addAttribute("userName", getCurrentUser().getUsername());
        /**
         * 明细
         */
        if (StringUtils.isNotEmpty(id)) {
            FexTranAppDtlVO fexTranAppDtlVO = fexTranAppService.getTranDtlById(id);
            if (tranType.equals(fexTranAppDtlVO.getTranType()) && (proId.equals(fexTranAppDtlVO.getProId()) || ForeignConstant.TRAN_TYPE_4.equals(tranType))) {
//                原产品
                if (isSwaptTran.equals(fexTranAppDtlVO.getIsSwaptTran()) || StringUtils.isEmpty(isSwaptTran)) {
                    model.addAttribute("tranDtl", fexTranAppDtlVO);
                    model.addAttribute("corp", corpService.get(fexTranAppDtlVO.getCorpId()));
                }
            } else {
//                修改后的产品
                FexTranAppDtlVO fexTranAppDtlVO1 = new FexTranAppDtlVO();
                fexTranAppDtlVO1.setId(fexTranAppDtlVO.getId());
                model.addAttribute("tranDtl", fexTranAppDtlVO1);
            }
            if(ForeignConstant.FEX_PRODUCT_FPS.equals(proId) ||
                    ForeignConstant.FEX_PRODUCT_PAYPAL.equals(proId)){
                model.addAttribute("tranDtlTmp", fexTranAppDtlVO);
                model.addAttribute("corpId", fexTranAppDtlVO.getCorpId());
                model.addAttribute("corp", corpService.get(fexTranAppDtlVO.getCorpId()));
                if(EmptyUtils.isNotEmpty(fexTranAppDtlVO.getCotpId())){
                    FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranAppDtlVO.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
                }
                //设置主体单位银行币别中文与英文简码
                if(EmptyUtils.isNotEmpty(fexTranAppDtlVO.getCorpAccCurId())){
                    BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(fexTranAppDtlVO.getCorpAccCurId());
                    fexTranAppDtlVO.setCorpAccEnglishCode(btCurrencyDTO.getEnglishCode());
                    fexTranAppDtlVO.setCorpAccCurName(btCurrencyDTO.getCurName());
                }
                //设置对手银行币别中文与英文简码
                if(EmptyUtils.isNotEmpty(fexTranAppDtlVO.getCotpAccCurId())){
                    BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(fexTranAppDtlVO.getCotpAccCurId());
                    fexTranAppDtlVO.setCotpAccEnglishCode(btCurrencyDTO.getEnglishCode());
                    fexTranAppDtlVO.setCotpAccCurName(btCurrencyDTO.getCurName());
                }
                //获取付款账户
                BtBankAccDTO payerAcc = bankAccService.getBankAccInfo(fexTranAppDtlVO.getCorpBankAcc());
                if(!EmptyUtils.isEmpty(payerAcc)){
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(payerAcc.getProv());
                    model.addAttribute("payerAcc", payerAcc);
                    if (sysCountryCustDTO != null) {
                        model.addAttribute("payerCountryId", sysCountryCustDTO.getId());
                        model.addAttribute("payerCountryNameZh", sysCountryCustDTO.getNameZh());
                        model.addAttribute("payerEnCode", sysCountryCustDTO.getEnCode());
                    }
                    //境外银行地址
                    if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())&& StringUtils.isNotBlank(payerAcc.getBankCode())) {
                        BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(payerAcc.getBankCode());
                        if (btSwiftInfoDTO != null) {
                            payerAcc.setBankAdd(btSwiftInfoDTO.getBankAdd());
                            model.addAttribute("bankAdd", payerAcc.getBankAdd());
                        }
                    }
                }
                if(EmptyUtils.isNotEmpty(fexTranAppDtlVO.getCotpId())){
                    FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(fexTranAppDtlVO.getCotpId());
                    if(!EmptyUtils.isEmpty(fexCotpVO)){
                        model.addAttribute("fexCotpVO", fexCotpVO);
                    }
                }
            }
            // 资金收付编辑：把已保存的交易模版带进 iframe，避免保存时 templateId 为空被冲掉
            if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
                String tid = templateId;
                if (StringUtils.isEmpty(tid) && StringUtils.isNotEmpty(fexTranAppDtlVO.getTemplateId())) {
                    tid = fexTranAppDtlVO.getTemplateId();
                }
                if (EmptyUtils.isNotEmpty(tid)) {
                    FexTranTemplateConfigVO fexTranTemplateConfig = fexTranConfigTemplateService.getTemplateConfigById(tid);
                    if (fexTranTemplateConfig != null) {
                        model.addAttribute("template", fexTranTemplateConfig);
                    }
                }
            }
        }
        return "fexTranApp/" + page;
    }

    /**
     * 保存
     * @param fexTranAppVO
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexTranAppVO fexTranAppVO) {
        fexTranAppService.save(fexTranAppVO, getCurrentUser());
        return success();
    }



    /**
     * 保存临时数据
     * @return
     */
    @PostMapping("/saveDtlTmp")
    @ResponseBody
    public AjaxJson saveDtlTmp(FexTranAppDtlTmpVO fexTranAppDtlTmpVO) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        if (StringUtils.isNotEmpty(fexTranAppDtlTmpVO.getId())) {
            fexTranAppService.deletDtlTmpById(fexTranAppDtlTmpVO.getId());
        }
        ajaxJson.setObj(fexTranAppService.saveDtlTmp(fexTranAppDtlTmpVO,this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 保存临时数据
     * @return
     */
    @PostMapping("/saveDtl")
    @ResponseBody
    public AjaxJson saveDtl(FexTranAppDtlVO fexTranAppDtlVO) {
        fexTranAppDtlVO.setCreateBy(getCurrentUser().getUsername());
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        if (StringUtils.isNotEmpty(fexTranAppDtlVO.getId())) {
            fexTranAppService.deleteDtlById(fexTranAppDtlVO.getId());
        }
        ajaxJson.setObj(fexTranAppService.saveDtl(fexTranAppDtlVO));
        return ajaxJson;
    }

    /**
     * 删除明细数据
     *
     * @return
     */
    @PostMapping("/deleteDtl")
    @ResponseBody
    public AjaxJson deleteDtlById(String id) {
        fexTranAppService.deleteDtlById(id);
        return success();
    }

    /**
     * 删除明细数据
     *
     * @return
     */
    @PostMapping("/deleteDtlTmp")
    @ResponseBody
    public AjaxJson deleteDtlTmp(String id) {
        fexTranAppService.deletDtlTmpById(id);
        return success();
    }

    /**
     * 删除主表数据
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id){
        fexTranAppService.delete(id);
        return success();
    }

    /**
     * 根据id查询临时表数据
     * @param input
     * @return
     */
    @PostMapping("/getDtlTmpDataTable")
    @ResponseBody
    public DataTablesOutput getDtlTmpDataTable(@RequestBody DataTablesInput input) {
        return fexTranAppService.getDtlTmpDataTable(input);
    }

    /**
     * 根据appid查询正式表数据
     * @param input
     * @return
     */
    @PostMapping("/getDtlDataTable")
    @ResponseBody
    public DataTablesOutput getDtlDataTable(@RequestBody DataTablesInput input) {
        return fexTranAppService.getDtlDataTable(input);
    }

    /**
     * 新增时更新掉期交易近远端id
     * @param id1
     * @param id2
     * @return
     */
    @PostMapping("/addUpdateParner")
    @ResponseBody
    public AjaxJson addUpdateParner(String id1,String id2){
        fexTranAppService.addUpdateParner(id1, id2);
        return success();
    }

    /**
     * 更新掉期交易近远端id
     * @param id1
     * @param id2
     * @return
     */
    @PostMapping("/editUpdateParner")
    @ResponseBody
    public AjaxJson editUpdateParner(String id1,String id2){
        fexTranAppService.editUpdateParner(id1, id2);
        return success();
    }

    /**
     * 获取银行账号
     *
     * @param curId
     * @param corpBank
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBoodBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBoodBankAcc(@RequestParam(required = false,defaultValue = "") String curId, String corpId,
                                             @RequestParam(required = false,defaultValue = "") String corpBank) {
        List<BtBankAccDTO> listAcc;
        AccountSearchDTO accountSearchDTO = new AccountSearchDTO();
        accountSearchDTO.setPermission("d");
        accountSearchDTO.setAccountType("2");
        boolean flag = true;
        if (StringUtils.isNotEmpty(curId)) {
            accountSearchDTO.setCurId(curId);
            flag = false;
        }
        if (StringUtils.isNotEmpty(corpBank)) {
            accountSearchDTO.setBankTypeId(corpBank);
            flag = false;
        }
        if (StringUtils.isNotEmpty(corpId)) {
            accountSearchDTO.setCorpId(corpId);
            flag = false;
        }
        if (flag) {
            listAcc = bankAccService.getBankAccListAll(new BtBankAccDTO());
        } else {
            listAcc = bankAccService.getBankAccByAccountDTO(accountSearchDTO);
        }
        return listAcc;
    }

    /**
     * 获取主体单位银行账户
     * @param corpId
     * @param curId
     * @param corpBank
     * @return
     */
    @PostMapping("/getCorpBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getCorpBankAcc(String corpId,
                                             @RequestParam(required = false,defaultValue = "") String countryId,
                                             @RequestParam(required = false,defaultValue = "") String curId,
                                             @RequestParam(required = false,defaultValue = "") String corpBank) {
        AccountSearchDTO searchDTO = new AccountSearchDTO();
        searchDTO.setCurId(curId);
        searchDTO.setBankTypeId(corpBank);
        searchDTO.setCorpId(corpId);
        searchDTO.setAccountType("2");
        searchDTO.setPermission("t");
        searchDTO.setUserId(getUserId());
        if (EmptyUtils.isNotEmpty(countryId)) {
            SysCountryCustDTO countryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
            if (countryCustDTO != null) {
                if(countryId.equals(countryCustDTO.getId())){
                    //境内
                    searchDTO.setIsDomesticBank(CommonConstant.YES);
                }else {
                    //境外
                    searchDTO.setIsDomesticBank(CommonConstant.NO);
                }
            }
        }
        List<BtBankAccDTO> btBankAccDTOS= bankAccService.getBankAccByAccountDTO(searchDTO);
        if(EmptyUtils.isNotEmpty(countryId)&&CommonConstant.NO.equals(searchDTO.getIsDomesticBank())&& EmptyUtils.isNotEmpty(btBankAccDTOS)){
            Map<String,List<BtSwiftInfoDTO>> swiftInfoDTOS= btSwiftInfoService.getByCountryCode(countryId).stream().collect(Collectors.groupingBy(BtSwiftInfoDTO::getSwiftCode));
            if(!swiftInfoDTOS.isEmpty()){
              return   btBankAccDTOS.stream().filter(e->swiftInfoDTOS.containsKey(e.getBankCode())).collect(Collectors.toList());
            }
        }
        return btBankAccDTOS;
    }


    /**
    *@Description 手机审批
    *@Param businessId
    *@Param model
    *@return String
    *@Date 2020/3/27 16:49
    *@Author liusy
    */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, Model model){
        model.addAttribute("fexTranAppVo", fexTranAppService.getFexTranAppVOById(businessId));
        model.addAttribute("tranDtls", fexTranAppService.listByAppId(businessId));
        return "fexTranApp/mobileViewFexTranApp";
    }


    /**
     *
     * 根据币种查货币对
     *
     * @param curId1
     * @param curId2
     * @return
     */
    @PostMapping("/getCurrencyPairId")
    @ResponseBody
    public AjaxJson getCurrencyPairId(String curId1,String curId2) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(fexTranAppService.getCurrencyPairIdBy(curId1,curId2));
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
}
