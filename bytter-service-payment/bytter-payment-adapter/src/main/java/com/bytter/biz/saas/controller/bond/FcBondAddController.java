package com.bytter.biz.saas.controller.bond;


import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bond.service.FcBondAddService;
import com.bytter.biz.bond.vo.BondAdditionPageVO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTableOutPutOrderUtils;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondAddService;
import com.bytter.guarantee.api.IFcBondBalService;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondBalVO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddController
 * @Description: 追加保证金
 * @date 2018年9月11日 上午10:05:48
 */
@Api(value = "/v1-payment/bond/fcbondAddition", tags = "追加保证金")
@RestController("v1FcBondAddController")
@RequestMapping("/v1-payment/bond/fcbondAddition")
public class FcBondAddController extends BaseController {
    @Autowired
    private FcBondAddService fcBondAddService;
    @DubboReference(check = false)
    private IFcBondAddService iFcBondAddService;
    @DubboReference(check = false)
    private IFcBondDetailService fcBondDetailService;
    @DubboReference(check = false)
    private IFcBondBalService fcBondBalService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private IUserService userService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Autowired
    NisAllocationReqService nisAllocationReqService;


    /**
     * 追加保证金表table列表
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation(("追加保证金表table列表"))
    @PostMapping("/BondAdddatatable")
    public AjaxJson<DataTablesOutput<FcBondAddDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<FcBondAddDTO> dataTablesUnion = iFcBondAddService.getDataTablesUnion(input, this.getCurrentUser());
        DataTableOutPutOrderUtils.orderOutputList(dataTablesInput,dataTablesUnion.getData(),"curName,bankAcc");
        setDictName(dataTablesUnion.getData());
        return AjaxJson.success(dataTablesUnion);
    }

    /**
     * 获取存出保证金账户列表
     */
    @ApiOperation(("获取存出保证金账户列表"))
    @PostMapping("/getSelectBondAcc.json")
    public AjaxJson<DataTablesOutput<FcBondAccVO>> getSelectBondAcc(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        NisAllocationReqDTO NisAllocationReqDTO = new NisAllocationReqDTO();
        NisAllocationReqDTO.setBillFrom("3");
        List<NisAllocationReqDTO> list = nisAllocationReqService.getNisAllocationReqBillFrom(NisAllocationReqDTO);
        if (list.size() > 0) {
            String erpIds = "";
            for (NisAllocationReqDTO nisAll : list) {
                erpIds += nisAll.getErpId() + ",";
            }
            erpIds = erpIds.substring(0, erpIds.length() - 1);
            input.getFormdata().put("erpIds", erpIds);
        }
        DataTablesOutput<FcBondAccVO> tablesOutput = iFcBondAddService.queryBondAccDataTables(input, this.getCurrentUser());
        DataTableOutPutOrderUtils.orderOutputList(input,tablesOutput.getData());
        return AjaxJson.success(tablesOutput);
    }

    /**
     * @author luxy 新增追加保证金页面
     */
    @ApiOperation(("新增追加保证金页面"))
    @RequestMapping("/addBondAdditionMoney/{bondId}")
    @LogMessage(content = "新增追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<BondAdditionPageVO> addBondAdditionMoney(@PathVariable("bondId") String id) {
        BondAdditionPageVO pageVO = new BondAdditionPageVO();
        if (StringUtils.isNotEmpty(id)) {
            FcBondDetailVO fcVo = fcBondDetailService.getFcBondById(id);
            FcBondBalVO fcbondbal = fcBondBalService.getBondBalByBondId(id);

            fcVo.setBalMoney(fcbondbal.getBondMoney().add(fcbondbal.getAddMoney()).subtract(fcbondbal.getBackMoney()));//保证金账户可用余额=保证金录入金额+追加金额-转回金额
            fcVo.setAvailMoney(fcbondbal.getBondMoney().add(fcbondbal.getAddMoney()).subtract(fcbondbal.getBackMoney())
                    .subtract(fcbondbal.getApplyMoney()));//保证金账户可用余额=保证金录入金额+追加金额-转回金额-占用金额
            setDictName(fcVo);
            pageVO.setFcBond(fcVo);
            pageVO.setAddMoney(fcbondbal.getAddMoney());
        }
        return AjaxJson.success(pageVO);
    }

    private void setDictName(FcBondDetailVO fcVo) {
        if(StringUtils.isNotEmpty(fcVo.getIsRegular())){
            fcVo.setIsRegularName("1".equals(fcVo.getIsRegular()) ? "是" : "否");
        }
        if(StringUtils.isNotEmpty(fcVo.getIsPayed())){
            fcVo.setIsPayedName("1".equals(fcVo.getIsPayed()) ? "是" : "否");
        }
    }

    private void setDictName(List<FcBondAddDTO> fcVoList) {
        Map<String, DictDTO> dictDTOMap = dictService.listMap("common.status");
        for (FcBondAddDTO vo : fcVoList) {
            if(vo.getStatus()!=null){
                if(dictDTOMap.get(String.valueOf(vo.getStatus())) !=null){
                    vo.setStatusName(dictDTOMap.get(String.valueOf(vo.getStatus())).getName());
                }
            }
        }

    }

