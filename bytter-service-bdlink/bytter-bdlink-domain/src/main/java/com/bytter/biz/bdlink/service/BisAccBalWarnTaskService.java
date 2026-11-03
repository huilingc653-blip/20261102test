package com.bytter.biz.bdlink.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.dao.BisBifMonitorDao;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.spi.service.IFundWarnExceptionDataService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.enums.AccBalMessageEnum;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 异常告警服务类
 * @author zhous
 * @date 2024/8/21 16:52
 */
@DubboService
@Service("bisAccBalWarnTaskService")
@Transactional
public class BisAccBalWarnTaskService extends AbstractService implements ITaskService {


    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @DubboReference(check = false)
    private IFundWarnExceptionDataService fundWarnExceptionDataService;

    @Autowired
    private BisBifMonitorDao bisBifMonitorDao;

    @Override
    @XxlJob("BisAccBalWarnTaskService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        try {
            this.logger.info("---启用余额异常告警定时任务，开始执行定时任务---");
            fundWarnExceptionDataTask(arguments);
            this.logger.info("---启用余额异常告警定时任务，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description 余额异常告警监控
     * @author zhous
     * @date 2024/8/22 16:32
     * @param arguments 分钟
     */
    private void fundWarnExceptionDataTask(String arguments) {
        if(StrUtil.isEmpty(arguments)){
            throw new ParameterException("参数不能为空");
        }
        if(!NumberUtil.isNumber(arguments)){
            throw new ParameterException("参数不是数字");
        }
        Integer minutes = Integer.valueOf(arguments);
        List<BisBifInitDTO> needSendList = new ArrayList<>();
        //获取是否直连银行为'是'的银行类型进行余额异常监控告警
        List<BisBifInitDTO> bisBifInitDTOS = bisBifInitService.getDirectConnectBankType();
        if(CollectionUtil.isNotEmpty(bisBifInitDTOS)){
            for(BisBifInitDTO bisBifInitDTO:bisBifInitDTOS){
                //判断该银行是否存在有效的在线账号
                BtBankAccDTO dto = new BtBankAccDTO();
                dto.setBifCode(bisBifInitDTO.getId());
                dto.setIsOnline(CommonConstant.YES);
                dto.setValidSign(CommonConstant.YES);
                List<BtBankAccDTO> bankAccList = btBankAccService.getBankAccList(dto);
                if (EmptyUtils.isEmptyList(bankAccList)) {
                    continue;
                }
                //获取银行最新的余额获取时间
                Date getMaxBalDate = bisBifMonitorDao.getMaxBalDate(bisBifInitDTO.getId());
                Date now = DateUtil.date();
                Date offsetMinuteDate = null;
                if(getMaxBalDate != null){
                    offsetMinuteDate = DateUtil.offsetMinute(getMaxBalDate, minutes.intValue());
                }
                if(getMaxBalDate == null || DateUtil.compare(offsetMinuteDate,now) < 0){
                    needSendList.add(bisBifInitDTO);
                }
            }
        }
        if(CollectionUtil.isNotEmpty(needSendList)){
            StringBuilder content = new StringBuilder();
            for (int i = 0; i <= needSendList.size()-1 ; i++) {
                content.append("【");
                content.append(needSendList.get(i).getName());
                content.append("】");
                if(i != needSendList.size()-1){
                    content.append("、");
                }
            }
            content.append("已超过【");
            content.append(arguments);
            content.append("】分钟未获取到余额，请检查。");
            //群发消息告警并保存记录
            fundWarnExceptionDataService.sendWarnExceptionDataByGroupChatBot(content.toString(), FundWarnConstant.EXCEPTION_WARN_CHANNEL_ALL,FundWarnConstant.EXCEPTION_WARN_TYPE_0);
        }
    }
}
