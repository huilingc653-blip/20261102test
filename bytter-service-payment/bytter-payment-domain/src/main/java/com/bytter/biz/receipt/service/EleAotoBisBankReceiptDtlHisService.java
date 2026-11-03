package com.bytter.biz.receipt.service;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.core.base.AbstractService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: service
 * @description: 银行电子回单定时扫描到历史表
 * @author: qijh
 * @create: 2019-12-05 11:02
 **/
@Service("eleAotoBisBankReceiptDtlHisService")
@Transactional
public class EleAotoBisBankReceiptDtlHisService extends AbstractService implements ITaskService {
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired(required = false)
    private BisBankReceiptDtlService bisBankReceiptDtlService;
    @Autowired(required = false)
    private EleAutoImplementService eleAutoImplementService;
    @Override
    @XxlJob("EleAotoBisBankReceiptDtlHisService")
    public void run(String arguments) {
        this.logger.info("---自动根据参数将日期前的电子回单扫描到历史表开始执行定时任务---");
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_TRANSFERDAYS);
        ConfigDTO configNumber = configService.getConfig(ConfigConstant.ELE, ReceiptConstant.ELE_CONFIG_TRANSFER_NUMBER);
        //根据参数获取提前天数
        int days = Integer.parseInt(configDTO.getValue());
        //当前日期
        Date nowDate = DateUtils.getDate();
        nowDate = DateUtils.addDay(nowDate, -days);
        DataTablesInput input = new DataTablesInput();
        // ----------- update by zhoul 2020-11-23 -----------
        // ----------- 回单迁移更改每次迁移10笔，一次性迁移太多，会导致事务超时 -----------
        input.setLength((configNumber == null || StringUtils.isBlank(configNumber.getValue())) ? 10 : Integer.parseInt(configNumber.getValue()));
        DataTablesOutput data = bisBankReceiptDtlService.getByTime(input, nowDate);
        if (!CollectionUtils.isEmpty(data.getData())) {
            eleAutoImplementService.runBisBankReceiptDtlHis(data.getData());
        }
        // ----------- 回单迁移修改结束 -----------
//        BigDecimal count = bisBankReceiptDtlService.getByTimeCount(nowDate);
//        int length = count.intValue()/20+1;
//        for(int i = 0;i<length;i++) {
//            DataTablesOutput data = bisBankReceiptDtlService.getByTime(input, nowDate);
//            eleAutoImplementService.runBisBankReceiptDtlHis(data.getData());
//        }
//        List<List<BisBankReceiptDtlVO>> list = this.averageAssign(bisBankReceiptDtlVOList,20);
//        for(List<BisBankReceiptDtlVO> volist : list){
//            eleAutoImplementService.runBisBankReceiptDtlHis(volist);
//        }
        this.logger.info("---自动根据参数将日期前的电子回单扫描到历史表结束执行定时任务---");
    }
    public <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> listGroup = new ArrayList<List<T>>();
        int listSize = source.size();
        int toIndex = n;
        for (int i = 0; i < source.size(); i += toIndex) {
            if (i + toIndex > listSize) {
                toIndex = listSize - i;
            }
            List<T> newList = source.subList(i, i + toIndex);
            listGroup.add(newList);
        }
        return listGroup;
    }
}
