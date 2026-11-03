package com.bytter.biz.budget.saas.controller.budget;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.service.FbsCorpBudgetService;
import com.bytter.biz.budget.service.FbsCorpItemService;
import com.bytter.biz.budget.spi.dto.SaveCollectFlagDTO;
import com.bytter.biz.budget.spi.dto.SaveCtlBudgetDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.vo.CorpVO;
import com.bytter.biz.budget.vo.FbsCorpBudgetVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.biz.vo.FbsCorpBudgetListVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: SYS_CORP
 * @date 2017-06-09 15:24:05
 */
@Api(value = "/v1-plan/budget/fbscorpbudget", tags = "资金计划参数配置")
@RestController("v1FbsCorpBudgetController")
@RequestMapping("/v1-plan/budget/fbscorpbudget")
public class FbsCorpBudgetController extends BaseController {
    @Autowired
    private FbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @Autowired
    private FbsCorpItemService fbsCorpItemService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 查询
     *
     * @param type
     * @param seach
     * @return
     */
    @ApiOperation(value = "单位资金计划列表")
    @LogMessage(content = "单位资金计划列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryList/{type}")
    public AjaxJson<FbsCorpBudgetListVO> queryList(@PathVariable("type") Integer type, String seach) throws UnsupportedEncodingException {
        FbsCorpBudgetListVO vo = new FbsCorpBudgetListVO();
        if (StringUtils.isNotBlank(seach)) {
            seach = URLDecoder.decode(seach, "UTF-8");
        }
        vo.setCorpList(this.fbsCorpBudgetService.listCorpIdsBySeach(seach));
        vo.setCorpBudgetMap(this.fbsCorpBudgetService.listFbsCorpBudget(seach));
        vo.setSeach(seach);
        String forwardName = type == 1 ? "corpBudget" : "corpCollect";
        vo.setForwardName("budget/" + forwardName);
        return AjaxJson.success(vo);
    }

    /**
     * 单位资金计划控制设置
     */
    @ApiOperation(value = "单位资金计划控制设置")
    @GetMapping("/corpBudget")
    public AjaxJson<FbsCorpBudgetListVO> corpBudget() {
        FbsCorpBudgetListVO vo = new FbsCorpBudgetListVO();
        vo.setCorpList(this.fbsCorpBudgetService.listCorpIdsBySeach(null));
        vo.setCorpBudgetMap(this.fbsCorpBudgetService.listFbsCorpBudget(null));
        return AjaxJson.success(vo);
    }

    /**
     * 网点单位多选控件
     *
     * @param netId
     * @return
     */
    @ApiOperation(value = "网点单位多选控件")
    @GetMapping("/mulcorp")
    public String mulCorp(String netId) {
        FbsCorpBudgetListVO vo = new FbsCorpBudgetListVO();
        List listId = new ArrayList<>();
        List<CorpVO> listCorpVo = this.fbsCorpBudgetService.listCorp(netId);
        for (CorpVO corp : listCorpVo) {
            listId.add(corp.getId());
        }
        List<CorpDTO> listCorpDTO = corpService.listCorpDTOByCorpIds2(listId);
        if (StringUtils.isNotEmpty(netId)) {
            vo.setNetId(netId);
        } else {
            vo.setNetId("null");
        }
        vo.setCorpList(StringUtils.isNotEmpty(listCorpDTO) ? corpService.listCorpTree(listCorpDTO) : null);
        vo.setCorpMap(this.fbsCorpBudgetService.mapCorp(netId));
        return "budget/mulCorp";
    }

    /**
     * 单位资金计划汇总设置
     */
    @ApiOperation(value = "单位资金计划汇总设置")
    @GetMapping("/corpCollect")
    public AjaxJson<FbsCorpBudgetListVO> corpCollect() {
        FbsCorpBudgetListVO vo = new FbsCorpBudgetListVO();
        vo.setCorpList(this.fbsCorpBudgetService.listCorpIdsBySeach(null));
        vo.setCorpBudgetMap(this.fbsCorpBudgetService.listFbsCorpBudgets(null));
        return AjaxJson.success(vo);
    }

    /**
     * 计划控制保存
     *
     * @return
     */
    @ApiOperation(value = "计划控制保存")
    @LogMessage(content = "添加单位资金计划控制", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveCtlBudget")
    public AjaxJson saveCtlBudget(@RequestBody SaveCtlBudgetDTO param) {
        List<String> oldCorpIds = this.fbsCorpBudgetService.listCorpIdsByReportBudget(null);
        List<String> coprIdList = this.getCorpIdByCancel(oldCorpIds, param.getReportCorpIds());
        if (!EmptyUtils.isEmptyList(coprIdList)) {
            StringBuilder corpNames = new StringBuilder();
            for (String corpId : coprIdList) {
                //判断单位是否上报过计划
                if (fbsBudgetService.isExistBudget(corpId)) {
                    CorpDTO dto = this.corpService.get(corpId);
                    if (!EmptyUtils.isEmpty(dto)) {
                        if (EmptyUtils.isBlank(corpNames.toString())) {
                            corpNames.append(dto.getName());
                        } else {
                            corpNames.append(CommonConstant.COMMA_SPLIT).append(dto.getName());
                        }
                    }
                }
            }
            if (!EmptyUtils.isBlank(corpNames.toString())) {
                throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_INPUT_MESSAGE_CANCEL, corpNames.toString());
            }
        }
        fbsCorpBudgetService.saveCtlBudget(param.getReportCorpIds(), param.getControlCorpIds());
        return success();
    }

