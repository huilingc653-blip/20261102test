package com.bytter.biz.finacial.closedeal.service;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.closedeal.dao.FcBalanceSheetDao;
import com.bytter.biz.finacial.closedeal.dao.FcMonthLedgerDao;
import com.bytter.biz.finacial.closedeal.entity.FcBalanceSheet;
import com.bytter.biz.finacial.closedeal.vo.FcReportFormatBean;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.biz.payment.api.service.IFcInitAccService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
@Service("fcMonthCloseService")
@Transactional
public class FcMonthCloseService<T, PK extends Serializable> extends BaseService<T, PK> {
	
	@Autowired
	private FcMonthLedgerDao fcMonthLedgerDao;
    @Autowired(required = false)
    private FcBalanceSheetDao fcBalanceSheetDao;
	@DubboReference(check = false)
	private IFcInitAccService iFcInitAccService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@Override
	public BaseDao<T, PK> getEntityDao() {
		return null;
	}
	
	/**
	 * 功能介绍 ： 是否存在上一年资产负债明细。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public boolean existsBalanceSheet(FcMonthCloseDTO closeDTO) {
        Integer lastYear = closeDTO.getCloseYear() - 1;
        List<FcBalanceSheet> list = fcBalanceSheetDao.getBalanceSheet(closeDTO, lastYear, closeDTO.getCloseYear());
		if (null != list && !list.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能介绍 ： 是否存在初始化账户明细。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public boolean existsInitAccs(FcMonthCloseDTO closeDTO){
		// 根据网点和日期获取初始化数据
//		List<FcInitAccDTO> initAccDTOs = fcMonthLedgerDao.queryNetInitAcc(closeDTO);
		com.bytter.biz.payment.api.dto.FcMonthCloseDTO fcMonthCloseDTO = new com.bytter.biz.payment.api.dto.FcMonthCloseDTO();
		BeanUtils.copyProperties(closeDTO,fcMonthCloseDTO);
		List<FcInitAccDTO> initAccDTOs = iFcInitAccService.queryInitAccByNetId(fcMonthCloseDTO.getNetCode());
		boolean accInitFlag = null == initAccDTOs ? false : true;//账户初始化标识
		return accInitFlag;
	}
	
	/**
	 * 功能介绍 : 初始化账户年初余额数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public Map<String, FcInitSubjectDTO> getInitSubjectDTO(FcMonthCloseDTO closeDTO, String curCode) {
		List<FcInitSubjectDTO> list = iFcInitAccService.getInitSubjectDTO(closeDTO.getNetCode());
		return listToMap(list);
	}
	/**
	* <br>
	* <b>功能：</b> 按科目币别转换为Map集合 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/6/2 <br>
	*/
	private Map<String, FcInitSubjectDTO> listToMap(List<FcInitSubjectDTO> list) {
        Map<String, FcInitSubjectDTO> map = new HashMap<String, FcInitSubjectDTO>();
        for (FcInitSubjectDTO subjectDTO : list) {
            String curId = subjectDTO.getCurId();
            String subjectCode = subjectDTO.getSubjectId();
            String key = curId + "_" + subjectCode;
            map.put(key, subjectDTO);
        }
        return map;
    }
    /**
     * 功能介绍 : 报表处理。<br>
     * <b>日期：</b>2017年7月17日<br>
     * @version 1.0
     * @author liyh
     */
    public void generateReportDate(FcMonthCloseDTO closeDTO) {
        Map<String, FcInitSubjectDTO> subjMap = new HashMap<>();
        boolean existBalSheet = existsBalanceSheet(closeDTO);//是否存在上年资产负债数据
        if (existBalSheet) {
            //后面会获取上一年的数据
        } else {
            boolean accInitFlag = existsInitAccs(closeDTO);//账户初始化标识
            if (accInitFlag) {
                //初始化年余额
                subjMap = getInitSubjectDTO(closeDTO, null);
            }
        }
        generateCommonReportDate(closeDTO, subjMap, Boolean.TRUE);
    }
	/**
	* <br>
	* <b>功能：</b> 获取所有科目币别的本年累计发生额 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/6/2 <br>
	*/
    public Map<String, FcInitSubjectDTO> getTotalSubjectBalMap(FcMonthCloseDTO closeDTO) {
        Integer lastYear = closeDTO.getCloseYear();//月结年份
        //获取所有科目币别的本年累计发生额
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, lastYear);
        calendar.add(Calendar.DAY_OF_YEAR, -1);//减1天
        Date lastYearLast = calendar.getTime();
        List<FcInitSubjectDTO> subjectDTOS = getTotalSubjectBal(closeDTO, lastYearLast);
        return listToMap(subjectDTOS);
    }
	
	/**
	 * 功能介绍 : 报表处理。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void generateIncomeReportDate(FcMonthCloseDTO closeDTO) {
		//需要计算年累计发生额
        //获取所有科目币别的本年累计发生额
        Map<String, FcInitSubjectDTO> subjMap = getTotalSubjectBalMap(closeDTO);
		//有效币别循环
        generateCommonReportDate(closeDTO, subjMap, true);
	}
	/**
	* <br>
	* <b>功能：</b> 通用报表处理 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/6/2 <br>
	*/
    public void generateCommonReportDate(FcMonthCloseDTO closeDTO, Map<String, FcInitSubjectDTO> subjMap, boolean initFlag) {
        //有效币别循环
		List<BtCurrencyDTO> curList = iBtCurrencyService.queryValidCur();
//		List<BtCurrencyDTO> curList = fcMonthLedgerDao.queryValidCur();
        for (BtCurrencyDTO currencyDTO : curList) {
            String curCode = currencyDTO.getId();
            //获取产品定义的报表格式
            List<FcReportFormatBean> formatBeans = getNetReportFormat(closeDTO, curCode,
                    FinacialConstant.REPORT_TYPE_1);
            //初始化年余额
			logger.error("---初始化年余额begin--- ，formatBeans ==>【{}】", formatBeans.size());
            for (FcReportFormatBean formatBean : formatBeans) {
                //网点和科目代码
                String subjectCode = formatBean.getSubject();
                if (StringUtils.isEmpty(subjectCode)) {
                    continue;
                }
                String key = curCode + "_" + subjectCode;
                FcInitSubjectDTO subjDTO = subjMap.get(key);
                if (null != subjDTO) {
                    formatBean.setYearBalance(subjDTO.getYearBalance());//年余额
					logger.error("币种："+subjDTO.getCurId() + "--科目："+subjDTO.getSubjectId()+"--余额："+subjDTO.getYearBalance());
                } else {
                    formatBean.setYearBalance(BigDecimal.ZERO);//0余额
                }
            }
			logger.error("---初始化年余额end---");
            Map<String, FcReportFormatBean> formatMap = turn2Map(formatBeans);
            //先计算大于200的过渡项目的值 大于200的过渡项目的值排序为降序Order By Line_No Desc;
            setFormatBeanVal(getBiggerLineNoFormat(formatBeans), formatMap, initFlag);
            //当计算完过滤项目的值时，再从头开始计算小于200的项目的值
            List<FcReportFormatBean> transBeans = getLowerLineNoFormat(formatBeans);
            setFormatBeanVal(transBeans, formatMap, initFlag);
            //取小于200的数据左右循环结果
            processFormatBean(transBeans, closeDTO, curCode,initFlag);
        }
        //未初始化，则更新年初余额
        if (!initFlag) {
            //updateInitYearBalance(closeDTO, lastYear);
        }
    }
	
	/**
	 * 功能介绍 : 查询网点的格式定义数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcReportFormatBean> getNetReportFormat(FcMonthCloseDTO closeDTO, String curCode, String reportType) {
		return null;
	}
	
	/**
	 * 功能介绍 : 更新年初余额。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateInitYearBalance(FcMonthCloseDTO closeDTO, Integer lastYear) {
		
	}
	
	/**
	 * 功能介绍 : 生成业务明细。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processFormatBean(List<FcReportFormatBean> transBeans, FcMonthCloseDTO closeDTO, String curCode,Boolean initFlag) {
		
	}
	
	/**
	 * 功能介绍 : 查询本期期末余额。<br>
	 * <b>日期：</b>2018-07-13<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public BigDecimal getFormatBalance(List<FcReportFormatBean> transBeans, Map<String, FcReportFormatBean> formatMap, FcReportFormatBean formatBean) {
		BigDecimal balance = BigDecimal.ZERO;
		String direction = formatBean.getDirection();// 方向
		String formula = formatBean.getFormula();// 公式定义
		if (!StringUtils.isEmpty(formula)) {
			String[] lineNos = formula.split("[+]");
			for (String lineNo : lineNos) {
				String key = direction + "_" + lineNo;
				FcReportFormatBean tempBean = formatMap.get(key);
				if (null != tempBean) {
					BigDecimal tempBal = tempBean.getBalance();
					String tempFormula = tempBean.getFormula();// 公式定义
					if (!StringUtils.isEmpty(tempFormula)) {
						//递归获取上级数据
						tempBal = getFormatBalance(transBeans, formatMap, tempBean);
					}
					
					if (null == tempBal) {
						tempBal = BigDecimal.ZERO;
					}
					Integer rect = tempBean.getRect();
                    if (null == rect) {
                        rect = Integer.valueOf(1);
                    } else if (Integer.valueOf(0).equals(rect)){
                        rect = Integer.valueOf(-1);
                    }
					balance = balance.add(tempBal.multiply(new BigDecimal(rect)));
				} else {
					
				}
			}
		}
		return balance;
	}
	
	/**
	 * 功能介绍 : 查询年初余额。<br>
	 * <b>日期：</b>2018-07-13<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public BigDecimal getFormatYearBalance(List<FcReportFormatBean> transBeans, Map<String, FcReportFormatBean> formatMap, FcReportFormatBean formatBean) {
		BigDecimal balance = BigDecimal.ZERO;
		String direction = formatBean.getDirection();// 方向
		String formula = formatBean.getFormula();// 公式定义
		if (!StringUtils.isEmpty(formula)) {
			String[] lineNos = formula.split("[+]");
			for (String lineNo : lineNos) {
				String key = direction + "_" + lineNo;
				FcReportFormatBean tempBean = formatMap.get(key);
				if (null != tempBean) {
					BigDecimal tempBal = tempBean.getYearBalance();
					String tempFormula = tempBean.getFormula();// 公式定义
					if (!StringUtils.isEmpty(tempFormula)) {
						//递归获取上级数据
						tempBal = getFormatYearBalance(transBeans, formatMap, tempBean);
					}
					
					if (null == tempBal) {
						tempBal = BigDecimal.ZERO;
					}
					Integer rect = tempBean.getRect();
                    if (null == rect) {
                        rect = Integer.valueOf(1);
                    } else if (Integer.valueOf(0).equals(rect)){
                        rect = Integer.valueOf(-1);
                    }
					balance = balance.add(tempBal.multiply(new BigDecimal(rect)));
				} else {
					
				}
			}
		}
		return balance;
	}
	
	
	/**
	 * 功能介绍 : 根据公式定义计算每行次的值。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void setFormatBeanVal(List<FcReportFormatBean> transBeans, Map<String, FcReportFormatBean> formatMap,
			boolean initFlag) {
		for (FcReportFormatBean formatBean : transBeans) {
			String formula = formatBean.getFormula();// 公式定义
			if (!StringUtils.isEmpty(formula)) {
				formatBean.setBalance(getFormatBalance(transBeans, formatMap, formatBean));
			}
			if (initFlag) {
				// 设置年余额
				if (!StringUtils.isEmpty(formula)) {
					formatBean.setYearBalance(getFormatYearBalance(transBeans, formatMap, formatBean));// 设置年余额
				}
			}
		}
	}
	
	/**
	 * 功能介绍 : 取行号大于等于200的格式定义。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private List<FcReportFormatBean> getBiggerLineNoFormat(List<FcReportFormatBean> formatBeans) {
		List<FcReportFormatBean> transBeans = new ArrayList<FcReportFormatBean>();
		if (null == formatBeans) {
			return transBeans;
		}
		for (FcReportFormatBean formatBean : formatBeans) {
			Integer lineNo = formatBean.getLineNo();
			if (lineNo.intValue() >= 200) {
				transBeans.add(formatBean);
			}
		}
		Collections.sort(transBeans, new Comparator<FcReportFormatBean>() {
			@Override
			public int compare(FcReportFormatBean o1, FcReportFormatBean o2) {
				return o2.getLineNo() - o1.getLineNo();
			}
		});
		return transBeans;
	}
	
	/**
	 * 功能介绍 : 取行号小于200的格式定义。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private List<FcReportFormatBean> getLowerLineNoFormat(List<FcReportFormatBean> formatBeans) {
		List<FcReportFormatBean> transBeans = new ArrayList<FcReportFormatBean>();
		if (null == formatBeans) {
			return transBeans;
		}
		for (FcReportFormatBean formatBean : formatBeans) {
			Integer lineNo = formatBean.getLineNo();
			if (lineNo.intValue() < 200) {
				transBeans.add(formatBean);
			}
		}
		Collections.sort(transBeans, new Comparator<FcReportFormatBean>() {
			@Override
			public int compare(FcReportFormatBean o1, FcReportFormatBean o2) {
				return o1.getLineNo() - o2.getLineNo();
			}
		});
		return transBeans;
	}
	
	/**
	 * 功能介绍 : 根据方向和行号分组。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private Map<String, FcReportFormatBean> turn2Map(List<FcReportFormatBean> formatBeans) {
		Map<String, FcReportFormatBean> map = new HashMap<String, FcReportFormatBean>();
		if (null == formatBeans) {
			return map;
		}
		for (FcReportFormatBean formatBean : formatBeans) {
			String direction = formatBean.getDirection();//方向
			Integer lineNo = formatBean.getLineNo();
			String key = direction + "_" + lineNo;
			map.put(key, formatBean);
		}
		return map;
	}
    /**
     * 功能介绍 : 查询损益类账户的本年累计。<br>
     * <b>日期：</b>2020年6月2日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcInitSubjectDTO> getTotalSubjectBal(FcMonthCloseDTO closeDTO, Date startDate) {
        return null;
    }
}
