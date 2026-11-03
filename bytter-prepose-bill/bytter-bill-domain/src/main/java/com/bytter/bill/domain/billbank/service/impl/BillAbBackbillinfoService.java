package com.bytter.bill.domain.billbank.service.impl;
import java.util.ArrayList;
import java.util.List;
import com.bytter.bill.domain.billbank.dao.BillAbBackbillinfoDao;
import com.bytter.bill.domain.billbank.entity.BillAbBackbillinfo;
import com.bytter.bill.domain.billbank.service.IBillAbBackbillinfoService;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.vo.BillAbBackbillinfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 票据背面信息
* @author onlineGenerator
* @date 2017-06-21 15:36:23
* @version 1.0
*
*/
@Service("billAbBackbillinfoService")
@Transactional
public class BillAbBackbillinfoService implements IBillAbBackbillinfoService {
	@Autowired
	private BillAbBackbillinfoDao abBackbillinfoDao;
	/**
	 * <b>方法描述：</b>批量保存背面信息<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public void batchSave(List<BillAbBackbillinfoVo> billAbBackbillinfoList){
		List<BillAbBackbillinfo> addList=new ArrayList<BillAbBackbillinfo>();
		
		//先根据票据号删除背面信息再保存
		for(BillAbBackbillinfoVo billAbBackbillinfo:billAbBackbillinfoList){
			abBackbillinfoDao.deleteByOrigNoAndRngNo(billAbBackbillinfo.getOrigNo(),billAbBackbillinfo.getSbbllRngStrtSn(),billAbBackbillinfo.getSbbllRngEndSn());
			BillAbBackbillinfo info=new BillAbBackbillinfo();
			BeanUtil.copyProperties(billAbBackbillinfo, info);
			addList.add(info);
		}
		//abBackbillinfoDao.batchSave(billAbBackbillinfoList);
		abBackbillinfoDao.batchSave(addList);
	}
	
	
}
