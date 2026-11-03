package com.bytter.core.echarts.data;
import java.util.List;
/**
 * Created by zhouj on 2017/8/30.
 */
public class EchartsData {
    /**
     * 堆积维度，以哪个属性堆积，堆积图用，标准柱状图（普通柱状图）不需要设值
     */
    private String stack;
    /**
     * 名称
     */
    private String name;
    /**
     * 数据
     */
    private List<Object> data;
    public EchartsData() {
        super();
    }
    public EchartsData(String name, List<Object> data) {
        super();
        this.name = name;
        this.data = data;
    }
    public EchartsData(String stack, String name, List<Object> data) {
        super();
        this.stack = stack;
        this.name = name;
        this.data = data;
    }
    public String getStack() {
        return stack;
    }
    public void setStack(String stack) {
        this.stack = stack;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Object> getData() {
        return data;
    }
    public void setData(List<Object> data) {
        this.data = data;
    }
}
