package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.biz.foreign.service.FexCorpPoolBankaccService;
import com.bytter.biz.foreign.service.FexCorpPoolLimitService;
import com.bytter.biz.foreign.service.FexCorpPoolService;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.biz.foreign.vo.TreeVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 外汇资金池表
 * @date 2020-02-12 14:31:56
 */
@Controller
@RequestMapping("/foreign/fexcorppool")
public class FexCorpPoolController extends BaseController {

    @Autowired
    private FexCorpPoolService fexCorpPoolService;
    @Autowired
    private FexCorpPoolBankaccService fexCorpPoolBankaccService;
    @DubboReference(check = false)
    private IFcCorpPoolService fcCorpPoolService;
    @Autowired
    private FexCorpPoolLimitService fexCorpPoolLimitService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    /**
     * 外汇资金池表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询外汇资金池账号列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexCorpPool";
    }

    @LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listBycorpId")
    public String listBycorpId(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        setTreeForPool(fexCorpPoolBankAccVO.getPoolId(), fexCorpPoolBankAccVO.getCorpId(), fexCorpPoolBankAccVO.getCurId(), model);
        return "foreign/listFexCorpPool";
    }

    /**
     * 组装树结构 资金池
     *
     * @param poolId
     * @param corpId
     * @param model
     */
    public void setTreeForPool(String poolId, String corpId, String curId, Model model) {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        //获取资金池账户维护的所有资金池
        List<TreeVO> treeList = null;
        //根据资金池id,单位id,币别id查询外汇资金池列表数据
        List<FexCorpPoolBankAccVO> fexCorpPoolBankAccVOS = fexCorpPoolService.listAllPool(poolId, corpId, "");

            treeList = this.convertCorpDTO(fexCorpPoolBankAccVOS);
            /**
             * 根据单位得到银行账号
             */
            for (TreeVO treeVO : treeList) {
                //根据资金池主体id查询主体账号信息
                List<FexCorpPoolBankAccVO> accList = fexCorpPoolBankaccService.listBankAccByParentId(treeVO.getId(), curId, ForeignConstant.FEX_PARENT_SIGN_1);
                if (!EmptyUtils.isEmptyList(accList)) {
                    treeVO.setAccList(accList);
                    for (FexCorpPoolBankAccVO vo : accList) {
                        //根据主体账号id查找成员单位账号信息
                        List<FexCorpPoolBankAccVO> childList = fexCorpPoolBankaccService.listBankAccByParentId(vo.getId(), "", ForeignConstant.FEX_PARENT_SIGN_0);
                        List<FexCorpPoolBankAccVO> childListTemp = new ArrayList<>();
                        for (FexCorpPoolBankAccVO bankAcc : childList) {
                            FexCorpPoolBankAccVO parentInfo = fexCorpPoolService.getFexCorpPoolBankAccVOById(vo.getParentId());
                            bankAcc.setPoolId(parentInfo.getPoolId());
                            bankAcc.setPoolName(parentInfo.getPoolName());
                            bankAcc.setPoolNature(parentInfo.getPoolNature());
                            // 有效标志为空默认取主体账号的有效标志
                            if(StringUtils.isEmpty(bankAcc.getValidSign())) {
                                bankAcc.setValidSign(vo.getValidSign());
                            }
                            childListTemp.add(bankAcc);
                        }
                        vo.setChildList(childListTemp);
                    }
                }
            }
        model.addAttribute("treeList", treeList);
        model.addAttribute("poolId", poolId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("curId", curId);
    }

    /**
     * 资金池单位转换
     * @param fexCorpPoolBankAccVOS
     * @return
     */
    private List<TreeVO>  convertCorpDTO(List<FexCorpPoolBankAccVO> fexCorpPoolBankAccVOS){
        List<TreeVO> list = new ArrayList<>();
        for(FexCorpPoolBankAccVO fexCorpPoolBankAccVO:fexCorpPoolBankAccVOS){
            TreeVO tree = new TreeVO();
            tree.setId(fexCorpPoolBankAccVO.getId());
            tree.setCode(fexCorpPoolBankAccVO.getPoolCode());
            tree.setName(fexCorpPoolBankAccVO.getPoolName());
            tree.setPoolSign(fexCorpPoolBankAccVO.getPoolSign());
            tree.setCorpId(fexCorpPoolBankAccVO.getCorpId());
            tree.setCorpName(fexCorpPoolBankAccVO.getCorpName());
            tree.setParentId(fexCorpPoolBankAccVO.getPoolId());
            tree.setEnglishCode(fexCorpPoolBankAccVO.getEnglishCode());
            list.add(tree);
        }
        return list;
    }

    /**
     * 外汇资金池表新增页面跳转
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @GetMapping("/addPool")
    public String add(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        return "foreign/addFexCorpPool";
    }

    /**
     * 保存外汇资金池
     * @param fexCorpPoolBankAccVO
     * @return
     */
    @LogMessage(content = "添加资金池单位表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePool")
    @ResponseBody
    public AjaxJson savePool(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        if(fexCorpPoolBankaccService.dataCheck(fexCorpPoolBankAccVO,false)){
            fexCorpPoolService.saveFexCorpPool(fexCorpPoolBankAccVO,this.getCurrentUser());
        }
        return success();
    }

    /**
     * 外汇资金池表修改页面跳转
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @GetMapping("/updatePool")
    public String updatePool(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        if (StringUtils.isNotEmpty(fexCorpPoolBankAccVO.getId())) {
            FexCorpPoolBankAccVO vo = fexCorpPoolService.getFexCorpPoolBankAccVOById(fexCorpPoolBankAccVO.getId());
            model.addAttribute("fexCorpPoolPage", vo);
        }
        return "foreign/updateFexCorpPool";
    }

    /**
     * 修改资金池单位表
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    @LogMessage(content = "资金池单位表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyPool")
    @ResponseBody
    public AjaxJson modifyPool(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        if(fexCorpPoolBankaccService.dataCheck(fexCorpPoolBankAccVO,false)){
            fexCorpPoolService.modifyFexCorpPool(fexCorpPoolBankAccVO, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 新增主账号页面跳转
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @GetMapping("/addPoolAcc")
    public String addPoolAcc(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        if(StringUtils.isNotEmpty(fexCorpPoolBankAccVO.getId())){
            FexCorpPoolBankAccVO vo = fexCorpPoolService.getFexCorpPoolBankAccVOById(fexCorpPoolBankAccVO.getId());
            model.addAttribute("fexCorpPoolPage", vo);
        }
        return "foreign/addFexCorpPoolBankacc";
    }

    /**
     * 保存主账号
     * @param fexCorpPoolBankAccVO
     * @return
     */
    @LogMessage(content = "添加主账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePoolAcc")
    @ResponseBody
    public AjaxJson savePoolAcc(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        if(fexCorpPoolBankaccService.dataCheck(fexCorpPoolBankAccVO,true)){
            fexCorpPoolService.saveFexCorpPoolBankAcc(fexCorpPoolBankAccVO, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 修改主账号页面跳转
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @GetMapping("/updatePoolAcc")
    public String updatePoolAcc(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        if (StringUtils.isNotEmpty(fexCorpPoolBankAccVO.getId())) {
            FexCorpPoolBankAccVO vo = fexCorpPoolBankaccService.getFexCorpPoolBankAccVOById(fexCorpPoolBankAccVO.getId());
            model.addAttribute("fexCorpPoolBankAccPage", vo);
        }
        return "foreign/updateFexCorpPoolBankacc";
    }

    /**
     * 修改主账号
     * @param fexCorpPoolBankAccVO
     * @return
     */
    @LogMessage(content = "修改主账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modifyPoolAcc")
    @ResponseBody
    public AjaxJson modifyPoolAcc(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        if(fexCorpPoolBankaccService.dataCheck(fexCorpPoolBankAccVO,true)){
            fexCorpPoolService.modifyFexCorpPoolBankAcc(fexCorpPoolBankAccVO, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 外汇资金池表详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金池单位表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPool/{id}")
    public String viewPool(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexCorpPoolBankAccVO vo = fexCorpPoolService.getFexCorpPoolBankAccVOById(id);
            model.addAttribute("fexCorpPoolPage", vo);
        }
        return "foreign/viewFexCorpPool";
    }

    /**
     * 外汇资金池主账号详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金池单位表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPoolAcc/{id}")
    public String viewPoolAcc(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexCorpPoolBankAccVO vo = fexCorpPoolBankaccService.getFexCorpPoolBankAccVOById(id);
            model.addAttribute("fexCorpPoolBankAccPage", vo);
        }
        return "foreign/viewFexCorpPoolBankacc";
    }

    /**
     * 外汇资金池成员账号详情页
     *
     * @return
     */
    @LogMessage(content = "查询资金池单位表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPoolChildAcc/{id}/{childId}")
    public String viewPoolChildAcc(@PathVariable("id") String id, @PathVariable("childId") String childId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexCorpPoolBankAccVO vo = fexCorpPoolBankaccService.getFexCorpPoolBankAccVOById(id);
            model.addAttribute("fexCorpPoolPage", vo);
        }
        if (StringUtils.isNotEmpty(childId)) {
            FexCorpPoolBankAccVO childAcc = fexCorpPoolBankaccService.getFexCorpPoolBankAccVOById(childId);
            model.addAttribute("fexCorpPoolBankAccPage", childAcc);
        }
        return "foreign/viewFexCorpPoolBankaccChild";
    }


    /**
     * 新增主账号页面跳转
     * @param fexCorpPoolBankAccVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @GetMapping("/addPoolAccChild")
    public String addPoolChildAcc(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        if(StringUtils.isNotEmpty(fexCorpPoolBankAccVO.getId())){
            FexCorpPoolBankAccVO vo = fexCorpPoolBankaccService.getFexCorpPoolBankAccVOById(fexCorpPoolBankAccVO.getId());
            model.addAttribute("fexCorpPoolPage", vo);
        }
        return "foreign/addFexCorpPoolBankaccChild";
    }

    /**
     * 保存成员账号
     * @param fexCorpPoolBankAccVO
     * @return
     */
    @LogMessage(content = "添加成员账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/savePoolAccChild")
    @ResponseBody
    public AjaxJson savePoolAccChild(FexCorpPoolBankAccVO fexCorpPoolBankAccVO) {
        if(fexCorpPoolBankaccService.dataCheck(fexCorpPoolBankAccVO,false)){
            fexCorpPoolService.saveFexCorpPoolChild(fexCorpPoolBankAccVO, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 删除外汇资金池信息
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_APPLY)
    @LogMessage(content = "删除外汇资金池信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{poolSign}/{parentSign}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("poolSign") String poolSign, @PathVariable("parentSign") String parentSign) {
        //判断是否是资金池,如果是判断下面是否有主账号
        if(EmptyUtils.isNotEmpty(poolSign) && ForeignConstant.FEX_POOL_SIGN_1.equals(poolSign)){
            List<FexCorpPoolBankAccVO> accVOS = fexCorpPoolBankaccService.listBankAccByParentId(id, "", poolSign);
            if(!EmptyUtils.isEmptyList(accVOS)){
                throw new ParameterException(NetbankMessageEnum.FOREIGN_FEXCORPPOOL_MESSAGE_BANKACC_NOTNULL);
            }
        }
        //判断是否是主账号,如果是判断下面是否有成员账号
        if(EmptyUtils.isNotEmpty(parentSign) && ForeignConstant.FEX_PARENT_SIGN_1.equals(parentSign)){
            //主账号查成员账号标识为0
            List<FexCorpPoolBankAccVO> accVOS = fexCorpPoolBankaccService.listBankAccByParentId(id, "", "");
            if(!EmptyUtils.isEmptyList(accVOS)){
                throw new ParameterException(NetbankMessageEnum.FOREIGN_FEXCORPPOOL_MESSAGE_CHILDACC_NOTNULL);
            }
        }
        if(EmptyUtils.isNotEmpty(poolSign) && ForeignConstant.FEX_POOL_SIGN_1.equals(poolSign)){
            fexCorpPoolService.delete(id);
        }else{
            List<FexCorpPoolLimit> fexCorpPoolLimits = fexCorpPoolLimitService.listFexCorpPoolLimitByrelatedId(id);
            for(FexCorpPoolLimit limit : fexCorpPoolLimits){
                fexCorpPoolLimitService.delete(limit.getId());
            }
            fexCorpPoolBankaccService.delete(id);

        }
        return success();
    }

    /**
     * 根据外汇资金池主体id查找账号信息
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/bankAccInfo")
    @ResponseBody
    public List<FexCorpPoolBankAccVO> getBankAccInfo(@RequestParam String id, Model model) {
        return fexCorpPoolBankaccService.listBankAccByParentId(id, null, ForeignConstant.FEX_PARENT_SIGN_1);
    }


    /**
     * 获取资金池
     *
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }

    /**
     * 获取资金池
     *
     * @return
     */
    @GetMapping("/getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolsRest() {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
    }

    /**
     * 获取资金池下面单位
     *
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }

    /**
     * 通过REST路径获取资金池下属单位。
     *
     * @param poolId 资金池ID
     * @return 当前用户有权限查看的资金池下属单位
     */
    @GetMapping("/getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChildsRest(String poolId) {
        return getUserCorpPoolChilds(poolId);
    }

    /**
     * 获取有效外汇资金池
     *
     * @return
     */
    @GetMapping("/getFexCorpPools")
    @ResponseBody
    public List<FexCorpPoolBankAccVO> getPoolAcc() {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fexCorpPoolService.getFexCorpPools(this.getCurrentUser(), resource);
    }

    /**
     * 获取有效外汇资金池(去重)
     *
     * @return
     */
    @GetMapping("/getFexCorpPoolsUniq")
    @ResponseBody
    public List<FexCorpPoolBankAccVO> getPoolAccUniq() {
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        return fexCorpPoolService.getPoolAccUniq(this.getCurrentUser(), resource);
    }

    /**
     * 获取资金池下面主账号
     *
     * @param poolId 外汇资金池id
     * @return
     */
    @GetMapping("/getFexCorpPoolChilds")
    @ResponseBody
    public List<FexCorpPoolBankAccVO> getFexCorpPoolChilds(String poolId) {
        return fexCorpPoolBankaccService.listBankAccByParentId(poolId,null,ForeignConstant.FEX_PARENT_SIGN_1);
    }

    /**
     * 根据资金池id和币别id获取主账号
     *
     * @param poolId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String poolId) {
        List<BtBankAccDTO> listAcc= fexCorpPoolBankaccService.getBankAcc(poolId);
        return listAcc;
    }

    /** 根据资金池id查找帐号列表
     * @param poolId 资金池id
     * @return
     */
    @PostMapping("/getListBy")
    @ResponseBody
    public List getListBy(String poolId) {
        if (StringUtils.isBlank(poolId)) {
            return new ArrayList<>();
        }
        String resource = ResourceConstant.FOREIGN_FOREIGN_FEXCORPPOOL_QUERY;
        List<FcCorpPoolDTO> userCorpPools = fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
        List<String> corpIds = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(userCorpPools)) {
            for (FcCorpPoolDTO dto : userCorpPools) {
                if (StringUtils.isNotBlank(dto.getCorpId())) {
                    corpIds.add(dto.getCorpId());
                }
            }
        }
        if (EmptyUtils.isEmptyList(corpIds)) {
            return new ArrayList<>();
        }
        List<BtBankAccDTO> bankAccList = btBankAccService.getListByCorpIds(corpIds);
        List<String> ids = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(bankAccList)) {
            for (BtBankAccDTO accDTO : bankAccList) {
                if (StringUtils.isNotBlank(accDTO.getId())) {
                    ids.add(accDTO.getId());
                }
            }
        }
        if (EmptyUtils.isEmptyList(ids)) {
            return new ArrayList<>();
        }
        return btBankAccService.getBankAccByIdList(ids);
    }
}
