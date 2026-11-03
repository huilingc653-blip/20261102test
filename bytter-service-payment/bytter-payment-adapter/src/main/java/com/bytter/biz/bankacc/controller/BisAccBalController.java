package com.bytter.biz.bankacc.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.service.BtBankAccBalService;
import com.bytter.biz.inneraccount.service.BtBankAccDtlService;
import com.bytter.biz.inneraccount.vo.BisAccBalVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.AccBalMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银行账号余额管理
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BIS_ACC_BAL
 * @date 2017-06-23 15:02:30
 */
@Controller
@RequestMapping("/bankacc/bisaccbal")
public class BisAccBalController extends BaseController {
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Autowired
    private BtBankAccBalService btBankAccBalService;

    @Autowired
    private BtBankAccDtlService btBankAccDtlService;

    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * BIS_ACC_BAL列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_MANAGERQUERY)
    @LogMessage(content = "查询BIS_ACC_BAL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisAccBal";
    }
    /**
     * BIS_ACC_BAL table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_MANAGERQUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisAccBalService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * BIS_ACC_BAL新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @GetMapping("/add")
    public String add(BisAccBalVO bisAccBalVo, Model model) {
        if (StringUtils.isNotEmpty(bisAccBalVo.getId())) {
            BisAccBalDTO bisAccBal = bisAccBalService.getById(bisAccBalVo.getId());
            model.addAttribute("bisAccBalPage", bisAccBal);
        }
        model.addAttribute("addFlag", bisAccBalVo.getAddFlag());
        model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "bankacc/addBisAccBal";
    }
    /**
     * BIS_ACC_BAL余额初始化
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @GetMapping("/initBal")
    public String initBal(BisAccBalVO bisAccBalVo, Model model) {
        model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "bankacc/initBisAccBal";
    }

    /**
     * 添加BIS_ACC_BAL
     *
     * @param bisAccBalDTO
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @LogMessage(content = "添加BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/initBalSave")
    @ResponseBody
    public AjaxJson initBalSave(BisAccBalDTO bisAccBalDTO) {
        this.checkInitAccBal(bisAccBalDTO);
        bisAccBalService.initBalSave(bisAccBalDTO, this.getCurrentUser());
        return success();
    }
    /**
     * BIS_ACC_BAL编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @GetMapping("/update")
    public String update(BisAccBalDTO bisAccBal, Model model) {
        if (StringUtils.isNotEmpty(bisAccBal.getId())) {
            bisAccBal = bisAccBalService.getById(bisAccBal.getId());
            if (ObjectUtil.isNotEmpty(bisAccBal.getGetDate())){
                bisAccBal.setGetDateStr(DateUtil.format(bisAccBal.getGetDate(), "yyyy-MM-dd"));
            }
            model.addAttribute("bisAccBalPage", bisAccBal);
        }
        return "bankacc/updateBisAccBal";
    }
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @LogMessage(content = "批量删除离线账户余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        bisAccBalService.batchDelete(ids);
        return success();
    }
    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @LogMessage(content = "批量提交离线账户余额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        bisAccBalService.batchSubmit(ids, getCurrentUser());
        return success();
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisAccBalService.delete(id);
        return success();
    }
    /**
     * BIS_ACC_BAL详情页
     *
     * @return
     */
    ////@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BISACCBAL_MANAGERQUERY,
