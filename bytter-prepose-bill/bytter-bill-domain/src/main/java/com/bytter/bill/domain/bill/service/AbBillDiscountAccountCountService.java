package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillDiscountAccountCountDao;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountCountVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:贴现到账统计Service
 * @date 2017-7-21 13:50:34
 */
@Service("abBillDiscountAccountCountService")
@Transactional
public class AbBillDiscountAccountCountService extends BaseService<AbBillDiscountAccountCountVO, String> {
    @Autowired
    private AbBillDiscountAccountCountDao abBillDiscountAccountCountDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Override
    public BaseDao<AbBillDiscountAccountCountVO, String> getEntityDao() {
        return abBillDiscountAccountCountDao;
    }
    /**
     * 列表显示贴现申请信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillDiscountAccountCount(DataTablesInput input) {
        //取日期范围
        Map<String, Object> formData = input.getFormdata();
        String statistical = (String) formData.get("eqi_statistical");
        String gbusinessDate = (String) formData.get("ged_businessDate");
        String lbusinessDate = (String) formData.get("led_businessDate");
        List<String> dateList = getDateList(statistical, gbusinessDate, lbusinessDate);
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_DISCOUNTACCOUNTCOUNT_QUERY_CORPOP, input.getCurrentUserId());
        input.getFormdata().put("isDiscountConfirm",abVoucherinfoViceService.isDiscountConfirm());
        List<AbBillDiscountAccountCountVO> listVo = abBillDiscountAccountCountDao.listAbBillDiscountAccountCount(input, corpAuthList);
        List<Map<Object, Object>> list = new ArrayList<>();
        if (listVo != null && !listVo.isEmpty()) {
            Map<Object, Object> map = null;
            BigDecimal realMoneySum = new BigDecimal(Double.toString(0.00));
            int i = 0;
            for (AbBillDiscountAccountCountVO vo : listVo) {
                String recNm = vo.getRecNm();
                String dateString = vo.getBusinessDateFmt();
                BigDecimal realMoney = vo.getRealMoney();
                if (map == null) {
                    map = new HashMap<>();
                    map.put("recNm", recNm);
                } else {
                    if (recNm != null && !recNm.equals(map.get("recNm")) || (map.get("recNm") != null && !map.get("recNm").equals(recNm))) {
                        //map中如果没有对应日期的值,默认0.00
                        for (int j = 0; j < dateList.size(); j++) {
                            if (!map.containsKey(dateList.get(j))) {
                                map.put(dateList.get(j), BigDecimal.valueOf(0.00));
                            }
                        }
                        map.put("realMoneySum", realMoneySum);
                        list.add(map);
                        map = new HashMap<>();
                        realMoneySum = new BigDecimal(Double.toString(0.00));
                        map.put("recNm", recNm);
                    }
                }
                map.put(dateString, realMoney);
                realMoneySum = realMoneySum.add(realMoney);
                if (i == listVo.size() - 1) {
                    map.put("realMoneySum", realMoneySum);
                    list.add(map);
                    for (int j = 0; j < dateList.size(); j++) {
                        if (!map.containsKey(dateList.get(j))) {
                            map.put(dateList.get(j), BigDecimal.valueOf(0.00));
                        }
                    }
                }
                i++;
            }
        }
        DataTablesOutput newput = new DataTablesOutput(0, list, 0, 0);
        return newput;
    }
    /**
     * 查询贴现到账统计页面初始信息
     *
     * @param model
     */
    @Transactional(readOnly = true)
    public void toListInit(Model model,AbBillDiscountAccountCountVO vo) {
        List<String> dateList = getDateList(vo.getStatistical(), vo.getGbusinessDate(), vo.getLbusinessDate());
        model.addAttribute("dateList", dateList);
        model.addAttribute("statistical", vo.getStatistical());
        model.addAttribute("gbusinessDate", vo.getGbusinessDate());
        model.addAttribute("lbusinessDate", vo.getLbusinessDate());
        model.addAttribute("billFlag", vo.getBillFlag());
        model.addAttribute("billType", vo.getBillType());
        model.addAttribute("dimensions", vo.getDimensions()!=null?vo.getDimensions():AccebillConstant.AB_STATISTICAL_DIMENSION_0);
        if(StringUtils.isNotEmpty(vo.getRecNm())){
			try {
				model.addAttribute("recNm", URLDecoder.decode(vo.getRecNm(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
        model.addAttribute("corpId", vo.getCorpId());
        model.addAttribute("isQuery", vo.getIsQuery());
    }
    /**
     * 获取日期范围
     *
     * @param statistical
     * @param gbusinessDate
     * @param lbusinessDate
     */
    public List<String> getDateList(String statistical, String gbusinessDate, String lbusinessDate) {
        //取时间范围
        SimpleDateFormat format;
        if (AccebillConstant.AB_STATISTICAL_TYPE1.equals(statistical)) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("yyyy-MM");
        }
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(gbusinessDate);
            endDate = format.parse(lbusinessDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List dateList = new ArrayList();
        if (startDate.getTime() == endDate.getTime()) {
            dateList.add(format.format(startDate));
        } else {
            dateList.add(format.format(startDate));//把开始时间加入集合
            Calendar cal = Calendar.getInstance();
            //使用给定的 Date 设置此 Calendar 的时间
            cal.setTime(startDate);
            boolean bContinue = true;
            while (bContinue) {
                //根据日历的规则，为给定的日历字段添加或减去指定的时间量
                if (AccebillConstant.AB_STATISTICAL_TYPE1.equals(statistical)) {
                    cal.add(Calendar.DATE, 1);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
                // 测试此日期是否在指定日期之后
                if (endDate.after(cal.getTime())) {
                    dateList.add(format.format(cal.getTime()));
                } else {
                    break;
                }
            }
            dateList.add(format.format(endDate));//把结束时间加入集合
        }
        return dateList;
    }
}
