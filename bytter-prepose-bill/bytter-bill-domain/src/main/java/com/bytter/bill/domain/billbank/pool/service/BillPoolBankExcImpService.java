package com.bytter.bill.domain.billbank.pool.service;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolBankExcImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolBankExc;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>票据池服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Service("billPoolBankExcImpService")
@Transactional
public class BillPoolBankExcImpService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolBankExcImpDao billPoolBankExcImpDao;
	
	/**
	 * <b>功能：</b>修改业务类型<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-21 <br>
	 * @param baseVo 银行接口代码
	 * @return 票据池信息
	 */
	public void updateBillPoolBankExc(PoolBaseVO baseVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("baseVo="+baseVo.toString());
		}
		AbPoolBankExc exc=billPoolBankExcImpDao.get(baseVo.getBusId(),true);
		if(exc==null|| StrUtil.isEmpty(exc.getId())){
			exc=new AbPoolBankExc();
			exc.setId(baseVo.getBusId());
			exc.setApplyId(baseVo.getApplyId());
			exc.setBankId(baseVo.getBankId());
			exc.setReCode(baseVo.getReCode());
			exc.setReMsg(baseVo.getReMsg());
			billPoolBankExcImpDao.save(exc);
			billPoolBankExcImpDao.flush();
		}else{
			boolean isUpdate=false;
			if(StrUtil.isNotEmpty(baseVo.getApplyId())
					&&!baseVo.getApplyId().equals(exc.getApplyId())){
				exc.setApplyId(baseVo.getApplyId());
				isUpdate=true;
			}
			if(StrUtil.isNotEmpty(baseVo.getBankId())
					&&!baseVo.getBankId().equals(exc.getBankId())){
				exc.setBankId(baseVo.getBankId());
				isUpdate=true;
			}
			if(StrUtil.isNotEmpty(baseVo.getReCode())
					&&!baseVo.getReCode().equals(exc.getReCode())){
				exc.setReCode(baseVo.getReCode());
				isUpdate=true;
			}
			if(StrUtil.isNotEmpty(baseVo.getReMsg())
					&&!baseVo.getReMsg().equals(exc.getReMsg())){
				exc.setReMsg(baseVo.getReMsg());
				isUpdate=true;
			}
			if(isUpdate){
				billPoolBankExcImpDao.update(exc);
			}
		}
	}
}
