package com.bytter.biz.inneraccount.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.dao.BisAccGatherPolicyDao;
import com.bytter.biz.inneraccount.entity.BisAccGatherPolicy;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.vo.BisAccGatherPolicyTreeVO;
import com.bytter.biz.inneraccount.vo.BisAccGatherPolicyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccCurService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号关系策略维护表
 * @date 2017-06-20 16:44:38
 */
@Service("bisAccGatherPolicyService")
@Transactional
public class BisAccGatherPolicyService extends BaseService<BisAccGatherPolicy, String> {
	@Autowired
	private BisAccGatherPolicyDao bisAccGatherPolicyDao;
	@Autowired
	private BisGatherPolicyService bisGatherPolicyService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBtBankAccCurService iBtBankAccCurService;

	@DubboReference(check = false)
	private IBtCurrencyService currencyService;

	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BisAccGatherPolicy entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(BisAccGatherPolicy entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BisAccGatherPolicy entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BisAccGatherPolicy entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BisAccGatherPolicy, String> getEntityDao() {
		return bisAccGatherPolicyDao;
	}
	/**
	 * 获取树形表格
	 *
	 * @return
	 * @throws Exception
	 */
	public List<BisAccGatherPolicyTreeVO> listTreeData(BisGatherPolicy bisGatherPolicy) throws Exception {
		List<BisAccGatherPolicyTreeVO> treeList = new ArrayList<BisAccGatherPolicyTreeVO>();
		//获取顶级数据，调拨策略类型
		List<BisAccGatherPolicyTreeVO> topList = this.getTopList(bisGatherPolicy.getGatherType());
		for (BisAccGatherPolicyTreeVO top : topList) {
			treeList.add(top);
			//获取第二级数据，调拨策略
			List<BisAccGatherPolicyTreeVO> secondList = this.getSecondList(top.getId(), bisGatherPolicy.getGatherPolicyCode(), top.getTreeId());
			for (BisAccGatherPolicyTreeVO second : secondList) {
				treeList.add(second);
				//获取第三级数据，父账号
				List<BisAccGatherPolicyTreeVO> prentAccList = this.getPrentAccList(second.getId(), second.getTreeId(), second.getCurId());
				for (BisAccGatherPolicyTreeVO prentAcc : prentAccList) {
					treeList.add(prentAcc);
					//获取第四级数据，子账号
					List<BisAccGatherPolicyTreeVO> childAccList = this.getChildAccList(second.getId(), prentAcc.getId(), prentAcc.getTreeId(), second.getCurId());
					treeList.addAll(childAccList);
				}
			}
		}
		List<String> curIds = treeList.stream().map(BisAccGatherPolicyTreeVO::getCurId).collect(Collectors.toList());
		Map<String, String> curDbs = currencyService.getCurrencyNameByIds(curIds);
		treeList.stream().forEach(vo -> {
			vo.setCurName(curDbs.get(vo.getCurId()));
		});
		return treeList;
	}
	/**
	 * 获取树形表格-增加父账号筛选
	 *
	 * @return
	 * @throws Exception
	 */
	public List<BisAccGatherPolicyTreeVO> listTreeData(BisGatherPolicy bisGatherPolicy,String parentAccName) throws Exception {
		List<BisAccGatherPolicyTreeVO> treeList = new ArrayList<BisAccGatherPolicyTreeVO>();
		//获取顶级数据，调拨策略类型
		List<BisAccGatherPolicyTreeVO> topList = this.getTopList(bisGatherPolicy.getGatherType());
		for (BisAccGatherPolicyTreeVO top : topList) {
			treeList.add(top);
			//获取第二级数据，调拨策略
			List<BisAccGatherPolicyTreeVO> secondList = this.getSecondList(top.getId(), bisGatherPolicy.getGatherPolicyCode(), top.getTreeId());
			for (BisAccGatherPolicyTreeVO second : secondList) {
				treeList.add(second);
				//获取第三级数据，父账号
				List<BisAccGatherPolicyTreeVO> prentAccList = this.getPrentAccList(second.getId(), second.getTreeId(), second.getCurId());
				for (BisAccGatherPolicyTreeVO prentAcc : prentAccList) {
					//筛选父账号
					if (prentAcc.getBankAcc().contains(parentAccName)) {
						treeList.add(prentAcc);
						//获取第四级数据，子账号
						List<BisAccGatherPolicyTreeVO> childAccList = this.getChildAccList(second.getId(), prentAcc.getId(), prentAcc.getTreeId(), second.getCurId());
						treeList.addAll(childAccList);
					}
				}
			}
		}
		List<String> curIds = treeList.stream().map(BisAccGatherPolicyTreeVO::getCurId).collect(Collectors.toList());
		Map<String, String> curDbs = currencyService.getCurrencyNameByIds(curIds);
		treeList.stream().forEach(vo -> {
			vo.setCurName(curDbs.get(vo.getCurId()));
		});
		return treeList;
	}
	/**
	 * 封装顶级数据：调拨类型
	 *
	 * @param gatherType 调拨类型
	 * @return
	 */
	public List<BisAccGatherPolicyTreeVO> getTopList(String gatherType) {
		List<BisAccGatherPolicyTreeVO> topList = new ArrayList<BisAccGatherPolicyTreeVO>();
		//定时归集
		BisAccGatherPolicyTreeVO vo1 = new BisAccGatherPolicyTreeVO();
		vo1.setId(AllocationConstant.GATHERTYPE_0);
		vo1.setFatherId("");
		vo1.setTreeId(AllocationConstant.GATHERTYPE_0);
		vo1.setTreePrentId("");
		//实时归集
		BisAccGatherPolicyTreeVO vo2 = new BisAccGatherPolicyTreeVO();
		vo2.setId(AllocationConstant.GATHERTYPE_1);
		vo2.setFatherId("");
		vo2.setTreeId(AllocationConstant.GATHERTYPE_1);
		vo2.setTreePrentId("");
		//定时下拨
		BisAccGatherPolicyTreeVO vo3 = new BisAccGatherPolicyTreeVO();
		vo3.setId(AllocationConstant.GATHERTYPE_2);
		vo3.setFatherId("");
		vo3.setTreeId(AllocationConstant.GATHERTYPE_2);
		vo3.setTreePrentId("");
		//根据查询条件调拨类型过滤数据
		if (StringUtils.isEmpty(gatherType)) {
			topList.add(vo1);
			topList.add(vo2);
			topList.add(vo3);
		} else {
			if (AllocationConstant.GATHERTYPE_0.equals(gatherType)) {
				topList.add(vo1);
			} else if (AllocationConstant.GATHERTYPE_1.equals(gatherType)) {
				topList.add(vo2);
			} else {
				topList.add(vo3);
			}
		}
		return topList;
	}
	/**
	 * 封装二级数据：调拨策略
	 *
	 * @param gatherType       调拨类型
	 * @param gatherPolicyCode 调拨策略代码
	 * @return
	 */
	public List<BisAccGatherPolicyTreeVO> getSecondList(String gatherType, String gatherPolicyCode, String treePrentId) {
		List<BisAccGatherPolicyTreeVO> secondList = new ArrayList<BisAccGatherPolicyTreeVO>();
		//获取调拨策略
		List<BisGatherPolicy> list = bisGatherPolicyService.listByGatherType(gatherType);
		for (BisGatherPolicy b : list) {
			boolean flag = (!StringUtils.isEmpty(gatherPolicyCode) && b.getGatherPolicyCode().indexOf(gatherPolicyCode) != -1);
			if (StringUtils.isEmpty(gatherPolicyCode) || flag) {
				BisAccGatherPolicyTreeVO vo = new BisAccGatherPolicyTreeVO();
				vo.setId(b.getId());
				vo.setFatherId(gatherType);
				vo.setGatherPolicyCode(b.getGatherPolicyCode());
				vo.setGatherPolicyName(b.getGatherPolicyName());
				vo.setBankAcc("");
				vo.setTreeId(treePrentId + "_" + b.getId());
				vo.setTreePrentId(treePrentId);
				vo.setCurId(b.getCurId());
				secondList.add(vo);
			}
		}
		return secondList;
	}
	/**
	 * 获取父账号列表
	 *
	 * @param gatherPolicyId
	 * @return
	 * @throws Exception
	 */
	public List<BisAccGatherPolicyTreeVO> getPrentAccList(String gatherPolicyId, String treePrentId, String curId) throws Exception {
		List<BisAccGatherPolicyTreeVO> prentAccList = new ArrayList<BisAccGatherPolicyTreeVO>();
		List<BisAccGatherPolicyVO> bankAccList = bisAccGatherPolicyDao.listByPrentBankAcc(gatherPolicyId);
		if (bankAccList != null && bankAccList.size() > 0) {
			for (BisAccGatherPolicyVO bankAcc : bankAccList) {
				injectVale(bankAcc);
				BisAccGatherPolicyTreeVO vo = new BisAccGatherPolicyTreeVO();
				vo.setId(bankAcc.getParentAccId());
				vo.setFatherId(gatherPolicyId);
				vo.setGatherPolicyCode("");
				vo.setGatherPolicyName("");
				vo.setBankAcc(bankAcc.getParentBankAcc());
				vo.setAccName(bankAcc.getParentAccName());
				vo.setBankName(bankAcc.getParentBankName());
				vo.setGatherPolicyId(gatherPolicyId);
				vo.setPrentAccId("");
				vo.setTreeId(treePrentId + "_" + bankAcc.getParentAccId());
				vo.setTreePrentId(treePrentId);
				vo.setCurId(curId);
				prentAccList.add(vo);
			}
		}
		return prentAccList;
	}
	/**
	 * 获取子账号列表
	 *
	 * @param gatherPolicyId
	 * @param prentAccId
	 * @return
	 * @throws Exception
	 */
	public List<BisAccGatherPolicyTreeVO> getChildAccList(String gatherPolicyId, String prentAccId, String treePrentId, String curId) throws Exception {
		List<BisAccGatherPolicyTreeVO> childAccList = new ArrayList<BisAccGatherPolicyTreeVO>();
		//通过币种ID查出银行账号ID集合，进行数据筛选
		List<String> bankAccIds = iBtBankAccCurService.getBankAccIdsByCurId(curId);
		StringBuilder InSql = new StringBuilder();
		InSql.append(SqlUtil.getInSql(bankAccIds, "g.bank_acc_id", true));
		InSql.append(" and ");
		InSql.append(SqlUtil.getInSql(bankAccIds, "g.parent_acc_id", true));
		List<BisAccGatherPolicyVO> list = bisAccGatherPolicyDao.listByBankAcc(gatherPolicyId, null, prentAccId, InSql.toString());
		if (list != null && list.size() > 0) {
			for (BisAccGatherPolicyVO policy : list) {
				injectVale(policy);
				BisAccGatherPolicyTreeVO vo = new BisAccGatherPolicyTreeVO();
				vo.setId(policy.getBankAccId());
				vo.setFatherId(policy.getParentAccId());
				vo.setGatherPolicyCode("");
				vo.setGatherPolicyName("");
				vo.setBankAcc(policy.getBankAcc());
				vo.setAccName(policy.getAccName());
				vo.setBankName(policy.getBankName());
				vo.setGatherPolicyId(gatherPolicyId);
				vo.setPrentAccId(prentAccId);
				vo.setTreeId(treePrentId + "_" + policy.getBankAccId());
				vo.setTreePrentId(treePrentId);
				vo.setCurId(curId);
				childAccList.add(vo);
			}
		}
		return childAccList;
	}
	/**
	 * 调拨策略关系--新增父账号
	 *
	 * @param ids
	 * @param gatherPolicyId
	 * @throws Exception
	 */
	public void savePrentAcc(String ids, String gatherPolicyId, String curId, ICurrentUser user) throws Exception {
		//获取调拨策略信息
		BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(gatherPolicyId);
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(bisGatherPolicy.getDefDate());
		String launchTime = bisGatherPolicy.getLaunchTime();
		cldr.set(Calendar.HOUR_OF_DAY, Integer.parseInt(launchTime.substring(0, 2)));
		cldr.set(Calendar.MINUTE, Integer.parseInt(launchTime.substring(2, 4)));
		cldr.set(Calendar.SECOND, Integer.parseInt(launchTime.substring(4, 6)));
		Date nowDate = cldr.getTime();
		//循环写入账号与调拨策略关系
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			//获取父账号信息
			BtBankAccDTO dto = btBankAccService.getBankAccByIdAndCurId(id, curId);
			//自动归集父账号必须为在线账号
			if(BtBankAccConstant.ISONLINE_0.equals(dto.getIsOnline())){
				continue;
			}
			BtBankAccDTO searchDTO = new BtBankAccDTO();
			searchDTO.setPrentAccId(id);
			searchDTO.setCurId(curId);
			//获取子账号
			List<BtBankAccDTO> listBankAcc = btBankAccService.getBankAccListAll(searchDTO);
			//获取父账号信息
			dto = btBankAccService.getBankAccByIdAndCurId(id, curId);
			if (listBankAcc != null && listBankAcc.size() > 0) {
				for (BtBankAccDTO bankAcc : listBankAcc) {
                    //调拨类型：0-定时归集，1-实时归集，2-定时下拨
                    //定时下拨子账号允许为离线账号
                    if(!AllocationConstant.GATHERTYPE_2.equals(bisGatherPolicy.getGatherType())) {
                        //自动归集子账号必须为在线账号
                        if (BtBankAccConstant.ISONLINE_0.equals(bankAcc.getIsOnline())) {
                            continue;
                        }
                    }
					BisAccGatherPolicy entity = new BisAccGatherPolicy();
					entity.setBankAccId(bankAcc.getId());
					entity.setGatherPolicyId(gatherPolicyId);
					if (!AllocationConstant.GATHERTYPE_2.equals(bisGatherPolicy.getGatherType())) {
						//获取归集子账号的接口代码
						entity.setBifCode(bankAcc.getBifCode());
					} else {
						//下拨则取父账号的接口代码
						entity.setBifCode(dto.getBifCode());
					}
					entity.setParentAccId(bankAcc.getPrentAccId());
					entity.setBankSourcecode(bankAcc.getBankCode());
					entity.setFirstTime(nowDate);
					/*if (bisGatherPolicy.getFrqMode() != null) {
						Date nextTime = getNextTime(bisGatherPolicy.getFrqMode(), bisGatherPolicy.getSerialDay());
						// 下次执行时间 根据归集策略计算
						entity.setNextTime(nextTime);
					}*/
					entity.setCreateBy(user.getLoginName());
					entity.setCreateDate(DateUtils.getDate());
					this.saveOrUpdate(entity);
				}
			}
		}
	}

