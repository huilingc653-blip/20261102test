package com.bytter.system.web.controller;
import com.bytter.core.echarts.ChartsFactory;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.echarts.data.MapData;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.service.SysAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 图表测试
 * Created by zhouj on 2017/9/1.
 */
@Controller
@RequestMapping("/echart/example")
public class EchartsController {
    @Autowired
    private SysAttachmentService sysAttachmentService;
    //图表
    @GetMapping("/echarts")
    public String echarts() {
        return "attachment/echarts";
    }
    //仪表盘
    @PostMapping("/echartsGauge")
    @ResponseBody
    public String echartsGauge() throws IOException {
        List<EchartsData> dataList = new ArrayList<EchartsData>();
        List<Object> o = new ArrayList<Object>();
        o.add(22);
        EchartsData e = new EchartsData("", "sss", o);
        dataList.add(e);
        List<Object> o1 = new ArrayList<Object>();
        o1.add(50);
        EchartsData e1 = new EchartsData("", "aaa", o1);
        dataList.add(e1);
        return ChartsFactory.createGauge("test", 0, 100, dataList);
    }
    //柱状图
    @PostMapping("/echartsData")
    @ResponseBody
    public String echartsData() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<Object> xlist = new ArrayList<Object>();
        xlist.add("周一");
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            EchartsData data = new EchartsData("", dto.getName(), datas);
            ylist.add(data);
        }
        return ChartsFactory.createBar("test", "x轴", xlist, "y轴", "kb", ylist);
    }
    //柱状和折线混搭图
    @PostMapping("/createMashupOne")
    @ResponseBody
    public String createMashupOne() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<Object> xlist = new ArrayList<Object>();
        xlist.add("周一");
        xlist.add("周二");
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        List<EchartsData> ylist1 = new ArrayList<EchartsData>();
        int count = 1;
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            for (AttachmentDTO dto1 : list) {
                datas.add(count);
                count++;
            }
            EchartsData data = new EchartsData("", dto.getName(), datas);
            ylist.add(data);
        }
        List<Object> datas = new ArrayList<Object>();
        datas.add(1222);
        datas.add(3344);
        EchartsData data = new EchartsData("", "轴是", datas);
        ylist1.add(data);
        return ChartsFactory.createMashupOne("test", "x轴", xlist, "y轴", "kb", "y轴1", "M", ylist, ylist1);
    }
    //漏斗图
    @PostMapping("/echartsFunnel")
    @ResponseBody
    public String echartsFunnel() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<EchartsData> list1 = new ArrayList<EchartsData>();
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            EchartsData data = new EchartsData(dto.getName(), datas);
            list1.add(data);
        }
        return ChartsFactory.createFunnel("test", list1);
    }
    //折线图
    @PostMapping("/echartsLine")
    @ResponseBody
    public String echartsLine() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<Object> xlist = new ArrayList<Object>();
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        int count = 1;
        for (AttachmentDTO dto : list) {
            xlist.add(dto.getName());
            List<Object> datas = new ArrayList<Object>();
            for (AttachmentDTO dto1 : list) {
                datas.add(count);
                count++;
            }
            EchartsData data = new EchartsData("", dto.getName(), datas);
            ylist.add(data);
        }
        return ChartsFactory.createLine("test", "x轴", xlist, "y轴", "kb", ylist);
    }
    //标准面积图/堆积面积图
    @PostMapping("/echartsArea")
    @ResponseBody
    public String echartsArea() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<Object> xlist = new ArrayList<Object>();
        xlist.add("周一");
        xlist.add("周二");
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        int count = 100;
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            datas.add(count);
            count++;
            EchartsData data = new EchartsData(dto.getName(), dto.getName(), datas);
            ylist.add(data);
        }
        return ChartsFactory.createAreaStack("test", "x轴", xlist, "y轴", "kb", ylist);
    }
    //温度计式图表
    @PostMapping("/createTermometer")
    @ResponseBody
    public String createTermometer() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<Object> xlist = new ArrayList<Object>();
        xlist.add("周一");
        xlist.add("周二");
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        int count = 100;
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(new BigDecimal(dto.getFileSize() + "").divide(new BigDecimal(1024)).floatValue());
            datas.add(count);
            count++;
            EchartsData data = new EchartsData("sum", dto.getName(), datas);
            ylist.add(data);
        }
        return ChartsFactory.createTermometer("test", "x", xlist, "y", "%", ylist);
    }
    //饼图
    @PostMapping("/echartsPie")
    @ResponseBody
    public String echartsPie() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<EchartsData> ylist = new ArrayList<EchartsData>();
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            EchartsData data = new EchartsData("", dto.getName(), datas);
            ylist.add(data);
        }
        return ChartsFactory.createPie("test", "center", "top", "50%", ylist);
    }
    //玫瑰图
    @PostMapping("/createRose")
    @ResponseBody
    public String createRose() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<EchartsData> list1 = new ArrayList<EchartsData>();
        for (AttachmentDTO dto : list) {
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            EchartsData data = new EchartsData(dto.getName(), datas);
            list1.add(data);
        }
        return ChartsFactory.createRose("test", list1);
    }
    //地图
    @PostMapping("/createChinaMap")
    @ResponseBody
    public String createChinaMap() throws IOException {
        List<AttachmentDTO> list = sysAttachmentService.findAttachmentByGroupId("402880415e11bbe4015e11bbe45c0000");
        List<MapData> list1 = new ArrayList<MapData>();
        for (AttachmentDTO dto : list) {
            List<EchartsData> dataList = new ArrayList<EchartsData>();
            List<Object> datas = new ArrayList<Object>();
            datas.add(dto.getFileSize());
            EchartsData echartsData = new EchartsData(dto.getName(), datas);
            dataList.add(echartsData);
            MapData data = new MapData(dto.getName(), dataList);
            list1.add(data);
        }
        return ChartsFactory.createChinaMap("test", list1);
    }
}
