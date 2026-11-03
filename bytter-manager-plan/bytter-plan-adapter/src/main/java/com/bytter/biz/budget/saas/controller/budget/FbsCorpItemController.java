package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.service.FbsCorpItemService;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.budget.vo.FbsCorpItemVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.biz.vo.CorpListVO;
import com.bytter.biz.vo.FbsCorpItemAddVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FBS_CORP_ITEM
 * @date 2017-07-11 10:46:42
 */
@Api(value = "/v1-plan/budget/fbscorpitem", tags = "科目")
@RestController("1FbsCorpItemController")
@RequestMapping("/v1-plan/budget/fbscorpitem")
public class FbsCorpItemController extends BaseController {
    @Autowired
    private FbsCorpItemService fbsCorpItemService;
    @DubboReference(check = false)
    private IFbsItemService fbsItemService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
    private IConfigService iConfigService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;

    /**
     * FBS_BUDGET_PERIOD新增页面跳转
     *
     * @return
     */

    @GetMapping("/add")
    public AjaxJson<FbsCorpItemAddVO> add() {
        FbsCorpItemAddVO model = new FbsCorpItemAddVO();
        List<FbsCorpItem> itemCorp = fbsCorpItemService.isItem();
        model.setItemCorp(itemCorp);
        //默认所有的有效计划科目
        List<FbsItemDTO> list = fbsItemService.listFbsItemInfo("1");
        model.setListItem(list);
        return AjaxJson.success(model);
    }

    /**
     * 添加FBS_CORP_ITEM
     *
     * @return
     */

