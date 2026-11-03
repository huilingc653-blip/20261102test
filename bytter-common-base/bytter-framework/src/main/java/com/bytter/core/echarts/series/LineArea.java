package com.bytter.core.echarts.series;
import com.bytter.core.echarts.style.LineAreaStyle;
import com.github.abel533.echarts.code.DataFilter;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Series;
/**
 * Created by zhouj on 2017/9/1.
 */
public class LineArea extends Series<LineArea> {
    private Boolean smooth;
    private Object dataFilter;
    private LineAreaStyle areaStyle;
    public LineArea() {
        this.type(SeriesType.line);
    }
    public LineArea(String name) {
        super(name);
        this.type(SeriesType.line);
    }
    public Object dataFilter() {
        return this.dataFilter;
    }
    public LineArea dataFilter(Object dataFilter) {
        this.dataFilter = dataFilter;
        return this;
    }
    public LineArea dataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
        return this;
    }
    public Boolean smooth() {
        return this.smooth;
    }
    public LineArea smooth(Boolean smooth) {
        this.smooth = smooth;
        return this;
    }
    public Boolean getSmooth() {
        return this.smooth;
    }
    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
    }
    public Object getDataFilter() {
        return this.dataFilter;
    }
    public void setDataFilter(Object dataFilter) {
        this.dataFilter = dataFilter;
    }
    public LineAreaStyle areaStyle() {
        if (this.areaStyle == null) {
            this.areaStyle = new LineAreaStyle();
        }
        return this.areaStyle;
    }
    public LineArea areaStyle(LineAreaStyle areaStyle) {
        this.areaStyle = areaStyle;
        return this;
    }
    public LineAreaStyle getAreaStyle() {
        return areaStyle;
    }
    public void setAreaStyle(LineAreaStyle areaStyle) {
        this.areaStyle = areaStyle;
    }
}
