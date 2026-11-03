package com.bytter.system.bankacc.controller;
import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.BankTypeByAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.AllocationConstant;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccCur;
import com.bytter.system.service.BtBankAccCommonService;
import com.bytter.system.service.BtBankAccRelationService;
import com.bytter.system.vo.BtBankAccTreeVO;
import com.bytter.system.vo.TreeVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC 银行账号父子关系维护
 * @date 2017-06-02 17:50:16
 */
@Controller
@RequestMapping("/bankacc/btbankaccrelation")
public class BtBankAccRelationController extends BaseController {
    @Autowired
    private BtBankAccRelationService btBankAccRelationService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private BtBankAccCommonService btBankAccCommonService;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    @Autowired
    private BtCurrencyService btCurrencyService;
    /**
     * BT_BANK_ACC列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY},
//            logical = Logical.OR)
    @LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{poolSign}")
    public String list(@PathVariable("poolSign") String poolSign, Model model) {
        //资金池标识 0-非资金池；1-资金池
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
            setTreeForPool(null, null, null, poolSign, model);
        }else{
            setTree(null, null, null, model);
        }

        model.addAttribute("poolSign", poolSign);
        return "bankacc/listBtBankAccRelation";
    }
    @LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/listBycorpId")
    @ResponseBody
    public AjaxJson listBycorpId(BtBankAccVO btBankAccVO, Model model) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        //资金池标识 0-非资金池；1-资金池
        BtBankAccTreeVO  btBankAccTreeVO = null;
        if(AllocationConstant.POOL_SIGN_1.equals(btBankAccVO.getPoolSign())) {
            btBankAccTreeVO = setTreeForPool(btBankAccVO.getNetId(), btBankAccVO.getCorpId(), btBankAccVO.getCurId(), btBankAccVO.getPoolSign(),model);
        }else{
            btBankAccTreeVO = setTree(btBankAccVO.getNetId(), btBankAccVO.getCorpId(), btBankAccVO.getCurId(),model);
        }
        btBankAccTreeVO.setPoolSign(btBankAccVO.getPoolSign());
        //资金池标识 0-非资金池；1-资金池
        //model.addAttribute("poolSign", btBankAccVO.getPoolSign());
        ajaxJson.setObj(btBankAccTreeVO);
        return ajaxJson;
    }
    /**
     * 组装树结构
     *
     * @param netId
     * @param corpId
     * @param curId
     */
    public BtBankAccTreeVO setTree(String netId, String corpId, String curId, Model model) {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start("get corpList:");
        List<CorpDTO> corpList = new ArrayList<>();
        if (!EmptyUtils.isBlank(corpId)) {
            CorpDTO corp = corpServiceI.get(corpId.trim());
            corpList.add(corp);
        } else if (!EmptyUtils.isEmpty(netId)) {
            corpList.add(corpServiceI.get(netId.trim()));
            //List<CorpDTO> corpListTemp = corpServiceI.listCorpByNetId(netId.trim());
            List<CorpDTO> corpListTemp = corpServiceI.listCorpBasicInfoByNetId(netId.trim());
            corpList.addAll(corpListTemp);
        } else {
            //corpList = corpServiceI.listEffectiveCorp();
            corpList = corpServiceI.listEffectiveCorpBasicInfo();
        }
        List<TreeVO> treeList = null;
        stopWatch.stop();
        if(!EmptyUtils.isEmpty(netId)) {
            // 过滤单位，只取具有资源权限的单位
            stopWatch.start("listUserCorp:");
            //List<CorpDTO> resourceCorpList = corpServiceI.listUserCorp(ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY, getUserId());
            List<CorpDTO> resourceCorpList = corpServiceI.listUserResourceCorps(ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY, getUserId());
            stopWatch.stop();
            stopWatch.start("listBankAccInfosByCorpId :");
            //获取在线账号
            List<BtBankAccVO> bankAccList = btBankAccRelationService.listBankAccInfosByCorpId(null, curId, BtBankAccConstant.ISONLINE_1);
            stopWatch.stop();
            stopWatch.start("listBankAccInfosByCorpId for allBankAccList:");
            //获取所有有效账号
            List<BtBankAccVO> allBankAccList = btBankAccRelationService.listBankAccInfosByCorpId(null, curId,null);
            stopWatch.stop();
            stopWatch.start("get bankAcc by corpId:");
            corpList.retainAll(resourceCorpList);
            treeList = BeanUtils.copyListProperties(corpList, TreeVO.class);
            Map<String,List<BtBankAccVO>> cropId2BtBankAccVO= Maps.newHashMap();
            Map<String,List<BtBankAccVO>> prentAccIdCurId2BtBankAccVO= Maps.newHashMap();
            //添加币别
            List<BtCurrencyVO> btCurrencys = btCurrencyService.getBtCurrency();
            Map<String, String> btCurrencyMap = btCurrencys.stream().collect(Collectors.toMap(BtCurrencyVO::getId, BtCurrencyVO::getCurName));
            if(EmptyUtils.isNotEmpty(bankAccList)) {
                for (BtBankAccVO bankAccVO : bankAccList) {
                    List<BtBankAccVO> bankAccVOListForCorpId = cropId2BtBankAccVO.get(bankAccVO.getCorpId());
                    if (bankAccVOListForCorpId == null) {
                        bankAccVOListForCorpId = Lists.newArrayList();
                        cropId2BtBankAccVO.put(bankAccVO.getCorpId(), bankAccVOListForCorpId);
                    }
                    bankAccVO.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAccVO.getCurId())) ? "" : (String)btCurrencyMap.get(bankAccVO.getCurId()));
                    bankAccVOListForCorpId.add(bankAccVO);
                }
            }
            if(EmptyUtils.isNotEmpty(allBankAccList)){
                for(BtBankAccVO bankAccVO : allBankAccList){
                    String prentAccIdCurId=bankAccVO.getPrentAccId() + CommonConstant.COLON_SPLIT + bankAccVO.getCurId();
                    List<BtBankAccVO> bankAccVOListForPrentAccIdCurId = prentAccIdCurId2BtBankAccVO.get(prentAccIdCurId);
                    if (bankAccVOListForPrentAccIdCurId == null) {
                        bankAccVOListForPrentAccIdCurId = Lists.newArrayList();
                        prentAccIdCurId2BtBankAccVO.put(prentAccIdCurId, bankAccVOListForPrentAccIdCurId);
                    }
                    bankAccVO.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAccVO.getCurId())) ? "" : (String)btCurrencyMap.get(bankAccVO.getCurId()));
                    bankAccVOListForPrentAccIdCurId.add(bankAccVO);
                }
            }
            /**
             * 根据单位得到银行账号
             */
            for (TreeVO treeVO : treeList) {
                List<BtBankAccVO> accList = cropId2BtBankAccVO.get(treeVO.getId());
                if (!EmptyUtils.isEmptyList(accList)) {
                    treeVO.setAccList(accList);
                    for (BtBankAccVO bankAccVO : accList) {
                        //List<BtBankAccVO> childList = this.getChildBankAccList(bankAccVO.getId(), bankAccVO.getCurId(), allBankAccList);
                        List<BtBankAccVO> childList = prentAccIdCurId2BtBankAccVO.get(bankAccVO.getId()+ CommonConstant.COLON_SPLIT +bankAccVO.getCurId());
                        bankAccVO.setChildList(childList);
                    }
                }
            }
            stopWatch.stop();
            cropId2BtBankAccVO = null;
            prentAccIdCurId2BtBankAccVO = null;
        }
        stopWatch.start("tree order :");
        //逻辑排序，将treeList重新排序，解决前端展开错乱的问题
        List<TreeVO> newList = new ArrayList<>();
        if(treeList!=null){
            newList.add(treeList.get(0));
            for (TreeVO vo : treeList) {
                if (newList.get(0).getId().equals(vo.getParentId())) {
                    newList.add(vo);
                }
            }
            for(int i =1;i<treeList.size();i++) {
                if (!newList.get(0).getId().equals(treeList.get(i).getParentId())) {
                    for(int j=1;j<newList.size();j++){
                        if(newList.get(j).getId().equals(treeList.get(i).getParentId())){
                            newList.add(j+1,treeList.get(i));
                            break;
                        }
                    }
                }
            }
        }

        BtBankAccTreeVO btBankAccTreeVO = new BtBankAccTreeVO();
        btBankAccTreeVO.setTreeList(newList);
        btBankAccTreeVO.setNetId(netId);
        btBankAccTreeVO.setCorpId(corpId);
        btBankAccTreeVO.setCurId(curId);
        model.addAttribute("treeList", newList);
        model.addAttribute("netId", netId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("curId", curId);
        stopWatch.stop();
        logger.info("setTree total spends:"+stopWatch.prettyPrint());
        return btBankAccTreeVO;
    }
    /**
     * 获取父账号
     * @param corpId
     * @param bankAccList
     * @return
     */
    public List<BtBankAccVO> getBankAccByCorpId(String corpId, List<BtBankAccVO> bankAccList){
        List<BtBankAccVO> accList = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(bankAccList)) {
            for (BtBankAccVO bankAccVO : bankAccList) {
                if(corpId.equals(bankAccVO.getCorpId())){
                    accList.add(bankAccVO);
                }
            }
        }
        return accList;
    }
    /**
     * 获取子账号
     * @param bankAccId
     * @param bankAccList
     * @return
     */
    public List<BtBankAccVO> getChildBankAccList(String bankAccId, String curId, List<BtBankAccVO> bankAccList){
        List<BtBankAccVO> accList = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(bankAccList)) {
            for (BtBankAccVO bankAccVO : bankAccList) {
                if(bankAccId.equals(bankAccVO.getPrentAccId()) && curId.equals(bankAccVO.getCurId())){
                    accList.add(bankAccVO);
                }
            }
        }
        return accList;
    }
    /**
     * 组装树结构 资金池
     *
     * @param netId
     * @param corpId
     * @param model
     */
    public BtBankAccTreeVO setTreeForPool(String netId, String corpId, String curId, String poolSign, Model model) {
        //是否资金池账号
        String isCapitalPool = "";
        if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
            isCapitalPool = BtBankAccConstant.ISCAPITALPOOL_1;
        }
        List<FcCorpPoolDTO> fcCorpPoolDTOList = new ArrayList<>();
        String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY;
        if (!EmptyUtils.isBlank(corpId)) {
            fcCorpPoolDTOList.add(fcCorpPoolServiceI.getById(netId));
            FcCorpPoolDTO corpPoolDTO = fcCorpPoolServiceI.getChildCorpByPoolId(netId, corpId);
            fcCorpPoolDTOList.add(corpPoolDTO);
        }else if(!EmptyUtils.isBlank(netId)){
            //获取有资源权限的资金池子单位
            List<FcCorpPoolDTO> userCorpPoolChilds = fcCorpPoolServiceI.getUserCorpPoolChilds(getCurrentUser(), resource, netId);
            fcCorpPoolDTOList.add(fcCorpPoolServiceI.getById(netId));
            fcCorpPoolDTOList.addAll(userCorpPoolChilds);
        }else{
            //获取有资源权限的资金池
            List<FcCorpPoolDTO> userCorpPools = fcCorpPoolServiceI.getUserCorpPools(getCurrentUser(), resource);
            for(FcCorpPoolDTO fcCorpPoolDTO : userCorpPools){
                fcCorpPoolDTOList.add(fcCorpPoolDTO);
                //获取有资源权限的资金池子单位
                List<FcCorpPoolDTO> userCorpPoolChilds = fcCorpPoolServiceI.getUserCorpPoolChilds(getCurrentUser(), resource, fcCorpPoolDTO.getId());
                fcCorpPoolDTOList.addAll(userCorpPoolChilds);
            }
        }
        List<TreeVO> treeList = null;
        List<BtCurrencyVO> btCurrencys = btCurrencyService.getBtCurrency();
        Map<String, String> btCurrencyMap = btCurrencys.stream().collect(Collectors.toMap(BtCurrencyVO::getId, BtCurrencyVO::getCurName));
        if(!EmptyUtils.isBlank(netId)) {
            treeList = this.convertCorpDTO(fcCorpPoolDTOList);
            /**
             * 根据单位得到银行账号
             */
            for (TreeVO treeVO : treeList) {
                //获取有资源权限的资金池子单位
                List<FcCorpPoolDTO> userCorpPoolChilds = fcCorpPoolServiceI.getUserCorpPoolChilds(getCurrentUser(), resource, treeVO.getId());
                HashMap<String, FcCorpPoolDTO> poolChildMap = new HashMap<>();
                for (FcCorpPoolDTO poolDTO : userCorpPoolChilds) {
                    poolChildMap.put(poolDTO.getCorpId(), poolDTO);
                }
                List<BtBankAccVO> accList = btBankAccRelationService.listBankAccByCorpId(treeVO.getCorpId(), curId, BtBankAccConstant.ISONLINE_1,isCapitalPool);
                if (!EmptyUtils.isEmptyList(accList)) {
                    treeVO.setAccList(accList);
                    for (BtBankAccVO bankAccVO : accList) {
                        List<BtBankAccVO> childList = btBankAccRelationService.getChildBankAccList(bankAccVO.getId(), bankAccVO.getCurId());
                        List<BtBankAccVO> childListTemp = new ArrayList<>();
                        for (BtBankAccVO bankAcc : childList) {
                            //过滤资金池子单位的子账号
                        	bankAcc.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAcc.getCurId())) ? "" : (String)btCurrencyMap.get(bankAcc.getCurId()));
                            if (poolChildMap.containsKey(bankAcc.getCorpId())) {
                                childListTemp.add(bankAcc);
                            }
                        }
                        //添加币别名称
                        bankAccVO.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAccVO.getCurId())) ? "" : (String)btCurrencyMap.get(bankAccVO.getCurId()));
                        bankAccVO.setChildList(childListTemp);
                    }
                }
            }
        }
        model.addAttribute("treeList", treeList);
        model.addAttribute("netId", netId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("curId", curId);

        BtBankAccTreeVO btBankAccTreeVO = new BtBankAccTreeVO();
        btBankAccTreeVO.setTreeList(treeList);
        btBankAccTreeVO.setNetId(netId);
        btBankAccTreeVO.setCorpId(corpId);
        btBankAccTreeVO.setCurId(curId);
        return btBankAccTreeVO;
    }
    /**
     * 资金池单位转换
     * @param fcCorpPoolDTOList
     * @return
     */
    private List<TreeVO> convertCorpDTO(List<FcCorpPoolDTO> fcCorpPoolDTOList){
        List<TreeVO> list = new ArrayList<>();
        for(FcCorpPoolDTO fcCorpPoolDTO:fcCorpPoolDTOList){
            TreeVO tree = new TreeVO();
            tree.setId(fcCorpPoolDTO.getId());
            if(AllocationConstant.POOL_SIGN_1.equals(fcCorpPoolDTO.getPoolSign())) {
                //资金池主体单位显示资金池编码+名称
                tree.setCode(fcCorpPoolDTO.getPoolCode());
                tree.setName(fcCorpPoolDTO.getPoolName());
            }else{
                //子单位显示子单位编码+名称
                tree.setCode(fcCorpPoolDTO.getCorpCode());
                tree.setName(fcCorpPoolDTO.getCorpName());
                tree.setParentId(fcCorpPoolDTO.getPoolId());
            }
            tree.setPoolSign(fcCorpPoolDTO.getPoolSign());
            tree.setCorpId(fcCorpPoolDTO.getCorpId());
            list.add(tree);
        }
        return list;
    }
    /**
     * BT_BANK_ACC table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String poolId = (String) dataTablesInput.getFormdata().get("poolId");
        if(!StringUtils.isBlank(poolId)){
            return btBankAccRelationService.getDataTablesUnionForPool(dataTablesInput, this.getCurrentUser());
        }else {
            return btBankAccRelationService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        }
    }
    /**
     * BT_BANK_ACC编辑页面跳转
     *
     * @return
     */
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_APPLY},
//            logical = Logical.OR)
    @GetMapping("/update")
    public String update(BtBankAcc btBankAcc, Model model) {
        if (StringUtils.isNotEmpty(btBankAcc.getId())) {
            BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
            model.addAttribute("btBankAccPage", vo);
            model.addAttribute("btBankAcc", btBankAcc);
            model.addAttribute("poolSign", AllocationConstant.POOL_SIGN_0);
        }
        return "bankacc/updateBtBankAccRelation";
    }
    /**
     * BT_BANK_ACC编辑页面跳转
     *
     * @return
     */
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_APPLY},
//            logical = Logical.OR)
    @GetMapping("/updateForPool")
    public String updateForPool(BtBankAccVO btBankAcc, Model model) {
        if (StringUtils.isNotEmpty(btBankAcc.getId())) {
            BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
            model.addAttribute("btBankAccPage", vo);
            model.addAttribute("btBankAcc", btBankAcc);
            model.addAttribute("poolSign", AllocationConstant.POOL_SIGN_1);
        }
        return "bankacc/updateBtBankAccRelation";
    }
    /**
     * BT_BANK_ACC详情页
     *
     * @return
     */
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_QUERY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY},
//            logical = Logical.OR)
    @LogMessage(content = "查询BT_BANK_ACC详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{curId}")
    public String view(@PathVariable("id") String id, @PathVariable("curId") String curId, Model model) {
        BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(id);
        model.addAttribute("btBankAccPage", vo);
        BtBankAccCur btBankAccCur = btBankAccCommonService.getBtBankAccCur(id, curId);
        if (!EmptyUtils.isEmpty(btBankAccCur)) {
            BtBankAccVO prentVO = btBankAccCommonService.getBtBankAccVOById(btBankAccCur.getPrentAccId());
            model.addAttribute("prentBtBankAccPage", prentVO);
        }
        return "bankacc/viewBtBankAccRelation";
    }
    /**
     * 增加子账号
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_APPLY},
//            logical = Logical.OR)
    @LogMessage(content = "增加子账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toAdd")
    @ResponseBody
    public AjaxJson toAdd(@RequestParam("ids") String ids, @RequestParam("prentAccId") String prentAccId, @RequestParam("curId") String curId) {
        btBankAccRelationService.toAddRelation(ids, prentAccId, curId);
        return success();
    }
    //@RequiresPermissions(value = {ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_APPLY,
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_APPLY},
//            logical = Logical.OR)
    @LogMessage(content = "删除BT_BANK_ACC父子关系", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{curId}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("curId") String curId) {
        btBankAccRelationService.toDeleteRelation(id, curId);
        return success();
    }
    /**
     * 获取资金池
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY;
        return fcCorpPoolServiceI.getUserCorpPools(this.getCurrentUser(), resource);
    }
    /**
     * 获取资金池
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCRELATION_POOL_QUERY;
        return fcCorpPoolServiceI.getUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
}
