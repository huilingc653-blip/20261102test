package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.service.RatCprationDisService;
import com.bytter.credit.domain.vo.ApproveListVO;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vo.RatCprationDisDetailVO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.dto.RatSaveDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 内部授信分配
 */
@Api(tags = "内部授信分配", value = "/v1-cms/cms/rat/ratcprationdis")
@RestController("v1RatCprationDisController")
@RequestMapping("/v1-cms/cms/rat/ratcprationdis")
public class RatCprationDisController extends BaseController {

    @Autowired
    private RatCprationDisService ratCprationDisService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 内部授信分配列表
     *
     * @param rationVO 查询条件(合同号/授信时间)
     * @param start    当前页
     * @param len      每页条数
     * @return 分配列表数据
     */
    @ApiOperation("查询内部授信分配列表")
    @LogMessage(content = "查询内部授信额度录入列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
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
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 录入数据已审批通过，可以进行分配
        input.addOrder("ratCode", false);
        DataTablesOutput output = ratCprationDisService.listDisOutputData(input, getUserId());
        return success(output);
    }

    /**
     * 新增授信分配页面数据
     *
     * @param ratCode 授信编号
     * @return 新增页面数据
     */
    @ApiOperation("新增授信分配页面数据")
    @LogMessage(content = "新增内部授信额度录入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public AjaxJson<RatCprationDisDetailVO> add(String ratCode) {
        RatCprationDisDetailVO result = new RatCprationDisDetailVO();
        if (StringUtils.isNotEmpty(ratCode)) {
            result.setRatCprationPage(ratCprationDisService.getRatCpRationDisInfo(ratCode));
        }
        return success(result);
    }

