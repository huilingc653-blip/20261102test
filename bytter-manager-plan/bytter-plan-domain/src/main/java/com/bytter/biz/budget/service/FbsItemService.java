package com.bytter.biz.budget.service;
import com.bytter.biz.budget.dao.FbsItemDao;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.vo.SimpleFbsItemVo;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SortList;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.I18nDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 预算科目
 * @date 2017-07-06 16:40:26
 */
@Service("fbsItemService")
@Transactional
public class FbsItemService extends BaseService<FbsItem, String> {
	@Autowired
	private FbsItemDao fbsItemDao;
	@Autowired
	private FbsCorpItemService fbsCorpItemService;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsItem entity) {
		List<String> allList = new ArrayList<String>();
		List<String> itemCodes = new ArrayList<String>();
		itemCodes.add(entity.getItemCode());
		//通过当前科目代码，查找是否有子节点
		List<FbsItemVO> list = fbsItemDao.listFbsItemBySuperItemCodes(itemCodes);
		//通过科目代码，递归查找所有子节点存储于allList
		getItemCodes(list, allList);
		//allList中添加当前节点代码
		allList.add(entity.getItemCode());
		//根据所有节点code判断是否存在使用记录
		Long count = fbsCorpItemService.getCountByCodes(allList);
		if (count > 0) {
			throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_EXISTS, entity.getItemCode());
		} else {
			fbsItemDao.delFbsItem(allList);
		}
		//删除多语言
		ii18nService.deleteBusinessBaseContext(entity.getId());
	}
	/**
	 * 通过科目代码，递归查找所有子节点
	 *
	 * @param list
	 * @param allList 存储所有子节点
	 */
	private void getItemCodes(List<FbsItemVO> list, List<String> allList) {
		List<String> itemCodes = new ArrayList<String>();
		if (EmptyUtils.isNotEmpty(list) && list.size() > 0) {
			for (FbsItemVO vo : list) {
				itemCodes.add(vo.getItemCode());
				allList.add(vo.getItemCode());
			}
		}
		if (itemCodes.size() > 0) {
			list = fbsItemDao.listFbsItemBySuperItemCodes(itemCodes);
			getItemCodes(list, allList);
		}
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
	public void save(FbsItem entity, ICurrentUser user, String locale) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isEmpty(entity.getId())) {
			String superItemCode = entity.getSuperItemCode();
			if (StringUtils.isNotEmpty(superItemCode)) {
				FbsItemVO itemVO = fbsItemDao.getFbsItemVO("", superItemCode);
				if (StringUtils.isNotEmpty(itemVO.getSuperItemCodes())) {
					entity.setSuperItemCodes(sb.append(itemVO.getSuperItemCodes()).append(",").append(superItemCode).toString());
				} else {
					entity.setSuperItemCodes(superItemCode);
				}
				String seqCode = fbsItemDao.getSequence(entity.getSuperItemCode(), 3);
				if(EmptyUtils.isBlank(seqCode)){
					seqCode = entity.getSuperItemCode() + "001";
				}
				entity.setSeqCode(entity.getSuperItemCode());
				entity.setItemCode(seqCode);
			} else {
				String seqCode = fbsItemDao.getSequence("9", 2);
				if(EmptyUtils.isBlank(seqCode)){
					seqCode = "900";
				}
				entity.setSeqCode("9");
				entity.setItemCode(seqCode);
			}
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
		}
		super.saveEntity(entity);
		//新增当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getItemName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsItem entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(FbsItem entity, ICurrentUser user, String locale) {
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		super.saveOrUpdate(entity);
		//更新当前语种
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(entity.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(entity.getItemName());
		ii18nService.saveOrUpdateBusinessBaseContext(i18nDTO);
	}
	@Override
	public BaseDao<FbsItem, String> getEntityDao() {
		return fbsItemDao;
	}
	public FbsItemVO getFbsItemVO(String id, String itemCode) {
		return fbsItemDao.getFbsItemVO(id, itemCode);
	}
	public List<com.bytter.biz.budget.spi.vo.FbsItemVO> getFbsItemVOS(Set<String> itemCodes) {
		if(EmptyUtils.isEmpty(itemCodes)){
			return Lists.newArrayListWithCapacity(0);
		}
		List<FbsItemVO> list = fbsItemDao.getFbsItemVOS(itemCodes);
		List<com.bytter.biz.budget.spi.vo.FbsItemVO> listVO = BeanUtils.copyListProperties(list,com.bytter.biz.budget.spi.vo.FbsItemVO.class);
		return listVO;
	}
	/**
	 * 组装字典相关
	 *
	 * @return
	 */
	public List<FbsItemVO> listFbsItemUnionDict(List<FbsItemVO> list) {
		Map<String, DictDTO> signMap = dictService.listMap(BudgetConstant.ITEM_SIGN_DICT);
		Map<String, DictDTO> sfYnMap = dictService.listMap(CommonConstant.COMMON_SF_YN_DICT);
		Map<String, DictDTO> validSignMap = dictService.listMap(CommonConstant.COMMON_VALID_SIGN_DICT);
		for (FbsItemVO vo:list) {
			//资金流向1， 资金流入-1 ，资金流出
			vo.setSignName(signMap.get(vo.getSign().toString()).getName());
			//是否冲抵科目  1-是 0-否
			vo.setIsInternalName(sfYnMap.get(vo.getIsInternal()).getName());
			//是否计划控制科目 1-是 0-否
			vo.setIsControlName(sfYnMap.get(vo.getIsControl()).getName());
			//有效标志 0 无效 1 有效
			vo.setValidSignName(validSignMap.get(vo.getValidSign()).getName());
			//是否刚性科目 1-是 0-否
			vo.setIsRigidName(sfYnMap.get(vo.getIsRigid()).getName());
			//是否期初科目 1-是 0-否
			vo.setIsBeginPeriodName(sfYnMap.get(vo.getIsBeginPeriod()).getName());
			//是否期末科目 1-是 0-否
			vo.setIsEndPeriodName(sfYnMap.get(vo.getIsEndPeriod()).getName());
			//是否允许填写计划 1-是 0-否
			vo.setIsWriteName(sfYnMap.get(vo.getIsWrite()).getName());
			//科目明细是否必须录入 1-是 0-否
			vo.setIsWriteDetailName(sfYnMap.get(vo.getIsWriteDetail()).getName());
		}
		return list;
	}
	/**
	 * 获取树形表格
	 *
	 * @return
	 * @throws Exception
	 */
	public List<FbsItemVO> listTreeData(FbsItemVO fbsItem, String validSign) {
		List<FbsItemVO> list = fbsItemDao.listFbsItem(fbsItem, validSign);
		if (list != null && list.size() > 0 && fbsItem != null) {
			List<String> codes = getListNoRepeat(list);
			if (codes != null && codes.size() > 0) {
				list = fbsItemDao.listFbsItemByCodes(codes, fbsItem);
			}
		}
		list = this.listFbsItemUnionDict(list);
		return getListSort(list, fbsItem);
	}
	/**
	 * 重复code过滤
	 *
	 * @param list
	 * @return
	 */
	private List<String> getListNoRepeat(List<FbsItemVO> list) {
		List<String> codes = new ArrayList<String>();
		for (FbsItemVO vo : list) {
			if (!codes.contains(vo.getItemCode())) {
				codes.add(vo.getItemCode());
			}
			if (StringUtils.isNotEmpty(vo.getSuperItemCodes())) {
				String[] superCodes = vo.getSuperItemCodes().split(",");
				for (String code : superCodes) {
					if (!codes.contains(code)) {
						codes.add(code);
					}
				}
			}
		}
		return codes;
	}
	/**
	 * 根据层级结构排序，组装 tree 结构
	 *
	 * @param list
	 * @return
	 */
	public List<FbsItemVO> getListSort(List<FbsItemVO> list, FbsItemVO fbsItem) {
		if (list == null || list.size() <= 0) {
			return list;
		}
		List<FbsItemVO> root = new ArrayList<FbsItemVO>();
		List<FbsItemVO> subList = new ArrayList<FbsItemVO>();
		for (FbsItemVO fbs : list) {
			if (StringUtils.isEmpty(fbs.getSuperItemCode())) {
				root.add(fbs);
			} else {
				subList.add(fbs);
			}
		}
		List<FbsItemVO> allList = new ArrayList<FbsItemVO>();
		if(EmptyUtils.isEmpty(fbsItem) || EmptyUtils.isEmpty(fbsItem.getShowItem())) {
			for (FbsItemVO fbs : root) {
				getListSort(fbs, subList, allList);
			}
		}else{
			if(fbsItem.getShowItem() > 1 || fbsItem.getShowItem() == -1){
				for (FbsItemVO fbs : subList) {
					fbs.setSuperItemCode("");
					getListSort(fbs, root, allList);
				}
			}else{
				for (FbsItemVO fbs : root) {
					getListSort(fbs, subList, allList);
				}
			}
		}
		SortList<FbsItemVO> sortList = new SortList<FbsItemVO>();
		sortList.Sort(allList, "getItemCode", "getSuperItemCode", null);
		return allList;
	}
	public void getListSort(FbsItemVO fbsItemVO, List<FbsItemVO> subList, List<FbsItemVO> allList) {
		allList.add(fbsItemVO);
		for (FbsItemVO sub : subList) {
			if (fbsItemVO.getItemCode().equals(sub.getSuperItemCode())) {
				getListSort(sub, subList, allList);
			}
		}
	}
	/**
	 * 根据科目代码查找科目信息
	 *
	 * @param itemCode
	 * @return
	 */
	public FbsItem getFbsItemByCode(String itemCode) {
		return fbsItemDao.getFbsItemByCode(itemCode);
	}
	/**
	 * 查询子节点是否存在 有效的 科目存在
	 *
	 * @param itemCode
	 * @return
	 */
	public boolean isEdit(String itemCode) {
		BigDecimal count = fbsItemDao.isEdit(itemCode);
		if (count.compareTo(BigDecimal.ZERO) > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 根据code获取实体
	 * @param itemCode
	 * @return
	 */
	public FbsItem getFbsItem(String itemCode) {
		return this.fbsItemDao.getFbsItem(itemCode);
	}
	/**
	 * 获取所有科目
	 * @return
	 */
	public List<FbsItem> getAllFbsItem() {
		return this.fbsItemDao.getAllFbsItem();
	}
	/**
	 * 获取科目最高层级
	 * @return
	 */
	public Integer getItemMaxLevel(){
		return fbsItemDao.getItemMaxLevel();
	}
	/**
	 * 获取计划执行科目
	 * @return
	 */
	public List<FbsItemVO> listFbsItemForProcess(Integer sign) {
		return fbsItemDao.listFbsItemForProcess(sign);
	}
	/**
	 * 获取计划执行科目
	 * @return
	 */
	public String getFbsItemForProcess(Integer sign) {
		List<FbsItemVO> itemList = fbsItemDao.listFbsItemForProcess(sign);
		String itemCode = "";
		for(FbsItemVO item: itemList){
			itemCode = itemCode + item.getItemCode() + CommonConstant.SEMI_SPLIT;
		}
		return itemCode;
	}
	/**
	 * 获取科目集合
	 * @return
	 */
	public HashMap<String, FbsItemVO> getAllItemMap(){
		HashMap<String, FbsItemVO> itemMap = new HashMap<>();
		List<FbsItemVO> list = fbsItemDao.listFbsItem(null, null);
		for(FbsItemVO item:list){
			itemMap.put(item.getItemCode(), item);
		}
		return itemMap;
	}
	/**
	 * 获取科目集合
	 * @return
	 */
	public HashMap<String, FbsItemVO> getAllItemMapV2(){
		HashMap<String, FbsItemVO> itemMap = new HashMap<>();
		List<FbsItemVO> list = fbsItemDao.listFbsItemV2(null, null);
		for(FbsItemVO item:list){
			itemMap.put(item.getItemCode(), item);
		}
		return itemMap;
	}

	/**
	 * 获取树形表格
	 *
	 * @return
	 * @throws Exception
	 */
	public List<FbsItemVO> listTreeData1(FbsItemVO fbsItem, String itemCode, String validSign) {
		List<FbsItemVO> list = fbsItemDao.listFbsItem(fbsItem, validSign);
		if (list != null && list.size() > 0 && fbsItem != null) {
			List<String> codes = getListNoRepeat(list);
			if (codes != null && codes.size() > 0) {
				list = fbsItemDao.listFbsItemByCodes(codes, fbsItem);
			}
		}
		List<FbsItemVO> itemVOList = getListSort(list, fbsItem);
		String[] split = itemCode.split(";");
		if (!EmptyUtils.isEmpty(itemVOList) && !EmptyUtils.isEmpty(split)) {
			for (FbsItemVO itemVO : itemVOList) {
				for (String s : split) {
					if (s.equals(itemVO.getItemCode())) {
						itemVO.setIsChecked("1");
					}
				}
			}
		}
		return itemVOList;
	}

    public List<SimpleFbsItemVo> getSimpleFbsItemVo(List<String> itemCodes) {
        return fbsItemDao.getSimpleFbsItemVo(itemCodes);
    }

    }