	/**
	 * 调拨策略关系--新增父账号
	 *
	 * @param ids
	 * @param gatherPolicyId
	 * @throws Exception
	 */
	public void savePrentAcc(String ids, String gatherPolicyId, String curId,String childIds, ICurrentUser user) throws Exception {
		//获取调拨策略信息
		BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(gatherPolicyId);
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(bisGatherPolicy.getDefDate());
		String launchTime = bisGatherPolicy.getLaunchTime();
		cldr.set(Calendar.HOUR_OF_DAY, Integer.parseInt(launchTime.substring(0, 2)));
		cldr.set(Calendar.MINUTE, Integer.parseInt(launchTime.substring(2, 4)));
		cldr.set(Calendar.SECOND, Integer.parseInt(launchTime.substring(4, 6)));
		Date nowDate = cldr.getTime();
		//循环写入账号与调拨策略关系
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			//获取父账号信息
			BtBankAccDTO dto = btBankAccService.getBankAccByIdAndCurId(id, curId);
			//自动归集父账号必须为在线账号
			if(BtBankAccConstant.ISONLINE_0.equals(dto.getIsOnline())){
				continue;
			}
			if (EmptyUtils.isNotEmpty(childIds)) {
				for (String childId : childIds.split(CommonConstant.COMMA_SPLIT)) {
					//获取子单位
					BtBankAccDTO bankAcc = btBankAccService.getBankAccByIdAndCurId(childId, curId);
					//调拨类型：0-定时归集，1-实时归集，2-定时下拨
					//定时下拨子账号允许为离线账号
					if(!AllocationConstant.GATHERTYPE_2.equals(bisGatherPolicy.getGatherType())) {
						//自动归集子账号必须为在线账号
						if (BtBankAccConstant.ISONLINE_0.equals(bankAcc.getIsOnline())) {
							continue;
						}
					}
					BisAccGatherPolicy entity = new BisAccGatherPolicy();
					entity.setBankAccId(bankAcc.getId());
					entity.setGatherPolicyId(gatherPolicyId);
					if (!AllocationConstant.GATHERTYPE_2.equals(bisGatherPolicy.getGatherType())) {
						//获取归集子账号的接口代码
						entity.setBifCode(bankAcc.getBifCode());
					} else {
						//下拨则取父账号的接口代码
						entity.setBifCode(dto.getBifCode());
					}
					entity.setParentAccId(bankAcc.getPrentAccId());
					entity.setBankSourcecode(bankAcc.getBankCode());
					entity.setFirstTime(nowDate);
					/*if (bisGatherPolicy.getFrqMode() != null) {
						Date nextTime = getNextTime(bisGatherPolicy.getFrqMode(), bisGatherPolicy.getSerialDay());
						// 下次执行时间 根据归集策略计算
						entity.setNextTime(nextTime);
					}*/
					entity.setCreateBy(user.getLoginName());
					entity.setCreateDate(DateUtils.getDate());
					this.saveOrUpdate(entity);
				}
			}
		}
	}
	/**
	 * 获取下次执行时间
	 * @param frqMode 调拨周期 4-按日，8-按周，16-按月
	 * @param serialDay 调拨日 间隔时长以天为单位周策略：选择周一至周日月策略：选择1号到31号
	 * @return Date
	 */
	private Date getNextTime(Integer frqMode, Integer serialDay) {
		Date date = null;
		if (frqMode == null) {
			return date;
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		switch (frqMode){
			case 4:
				// 按日，间隔serialDay天
				calendar.setTime(new Date());
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + serialDay);
				date = calendar.getTime();
				break;
			case 8:
				calendar.setTime(new Date());
				if (calendar.get(Calendar.DAY_OF_WEEK) - 1 < serialDay){
					// 计算本周的日期
					String[] split = DateUtils.getTimeInterval(new Date()).split(",");
					try {
						Date beginDate = simpleDateFormat.parse(split[0]);
						Date endDate = simpleDateFormat.parse(split[1]);
						date = DateUtils.findDates(beginDate,endDate).get(serialDay-1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else {
					// 获取下周的日期
					String[] split = DateUtils.getTimeInterval(DateUtils.getDateAfter(7)).split(",");
					try {
						Date beginDate = simpleDateFormat.parse(split[0]);
						Date endDate = simpleDateFormat.parse(split[1]);
						date = DateUtils.findDates(beginDate,endDate).get(serialDay-1);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				break;
			case 16:
				calendar.setTime(new Date());
				// 本月总天数
				int thisMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (thisMonthDays <= serialDay){
					date = DateUtils.getLastMonthDate(new Date());
				}else {
					int nowDate = DateUtils.getDay(new Date());
					// 当前日期 小于 设置的日期  下次执行时间即设置的日期
					if (nowDate < serialDay){
						String dateString = DateUtils.getDataString(new SimpleDateFormat("yyyy-MM-dd"));
						String resultDate = new StringBuilder(dateString.substring(0,dateString.length()-2)).append(serialDay).toString();
						System.out.println(DateUtils.parseDate(resultDate));
						date = DateUtils.parseDate(resultDate);
					}else {
						calendar.setTime(DateUtils.getNextMonthStartTime());
						int nextMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						if (nextMonthDays<=serialDay){
							date = DateUtils.getNextMonthEndTime();
						}else {
							calendar.setTime(DateUtils.getNextMonthStartTime());
							calendar.add(Calendar.DATE,serialDay-1);
							date = calendar.getTime();
						}
					}
				}
				break;
			default:
				break;
		}
		return date;
	}
	/**
	 * 调拨策略关系--新增子账号
	 *
	 * @param ids
	 * @param gatherPolicyId
	 * @param prentAccId
	 * @throws Exception
	 */
	public void saveChildAcc(String ids, String gatherPolicyId, String prentAccId, String curId, ICurrentUser user) throws Exception {
		//获取调拨策略信息
		BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(gatherPolicyId);
		Calendar cldr = Calendar.getInstance();
		cldr.setTime(bisGatherPolicy.getDefDate());
		String launchTime = bisGatherPolicy.getLaunchTime();
		cldr.set(Calendar.HOUR_OF_DAY, Integer.parseInt(launchTime.substring(0, 2)));
		cldr.set(Calendar.MINUTE, Integer.parseInt(launchTime.substring(2, 4)));
		cldr.set(Calendar.SECOND, Integer.parseInt(launchTime.substring(4, 6)));
		Date nowDate = cldr.getTime();
		//获取父账号信息
		BtBankAccDTO prentAcc = btBankAccService.getBankAccByIdAndCurId(prentAccId, curId);
		//循环写入账号与调拨策略关系
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			//获取子账号信息
			BtBankAccDTO bankAcc = btBankAccService.getBankAccByIdAndCurId(id, curId);
			BisAccGatherPolicy entity = new BisAccGatherPolicy();
			entity.setBankAccId(bankAcc.getId());
			entity.setGatherPolicyId(gatherPolicyId);
			if (!AllocationConstant.GATHERTYPE_2.equals(bisGatherPolicy.getGatherType())) {
				//获取归集子账号的接口代码
				entity.setBifCode(bankAcc.getBifCode());
			} else {
				//下拨则取父账号的接口代码
				entity.setBifCode(prentAcc.getBifCode());
			}
			entity.setParentAccId(bankAcc.getPrentAccId());
			entity.setBankSourcecode(bankAcc.getBankCode());
			entity.setFirstTime(nowDate);
			/*Date nextTime = getNextTime(bisGatherPolicy.getFrqMode(), bisGatherPolicy.getSerialDay());
			// 下次执行时间 根据归集策略计算
			entity.setNextTime(nextTime);*/
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
			this.saveOrUpdate(entity);
		}
	}
	/**
	 * 调拨策略删除
	 *
	 * @param bankAccId
	 * @param prentAccId
	 * @param gatherPolicyId
	 */
	public void deleteBisGatherPolicy(String bankAccId, String prentAccId, String gatherPolicyId) {
		bisAccGatherPolicyDao.deleteBisGatherPolicy(bankAccId, prentAccId, gatherPolicyId);
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtBankAccVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		//调拨策略
		String gatherPolicyId = (String) dataTablesInput.getFormdata().get("gatherPolicyId");
		//父账号
		String prentAccId = (String) dataTablesInput.getFormdata().get("eqs_prentAccId");
		//查父账号列表
		if (StringUtils.isEmpty(prentAccId)) {
			//父账号必须是在线账号
			sql.append(" and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
			List<String> parentAccIds = bisAccGatherPolicyDao.getParentAccIdsByGatherPolicyId(gatherPolicyId);
			if(!EmptyUtils.isEmpty(parentAccIds)){
				sql.append(" and ");
				sql.append(SqlUtil.getNotinSql(parentAccIds, "acc.id", true));
			}
//			sql.append(" and acc.id not in(select parent_acc_id from bis_acc_gather_policy where gather_policy_id='" + gatherPolicyId + "' and parent_acc_id is not null)");
			//过滤出必须是父账号
			sql.append(" and acc.parent_sign='1'");
		} else {
			sql.append(" and acccur.prent_acc_id = '" + prentAccId + "'");
			List<String> bankAccIds = bisAccGatherPolicyDao.getBankAccIdsByGatherPolicyIdAndParentAccId(gatherPolicyId, prentAccId);
			if(!EmptyUtils.isEmpty(bankAccIds)){
				sql.append(" and ");
				sql.append(SqlUtil.getNotinSql(bankAccIds, "acc.id", true));
			}
//			sql.append(" and acc.id not in(select bank_acc_id from bis_acc_gather_policy where gather_policy_id='" + gatherPolicyId + "' and parent_acc_id='" + prentAccId + "')");
			//调拨类型：0-定时归集，1-实时归集，2-定时下拨
			//定时下拨子账号允许为离线账号
			String gatherType = (String) dataTablesInput.getFormdata().get("gatherType");
			if(!AllocationConstant.GATHERTYPE_2.equals(gatherType)){
				sql.append(" and acc.is_online = " + BtBankAccConstant.ISONLINE_1);
			}
		}
		//银行账号
		String bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
		if (!StringUtils.isEmpty(bankAcc)) {
			sql.append(" and acc.bank_acc like '%" + bankAcc + "%'");
		}
		//银行类别
		String bankTypeId = (String) dataTablesInput.getFormdata().get("eqs_bankTypeId");
		if (!StringUtils.isEmpty(bankTypeId)) {
			sql.append(" and acc.bank_type_id = '" + bankTypeId + "'");
		}
		//账户属性
		String attributeId = (String) dataTablesInput.getFormdata().get("eqs_attributeId");
		if (!StringUtils.isEmpty(attributeId)) {
			sql.append(" and acc.attribute_id = '" + attributeId + "'");
		}
		//System.out.println(sql.toString());

		List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY,dataTablesInput.getCurrentUserId());
		if (CollectionUtil.isNotEmpty(corpAuthList)){
			String corpAuthSql= SqlUtil.getInSql(corpAuthList, "acc.corp_id", true);
			sql.append(" and ").append(corpAuthSql);
		}

		DataTablesOutput<BtBankAccVO> dataTablesUnion2 = this.btBankAccService.getDataTablesUnion2(dataTablesInput, sql.toString());
		if (ObjectUtil.isNotEmpty(dataTablesUnion2)){
			List<BtBankAccVO> dbData = dataTablesUnion2.getData();
			List<String> curIds = dbData.stream().map(BtBankAccVO::getCurId).collect(Collectors.toList());
			List<String> corpIds = dbData.stream().map(BtBankAccVO::getCorpId).collect(Collectors.toList());
			Map<String, String> curDbs = currencyService.getCurrencyNameByIds(curIds);
			Map<String, String> corpDbs = corpService.getNameByIds(corpIds);
			dbData.stream().forEach(vo -> {
				vo.setCurName(curDbs.get(vo.getCurId()));
				vo.setCorpName(corpDbs.get(vo.getCorpId()));
			});


		}
		return dataTablesUnion2;
	}

	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtBankAccVO> getDataTablesUnionChild(DataTablesInput dataTablesInput) {
		DataTablesOutput<BtBankAccVO> dataTablesUnion2 = this.btBankAccService.getDataTablesUnion2(dataTablesInput, null);
		return dataTablesUnion2;
	}
	/**
	 * 调拨策略删除
	 *
	 * @param bankAccId
	 * @param prentAccId
	 */
	public void deleteBisGatherPolicyByCurId(String bankAccId, String prentAccId, String curId) {
		bisAccGatherPolicyDao.deleteBisGatherPolicyByCurId(bankAccId, prentAccId, curId);
	}
	/**
	 * 根据策略ID更新下一次执行日期
	 * @param policyId
	 */
	public void updateNextTimeByPolicyId(String policyId) {
		bisAccGatherPolicyDao.updateNextTimeByPolicyId(policyId);
	}
	public void injectVale(BisAccGatherPolicyVO bisAccGatherPolicyVO){
		BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(bisAccGatherPolicyVO.getParentAccId());
		if(!EmptyUtils.isEmpty(btBankAccDTO)){
			bisAccGatherPolicyVO.setParentBankAcc(btBankAccDTO.getBankAcc());
			bisAccGatherPolicyVO.setParentAccName(btBankAccDTO.getAccName());
			bisAccGatherPolicyVO.setParentBankName(btBankAccDTO.getBankName());
		}
		btBankAccDTO = btBankAccService.getBankAccById(bisAccGatherPolicyVO.getBankAccId());
		if(!EmptyUtils.isEmpty(btBankAccDTO)){
			bisAccGatherPolicyVO.setBankAcc(btBankAccDTO.getBankAcc());
			bisAccGatherPolicyVO.setAccName(btBankAccDTO.getAccName());
			bisAccGatherPolicyVO.setBankName(btBankAccDTO.getBankName());
		}
	}
}
