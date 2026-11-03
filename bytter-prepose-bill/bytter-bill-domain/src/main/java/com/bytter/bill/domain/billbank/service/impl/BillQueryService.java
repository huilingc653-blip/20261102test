package com.bytter.bill.domain.billbank.service.impl;
import cn.hutool.core.bean.BeanUtil;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.service.IBillQueryService;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <br>
 * <b>功能：</b>票据查询服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b>  2018-01-05 <br>
 */
@Service("billQueryService")
@Transactional
public class BillQueryService implements IBillQueryService {
	@Autowired
	private BillBankDao billBankDao;

	public static String newDraftFlg;

	static{
		initNewDraftFlg();
	}

	static void initNewDraftFlg(){
		if(newDraftFlg == null){
			String absFilePath = "/config/billBankImp.properties";
			ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
			assert defaultClassLoader != null;
			URL resource = defaultClassLoader.getResource(absFilePath);
			Properties properties = new Properties();
			try {
				properties.load(resource.openStream());
				newDraftFlg = properties.getProperty("newDraftFlg");
				BillPoolConstant.IS_RUN_POOL = "1".equals(properties.getProperty("runPoolSign"));
			} catch (Exception e) {
			}
		}
		System.out.println("===========newDraftFlg:"+newDraftFlg);
		System.out.println("===========runPoolSign:"+ BillPoolConstant.IS_RUN_POOL);
	}

	/**
	 * 票据池运行开关：从Spring配置(Nacos)读取runPoolSign，替代原有classpath下的billBankImp.properties
	 * <br>
	 * 说明：static块读取的config/billBankImp.properties在classpath中不存在时会吞掉异常，
	 * 导致IS_RUN_POOL一直为false；此处通过Nacos配置runPoolSign=1来开启票据池。
	 */
	@Value("${runPoolSign:false}")
	private String runPoolSign;

	/**
	 * 根据Nacos配置初始化票据池开关
	 */
	@PostConstruct
	public void initRunPoolSign() {
		BillPoolConstant.IS_RUN_POOL = "1".equals(runPoolSign);
		System.out.println("===========Nacos runPoolSign:" + runPoolSign);
	}

	 
	/**
	 * <b>功能：</b>根据银行代码查询已完成，不是作废的，最大流水id的数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-01-04 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	public List<VoucherInfoVo> queryBillByBifCode(String bifCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBillByBifCode(bifCode);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>查询银行账号持有票据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	public List<VAccListVo> queryVAccListVoHoldBill(String bifCode) {
		List<VAccListVo> vAccListVoList=billBankDao.queryVAccListVo(bifCode);
		List<VAccListVo> accHoldBillList=new ArrayList<VAccListVo>();
		String[] briefIdS={
//				EDraftConstant.BUSINESS_001,
				EDraftConstant.BUSINESS_002,
				EDraftConstant.BUSINESS_9002,
				EDraftConstant.BUSINESS_003,
				EDraftConstant.BUSINESS_9003,
				EDraftConstant.BUSINESS_010,
				EDraftConstant.BUSINESS_012,
				EDraftConstant.BUSINESS_018,
				EDraftConstant.BUSINESS_019,
				EDraftConstant.BUSINESS_024
		};
		for(VAccListVo info:vAccListVoList){
			for(String briefId:briefIdS){
				VAccListVo accHoldBill=new VAccListVo();
				accHoldBill.setBifCode(info.getBifCode());
				accHoldBill.setBankAcc(info.getBankAcc());
				accHoldBill.setBriefId(briefId);
				accHoldBill.setBillType(info.getBillType());
				accHoldBill.setBankCode(info.getBankCode());
				accHoldBill.setBankName(info.getBankName());
				if(StringUtil.isBlank(newDraftFlg)){
					accHoldBill.setNewDraftFlg("0");
					VAccListVo accHoldBill2=new VAccListVo();
					BeanUtil.copyProperties(accHoldBill,accHoldBill2);
					accHoldBill2.setNewDraftFlg("1");
					accHoldBillList.add(accHoldBill2);
				}else {
					accHoldBill.setNewDraftFlg(newDraftFlg);
				}
				accHoldBillList.add(accHoldBill);
			}
		}
		return accHoldBillList;
	}
	/**
	 * <b>功能：</b>查询银行账号持有票据(票据池)<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-29 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	public List<VAccListVo> queryVAccListVoHoldBillPool(String bifCode) {
		List<VAccListVo> vAccListVoList=billBankDao.queryVAccListVo(bifCode);
		List<VAccListVo> accHoldBillList=new ArrayList<VAccListVo>();
		for(VAccListVo info:vAccListVoList){
			VAccListVo accHoldBill=new VAccListVo();
			accHoldBill.setBifCode(info.getBifCode());
			accHoldBill.setBankAcc(info.getBankAcc());
			accHoldBill.setBillType(info.getBillType());
			accHoldBill.setBankName(info.getBankName());
			accHoldBill.setBankCode(info.getBankCode());
			if(StringUtil.isBlank(newDraftFlg)){
				accHoldBill.setNewDraftFlg("0");
				VAccListVo accHoldBill2=new VAccListVo();
				BeanUtil.copyProperties(accHoldBill,accHoldBill2);
				accHoldBill2.setNewDraftFlg("1");
				accHoldBillList.add(accHoldBill2);
			}else {
				accHoldBill.setNewDraftFlg(newDraftFlg);
			}
			accHoldBillList.add(accHoldBill);
		}
		return accHoldBillList;
	}
	/**
	 * <b>功能：</b>查询待签收业务类型<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2019-6-4 <br>
	 * @param bifCode 银行接口代码
	 * @return 账户和对应查询业务
	 */
	public List<VAccListVo> queryVAccListVoSignBill(String bifCode) {
		List<VAccListVo> vAccListVoList=billBankDao.queryVAccListVo(bifCode);
		List<VAccListVo> accSignBillList=new ArrayList<VAccListVo>();
		String[] briefIdS={
				EDraftConstant.BUSINESS_002,
				EDraftConstant.BUSINESS_003,
				EDraftConstant.BUSINESS_010,
				EDraftConstant.BUSINESS_012,
				EDraftConstant.BUSINESS_018,
				EDraftConstant.BUSINESS_019,
				EDraftConstant.BUSINESS_020,
				EDraftConstant.BUSINESS_021,
				EDraftConstant.BUSINESS_024
		};
		for(VAccListVo info:vAccListVoList){
			for(String briefId:briefIdS){
				VAccListVo accSignBill=new VAccListVo();
				accSignBill.setBifCode(info.getBifCode());
				accSignBill.setBankAcc(info.getBankAcc());
				accSignBill.setBriefId(briefId);
				accSignBill.setBillType(info.getBillType());
				accSignBill.setBankName(info.getBankName());
				accSignBill.setBankCode(info.getBankCode());
				if(StringUtil.isBlank(newDraftFlg)){
					accSignBill.setNewDraftFlg("0");
					VAccListVo accHoldBill2=new VAccListVo();
					BeanUtil.copyProperties(accSignBill,accHoldBill2);
					accHoldBill2.setNewDraftFlg("1");
					accSignBillList.add(accHoldBill2);
				}else {
					accSignBill.setNewDraftFlg(newDraftFlg);
				}
				accSignBillList.add(accSignBill);
			}
		}
		return accSignBillList;
	}
}
