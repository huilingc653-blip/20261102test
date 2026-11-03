package com.bytter.increment.saas.controller;

import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqImportVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.v1.ChoProductReqPageVO;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@ApiOperation(value = "理财产品登记", notes = "理财产品登记")
@RestController("v1ChoProductReqController")
@RequestMapping("/v1-increment/chowmatistic/choproductreq")
public class ChoProductReqController extends BaseController {
    @Autowired
    private ChoProductReqService choProductReqService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @ApiOperation(value = "加载列表数据", notes = "加载列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(choProductReqService.getDataTablesUnion(tablesInput,this.getCurrentUser()));
    }

    /**
     * 导入
     *
     * @param list 理财产品登记数据
     * @return 是否成功
     */
    @LogMessage(content = "导入理财产品登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ChoProductReqImportVo> list) {
        return choProductReqService.saveImport(list, this.getCurrentUser());
    }
    /**
     * 下载理财产品登记导入模板
     *
     * @param request  request
     * @param response response
     * @param vo       查询条件
     * @param model    model
     */
    @LogMessage(content = "下载理财产品登记导入模板", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/downloadReqModel")
    public void downloadReqModel(HttpServletRequest request, HttpServletResponse response, ChoProductReqVo vo, Model model) throws Exception {
        choProductReqService.downloadReqModel(request, response, vo, model, getCurrentUser());
    }

    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProductReq choProduct){
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductReqService.saveChoProductReq(choProduct,getCurrentUser());
        return success();
    }
    @ApiOperation("批量提交IDs")
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO idsDTO){
        String ids = idsDTO.getIds();
        choProductReqService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @GetMapping("/update")
    public AjaxJson<ChoProductReqPageVO> update(String id){
        ChoProductReqPageVO pageVO = new ChoProductReqPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(id);
            pageVO.setChoProductReqPage(choProductReqVo);
        }
        return success(pageVO);
    }
    @ApiOperation("修改理财产品登记")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductReq choProductReq){
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        choProductReq.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductReq.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyProperties(choProductReq, t);
        choProductReqService.toModify(t,getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        ChoProductReq entity = choProductReqService.get(id);
        this.choProductReqService.deleteEntity(entity);
        return success();
    }
    @PostMapping("/back/{id}")
    @ResponseBody
    public AjaxJson back(@PathVariable("id") String id){
        this.choProductReqService.back(id);
        return success();
    }
    @GetMapping("/view/{id}")
    public AjaxJson<ChoProductReqPageVO> view(@PathVariable("id") String id){
        ChoProductReqPageVO pageVO = new ChoProductReqPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(id);
            pageVO.setBusType(ChoMessageConstant.BUS_TYPE_PRODUCT_REQ);
            pageVO.setChoProductReqPage(choProductReqVo);
        }
        return success(pageVO);
    }
    /**
     * 获取账号
     * @param corpId
     * @param bankCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId, String bankCode, String curId, String bankType){
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if(!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId) && !StringUtils.isEmpty(bankType)){
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if(!EmptyUtils.isEmptyList(listOnlineBankAcc)){
                listAcc.addAll(listOnlineBankAcc);
            }
            if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        //过滤活期账号
        List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
        if(!EmptyUtils.isEmptyList(listAcc)){
            for(BtBankAccDTO dto: listAcc){
                if(curId.equalsIgnoreCase(dto.getCurId()) && bankType.equalsIgnoreCase(dto.getBankTypeId())
                        && (StringUtils.isBlank(bankCode) || bankCode.equalsIgnoreCase(dto.getBankCode()))){
                    listAccResult.add(dto);
                }
            }
        }
        return success(listAccResult);
    }

    @PostMapping("/listValidSign")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableValidSign(@RequestBody V1DataTablesInput dataTablesInput){
        Date deadlineDate = DateUtils.getDateYMD();
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(choProductReqService.getDataTablesValidSign(tablesInput,deadlineDate,this.getCurrentUser()));
    }


    @PostMapping("/modifyAttachmentId")
    @ResponseBody
    public AjaxJson modifyAttachmentId(@RequestBody ChoProductReq choProductReq){
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        t.setAttachmentId(choProductReq.getAttachmentId());
        choProductReqService.modifyAttachmentId(t,getCurrentUser());
        return success();
    }

    @PostMapping("/filterBySource")
    @ResponseBody
    public AjaxJson<List<CorpDTO>> filterBySource(String corpVOList) {
        return success(choProductReqService.getFilterBySource(corpVOList));
    }

    /**
     * 银行类型维护列表-根据理财产品登记表中银行类型获取
     *
     * @return
     */
    @GetMapping("/bankTypeJson")
    @ResponseBody
    public AjaxJson<List<BankTypeDTO>> getBankTypeJson() {
        return success(choProductReqService.getBankTypeJson());
    }

    /**
     * 购买银行列表-根据理财产品登记表中购买银行获取，可根据银行类型联动查询
     *
     * @return
     */
    @GetMapping("/bankInfoJson")
    @ResponseBody
    public AjaxJson<List<ChoProductReqVo>> getBankInfoJson(String bankTypeIds) {
        return success(choProductReqService.getBankInfoJson(bankTypeIds));
    }
}
