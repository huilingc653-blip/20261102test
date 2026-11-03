package com.bytter.biz.saas.controller.netbank;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisMessageVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 结算日志表
 * @date 2017-05-23 15:20:33
 */
@Api(value = "/v1-payment/netbank/nismessage", tags = "结算日志表")
@RestController("v1NisMessageController")
@RequestMapping("/v1-payment/netbank/nismessage")
public class NisMessageController extends BaseController {

    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 日志查询
     *
     * @param billNo
     * @param busType
     * @return
     */
    @ApiOperation("日志查询")
    @GetMapping("/list/{billNo}/{busType}")
    public AjaxJson<List<NisMessageVO>> list(@PathVariable("billNo") String billNo, @PathVariable("busType") String busType) {
        List<NisMessage> listNisMessage = nisMessageService.listMessage(billNo, busType);
        if (EmptyUtils.isNotEmpty(listNisMessage)) {
            List<NisMessageVO> listNisMessageVo = BeanUtil.copyToList(listNisMessage, NisMessageVO.class);
            Map<String, DictDTO> map = dictService.listMap("nisMessage.messageType");
            if (!map.isEmpty()) {
                listNisMessageVo.forEach(e -> {
                    if (e.getMessageType() != null) {
                        DictDTO dto = map.get(e.getMessageType().toString());
                        if (dto != null) {
                            e.setMessageTypeName(dto.getName());
                        }
                    }
                });
            }
            return AjaxJson.success(listNisMessageVo);
        }
        return AjaxJson.success();
    }
}
