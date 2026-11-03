package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
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
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 资金需求单
 */
@Api(value = "/v1-payment/netbank/nisdemand", tags = "资金需求清单")
@RestController("v1NisDemandController")
@RequestMapping("/v1-payment/netbank/nisdemand")
public class NisDemandController extends BaseController {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;


    @ApiOperation("列表查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisDemandVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<NisDemandVO> out = nisDemandService.getDataTablesUnion(input, this.getCurrentUser());
        setDict(out);
        return AjaxJson.success(out);
    }



    /**
     * 获取需求账号
     *
     * @param resource
     * @return
     */
    @ApiOperation("获取需求账号")
    @PostMapping("/getBankAccQuery")
    public AjaxJson<List<BtBankAccDTO>> getBankAccQuery(String resource, String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 获取需求账号
     *
     * @param corpId
     * @return
     */
    @ApiOperation("获取需求账号")
    @PostMapping("/getBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(corpId.trim())) {
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String parentIds = StringUtils.isBlank(corp.getParentIds()) ? corp.getId() : corp.getParentIds();
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), parentIds, null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
            List<BtBankAccDTO> listBankAcc = new ArrayList<>();
            if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
                for (BtBankAccDTO dto : listBankAccTemp) {
                    //账号属性对外付款不为收款的账号才能对外付款
                    if ("2".equals(dto.getForeignDes()) || "3".equals(dto.getForeignDes())) {
                        dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                        listBankAcc.add(dto);
                    }
                }
            }
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                listAcc.addAll(listBankAcc);
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 获取账号余额
     *
     * @param bankAcc
     * @param curId
     * @return
     */
    @ApiOperation("获取账号余额")
    @GetMapping("/getBal")
    public AjaxJson<BisAccBalDTO> getBal(String bankAcc, String curId) {
        BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(bankAcc, curId);
        if (EmptyUtils.isEmpty(bisAccBalDTO)) {
            bisAccBalDTO = new BisAccBalDTO();
        }
        return success(bisAccBalDTO);
    }

    /**
     * 获取父账号
     *
     * @param corpId
     * @return
     */
    @ApiOperation("获取父账号")
    @GetMapping("/getSuperCorp")
    public AjaxJson<CorpDTO> getSuperCorp(String corpId) {
        CorpDTO corpDTO = corpService.getParentById(corpId);
        if (EmptyUtils.isEmpty(corpDTO)) {
            corpDTO = new CorpDTO();
        }
        return success(corpDTO);
    }

    /**
     * 根据银行账号id和币别，获取对应的父账号信息
     *
     * @param bankAccId
     * @param curId
     * @return
     */
    @ApiOperation("根据银行账号id和币别，获取对应的父账号信息")
    @GetMapping("/getSuperBankAcc")
    public AjaxJson<BtBankAccDTO> getSuperBankAcc(String bankAccId, String curId) {
        return success(btBankAccService.getParentAccByAccId(bankAccId, curId));
    }

    @ApiOperation("添加资金需求清单")
    @LogMessage(content = "添加资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisDemandVO nisDemandVO) {
        String billNo = nisDemandService.saveNisDemand(nisDemandVO, this.getCurrentUser());
        return success(billNo);
    }

