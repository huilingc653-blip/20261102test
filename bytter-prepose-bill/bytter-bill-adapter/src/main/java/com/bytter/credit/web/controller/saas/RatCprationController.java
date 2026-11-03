package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.service.RatCprationService;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.ApproveListVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatCprationDetailVO;
import com.bytter.credit.domain.vo.RatCprationFormVO;
import com.bytter.credit.dto.RatSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 内部授信录入
 */
@Api(tags = "内部授信录入", value = "/v1-cms/cms/rat/ratcpration")
@RestController("v1RatCprationController")
@RequestMapping("/v1-cms/cms/rat/ratcpration")
public class RatCprationController extends BaseController {

    @Autowired
    private RatCprationService ratCprationService;

    /**
     * 内部授信数据列表
     *
     * @param rationVO 查询条件(合同号/授信时间)
     * @param start    当前页
     * @param len      每页条数
     * @return 内部授信数据列表
     */
    @ApiOperation("查询内部授信数据列表")
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
        if (!StringUtils.isEmpty(rationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", rationVO.getContractNo());
        }
        if (null != rationVO.getBeginDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(rationVO.getBeginDate()), Date.class);// 小于等于授信结束日期
        }
        DataTablesOutput output = ratCprationService.queryRatCprationData(input, RationConstant.CMS_CPRATION_EDIT_QUERY,
                getUserId());
        return success(output);
    }

    /**
     * 新增内部授信页面数据
     *
     * @return 关联合同/授信方式配置
     */
    @ApiOperation("新增内部授信页面数据")
    @GetMapping("/add")
    public AjaxJson<RatCprationFormVO> add() {
        RatCprationFormVO result = new RatCprationFormVO();
        result.setRelateCon(ratCprationService.getRelateCon());
        result.setConType(ratCprationService.getRatConType());
        return success(result);
    }

    /**
     * 检查合同号是否重复
     *
     * @param contractNo 合同号
     * @return 是否重复
     */
    @ApiOperation("检查合同号是否重复")
    @GetMapping("/checkContractNo")
    public AjaxJson<Boolean> checkContractNo(String contractNo) {
        return success(ratCprationService.checkContractNo(contractNo));
    }

    /**
     * 暂存/提交内部授信录入信息
     *
     * @param saveDTO 业务数据 + 保存标志(0-暂存 1-提交)
     * @return 操作结果
     */
    @ApiOperation("暂存/提交内部授信录入信息")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody RatSaveDTO<RatBkRationVm> saveDTO) {
        RatBkRationVm cpRationDTO = saveDTO.getData();
        setRatCprationDtls(cpRationDTO);
        ratCprationService.tempOrSubmit(cpRationDTO, saveDTO.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 编辑内部授信页面数据
     *
     * @param id 主键id
     * @return 内部授信数据/配置
     */
    @ApiOperation("编辑内部授信页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCprationFormVO> update(String id) {
        RatCprationFormVO result = new RatCprationFormVO();
        RatBkRationVm ratCpration = ratCprationService.getCpRationByRatCode(id);
        result.setRatCpration(ratCpration);
        result.setRelateCon(ratCprationService.getRelateCon());
        result.setConType(ratCprationService.getRatConType());
        result.setConId(ratCpration.getConId());
        return success(result);
    }

    /**
     * 删除内部授信信息
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除内部授信信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkration ratBkration = ratCprationService.get(id);
        ratBkration.setStatus(RationConstant.STATUS_DELETE);
        ratCprationService.updateEntity(ratBkration);
        return success();
    }

    /**
     * 查看内部授信信息
     *
     * @param id 主键id
     * @return 内部授信详情
     */
    @ApiOperation("查看内部授信信息")
    @GetMapping("/view/{id}")
    public AjaxJson<RatCprationDetailVO> view(@PathVariable("id") String id) {
        RatCprationDetailVO result = new RatCprationDetailVO();
        result.setRatCprationPage(ratCprationService.queryCprationById(id));
        result.setViewFlag("1");
        result.setConId(result.getRatCprationPage().getConId());
        return success(result);
    }

    /**
     * 内部授信录入审批列表数据
     *
     * @param dataTablesInput 表格入参
     * @return 审批列表数据
     */
    @ApiOperation("查询内部授信录入审批列表数据")
    @PostMapping("/CpApproveDataTable")
    public AjaxJson<DataTablesOutput> CpApproveDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratCprationService.getApproveDataTable(input));
    }

    /**
     * 内部授信录入审批列表页面数据
     *
     * @param flowStatus 流程状态
     * @param cycleTime  周期时间
     * @return 审批列表页面数据
     */
    @ApiOperation("内部授信录入审批列表页面数据")
    @GetMapping("/listApprove")
    public AjaxJson<ApproveListVO> listApprove(String flowStatus, String cycleTime) {
        ApproveListVO result = new ApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 内部授信录入审批详情页面数据
     *
     * @param id     业务id
     * @param taskId 审批任务id
     * @return 审批详情数据
     */
    @ApiOperation("内部授信录入审批详情页面数据")
    @GetMapping("/viewApprove")
    public AjaxJson<RatCprationDetailVO> viewApprove(String id, String taskId) {
        RatCprationDetailVO result = new RatCprationDetailVO();
        result.setRatCprationPage(ratCprationService.queryCprationById(id));
        result.setTaskId(taskId);
        result.setRelateCon(ratCprationService.getRelateCon());
        result.setConType(ratCprationService.getRatConType());
        result.setConId(result.getRatCprationPage().getConId());
        return success(result);
    }

    /**
     * 内部授信录入手机审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 手机审批详情数据
     */
    @ApiOperation("内部授信录入手机审批详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatCprationDetailVO> mobileApprove(String businessId, String taskId) {
        RatCprationDetailVO result = new RatCprationDetailVO();
        result.setRatCprationPage(ratCprationService.queryCprationById(businessId));
        result.setTaskId(taskId);
        return success(result);
    }

    private void setRatCprationDtls(RatBkRationVm cpRationDTO) {
        List<RatBkRationDtlVm> dtls = new ArrayList<RatBkRationDtlVm>();
        List<RatBkRationDtlVm> rationDtls = cpRationDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationDtlVm>();
        }
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO && !StringUtils.isEmpty(dtlDTO.getItemFlag())) {
                dtls.add(dtlDTO);
            }
            if (null == dtlDTO.getMoney()) {
                dtlDTO.setMoney(BigDecimal.ZERO);
            }
        }
        // 单品种排序
        Collections.sort(dtls, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        // 重设itemorder
        Integer index = Integer.valueOf(1);
        for (RatBkRationDtlVm dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
        }
        cpRationDTO.setRationDtls(dtls);
    }
}
