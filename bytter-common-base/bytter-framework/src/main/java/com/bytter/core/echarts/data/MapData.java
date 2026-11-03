package com.bytter.core.echarts.data;
import java.util.List;
/**
 * Created by zhouj on 2017/8/25.
 */
public class MapData {
    private String name; // 名称
    private List<EchartsData> data;
    public MapData() {
        super();
    }
    public MapData(String name, List<EchartsData> data) {
        super();
        this.name = name;
        this.data = data;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<EchartsData> getData() {
        return data;
    }
    public void setData(List<EchartsData> data) {
        this.data = data;
    }
}