    /**
     * 获取二次授信单位
     *
     * @param netId 网络id
     * @return 单位树列表
     */
    @ApiOperation("获取二次授信单位")
    @GetMapping("/listNetCorp")
    public AjaxJson<List<CorpDTO>> listNetCorp(String netId) {
        List<CorpDTO> corpDTOs = corpService.getCpRationGroupCorpNet(RationConstant.CMS_CPRATION_DIS_APPLY, netId,
                getUserId());
        return success(TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        }));
    }

    /**
     * 暂存/提交分配信息
     *
     * @param saveDTO 业务数据 + 保存标志(0-暂存 1-提交)
     * @return 操作结果
     */
    @ApiOperation("暂存/提交分配信息")
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrsubmit(@RequestBody RatSaveDTO<RatBkrationDisListDTO> saveDTO) {
        RatBkrationDisListDTO ratCprationDis = saveDTO.getData();
        setRatCprationDisDtls(ratCprationDis);
        ratCprationDisService.tempOrSubmit(ratCprationDis, getCurrentUser(), saveDTO.getSaveType());
        return success();
    }

    /**
     * 分配列表数据
     *
     * @param disVO 查询条件(单位/授信录入主键)
     * @param start 当前页
     * @param len   每页条数
     * @return 分配列表数据
     */
    @ApiOperation("查询分配列表数据")
    @GetMapping("/disList")
    public AjaxJson<DataTablesOutput> disList(RatBkrationDisVO disVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(disVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", disVO.getCorpId());// 单位
        }
        if (!StringUtils.isEmpty(disVO.getRatCode())) {
            input.addFilter(MatchTypeEnum.EQ, "ratCode", disVO.getRatCode());// 授信录入主键编号
        }
        DataTablesOutput output = ratCprationDisService.queryRationDisData(input, getUserId());
        return success(output);
    }

    /**
     * 内部授信分配编辑页面数据
     *
     * @param rationDis 分配信息(id)
     * @return 编辑页面数据
     */
    @ApiOperation("内部授信分配编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatCprationDisDetailVO> update(RatBkrationDis rationDis) {
        RatCprationDisDetailVO result = new RatCprationDisDetailVO();
        if (StringUtils.isNotEmpty(rationDis.getId())) {
            result.setRatCprationPage(ratCprationDisService.queryRatRationDisInfo(rationDis.getId()));
        }
        return success(result);
    }

    /**
     * 删除授信分配信息
     *
     * @param id 主键id
     * @return 操作结果
     */
    @ApiOperation("删除授信分配信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDis ratCprationDis = ratCprationDisService.get(id);
        ratCprationDis.setStatus(RationConstant.STATUS_DELETE);
        ratCprationDisService.updateEntity(ratCprationDis);
        return success();
    }

    /**
     * 查看内部授信分配信息
     *
     * @param id 主键id
     * @return 分配详情数据
     */
    @ApiOperation("查看内部授信分配信息")
    @GetMapping("/viewDis/{id}")
    public AjaxJson<RatCprationDisDetailVO> viewDis(@PathVariable("id") String id) {
        RatCprationDisDetailVO result = new RatCprationDisDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setRatCprationPage(ratCprationDisService.queryRatRationDisInfo(id));
            CorpDTO corpDTO = corpService.get(result.getRatCprationPage().getDisCorpId());
            result.getRatCprationPage().setDisCorpName(corpDTO.getName());// 二次授信单位名称
        }
        result.setViewFlag("1");
        return success(result);
    }

    /**
     * 内部授信分配审批列表页面数据
     *
     * @param flowStatus 流程状态
     * @param cycleTime  周期时间
     * @return 审批列表页面数据
     */
    @ApiOperation("内部授信分配审批列表页面数据")
    @GetMapping("/listApprove")
    public AjaxJson<ApproveListVO> listApprove(String flowStatus, String cycleTime) {
        ApproveListVO result = new ApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 内部授信分配审批列表数据
     *
     * @param dataTablesInput 表格入参
     * @return 审批列表数据
     */
    @ApiOperation("查询内部授信分配审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return success(ratCprationDisService.listApproveOutputData(input));
    }

    /**
     * 内部授信分配审批详情页面数据
     *
     * @param id     业务id
     * @param taskId 审批任务id
     * @return 审批详情数据
     */
    @ApiOperation("内部授信分配审批详情页面数据")
    @GetMapping("/viewCprationDisApprove")
    public AjaxJson<RatCprationDisDetailVO> viewCprationDisApprove(String id, String taskId) {
        RatCprationDisDetailVO result = new RatCprationDisDetailVO();
        result.setRatCprationPage(ratCprationDisService.queryRatRationDisInfo(id));
        CorpDTO corpDTO = corpService.get(result.getRatCprationPage().getDisCorpId());
        result.getRatCprationPage().setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 内部授信分配手机审批详情页面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     * @return 手机审批详情数据
     */
    @ApiOperation("内部授信分配手机审批详情页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<RatCprationDisDetailVO> mobileApprove(String businessId, String taskId) {
        RatCprationDisDetailVO result = new RatCprationDisDetailVO();
        result.setRatCprationPage(ratCprationDisService.queryRatRationDisInfo(businessId));
        CorpDTO corpDTO = corpService.get(result.getRatCprationPage().getDisCorpId());
        result.getRatCprationPage().setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        result.setTaskId(taskId);
        return success(result);
    }

    private void setRatCprationDisDtls(RatBkrationDisListDTO ratCprationDis) {
        List<RatBkrationDisDtlDTO> dtls = new ArrayList<RatBkrationDisDtlDTO>();
        List<RatBkrationDisDtlDTO> rationDtls = ratCprationDis.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkrationDisDtlDTO>();
        }
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (null == dtlDTO.getMoney()) {
                        dtlDTO.setMoney(BigDecimal.ZERO);
                    }
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
                        dtls.add(dtlDTO);
                    }
                }
            }
        }
        // 按照单品种排序
        Collections.sort(dtls, new Comparator<RatBkrationDisDtlDTO>() {
            public int compare(RatBkrationDisDtlDTO o1, RatBkrationDisDtlDTO o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        ratCprationDis.setRationDtls(dtls);
    }
}
