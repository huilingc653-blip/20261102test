package com.bytter.bill.domain.billbank.pool.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAbVoucherinfoViceDao;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillVoucherInfoDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolDtlImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolDtl;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据池明细表服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-26 <br>
 */
@Service("billPoolDtlImpService")
@Transactional
public class BillPoolDtlImpService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolDtlImpDao billPoolDtlImpDao;
	
	@Autowired
	private BillAccrecordsDao billAccrecordsImpDao;
	
	@Autowired
	private BillAbVoucherinfoViceDao billAbVoucherinfoViceDao;
	
	@Autowired
	private BillVoucherInfoDao billVoucherInfoDao;
	
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus
	 * @return
	 */
	public int updatePoolDtlOptStatusByBusId(String busId,String optStatus){
		if (logger.isDebugEnabled()) {
			logger.debug("busId="+busId+",busId="+busId);
		}
		if(StrUtil.isEmpty(busId)||StrUtil.isEmpty(optStatus)){
			return 0;
		}
		AbPoolDtl poolDtl=this.queryPoolDtlById(busId);
		if(optStatus.equals(poolDtl.getOptStatus())){
			return 0;
		}
		return billPoolDtlImpDao.updatePoolDtlOptStatusByBusId(busId, optStatus, null);
	}
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus
	 * @return
	 */
	public int updatePoolDtlOptStatusByBusId(String busId,String optStatus, String validSign){
		if (logger.isDebugEnabled()) {
			logger.debug("busId="+busId+",busId="+busId);
		}
		if(StrUtil.isEmpty(busId)||StrUtil.isEmpty(optStatus)){
			return 0;
		}
		AbPoolDtl poolDtl=this.queryPoolDtlById(busId);
		if(poolDtl!=null && optStatus.equals(poolDtl.getOptStatus())){
			return 0;
		}
		return billPoolDtlImpDao.updatePoolDtlOptStatusByBusId(busId, optStatus, validSign);
	}
	/**
	 * 根据业务id修改票据从表票据池状态和业务占用状态
	 * @param busId 业务ID
	 * @param poolStatus 池状态 空或0池外1池内2入池在途3出池在途
	 * @param boxStatus 业务占用状态
	 * @return
	 */
	public int updateVoucherinfoViceByBusId(String busId,String poolStatus,Integer boxStatus){
		if(StrUtil.isEmpty(busId)||StrUtil.isEmpty(poolStatus)||boxStatus==null){
			return 0;
		}
		return billAbVoucherinfoViceDao.updateVoucherinfoViceByBusId(busId, poolStatus, boxStatus);
	}
	
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus 指令状态
	 * @param reCode 银行接口返回代码
	 * @param reMsg 银行返回内容
	 * @param billId 外围系统流水号
	 * @return
	 */
	public int updateAccrecordsByBusId(String busId,String optStatus,String reCode,String reMsg,String billId){
		if(StrUtil.isEmpty(busId)
				||(StrUtil.isEmpty(optStatus)
						&&StrUtil.isEmpty(reCode)
						&&StrUtil.isEmpty(reMsg))){
			return 0;
		}
		return billAccrecordsImpDao.updateAccrecordsByBusId(busId, optStatus, reCode, reMsg, null, billId);
	}
	/**
	 * 根据业务id修改票据明细表有效标记
	 * @param busId 业务ID
	 * @param validSign 有效标记
	 * @return
	 */
	public int updateAccrecordsByBusId(String busId,String optStatus,String validSign){
		if(StrUtil.isEmpty(busId)
				||StrUtil.isEmpty(validSign)){
			return 0;
		}
		return billAccrecordsImpDao.updateAccrecordsByBusId(busId, optStatus, null, null, validSign);
	}
	/**
	 * 根据业务id修改票据明细表有效标记
	 * @param busId 业务ID
	 * @param validSign 有效标记
	 * @return
	 */
	public int updateAccrecordsByBusId(String busId,String validSign){
		if(StrUtil.isEmpty(busId)
				||StrUtil.isEmpty(validSign)){
			return 0;
		}
		return billAccrecordsImpDao.updateAccrecordsByBusId(busId, null, null, null, validSign);
	}
	/**
	 * 根据业务id修改票据明细表有效标记
	 * @param busId 业务ID
	 * @param validSign 有效标记
	 * @return
	 */
	public int updatePoolDtlValidSignByBusId(String busId,String validSign){
		if(StrUtil.isEmpty(busId)
				||StrUtil.isEmpty(validSign)){
			return 0;
		}
		return billPoolDtlImpDao.updatePoolDtlValidSignByBusId(busId, validSign);
	}
	/**
	 * <b>功能：</b>根据业务ID获取票据池明细表数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public AbPoolDtl queryPoolDtlById(String busId){
		if(StrUtil.isEmpty(busId)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("id", busId);
		return billPoolDtlImpDao.getAbPoolDtlByParams(params);
	}
	
	/**
	 * <b>功能：</b>根据业务ID获取票据池明细表数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public BillAbAccrecords queryAccrecordsById(String busId){
		if(StrUtil.isEmpty(busId)){
			return null;
		}
		return billAccrecordsImpDao.getBillAbAccrecordsById(busId);
	}
	/**
	 * <b>功能：</b>根据业务ID获取票据主明细表数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param busId 业务ID
	 * @return
	 */
	public BillAbVoucherinfo getBillAbVoucherinfoById(String busId){
		if(StrUtil.isEmpty(busId)){
			return null;
		}
		return billVoucherInfoDao.getBillAbVoucherinfoById(busId);
	}
	
	/**
	 * <b>功能：</b>根据传递参数（池ID和单位以及票据号）获取池内有效票据数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-03-04 <br>
	 * @param pbId 业务ID
	 * @return
	 */
	public AbPoolDtl getAbPoolDtlHoldByParams(String pbId,String corpId,String origNo){
		if(StrUtil.isEmpty(pbId)||StrUtil.isEmpty(corpId)||StrUtil.isEmpty(origNo)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("pbId", pbId);
		params.put("corpId", corpId);
		params.put("origNo", origNo);
		params.put("briefIds", new String[]{BillPoolConstant.BRIEF_TYPE_IN,BillPoolConstant.BRIEF_TYPE_TO_HOSTING,BillPoolConstant.BRIEF_TYPE_TO_PLEDGE});
		params.put("validSign", BillPoolConstant.VALID_SIGN_1);
		return billPoolDtlImpDao.getAbPoolDtlByParams(params);
	}
	
	/**
	 * 根据票据主信息ID和单位ID获取从表数据
	 * @param vid 票据号
	 * @param corpId 单位id
	 * @param bankAcc 所属账户
	 * @return
	 */
	public BillAbVoucherinfoVice getVoucherinfoViceByVidAndCorpId(String vid,String corpId,String bankAcc){
		return billAbVoucherinfoViceDao.getVoucherinfoViceByVidAndCorpId(vid, corpId, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, bankAcc);
	}
	
	/**
	 * 根据票据号获取票据主表信息
	 * @param origNo 票据号
	 * @return
	 */
	public BillAbVoucherinfo getBillVoucherInfoByOrigNo(String origNo){
		return billVoucherInfoDao.getBillVoucherInfoByOrigNo(origNo,"0","0");
	}
	/**
	 * <b>功能：</b>根据入参获取持有票据明细表数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-26 <br>
	 * @param vid 业务ID
	 * @param vvid 业务ID
	 * @return
	 */
	public BillAbAccrecords queryHoldAccrecords(String vid,String vvid,String reqAcc){
		if(StrUtil.isEmpty(vid)||(StrUtil.isEmpty(vvid)&&StrUtil.isEmpty(reqAcc))){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("vid", vid);
		if(StrUtil.isNotEmpty(vvid)){
			params.put("vvid", vvid);
		}
		if(StrUtil.isNotEmpty(reqAcc)){
			params.put("reqAcc", reqAcc);
		}
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
//		'6001','9003','9010','9012','9018','9019','6019','6212'
		params.put("briefIds", new String[]{AccebillConstant.BRIEF_STATUS_6001
				,AccebillConstant.BRIEF_STATUS_6001
				,AccebillConstant.BRIEF_STATUS_9003
				,AccebillConstant.BRIEF_STATUS_9010
				,AccebillConstant.BRIEF_STATUS_9012
				,AccebillConstant.BRIEF_STATUS_9018
				,AccebillConstant.BRIEF_STATUS_9019
				,AccebillConstant.BRIEF_STATUS_6019
				,AccebillConstant.BRIEF_STATUS_6212});
		return billAccrecordsImpDao.getBillAbAccrecordsByParams(params);
	}
	
	/**
	 * 根据流水id修改质押转托管确认状态
	 * @param busId 流水id
	 * @param poolPledgeStatus 质押转托管确认状态:-1质押转托管申请失败、 0质押转托管确认失败、1质押转托管确认在途
	 * @param reMsg 返回消息
	 * @return
	 */
	public int updatePoolPledgeStatusByBusId(String busId,String poolPledgeStatus,String reMsg){
		if(StrUtil.isEmpty(busId) || StrUtil.isEmpty(poolPledgeStatus)){
			return 0;
		}
		return billAccrecordsImpDao.updatePoolPledgeStatusByBusId(busId, poolPledgeStatus, reMsg);
	}
}