    @LogMessage(content = "添加FBS_CORP_ITEM", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestParam("corpId") String corpIds, @RequestParam("itemCodes[]") List<String> itemCodes) {
        for (String corpId : corpIds.split(CommonConstant.COMMA_SPLIT)) {
            if (EmptyUtils.isEmpty(itemCodes)) {
                itemCodes.add("");
            }
            //本单位分配的科目
            HashMap<String, String> itemMap = new HashMap<String, String>();
            for (String itemCode : itemCodes) {
                itemMap.put(itemCode, itemCode);
            }
            //获取当前分配科目单位
            CorpDTO curCorp = iCorpService.get(corpId);
            //判断单位是否需要汇总
            if (fbsCorpBudgetService.getCollectFlag(corpId)) {
                CorpDTO parentCorp = iCorpService.get(curCorp.getParentId());
                //获取上级单位已分配的科目
                List<FbsCorpItem> parentItemList = fbsCorpItemService.getItemCode(curCorp.getParentId());
                if (!EmptyUtils.isEmptyList(parentItemList)) {
                    if (parentItemList.size() < itemCodes.size()) {
                        throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), parentCorp.getCode());
                    } else {
                        HashMap<String, String> parentItemMap = new HashMap<String, String>();
                        //上级单位分配的科目
                        for (FbsCorpItem item : parentItemList) {
                            parentItemMap.put(item.getItemCode(), item.getItemCode());
                        }
                        for (Map.Entry<String, String> entry : itemMap.entrySet()) {
                            if (!EmptyUtils.isBlank(entry.getKey()) && !parentItemMap.containsKey(entry.getKey())) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), parentCorp.getCode());
                            }
                        }
                    }
                } else {
                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, curCorp.getCode(), parentCorp.getCode());
                }
            }
            //判断单位是否存在下级要汇总单位
            if (fbsCorpBudgetService.getChildCollectFlag(corpId)) {
                List<FbsCorpBudgetDTO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(corpId);
                if (!EmptyUtils.isEmptyList(list)) {
                    for (FbsCorpBudgetDTO dto : list) {
                        if (dto.getReportBudget() != 0 && dto.getCollectFlag() == 1) {
                            List<FbsCorpItem> childItemList = fbsCorpItemService.getItemCode(dto.getCorpId());
                            HashMap<String, String> childItemMap = new HashMap<String, String>();
                            //下级级单位分配的科目
                            for (FbsCorpItem item : childItemList) {
                                childItemMap.put(item.getItemCode(), item.getItemCode());
                            }
                            for (Map.Entry<String, String> entry : childItemMap.entrySet()) {
                                if (!itemMap.containsKey(entry.getKey())) {
                                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_CHILDRENCORPMOREPARENT, dto.getCorpCode(), curCorp.getCode());
                                }
                            }
                        }
                    }
                }
            }
            //判断单位是否存已经上报的计划
            if(fbsBudgetService.isExistBudget(corpId)){
                //获取单位原科目集合
                List<FbsCorpItem> itemcodeList = fbsCorpItemService.getItemCode(corpId);
                if (itemcodeList.size() > itemCodes.size()) {
                    throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_ALREADYEXIST,curCorp.getCode());
                }
            }
            fbsCorpItemService.saveCorpItem(corpId, itemCodes, this.getCurrentUser());
        }
        return success();

      //  fbsCorpItemService.fbscorpitemSave(corpIds.split(CommonConstant.COMMA_SPLIT), itemCodes, this.getCurrentUser());
       // return success();
    }

    /**
     * 根据单位id查找相应的科目
     *
     * @param id
     * @return
     * @throws Exception
     */
    @LogMessage(content = "查询预算科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/corpList")
    public AjaxJson<CorpListVO> corpList(String id) throws Exception {
        CorpListVO model = new CorpListVO();
        List<FbsCorpItem> itemCorp = fbsCorpItemService.isItem();
        model.setItemCorp(itemCorp);
        //查出单位id的性质
        boolean isSubCorp = fbsCorpBudgetService.collectFlagByCorpId(id);
        model.setIsSubCorp(isSubCorp);
        Map<String, String> corpItemCode = getCorpItem(id, isSubCorp);
        model.setSuperCorpItemCode(corpItemCode);
        //所有的有效计划科目
        List<FbsItemDTO> list = fbsItemService.listFbsItemInfo("1");
        model.setListItem(list);
        //根据单位id查找已分配的计划科目
        List<FbsCorpItemVO> fbs = fbsCorpItemService.getAlreadyCorpItem(id);
        model.setFbs(fbs);
        model.setCorpId(id);
        return AjaxJson.success(model);
    }

    /**
     * 获取业务类别信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/getCheckItem")
    @ResponseBody
    public AjaxJson<List<FbsItemDTO>> getCheckItem(String ids, String isBatch) throws Exception {
        if ("0".equals(isBatch)) {
            //查出单位id的性质
            boolean isSubCorp = fbsCorpBudgetService.collectFlagByCorpId(ids);
            Map<String, String> corpItemCode = getCorpItem(ids, isSubCorp);
            //所有的有效计划科目
            List<FbsItemDTO> list = fbsItemService.listFbsItemInfo("1");
            //根据单位id查找已分配的计划科目
            List<FbsCorpItemVO> fbs = fbsCorpItemService.getAlreadyCorpItem(ids);
            return AjaxJson.success(getList(list, isSubCorp, corpItemCode, fbs));
        } else {//多个
            Map<String, String> corpItemCodes = new HashMap<>();
            Map<String, String> showCodes = new HashMap<>();
            boolean isSubCorp = false;
            List<FbsCorpItemVO> showList = new ArrayList<>();
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                //查出单位id的性质(是否是汇总科目)
                isSubCorp = fbsCorpBudgetService.collectFlagByCorpId(id);
                if (isSubCorp) {
                    isSubCorp = true;
                }
                //汇总单位,取上级单位分配科目
                //不汇总,取所有有效科目
                Map<String, String> corpItemCode = getCorpItem(id, isSubCorp);
                if (corpItemCodes.isEmpty()) {
                    corpItemCodes.putAll(corpItemCode);
                } else {
                    for (String keyCode : corpItemCode.keySet()) {
                        if (corpItemCodes.containsKey(keyCode)) {
                            showCodes.put(keyCode, corpItemCode.get(keyCode));
                        }
                    }
                    corpItemCodes.clear();
                    corpItemCodes.putAll(showCodes);
                    showCodes.clear();
                }
                List<FbsCorpItemVO> fbs = fbsCorpItemService.getAlreadyCorpItem(id);
                showList.addAll(fbs);
            }
            //所有的有效计划科目
            List<FbsItemDTO> list = fbsItemService.listFbsItemInfo("1");
            return AjaxJson.success(getList(list, isSubCorp, corpItemCodes, showList));
        }
    }

    /**
     * 绘制选中科目数据
     *
     * @param list         所有的有效计划科目
     * @param isSubCorp    是否汇总单位
     * @param corpItemCode 根据是否汇总科目取计划科目，如果是汇总单位则取父单位分配的所有计划科目
     * @param fbs          单位id查找已分配的计划科目
     * @return
     */
    public List<FbsItemDTO> getList(List<FbsItemDTO> list, boolean isSubCorp, Map<String, String> corpItemCode, List<FbsCorpItemVO> fbs) {
        for (FbsItemDTO data : list) {
            if (isSubCorp && StringUtils.isBlank(corpItemCode.get(data.getItemCode()))) {
                continue;
            }
            for (FbsCorpItemVO f : fbs) {
                if (f.getItemCode().equals(data.getItemCode())) {
                    data.setChecked(true);
                }
            }
        }
        return list;
    }

    /**
     * 根据是否汇总科目取计划科目
     * 如果是汇总单位则取父单位分配的所有计划科目
     *
     * @param corpId
     * @return
     * @throws Exception
     */
    public HashMap getCorpItem(String corpId, boolean isSubCorp) throws Exception {
        HashMap corpItemCode = new HashMap();
        if (isSubCorp) {
            //汇总单位,取上级单位分配科目
            CorpDTO parentCrop = iCorpService.getParentById(corpId);
            List<FbsCorpItem> listCorpItem = fbsCorpItemService.getItemCode(parentCrop.getId());
            for (FbsCorpItem it : listCorpItem) {
                corpItemCode.put(it.getItemCode(), it.getItemCode());
            }
        } else {
            //不汇总,取所有有效科目
            List<FbsItemDTO> listItem = fbsItemService.listFbsItemInfo("1");
            for (FbsItemDTO it : listItem) {
                corpItemCode.put(it.getItemCode(), it.getItemCode());
            }
        }
        return corpItemCode;
    }

    @GetMapping("/tree")
    @ResponseBody
    public AjaxJson<List<Map<String, Object>>> tree(String corpId, String itemCode, Date processDate, String sign) {
        String itemCodes = "";
        if (StringUtils.isNotBlank(sign)) {
            itemCodes = fbsItemService.getFbsItemForProcess(Integer.valueOf(sign));
        }
        List<FbsItemVO> itemList = null;
        HashMap<String, FbsItemVO> itemMap = new HashMap<>();
        String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
        for (int i = 0; i < corpIds.length; i++) {
            List<FbsItemVO> itemListTemp = fbsCorpItemService.getAllItemByCorpId(corpIds[i]);
            if (i == 0) {
                itemList = itemListTemp;
                for (FbsItemVO itemVO : itemListTemp) {
                    itemMap.put(itemVO.getItemCode(), itemVO);
                }
            } else {
                HashMap<String, FbsItemVO> itemMapTemp = new HashMap<>();
                for (FbsItemVO itemVO : itemListTemp) {
                    itemMapTemp.put(itemVO.getItemCode(), itemVO);
                    if (!itemMap.containsKey(itemVO.getItemCode())) {
                        itemMap.remove(itemVO.getItemCode());
                        itemList.remove(itemVO);
                    }
                }
                for (FbsItemVO itemVO : itemList) {
                    if (!itemMapTemp.containsKey(itemVO.getItemCode())) {
                        itemMap.remove(itemVO.getItemCode());
                    }
                }
                itemList = new ArrayList<>();
                for (String key : itemMap.keySet()) {
                    itemList.add(itemMap.get(key));
                }
            }
        }
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        if (EmptyUtils.isEmptyList(itemList)) {
            return AjaxJson.success(tempList);
        }
        if (!EmptyUtils.isEmpty(processDate)) {
            // 校验预算周期当前时间是否创建
            String timeType = this.iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
            //根据执行日期获取计划周期
            FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
            for (int i = 0; i < corpIds.length; i++) {
                FbsBudget fbsBudget = this.fbsBudgetService.getCorpBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
                //当前执行周期的计划未编制或者未编制完成时判断计划执行规则
                if (EmptyUtils.isEmpty(fbsBudget) || (fbsBudget != null && fbsBudget.getStatus() < BudgetConstant.STATUE_CHECK)) {
                    //针对本期计划执行
                    if (DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getBeginDate()) >= 0
                            && DateUtils.dayDiffs(DateUtils.getDateYMD(), fbsBudgetPeriod.getEndDate()) <= 0) {
                        FbsBudgetVO fbsBudgetVO = this.fbsBudgetService.buildBudget(corpIds[i], timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), true);
                        if (fbsBudgetVO != null) {
                            fbsBudget = new FbsBudget();
                            BeanUtils.copyNotNullProperties(fbsBudgetVO, fbsBudget);
                        } else {
                            return AjaxJson.success(tempList);
                        }
                    }
                }
                if (EmptyUtils.isEmpty(fbsBudget)) {
                    return AjaxJson.success(tempList);
                }
            }
        }
        for (FbsItemVO item : itemList) {
            if (StringUtils.isEmpty(itemCode)) {
                if (StringUtils.isNotBlank(itemCodes) && itemCodes.indexOf(item.getItemCode().substring(0, 3)) == -1) {
                    continue;
                }
            } else {
                boolean flag = false;
                for (String code : itemCode.split(";")) {
                    if (item.getItemCode().startsWith(code) && item.getItemCode().indexOf(code) > -1) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    continue;
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.getItemCode());
            map.put("pId", item.getSuperItemCode());
            map.put("name", item.getItemName());
            map.put("code", item.getItemCode());
            map.put("sign", item.getSign());
            map.put("isInternal", item.getIsInternal());
            map.put("isControl", item.getIsControl());
            map.put("isSystem", item.getIsSystem());
            map.put("isRigid", item.getIsRigid());
            map.put("isBeginPeriod", item.getIsBeginPeriod());
            map.put("isEndPeriod", item.getIsEndPeriod());
            map.put("overPerSpend", item.getOverPerSpend());
            map.put("itemRelation", item.getItemRelation());
            if ("1".equals(item.getIsWrite())) {
                item.setCanSelect(true);
            }
            map.put("canSelect", item.isCanSelect());
            tempList.add(map);
        }
        return AjaxJson.success(tempList);
    }
}
