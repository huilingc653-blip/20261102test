package com.bytter.core.echarts.style;
import com.github.abel533.echarts.style.itemstyle.Normal;
import java.io.Serializable;
/**
 * Created by zhouj on 2017/9/1.
 */
public class LineAreaStyle implements Serializable {
    private static final long serialVersionUID = 418674375057055357L;
    private Normal normal;
    public LineAreaStyle() {
    }
    public Normal normal() {
        if (this.normal == null) {
            this.normal = new Normal();
        }
        return this.normal;
    }
    public LineAreaStyle normal(Normal normal) {
        this.normal = normal;
        return this;
    }
    public Normal getNormal() {
        return this.normal;
    }
    public void setNormal(Normal normal) {
        this.normal = normal;
    }
}
