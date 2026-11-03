package com.bytter.increment.saas.controller;

import cn.hutool.json.JSONUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductRedeemService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductRedeemBeanVO;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemLeftMoneyVO;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemPageVO;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemQueryInfoVO;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemSelectVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
@Api(value = "理财相关产品",tags = "理财相关产品")
@RestController("v1ChoProductRedeemController")
@RequestMapping("/v1-increment/chowmatistic/choproductredeem")
public class ChoProductRedeemController extends BaseController {
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @GetMapping("/view/{id}")
    public AjaxJson<ChoProductRedeemPageVO> view(@PathVariable("id") String id){
        ChoProductRedeemPageVO pageVO = new ChoProductRedeemPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRedeemService.getChoProductRedeemVoById(id);
            List<ChoProductRedeemDetailVO> list = choProductRedeemService.getChoProductRedeemDetailVOById(id);
            //设置上次赎回日期和待赎回金额
            for(ChoProductRedeemDetailVO choProductRedeemDetailVO : list){
                List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(choProductRedeemDetailVO.getReqId());
                if (!CollectionUtils.isEmpty(detailList)) {
                    choProductRedeemDetailVO.setLastRedeemDate(detailList.get(detailList.size() - 1).getRedeemDate());
                }
                BigDecimal leftMoney = choProductRedeemService.getLeftMoneyByReqId(choProductRedeemDetailVO.getReqId());
                choProductRedeemDetailVO.setLeftMoney(leftMoney);
            }
            pageVO.setChoProductRedeemPage(choProductReqVo);
            pageVO.setList( list);
            pageVO.setBusType(ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
        }
        return success(pageVO);
    }

    /**
     * <p>
     * （组件） 理财赎回
     * </p>
     *
     * @return selectBtSwiftInfo
     */
    @GetMapping("/select")
    public AjaxJson<ChoProductRedeemSelectVO> select(String corpId,String id) {
        ChoProductRedeemSelectVO selectVO = new ChoProductRedeemSelectVO();
        if(StringUtils.isNotBlank(corpId)){
            CorpDTO corpDTO = iCorpService.get(corpId);
            selectVO.setCorp(corpDTO);
        }
        if(StringUtils.isNotBlank(id)){
        	BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
            if(StringUtils.isNotEmpty(bisAccDtlDTO)){
                // 获取交易日期
                String date = DateUtils.date2Str(bisAccDtlDTO.getTransTime(), DateUtils.date_sdf());
                selectVO.setEndDate(date);
                Date dateStart = DateUtils.addDay(bisAccDtlDTO.getTransTime(), -30);
                selectVO.setStartDate(DateUtils.date2Str(dateStart, DateUtils.date_sdf()));
                // 获取交易金额
                selectVO.setDto(bisAccDtlDTO);
            }
        }
        return success(selectVO);
    }

