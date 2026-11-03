package com.bytter.biz.report.service;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.report.dao.BankAccountCountDao;
import com.bytter.biz.report.vo.BankAccountCountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.echarts.data.EchartsData;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.github.abel533.echarts.*;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.feature.DataZoom;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.Mark;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @author shenc
 * @Description 银行账号数量统计表
 * @date 2018.12.1
 */
@Transactional
@Service("bankAccountCountService")
public class BankAccountCountService extends BaseService<BankAccountCountVO,String> {
    @Autowired
    private BankAccountCountDao bankAccountCountDao;
    @DubboReference
    ICorpService corpService;
    @DubboReference
    IBtCurrencyService btCurrencyService;
    @Override
    public BaseDao<BankAccountCountVO, String> getEntityDao() {
        return bankAccountCountDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser currentUser) {
        Date startDate = DateUtils.str2Date((String) dataTablesInput.getFormdata().get("ged_regDate"),DateUtils.date_sdf());
        Date endDate = DateUtils.str2Date((String) dataTablesInput.getFormdata().get("led_regDate"),DateUtils.date_sdf());
        String status = (String) dataTablesInput.getFormdata().get("eqs_status");
        DataTablesOutput output = bankAccountCountDao.getDataTablesUnion(dataTablesInput);
        List<BankAccountCountVO> data = output.getData();
        Map<String, CorpDTO> mapCorp = corpService.getCorpMap();
  	    Map<String, BtCurrencyDTO> mapCur = btCurrencyService.getCurrency();
        //当查询状态为正常时,不用计算已被注销的账号的数量
        if ("N".equalsIgnoreCase(status)) {
            for (BankAccountCountVO vo : data) {
                vo.setCancelCount(0);
                vo.setAvailableCount(vo.getCount() - vo.getCancelCount());
                vo.setCorpName(!ObjectUtils.isEmpty(mapCorp.get(vo.getCorpId())) ? mapCorp.get(vo.getCorpId()).getName() : "");
                vo.setCurName(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? mapCur.get(vo.getCurId()).getCurName() : "");
            }
        }else{
            for (BankAccountCountVO vo : data) {
                Integer cancelCount = bankAccountCountDao.getCancelBankCount(vo, startDate, endDate);
                vo.setCancelCount(cancelCount);
                vo.setAvailableCount(vo.getCount() - vo.getCancelCount());
                vo.setCorpName(!ObjectUtils.isEmpty(mapCorp.get(vo.getCorpId())) ? mapCorp.get(vo.getCorpId()).getName() : "");
                vo.setCurName(!ObjectUtils.isEmpty(mapCur.get(vo.getCurId())) ? mapCur.get(vo.getCurId()).getCurName() : "");
            }
        }
        output.setData(data);
        return output;
    }
    public Object getBankCountData(BankAccountCountVO vo){
        List<BankAccountCountVO> bankCount = bankAccountCountDao.getBankCountByBankType(vo);
        Object data = dealPieDate("银行账号数量统计表(银行类别)",bankCount);
        return data;
    }
    public Object getBankCountDataByNature(BankAccountCountVO vo){
        List<BankAccountCountVO> bankCount = bankAccountCountDao.getBankCountDataByNature(vo);
        Object data = formatData("银行账号数量统计表(性质)",bankCount);
        return data;
    }
    /**
     * 账户性质创建饼图数据
     * 组装List<EchartsData>数据
     * @param title
     * @param bankCount
     * @return
     */
    private Object formatData(String title, List<BankAccountCountVO> bankCount) {
        List<EchartsData> mergeList = new ArrayList<>();
        for (int i = 0; i < bankCount.size(); i++) {
            EchartsData data = new EchartsData();
            data.setName(bankCount.get(i).getNatureName());
            List<Object> list = new ArrayList<Object>();
            list.add(bankCount.get(i).getCount());
            data.setData(list);
            mergeList.add(data);
        }
        List<Object> radiusArray = new ArrayList<Object>();
        radiusArray.add("0%");
        radiusArray.add("50%");
        return createPieAndRingJson("账户性质",title,"center","center",radiusArray,mergeList);
    }
    /**
     * 银行列表创建饼图数据
     * 根据数据库中查询的结果拼接饼图数据
     * @param bankCount
     */
    private Object dealPieDate(String title, List<BankAccountCountVO> bankCount) {
        List<EchartsData> echartsDatas;
        echartsDatas = mergeExceed(bankCount);
        List<Object> radiusArray = new ArrayList<Object>();
        radiusArray.add("0%");
        radiusArray.add("50%");
        return createPieAndRingJson("银行类别",title, "center", "center", radiusArray, echartsDatas);
    }
    /**
     * 合并超过10项的其他银行数据
     * @param bankCount
     * @return
     */
    private List<EchartsData> mergeExceed(List<BankAccountCountVO> bankCount){
        List<EchartsData> mergeList = new ArrayList<>();
        Integer otherCount = 0;
        for (int i=0;i<bankCount.size();i++){
            if (i<9){
                EchartsData data = new EchartsData();
                data.setName(bankCount.get(i).getBankTypeName());
                List<Object> list = new ArrayList<Object>();
                list.add(bankCount.get(i).getCount());
                data.setData(list);
                mergeList.add(data);
            }else{
                otherCount += bankCount.get(i).getCount();
            }
        }
        if (bankCount.size()>10) {
            EchartsData vo = new EchartsData();
            vo.setName("其他");
            List<Object> list = new ArrayList<Object>();
            list.add(otherCount);
            vo.setData(list);
            mergeList.add(vo);
        }
        return mergeList;
    }
    private Object createPieAndRingJson(String subTitle,String text,String x,String y,List<Object> radiusArray,List<EchartsData> dataList) {
        Option option = new Option();
        //设置标题
        Title title = new Title();
        title.setText(text);
        title.setX(X.center);
        option.setTitle(title);
        //设置工具提示
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("{a} <br/>{b} : {c} ({d}%)");
        tooltip.setTrigger(Trigger.item);
        option.setTooltip(tooltip);
        //设置工具栏
        Toolbox toolbox = new Toolbox().show(true);
        toolbox.feature(new Mark().show(false), Tool.dataView, new DataZoom().show(false), new MagicType(Magic.pie, Magic.funnel).show(true), Tool.restore, Tool.saveAsImage);
        option.setToolbox(toolbox);
        //设置图例
        Legend legendBean = new Legend().orient(Orient.vertical).x(X.right).y(Y.valueOf(y));
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
        Normal normal = new Normal();
        normal.setFormatter("{d}%");
        ItemStyle itemStyle = new ItemStyle();
        itemStyle.setNormal(normal);
        ringSeries.name(subTitle).type(SeriesType.pie).radius(radiusArray.toArray(new Object[radiusArray.size()])).label(itemStyle);
        ringSeries.data(series.toArray(new Object[series.size()]));
        option.series(ringSeries);
        return JSONUtil.parse(option);
    }
}
