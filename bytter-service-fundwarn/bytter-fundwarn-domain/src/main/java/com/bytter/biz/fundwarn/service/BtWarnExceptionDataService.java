package com.bytter.biz.fundwarn.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.fundwarn.dao.BtWarnExceptionDataDao;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.entity.BtWarnExceptionData;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.spi.service.IFundWarnExceptionDataService;
import com.bytter.biz.fundwarn.spi.service.IFundWarnService;
import com.bytter.biz.fundwarn.vo.BtWarnExceptionDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICorpWeiXinMessageService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IDingTalkMessageService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.GroupChatBotMsgDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
* @Description 异常告警服务类
* @author zhous
* @date 2024/8/21 16:52
*/
@DubboService
@Service("btWarnExceptionDataService")
@Transactional
public class BtWarnExceptionDataService extends BaseService<BtWarnExceptionData,String> implements IFundWarnExceptionDataService {

    @Autowired
    private BtWarnExceptionDataDao btWarnExceptionDataDao;

    @DubboReference(check = false)
    private IDictService dictService;

    @DubboReference(check = false)
    private ICorpWeiXinMessageService corpWeiXinMessageService;

    @DubboReference(check = false)
    private IDingTalkMessageService dingTalkMessageService;

    @Value("${weiXin.corp.bot.webhookUrl}")
    private String corpWxWebHookUrl;

    @Value("${dingTalk.bot.token}")
    private String dingTalkToken;

    @Value("${dingTalk.bot.secret}")
    private String dingTalkSecret;

    @Override
    public BaseDao<BtWarnExceptionData, String> getEntityDao() {
        return btWarnExceptionDataDao;
    }

    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listWarnData(DataTablesInput dataTablesInput) {
        DataTablesOutput<BtWarnExceptionDataVO>  dataTablesOutput = btWarnExceptionDataDao.getDataTables(dataTablesInput);
        if (ObjectUtil.isNotEmpty(dataTablesOutput.getData())) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("EXCEPTION_WARN_DATA_SEND_STATUS", "EXCEPTION_WARN_DATA_CHANNEL", "EXCEPTION_WARN_DATA_TYPE");
            for (BtWarnExceptionDataVO vo : dataTablesOutput.getData()) {
                injectValues(vo, listDictMap);
            }
        }
        return dataTablesOutput;
    }

    /**
    * @Description 根据id获取异常告警信息
    * @author zhous
    * @date 2024/8/22 14:04
    */
    public BtWarnExceptionDataVO getWarnExceptionDataById(String id) {
        BtWarnExceptionDataVO vo = new BtWarnExceptionDataVO();
        BtWarnExceptionData btWarnExceptionData = btWarnExceptionDataDao.get(id, true);
        BeanUtils.copyNotNullProperties(btWarnExceptionData, vo);
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("EXCEPTION_WARN_DATA_SEND_STATUS", "EXCEPTION_WARN_DATA_CHANNEL", "EXCEPTION_WARN_DATA_TYPE");
        injectValues(vo, listDictMap);
        return vo;
    }


    private void injectValues(BtWarnExceptionDataVO vo,Map<String, Map<String, DictDTO>> listDictMap){
        // 发送状态 0-发送成功 1-发送失败
        Optional.ofNullable(vo.getSendStatus()).ifPresent(sendStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("EXCEPTION_WARN_DATA_SEND_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendStatus + "")))
                    .ifPresent(dict -> vo.setSendStatusName(dict.getName()));
        });
        // 渠道 0-企业微信 1-钉钉
        Optional.ofNullable(vo.getChannel()).ifPresent(channel -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("EXCEPTION_WARN_DATA_CHANNEL");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(channel + "")))
                    .ifPresent(dict -> vo.setChannelName(dict.getName()));
        });
        // 告警类型 0-应用接口 1-前置机服
        Optional.ofNullable(vo.getWarnType()).ifPresent(warnType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("EXCEPTION_WARN_DATA_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(warnType + "")))
                    .ifPresent(dict -> vo.setWarnTypeName(dict.getName()));
        });
    }

    @Override
    public void sendWarnExceptionDataByGroupChatBot(String warnContent, String sendChannel,String warnType) {
        switch (sendChannel) {
            case FundWarnConstant.EXCEPTION_WARN_CHANNEL_ALL://全渠道发送
                commonSendMsByGroupChatBot(warnContent,FundWarnConstant.EXCEPTION_WARN_CHANNEL_0,warnType);
                commonSendMsByGroupChatBot(warnContent,FundWarnConstant.EXCEPTION_WARN_CHANNEL_1,warnType);
                break;
            case FundWarnConstant.EXCEPTION_WARN_CHANNEL_0://企业微信
                commonSendMsByGroupChatBot(warnContent,FundWarnConstant.EXCEPTION_WARN_CHANNEL_0,warnType);
                break;
            case FundWarnConstant.EXCEPTION_WARN_CHANNEL_1://钉钉
                commonSendMsByGroupChatBot(warnContent,FundWarnConstant.EXCEPTION_WARN_CHANNEL_1,warnType);
                break;
        }
    }

    /**
    * @Description
    * @author zhous
    * @date 2024/8/23 9:30
    * @param warnContent 告警内容
    * @param channel 渠道 0-企业微信 1-钉钉
    * @param warnType 告警类型 0-应用接口 1-前置机服务
    */
    private void commonSendMsByGroupChatBot(String warnContent, String channel, String warnType) {
        GroupChatBotMsgDTO dto = null;
        if(FundWarnConstant.EXCEPTION_WARN_CHANNEL_0.equals(channel)){
            //企业微信群聊机器人发送信息
            dto = corpWeiXinMessageService.groupChatRobotPushText(corpWxWebHookUrl, warnContent, false, null, null);
        }else if(FundWarnConstant.EXCEPTION_WARN_CHANNEL_1.equals(channel)){
            //发送钉钉群聊机器人发送信息
            dto = dingTalkMessageService.groupChatRobotPushText(dingTalkToken, dingTalkSecret, warnContent, false, null, null);
        }
        saveWarnExceptionDataLog(dto, warnContent, channel, warnType);
    }

    /**
    * @Description 组装异常告警信息
    * @author zhous
    * @date 2024/8/22 16:20
    */
    private void saveWarnExceptionDataLog(GroupChatBotMsgDTO dto,String content, String sendChannel, String warnType) {
        BtWarnExceptionData data = new BtWarnExceptionData();
        data.setSendDate(new Date());
        data.setWarnContent(content);
        data.setChannel(sendChannel);
        data.setWarnType(warnType);
        data.setReqMsg(dto.getReqMsg());
        data.setRespMsg(dto.getRespMsg());
        data.setSendStatus(dto.getStatus());
        btWarnExceptionDataDao.save(data);
    }
}