    /**
     * 批量作废资金需求清单
     *
     * @return
     */
    @ApiOperation("批量作废资金需求清单")
    @LogMessage(content = "批量作废资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDefeat")
    public AjaxJson batchDefeat(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisDemand entity = nisDemandService.get(id, true);
            //暂存和已确认状态才可以提交
            if (entity != null && !NisDemandConstant.STATUE_TEMP.equals(entity.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisDemandService.batchDefeatNisDemand(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 作废资金需求清单
     *
     * @param id
     * @return
     */
    @ApiOperation("作废资金需求清单")
    @LogMessage(content = "作废资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/defeat/{id}")
    public AjaxJson defeat(@PathVariable("id") String id) {
        //状态校验
        NisDemand entity = nisDemandService.get(id, true);
        //暂存和已确认状态才可以提交
        if (entity != null && !NisDemandConstant.STATUE_TEMP.equals(entity.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        nisDemandService.defeatNisDemand(id, this.getCurrentUser());
        return success();
    }


    /**
     * 修改资金需求清单
     *
     * @param nisDemandVO
     * @return
     */
    @ApiOperation("修改资金需求清单")
    @LogMessage(content = "修改资金需求清单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody NisDemandVO nisDemandVO) {
        nisDemandService.updateNisDemand(nisDemandVO, this.getCurrentUser());
        return success();
    }

    /**
     * 资金需求清单编辑页面跳转
     *
     * @return
     */
    @ApiOperation("资金需求清单编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisDemandVO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            NisDemandVO nisDemandVO = nisDemandService.getByIdUnion(id);
            //暂存和编辑状态才可以提交
            if (!EmptyUtils.isEmpty(nisDemandVO) && !NisDemandConstant.STATUE_TEMP.equals(nisDemandVO.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(nisDemandVO.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisDemandVO.getBillNo());
            }
            return AjaxJson.success(nisDemandVO);
        }
        return AjaxJson.success();
    }

    /**
     * 付款通知表详情页
     *
     * @return
     */
    @ApiOperation("资金需求清单编辑页面跳转")
    @LogMessage(content = "付款通知表详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisDemandVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(nisDemandService.getByIdUnion(id));
    }


    /**
     * 付款通知表详情页
     *
     * @return
     */
    @ApiOperation("付款通知表详情页")
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{BillNo}")
    public AjaxJson<NisDemandVO> viewByBillNo(@PathVariable("BillNo") String BillNo) {
        return AjaxJson.success(nisDemandService.getByBillNoUnion(BillNo));
    }

    /**
     * 生成上级子账户需求
     *
     * @return
     */
    @ApiOperation("生成上级子账户需求")
    @LogMessage(content = "生成上级子账户需求单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/generateDemand")
    public AjaxJson generateDemand(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        List<String> idList = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        this.nisDemandService.generateDemand(idList, this.getCurrentUser());
        return success();
    }

    /**
     * 生成下拨付款指令
     *
     * @return
     */
    @ApiOperation("生成下拨付款指令")
    @LogMessage(content = "生成上级子账户需求单", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/generateAllocation")
    public AjaxJson generateAllocation(@RequestBody BisIDsDTO idsDTO) {
        String ids = idsDTO.getIds();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            nisDemandService.generateAllocation(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 付款单是否存在需求单
     *
     * @param id 付款id
     * @return
     */
    @ApiOperation("付款单是否存在需求单")
    @GetMapping("/isHasDemand")
    public AjaxJson isHasDemand(String id, String payAcc, String curId) {
        AjaxJson j = new AjaxJson();
        NisDemandVO nisDemandVO = nisDemandService.listByPayBillIdNoStatus(id);
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(payAcc, curId);
        if (EmptyUtils.isEmpty(nisDemandVO) && !EmptyUtils.isEmpty(btBankAccDTO) && "1".equals(btBankAccDTO.getIsDemand())) {
            j.setSuccess(false);
        } else {
            j.setSuccess(true);
        }
        return j;
    }


    @ApiOperation("查询付款单")
    @PostMapping("/datatableQuery")
    public AjaxJson<DataTablesOutput<NisDemandVO>> datatableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<NisDemandVO> out = nisDemandService.getDataTablesUnionQuery(input, this.getCurrentUser());
        setDict(out);
        return AjaxJson.success(out);
    }


    /**
     * 批量选择付款账号
     *
     * @return
     */
    @ApiOperation("批量选择付款账号")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetSuperAcc")
    public AjaxJson batchSetSuperAcc(@RequestBody NisDemandVO nisDemandVO) {
        nisDemandService.batchSetSuperAcc(nisDemandVO, this.getCurrentUser());
        return success();
    }


    private void setDict(DataTablesOutput<NisDemandVO> out) {
        Map<String, DictDTO> demandTypeMap = dictService.listMap("nisDemand.demandType");
        Map<String, DictDTO> demandStatusMap = dictService.listMap("nisDemand.status");
        Map<String, DictDTO> posStatusMap = dictService.listMap("nis.demand.posStatus");
        Map<String, DictDTO> sfYnMap = dictService.listMap("sf_yn");
        out.getData().forEach(vo -> {
            if(StringUtils.isNotEmpty(vo.getDemandType())){
                vo.setDemandTypeName(demandTypeMap.get(vo.getDemandType()).getName());
            }
            if(vo.getStatus()!=null){
                vo.setStatusName(demandStatusMap.get(vo.getStatus().toString()).getName());
            }
            if(StringUtils.isNotEmpty(vo.getIsSummary())){
                vo.setIsSummaryName(sfYnMap.get(vo.getIsSummary()).getName());
            }
            if(StringUtils.isNotEmpty(vo.getIsSummary())){
                vo.setIsSummaryName(sfYnMap.get(vo.getIsSummary()).getName());
            }
            if(StringUtils.isNotEmpty(vo.getPosStatus())){
                vo.setPosStatusName(posStatusMap.get(vo.getPosStatus()).getName());
            }
        });
    }

}
