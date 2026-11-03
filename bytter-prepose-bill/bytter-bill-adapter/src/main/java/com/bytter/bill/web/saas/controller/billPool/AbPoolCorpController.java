package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolCorpService;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpAddLowerVO;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpListVO;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpUpdateVO;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 票据池单位维护
 */
@Api(value = "/v1-preposebill/billPool/corp", tags = "票据池单位维护")
@RestController("v1AbPoolCorpController")
@RequestMapping("/v1-preposebill/billPool/corp")
public class AbPoolCorpController extends BaseController {

    @Autowired
    private AbPoolCorpService abPoolCorpService;

    /**
     * 票据池单位维护列表数据
     */
    @ApiOperation("票据池单位维护列表数据")
    @GetMapping("/list")
    public AjaxJson<AbPoolCorpListVO> list(AbPoolCorpVO queryVO) {
        AbPoolCorpListVO result = new AbPoolCorpListVO();
        result.setListInfo(abPoolCorpService.listAbPoolCorpVOTree(queryVO));
        result.setQueryVO(queryVO);
        return success(result);
    }

    /**
     * 票据池单位维护列表数据
     */
    @ApiOperation("票据池单位维护列表数据")
    @PostMapping("/listPoolCorp")
    public AjaxJson<AbPoolCorpListVO> listPoolCorp(@RequestBody AbPoolCorpVO queryVO) {
        AbPoolCorpListVO result = new AbPoolCorpListVO();
        result.setListInfo(abPoolCorpService.listAbPoolCorpVOTree(queryVO));
        result.setQueryVO(queryVO);
        return success(result);
    }

    /**
     * 获取树形结构单位信息
     */
    @ApiOperation("获取树形结构单位信息")
    @GetMapping("/treeData")
    public AjaxJson<Object> treeCorpData(String resource) {
        return success(abPoolCorpService.getCorpTreeMapList(resource, getCurrentUser()));
    }

    /**
     * 票据池单位新增页面数据
     */
    @ApiOperation("票据池单位新增页面数据")
    @GetMapping("/add")
    public AjaxJson<AbPoolCorpAddLowerVO> add() {
        AbPoolCorpAddLowerVO result = new AbPoolCorpAddLowerVO();
        result.setCorpIds((String) abPoolCorpService.listAllPoolCorpIds(null));
        return success(result);
    }

    /**
     * 下级单位新增页面数据
     */
    @ApiOperation("下级单位新增页面数据")
    @GetMapping("/addLower")
    public AjaxJson<AbPoolCorpAddLowerVO> addLower(String parentId, String bustypeLevel) {
        AbPoolCorpAddLowerVO result = new AbPoolCorpAddLowerVO();
        result.setParentId(parentId);
        result.setBustypeLevel(bustypeLevel);
        result.setCorpIds((String) abPoolCorpService.listAllPoolCorpIds(null));
        return success(result);
    }

    /**
     * 票据池单位修改页面数据
     */
    @ApiOperation("票据池单位修改页面数据")
    @GetMapping("/update")
    public AjaxJson<AbPoolCorpUpdateVO> update(String id) {
        AbPoolCorpUpdateVO result = new AbPoolCorpUpdateVO();
        result.setAbPoolCorpVO(abPoolCorpService.getAbPoolCorpInfo(id));
        result.setCorpIds((String) abPoolCorpService.listAllPoolCorpIds(id));
        result.setId(id);
        return success(result);
    }

    /**
     * 票据池单位维护新增或修改操作
     */
    @LogMessage(content = "票据池单位维护新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("票据池单位维护新增或修改操作")
    @PostMapping("/saveOrUpdate")
    public AjaxJson saveOrUpdate(@RequestBody AbPoolCorpVO infoVO) {
        return abPoolCorpService.saveOrUpdate(infoVO, getCurrentUser());
    }

    /**
     * 票据池单位删除
     */
    @ApiOperation("票据池单位删除")
    @PostMapping("/delete")
    public AjaxJson deleteBillPoolCorp(@RequestParam("ids") String ids) {
        return abPoolCorpService.deleteAbPoolCorp(ids, getCurrentUser());
    }

    /**
     * 票据池单位禁用或启用
     */
    @LogMessage(content = "启用/禁用", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @ApiOperation("票据池单位禁用或启用")
    @PostMapping("/disableOrEnable/{operate}")
    public AjaxJson disableOrEnable(@PathVariable("operate") String operate, @RequestParam("id") String id) {
        return abPoolCorpService.disableOrEnable(operate, id, getCurrentUser());
    }
}