    @ApiOperation(("查询历史追加明细"))
    @PostMapping("/addHisDatatable")
    public AjaxJson<DataTablesOutput<FcBondAddDTO>> addHisDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<FcBondAddDTO> fcBondAddDTODataTablesOutput = iFcBondAddService.addHisDatatable(input, this.getCurrentUser());
        setDictName(fcBondAddDTODataTablesOutput.getData());
        return success(fcBondAddDTODataTablesOutput);
    }

    /**
     * 保存追加保证金
     *
     * saveType 提交/暂存标志位，1-提交 0-暂存
     */
    @ApiOperation(("保存追加保证金"))
    @PostMapping("/saveFcBondAddition")
    @LogMessage(content = "保存追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson saveFcBondAddition(@RequestBody FcBondAddDTO fcBondAddDTO) {
        fcBondAddService.saveFcBondAdd(fcBondAddDTO,fcBondAddDTO.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * @param id 追加保证金的id 编辑追加保证金页面
     * @author luxy
     */
    @ApiOperation(("追加保证金表table列表"))
    @GetMapping("/editBondAddition/{id}")
    @LogMessage(content = "编辑追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<BondAdditionPageVO> editBond(@PathVariable("id") String id) {
        BondAdditionPageVO pageVO = new BondAdditionPageVO();
        if (StringUtils.isNotEmpty(id)) {
            FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
            FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
            if (StringUtils.isNotEmpty(fcbondAddDTO.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcbondAddDTO.getOutBankAcc(), fcBondVo.getCurId());
                if (null != bankInfo) {
                    fcbondAddDTO.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcbondAddDTO.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            setDictName(fcBondVo);
            pageVO.setFcbondAddDTO(fcbondAddDTO);
            pageVO.setFcBond(fcBondVo);
        }
        return AjaxJson.success(pageVO);
    }

    /**
     * @param id 追加保证金的id 查看追加保证金的信息
     * @author luxy
     */
    @ApiOperation(("查看追加保证金的信息"))
    @GetMapping("/viewBondAddition/{id}")
    @LogMessage(content = "查看追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<BondAdditionPageVO> viewBondAddition(@PathVariable("id") String id) {
        BondAdditionPageVO pageVO = new BondAdditionPageVO();
        if (StringUtils.isNotEmpty(id)) {
            FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
            FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
            List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
            if (StringUtils.isNotEmpty(fcbondAddDTO.getOutBankAcc())) {
                BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcbondAddDTO.getOutBankAcc(), fcbondAddDTO.getCurId());
                if (null != bankInfo) {
                    fcbondAddDTO.setOutBankAccMoney(bankInfo.getAvailBal());
                } else {
                    fcbondAddDTO.setOutBankAccMoney(BigDecimal.ZERO);
                }
            }
            pageVO.setFcbondAddDTO(fcbondAddDTO);
            setDictName(fcBondVo);
            pageVO.setFcBond(fcBondVo);
            setDictName(addHisList);
            pageVO.setAddHisList(addHisList);
        }
        return AjaxJson.success(pageVO);
    }

    /**
     * @param id 追加保证金的id 删除追加保证金
     * @author luxy
     */
    @ApiOperation(("删除追加保证金"))
    @PostMapping("/deleteBondAddition/{id}")
    @LogMessage(content = "删除追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson deleteBondAddtion(@PathVariable("id") String id) {
        iFcBondAddService.deleteBondAdd(id, this.getCurrentUser());
        return success();
    }

    /**
     * 查询历史追加明细
     *
     */
    public List<FcBondAddDTO> queryAddHis(String bondId) {
        List<FcBondAddDTO> voList = iFcBondAddService.queryAddHis(bondId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (FcBondAddDTO vo : voList) {
            UserDTO user = userService.getByLoginName(vo.getCreateBy());
            vo.setCreateByName(user.getUsername());
            if (vo.getAddDate() != null) {
                vo.setAddDateStr(sdf.format(vo.getAddDate()));
            }
        }
        return voList;
    }


    /**
     * 追加保证金审批表 table列表
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation(("追加保证金表table列表"))
    @PostMapping("/bondAddApproveTable")
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> bondAddApproveTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(iFcBondAddService.getApproveDataTables(input));
    }

    /**
     * 审批详情页面
     */
    @ApiOperation(("审批详情页面"))
    @GetMapping("/infoApprove")
    public AjaxJson<Map<String, Object>> infoApprove(String id, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
            FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
            List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
            model.put("fcbondAddDTO", fcbondAddDTO);
            model.put("fcBond", fcBondVo);
            model.put("addHisList", addHisList);
            model.put("taskId", taskId);// 必须要做此步骤
        }
        return AjaxJson.success(model);
    }

    /**
     * 手机审批详情页面
     */
    @ApiOperation("手机审批详情页面")
    @GetMapping("/mobileApprove")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(businessId);
            FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
            List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
            model.put("fcbondAddDTO", fcbondAddDTO);
            model.put("fcBond", fcBondVo);
            if (addHisList != null && addHisList.size() > 0) {
                model.put("addHisList", addHisList);
            }
            model.put("taskId", taskId);// 必须要做此步骤
        }
        return AjaxJson.success(model);
    }
}
