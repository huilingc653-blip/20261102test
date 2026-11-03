package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexBankAccBalReportDao;
import com.bytter.biz.foreign.vo.FexBankAccBalReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外汇存量币种分布统计
 *
 * @author qinl
 * @description 外汇存量币种分布统计service
 * @date 2020.03.19
 */
@Service("fexBankAccBalReportService")
@Transactional
public class FexBankAccBalReportService extends BaseService<FexBankAccBalReportVO, String> {

    @Autowired
    private FexBankAccBalReportDao fexBankAccBalReportDao;
    @Autowired
    private IFcExcrateAdjustService fcExcrateAdjustService;

    @Override
    public BaseDao<FexBankAccBalReportVO, String> getEntityDao() {
        return fexBankAccBalReportDao;
    }

    /**
     * 统计报表入口，条件组装
     *
     * @param input 传参
     * @return 界面显示数据
     */
    public DataTablesOutput<FexBankAccBalReportVO> conditionsEntrance(DataTablesInput input) {
        //性能优化，首次进入报表页面不加载列表
        if (input.getDraw() == 1){
            return new DataTablesOutput<>();
        }
        String date = (String) input.getFormdata().get("date");
        String curId = (String) input.getFormdata().get("curId");
        String currencyType = (String) input.getFormdata().get("currencyType");
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");
        //获取所有余额数据
        List<FexBankAccBalReportVO> list = fexBankAccBalReportDao.selectBalance(date,curId);

        //根据币别分类数据
        Map<String,FexBankAccBalReportVO> map = new HashMap<>();
        for(FexBankAccBalReportVO vo:list){
            if(map.containsKey(vo.getCurId())){
                vo.setBal(map.get(vo.getCurId()).getBal().add(vo.getBal()));
                map.put(vo.getCurId(),vo);
            }else{
                map.put(vo.getCurId(),vo);
            }
        }
        //map转list
        List<FexBankAccBalReportVO> fexBankAccBalReportVOS = convertData(map);

        //计算总余额
        BigDecimal all = new BigDecimal(0);

        //设置折算金额和份额
        for(FexBankAccBalReportVO vo :fexBankAccBalReportVOS){
            //计算规则，先转本位币，再转指定货币
            BigDecimal rate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(),reducedCurId,DateUtils.parse(date, "yyyy-MM-dd"), currencyType);
            BigDecimal multiply;
            if(rate == null){
                multiply = null;
            }else{
                multiply = vo.getBal().multiply(rate);
            }
            vo.setReducedBal(multiply);
            if(multiply!=null){
                all = all.add(vo.getReducedBal());
            }
        }

        if(all.compareTo(BigDecimal.ZERO)!=0){
            for(FexBankAccBalReportVO vo :fexBankAccBalReportVOS){
                if(vo.getReducedBal()!=null){
                    vo.setStockShare(vo.getReducedBal().divide(all,4, RoundingMode.HALF_UP));
                }
            }
        }


        //数据输出
        DataTablesOutput<FexBankAccBalReportVO> output = new DataTablesOutput<>();
        output.setData(fexBankAccBalReportVOS);
        pageFilter(input,output);
        return output;
    }

    /**
     * 分页过滤器
     */
    private <T> void pageFilter(DataTablesInput input, DataTablesOutput<T> output) {
        List<T> list = output.getData();
        if (EmptyUtils.isEmpty(list)) {
            output.setDraw(0);
            output.setRecordsTotal(0);
            output.setRecordsFiltered(0);
            output.setData(new ArrayList<T>());
            return;
        }
        int totalNum = list.size();
        Integer pageNumber = input.getPageNumber();
        int page = (totalNum + 10 - 1) / 10;
        if (pageNumber == page) {
            output.setData(list.subList(input.getStart(), totalNum));
        } else {
            if (input.getLength() > totalNum) {
                output.setData(list.subList(input.getStart(), totalNum));
            } else {
                output.setData(list.subList(input.getStart(), input.getStart() + 10));
            }
        }
        output.setRecordsTotal(list.size());
        output.setRecordsFiltered(list.size());
    }

    /**
     * map转list
     *
     * @param map 待转化的数据
     * @return list
     */
    private <E> List<E> convertData(Map<String, E> map) {
        List<E> calculateList = new ArrayList<>();
        for (String key : map.keySet()) {
            calculateList.add(map.get(key));
        }
        return calculateList;
    }
}