    /**
     * 获取取消勾选的单位
     *
     * @param oldCorpIdList
     * @param newCorpIdList
     * @return
     */
    public List<String> getCorpIdByCancel(List<String> oldCorpIdList, List<String> newCorpIdList) {
        List<String> coprIdList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (String corpId : newCorpIdList) {
            map.put(corpId, corpId);
        }
        for (String corpId : oldCorpIdList) {
            if (EmptyUtils.isEmpty(map.get(corpId))) {
                coprIdList.add(corpId);
            }
        }
        return coprIdList;
    }

    /**
     * 计划汇总保存
     *
     * @return
     */
    @ApiOperation(value = "计划汇总保存")
    @LogMessage(content = "单位资金计划汇总设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveCollectFlag")
    public AjaxJson saveCollectFlag(@RequestBody SaveCollectFlagDTO dto) {
        if (ObjectUtil.isNotEmpty(dto.getCorpIds())) {
            for (String corpId : dto.getCorpIds()) {
                CorpDTO corp = corpService.get(corpId);
                if (StringUtils.isNotEmpty(corp.getParentId())) {
                    CorpDTO parentCorp = corpService.get(corp.getParentId());
                    List<FbsCorpItem> list = fbsCorpItemService.getItemCode(corpId);//本单位科目
                    List<FbsCorpItem> parentList = fbsCorpItemService.getItemCode(corp.getParentId());//上级单位科目
                    if (!EmptyUtils.isEmptyList(list) && !EmptyUtils.isEmptyList(parentList)) { //上级和下级都有科目，比较长度，查看下级科目是否上级中也有，没有返回false;
                        if (list.size() > parentList.size()) {//子单位科目比父科目多，不允许汇总
                            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, corp.getCode(), parentCorp.getCode());
                        } else {
                            HashMap<String, FbsCorpItem> itemMap = new HashMap<String, FbsCorpItem>();//本单位分配的科目
                            HashMap<String, FbsCorpItem> parentItemMap = new HashMap<String, FbsCorpItem>();//上级单位分配的科目
                            for (FbsCorpItem item : list) {
                                itemMap.put(item.getItemCode(), item);
                            }
                            for (FbsCorpItem item : parentList) {
                                parentItemMap.put(item.getItemCode(), item);
                            }
                            for (Entry<String, FbsCorpItem> entry : itemMap.entrySet()) {
                                if (!parentItemMap.containsKey(entry.getKey())) {
                                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, corp.getCode(), parentCorp.getCode());
                                }
                            }
                        }
                    } else if (!EmptyUtils.isEmptyList(list) && EmptyUtils.isEmptyList(parentList)) {
                        throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, corp.getCode(), parentCorp.getCode());
                    } else if (EmptyUtils.isEmptyList(list) && !EmptyUtils.isEmptyList(parentList)) {
                        throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_INPUT_MESSAGE_CHILD_CORP_ITMECODE, corp.getCode());
                    }
                } else {
                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_CORP_MESSAGE_ISPARENT, corp.getCode());
                }
            }
        }
        fbsCorpBudgetService.saveCollectFlag(dto.getCorpIds());
        return success();
    }

    /**
     * 有效单位列表查询
     *
     * @return
     */
    @ApiOperation(value = "有效单位列表查询")
    @GetMapping("getlistEffectiveCorp")
    public AjaxJson getlistEffectiveCorp() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        Map<String, Object> map = new HashMap<>();
        map.put("corpAll", this.fbsCorpBudgetService.listFbsCorpBudget(""));
        j.setObj(map);
        return j;
    }

    /**
     * 初始化零计划
     *
     * @param corpId
     * @return
     */
    @ApiOperation(value = "初始化零计划")
    @LogMessage(content = "单位资金计划汇总设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/initZeroBudget/{corpId}")
    public AjaxJson initZeroBudget(@PathVariable("corpId") String corpId) {
        //计划不控制单位不允许初始化计划
        if (!fbsCorpBudgetService.reportBudgetByCorpId(corpId)) {
            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_INPUT_MESSAGE_NOTCTLBUDGET);
        }
        //获取单位分配的科目
        List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpId(corpId);
        if (EmptyUtils.isEmptyList(listFbsItem)) {
            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_INPUT_MESSAGE_ITMECODE);
        }
        //判断单位是否上报过计划
        if (fbsBudgetService.isExistBudget(corpId)) {
            throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_INPUT_MESSAGE_ZERO);
        }
        fbsCorpBudgetService.initZeroBudget(corpId, this.getCurrentUser());
        return success();
    }


    @GetMapping("/tree")
    public AjaxJson<List<Map<String, Object>>> tree(String type) {
        List<FbsCorpBudgetVO> list = null;
        if ("controlBudget".equalsIgnoreCase(type)) {
            list = this.fbsCorpBudgetService.listFbsCorpBudgetDTOByControlBudget();
        } else if ("collectFlag".equalsIgnoreCase(type)) {
        } else {
            list = this.fbsCorpBudgetService.listFbsCorpBudgetVOByReportBudget();
        }
        List<Map<String, Object>> tempList = new ArrayList<>();
        if (EmptyUtils.isEmptyList(list)) {
            return AjaxJson.success(tempList);
        }
        for (FbsCorpBudgetVO vo : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", vo.getCorpId());
            map.put("pId", vo.getCorpParentId());
            map.put("name", vo.getCorpName());
            map.put("type", vo.getCorpType());
            map.put("code", vo.getCorpCode());
            map.put("netId", vo.getCorpNetId());
            tempList.add(map);
        }
        return AjaxJson.success(tempList);
    }
}
