package com.bytter.biz.fundwarn.service;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.core.base.AbstractService;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 预警消息推送操作服务类
 * @author zhengg
 *
 */
@Service("fundWarnNotificationService")
@Transactional
public class FundWarnNotificationService extends AbstractService {
	
	@DubboReference(check = false)
	private INotificationService notificationService;
	
	/**
	 * 发送消息调用
	 * @param dataInfo 数据对象
	 */
	public void sendMsg(BtWarnDataInfoVO dataInfo){
		if(dataInfo!=null&&checkIsSendMsg(dataInfo)){
			notificationService.send(dataInfo.getId(), dataInfo.getCorpId(), BtFundWarnUtils.getWarnNotification(dataInfo.getTypeCode(), dataInfo.getDimensionCode()), dataInfo);
		}
	}
	
	/***
	 * 检查是否需求做消息推送
	 * @param dataInfo 信息实体
	 * @return
	 */
	private boolean checkIsSendMsg(BtWarnDataInfoVO dataInfo){
		boolean reBoo=false;
//		//大额现金支出和大额账户支出有信息就提醒
//		if(FundWarnConstant.TYPECODE_BANKACCPAY.equals(dataInfo.getTypeCode())
//				||FundWarnConstant.TYPECODE_CASHPAY.equals(dataInfo.getTypeCode())){
//			reBoo=true;
//		}
		if("1".equals(dataInfo.getSendMsgSign())){
			reBoo=true;
		}
		return reBoo;
	}
}