    @PostMapping("/datatableForSelect")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableForSelect(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductRedeemService.getDataTablesUnionForSelect(input,this.getCurrentUser()));
    }

    @ApiOperation(value = "加载赎回列表数据", notes = "加载赎回列表数据")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choProductRedeemService.getDataTablesUnion(input,this.getCurrentUser()));
    }

    @ApiOperation(value = "保存理财产品赎回", notes = "保存理财产品赎回")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ChoProductRedeemBeanVO choProductRedeemBeanVO){
        ChoProductRedeem entity = new ChoProductRedeem();
        BeanUtils.copyNotNullProperties(choProductRedeemBeanVO,entity);
        if (entity.getRedeemMoney() == null) {
            entity.setRedeemMoney(new BigDecimal(0));
        }
        entity.setCreateBy(this.getCurrentUser().getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        choProductRedeemService.saveChoProductRedeem(entity,choProductRedeemBeanVO,getCurrentUser(),false);
        return success();
    }

    @ApiOperation(value = "批量提交赎回", notes = "批量提交赎回")
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody BisIDsDTO idsDTO){
        String ids = idsDTO.getIds();
        choProductRedeemService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }

    @ApiOperation(value = "获取理财产品赎回编辑数据", notes = "获取理财产品赎回编辑数据")
    @GetMapping("/update")
    public AjaxJson<ChoProductRedeemPageVO> update(String id){
        ChoProductRedeemPageVO pageVO = new ChoProductRedeemPageVO();
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRedeemService.getChoProductRedeemVoById(id);
            pageVO.setChoProductRedeemPage(choProductReqVo);
        }
        return success(pageVO);
    }

    @ApiOperation(value = "修改理财产品赎回", notes = "修改理财产品赎回")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ChoProductRedeemBeanVO choProductRedeemBeanVO){
        ChoProductRedeem t = choProductRedeemService.get(choProductRedeemBeanVO.getId(),true);
        choProductRedeemBeanVO.setUpdateBy(this.getCurrentUser().getLoginName());
        choProductRedeemBeanVO.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(choProductRedeemBeanVO, t,"bankName","bankAcc");
        choProductRedeemService.toModify(t,choProductRedeemBeanVO,getCurrentUser());
        return success();
    }

    @ApiOperation(value = "删除理财产品赎回", notes = "删除理财产品赎回")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        ChoProductRedeem entity = choProductRedeemService.get(id,true);
        this.choProductRedeemService.deleteEntity(entity);
        return success();
    }

    @ApiOperation(value = "加载审核列表数据", notes = "加载审核列表数据")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(this.choProductRedeemService.dataTableApprove(input));
    }

    @ApiOperation(value = "获取待赎回金额和上次赎回日期", notes = "获取待赎回金额和上次赎回日期")
    @GetMapping("/getLeftMoney/{reqiId}")
    @ResponseBody
    public AjaxJson<ChoProductRedeemLeftMoneyVO> getLeftMoney(@PathVariable("reqiId") String reqId){
        ChoProductRedeemLeftMoneyVO vo = new ChoProductRedeemLeftMoneyVO();
        if (StringUtils.isNotEmpty(reqId)) {
            BigDecimal leftMoney = choProductRedeemService.getLeftMoneyByReqId(reqId);
            vo.setLeftMoney(leftMoney);
            BigDecimal leftRedeemPortion = choProductRedeemService.getLeftRedeemPortionByReqId(reqId);
            vo.setLeftRedeemPortion(leftRedeemPortion);
            List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(reqId);
            if (!CollectionUtils.isEmpty(detailList)) {
                vo.setLastRedeemDate(detailList.get(detailList.size() - 1).getRedeemDate());
            }
        }
        return success(vo);
    }

    @ApiOperation(value = "查询可赎回信息", notes = "查询可赎回信息")
    @GetMapping("/queryRedeemInfo/{reqId}")
    @ResponseBody
    public AjaxJson<ChoProductRedeemQueryInfoVO> queryRedeemInfo(@PathVariable("reqId") String reqId){
        ChoProductRedeemQueryInfoVO vo = new ChoProductRedeemQueryInfoVO();
        if (StringUtils.isNotEmpty(reqId)) {
            ChoProductReq choProductReq = choProductReqService.get(reqId);
            List<ChoProductRedeemDetail> detailList = choProductRedeemService.listProductRedeemApproved(reqId);
            BigDecimal maxRedeemMoney = choProductReq.getBuyMoney();
            BigDecimal maxRedeemPortion = choProductReq.getPortion();
            if (!CollectionUtils.isEmpty(detailList)) {
                for (ChoProductRedeemDetail redeem : detailList) {
                    maxRedeemMoney = maxRedeemMoney.subtract(redeem.getRedeemMoney());
                    maxRedeemPortion = maxRedeemPortion.subtract(redeem.getRedeemPortion());
                }
                vo.setLastRedeemDate(detailList.get(detailList.size() - 1).getRedeemDate());
            }
            vo.setMaxRedeemMoney(maxRedeemMoney);
            vo.setMaxRedeemPortion(maxRedeemPortion);
        }
        return success(vo);
    }

    @ApiOperation(value = "更新赎回明细收益", notes = "更新赎回明细收益")
    @PostMapping("/updateRedeemDetail")
    @ResponseBody
    public AjaxJson updateRedeemDetail(String redeemDetailId, BigDecimal incomeMoney, BigDecimal incomeRate){
        choProductRedeemService.updateRedeemDetail(redeemDetailId, incomeMoney, incomeRate);
        return success();
    }
}
