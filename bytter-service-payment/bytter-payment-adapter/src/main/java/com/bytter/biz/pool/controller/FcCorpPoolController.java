package com.bytter.biz.pool.controller;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.constant.PoolContant;
import com.bytter.biz.payment.api.service.IFexCorpPoolService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.FcCorpPool;
import com.bytter.system.api.vo.FcCorpPoolVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金池单位表
 * @date 2019-04-30 16:44:38
 */
@Controller
@RequestMapping("/pool/fcCorpPool")
public class FcCorpPoolController extends BaseController {
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private INisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
	private IFexCorpPoolService fexCorpPoolService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;
    /**
     * 资金池关系列表 页面跳转
     *
     * @return
     * @throws Exception
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @LogMessage(content = "查询资金池关系列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(FcCorpPool FcCorpPool, Model model) throws Exception {
        return "pool/listFcCorpPool";
    }
    /**
     * 资金池单位表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @GetMapping("/add")
    public String add(FcCorpPool fcCorpPool, Model model) {
        //为1说明是主体单位增加子单位
        if (StringUtils.isNotBlank(fcCorpPool.getPoolSign()) && PoolContant.POOL_SIGN_1.equals(fcCorpPool.getPoolSign())) {
            fcCorpPool = fcCorpPoolService.getFcCorpPool(fcCorpPool.getPoolId());
            model.addAttribute("fcCorpPoolPage", fcCorpPool);
        }else{
            model.addAttribute("fcCorpPoolPage", fcCorpPool);
        }
        return "pool/addFcCorpPool";
    }
    /**
     * 添加资金池单位表
     * @param fcCorpPool
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @LogMessage(content = "添加资金池单位表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcCorpPool fcCorpPool){
        fcCorpPoolService.saveCorpPool(fcCorpPool,this.getCurrentUser());
        return success();
    }
    /**
     * 资金池单位表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @GetMapping("/update")
    public String update(FcCorpPool fcCorpPool, Model model) {
        FcCorpPoolVO fcCorpPoolVO = null;
        if (StringUtils.isNotEmpty(fcCorpPool.getId())) {
            fcCorpPoolVO = fcCorpPoolService.getFcCorpPoolById(fcCorpPool.getId());
            model.addAttribute("fcCorpPoolPage", fcCorpPoolVO);
        }
        return "pool/updateFcCorpPool";
    }
    /**
     * 修改资金池单位表
     * @param fcCorpPool
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @LogMessage(content = "资金池单位表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(FcCorpPool fcCorpPool){
        ICurrentUser currentUser = this.getCurrentUser();
        fcCorpPoolService.modifyCorpPool(fcCorpPool,currentUser);
        //只有主体单位才初始化日结日期 不能放在system里面处理该业务
        if(PoolContant.POOL_SIGN_1.equals(fcCorpPool.getPoolSign())){
            fcDayCloseService.initPoolNetCloseDate(fcCorpPool.getId(), currentUser.getId());
        }
        return  success();
    }
    /**
     * 资金池单位表详情页
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @LogMessage(content = "查询资金池单位表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcCorpPoolVO fcCorpPoolVO = null;
        if (StringUtils.isNotEmpty(id)) {
            fcCorpPoolVO = fcCorpPoolService.getFcCorpPoolById(id);
            model.addAttribute("fcCorpPoolPage", fcCorpPoolVO);
        }
        return "pool/viewFcCorpPool";
    }
    /**
     * 根据单位ID获取单位名称
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "getCorpName")
    @ResponseBody
    public AjaxJson getCorpName(String id) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        CorpDTO corp = corpService.get(id);
        if (StringUtils.isEmpty(corp.getName())) {
            corp.setName(corp.getName());
        }
        j.setObj(corp);
        return j;
    }
    @GetMapping("/tree.json")
    @ResponseBody
    public Object treeJson(String poolCode,String poolName, String status) {
        List<FcCorpPoolVO> list = fcCorpPoolService.getTreeTable(poolCode,poolName,status);
        return new TreeGridResponse<>(list);
    }
    /**
     * 查询主体单位下面是否有子单位
     *
     * @param id 主体单位id
     * @return
     */
    @GetMapping("/judgePool/{id}/{poolSign}")
    @ResponseBody
    public Object judgePool(@PathVariable("id") String id,@PathVariable("poolSign") String poolSign) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            map.put("hasChildPool", false);
            map.put("poolNoUse", false);
            return map;
        }
        if (PoolContant.POOL_SIGN_0.equals(poolSign)) {
            FcCorpPool fcCorpPool = fcCorpPoolService.getFcCorpPool(id);
            if (!EmptyUtils.isEmpty(fcCorpPool) && PoolContant.STATUS_0.equals(fcCorpPool.getStatus())) {
                map.put("poolNoUse", true);
            }
        } else {
            if (!EmptyUtils.isEmptyList(fcCorpPoolService.listByPoolId(id))) {
                map.put("hasChildPool", true);
            }
        }
        //增加控制资金池是否在外汇资金池中维护，如已维护不允许删除
        boolean isUsePool = fexCorpPoolService.isUsePool(id);
        map.put("isUsePool", isUsePool);
        return map;
    }
    /**
     * 校验资金池内部账户余额必须为0，且不能存在未结清的利息
     * 不能存在在途的业务，资金调拨手动调拨、自动调拨未交易成功
     * @param corpId 子单位的单位id
     * @return
     */
    @GetMapping("/vaildAcc/{poolId}/{corpId}")
    @ResponseBody
    public Object vaildAcc(@PathVariable("poolId") String poolId, @PathVariable("corpId") String corpId) {
        boolean vaildAcc = fcCorpPoolService.vaildAcc(corpId,this.getCurrentUser());
        HashMap<String, Object> map = new HashMap<>();
        if(vaildAcc){
            map.put("accBalance", true);
        }else{
            map.put("accBalance", false);
        }
        //资金调拨在途业务
        boolean existFlag = nisAllocationReqService.isExistNotFinishedNisAllocationReq(corpId);
        if (existFlag) {
            map.put("existFlag",true);
        }else{
            map.put("existFlag",false);
        }
        //单位存在（开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回）数据
        boolean useAccFlag = fcCorpPoolService.fcAccUse(poolId, corpId,true);
        if (useAccFlag) {
            map.put("useAccFlag",true);
        }else{
            map.put("useAccFlag",false);
        }
        //根据单位查找账户是否有关联关系
        boolean relevanceFlag = fcCorpPoolService.relevance(poolId,corpId);
        if (relevanceFlag) {
            map.put("relevanceFlag",true);
        }else{
            map.put("relevanceFlag",false);
        }
        return map;
    }
    /**
     * 查询子单位下面是否已开户
     *
     * @param corpId 子单位id
     * @return
     */
    @GetMapping("/childAcc/{poolId}/{corpId}")
    @ResponseBody
    public Object childAcc(@PathVariable("poolId") String poolId,@PathVariable("corpId") String corpId) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(corpId)) {
            map.put("childAccFlag", false);
            return map;
        }
        //单位存在账号
        boolean childAccFlag = fcCorpPoolService.fcAccUse(poolId,corpId,false);
        if (childAccFlag) {
            map.put("childAccFlag",true);
        }else{
            map.put("childAccFlag",false);
        }
        return map;
    }
    /**
     * 删除资金池
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fcCorpPoolService.deleteFcCorpPool(id);
        return success();
    }
}
