package com.bytter.credential.saas.internation.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.ArrivalLetterService;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.credential.domain.international.vo.v1.ArrivalArrovalInfoPageVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICommonUtilService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.system.velocity.tags.ExcRateTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Api(value = "信用证收款",tags = "信用证收款")
@RestController("v1ArrivalLetterController")
@RequestMapping("/v1-credential/international/credit/arrivalletter")
public class ArrivalLetterController extends BaseController {
	@Autowired 
	private ArrivalLetterService arrivalLetterService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@Autowired
	private ReceiveLetterService receiveLetterService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService excrateAdjustService;
	@DubboReference(check = false)
    private ICommonUtilService commonUtilService;

	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;

	/**
	 * 加载收到信用证的INFO信息
	 * @param id 主键id
	 * @param flag 是否显示关闭按钮标志
	 * @return
	 */
	@ApiOperation(value = "加载收到信用证的INFO信息")
	@LogMessage(content = "查询交单信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewArrovalInfo")
	public AjaxJson<ArrivalArrovalInfoPageVO> viewArrovalInfo(String id,String flag) {
		ArrivalArrovalInfoPageVO pageVO = new ArrivalArrovalInfoPageVO();
		if(EmptyUtils.isNotEmpty(id) && !"null".equals(id)) {
			LcBillArrival arrival = arrivalLetterService.get(id);
			
			LcBillArrivalVO lcBillArrivalVO = new LcBillArrivalVO();
			BeanUtils.copyProperties(arrival, lcBillArrivalVO);
			Map<String,String> mapCur = commonUtilService.mapListCurrency();
			lcBillArrivalVO.setCurName(mapCur.get(lcBillArrivalVO.getCurId())!=null ? (String)mapCur.get(lcBillArrivalVO.getCurId()): "");
			LcEnrol lcEnrol = receiveLetterService.get(arrival.getLcId());
			ExcRateTag excRateTag = new ExcRateTag();
			if(lcBillArrivalVO.getExchange()!= null){
				String excRateTagConvert = excRateTag.getConvert(lcBillArrivalVO.getExchange());
				lcBillArrivalVO.setExchangeShow(excRateTagConvert);
			}
			pageVO.setArrival(lcBillArrivalVO);
			pageVO.setCreditNo(lcEnrol.getCreditNo());
			pageVO.setLcInter(lcEnrol.getLcInter());
			pageVO.setFlag(flag);
			pageVO.setLcEnrol(lcEnrol);
		}
		return success(pageVO);
	}

}
