package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.netbank.service.BtAssignPayWayService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.BtAssignPayWayUserVO;
import com.bytter.biz.netbank.vo.BtAssignPayWayVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.vo.BtAssignPayWayAddVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款方式分配表
 * @date 2017-05-17 10:08:00
 */
@Api(value = "/v1-payment/netbank/btassignpayway", tags = "付款方式分配")
@RestController("v1BtAssignPayWayController")
@RequestMapping("/v1-payment/netbank/btassignpayway")
public class BtAssignPayWayController extends BaseController {
    @Autowired
    private BtAssignPayWayService btAssignPayWayService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;

    /**
     * 付款方式分配表 table列表
     *
     * @return
     */
    @ApiOperation("付款方式分配表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BtAssignPayWayVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(btAssignPayWayService.getDataTablesUnion(dataTablesInput.convertAndInitColumns( )));
    }

    /**
     * 用户付款方式分配表 table列表
     *
     * @return
     */
    @ApiOperation("用户付款方式分配表 table列表")
    @PostMapping("/userDatatable")
    public AjaxJson<DataTablesOutput<BtAssignPayWayUserVO>> userDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return AjaxJson.success(btAssignPayWayService.getUserDataTablesUnion(dataTablesInput));
    }

    /**
     * 付款方式分配表新增页面跳转
     *
     * @return
     */
    @ApiOperation("付款方式分配表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<BtAssignPayWayAddVO> add() {
        BtAssignPayWayAddVO vo = BtAssignPayWayAddVO.builder().corps(btAssignPayWayService.getAssignCorps())
                .voucherTypes(fcVoucherTypeService.getFcVoucherTypeForAssign())
                .isEnableUserAssign(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEUSERASSIGN).getValue()).build();
        return AjaxJson.success(vo);
    }


    /**
     * 添加付款方式分配表
     *
     * @param btAssignPayWay
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTASSIGNPAYWAY_APPLY)
    @ApiOperation("添加付款方式分配表")
    @LogMessage(content = "添加付款方式分配表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody BtAssignPayWayVO btAssignPayWay) {
        if (StringUtils.isEmpty(btAssignPayWay.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_CORPID_NULL);
        }
        String[] voucherCodes = null;
        if (StringUtils.isNotBlank(btAssignPayWay.getVoucherCodesStr())) {
            voucherCodes = btAssignPayWay.getVoucherCodesStr().split(CommonConstant.COMMA_SPLIT);
        }
        btAssignPayWayService.assignPayWay(btAssignPayWay, voucherCodes, this.getCurrentUser());
        return success();
    }

    /**
     * 添加用户付款方式分配表
     *
     * @param btAssignPayWayUser
     * @return
     */
    @ApiOperation("添加用户付款方式分配表")
    @LogMessage(content = "添加用户付款方式分配表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveUser")
    public AjaxJson saveUser(@RequestBody BtAssignPayWayUserVO btAssignPayWayUser) {
        if (StringUtils.isEmpty(btAssignPayWayUser.getUserId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_USER_NULL);
        }
        String[] voucherCodes = null;
        if (StringUtils.isNotBlank(btAssignPayWayUser.getVoucherCodesStr())) {
            voucherCodes = btAssignPayWayUser.getVoucherCodesStr().split(CommonConstant.COMMA_SPLIT);
        }
        btAssignPayWayService.assignPayWayUser(btAssignPayWayUser, voucherCodes, this.getCurrentUser());
        return success();
    }

    /**
     * 费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代发工资
     *
     * @param corpId
     * @return
     */
    @ApiOperation("费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代发工资")
    @GetMapping("/getAssignPayWayFilter/{corpId}")
    public AjaxJson<List<BtAssignPayWayVO>> getAssignPayWayFilter(@PathVariable("corpId") String corpId) {
        List<BtAssignPayWayVO> listSpecialVoucherType = new ArrayList<>();
        List<BtAssignPayWayVO> listAll = btAssignPayWayService.getByCorpId(corpId);
        for (BtAssignPayWayVO type : listAll) {
            if (NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())) {
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }

    /**
     * 查询已分配的有效的付款方式
     *
     * @param corpId
     * @return
     */
    @ApiOperation("查询已分配的有效的付款方式")
    @GetMapping("/getAssignPayWay/{corpId}")
    public AjaxJson<List<BtAssignPayWayVO>> getAssignPayWay(@PathVariable("corpId") String corpId) {
        return AjaxJson.success(btAssignPayWayService.getByCorpId(corpId));
    }

    /**
     * 查询已分配的有效的付款方式
     *
     * @param corpId
     * @return
     */
    @ApiOperation("查询已分配的有效的付款方式")
    @GetMapping("/getAssignPayWayAll/{corpId}")
    public AjaxJson<List<BtAssignPayWayVO>> getAssignPayWayAll(@PathVariable("corpId") String corpId) {
        return AjaxJson.success(btAssignPayWayService.getAll(corpId, this.getUserId()));
    }

    /**
     * 查询已分配的有效的付款方式
     *
     * @param userId
     * @return
     */
    @ApiOperation("查询已分配的有效的付款方式")
    @GetMapping("/getAssignPayWayByUserId/{userId}")
    public AjaxJson<List<BtAssignPayWayUserVO>> getAssignPayWayByUserId(@PathVariable("userId") String userId) {
        return AjaxJson.success(btAssignPayWayService.getByUserId(userId));
    }

    /**
     * 查询所有有效供付款方式分配的付款方式
     *
     * @return
     */
    @GetMapping("/json")
    public AjaxJson<List<FcVoucherTypeDTO>> json() {
        return AjaxJson.success(fcVoucherTypeService.getFcVoucherTypeForAssign());
    }

    /**
     * 外汇管理-获取已分配的系统结算方式为34-外付网银的付款方式
     *
     * @param corpId
     * @return
     */
    @GetMapping(params = "/getAssignPayWayForForeign/{corpId}")
    public AjaxJson<List<BtAssignPayWayVO>> getAssignPayWayForForeign(@PathVariable("corpId") String corpId) {
        List<BtAssignPayWayVO> listSpecialVoucherType = new ArrayList<>();
        List<BtAssignPayWayVO> listAll = btAssignPayWayService.getByCorpId(corpId);
        for (BtAssignPayWayVO type : listAll) {
            if (NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())) {
                listSpecialVoucherType.add(type);
            }
        }
        return AjaxJson.success(listSpecialVoucherType);
    }

    /**
     * 获取有效的用户
     *
     * @return
     */
    @PostMapping("/getUserDTO")
    public AjaxJson<List<UserDTO>> getUserDTO() {
        List<UserDTO> userDTOList = userService.listAllUser();
        if (EmptyUtils.isNotEmpty(userDTOList)) {
            for (UserDTO userDTO : userDTOList) {
                String name = userDTO.getLoginName() + "(" + userDTO.getUsername() + ")";
                userDTO.setLoginName(name);
            }
        }
        return AjaxJson.success(userDTOList);
    }
}
