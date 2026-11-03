package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.service.RatCprationApplyService;
import com.bytter.credit.domain.service.RatCprationDisService;
import com.bytter.credit.domain.service.RatCprationLogService;
import com.bytter.credit.domain.service.RatUrationApplyService;
import com.bytter.credit.domain.vo.ApproveListVO;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatCprationApplyDetailVO;
import com.bytter.credit.dto.RatSaveDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.credit.dto.RatUrationApplyDTO;
import com.bytter.credit.dto.RatUrationApplyDtlDTO;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import com.bytter.credit.enums.RatMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 内部授信额度占用
 */
@Api(tags = "内部授信额度占用", value = "/v1-cms/cms/rat/cprationapply")
@RestController("v1RatCprationApplyController")
@RequestMapping("/v1-cms/cms/rat/cprationapply")
public class RatCprationApplyController extends BaseController {

    @Autowired
    private RatCprationApplyService ratCprationApplyService;
    @Autowired
    private RatCprationDisService ratCprationDisService;
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatCprationLogService ratLogService;

    /**
     * 查询待占用列表数据
     *
     * @param rationVO 查询条件(合同号/授信时间/单位)
     * @param start    当前页
     * @param len      每页条数
     * @return 待占用列表数据
     */
    @ApiOperation("查询待占用列表数据")
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
        DataTablesOutput output;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "disCorp", rationVO.getCorpId());// 单位ID
            output = ratCprationDisService.queryCpRationDisData(input, rationVO.getCorpId(),
                    RationConstant.CMS_CPRATION_DIS_QUERY, getUserId());
        } else {
            output = new DataTablesOutput();
        }
        return success(output);
    }

    /**
     * 查看分配信息
     *
     * @param ratApply 分配信息(disCode)
     * @return 分配信息详情
     */
    @ApiOperation("查看分配信息")
    @GetMapping("/viewApply")
    public AjaxJson<RatCprationApplyDetailVO> viewApply(RatUrationApply ratApply) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        if (StringUtils.isNotEmpty(ratApply.getDisCode())) {
            result.setRatCprationPage(ratCprationApplyService.initRatCprationApplyInfo(ratApply));
        }
        result.setViewFlag("1");
        return success(result);
    }

    /**
     * 新增内部授信额度占用页面数据
     *
     * @param ratApply 分配信息(disCode)
     * @return 新增页面数据
     */
    @ApiOperation("新增内部授信额度占用页面数据")
    @GetMapping("/add")
    public AjaxJson<RatCprationApplyDetailVO> add(RatUrationApply ratApply) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        if (StringUtils.isNotEmpty(ratApply.getDisCode())) {
            result.setRatCprationPage(ratCprationApplyService.initRatCprationApplyInfo(ratApply));
        }
        return success(result);
    }

    /**
     * 获取内部授信分配信息系统授信品种可占用的品种
     *
     * @param disCode 分配编号
     * @return 可占用品种列表
     */
    @ApiOperation("获取可占用的授信品种")
    @GetMapping("/getCpRationDisSysItem")
    public AjaxJson<List<RatSysItemDTO>> getRationDisSysItem(String disCode) {
        return success(ratCprationApplyService.getCpRationDisSysItem(disCode));
    }

    /**
     * 录入暂存/提交授信占用信息
     *
     * @param saveDTO 业务数据 + 保存标志(0-暂存 1-提交)
     * @return 操作结果
     */
    @ApiOperation("录入暂存/提交授信占用信息")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody RatSaveDTO<RatUrationApplyDTO> saveDTO) {
        RatUrationApplyDTO applyDTO = saveDTO.getData();
        setCprationApply(applyDTO);// 设置授信占用明细
        ratCprationApplyService.saveRatCprationApply(applyDTO, getCurrentUser(), saveDTO.getSaveType());
        return success();
    }

    /**
     * 编辑暂存/提交授信占用信息
     *
     * @param saveDTO 业务数据 + 保存标志(0-暂存 1-提交)
     * @return 操作结果
     */
    @ApiOperation("编辑暂存/提交授信占用信息")
    @PostMapping("/editTempOrSubmit")
    public AjaxJson editTempOrSubmit(@RequestBody RatSaveDTO<RatUrationApplyListDTO> saveDTO) {
        ratCprationApplyService.updateRatCprationApply(saveDTO.getData(), getCurrentUser(), saveDTO.getSaveType());
        return success();
    }

    /**
     * 内部授信占用列表
     *
     * @param disCode 分配编号
     * @return 授信分配信息
     */
    @ApiOperation("查询内部授信占用列表")
    @GetMapping("/applyList")
    public AjaxJson<RatBkrationDisVO> applyList(String disCode) {
        return success(ratCprationDisService.queryRatCpRationDisInfo(disCode));
    }

    /**
     * 授信占用dataTable
     *
     * @param dataTablesInput 表格入参
     * @param ratCode         授信编号
     * @param corpId          单位id
     * @return 占用列表数据
     */
    @ApiOperation("查询授信占用表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String ratCode,
            String corpId) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        input.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
        input.addColumn("createDate", false, true, null);
        input.addOrder("createDate", false);
        String ratType = RationConstant.RAT_TYPE_1;// 授信种类为企业授信
        if (!StringUtils.isEmpty(ratType)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        }
        return success(ratUrationApplyService.queryURationApplyData(input, RationConstant.CMS_CPRATION_APPLY_QUERY,
                getUserId()));
    }

    /**
     * 编辑内部授信额度占用页面数据
     *
     * @param ratApply 占用信息(id)
     * @return 编辑页面数据
     */
    @ApiOperation("编辑内部授信额度占用页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCprationApplyDetailVO> update(RatUrationApply ratApply) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        if (StringUtils.isNotEmpty(ratApply.getId())) {
            result.setApplyDTO(ratCprationApplyService.editRatUrationInfo(ratApply));
            result.setRatCprationPage(ratCprationApplyService.initRatCprationApplyInfo(ratApply));
        }
        return success(result);
    }

    /**
     * 改变授信占用状态为删除
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除授信占用信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratUrationApplyService.updateUrationDel(id, getUserId());
        return success();
    }

    /**
     * 查看占用详情
     *
     * @param ratApply 占用信息(id)
     * @return 占用详情数据
     */
    @ApiOperation("查看占用详情")
    @GetMapping("/view")
    public AjaxJson<RatCprationApplyDetailVO> view(RatUrationApply ratApply) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        result.setApplyDTO(ratCprationApplyService.editRatUrationInfo(ratApply));
        result.setRatCprationPage(ratCprationDisService.queryRatCpRationDisInfo(result.getApplyDTO().getDisCode()));
        result.setViewFlag("1");
        return success(result);
    }

    /**
     * 释放占用信息
     *
     * @param applyDTO 占用信息(id/money)
     * @return 操作结果
     */
    @ApiOperation("释放占用信息")
    @PostMapping("/release")
    public AjaxJson release(@RequestBody RatUrationApplyListDTO applyDTO) {
        ratUrationApplyService.updateUrationRelease(applyDTO.getId(), getUserId());
        String msg = RationConstant.MSGA_RELEASE_RAT;// 操作信息：释放额度
        Integer messageType = RationConstant.MESSAGE_TYPE_RELEASE;// 操作类型
        // 记录到日志表
        ratLogService.saveRatProcessMessge(applyDTO.getId(), applyDTO.getMoney(), messageType, msg, getCurrentUser());
        return success();
    }

    /**
     * 内部授信占用审批列表页面数据
     *
     * @param flowStatus 流程状态
     * @param cycleTime  周期时间
     * @return 审批列表页面数据
     */
    @ApiOperation("内部授信占用审批列表页面数据")
    @GetMapping("/listApprove")
    public AjaxJson<ApproveListVO> listApprove(String flowStatus, String cycleTime) {
        ApproveListVO result = new ApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 内部授信占用审批列表数据
     *
     * @param dataTablesInput 表格入参
     * @return 审批列表数据
     */
    @ApiOperation("查询内部授信占用审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return success(ratCprationApplyService.getDataApplyApproveTables(input));
    }

    /**
     * 授信占用审批详情页面数据
     *
     * @param id     业务id
     * @param taskId 审批任务id
     * @return 审批详情数据
     */
    @ApiOperation("授信占用审批详情页面数据")
    @GetMapping("/viewApplyApprove")
    public AjaxJson<RatCprationApplyDetailVO> viewApplyApprove(String id, String taskId) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        if (!EmptyUtils.isEmpty(id)) {
            RatUrationApply ratApply = ratCprationApplyService.get(id);
            result.setApplyDTO(ratCprationApplyService.editRatUrationInfo(ratApply));
            result.setRatCprationPage(ratCprationDisService.queryRatCpRationDisInfo(result.getApplyDTO().getDisCode()));
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 授信占用手机审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 手机审批详情数据
     */
    @ApiOperation("授信占用手机审批详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatCprationApplyDetailVO> mobileApprove(String businessId, String taskId) {
        RatCprationApplyDetailVO result = new RatCprationApplyDetailVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            RatUrationApply ratApply = ratCprationApplyService.get(businessId);
            result.setApplyDTO(ratCprationApplyService.editRatUrationInfo(ratApply));
            result.setRatCprationPage(ratCprationDisService.queryRatCpRationDisInfo(result.getApplyDTO().getDisCode()));
        }
        result.setTaskId(taskId);
        return success(result);
    }

    private void setCprationApply(RatUrationApplyDTO applyDTO) {
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        if (EmptyUtils.isEmptyList(applyDtls)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_ITEM_NULL);
        }
        List<RatUrationApplyListDTO> dtls = new ArrayList<RatUrationApplyListDTO>();
        for (RatUrationApplyListDTO listDTO : applyDtls) {
            if (StringUtils.isEmpty(listDTO.getType())) {
                continue;
            }
            setUrationApplyList(listDTO);
            listDTO.setRatType(applyDTO.getRatType());
            listDTO.setRatCode(applyDTO.getRatCode());
            listDTO.setDisCode(applyDTO.getDisCode());
            listDTO.setCurCode(applyDTO.getCurCode());
            listDTO.setCorpId(applyDTO.getCorpId());
            listDTO.setStatus(applyDTO.getStatus());
            dtls.add(listDTO);
        }
        applyDTO.setApplyDtls(dtls);
    }

    private void setUrationApplyList(RatUrationApplyListDTO applyDTO) {
        // 去掉无效数据
        List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
        List<RatUrationApplyDtlDTO> dtls = new ArrayList<RatUrationApplyDtlDTO>();
        if (!EmptyUtils.isEmptyList(applyDtls)) {
            for (RatUrationApplyDtlDTO listDTO : applyDtls) {
                if (StringUtils.isEmpty(listDTO.getItemCode())) {
                    continue;
                }
                if (null == listDTO.getMaxMoney()) {
                    listDTO.setMaxMoney(BigDecimal.ZERO);
                }
                dtls.add(listDTO);
            }
            applyDTO.setApplyDtls(dtls);
        }
    }
}
