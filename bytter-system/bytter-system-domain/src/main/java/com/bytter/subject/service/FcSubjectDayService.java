package com.bytter.subject.service;

import com.bytter.basic.service.BtCurrencyService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.subject.dao.FcSubjectDayDao;
import com.bytter.subject.entity.FcSubjectDay;
import com.bytter.subject.vo.FcSubjectDayVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.User;
import com.bytter.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: Service
* @Description: 科目日结单表
* @author onlineGenerator
* @date 2017-06-27 16:27:23
* @version 1.0
* TODO	
*/
@Service("fcSubjectDayService")
@Transactional
public class FcSubjectDayService extends BaseService<FcSubjectDay,String> {
	@Autowired
	private FcSubjectDayDao fcSubjectDayDao;
	@Autowired
	private UserService userService;
	@Autowired
	private ICorpService corpServiceI;
	@Autowired
	private BtCurrencyService btCurrencyService;
	

	public BaseDao<FcSubjectDay, String> getEntityDao() {
		return fcSubjectDayDao;
	}
	
	/***
	 *获取页面初始条件 
	 */
	public FcSubjectDayVo getInit(ICurrentUser user){
		FcSubjectDayVo fd = new FcSubjectDayVo();
		User currentLoginUser = userService.getByLoginName(user.getLoginName());
		Corp currentCorp = currentLoginUser.getCorp();
		fd.setNetId(currentCorp.getId());
		fd.setOptDate(DateUtils.getDate()); //TODO 应该设置为网点最后一次日结日期
		return fd;
	}
	
	/**
	 * 科目日结单查询
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput findSubjectDayGroupBySubject( DataTablesInput dataTablesInput, String resource, String userId) {
		List<String> corpAuth = corpServiceI.getUserCorpIds(resource, userId);
		return fcSubjectDayDao.findSubjectDayGroupBySubject(dataTablesInput,corpAuth);
	}
	
	/**
	 * 科目汇总表
	 */
	public Map<String, Object> getFcSubjectDaySum(String curr, FcSubjectDayVo fsd, String resource, String userId) {
		List<String> corpAuth = corpServiceI.getUserCorpIds(resource, userId);
		int currentPage;
		if(EmptyUtils.isEmpty(curr)){
			currentPage = 1;
		}else{
			currentPage = Integer.parseInt(curr);
		}
		List<Map<String,Object>> datas = fcSubjectDayDao.getSubjectSumHead(fsd,corpAuth);
		Map<String, Object> result = new HashMap<String, Object>();
		if (EmptyUtils.isEmptyList(datas)) {
            return result;
        }
		Map<String,Object> pageInfo = datas.get(currentPage - 1);
		result = fcSubjectDayDao.findSubjectDay(pageInfo , currentPage,corpAuth);
		result.put("total", datas.size());
		/*if(datas.size() / 10 >1){
			result.put("count", datas.size()/10 + 1);
		}*/
		result.put("curr", currentPage);
		return result;
	}
	
	/**
	 * 功能介绍 : 保存科目日结单数据。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void saveSubjectDayBeans(List<FcSubjectDayBeanDTO> beans) {
		List<FcSubjectDay> list = new ArrayList<FcSubjectDay>();
		if (null != beans) {
			list = BeanUtils.copyListProperties(beans, FcSubjectDay.class);
			// // 冗余单位名称和币种名称，目前都在system模块，不需要再冗余了
			// List<String> netIdList = list.stream().map(FcSubjectDay::getNetId).distinct().collect(Collectors.toList());
			// List<String> curIdList = list.stream().map(FcSubjectDay::getCurId).distinct().collect(Collectors.toList());
			// List<BtCurrencyVO> currencyList = btCurrencyService.listByIds(curIdList);
			// Map<String, CorpDTO> corpMap = corpServiceI.listCorpDtoByCorpIds(netIdList);
			// Map<String, BtCurrencyVO> currencyMap = currencyList.stream().collect(Collectors.toMap(BtCurrencyVO::getId, Function.identity()));
			// for (FcSubjectDay subjectDay : list){
			// 	Optional.ofNullable(subjectDay.getNetId())
			// 			.flatMap(netId -> Optional.ofNullable(corpMap.get(netId)))
			// 			.ifPresent(corp -> subjectDay.setNetName(corp.getName()));
			// 	Optional.ofNullable(subjectDay.getCurId())
			// 			.flatMap(curId -> Optional.ofNullable(currencyMap.get(curId)))
			// 			.ifPresent(currency -> subjectDay.setNetName(currency.getCurName()));
			// }
			batchSave(list);
		}
	}
	
	/**
	 * 功能介绍 : 查询月结区间内的科目日结单。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<FcSubjectDayBeanDTO> querySetMonthSubDay(FcMonthCloseDTO closeDTO) {
		return fcSubjectDayDao.querySetMonthSubDay(closeDTO);
	}

	public boolean deleteSubjectDay(FcDayCloseDTO fcDayCloseDTO) {
		return fcSubjectDayDao.deleteSubjectDay(fcDayCloseDTO);
	}

    public boolean isIncomBlanceZero(FcMonthCloseDTO closeDTO) {
		return fcSubjectDayDao.isIncomBlanceZero(closeDTO);
    }
}