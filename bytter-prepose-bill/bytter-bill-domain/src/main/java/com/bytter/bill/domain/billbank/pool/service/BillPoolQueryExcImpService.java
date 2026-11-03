package com.bytter.bill.domain.billbank.pool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolQueryExcImpDao;
import com.bytter.bill.domain.billbank.pool.entity.AbPoolQueryExc;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b>票据池查询指令服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-11-18 <br>
 */
@Service("billPoolQueryExcImpService")
@Transactional
public class BillPoolQueryExcImpService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolQueryExcImpDao billPoolQueryExcImpDao;
	/**
	 * 获取默认第一个的顶级单位
	 * @return
	 */
	public PoolBaseVO getTopLevelCorp(){
		return billPoolQueryExcImpDao.getTopLevelCorp();
	}
	
	/**
	 * <b>功能：</b>根据池单位ID和池id和查询指令类型新增指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-21 <br>
	 * @param bpId 池id
	 * @param corpId 单位ID
	 * @param queryType 查询指令类型：票据池额度01\出入池登记簿02\在池登记簿03\保证金交易明细04
	 */
	public void saveBillPoolQueryExc(String bpId,String corpId,String queryType){
		Date beginDate=null;
		Date endDate=null;
		if(!BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT.equals(queryType)){
			beginDate= DateUtils.addDay(DateUtils.getDateYMD(),-1);
			endDate=DateUtils.getDateYMD();
		}
		saveBillPoolQueryExc(bpId, corpId, queryType, beginDate, endDate);
	}
	
	/**
	 * <b>功能：</b>根据池单位ID和池id和查询指令类型新增指令<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-21 <br>
	 * @param bpId 池id
	 * @param corpId 单位ID
	 * @param queryType 查询指令类型：票据池额度01\出入池登记簿02\在池登记簿03\保证金交易明细04
	 * @param beginDate 
	 * @param endDate 
	 */
	public void saveBillPoolQueryExc(String bpId,String corpId,String queryType,Date beginDate,Date endDate){
		if (logger.isDebugEnabled()) {
			logger.debug("bpId="+bpId+" ,corpId="+corpId+" ,queryType="+queryType+" ,beginDate="+beginDate+" ,endDate="+endDate);
		}
		if(StringUtil.isEmpty(bpId)
				||StringUtil.isEmpty(corpId)
				||StringUtil.isEmpty(queryType)){
			return;
		}
		AbPoolQueryExc poolQueryExc=new AbPoolQueryExc();
		poolQueryExc.setBpId(bpId);
		poolQueryExc.setCorpId(corpId);
		poolQueryExc.setExcType(queryType);
		poolQueryExc.setStatus(AccebillConstant.AB_BILL_OPT_STATUS16);
		poolQueryExc.setCreateTime(new Date());
		poolQueryExc.setOperDateStart(beginDate);
		poolQueryExc.setOperDateEnd(endDate);
		billPoolQueryExcImpDao.saveOrUpdate(poolQueryExc);
	}
}
