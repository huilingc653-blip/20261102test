package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.service.RatCprationAddService;
import com.bytter.credit.domain.service.RatCprationService;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vo.ApproveListVO;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatCprationAddDetailVO;
import com.bytter.credit.dto.RatSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 内部授信追加
 */
@Api(tags = "内部授信追加", value = "/v1-cms/cms/rat/ratcprationadd")
@RestController("v1RatCprationAddController")
@RequestMapping("/v1-cms/cms/rat/ratcprationadd")
public class RatCprationAddController extends BaseController {

    @Autowired
    private RatCprationAddService ratCprationAddService;
    @Autowired
    private RatCprationService ratCprationService;

    /**
     * 查询内部授信追加列表
     *
     * @param rationVO 查询条件(合同号/授信时间)
     * @param start    当前页
     * @param len      每页条数
     * @return 内部授信追加列表
     */
    @ApiOperation("查询内部授信追加列表")
    @GetMapping("/list")
    public AjaxJson<DataTablesOutput> list(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        if (null != rationVO.getBeginDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 小于等于授信结束日期
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 授信审批通过
        DataTablesOutput output = ratCprationService.queryRatCprationData(input, RationConstant.CMS_CPRATION_EDIT_QUERY,
                getUserId());
        return success(output);
    }

    /**
     * 授信追加信息表新增页面数据
     *
     * @param ratCprationAdd 授信追加信息(ratCode 原授信编号)
     * @return 追加页面数据
     */
    @ApiOperation("授信追加信息表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatCprationAddDetailVO> add(RatBkrationAdd ratCprationAdd) {
        RatCprationAddDetailVO result = new RatCprationAddDetailVO();
        if (StringUtils.isNotEmpty(ratCprationAdd.getRatCode())) {
            result.setRatCprationPage(ratCprationAddService.getRatRationAddInfo(ratCprationAdd.getRatCode()));
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信额度追加
        return success(result);
    }

    /**
     * 暂存/提交内部授信追加信息
     *
     * @param saveDTO 业务数据 + 保存标志(0-暂存 1-提交)
     * @return 操作结果
     */
    @ApiOperation("暂存/提交内部授信追加信息")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody RatSaveDTO<RatBkRationAddVm> saveDTO) {
        RatBkRationAddVm listDTO = saveDTO.getData();
        setRatCprationAddDtls(listDTO);
        ratCprationAddService.saveRationAdd(listDTO, saveDTO.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 内部授信追加列表
     *
     * @param addVO 查询条件(单位/授信录入主键)
     * @param start 当前页
     * @param len   每页条数
     * @return 追加列表数据
     */
    @ApiOperation("查询内部授信追加列表数据")
    @GetMapping("/addList")
    public AjaxJson<DataTablesOutput> addList(RatBkrationAddListVO addVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(addVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", addVO.getCorpId());// 单位
        }
        if (!StringUtils.isEmpty(addVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratCode", addVO.getRatCode());// 授信录入主键编号
        }
        input.addFilter(MatchTypeEnum.EQ, "flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信追加
        DataTablesOutput output = ratCprationAddService.queryRationAddData(input, getUserId());
        return success(output);
    }

    /**
     * 查看追加信息
     *
     * @param id 主键id
     * @return 追加详情
     */
    @ApiOperation("查看追加信息")
    @GetMapping("/view/{id}")
    public AjaxJson<RatCprationAddDetailVO> view(@PathVariable("id") String id) {
        RatCprationAddDetailVO result = new RatCprationAddDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setRatCprationPage(ratCprationAddService.editRatRationAddInfo(id));
        }
        result.setViewFlag("1");
        return success(result);
    }

    /**
     * 删除追加信息
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除追加信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationAdd ratCprationAdd = ratCprationAddService.get(id);
        ratCprationAdd.setStatus(RationConstant.STATUS_DELETE);
        ratCprationAddService.updateEntity(ratCprationAdd);
        return success();
    }

    /**
     * 修改内部授信追加信息页面数据
     *
     * @param rationAdd 追加信息(id)
     * @return 追加页面数据
     */
    @ApiOperation("修改内部授信追加信息页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCprationAddDetailVO> update(RatBkrationAdd rationAdd) {
        RatCprationAddDetailVO result = new RatCprationAddDetailVO();
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            result.setRatCprationPage(ratCprationAddService.editRatRationAddInfo(rationAdd.getId()));
        }
        result.setFlag(RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信额度追加
        return success(result);
    }

    /**
     * 追加审批列表页面数据
     *
     * @param flowStatus 流程状态
     * @param cycleTime  周期时间
     * @return 审批列表页面数据
     */
    @ApiOperation("追加审批列表页面数据")
    @GetMapping("/listRatCpAddApprove")
    public AjaxJson<ApproveListVO> listRatCpAddApprove(String flowStatus, String cycleTime) {
        ApproveListVO result = new ApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        result.setFlag(RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信
        return success(result);
    }

    /**
     * 追加审批列表数据
     *
     * @param dataTablesInput 表格入参
     * @return 审批列表数据
     */
    @ApiOperation("查询追加审批列表数据")
    @PostMapping("/RatCpAddDataApprove")
    public AjaxJson<DataTablesOutput> RatCpAddDataApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return success(ratCprationAddService.listApproveOutputData(input));
    }

    /**
     * 追加审批详情页面数据
     *
     * @param id     业务id
     * @param taskId 审批任务id
     * @return 审批详情数据
     */
    @ApiOperation("追加审批详情页面数据")
    @GetMapping("/viewRatCpApprove")
    public AjaxJson<RatCprationAddDetailVO> viewRatCpApprove(String id, String taskId) {
        RatCprationAddDetailVO result = new RatCprationAddDetailVO();
        result.setRatCprationPage(ratCprationAddService.editRatRationAddInfo(id));
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 追加手机审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 手机审批详情数据
     */
    @ApiOperation("追加手机审批详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatCprationAddDetailVO> mobileApprove(String businessId, String taskId) {
        RatCprationAddDetailVO result = new RatCprationAddDetailVO();
        result.setRatCprationPage(ratCprationAddService.editRatRationAddInfo(businessId));
        result.setTaskId(taskId);
        return success(result);
    }

    private void setRatCprationAddDtls(RatBkRationAddVm listDTO) {
        List<RatBkRationAddDtlVm> dtls = new ArrayList<RatBkRationAddDtlVm>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationAddDtlVm>();
        }
        for (RatBkRationAddDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (null == dtlDTO.getMoney()) {
                        dtlDTO.setMoney(BigDecimal.ZERO);
                    }
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                        dtls.add(dtlDTO);
                    }
                }
            }
        }
        // 按照单品种排序
        Collections.sort(dtls, new Comparator<RatBkRationAddDtlVm>() {
            public int compare(RatBkRationAddDtlVm o1, RatBkRationAddDtlVm o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        listDTO.setRationDtls(dtls);
    }
}
