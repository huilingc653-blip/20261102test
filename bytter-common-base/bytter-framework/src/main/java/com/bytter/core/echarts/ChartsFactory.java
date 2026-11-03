package com.bytter.core.echarts;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.echarts.data.MapData;
import com.bytter.core.echarts.series.LineArea;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.github.abel533.echarts.*;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.DataView;
import com.github.abel533.echarts.feature.DataZoom;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.Mark;
import com.github.abel533.echarts.series.*;
import com.github.abel533.echarts.series.gauge.Detail;
import java.util.Map;
import java.util.*;
/**
 * 创建图形工厂 Created by zhouj on 2017/8/25.
 */
public class ChartsFactory {
    /**
     * 创建仪表盘
     * 
     * @param text
     *            仪表盘文本名称
     * @param min
     *            仪盘最小值
     * @param max
     *            仪盘最大值
     * @param dataList
     *            仪表盘的值
     */
    public static String createGauge(String text, Integer min, Integer max, List<EchartsData> dataList) {
        Option option = new Option();
        // 设置标题
        Title title = new Title().x(X.center);
        title.setText(text);
        option.setTitle(title);
        // 设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("{a} <br/>{b} : {c}%");
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        option.setToolbox(toolbox);
        // 设值
        List<Series> series = new ArrayList<Series>();
        for (EchartsData echartsData : dataList) {
            Gauge gaugeSeries = new Gauge();
            gaugeSeries.setName("");
            gaugeSeries.setType(SeriesType.gauge);
            gaugeSeries.min(min).max(max);
            gaugeSeries.axisLabel().position(3).rotate(2).color("#333");
            Detail detail = new Detail();
            detail.setFormatter("{value}%");
            gaugeSeries.detail(detail);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", echartsData.getData().toArray(new Object[echartsData.getData().size()]));
            map.put("name", echartsData.getName());
            gaugeSeries.data(map);
            series.add(gaugeSeries);
        }
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建柱状图/柱状堆积
     * 
     * @param text
     *            标题
     * @param xAxisName
     *            x轴名称
     * @param yAxisName
     *            y轴名称
     * @param yAxisUnit
     *            单位 如"%"、"℃"之类
     * @param xData
     *            x轴值
     * @param dataList
     *            数据
     */
    public static String createBar(String text, String xAxisName, List<Object> xData, String yAxisName,
            String yAxisUnit, List<EchartsData> dataList) {
        String trigger = "axis";
        String x = "center";
        String y = "top";
        boolean isShow = true;
        String formatter = "{value}" + yAxisUnit;
        List<Series> series = new ArrayList<Series>();
        // 拿到图例
        Object legend[] = new Object[dataList.size()];
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legend[i] = dataList.get(i).getName();
                Bar temp = new Bar();
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.bar);
                series.add(temp);
            }
            if (dataList.get(0).getData().size() == 1) {
                y = "bottom";
                trigger = "item";
            }
        } else {
            series = new ArrayList<Series>();
            Bar seriesBean = new Bar();
            seriesBean.data(new ArrayList());
            series.add(seriesBean);
        }
        Option option = new Option();
        // 设置标题
        Title title = new Title();
        title.setText(text);
        option.setTitle(title);
        // 设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.valueOf(trigger));
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.show(isShow).feature(Tool.mark, Tool.dataView, new DataZoom().show(false),
                new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend(legend).x(X.valueOf(x)).y(Y.valueOf(y));
        option.setLegend(legendBean);
        // 设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        xAxis.type(AxisType.category);
        if (EmptyUtils.isEmptyList(xData)) {
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(xAxis);
        // 设置y轴
        ValueAxis yAxis = new ValueAxis();
        yAxis.type(AxisType.value);
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        yAxis.setAxisLabel(axisLabel);
        yAxis.setName(yAxisName);
        option.yAxis(yAxis);
        // 数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 柱状和折线混搭图
     * 
     * @param text
     *            标题
     * @param xAxisName
     *            x轴名称
     * @param y1AxisName
     *            y1轴名称
     * @param y2AxisName
     *            y2轴名称
     * @param xData
     *            x轴值
     * @param barList
     *            柱状图数据
     * @param lineList
     *            折线图数据 柱状图 设置name和data两个属性，支持堆积，若堆积设置stack 折线图除设置name和data外，还需设置type为line
     * @param y1Unit
     *            单位 如"%"、"℃"之类
     * @param y2Unit
     *            单位 如"%"、"℃"之类
     */
    public static String createMashupOne(String text, String xAxisName, List<Object> xData, String y1AxisName,
            String y1Unit, String y2AxisName, String y2Unit, List<EchartsData> barList, List<EchartsData> lineList) {
        List<Series> series = new ArrayList<Series>();
        if (!EmptyUtils.isEmptyList(barList)) {
            for (EchartsData barDataBean : barList) {
                Bar barSeries = new Bar();
                barSeries.setData(barDataBean.getData());
                barSeries.setName(barDataBean.getName());
                barSeries.setStack(barDataBean.getStack());
                barSeries.setType(SeriesType.bar);
                series.add(barSeries);
            }
        }
        if (!EmptyUtils.isEmptyList(lineList)) {
            for (EchartsData lineDataBean : lineList) {
                Line barSeries = new Line();
                barSeries.setData(lineDataBean.getData());
                barSeries.setName(lineDataBean.getName());
                barSeries.setType(SeriesType.line);
                barSeries.setyAxisIndex(1);
                series.add(barSeries);
            }
        }
        Option option = new Option();
        String y1Formatter = "{value}" + y1Unit;
        String y2Formatter = "{value}" + y2Unit;
        // 拿到图例
        Object legend[] = new Object[series.size()];
        if (!EmptyUtils.isEmptyList(series)) {
            for (int i = 0; i < series.size(); i++) {
                legend[i] = series.get(i).getName();
            }
        } else {
            series = new ArrayList<Series>();
            Bar seriesBean = new Bar();
            seriesBean.setData(new ArrayList());
            series.add(seriesBean);
        }
        // 设置标题
        Title title = new Title();
        title.setText(text);
        option.setTitle(title);
        // 设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setTrigger(Trigger.axis);
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.mark, Tool.dataView, new DataZoom().show(false),
                new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend(legend).x(X.center).y(Y.top);
        option.setLegend(legendBean);
        // 设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.name(xAxisName).type(AxisType.category);
        if (xData == null) {
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(xAxis);
        // 设置y轴
        ValueAxis y1 = new ValueAxis();
        y1.setName(y1AxisName);
        AxisLabel y1AxisLabel = new AxisLabel().formatter(y1Formatter);
        y1.setAxisLabel(y1AxisLabel);
        ValueAxis y2 = new ValueAxis();
        y2.setName(y2AxisName);
        AxisLabel y2AxisLabel = new AxisLabel().formatter(y2Formatter);
        y2.setAxisLabel(y2AxisLabel);
        option.yAxis(y1, y2);
        // 数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建折线图
     * 
     * @param text
     *            标题
     * @param xAxisName
     *            x轴名称
     * @param yAxisName
     *            y轴名称
     * @param xData
     *            x轴值
     * @param dataList
     *            值
     * @param unit
     *            单位 如"%"、"℃"之类
     */
    public static String createLine(String text, String xAxisName, List<Object> xData, String yAxisName, String unit,
            List<EchartsData> dataList) {
        List<Series> series = new ArrayList<Series>();
        // 拿到图例
        String legend[] = new String[dataList.size()];
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legend[i] = dataList.get(i).getName();
                Line temp = new Line();
                temp.setName(dataList.get(i).getName());
                temp.setData(dataList.get(i).getData());
                temp.setType(SeriesType.line);
                series.add(temp);
            }
        } else {
            series = new ArrayList<Series>();
            Line seriesBean = new Line();
            seriesBean.setData(new ArrayList());
            series.add(seriesBean);
        }
        Option option = new Option();
        // 设置标题
        Title title = new Title();
        title.setText(text);
        option.setTitle(title);
        // 设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.trigger(Trigger.axis);
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.mark, Tool.dataView, new DataZoom().show(false),
                new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend(legend).x(X.center).y(Y.top);
        option.setLegend(legendBean);
        // 设置x轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setName(xAxisName);
        if (xData == null) {
            xData = new ArrayList<Object>();
        }
        xAxis.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(xAxis);
        // 设置y轴
        ValueAxis yAxis = new ValueAxis();
        yAxis.setName(yAxisName);
        String formatter = "{value} " + unit;
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        yAxis.setAxisLabel(axisLabel);
        option.yAxis(yAxis);
        // 数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建饼图
     * 
     * @param text
     *            标题
     * @param x
     *            图例水平安放位置，默认为全图居中，可选为：'center' | 'left' | 'right'
     * @param y
     *            图例垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center'
     * @param radius
     *            半径 格式50%
     * @param dataList
     *            饼图数据
     */
    public static String createPie(String text, String x, String y, String radius, List<EchartsData> dataList) {
        List<Object> radiusArray = new ArrayList<Object>();
        radiusArray.add("0%");
        radiusArray.add(radius);
        return createPieAndRingJson(text, x, y, radiusArray, dataList);
    }
    /**
     * 创建环形图
     * 
     * @param text
     *            标题
     * @param x
     *            图例水平安放位置，默认为全图居中，可选为：'center' | 'left' | 'right'
     * @param y
     *            图例垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center'
     * @param innerRadius
     *            内半径，格式20%
     * @param outerRadius
     *            外半径，格式30%
     * @param dataList
     *            环形图数据
     */
    public static String createRing(String text, String x, String y, String innerRadius, String outerRadius,
            List<EchartsData> dataList) {
        List<Object> radiusArray = new ArrayList<Object>();
        radiusArray.add(innerRadius);
        radiusArray.add(outerRadius);
        return createPieAndRingJson(text, x, y, radiusArray, dataList);
    }
    /**
     * 饼图和环形图生成json
     * 
     * @param text
     *            标题
     * @param x
     *            图例水平安放位置，默认为全图居中，可选为：'center' | 'left' | 'right'
     * @param y
     *            图例垂直安放位置，默认为全图顶端，可选为：'top' | 'bottom' | 'center'
     * @param radiusArray
     *            []半径数组，[内半径，外半径] 格式{"50%", "70%"}
     */
    private static String createPieAndRingJson(String text, String x, String y, List<Object> radiusArray,
            List<EchartsData> dataList) {
        Option option = new Option();
        // 设置标题
        Title title = new Title();
        title.setText(text);
        option.setTitle(title);
        // 设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("{a} <br/>{b} : {c} ({d}%)");
        tooltip.setTrigger(Trigger.item);
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(new Mark().show(false), Tool.dataView, new DataZoom().show(false),
                new MagicType(Magic.pie, Magic.funnel).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend().x(X.valueOf(x)).y(Y.valueOf(y));
        Object[] legendData = new Object[dataList.size()];
        List<Object> series = new ArrayList<Object>();
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", dataList.get(i).getName());
                map.put("value", dataList.get(i).getData().toArray(new Object[dataList.get(i).getData().size()]));
                series.add(map);
            }
        }
        legendBean.data(legendData);
        option.setLegend(legendBean);
        Pie ringSeries = new Pie();
        ringSeries.name("").type(SeriesType.pie).radius(radiusArray.toArray(new Object[radiusArray.size()]));
        ringSeries.data(series.toArray(new Object[series.size()]));
        option.series(ringSeries);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 标准面积图/堆积面积图 属于折线图一种
     * 
     * @param text
     *            标题
     * @param xAxisName
     *            x轴名称
     * @param yAxisName
     *            y轴名称
     * @param xData
     *            x轴值
     * @param unit
     *            单位 如"%"、"℃"之类
     * @param dataList
     *            数据
     */
    public static String createAreaStack(String text, String xAxisName, List<Object> xData, String yAxisName,
            String unit, List<EchartsData> dataList) {
        Option option = new Option();
        List<Series> series = new ArrayList<Series>();
        // 设置标题
        option.title(text).tooltip().trigger(Trigger.axis);
        // 设置图例
        Legend legendBean = new Legend();
        Object[] legendData = new Object[dataList.size()];
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
                LineArea temp = new LineArea();
                temp.setData(dataList.get(i).getData());
                temp.setName(dataList.get(i).getName());
                temp.setStack(dataList.get(i).getStack());
                temp.smooth(true).type(SeriesType.line);
                temp.areaStyle().normal();
                series.add(temp);
            }
        } else {
            Line seriesBean = new Line();
            seriesBean.setData(new ArrayList());
            series.add(seriesBean);
        }
        legendBean.data(legendData);
        option.setLegend(legendBean);
        // 设置x轴
        CategoryAxis x1 = new CategoryAxis();
        x1.setName(xAxisName);
        x1.boundaryGap(false);
        if (xData == null) {
            xData = new ArrayList<Object>();
        }
        x1.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(x1);
        // 设置y轴
        ValueAxis y1 = new ValueAxis();
        y1.setName(yAxisName);
        String formatter = "{value} " + unit;
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        y1.setAxisLabel(axisLabel);
        option.yAxis(y1);
        // 设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.feature(new Mark().show(false), new DataView().show(true), new DataZoom().show(false),
                new MagicType(Magic.line, Magic.bar, Magic.stack, Magic.tiled).show(true), Tool.restore,
                Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 温度计式图表 柱状图
     * 
     * @param text
     *            标题
     * @param xAxisName
     *            x轴名称
     * @param yAxisName
     *            y轴名称
     * @param xData
     *            x轴值
     * @param unit
     *            单位 如"%"、"℃"之类
     * @param dataList
     *            数据
     */
    public static String createTermometer(String text, String xAxisName, List<Object> xData, String yAxisName,
            String unit, List<EchartsData> dataList) {
        Option option = new Option();
        // 设置标题
        option.title(text).tooltip().trigger(Trigger.axis);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.mark, Tool.dataView, new DataZoom().show(false), new MagicType().show(false), Tool.restore,
                Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend();
        Object[] legendData = new Object[dataList.size()];
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
            }
        }
        legendBean.setSelectedMode(true);
        legendBean.data(legendData);
        option.setLegend(legendBean);
        // 设置x轴
        CategoryAxis x1 = new CategoryAxis().show(true);
        x1.setName(xAxisName);
        if (xData == null) {
            xData = new ArrayList<Object>();
        }
        x1.data(xData.toArray(new Object[xData.size()]));
        option.xAxis(x1);
        // 设置y轴
        ValueAxis y1 = new ValueAxis();
        y1.setName(yAxisName);
        String formatter = "{value} " + unit;
        AxisLabel axisLabel = new AxisLabel().formatter(formatter);
        y1.setAxisLabel(axisLabel);
        option.yAxis(y1);
        List<Series> series = new ArrayList<Series>();
        if (!EmptyUtils.isEmptyList(dataList)) {
            EchartsData thermometer = dataList.get(0);
            Bar temp = new Bar();
            temp.setName(thermometer.getName());
            temp.setData(thermometer.getData());
            temp.setStack(thermometer.getStack());
            temp.itemStyle().normal().label().position(Position.inside).show(true).color("#fff").textStyle()
                    .color("tomato");
            temp.setType(SeriesType.bar);
            series.add(temp);
            EchartsData thermometerBean = dataList.get(1);
            Bar tempBean = new Bar();
            tempBean.setName(thermometerBean.getName());
            tempBean.setData(thermometerBean.getData());
            tempBean.setStack(thermometer.getStack());
            tempBean.setType(SeriesType.bar);
            tempBean.itemStyle().normal().label().position(Position.top).show(true).color("#fff").textStyle()
                    .color("tomato");
            series.add(tempBean);
        }
        // 数据
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建漏斗图
     * 
     * @param text
     *            标题
     * @param dataList
     *            漏斗图的值
     */
    public static String createFunnel(String text, List<EchartsData> dataList) {
        Option option = new Option();
        // 设置标题
        option.title(text);
        // 设置图例
        Legend legendBean = new Legend();
        Object[] legendData = new Object[dataList.size()];
        List<Object> series = new ArrayList<Object>();
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("value", dataList.get(i).getData().toArray(new Object[dataList.get(i).getData().size()]));
                map.put("name", dataList.get(i).getName());
                series.add(map);
            }
        }
        legendBean.x(X.center).y(Y.bottom);
        legendBean.setSelectedMode(true);
        legendBean.data(legendData);
        option.setLegend(legendBean);
        // 设置工具提示
        Tooltip tooltip = new Tooltip().formatter("{a} <br/>{b} : {c}");
        tooltip.trigger(Trigger.item);
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(new Mark().show(false), Tool.dataView, new MagicType().show(false), Tool.restore,
                Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 数据
        Funnel funnelSeries = new Funnel();
        funnelSeries.setName(text);
        funnelSeries.data(series.toArray(new Object[series.size()]));
        funnelSeries.setType(SeriesType.funnel);
        series.add(funnelSeries);
        option.series(funnelSeries);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建玫瑰图
     * 
     * @param text
     *            标题
     */
    public static String createRose(String text, List<EchartsData> dataList) {
        Option option = new Option();
        // 设置标题
        option.title().x(X.center).text(text);
        // 设置工具提示
        Tooltip tooltip = new Tooltip().formatter("{a} <br/>{b} : {c} ({d}%)");
        tooltip.setTrigger(Trigger.item);
        option.setTooltip(tooltip);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(new Mark().show(false), Tool.dataView, new MagicType(Magic.pie, Magic.funnel).show(false),
                Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        // 设置图例
        Legend legendBean = new Legend().x(X.center).y(Y.bottom);
        Object[] legendData = new Object[dataList.size()];
        List<Object> series = new ArrayList<Object>();
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("value", dataList.get(i).getData().toArray(new Object[dataList.get(i).getData().size()]));
                map.put("name", dataList.get(i).getName());
                series.add(map);
            }
        }
        legendBean.data(legendData);
        option.setLegend(legendBean);
        Pie roseSeries = new Pie();
        // 数据
        String radius[] = { "20", "110" };
        roseSeries.setName(text);
        roseSeries.setType(SeriesType.pie);
        roseSeries.setRadius(radius);
        roseSeries.setRoseType(RoseType.area);
        roseSeries.data(series.toArray(new Object[series.size()]));
        option.series(roseSeries);
        return JsonUtils.toJSONString(option);
    }
    /**
     * 创建中国地图
     * 
     * @param text
     *            柱状图标题
     */
    public static String createChinaMap(String text, List<MapData> dataList) {
        Option option = new Option();
        List<Series> series = new ArrayList<Series>();
        Object[] legendData = new Object[dataList.size()];
        List<EchartsData> valueList = new ArrayList<EchartsData>();
        int dataRangeMax = 0;
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                legendData[i] = dataList.get(i).getName();
                EMap seriesBean = new EMap();
                seriesBean.setMapType("china");
                seriesBean.setType(SeriesType.map);
                seriesBean.setName(dataList.get(i).getName());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("value", dataList.get(i).getData());
                map.put("name", dataList.get(i).getName());
                seriesBean.data(map);
                valueList.addAll(dataList.get(i).getData());
                seriesBean.itemStyle().normal().show(true);
                seriesBean.itemStyle().emphasis().show(true);
                series.add(seriesBean);
            }
            int valueArray[] = new int[valueList.size()];
            int count = 0;
            for (EchartsData echartsData : valueList) {
                for (Object object : echartsData.getData()) {
                    valueArray[count] = Integer.parseInt(object.toString());
                    count++;
                }
            }
            Arrays.sort(valueArray);
            dataRangeMax = valueArray[valueArray.length - 1];
        }
        // 设置标题
        option.title().text(text).x(X.center);
        // 设置工具提示
        option.tooltip(Trigger.item);
        // 设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(Tool.mark, Tool.dataView, new MagicType().show(false), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        toolbox.setX(X.right);
        toolbox.setY(Y.center);
        toolbox.setOrient(Orient.vertical);
        option.setToolbox(toolbox);
        // 值域漫游
        DataRange dataRange = new DataRange();
        dataRange.setX(X.left);
        dataRange.setY(Y.bottom);
        dataRange.setCalculable(true);
        dataRange.setMin(0);
        dataRange.setMax(dataRangeMax);
        option.setDataRange(dataRange);
        // VisualMap
        List<VisualMap> visualMapList = new ArrayList<VisualMap>();
        VisualMap map = new VisualMap().min(0).max(1000000000).left(X.left).top(Y.bottom).calculable(true)
                .text(new String[] { "高", "低" });
        visualMapList.add(map);
        option.visualMap(visualMapList);
        // 指定漫游组件可控地图类型
        RoamController roamController = new RoamController();
        roamController.setShow(true);
        roamController.setX(X.right);
        roamController.mapTypeControl("china", true);
        option.setRoamController(roamController);
        // 设置图例
        Legend legend = new Legend();
        legend.setOrient(Orient.vertical);
        legend.setX(X.left);
        legend.data(legendData);
        option.setLegend(legend);
        // 设置数据
        option.series(series);
        return JsonUtils.toJSONString(option);
    }
}
