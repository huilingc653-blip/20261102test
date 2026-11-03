package com.bytter.biz.saas.controller.pool;

import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.constant.PoolContant;
import com.bytter.biz.payment.api.service.IFexCorpPoolService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.vo.JudgePoolChildMenuVO;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/// ///import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金池单位表
 * @date 2019-04-30 16:44:38
 */
@Api(value = "/v1-payment/pool/fcCorpPool", tags = "单位资金池维护")
@RestController("v1FcCorpPoolController")
@RequestMapping("/v1-payment/pool/fcCorpPool")
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
     * 资金池单位表新增页面跳转
     *
     * @return
     */
    @ApiOperation(("资金池单位表新增页面跳转"))
    @GetMapping("/add")
    public AjaxJson<FcCorpPool> add(FcCorpPool fcCorpPool) {
        //为1说明是主体单位增加子单位
        if (StringUtils.isNotBlank(fcCorpPool.getPoolSign()) && PoolContant.POOL_SIGN_1.equals(fcCorpPool.getPoolSign())) {
            return AjaxJson.success(fcCorpPoolService.getFcCorpPool(fcCorpPool.getPoolId()));
        } else {
            return AjaxJson.success(fcCorpPool);
        }
    }

    /**
     * 资金池单位表编辑页面跳转
     *
     * @return
     */
    @ApiOperation(("查询资金池单位表详情"))
    @GetMapping("/update")
    public AjaxJson<FcCorpPoolVO> update(@RequestBody FcCorpPool fcCorpPool) {
        return AjaxJson.success(fcCorpPoolService.getFcCorpPoolById(fcCorpPool.getId()));
    }


    @ApiOperation("列表树查询")
    @GetMapping("/tree.json")
    @ResponseBody
    public AjaxJson<TreeGridResponse<FcCorpPoolVO>> treeJson(@RequestParam("poolCode") String poolCode,@RequestParam("poolName")  String poolName,@RequestParam("status")   String status) {
        List<FcCorpPoolVO> list = fcCorpPoolService.getTreeTable(poolCode, poolName, status);
        return AjaxJson.success(new TreeGridResponse<>(list));
    }

    /**
     * 查询主体单位下面是否有子单位
     *
     * @param id 主体单位id
     * @return
     */
    @ApiOperation("查询主体单位下面是否有子单位")
    @GetMapping("/judgePool/{id}/{poolSign}")
    public AjaxJson<JudgePoolChildMenuVO> judgePool(@PathVariable("id") String id, @PathVariable("poolSign") String poolSign) {

        if (StringUtils.isEmpty(id)) {
            return AjaxJson.success(JudgePoolChildMenuVO.builder().hasChildPool(false).poolNoUse(false).build());
        }
        JudgePoolChildMenuVO vo = new JudgePoolChildMenuVO();
        if (PoolContant.POOL_SIGN_0.equals(poolSign)) {
            FcCorpPool fcCorpPool = fcCorpPoolService.getFcCorpPool(id);
            if (!EmptyUtils.isEmpty(fcCorpPool) && PoolContant.STATUS_0.equals(fcCorpPool.getStatus())) {
                vo.setPoolNoUse(true);
            }
        } else {
            if (!EmptyUtils.isEmptyList(fcCorpPoolService.listByPoolId(id))) {
                vo.setHasChildPool(true);
            }
        }
        //增加控制资金池是否在外汇资金池中维护，如已维护不允许删除
        boolean isUsePool = fexCorpPoolService.isUsePool(id);
        vo.setIsUsePool(isUsePool);
        return AjaxJson.success(vo);
    }

    /**
     * 校验资金池内部账户余额必须为0，且不能存在未结清的利息
     * 不能存在在途的业务，资金调拨手动调拨、自动调拨未交易成功
     * @param corpId 子单位的单位id
     * @return
     */
    @ApiOperation("校验资金池内部账户余额必须为0，且不能存在未结清的利息")
    @GetMapping("/vaildAcc/{poolId}/{corpId}")
    @ResponseBody
    public AjaxJson<JudgePoolChildMenuVO> vaildAcc(@PathVariable("poolId") String poolId, @PathVariable("corpId") String corpId) {
        JudgePoolChildMenuVO vo = new JudgePoolChildMenuVO();
        vo.setAccBalance(fcCorpPoolService.vaildAcc(corpId, this.getCurrentUser()));
        //资金调拨在途业务
        vo.setExistFlag(nisAllocationReqService.isExistNotFinishedNisAllocationReq(corpId));
        //单位存在（开户待复核、开户复核打回、改户待复核、改户复核打回、销户待复核、销户复核打回）数据
        vo.setUseAccFlag(fcCorpPoolService.fcAccUse(poolId, corpId, true));
        //根据单位查找账户是否有关联关系
        vo.setRelevanceFlag(fcCorpPoolService.relevance(poolId, corpId));
        return AjaxJson.success(vo);
    }

    /**
     * 查询子单位下面是否已开户
     *
     * @param corpId 子单位id
     * @return
     */
    @ApiOperation("查询子单位下面是否已开户")
    @GetMapping("/childAcc/{poolId}/{corpId}")
    @ResponseBody
    public AjaxJson<JudgePoolChildMenuVO> childAcc(@PathVariable("poolId") String poolId, @PathVariable("corpId") String corpId) {
        if (StringUtils.isEmpty(corpId)) {
            return AjaxJson.success(JudgePoolChildMenuVO.builder().childAccFlag(false).build());
        }
        //单位存在账号
        boolean childAccFlag = fcCorpPoolService.fcAccUse(poolId, corpId, false);
        return AjaxJson.success(JudgePoolChildMenuVO.builder().childAccFlag(childAccFlag).build());
    }

    /**
     * 添加资金池单位表
     * @param fcCorpPool
     * @return
     */
    @ApiOperation("添加资金池单位表")
    @LogMessage(content = "添加资金池单位表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody  FcCorpPool fcCorpPool){
        fcCorpPoolService.saveCorpPool(fcCorpPool,this.getCurrentUser());
        return success();
    }

    /**
     * 修改资金池单位表
     * @param fcCorpPool
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_POOL_FCCORPPOOL_APPLY)
    @ApiOperation("修改资金池单位表")
    @LogMessage(content = "资金池单位表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody FcCorpPool fcCorpPool){
        ICurrentUser currentUser = this.getCurrentUser();
        fcCorpPoolService.modifyCorpPool(fcCorpPool,currentUser);
        //只有主体单位才初始化日结日期 不能放在system里面处理该业务
        if(PoolContant.POOL_SIGN_1.equals(fcCorpPool.getPoolSign())){
            fcDayCloseService.initPoolNetCloseDate(fcCorpPool.getId(), currentUser.getId());
        }
        return  success();
    }

    /**
     * 根据单位ID获取单位名称
     *
     * @param id
     * @return
     */
    @ApiOperation("根据单位ID获取单位名称")
    @GetMapping("getCorpName/{id}")
    @ResponseBody
    public AjaxJson<CorpDTO> getCorpName(@PathVariable("id") String id) {
        CorpDTO corp = corpService.get(id);
        if (StringUtils.isEmpty(corp.getName())) {
            corp.setName(corp.getName());
        }
        return AjaxJson.success(corp);
    }

    /**
     * 删除资金池
     * @param id
     * @return
     */
    @ApiOperation("删除资金池")
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fcCorpPoolService.deleteFcCorpPool(id);
        return success();
    }
}
