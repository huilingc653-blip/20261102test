package com.bytter.biz.budget.saas.controller.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.biz.vo.FbsMessageVO;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bytter.biz.budget.entity.FbsMessage;
import com.bytter.biz.budget.service.FbsMessageService;
import com.bytter.core.base.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划日志表
 * @date 2017-05-23 15:20:33
 */
@Api(value = "/v1-plan/budget/fbsmessage", tags = "资金计划日志表")
@RestController("v1FbsMessageController")
@RequestMapping("/v1-plan/budget/fbsmessage")
public class FbsMessageController extends BaseController {
    @Autowired
    private FbsMessageService fbsMessageService;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 日志查询
     *
     * @param billId
     * @param busName
     * @return
     */
    @GetMapping("/list/{billId}/{busName}")
    public AjaxJson<List<FbsMessageVO>> list(@PathVariable("billId") String billId, @PathVariable("busName") String busName) {
        List<FbsMessage> fbsMessages = fbsMessageService.listMessage(billId, busName);
        List<FbsMessageVO> listVO = new ArrayList<>();
        Map<String, DictDTO> typeDictDTOMap = dictService.listMap("fbsMessage.messageType");
        fbsMessages.stream().forEach(fbsMessage -> {
            FbsMessageVO fbsMessageVO = new FbsMessageVO();
            BeanUtils.copyProperties(fbsMessage, fbsMessageVO);
            fbsMessageVO.setEnrolDate(fbsMessage.getEnterDate());
            if(fbsMessage.getMessageType()!=null){
                fbsMessageVO.setMessageTypeName(typeDictDTOMap.get(fbsMessage.getMessageType().toString()).getName());
            }
            listVO.add(fbsMessageVO);
        });
        return AjaxJson.success(listVO);
    }
}
