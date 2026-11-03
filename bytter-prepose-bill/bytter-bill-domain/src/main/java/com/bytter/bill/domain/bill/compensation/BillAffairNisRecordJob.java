package com.bytter.bill.domain.bill.compensation;

import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbEmitBillService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billbank.dao.BillAbVoucherinfoViceDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.UserDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import oracle.sql.DATE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 票据补偿结算事务
 * @author pzc  2026/03/11
 * @since 1.0.0
 */
@Slf4j
@Component
public class BillAffairNisRecordJob implements ITaskService {

    @Autowired
    private BillAbVoucherinfoViceDao billAbVoucherinfoViceDao;

    @Autowired
    private AbEmitBillService abEmitBillService;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    /**
     * 用最简单的方式补偿
     * --票据补偿机制方案
     *
     * 1、查询票据从表，来源类型为10付款通知的全部数据。找到来源id
     * select VOUCHER_ID,CREATE_DATE from ab_voucherinfo_vice WHERE SOURCE='10'
     *
     * 2、通过VOUCHER_ID来源id去查询付款通知表
     * SELECT * FROM nis_billhead WHERE id='b141aa5f59e44df39f1ad1452616fb9e'
     *
     * 3、如果找到，说明事务准确，无需处理
     *
     * 4、如果未找到，说明事务异常。执行删除票据逻辑
     * @param arguments
     */
    @XxlJob("BillAffairNisRecordJob")
    public void run(String arguments) {
        log.info("Execute task: BillAffairNisRecordJob ");

        // 创建一个要处理的Date对象
        Date date = new Date();
        // 创建一个Calendar对象
        Calendar calendar = Calendar.getInstance();
        // 将Date对象设置为Calendar对象的时间
        calendar.setTime(date);
        // 结束时间将Calendar对象的时间减半小时（即减去30分钟）
        calendar.add(Calendar.MINUTE, -30);
        // 将Calendar对象转换为Date对象
        Date date2 = calendar.getTime();


        // 开始时间将Calendar对象的时间减去30天
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        // 将Calendar对象转换为Date对象
        Date date1 = calendar.getTime();


        String statusDate= DateUtils.format(date1,DateUtils.PATTERN_CLASSICAL);
        String endDate= DateUtils.format(date2,DateUtils.PATTERN_CLASSICAL);


        //拿到票据存在了，付款单不存在的数据
        List<BillAbVoucherinfoVice>  billViceList=billAbVoucherinfoViceDao.queryBySource("10",statusDate,endDate);


        if (billViceList!=null &&billViceList.size()!=0) {

            List<AbAccrecordsVO> list=new ArrayList<>();
            for (BillAbVoucherinfoVice vice : billViceList) {
                List<AbAccrecordsVO> abAccrecordsVOAS = abAccrecordsService.listAbAccrecordsIdByVvId(vice.getId());
                list.addAll(abAccrecordsVOAS);
            }

            //删除票据信息
            if (list.size() != 0) {
                String aids=list.stream()
                        .map(vo -> String.valueOf(vo.getId()))
                        .collect(Collectors.joining(","));
                UserDTO userDTO=new UserDTO();
                userDTO.setLoginName("admin");
                abEmitBillService.batchDelete(aids,"事务异常删除",userDTO);
            }



        }

    }

}
