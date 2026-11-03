package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.dto.AbPoolBusTypeBatchOptDTO;
import com.bytter.bill.domain.billPool.service.AbPoolBusTypeService;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeListVO;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 业务类型维护
 */
@Api(value = "/v1-preposebill/billPool/bustype", tags = "业务类型维护")
@RestController("v1AbPoolBusTypeController")
@RequestMapping("/v1-preposebill/billPool/bustype")
public class AbPoolBusTypeController extends BaseController {

    @Autowired
    private AbPoolBusTypeService abPoolBusTypeService;

    /**
     * 业务类型列表数据
     */
    @ApiOperation("业务类型列表数据")
    @GetMapping("/list")
    public AjaxJson<AbPoolBusTypeListVO> list(@ModelAttribute AbPoolBusTypeVO queryVO,
            @RequestParam(value = "bustypeName", required = false) String bustypeName,
            @RequestParam(value = "busTypeName", required = false) String busTypeName) {
        if (queryVO == null) {
            queryVO = new AbPoolBusTypeVO();
        }
        fillBustypeName(queryVO, bustypeName, busTypeName);
        return success(queryList(queryVO));
    }

    /**
     * 业务类型列表数据（JSON 入参）
     */
    @ApiOperation("业务类型列表数据")
    @PostMapping("/list")
    public AjaxJson<AbPoolBusTypeListVO> listPost(@RequestBody(required = false) AbPoolBusTypeVO queryVO) {
        return success(queryList(queryVO));
    }

    /**
     * 业务类型新增或修改页面数据
     */
    @ApiOperation("业务类型新增或修改页面数据")
    @GetMapping("/addOrUpdate/{id}")
    public AjaxJson<AbPoolBusTypeVO> addOrUpdate(@PathVariable("id") String id, String opt) {
        AbPoolBusTypeVO abPoolBusTypeVO = abPoolBusTypeService.getAbPoolBustTypeVOByBpId(id);
        if (!StringUtils.isEmpty(opt) && opt.equals("add")) {
            abPoolBusTypeVO = new AbPoolBusTypeVO();
            abPoolBusTypeVO.setBustypeLevel(AbPoolConstant.AB_POLL_BUSTYPE_LEVEL_1);
        }
        return success(abPoolBusTypeVO);
    }

    /**
     * 业务类型新增下级页面数据
     */
    @ApiOperation("业务类型新增下级页面数据")
    @GetMapping("/addNextLevel/{id}")
    public AjaxJson<AbPoolBusTypeVO> addNextLevel(@PathVariable("id") String id, String opt) {
        AbPoolBusTypeVO abPoolBusTypeVO = abPoolBusTypeService.getAbPoolBustTypeVOByBpId(id);
        AbPoolBusTypeVO newVO = new AbPoolBusTypeVO();
        newVO.setBustypeLevel(abPoolBusTypeVO.getBustypeLevel() + 1);
        newVO.setParentId(abPoolBusTypeVO.getId());
        return success(newVO);
    }

    /**
     * 业务类型批量启用/禁用
     */
    @ApiOperation("业务类型批量启用/禁用")
    @PostMapping("/batchOpt")
    public AjaxJson batchOpt(@RequestBody AbPoolBusTypeBatchOptDTO dto) {
        return abPoolBusTypeService.batchBusTypeOptWith(dto.getIds(), dto.getOpt(), getCurrentUser());
    }

    /**
     * 业务类型新增或修改操作
     */
    @ApiOperation("业务类型新增或修改操作")
    @PostMapping("/updateAbPoolBusType")
    public AjaxJson updateAbPoolBusType(@RequestBody AbPoolBusTypeVO abPoolBusTypeVO) {
        return abPoolBusTypeService.settingPoolBusType(abPoolBusTypeVO, getCurrentUser());
    }

    /**
     * 获取树形结构业务类型信息
     */
    @ApiOperation("获取树形结构业务类型信息")
    @GetMapping("/getTreeData")
    public AjaxJson<Object> getTreeData(String resource, AbPoolBusTypeVO queryVO) {
        if (queryVO == null) {
            queryVO = new AbPoolBusTypeVO();
        }
        queryVO.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return success(abPoolBusTypeService.getTreeDataList(resource, queryVO, getCurrentUser()));
    }

    private AbPoolBusTypeListVO queryList(AbPoolBusTypeVO queryVO) {
        if (queryVO == null) {
            queryVO = new AbPoolBusTypeVO();
        }
        if (StringUtils.isNotBlank(queryVO.getBustypeName())) {
            queryVO.setBustypeName(queryVO.getBustypeName().trim());
        }
        queryVO.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
        List<AbPoolBusTypeVO> listInfo = abPoolBusTypeService.listAbPoolBusTypeVO(queryVO);
        AbPoolBusTypeListVO result = new AbPoolBusTypeListVO();
        result.setListInfo(listInfo == null ? new ArrayList<>() : listInfo);
        result.setQueryVO(queryVO);
        return result;
    }

    private void fillBustypeName(AbPoolBusTypeVO queryVO, String bustypeName, String busTypeName) {
        if (queryVO == null) {
            return;
        }
        if (StringUtils.isNotBlank(queryVO.getBustypeName())) {
            return;
        }
        String name = StringUtils.isNotBlank(bustypeName) ? bustypeName : busTypeName;
        if (StringUtils.isNotBlank(name)) {
            queryVO.setBustypeName(name);
        }
    }
}
