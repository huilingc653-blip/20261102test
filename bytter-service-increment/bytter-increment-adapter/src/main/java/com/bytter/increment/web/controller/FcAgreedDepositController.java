package com.bytter.increment.web.controller;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.AgreedConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.service.FcAgreedDepositService;
import com.bytter.increment.domain.service.FcAgreedDetailService;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.IFcCorpPoolService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 协定存款登记表
 * @date 2019-05-10 16:44:38
 */
@Controller
@RequestMapping("/agreeddeposit/fcagreeddeposit")
public class FcAgreedDepositController extends BaseController {
    @Autowired
    private FcAgreedDepositService fcAgreedDepositService;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    /**
     * 协定存款登记列表 页面跳转
     * @return
     */
    @LogMessage(content = "查询协定存款登记表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
        return "agreeddeposit/listFcAgreedDeposit";
    }
    /**
     * 协定存款登记列表 页面跳转
     * @return
     */
    @LogMessage(content = "查询协定存款延期列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY)
    @GetMapping("/listPos")
    public String listPos(Model model) {
        return "agreeddeposit/listPosFcAgreedDeposit";
    }
    /**
     * 协定存款登记列表 页面跳转
     * @return
     */
    @LogMessage(content = "查询协定存款终止列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY)
    @GetMapping("/listTerm")
    public String listTerm(Model model) {
        return "agreeddeposit/listTermFcAgreedDeposit";
    }
    /**
     * 协定存款登记表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = this.getCurrentUser();
        return fcAgreedDepositService.findListBysql(dataTablesInput,user);
    }
    /**
     * 协定存款登记复核列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定存款登记表复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY)
    @GetMapping("/listCheck")
    public String listCheck(Model model) {
        return "agreeddeposit/listFcAgreedDepositCheck";
    }
    /**
     * 协定存款延期复核列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY)
    @LogMessage(content = "查询协定存款登记表复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listPosCheck")
    public String listPosCheck(Model model) {
        return "agreeddeposit/listPosFcAgreedDepositCheck";
    }
    /**
     * 协定存款延期复核列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY)
    @LogMessage(content = "查询协定存款登记表复核列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listTermCheck")
    public String listTermCheck(Model model) {
        return "agreeddeposit/listTermFcAgreedDepositCheck";
    }
    /**
     * 协定存款登记表复核 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatableCheck")
    @ResponseBody
    public DataTablesOutput datatableCheck(@RequestBody DataTablesInput dataTablesInput){
        ICurrentUser user = this.getCurrentUser();
        return fcAgreedDepositService.checkListBysql(dataTablesInput,user);
    }
    /**
     * 协定存款登记新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY)
    @GetMapping("/add")
    public String add(FcAgreedDepositVO fcAgreedDepositVO, Model model) {
        if (StringUtils.isNotEmpty(fcAgreedDepositVO.getId())) {
            FcAgreedDeposit fcAgreedDeposit = fcAgreedDepositService.get(fcAgreedDepositVO.getId());


            model.addAttribute("fcAgreedDepositPage", fcAgreedDeposit);
        }
        return "agreeddeposit/addFcAgreedDeposit";
    }
    /**
     * 协定存款登记编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY)
    @GetMapping("/update")
    public String update(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if(fcAgreedDepositVO != null){
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
            Date tallyDate = fcDayCloseService.getPoolDayCloseDate(fcAgreedDepositVO.getNetId());
            tallyDate = DateUtils.addDay(tallyDate, 1);
            String dateStr = DateUtils.formatDate(tallyDate);
            model.addAttribute("dateStr", dateStr);
        }
        return "agreeddeposit/updateFcAgreedDeposit";
    }
    /**
     * 协定存款延期 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定延期登记表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY)
    @GetMapping("/addPos")
    public String addPos(Model model) {
        return "agreeddeposit/addPosAgreedDeposit";
    }
    /**
     * 协定存款延期编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY)
    @GetMapping("/updatePos")
    public String updatePos(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if(fcAgreedDepositVO != null){
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
            Date tallyDate = fcDayCloseService.getPoolDayCloseDate(fcAgreedDepositVO.getNetId());
            tallyDate = DateUtils.addDay(tallyDate, 1);
            String dateStr = DateUtils.formatDate(tallyDate);
            model.addAttribute("dateStr", dateStr);
        }
        return "agreeddeposit/updatePosAgreedDeposit";
    }
    /**
     * 协定存款终止登记 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询协定终止登记表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY)
    @GetMapping("/addTerm")
    public String addTerm(Model model) {
        return "agreeddeposit/addTermAgreedDeposit";
    }
    /**
     * 协定存款终止编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY)
    @GetMapping("/updateTerm")
    public String updateTerm(FcAgreedDepositVO fcAgreedDeposit, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getAgreeVo(fcAgreedDeposit);
        if(fcAgreedDepositVO != null){
            model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        }
        return "agreeddeposit/updateTermAgreedDeposit";
    }
    /**
     * 协定存款登记表详情页
     *
     * @return
     */
    @LogMessage(content = "查询协定存款登记表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{opType}/{id}")
    public String view(@PathVariable("opType") String opType, @PathVariable("id") String id, Model model) {
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        model.addAttribute("opType", opType);
        if(AgreedConstant.BUS_TYPE_0.equals(opType)){
            return "agreeddeposit/viewFcAgreedDeposit";
        }else if(AgreedConstant.BUS_TYPE_1.equals(opType)){
            return "agreeddeposit/viewPosFcAgreedDeposit";
        }else{
            return "agreeddeposit/viewTermFcAgreedDeposit";
        }
    }
    /**
     * 协定存款登记表详情页
     *
     * @return
     */
    @LogMessage(content = "查询复核协定存款登记表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/check/{busType}/{id}")
    public String check(@PathVariable("busType") String busType, @PathVariable("id") String id, Model model) {
        model.addAttribute("busType", busType);
        model.addAttribute("id", id);
        FcAgreedDepositVO fcAgreedDepositVO = fcAgreedDepositService.getByUnion(id);
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(id);
        fcAgreedDepositVO.setDetailList(details);
        model.addAttribute("fcAgreedDepositPage", fcAgreedDepositVO);
        return "agreeddeposit/agreedDepositCheckView";
    }
    /**
     * 添加协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款延期
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款延期登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY)
    @PostMapping("/savePos")
    @ResponseBody
    public AjaxJson savePos(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款终止
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "添加协定存款终止登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY)
    @PostMapping("/saveTerm")
    @ResponseBody
    public AjaxJson saveTerm(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.saveFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 添加协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "协定存款复核",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/opCheck")
    @ResponseBody
    public AjaxJson opCheck(FcAgreedDepositVO fcAgreedDeposit){
        fcAgreedDepositService.opCheck(fcAgreedDeposit,this.getCurrentUser());
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY)
    @PostMapping("/modifyPos")
    @ResponseBody
    public AjaxJson modifyPos(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改协定存款登记表
     * @param fcAgreedDeposit
     * @return
     */
    @LogMessage(content = "修改协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    ////@RequiresPermissions(ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY)
    @PostMapping("/modifyTerm")
    @ResponseBody
    public AjaxJson modifyTerm(FcAgreedDepositVO fcAgreedDeposit){
        if (dataCheck(fcAgreedDeposit)) {
            fcAgreedDepositService.updateFcAgreedDeposit(fcAgreedDeposit,this.getCurrentUser());
        }
        return success();
    }
    /**
     * 删除协定存款信息
     * @param id
     * @return
     */
    @LogMessage(content = "删除协定存款登记表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_APPLY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_APPLY,
//            ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_APPLY},
//            logical = Logical.OR)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fcAgreedDepositService.delFcAgreedDeposit(id);
        return success();
    }
    /**
     * 获取资金池
     * @param opType 0登记 1延期 2终止
     * @param checkSign 0 登记标记 1 复核标记
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools(String opType,String checkSign) {
        String resource = "";
        if(AgreedConstant.POOL_SIGN_0.equals(checkSign)){
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY;
            }
        }else{
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY;
            }
        }
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }
    /**
     * 获取资金池
     * @param poolId
     * @param opType 0登记 1延期 2终止
     * @param checkSign 0 登记标记 1 复核标记
     * @return
     */
    @RequestMapping(params = "listUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId, String opType, String checkSign) {
        String resource = "";
        if(AgreedConstant.POOL_SIGN_0.equals(checkSign)){
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POS_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERM_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REG_QUERY;
            }
        }else{
            if(AgreedConstant.BUS_TYPE_1.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_POSCHECK_QUERY;
            }else if(AgreedConstant.BUS_TYPE_2.equals(opType)){
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_TERMCHECK_QUERY;
            }else{
                resource = ResourceConstant.NETBANK_AGREEDDEPOSIT_FCAGREEDDEPOSIT_REGCHECK_QUERY;
            }
        }
        return fcCorpPoolService.getUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
    /**
     * 根据资金池或开户单位查找内部账号
     * @param netId
     * @param corpId
     * @return
     */
    @RequestMapping(params = "getBlnctrAcc")
    @ResponseBody
    public List<FcAccVO> getBlnctrAcc(String netId, String corpId, String busType){
        List<FcAccVO> list = fcAgreedDepositService.getBlnctrAcc(this.getCurrentUser(),netId,corpId,busType);
        return list;
    }
    /**
     * <b>功能介绍:获取费用明细</b><br>
     * <b>日期：</b>2017年6月15日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @PostMapping("/listFcAgreedDetail")
    @ResponseBody
    public List<FcAgreedDetail> listFcChargeTypeDtls(String agreedId){
        return  fcAgreedDetailService.getDetailList(agreedId);
    }
    /**
     * 根据账户id查找协定信息
     * @param accId
     * @return
     */
    @RequestMapping(params = "getInfo")
    @ResponseBody
    public AjaxJson getInfo(String accId,String busType){
        AjaxJson j=new AjaxJson();
        FcAgreedDepositVO vo = fcAgreedDepositService.getAgreeInfo(accId,busType);
        j.setSuccess(true);
        j.setObj(vo);
        return j;
    }
    /**
     * 数据校验
     *
     * @param vo
     * @return
     */
    private boolean dataCheck(FcAgreedDepositVO vo) {
        boolean flag = true;
        if(InnerFinanceConstant.BUS_TYPE_0.equals(vo.getOpType())){
            List<FcAgreedDepositVO> contractList = fcAgreedDepositService.getInfoByContractNo(vo.getContractNo());
            if (!EmptyUtils.isEmptyList(contractList)) {
                if (StringUtils.isBlank(vo.getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }else if (!vo.getId().equals(contractList.get(0).getId())) {
                    throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE, vo.getContractNo());
                }
            }
        }
        List<Integer> status = new ArrayList<Integer>();
        status.add(AgreedConstant.STATUE_TEMP);
        status.add(AgreedConstant.STATUE_BACK);
        List<FcAgreedDepositVO> agreedList = fcAgreedDepositService.getAgreedInfo(vo.getAccId(), vo.getOpType(), status);
        if (!EmptyUtils.isEmptyList(agreedList)) {
            if (StringUtils.isBlank(vo.getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            } else if (!vo.getId().equals(agreedList.get(0).getId())) {
                throw new ParameterException(ChoInnerFinanceMessageEnum.NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE, vo.getBlnctrAcc());
            }
        }
        return flag;
    }
    /**
     * 根据资金池获取日期
     * @param netId
     * @return
     */
    @RequestMapping(params = "getBeginDate")
    @ResponseBody
    public AjaxJson getBeginDate(String netId){
        AjaxJson j=new AjaxJson();
        Date tallyDate = fcDayCloseService.getPoolDayCloseDate(netId);
        tallyDate = DateUtils.addDay(tallyDate, 1);
        String dateStr = DateUtils.formatDate(tallyDate);
        j.setSuccess(true);
        j.setObj(dateStr);
        return j;
    }
}
