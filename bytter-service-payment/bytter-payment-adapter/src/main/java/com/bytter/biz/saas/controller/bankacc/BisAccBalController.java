package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.inneraccount.dto.v1.*;
import com.bytter.biz.inneraccount.service.BtBankAccBalService;
import com.bytter.biz.inneraccount.service.BtBankAccDtlService;
import com.bytter.biz.inneraccount.vo.BisAccBalVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccBalPageVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccBalQueryVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "银行账号余额管理", tags = "银行账号余额管理")
@RestController("v1BisAccBalController")
@RequestMapping("/v1-payment/bankacc/bisaccbal")
public class BisAccBalController extends BaseController {
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Autowired
    private BtBankAccBalService btBankAccBalService;
    @DubboReference(check = false)
    private ICorpService corpService;


    @Autowired
    private BtBankAccDtlService btBankAccDtlService;

    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    /**
     * BIS_ACC_BAL table列表
     *
     * @return
     */
    @ApiOperation("银行账号余额管理table")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccBalVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisAccBalService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 添加BIS_ACC_BAL
     *
     * @param bisAccBalDTO
     * @return
     */
    @ApiOperation("添加银行账户余额")
    @LogMessage(content = "添加BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/initBalSave")
    @ResponseBody
    public AjaxJson initBalSave(@RequestBody BisAccBalDTO bisAccBalDTO) {
        this.checkInitAccBal(bisAccBalDTO);
        bisAccBalService.initBalSave(bisAccBalDTO, this.getCurrentUser());
        return success();
    }
    /**
     * BIS_ACC_BAL编辑页面跳转
     *
     * @return
     */
    @ApiOperation("更新/变更前的数据获取银行账户余额")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BtBankAccBalPageVO> update(String id) {
        BtBankAccBalPageVO pageVO = new BtBankAccBalPageVO();
        if (StringUtils.isNotEmpty(id)) {
            BisAccBalDTO bisAccBal = bisAccBalService.getById(id);
            if (ObjectUtil.isNotEmpty(bisAccBal.getGetDate())){
                bisAccBal.setGetDateStr(DateUtil.format(bisAccBal.getGetDate(), "yyyy-MM-dd"));
            }
            pageVO.setBisAccBalPage(bisAccBal);

        }
        return success(pageVO);
    }
    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除离线账户余额")
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
    @ApiOperation("批量提交离线账户余额")
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
    @ApiOperation("删除账户余额")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisAccBalService.delete(id);
        return success();
    }

    @ApiOperation("查询银行账户余额详情")
    @LogMessage(content = "查询BIS_ACC_BAL详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<BtBankAccBalPageVO> view(@PathVariable("id") String id) {
        BtBankAccBalPageVO pageVO = new BtBankAccBalPageVO();
        BisAccBalDTO bisAccBal = bisAccBalService.getByUnion(id);
        pageVO.setBisAccBalPage(bisAccBal);
        pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_ACCBAL);
        return success(pageVO);
    }
    /**
     * 添加BIS_ACC_BAL
     *
     * @param bisAccBalDTO
     * @return
     */
    @ApiOperation("保存银行账户余额")
    @LogMessage(content = "添加BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisAccBalDTO bisAccBalDTO) {
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
    @ApiOperation("提交银行账户余额修改")
    @LogMessage(content = "修改BIS_ACC_BAL", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody BisAccBalDTO bisAccBal) {
        bisAccBalService.updateBisAccBal(bisAccBal, this.getCurrentUser());
        return success();
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
                throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_EXIST_DTL, (String) map.get("bankAcc"));
            }
            //查询不是初始化余额信息数量
            Integer balCount = btBankAccBalService.selectNotInitBalCount(bankAcc,accCurId);
            if(balCount > 0){
                //账号{0}存在余额信息不能进行初始化
                throw new ParameterException(AccBalMessageEnum.BIS_ACCBAL_MESSAGE_EXIST_BAL, (String) map.get("bankAcc"));
            }
        }
    }


    @ApiOperation("通过账户号获取银行账户相关信息")
    @PostMapping("/getBankAccByAccount")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getBankAccByAccountDTO(@RequestBody BtBankSearchAccountDTO paramsDTO) {
        String curId = paramsDTO.getCurId();
        String bankType = paramsDTO.getBankType();
        String corpId = paramsDTO.getCorpId();
        String bankAcc = paramsDTO.getBankAcc();

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
            return success(new ArrayList<>());
        } else {
            return success(btBankAccDTOList);
        }
    }
    @ApiOperation("根据选择的收款账户初始化付款账户信息列表")
    @PostMapping("/getBankAccByAccountInit")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getBankAccByAccountInit(@RequestBody BtBankSearchAccountDTO paramsDTO) {
        String curId = paramsDTO.getCurId();
        String bankType = paramsDTO.getBankType();
        String corpId = paramsDTO.getCorpId();
        String bankAcc = paramsDTO.getBankAcc();
        String natureId = paramsDTO.getNatureId();
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
        //账户性质不为空
        boolean natureIdIsNotnull = !EmptyUtils.isBlank(natureId);
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
            if (natureIdIsNotnull) {
                if (!natureId.equals(bankAccDTO.getNatureId())) {
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
            return success(new ArrayList<>());
        } else {
            //设置账号的初始化余额信息
            for(BtBankAccDTO bankAccDTO:btBankAccDTOList){
                BisAccBalDTO bisAccBal = bisAccBalService.getInitBisAccBal(bankAccDTO.getBankAcc(), bankAccDTO.getCurId());
                if(bisAccBal != null){
                    bankAccDTO.setInitBalanceDate(bisAccBal.getBalDate());
                    //当天的收入支出
                    BigDecimal balFromDetail = BigDecimal.ZERO;
                    if(bisAccBal.getBalDate() != null){
                        balFromDetail = bisAccDtlService.getBalFromDetail(bankAccDTO.getBankAcc(), bankAccDTO.getCurId(), bisAccBal.getBalDate());
                    }
                    if(bisAccBal.getBal() != null){
                        bankAccDTO.setInitBalance(bisAccBal.getBal().subtract(balFromDetail));
                    }
                    if(bisAccBal.getFrzBal() != null){
                        bankAccDTO.setInitAvailBal(bisAccBal.getAvailBal().subtract(balFromDetail));
                    }
                } else {
                    bankAccDTO.setInitBalanceDate(null);
                }
            }
            return success(btBankAccDTOList);
        }
    }
    /**
     * 银行账户余额查询 页面跳转
     *
     * @return
     */
    @ApiOperation("银行账户余额查询 页面跳转前数据查询")
    @GetMapping("/query")
    @ResponseBody
    public AjaxJson<BtBankAccBalQueryVO> query(String corpId, String queryDate) {
        BtBankAccBalQueryVO queryVO = new BtBankAccBalQueryVO();
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
            queryVO.setCorpId(ids);
        }
        if (ObjectUtil.isNotEmpty(queryDate)){
            queryVO.setQueryDate(queryDate);
        }
        return success(queryVO);
    }

    /**
     * 银行账户余额查询 多级日报表/多级资金报表页面跳转
     *
     * @return
     */
    @ApiOperation("银行账户余额查询 多级日报表/多级资金报表页面跳转")
    @GetMapping("/multiQuery")
    @ResponseBody
    public AjaxJson<BtBankAccBalQueryVO> multiQuery(String corpId,String queryDate) {
        BtBankAccBalQueryVO queryVO = new BtBankAccBalQueryVO();
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


            queryVO.setCorpId(ids);
        }
        if (ObjectUtil.isNotEmpty(queryDate)){
            queryVO.setQueryDate(queryDate);
        }
        return success(queryVO);
    }
    @ApiOperation("余额查询")
    @GetMapping("/toQuery")
    @ResponseBody
    public AjaxJson<BtBankAccBalQueryVO> toQuery(String corpId,String queryDate,String type) {
        BtBankAccBalQueryVO queryVO = new BtBankAccBalQueryVO();
        if (ObjectUtil.isNotEmpty(corpId)){
            CorpDTO corp = corpService.get(corpId);
            if(corp != null && "100000".equals(corp.getCorpCode())){
                CorpDTO corp100 = corpService.getCorpByCode("100");
                if(corp100!=null){
                    corpId = corpId + "," + corp100.getId();
                }
            }
            queryVO.setCorpName(corp.getName());
        }
        if (ObjectUtil.isEmpty(queryDate) || DateUtils.parse(queryDate,"yyyy-MM-dd").after(DateUtils.getDateYMD())){
            queryDate = DateUtils.getDate("yyyy-MM-dd");
        }
        queryVO.setCorpId(corpId);
        queryVO.setQueryDate(queryDate);
        queryVO.setAccGroup(type);

        return success(queryVO);
    }
    /**
     * 银行账户余额查询
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询—> 银行账户余额查询")
    @PostMapping("/getDataTablesQuery")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccBalVO>> getDataTablesQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisAccBalService.getDataTablesQuery(input, this.getCurrentUser()));
    }
    /**
     * 由明细生成余额
     *
     * @param bisAccBalDTO
     * @return
     */
    @ApiOperation("由明细生成余额")
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
     * @param searchDTO
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND)
    @ApiOperation("银行账户流水已截止日期维护")
    @PostMapping("/listBankAccByEnd")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> listBankAccByEnd(@RequestBody BtAccBalTimeEndSearchDTO searchDTO) {
        try {
            StringBuilder sql = new StringBuilder();
            String corpAuthSql = SqlUtil.getInSql(
                    corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND, this.getCurrentUser().getId()), "acc.corp_id",
                    true);
            sql.append(" and ").append(corpAuthSql);
            String corpId = searchDTO.getCorpId();
            String bankAcc = searchDTO.getBankAcc();
            String curId = searchDTO.getCurId();
            String bifCode = searchDTO.getBifCode();
            List<BtBankAccDTO> list = this.iBtBankAccService.listBankAccByEnd(corpId, bankAcc, bifCode, curId,sql.toString());
            if (EmptyUtils.isEmptyList(list)) {
                list = new ArrayList<>();
            }
            return success(list);
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return success();
        }
    }
    /**
     * 根据账户截至日期生成明细
     *
     * @param bisAccBalDTO
     * @return
     */
    @ApiOperation("修改截止交易日期")
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
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("银行账户余额审核列表查询")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.bisAccBalService.getDataTablesApprove(input));
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转审核页面前的数据获取")
    @GetMapping("/approve")
    @ResponseBody
    public AjaxJson<BtBankAccBalPageVO> approve(String id, String taskId) {
        BtBankAccBalPageVO pageVO = new BtBankAccBalPageVO();
        if (StringUtils.isNotEmpty(id)) {
            BisAccBalDTO bisAccBalDTO = this.bisAccBalService.getByUnion(id);
            pageVO.setBisAccBalPage(bisAccBalDTO);
            pageVO.setTaskId(taskId);
            pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_ACCBAL);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转手机审核页面前的数据获取")
    @GetMapping("/mobileApprove")
    @ResponseBody
    public AjaxJson<BtBankAccBalPageVO> mobileApprove(String businessId, String taskId) {
        BtBankAccBalPageVO pageVO = new BtBankAccBalPageVO();
        if (StringUtils.isNotEmpty(businessId)) {
            BisAccBalDTO dto = this.bisAccBalService.getByUnion(businessId);
            if (dto.getBalDate() != null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dto.setBalDateStr(sdf.format(dto.getBalDate()));
            }
            pageVO.setBisAccBalPage(dto);
            pageVO.setTaskId(taskId);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
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
    @ApiOperation("导出全部")
    @PostMapping("/exportAll")
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
    @ApiOperation("获取账户最新余额")
    @GetMapping( "/getBal")
    @ResponseBody
    public AjaxJson<BigDecimal> getBal(String bankAcc, String curId) {
        BisAccBalDTO bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, null, true);
        if (bal != null) {
            if(bal.getBal() != null){
                return success(bal.getAvailBal());
            }
        }
        return success(BigDecimal.ZERO);
    }

    /**
     * 获取账户最新余额
     *
     * @return
     */
    @ApiOperation("获取账户All余额")
    @GetMapping(path = "/getAllBal")
    @ResponseBody
    public AjaxJson<BisAccBalDTO> getAllBal(String bankAcc, String curId) {
        BisAccBalDTO bal = bisAccBalService.getBisAccBalBy(bankAcc, curId, null, true);
        if (bal != null) {
            if(bal.getBal() != null){
                return success(bal);
            }
        }
        return success(new BisAccBalDTO());
    }

    /**
     * 获取账户信息
     *
     * @return
     */
    @ApiOperation("获取账户信息")
    @PostMapping("/getAccountInfo")
    @ResponseBody
    public AjaxJson<List<AccountDTO>> getAccountInfo(@RequestBody AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(this.getUserId());
        return success(btBankAccBalService.getAccountInfo(accountSearchDTO));
    }

    @GetMapping("/add")
    public AjaxJson<Map<String,Object>> add(BisAccBalVO bisAccBalVo) {
        Map<String,Object> model=new HashMap<>();
        model.put("addFlag", bisAccBalVo.getAddFlag());
        model.put("sysDate", DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return AjaxJson.success(model);
    }
}