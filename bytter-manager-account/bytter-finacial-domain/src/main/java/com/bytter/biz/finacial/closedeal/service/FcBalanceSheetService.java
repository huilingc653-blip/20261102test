package com.bytter.biz.finacial.closedeal.service;
import com.bytter.biz.finacial.closedeal.dao.FcBalanceSheetDao;
import com.bytter.biz.finacial.closedeal.entity.FcBalanceSheet;
import com.bytter.biz.finacial.closedeal.vo.FcReportFormatBean;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: FC_BALANCE_SHEET
 * @author onlineGenerator
 * @date 2017-07-17 18:39:24
 * @version 1.0
 *
 */
@Service("fcBalanceSheetService")
@Transactional
public class FcBalanceSheetService extends FcMonthCloseService<FcBalanceSheet,String> {
	@Autowired
	private FcBalanceSheetDao fcBalanceSheetDao;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcBalanceSheet entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcBalanceSheet entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcBalanceSheet entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcBalanceSheet entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcBalanceSheet, String> getEntityDao() {
		return fcBalanceSheetDao;
	}
	@Override
	public List<FcReportFormatBean> getNetReportFormat(FcMonthCloseDTO closeDTO, String curCode, String reportType) {
		return fcBalanceSheetDao.getNetReportFormat(closeDTO, curCode,
				FinacialConstant.REPORT_TYPE_1);
	}
	@Override
	public void updateInitYearBalance(FcMonthCloseDTO closeDTO, Integer lastYear) {
		fcBalanceSheetDao.updateInitYearBalance(closeDTO, lastYear);
	}
	/**
	 * 功能介绍 : 生成资产负债明细。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Override
	public void processFormatBean(List<FcReportFormatBean> transBeans, FcMonthCloseDTO closeDTO, String curCode,Boolean initFlag) {
		Integer lastYear = closeDTO.getCloseYear() - 1;
//		年初数取上一年12月期末数
		List<FcBalanceSheet> sheets = fcBalanceSheetDao.getBalanceSheet(closeDTO,lastYear,closeDTO.getCloseYear());
		List<FcReportFormatBean> lBeans = new ArrayList<FcReportFormatBean>();
		List<FcReportFormatBean> rBeans = new ArrayList<FcReportFormatBean>();
		for (FcReportFormatBean formatBean : transBeans) {
			if (FinacialConstant.REPORT_DIRECTION_L.equals(formatBean.getDirection())) {
				lBeans.add(formatBean);
			} else {
				rBeans.add(formatBean);
			}
		}
		Map<Integer, FcReportFormatBean> rFormatMap = new HashMap<Integer, FcReportFormatBean>();
		for (FcReportFormatBean bean : lBeans) {
			rFormatMap.put(bean.getOrderNo(), bean);
		}
		List<FcBalanceSheet> fcBalanceSheets = new ArrayList<>();
		if(!lBeans.isEmpty() && !rBeans.isEmpty()){//资产负债都不为空
			for(FcReportFormatBean lbean:lBeans){//循环放入左边数据
				FcBalanceSheet sheet = new FcBalanceSheet();
				sheet.setYearMonth2(closeDTO.getYearMonth2());
				sheet.setNetCode(lbean.getNetCode());
				sheet.setCurCode(curCode);
				//资产类项目数据
				if(initFlag) {
					if (EmptyUtils.isNotEmpty(sheets)) {
                        for (FcBalanceSheet s : sheets) {
                            if (lbean.getItem().equals(s.getAItem()) && sheet.getCurCode().equals(s.getCurCode())) {
                                sheet.setAYearBalance(s.getAYearBalance());
                                break;
                            }
                        }
                    } else {
                        sheet.setAYearBalance(lbean.getYearBalance());
                    }
				}else {
					sheet.setAYearBalance(lbean.getYearBalance());
				}
				sheet.setAItem(lbean.getItem());
				sheet.setAOrderNo(lbean.getOrderNo());
				sheet.setAMonthBalance(lbean.getBalance());
				fcBalanceSheets.add(sheet);
			}
//			循环放入右边数据
			for (FcReportFormatBean rbean : rBeans) {
				FcBalanceSheet sheet = null;
				for (FcBalanceSheet fcBalanceSheet : fcBalanceSheets) {
//					循环通过单号匹配
					if (rbean.getOrderNo() == fcBalanceSheet.getAOrderNo()) {
						sheet = fcBalanceSheet;
					}
				}
				if (sheet == null ) {
					sheet = new FcBalanceSheet();
                    sheet.setYearMonth2(closeDTO.getYearMonth2());
                    sheet.setNetCode(rbean.getNetCode());
                    sheet.setCurCode(curCode);
					fcBalanceSheets.add(sheet);
				}
				//负债类项目数据
				if (initFlag) {
                    if (EmptyUtils.isNotEmpty(sheets)) {
                        for (FcBalanceSheet s : sheets) {
                            if (rbean.getItem().equals(s.getLItem()) && null != sheet && sheet.getCurCode().equals(s.getCurCode())) {
                                sheet.setLYearBalance(s.getLYearBalance());
                                break;
                            }
                        }
                    } else {
					    sheet.setLYearBalance(rbean.getYearBalance());
                    }
				} else {
					sheet.setLYearBalance(rbean.getYearBalance());
				}
				sheet.setLItem(rbean.getItem());
				sheet.setLOrderNo(rbean.getOrderNo());
				sheet.setLMonthBalance(rbean.getBalance());
			}
		}else if(!lBeans.isEmpty() && rBeans.isEmpty()){//资产类不为空，负债类为空
			for(FcReportFormatBean lbean:lBeans){
				FcBalanceSheet sheet = new FcBalanceSheet();
				sheet.setYearMonth2(closeDTO.getYearMonth2());
				sheet.setNetCode(lbean.getNetCode());
				sheet.setCurCode(curCode);
				//资产类项目数据
				if(!initFlag) {
					for (FcBalanceSheet s : sheets) {
						if (lbean.getItem().equals(s.getAItem()) && sheet.getCurCode().equals(s.getCurCode())) {
							sheet.setAYearBalance(s.getAYearBalance());
							break;
						}
					}
				}else {
					sheet.setAYearBalance(lbean.getYearBalance());
				}
				sheet.setAItem(lbean.getItem());
				sheet.setAOrderNo(lbean.getOrderNo());
				sheet.setAMonthBalance(lbean.getBalance());
				fcBalanceSheets.add(sheet);
			}
		}else if(lBeans.isEmpty() && !rBeans.isEmpty()){//资产类为空，负载类不为空
			for(FcReportFormatBean rbean:rBeans){
				FcBalanceSheet sheet = new FcBalanceSheet();
				sheet.setYearMonth2(closeDTO.getYearMonth2());
				sheet.setNetCode(rbean.getNetCode());
				sheet.setCurCode(curCode);
				//负债类项目数据
				if(!initFlag) {
					for (FcBalanceSheet s : sheets) {
						if (rbean.getItem().equals(s.getLItem()) && sheet.getCurCode().equals(s.getCurCode())) {
							sheet.setLYearBalance(s.getLYearBalance());
							break;
						}
					}
				}else {
					sheet.setLYearBalance(rbean.getYearBalance());
				}
				sheet.setLItem(rbean.getItem());
				sheet.setLOrderNo(rbean.getOrderNo());
				sheet.setLMonthBalance(rbean.getBalance());
				fcBalanceSheets.add(sheet);
			}
		}
		batchSave(fcBalanceSheets);
	}
	/**
	 * 功能介绍 : 转移资产负债数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void reverseBalanceSheet(FcMonthCloseDTO closeDTO) {
		fcBalanceSheetDao.deletebalanceSheet(closeDTO);
	}
}
