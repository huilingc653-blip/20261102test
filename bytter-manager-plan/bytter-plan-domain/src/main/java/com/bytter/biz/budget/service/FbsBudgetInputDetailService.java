package com.bytter.biz.budget.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.dao.FbsBudgetDetailDao;
import com.bytter.biz.budget.entity.FbsBudgetDetail;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetInputDetailVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.payment.api.dto.BtPayTypeDTO;
import com.bytter.biz.payment.api.service.IBtPayTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:59:31
 */
@Service("fbsBudgetInputDetailService")
@Transactional
public class FbsBudgetInputDetailService extends BaseService<FbsBudgetDetail, String> implements IUserExcelVerifyHandler<FbsBudgetInputDetailVO> {
	@Autowired
	private FbsBudgetDetailDao fbsBudgetDetailDao;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@DubboReference(check = false)
	private IDictService dictService;


	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetDetail entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(FbsBudgetDetail entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetDetail entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetDetail entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetDetail, String> getEntityDao() {
		return fbsBudgetDetailDao;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FbsBudgetInputDetailVO vo, Object... obj) {
		List<String> msgList = new ArrayList<>();
		String flag = "";
		String timeType = "";
		Date beginDate = null;
		Date endDate = null;
		String id="";
		Set<String> itemCodes = new HashSet<>();
		if (!EmptyUtils.isEmpty(obj) && !EmptyUtils.isEmpty(obj[1])) {
			Map<String, String[]> params = (Map<String, String[]>) obj[1];
			if(!EmptyUtils.isEmpty(params) && params.size() > 0) {
				flag = params.get("flag")[0];
				timeType = params.get("timeType")[0];
				id = params.get("id")[0];
				beginDate = DateUtils.parseDate(params.get("beginDate")[0]);
				endDate = DateUtils.parseDate(params.get("endDate")[0]);
				if("super".equals(flag)){
					flag = "add";
				}
				FbsBudgetVO fbsBudgetVO = fbsBudgetService.getBudget(id);
				List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetVO.getDtlList();
				for(FbsBudgetDtlAbsVO fbsBudgetDtlAbsVO : dtlList){
					if(fbsBudgetDtlAbsVO.getItemCode().equals(vo.getItemCode())){
						vo.setDetailId(fbsBudgetDtlAbsVO.getId());
					}
					itemCodes.add(fbsBudgetDtlAbsVO.getItemCode());
				}
				vo.setFlag(flag);
				vo.setTimeType(timeType);
				vo.setBudgetId(id);
			}
		}

		if (StringUtils.isBlank(vo.getItemCode())){
			msgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST);
		} else {
			if (!itemCodes.contains(vo.getItemCode())){
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_ITEMCODE_EQ);
			}
		}

		if (EmptyUtils.isEmpty(vo.getBMoney()) && EmptyUtils.isEmpty(vo.getBtempMoney())) {
			msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_MONEY_NULL);
		} else {
			if (BudgetConstant.BUDGET_TARGET_INPUT.equals(flag)) {
				if (!EmptyUtils.isEmpty(vo.getBMoney()) && BigDecimal.ZERO.intValue() == vo.getBMoney().intValue()) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BMONE_EQ_ZERO);
				}
			} else {
				if (!EmptyUtils.isEmpty(vo.getBtempMoney()) && BigDecimal.ZERO.intValue() == vo.getBtempMoney().intValue()) {
					msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_BTEMPMONE_EQ_ZERO);
				}
			}
		}
		if (!EmptyUtils.isBlank(vo.getSettlType())) {
			String settlType = this.dictService.getCodeByName("settlType", vo.getSettlType());
			if (EmptyUtils.isBlank(settlType)) {
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_SETTLTYPE_FORMAT);
			}
		}
		if (EmptyUtils.isEmpty(vo.getVestingDate())) {
			msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_NULL);
		} else {
			if (vo.getVestingDate().getTime() < beginDate.getTime() ||
					vo.getVestingDate().getTime() > endDate.getTime()) {
				msgList.add(BudgetConstant.FBS_BUDGET_DETAIL_MESSAGE_VESTINGDATE_ERROR);
			}
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<FbsBudgetInputDetailVO> list, Object... objects) {
	}
}
