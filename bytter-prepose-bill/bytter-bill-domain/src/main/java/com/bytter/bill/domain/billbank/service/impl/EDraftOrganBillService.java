package com.bytter.bill.domain.billbank.service.impl;
import java.util.List;
import com.bytter.bill.domain.billbank.dao.EDraftOrganBillDao;
import com.bytter.bill.domain.billbank.entity.EDraftOrganBill;
import com.bytter.bill.domain.billbank.service.IEDraftOrganBillService;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.vo.EDraftOrganBillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("eDraftOrganBillService")
@Transactional
public class EDraftOrganBillService  implements IEDraftOrganBillService {
	
	@Autowired
	private EDraftOrganBillDao eDraftOrganBillDao;
	
	/**
	 * 
	 * <b>方法描述：</b>根据机构类型或机构名称查询机构信息<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public List<EDraftOrganBillVo> queryEDraftOrganByParam(String organType, String organName, Long organId) {
//		return eDraftOrganBillDao.queryEDraftOrganByParam(organType, organName, organId);
		List<EDraftOrganBill> infoList= eDraftOrganBillDao.queryEDraftOrganByParam(organType, organName, organId);
		List<EDraftOrganBillVo> reList= BeanUtil.copyListProperties(infoList, EDraftOrganBillVo.class);
		return reList;
	}
	/**
	 *
	 * <b>方法描述：</b>根据机构名称查询机构号<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2018-04-24 <br>
	 */
	public String queryEDraftOrganIdByOrganName(String organName) {
		List<EDraftOrganBill> list=eDraftOrganBillDao.queryEDraftOrganByParam(null, organName, null);
		if(list!=null&&list.size()>0){
			return String.valueOf(list.get(0).getOrganId());
		}
		return null;
	}
	/**
	 *
	 * <b>方法描述：</b>根据机构类型删除数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public void deleteByType(String organType){
		eDraftOrganBillDao.deleteByType(organType);
	}
	
	public void addAll(List<EDraftOrganBillVo> eDraftOrganBillList) {
		List<EDraftOrganBill> infoList=BeanUtil.copyListProperties(eDraftOrganBillList, EDraftOrganBill.class);
		eDraftOrganBillDao.batchSave(infoList);
	}
	
	/**
	 *
	 * <b>方法描述：</b>清空数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public void deleteAll() {
		eDraftOrganBillDao.deleteAll();
	}
}
