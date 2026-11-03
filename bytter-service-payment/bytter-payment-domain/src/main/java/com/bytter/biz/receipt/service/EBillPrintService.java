package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.receipt.dao.EBillPrintDao;
import com.bytter.biz.receipt.entity.EBillPrint;
import com.bytter.biz.receipt.entity.EReceiptType;
import com.bytter.biz.receipt.vo.EReceiptTypeVO;
import com.bytter.biz.receipt.vo.EleBillQueryVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.constant.AccountGrantConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
* @Title: Service
* @Description: 电子回单打印
* @author onlineGenerator
* @date 2017-08-23 14:39:27
* @version 1.0
*
*/
@Service("eBillPrintService")
@Transactional
public class EBillPrintService extends BaseService<EBillPrint,String> {
	@Autowired
	private EBillPrintDao eBillPrintDao;
	@Autowired
	private EReceiptTypeService eReceiptTypeService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Override
	public BaseDao<EBillPrint, String> getEntityDao() {
		return eBillPrintDao;
	}
	/**
	 * 根据状态/单位获取不同电子回单类型 数量
	 * @param status
	 * @param corpIds
	 * @return
	 */
	public List<EReceiptTypeVO> listEleBillNum(String status, String corpIds, ICurrentUser user){
        List<EReceiptTypeVO> listVOTemp = new ArrayList<>();
		//取有效的回单类型
		List<EReceiptType> listType = eReceiptTypeService.listReceiptType(status,null);
        if(EmptyUtils.isEmpty(listType)){
            return listVOTemp;
        }
		List<String>  listTableName = new ArrayList<>();
		for (EReceiptType type : listType) {
			String tableName = ReceiptConstant.BILL_MAP.get(type.getBillType());
			if(StringUtil.isNotEmpty(tableName)&& !listTableName.contains(tableName)) {
				listTableName.add(tableName);
			}
		}
		List<EReceiptTypeVO> listVO = BeanUtils.copyListProperties(listType, EReceiptTypeVO.class);
		List<String> accountGrantList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, null,
				AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
		//取有效的回单类型 的 条目数
		List<EleBillQueryVO> listCount = eBillPrintDao.listEleBillNum("1",listTableName,corpIds,accountGrantList);
		//设置总条数
		int allCount = 0;
		for (EReceiptTypeVO vo : listVO) {
			boolean flag = false;
			for (EleBillQueryVO eleBillQueryVO : listCount) {
				if(vo.getBillType().equals(eleBillQueryVO.getBillType())) {
					vo.setBillText(vo.getBillName()+"(有效回单"+eleBillQueryVO.getCount()+")");
					allCount += eleBillQueryVO.getCount();
					flag = true;
					break;
				}
			}
			if(!flag){
				vo.setBillText(vo.getBillName()+"(有效回单0)");
			}
		}
		EReceiptTypeVO all  = new EReceiptTypeVO();
		all.setBillText("全部（有效回单"+allCount+"）");
		all.setBillType("all");

		listVOTemp.add(all);
		listVOTemp.addAll(listVO);
		return listVOTemp;
	}
	/**
	 * 创建电子回单打印信息
	 * @param billNo 回单编号
	 * @param corpId 单位id
	 * @param prints 打印次数
	 */
	public void saveEBillMessage(String billNo,String corpId,Integer prints, ICurrentUser user){
		//对账单存在多条打印记录
		EBillPrint entity = eBillPrintDao.getEntityByParams(corpId,billNo);
		if(entity == null) {
			entity = new EBillPrint();
		}
		entity.setBillNo(billNo);
		entity.setCorpId(corpId);
		entity.setPrints(prints);
		entity.setCreateDate(new Date());
		entity.setCreateBy(user.getLoginName());
		entity.setCreateName(user.getUsername());
		eBillPrintDao.saveOrUpdate(entity);
	}
	/**
	 * 更新打印次数
	 * @param corpId
	 * @param billNo
	 */
	public void prints(String corpId, String billNo, ICurrentUser user){
		EBillPrint eBillPrint = eBillPrintDao.getEntityByParams(corpId,billNo);
		if(eBillPrint !=null){
			eBillPrint.setPrints(eBillPrint.getPrints()+1);
			eBillPrint.setUpdateBy(user.getLoginName());
			eBillPrint.setUpdateName(user.getUsername());
			eBillPrint.setUpdateDate(DateUtils.getDate());
			eBillPrintDao.saveOrUpdate(eBillPrint);
		}
	}
}