//            ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY}, logical = Logical.OR)
    @LogMessage(content = "查询BIS_ACC_BAL详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisAccBalDTO bisAccBal = bisAccBalService.getByUnion(id);
        model.addAttribute("bisAccBalPage", bisAccBal);
        model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_ACCBAL);
        return "bankacc/viewBisAccBal";
    }
    /**
     * 添加BIS_ACC_BAL
     *
     * @param bisAccBalDTO
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @LogMessage(content = "添加BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisAccBalDTO bisAccBalDTO) {
        this.checkMulAccount(bisAccBalDTO);
        bisAccBalService.saveBisAccBal(bisAccBalDTO, this.getCurrentUser());
        return success();
    }
    /**
     * 修改BIS_ACC_BAL
     *
     * @param bisAccBal
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY)
    @LogMessage(content = "修改BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisAccBalDTO bisAccBal) {
        bisAccBalService.updateBisAccBal(bisAccBal, this.getCurrentUser());
        return success();
    }
    /**
     * 根据条件加载银行账户信息
     *
     * @param corpId
     * @param bankAcc
     * @param bankType
     * @return
     */
    @PostMapping("/getBankAccBy")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccBy(String corpId, String bankAcc, String bankType, String curId) {
        try {
            BtBankAccDTO dto = new BtBankAccDTO();
            if (!StringUtils.isBlank(corpId)) {
                dto.setCorpId(corpId);
            }
            if (!StringUtils.isBlank(bankAcc)) {
                dto.setBankAcc(bankAcc);
            }
            if (!StringUtils.isBlank(bankType)) {
                dto.setBankTypeId(bankType);
            }
            if (!StringUtils.isBlank(curId)) {
                dto.setCurId(curId);
            }
            dto.setIsOnline(BtBankAccConstant.ISONLINE_0);
            return iBtBankAccService.getBankAccListAll(dto);
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return new ArrayList<>();
        }
    }
    @PostMapping("/getBankAccByAccount")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccByAccountDTO(String corpId, String bankAcc, String bankType, String curId) {
        List<String> curIds = new ArrayList<>();
        if (!EmptyUtils.isBlank(curId)) {
            curIds.add(curId);
        }
        // 根据当前用户ID,资源,币别,权限,搜索的离线账号
        List<BtBankAccDTO> btBankAccs = this.accountGrantService.listDTONotUnionByMultiVal(getUserId(), corpId, ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY,
                curIds, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
        List<BtBankAccDTO> btBankAccDTOList = new ArrayList<>();
        // 根据账号和银行类型过滤结果
        //银行账号输入框不为空
        boolean bankAccIsNotnull = !EmptyUtils.isBlank(bankAcc);
        //银行类型不为空
        boolean bankTypeIsNotnull = !EmptyUtils.isBlank(bankType);
        //币别信息不为空
        boolean curIdIsNotnull = !EmptyUtils.isBlank(curId);
        for (BtBankAccDTO bankAccDTO : btBankAccs) {
            //过滤掉注销状态和无效状态的账号
            if (CommonConstant.VALID_SIGN_NO.equals(bankAccDTO.getValidSign())
                    || CommonConstant.STATUE_DELETE.toString().equals(bankAccDTO.getStatus().toString())) {
                continue;
            }
            // 过滤币别,btBankAccs返回的银行账号包含多币别
            if (curIdIsNotnull) {
                if (!curId.equals(bankAccDTO.getCurId())) {
                    continue;
                }
            }
            if (bankAccIsNotnull && bankTypeIsNotnull) {
                if (bankType.equals(bankAccDTO.getBankTypeId()) && bankAccDTO.getBankAcc().contains(bankAcc)) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else if (bankAccIsNotnull) {
                if (bankAccDTO.getBankAcc().contains(bankAcc)) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else if (bankTypeIsNotnull) {
                if (bankType.equals(bankAccDTO.getBankTypeId())) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else {
                btBankAccDTOList.add(bankAccDTO);
            }
        }
        if (EmptyUtils.isEmptyList(btBankAccDTOList)) {
            return new ArrayList<>();
        } else {
            return btBankAccDTOList;
        }
    }
    @PostMapping("/getBankAccByAccountInit")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccByAccountInit(String corpId, String bankAcc, String bankType, String curId) {
        List<String> curIds = new ArrayList<>();
        if (!EmptyUtils.isBlank(curId)) {
            curIds.add(curId);
        }
        // 根据当前用户ID,资源,币别,权限,搜索的离线账号
        List<BtBankAccDTO> btBankAccs = this.accountGrantService.listDTONotUnionByMultiVal(getUserId(), corpId, ResourceConstant.NETBANK_BANKACC_BISACCBAL_APPLY,
                curIds, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
        List<BtBankAccDTO> btBankAccDTOList = new ArrayList<>();
        // 根据账号和银行类型过滤结果
        //银行账号输入框不为空
        boolean bankAccIsNotnull = !EmptyUtils.isBlank(bankAcc);
        //银行类型不为空
        boolean bankTypeIsNotnull = !EmptyUtils.isBlank(bankType);
        //币别信息不为空
        boolean curIdIsNotnull = !EmptyUtils.isBlank(curId);
        for (BtBankAccDTO bankAccDTO : btBankAccs) {
            //过滤掉注销状态和无效状态的账号
            if (CommonConstant.VALID_SIGN_NO.equals(bankAccDTO.getValidSign())
                    || CommonConstant.STATUE_DELETE.toString().equals(bankAccDTO.getStatus().toString())) {
                continue;
            }
            // 过滤币别,btBankAccs返回的银行账号包含多币别
            if (curIdIsNotnull) {
                if (!curId.equals(bankAccDTO.getCurId())) {
                    continue;
                }
            }
            if (bankAccIsNotnull && bankTypeIsNotnull) {
                if (bankType.equals(bankAccDTO.getBankTypeId()) && bankAccDTO.getBankAcc().contains(bankAcc)) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else if (bankAccIsNotnull) {
                if (bankAccDTO.getBankAcc().contains(bankAcc)) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else if (bankTypeIsNotnull) {
                if (bankType.equals(bankAccDTO.getBankTypeId())) {
                    btBankAccDTOList.add(bankAccDTO);
                }
            } else {
                btBankAccDTOList.add(bankAccDTO);
            }
        }
        if (EmptyUtils.isEmptyList(btBankAccDTOList)) {
            return new ArrayList<>();
        } else {
            //设置账号的初始化余额信息
            for(BtBankAccDTO bankAccDTO:btBankAccDTOList){
                BisAccBalDTO bisAccBal = bisAccBalService.getInitBisAccBal(bankAccDTO.getBankAcc(), bankAccDTO.getCurId());
                if(bisAccBal != null){
                    bankAccDTO.setInitBalanceDate(bisAccBal.getBalDate());
                    bankAccDTO.setInitBalance(bisAccBal.getBal());
                    bankAccDTO.setInitAvailBal(bisAccBal.getAvailBal());
                } else {
                    bankAccDTO.setInitBalanceDate(null);
                }
            }
            return btBankAccDTOList;
        }
    }
    /**
     * 银行账户余额查询 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY)
    @GetMapping("/query")
    public String query(String corpId,String queryDate,Model model) {
        if (ObjectUtil.isNotEmpty(corpId)){
            String ids = corpId;
            // 判断如果是三级单位的，则把下级单位id返回到页面
            CorpDTO corpDTO = corpService.get(corpId);
            if ("100000".equals(corpDTO.getCode())){
                CorpDTO corp100 = corpService.getCorpByCode("100");
                ids += "," + corp100.getId();
            }
            if (corpDTO != null && corpDTO.getParentIds().split(",").length == 3){
                List<CorpDTO> corps = corpService.getCorpByParentId(corpId);
                if (ObjectUtil.isNotEmpty(corps)){
                    for (CorpDTO temp : corps) {
                        ids += "," + temp.getId();
                    }
                }
            }

            model.addAttribute("corpId",ids);
        }
        if (ObjectUtil.isNotEmpty(queryDate)){
            model.addAttribute("queryDate",queryDate);
        }
        return "bankacc/listBisAccBalQuery";
    }

    /**
     * 银行账户余额查询 多级日报表/多级资金报表页面跳转
     *
     * @return
     */
    @GetMapping("/multiQuery")
    public String multiQuery(String corpId,String queryDate,Model model) {
        if (ObjectUtil.isNotEmpty(corpId)){
            String ids = corpId;
            // 判断如果是三级单位的，则把下级单位id返回到页面
            CorpDTO corpDTO = corpService.get(corpId);
            if (ObjectUtil.isNotEmpty(corpDTO)){
                if ("100000".equals(corpDTO.getCode())){
                    CorpDTO corp100 = corpService.getCorpByCode("100");
                    ids += "," + corp100.getId();
                }

                // 如果存在下级隐藏。则把下级隐藏的单位id返回到页面
                List<CorpDTO> subs = corpService.listSubCorp(Arrays.asList(corpId),false);
                for (CorpDTO temp : subs){
                    // 把隐藏单位添加进去
                    if (CommonConstant.NO.equals(temp.getReportDisplay())){
                        ids += "," + temp.getId();
                    }
                }
            }


            model.addAttribute("corpId",ids);
        }
        if (ObjectUtil.isNotEmpty(queryDate)){
            model.addAttribute("queryDate",queryDate);
        }
        return "bankacc/listBisAccBalQuery";
    }
    @GetMapping("/toQuery")
    public String toQuery(String corpId,String queryDate,String type,Model model) {
        if (ObjectUtil.isNotEmpty(corpId)){
            CorpDTO corp = corpService.get(corpId);
            if(corp != null && "100000".equals(corp.getCorpCode())){
                CorpDTO corp100 = corpService.getCorpByCode("100");
                if(corp100!=null){
                    corpId = corpId + "," + corp100.getId();
                }
            }
            model.addAttribute("corpName",corp.getName());
        }
        if (ObjectUtil.isEmpty(queryDate) || DateUtils.parse(queryDate,"yyyy-MM-dd").after(DateUtils.getDateYMD())){
            queryDate = DateUtils.getDate("yyyy-MM-dd");
        }
        model.addAttribute("corpId",corpId);
        model.addAttribute("queryDate",queryDate);
        model.addAttribute("accGroup",type);
        return "bankacc/listBisAccBalQuery4report";
    }
    /**
     * 银行账户余额查询
     *
     * @param dataTablesInput
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/getDataTablesQuery")
    @ResponseBody
    public DataTablesOutput getDataTablesQuery(@RequestBody DataTablesInput dataTablesInput) {
        return bisAccBalService.getDataTablesQuery(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 由明细生成余额
     *
     * @param bisAccBalDTO
     * @return
     */
    @PostMapping("/saveAccBalByDtl")
    @ResponseBody
    public AjaxJson saveAccBalByDtl(@RequestBody BisAccBalDTO bisAccBalDTO) {
        AjaxJson json = new AjaxJson();
        this.bisAccBalService.saveAccBalByDtl(bisAccBalDTO, this.getCurrentUser());
        json.setMessageEnum(CommonMessageEnum.COMMON_MESSAGE_SUCCESS);
        json.setMsg("操作成功!");
        json.setSuccess(true);
        return json;
    }
    /**
     * 账户明细截至时间维护页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND)
    @GetMapping("/selectBankAccEnd")
    public String selectBankAccEnd() {
        return "bankacc/selectBankAccEnd";
    }
    /**
     * 账户明细截至日期
     * zuoy 2017-07-10
     *
     * @param corpId
     * @param bankAcc
     * @param bifCode
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND)
    @PostMapping("/listBankAccByEnd")
    @ResponseBody
    public List<BtBankAccDTO> listBankAccByEnd(String corpId, String bankAcc, String bifCode, String curId) {
        try {
            StringBuilder sql = new StringBuilder();
            String corpAuthSql = SqlUtil.getInSql(
                    corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND, this.getCurrentUser().getId()), "acc.corp_id",
                    true);
            sql.append(" and ").append(corpAuthSql);
            List<BtBankAccDTO> list = this.iBtBankAccService.listBankAccByEnd(corpId, bankAcc, bifCode, curId,sql.toString());
            if (EmptyUtils.isEmptyList(list)) {
                list = new ArrayList<>();
            }
            return list;
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return new ArrayList<>();
        }
    }
    /**
     * 根据账户截至日期生成明细
     *
     * @param bisAccBalDTO
     * @return
     */
    @PostMapping("/toComplete")
    @ResponseBody
    public AjaxJson toComplete(@RequestBody BisAccBalDTO bisAccBalDTO) {
        AjaxJson json = new AjaxJson();
        this.bisAccBalService.saveComplete(bisAccBalDTO, this.getCurrentUser());
        json.setSuccess(true);
        json.setMsg("操作成功！");
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bankacc/listBisAccBalApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.bisAccBalService.getDataTablesApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            BisAccBalDTO bisAccBalDTO = this.bisAccBalService.getByUnion(id);
            model.addAttribute("bisAccBalPage", bisAccBalDTO);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_ACCBAL);
        }
        return "bankacc/approveBisAccBal";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            BisAccBalDTO dto = this.bisAccBalService.getByUnion(businessId);
            if (dto.getBalDate() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dto.setBalDateStr(sdf.format(dto.getBalDate()));
            }
            model.addAttribute("bisAccBalPage", dto);
            model.addAttribute("taskId", taskId);
        }
        return "bankacc/viewApproveBisAccBal";
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BisAccBalDTO> list) {
        long startT=System.currentTimeMillis();
        bisAccBalService.saveImport(list, this.getCurrentUser());
        return success();
    }
    public String getAccNo() {
        return SequenceSingleton.getSequence(SequenceEnum.BT_BANK_ACC_DTL);
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                List<BisAccBalDTO> dataTablesQuery = bisAccBalService.listExport(dataTablesInput, user);
                HashMap<String, BisAccBalDTO> curTotalMap = new HashMap<>();
                for(BisAccBalDTO vo:dataTablesQuery){
                    BisAccBalDTO bisAccBal;
                    if(curTotalMap.containsKey(vo.getCurName())){
                        bisAccBal = curTotalMap.get(vo.getCurName());
                        if(!EmptyUtils.isEmpty(vo.getBal())) {
                            bisAccBal.setBal(bisAccBal.getBal().add(vo.getBal()));
                        }
                        if(!EmptyUtils.isEmpty(vo.getAvailBal())) {
                            bisAccBal.setAvailBal(bisAccBal.getAvailBal().add(vo.getAvailBal()));
                        }
                    } else {
                        bisAccBal = new BisAccBalDTO();
                        if(EmptyUtils.isEmpty(vo.getBal())){
                            bisAccBal.setBal(BigDecimal.ZERO);
                        }else{
                            bisAccBal.setBal(vo.getBal());
                        }
                        if(EmptyUtils.isEmpty(vo.getAvailBal())){
                            bisAccBal.setAvailBal(BigDecimal.ZERO);
                        }else {
                            bisAccBal.setAvailBal(vo.getAvailBal());
                        }
                    }
                    bisAccBal.setBankAcc(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), bisAccBal);
                }
                for(String key:curTotalMap.keySet()){
                    dataTablesQuery.add(curTotalMap.get(key));
                }
                return dataTablesQuery;
            }
        }, totalParam, fileNameParam,"银行账户余额查询", 100000, BisAccBalVO.class);
    }

    //初始化余额 需要判断是否已经产生流水 或者 余额变更  如果有则 不能在进行初始化
    private void checkInitAccBal(BisAccBalDTO bisAccBalDTO){
        //判断每个账户是否能够初始化
        List<Map<String, Object>> btBankAccList = bisAccBalDTO.getBtBankAccList();
        for (Map<String, Object> map : btBankAccList) {
            String bankAcc = map.get("bankAcc").toString();
            String accCurId = map.get("accCurId").toString();
            //查询当天流水信息数量
            Integer dtlCount = btBankAccDtlService.selectDtlCount(bankAcc,accCurId);
            if(dtlCount > 0){
                //账号{0}存在流水信息不能进行初始化
                throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_EXIST_DTL, bankAcc);
            }
            //查询当天余额信息数量
            Integer balCount = btBankAccBalService.selectNotInitBalCount(bankAcc,accCurId);
            if(balCount > 0){
                //账号{0}存在余额信息不能进行初始化
                throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_EXIST_BAL, bankAcc);
            }
        }
    }
    /**
     * 同一个账号多次数据新增条件判断
     *
     * @param bisAccBalDTO
     */
    private void checkMulAccount(BisAccBalDTO bisAccBalDTO) {
        List<Map<String, Object>> btBankAccList = bisAccBalDTO.getBtBankAccList();
        Map<String, Map<String, Object>> mapFilter = new HashMap<>();
        for (Map<String, Object> map : btBankAccList) {
            StringBuilder builder = new StringBuilder();
            builder.append(map.get("bankAcc"));
            builder.append(map.get("accCurId"));
            builder.append(map.get("bankAccId"));
            if (mapFilter.containsKey(builder.toString())) {
                if (mapFilter.get(builder.toString()).get("balDate").equals(map.get("balDate"))) {
                    throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_MULBALDATE_CHECK_BAL, (String) map.get("bankAcc"), (String) map.get("balDate"));
                }
            }
            mapFilter.put(builder.toString(), map);
        }
    }
    /**
     * 获取账户最新余额
     *
     * @return
     */
    @RequestMapping(params = "getBal")
    @ResponseBody
    public BigDecimal getBal(String bankAcc, String curId) {
        BisAccBalDTO bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, null, true);
        if (bal != null) {
            if(bal.getBal() != null){
                return bal.getAvailBal();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取账户最新余额（REST风格地址）
     *
     * @return
     */
    @RequestMapping("/getBal")
    @ResponseBody
    public BigDecimal getBalRest(String bankAcc, String curId) {
        return getBal(bankAcc, curId);
    }

    /**
     * 获取账户最新余额
     *
     * @return
     */
    @RequestMapping(params = "getAllBal")
    @ResponseBody
    public BisAccBalDTO getAllBal(String bankAcc, String curId) {
        BisAccBalDTO bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, null, true);
        if (bal != null) {
            if(bal.getBal() != null){
                return bal;
            }
        }
        return new BisAccBalDTO();
    }

    /**
     * 获取账户信息
     *
     * @return
     */
    @PostMapping("/getAccountInfo")
    @ResponseBody
    public List<AccountDTO> getAccountInfo(AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(this.getUserId());
        return btBankAccBalService.getAccountInfo(accountSearchDTO);
    }
}
