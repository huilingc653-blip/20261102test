package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.bill.domain.billbank.entity.BillSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>底层通用业务操作service<br>
 * <b>日期：</b> 2020-7-14 <br>
 */
@Service("billCommonBaseBillBankService")
@Transactional
public class BillCommonBaseService {
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillSignDao billSignDao;
	/**
	 * <b>功能：</b>自动签收后处理来自外部的票据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-12-29 <br>
	 * @param  voucher 票据主信息
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void autoSignManage(VoucherInfoVo voucher) {
		billBankDao.autoSignManage(voucher);
	}
	
	/**
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void saveBillSign(BillSignVo billSign) {
		BillSign info=new BillSign();
		BeanUtil.copyProperties(billSign, info);
		info.setRmk(billSign.getRmrkByPropsr());
		info.setRcrsokmoney(billSign.getRechMoney());
		info.setPayerbankName(billSign.getPayerBankName());
		billSignDao.save(info);
	}
	
	/**
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void save(BillSign billSign) {
		billSignDao.save(billSign);
	}
}
