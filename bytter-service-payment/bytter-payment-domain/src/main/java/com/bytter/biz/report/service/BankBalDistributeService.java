package com.bytter.biz.report.service;
import com.bytter.biz.report.dao.BankBalDistributeDao;
import com.bytter.biz.report.vo.BankBalDistributeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.DictDTO;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.DataZoom;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shenc
 * @Description 银行余额统计报表
 * @date 2018.12.14
 */
@Transactional
@Service("bankBalDistributeService")
public class BankBalDistributeService extends BaseService<BankBalDistributeVo,String> {
    @Autowired
    private BankBalDistributeDao bankBalDistributeDao;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<BankBalDistributeVo, String> getEntityDao() {
        return bankBalDistributeDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        DataTablesOutput datas = bankBalDistributeDao.getDataTablesUnion(input);
        String changeCur=(String)input.getFormdata().get("changeCur");
        String reducedCurId=(String)input.getFormdata().get("reducedCurId");
        String rateType=(String)input.getFormdata().get("rateType");
        Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("rateDate"), "yyyy-MM-dd"));
        //获取汇率
        if (datas != null && datas.getData() != null) {
            List<BankBalDistributeVo> list = datas.getData();
            Map<String, String> corpNameMap = corpService.getNameByIds(list.stream().map(BankBalDistributeVo::getCorpId).collect(Collectors.toList()));
            Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(list.stream().map(BankBalDistributeVo::getCurId).collect(Collectors.toList()));
            Map<String, DictDTO> dictDTOMap = dictService.listMap("btBankAcc.accType");
            HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
            for(BankBalDistributeVo vo:list){
                vo.setCorpName(corpNameMap.get(vo.getCorpId()));
                vo.setCurName(currencyNameMap.get(vo.getCurId()));
                vo.setAccTypeName(dictDTOMap.getOrDefault(vo.getAccType(), DictDTO.EMPTY).getName());
                BigDecimal curExrate = BigDecimal.ONE;
                if(CommonConstant.YES.equals(changeCur)) {
                    if (curExrateMap.containsKey(vo.getCurId())) {
                        curExrate = curExrateMap.get(vo.getCurId());
                    } else {
                        curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
                        curExrateMap.put(vo.getCurId(), curExrate);
                    }
                }
                vo.setCurExrate(curExrate);
            }
        }
        return datas;
    }
    public Object getBankBal(BankBalDistributeVo vo) {
        List<BankBalDistributeVo> bankBal = bankBalDistributeDao.getBankBal(vo);
        Object jsonObject = dealData(bankBal);
        return jsonObject;
    }
    private Object dealData(List<BankBalDistributeVo> bankBal) {
        List<EchartsData> echartsDatas;
        echartsDatas = formatData(bankBal);
        return createBar("银行余额分布","",getData(bankBal),"","",echartsDatas);
    }
    private List<EchartsData> formatData(List<BankBalDistributeVo> bankBal) {
        List<EchartsData> mergeList = new ArrayList<>();
        mergeExceed(bankBal);
        EchartsData data = new EchartsData();
        data.setName("汇总");
        List<Object> list = new ArrayList<Object>();
        for (int i=0;(bankBal.size()>10?i<10:i<bankBal.size());i++) {
            BankBalDistributeVo vo = bankBal.get(i);
            list.add(vo.getBalMoney());
        }
        data.setData(list);
        mergeList.add(data);
        return mergeList;
    }
    /**
     * 处理 bankBal 超过10个后,用"其他"显示数据
     * @param bankBal
     */
    private void mergeExceed(List<BankBalDistributeVo> bankBal) {
        if (bankBal.size()>10){
            BigDecimal otherCount = BigDecimal.ZERO;
            for (int i=9;i<bankBal.size();i++){
                otherCount = otherCount.add(bankBal.get(i).getBalMoney());
            }
            BankBalDistributeVo vo = new BankBalDistributeVo();
            vo.setBankTypeName("其他");
            vo.setBalMoney(otherCount);
            bankBal.add(9,vo);
        }
    }
    public List<Object> getData(List<BankBalDistributeVo> bankBal){
        List<Object> mergeList = new ArrayList<>();
        mergeExceed(bankBal);
        for (int i=0;(bankBal.size()>10?i<10:i<bankBal.size());i++) {
            BankBalDistributeVo vo = bankBal.get(i);
            mergeList.add(vo.getBankTypeName());
        }
        return mergeList;
    }
    public static String createBar(String text,String xAxisName, List<Object> xData,String yAxisName,String yAxisUnit,List<EchartsData> dataList){
        String trigger = "axis";
        String x = "center";
        String y = "top";
        boolean isShow = true;
        String formatter="{value}" + yAxisUnit;
        List<Series> series = new ArrayList<Series>();
        //拿到图例
        Object[] legend = new Object[dataList.size()];
        if(!EmptyUtils.isEmptyList(dataList)){
            for (int i = 0; i < dataList.size(); i++) {
                legend[i] = dataList.get(i).getName();
                Bar temp = new Bar();
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.bar);
                temp.setBarWidth(50);
                temp.setBarMinHeight(10);
                series.add(temp);
            }
            if(dataList.get(0).getData().size()==1){
                y = "bottom";
                trigger = "item";
            }
        }else{
            series = new ArrayList<Series>();
            Bar seriesBean = new Bar();
            seriesBean.setBarWidth(10);
            seriesBean.data(new ArrayList());
            series.add(seriesBean);
        }
        Option option = new Option();
        //设置标题
        Title title=new Title();
        title.setRight("50%");
        title.setText(text);
        option.setTitle(title);
        //设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.valueOf(trigger));
        option.setTooltip(tooltip);
        //设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.show(isShow).feature(Tool.mark, Tool.dataView,new DataZoom().show(false), new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        //设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        xAxis.type(AxisType.category);
        if(EmptyUtils.isEmptyList(xData)){
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(xAxis);
        //设置y轴
        ValueAxis yAxis = new ValueAxis();
        yAxis.type(AxisType.value);
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        yAxis.setAxisLabel(axisLabel);
        yAxis.setName(yAxisName);
        option.yAxis(yAxis);
        //数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
}
