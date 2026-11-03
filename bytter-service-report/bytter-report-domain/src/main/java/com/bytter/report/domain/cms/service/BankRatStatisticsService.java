package com.bytter.report.domain.cms.service;

import com.bytter.report.domain.cms.vo.RatBkrationVO;
import com.bytter.report.domain.cms.vo.RatUrationDisBean;
import com.bytter.report.domain.cms.dao.BankRatStatisticsDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.echarts.*;
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

@Transactional
@Service("bankRatStatisticsService")
public class BankRatStatisticsService extends BaseService<RatBkrationVO,String> {

    @Autowired
    private BankRatStatisticsDao bankRatStatisticsDao;
    @DubboReference
    private ICorpService corpService;

    @Override
    public BaseDao<RatBkrationVO, String> getEntityDao() {
        return bankRatStatisticsDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput input,ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_BANKRATSTATISTICS_QUERY, user.getId());
        DataTablesOutput dataTablesOutput = bankRatStatisticsDao.getDataTables(input,corpAuth);
        List<RatUrationDisBean> list = dataTablesOutput.getData();
        if(EmptyUtils.isNotEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            if(null != corpMap){
                for(RatUrationDisBean bean : list){
                    CorpDTO corpDTO = corpMap.get(bean.getCorpId());
                    if(null != corpDTO){
                        bean.setCorpName(corpDTO.getName());
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    public Object getBankBal(RatBkrationVO vo,ICurrentUser user) {//1
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_BANKRATSTATISTICS_QUERY, user.getId());
        List<RatUrationDisBean> ratBankList = bankRatStatisticsDao.getCartogramData(vo,corpAuth);
        Object jsonObject = dealData(ratBankList);
        return jsonObject;
    }

    private Object dealData(List<RatUrationDisBean> ratBankList) {//2
        List<EchartsData> echartsDatas;
        echartsDatas = formatData(ratBankList);
        return createBar(null,"",getData(ratBankList),"","",echartsDatas);
    }

    private List<EchartsData> formatData(List<RatUrationDisBean> ratBankList) {//3
        List<EchartsData> mergeList = new ArrayList<>();
        mergeExceed(ratBankList);

        EchartsData data = new EchartsData();
        EchartsData data1 = new EchartsData();
        List<Object> list = new ArrayList<Object>();
        List<Object> list1 = new ArrayList<Object>();
        for (int i=0;(ratBankList.size()>10?i<10:i<ratBankList.size());i++) {
            RatUrationDisBean vo = ratBankList.get(i);
            list.add(vo.getMoney());
            list1.add(vo.getUseMoney());
        }
      
        data.setName("授信额度");
        data.setData(list);
        data1.setName("占用额度");
        data1.setData(list1);
        mergeList.add(data);
        mergeList.add(data1);
        return mergeList;
    }

    /**
     * 处理 bankBal 超过10个后,用"其他"显示数据
     * @param ratBankList
     */
    private void mergeExceed(List<RatUrationDisBean> ratBankList) {//4
        if (ratBankList.size()>10){
            BigDecimal otherMoneyCount = BigDecimal.ZERO;
            BigDecimal otherUseMoneyCount = BigDecimal.ZERO;
            for (int i=9;i<ratBankList.size();i++){
                otherMoneyCount = otherMoneyCount.add(ratBankList.get(i).getMoney());
                otherUseMoneyCount = otherUseMoneyCount.add(ratBankList.get(i).getUseMoney());
            }
            RatUrationDisBean bean = new RatUrationDisBean();
            bean.setBankType("其他");
            bean.setMoney(otherMoneyCount);
            bean.setUseMoney(otherUseMoneyCount);
            ratBankList.add(9,bean);
        }
    }

    public List<Object> getData(List<RatUrationDisBean> ratBankList){//5
        List<Object> mergeList = new ArrayList<>();
        mergeExceed(ratBankList);

        for (int i=0;(ratBankList.size()>10?i<10:i<ratBankList.size());i++) {
            RatUrationDisBean vo = ratBankList.get(i);
            mergeList.add(vo.getBankTypeName());
        }
        return mergeList;
    }
    
    public static String createBar(String text,String xAxisName, List<Object> xData,String yAxisName,String yAxisUnit,List<EchartsData> dataList){//6
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
//        Toolbox toolbox = new Toolbox();
//        toolbox.show(isShow).feature(Tool.mark, Tool.dataView,new DataZoom().show(false), new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
//        option.setToolbox(toolbox);
        //设置图例
        Legend legendBean = new Legend();
        legendBean.data("授信额度");
        legendBean.data("占用额度");
        option.setLegend(legendBean);
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
