package com.bytter.system.saas.controller.bankacc;

import com.bytter.basic.service.BtCurrencyService;
import com.bytter.basic.vo.BtCurrencyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.dto.v1.BtBankAccRelationAddDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.AllocationConstant;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.dto.v1.RelationSearchDTO;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccCur;
import com.bytter.system.service.BtBankAccCommonService;
import com.bytter.system.service.BtBankAccRelationService;
import com.bytter.system.vo.BtBankAccTreeVO;
import com.bytter.system.vo.TreeVO;
import com.bytter.system.vo.v1.AccRelationTreeVO;
import com.bytter.system.vo.v1.BtBankAccRelationPageVo;
import com.bytter.system.vo.v1.BtBankAccRelationViewPageVo;
import com.bytter.system.vo.v1.BtBankAccTreeListVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC 银行账号父子关系维护
 * @date 2017-06-02 17:50:16
 */
@Api(value = "银行账号父子关系维护", tags = "银行账号父子关系维护")
@RestController("v1BtBankAccRelationController")
@RequestMapping("/v1-system/bankacc/btbankaccrelation")
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


    @ApiOperation("父子关系维护")
    @LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/listBycorpId")
    @ResponseBody
    public AjaxJson<BtBankAccTreeVO> listBycorpId(@RequestBody RelationSearchDTO searchDTO) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        //资金池标识 0-非资金池；1-资金池
        BtBankAccTreeListVO  btBankAccTreeVO = null;
        if(AllocationConstant.POOL_SIGN_1.equals(searchDTO.getPoolSign())) {
//            btBankAccTreeVO = setTreeForPool(searchDTO.getNetId(), searchDTO.getCorpId(), searchDTO.getCurId(), searchDTO.getPoolSign());
        }else{
            btBankAccTreeVO = setTree(searchDTO.getNetId(), searchDTO.getCorpId(), searchDTO.getCurId());
        }
        btBankAccTreeVO.setPoolSign(searchDTO.getPoolSign());
        //资金池标识 0-非资金池；1-资金池
        ajaxJson.setObj(btBankAccTreeVO);
        return ajaxJson;
    }

    /**
     * BT_BANK_ACC table列表
     *
     * @return
     */
    @ApiOperation("增添新子账户时的dataTable")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BtBankAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String poolId = (String) input.getFormdata().get("poolId");
        if(!StringUtils.isBlank(poolId)){
            return success(btBankAccRelationService.getDataTablesUnionForPool(input, this.getCurrentUser()));
        }else {
            return success(btBankAccRelationService.getDataTablesUnion(input, this.getCurrentUser()));
        }
    }
    /**
     * BT_BANK_ACC编辑页面跳转
     *
     * @return
     */
    @ApiOperation("更新前获取账号信息")
    @GetMapping("/getInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BtBankAccRelationPageVo> getInfoBeforeUpdate(BtBankAcc btBankAcc) {
        BtBankAccRelationPageVo pageVo = new BtBankAccRelationPageVo();
        if (StringUtils.isNotEmpty(btBankAcc.getId())) {
            BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
            pageVo.setBtBankAccPage(vo);
            pageVo.setBtBankAcc(btBankAcc);
            pageVo.setPoolSign(AllocationConstant.POOL_SIGN_0);
            return success(pageVo);
        }
        return success();
    }
    /**
     * BT_BANK_ACC编辑页面跳转
     *
     * @return
     */
    @ApiOperation("资金池更新前获取账号信息")
    @GetMapping("/getPoolInfoBeforeUpdate")
    @ResponseBody
    public AjaxJson<BtBankAccRelationPageVo> updateForPool(BtBankAcc btBankAcc) {
        BtBankAccRelationPageVo pageVo = new BtBankAccRelationPageVo();
        if (StringUtils.isNotEmpty(btBankAcc.getId())) {
            BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
            pageVo.setBtBankAccPage(vo);
            pageVo.setBtBankAcc(btBankAcc);
            pageVo.setPoolSign(AllocationConstant.POOL_SIGN_1);
            return success(pageVo);
        }
        return success();
    }
    /**
     * BT_BANK_ACC详情页
     *
     * @return
     */
    @ApiOperation("查询账户详情")
    @LogMessage(content = "查询BT_BANK_ACC详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{curId}")
    public AjaxJson<BtBankAccRelationViewPageVo> view(@PathVariable("id") String id, @PathVariable("curId") String curId) {
        BtBankAccRelationViewPageVo pageVo = new BtBankAccRelationViewPageVo();
        BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(id);
        pageVo.setBtBankAccPage(vo);
        BtBankAccCur btBankAccCur = btBankAccCommonService.getBtBankAccCur(id, curId);
        if (!EmptyUtils.isEmpty(btBankAccCur)) {
            BtBankAccVO prentVO = btBankAccCommonService.getBtBankAccVOById(btBankAccCur.getPrentAccId());
            pageVo.setPrentBtBankAccPage(prentVO);
        }
        return success(pageVo);
    }
    /**
     * 增加子账号
     *
     * @return
     */
    @ApiOperation("增添新子账户")
    @LogMessage(content = "增加子账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/toAdd")
    @ResponseBody
    public AjaxJson toAdd(@RequestBody BtBankAccRelationAddDTO addDTO) {
        btBankAccRelationService.toAddRelation(addDTO.getIds(), addDTO.getPrentAccId(), addDTO.getCurId());
        return success();
    }

    @ApiOperation("删除父子关系")
    @LogMessage(content = "删除BT_BANK_ACC父子关系", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{curId}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("curId") String curId) {
        btBankAccRelationService.toDeleteRelation(id, curId);
        return success();
    }
    /**
     * 组装树结构
     *
     * @param netId
     * @param corpId
     * @param curId
     */
    public BtBankAccTreeListVO setTree(String netId, String corpId, String curId) {
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
        List<AccRelationTreeVO> treeList = null;
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
            
            //转换为组合模式的树形结构
            treeList = this.convertToCombinationTree(corpList, bankAccList, allBankAccList, curId);
            
            stopWatch.stop();
        }
        stopWatch.start("tree order :");
        //逻辑排序，将 treeList 重新排序，解决前端展开错乱的问题
        List<AccRelationTreeVO> newList = new ArrayList<>();
        if(treeList!=null){
            newList.add(treeList.get(0));
            for (AccRelationTreeVO vo : treeList) {
                if (newList.get(0).getParentId() != null && 
                    newList.get(0).getParentId().equals(vo.getParentId()) && !newList.get(0).getId().equals(vo.getId())) {
                    newList.add(vo);
                }
            }
            for(int i =1;i<treeList.size();i++) {
                if (newList.get(0).getParentId() != null && 
                    !newList.get(0).getParentId().equals(treeList.get(i).getParentId())) {
                    for(int j=1;j<newList.size();j++){
                        if(newList.get(j).getId().equals(treeList.get(i).getParentId())){
                            newList.add(j+1,treeList.get(i));
                            break;
                        }
                    }
                }
            }
        }

        BtBankAccTreeListVO btBankAccTreeListVO = new BtBankAccTreeListVO();
        btBankAccTreeListVO.setTreeList(newList);
        btBankAccTreeListVO.setNetId(netId);
        btBankAccTreeListVO.setCorpId(corpId);
        btBankAccTreeListVO.setCurId(curId);
        stopWatch.stop();
        logger.info("setTree total spends:"+stopWatch.prettyPrint());
        return btBankAccTreeListVO;
    }
    
    /**
     * 转换为组合模式的树形结构
     * @param corpList 单位列表
     * @param bankAccList 在线账号列表
     * @param allBankAccList 所有账号列表
     * @param curId 币别 ID
     * @return 组合模式的树形结构
     */
    private List<AccRelationTreeVO> convertToCombinationTree(List<CorpDTO> corpList, 
                                                              List<BtBankAccVO> bankAccList, 
                                                              List<BtBankAccVO> allBankAccList, 
                                                              String curId) {
        List<AccRelationTreeVO> treeList = new ArrayList<>();
        
        //构建单位到账号的映射
        Map<String, List<BtBankAccVO>> cropId2BtBankAccVO = Maps.newHashMap();
        Map<String, List<BtBankAccVO>> prentAccIdCurId2BtBankAccVO = Maps.newHashMap();
        
        //添加币别
        List<BtCurrencyVO> btCurrencys = btCurrencyService.getBtCurrency();
        Map<String, String> btCurrencyMap = btCurrencys.stream().collect(Collectors.toMap(BtCurrencyVO::getId, BtCurrencyVO::getCurName));
        
        //处理在线账号，按单位 ID 分组
        if(EmptyUtils.isNotEmpty(bankAccList)) {
            for (BtBankAccVO bankAccVO : bankAccList) {
                List<BtBankAccVO> bankAccVOListForCorpId = cropId2BtBankAccVO.get(bankAccVO.getCorpId());
                if (bankAccVOListForCorpId == null) {
                    bankAccVOListForCorpId = Lists.newArrayList();
                    cropId2BtBankAccVO.put(bankAccVO.getCorpId(), bankAccVOListForCorpId);
                }
                //设置币别信息
                if (EmptyUtils.isBlank(bankAccVO.getCurId())) {
                    bankAccVO.setCurId(curId);
                }
                bankAccVO.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAccVO.getCurId())) ? "" : (String)btCurrencyMap.get(bankAccVO.getCurId()));
                bankAccVOListForCorpId.add(bankAccVO);
            }
        }
        
        //处理所有账号，按父账号 ID 分组
        if(EmptyUtils.isNotEmpty(allBankAccList)){
            for(BtBankAccVO bankAccVO : allBankAccList){
                String prentAccIdCurId = bankAccVO.getPrentAccId();
                List<BtBankAccVO> bankAccVOListForPrentAccIdCurId = prentAccIdCurId2BtBankAccVO.get(prentAccIdCurId);
                if (bankAccVOListForPrentAccIdCurId == null) {
                    bankAccVOListForPrentAccIdCurId = Lists.newArrayList();
                    prentAccIdCurId2BtBankAccVO.put(prentAccIdCurId, bankAccVOListForPrentAccIdCurId);
                }
                //设置币别信息
                if (EmptyUtils.isBlank(bankAccVO.getCurId())) {
                    bankAccVO.setCurId(curId);
                }
                bankAccVO.setCurName(ObjectUtils.isEmpty(btCurrencyMap.get(bankAccVO.getCurId())) ? "" : (String)btCurrencyMap.get(bankAccVO.getCurId()));
                bankAccVOListForPrentAccIdCurId.add(bankAccVO);
            }
        }
        Integer rid = 0;
        //先构建所有单位节点（不包含账号）
        Map<String, AccRelationTreeVO> corpMap = Maps.newHashMap();
        for (CorpDTO corp : corpList) {
            AccRelationTreeVO corpNode = new AccRelationTreeVO();
            corpNode.setId(corp.getId());
            corpNode.setCode(corp.getCode());
            corpNode.setName(corp.getName());
            corpNode.setCorpId(corp.getId());
            corpNode.setParentId(corp.getParentId());
            corpNode.setDataType("corp");
            corpNode.setCurId(curId);
            corpNode.setRid(generateRid(corp.getId(), rid++));
            corpMap.put(corp.getId(), corpNode);
        }
        
        //构建单位的父子关系
        for (CorpDTO corp : corpList) {
            AccRelationTreeVO corpNode = corpMap.get(corp.getId());
            
            //如果有父单位，将当前单位添加到父单位的 accList 中
            if (!EmptyUtils.isBlank(corp.getParentId()) && corpMap.containsKey(corp.getParentId())) {
                AccRelationTreeVO parentCorpNode = corpMap.get(corp.getParentId());
                List<AccRelationTreeVO> childList = parentCorpNode.getAccList();
                if (childList == null) {
                    childList = new ArrayList<>();
                    parentCorpNode.setAccList(childList);
                }
                childList.add(corpNode);
            } else {
                //没有父单位或父单位不在列表中，作为根节点
                treeList.add(corpNode);
            }
        }
        
        //为每个单位节点添加账号信息
        Map<String, Set<String>> existAccountMap = new HashMap<>();
        for (AccRelationTreeVO corpNode : getAllCorpNodes(treeList)) {
            List<BtBankAccVO> accList = cropId2BtBankAccVO.get(corpNode.getCorpId());
            if (!EmptyUtils.isEmptyList(accList)) {
                List<AccRelationTreeVO> childNodes = new ArrayList<>();
                String corpNodeKey = corpNode.getId() + "-" + corpNode.getCurId();
                for (BtBankAccVO bankAccVO : accList) {
                    String accountKey = corpNode.getId() + "-" +  bankAccVO.getId() + "-" + bankAccVO.getCurId();
                    if(existAccountMap.get(corpNodeKey) == null){
                        existAccountMap.put(corpNodeKey, new HashSet<>());
                    }
                    if(existAccountMap.get(corpNodeKey).contains(accountKey)){
                        continue;
                    }
                    //创建账号节点
                    AccRelationTreeVO accNode = new AccRelationTreeVO();
                    accNode.setId(bankAccVO.getId());
                    accNode.setCode(bankAccVO.getBankAcc());
                    accNode.setName(bankAccVO.getAccName());
                    accNode.setBankAcc(bankAccVO.getBankAcc());
                    accNode.setAccName(bankAccVO.getAccName());
                    accNode.setBankName(bankAccVO.getBankName());
                    accNode.setParentId(corpNode.getId());
                    accNode.setDataType("account");
                    accNode.setCorpId(bankAccVO.getCorpId());
                    accNode.setCurId(bankAccVO.getCurId());
                    accNode.setCurName(bankAccVO.getCurName());
                    accNode.setRid(generateRid(bankAccVO.getId(), rid++));
                    
                    //获取子账号列表
                    List<BtBankAccVO> childList = prentAccIdCurId2BtBankAccVO.get(bankAccVO.getId());
                    if (!EmptyUtils.isEmptyList(childList)) {
                        //转换为 AccRelationTreeVO 列表
                        List<AccRelationTreeVO> subAccountNodes = new ArrayList<>();
                        for (BtBankAccVO childAcc : childList) {
                            //设置币别信息
                            if (EmptyUtils.isBlank(childAcc.getCurId())) {
                                childAcc.setCurId(bankAccVO.getCurId());
                            }
                            if (EmptyUtils.isBlank(childAcc.getCurName())) {
                                childAcc.setCurName(bankAccVO.getCurName());
                            }
                            
                            //创建子账户节点
                            AccRelationTreeVO subAccountNode = new AccRelationTreeVO();
                            subAccountNode.setId(childAcc.getId());
                            subAccountNode.setCode(childAcc.getBankAcc());
                            subAccountNode.setName(childAcc.getAccName());
                            subAccountNode.setBankAcc(childAcc.getBankAcc());
                            subAccountNode.setAccName(childAcc.getAccName());
                            subAccountNode.setParentId(bankAccVO.getId());
                            subAccountNode.setBankName(childAcc.getBankName());
                            subAccountNode.setDataType("subAccount");
                            subAccountNode.setCorpId(childAcc.getCorpId());
                            subAccountNode.setCurId(childAcc.getCurId());
                            subAccountNode.setCurName(childAcc.getCurName());
                            subAccountNode.setRid(generateRid(childAcc.getId(), rid++));
                            
                            subAccountNodes.add(subAccountNode);
                        }
                        accNode.setChildList(subAccountNodes);
                    }
                    existAccountMap.get(corpNodeKey).add(accountKey);
                    childNodes.add(accNode);
                }
                
                //如果已经有子单位（通过上面的父子关系构建），需要将账号和子单位合并
                List<AccRelationTreeVO> existingChildList = corpNode.getAccList();
                if (existingChildList != null && !existingChildList.isEmpty()) {
                    //将账号添加到子单位后面
//                    existingChildList.forEach(child -> {
//                        if(!existAccountMap.get(corpNodeKey).contains(corpNode.getId() + "-" + child.getId() + "-" + child.getCurId())){
//                            childNodes.add(child);
//                        }
//                    });
                    childNodes.addAll(existingChildList);
                }
                
                corpNode.setAccList(childNodes);
            }
        }
        
        return treeList;
    }
    
    /**
     * 生成唯一标识 rid
     * @param id 基础 ID
     * @param seqNum 序列号，如果为 null 则使用 0001
     * @return rid = id + 四位序列号
     */
    private String generateRid(String id, Integer seqNum) {
        if (seqNum == null) {
            seqNum = 1;
        }
        return id +"_"+ String.format("%04d", seqNum);
    }
    
    /**
     * 递归获取所有单位节点
     * @param treeList 树形列表
     * @return 所有单位节点
     */
    private List<AccRelationTreeVO> getAllCorpNodes(List<AccRelationTreeVO> treeList) {
        List<AccRelationTreeVO> allNodes = new ArrayList<>();
        for (AccRelationTreeVO node : treeList) {
            allNodes.add(node);
            if (!EmptyUtils.isEmptyList(node.getAccList())) {
                for (AccRelationTreeVO child : node.getAccList()) {
                    if ("corp".equals(child.getDataType())) {
                        allNodes.add(child);
                        //递归获取子单位的子节点
                        List<AccRelationTreeVO> subChildNodes = getAllCorpNodes(java.util.Arrays.asList(child));
                        allNodes.addAll(subChildNodes);
                    }
                }
            }
        }
        return allNodes;
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
     */
    public BtBankAccTreeVO setTreeForPool(String netId, String corpId, String curId, String poolSign) {
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

}
